package org.mapsforge.map.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.jvm.internal.ByteCompanionObject;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Tag;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MapReadResult;
import org.mapsforge.map.datastore.PoiWayBundle;
import org.mapsforge.map.datastore.PointOfInterest;
import org.mapsforge.map.datastore.Way;
import org.mapsforge.map.reader.header.MapFileException;
import org.mapsforge.map.reader.header.MapFileHeader;
import org.mapsforge.map.reader.header.MapFileInfo;
import org.mapsforge.map.reader.header.SubFileParameter;

public class MapFile extends MapDataStore {
    private static final long BITMASK_INDEX_OFFSET = 549755813887L;
    private static final long BITMASK_INDEX_WATER = 549755813888L;
    private static final byte DEFAULT_START_ZOOM_LEVEL = 12;
    private static final int INDEX_CACHE_SIZE = 64;
    private static final String INVALID_FIRST_WAY_OFFSET = "invalid first way offset: ";
    private static final Logger LOGGER = Logger.getLogger(MapFile.class.getName());
    private static final int POI_FEATURE_ELEVATION = 32;
    private static final int POI_FEATURE_HOUSE_NUMBER = 64;
    private static final int POI_FEATURE_NAME = 128;
    private static final int POI_LAYER_BITMASK = 240;
    private static final int POI_LAYER_SHIFT = 4;
    private static final int POI_NUMBER_OF_TAGS_BITMASK = 15;
    private static final byte SIGNATURE_LENGTH_BLOCK = 32;
    private static final byte SIGNATURE_LENGTH_POI = 32;
    private static final byte SIGNATURE_LENGTH_WAY = 32;
    private static final String TAG_KEY_ELE = "ele";
    private static final String TAG_KEY_HOUSE_NUMBER = "addr:housenumber";
    private static final String TAG_KEY_NAME = "name";
    private static final String TAG_KEY_REF = "ref";
    public static final MapFile TEST_MAP_FILE = new MapFile();
    private static final int WAY_FEATURE_DATA_BLOCKS_BYTE = 8;
    private static final int WAY_FEATURE_DOUBLE_DELTA_ENCODING = 4;
    private static final int WAY_FEATURE_HOUSE_NUMBER = 64;
    private static final int WAY_FEATURE_LABEL_POSITION = 16;
    private static final int WAY_FEATURE_NAME = 128;
    private static final int WAY_FEATURE_REF = 32;
    private static final int WAY_LAYER_BITMASK = 240;
    private static final int WAY_LAYER_SHIFT = 4;
    private static final int WAY_NUMBER_OF_TAGS_BITMASK = 15;
    public static int wayFilterDistance = 20;
    public static boolean wayFilterEnabled = true;
    private final IndexCache databaseIndexCache;
    private final long fileSize;
    private final FileChannel inputChannel;
    private final MapFileHeader mapFileHeader;
    private final long timestamp;
    private byte zoomLevelMax;
    private byte zoomLevelMin;

    private enum Selector {
        ALL,
        POIS,
        LABELS
    }

    private MapFile() {
        this.zoomLevelMin = 0;
        this.zoomLevelMax = ByteCompanionObject.MAX_VALUE;
        this.databaseIndexCache = null;
        this.fileSize = 0;
        this.inputChannel = null;
        this.mapFileHeader = null;
        this.timestamp = System.currentTimeMillis();
    }

    public MapFile(File file) {
        this(file, (String) null);
    }

    public MapFile(File file, String str) {
        super(str);
        this.zoomLevelMin = 0;
        this.zoomLevelMax = ByteCompanionObject.MAX_VALUE;
        if (file != null) {
            try {
                if (!file.exists()) {
                    throw new MapFileException("file does not exist: " + file);
                } else if (!file.isFile()) {
                    throw new MapFileException("not a file: " + file);
                } else if (file.canRead()) {
                    FileChannel channel = new FileInputStream(file).getChannel();
                    this.inputChannel = channel;
                    long size = channel.size();
                    this.fileSize = size;
                    ReadBuffer readBuffer = new ReadBuffer(channel);
                    MapFileHeader mapFileHeader2 = new MapFileHeader();
                    this.mapFileHeader = mapFileHeader2;
                    mapFileHeader2.readHeader(readBuffer, size);
                    this.databaseIndexCache = new IndexCache(channel, 64);
                    this.timestamp = file.lastModified();
                } else {
                    throw new MapFileException("cannot read file: " + file);
                }
            } catch (Exception e) {
                closeFileChannel();
                throw new MapFileException(e.getMessage());
            }
        } else {
            throw new MapFileException("mapFile must not be null");
        }
    }

    public MapFile(FileChannel fileChannel, long j, String str) {
        super(str);
        this.zoomLevelMin = 0;
        this.zoomLevelMax = ByteCompanionObject.MAX_VALUE;
        if (fileChannel != null) {
            try {
                this.inputChannel = fileChannel;
                long size = fileChannel.size();
                this.fileSize = size;
                ReadBuffer readBuffer = new ReadBuffer(fileChannel);
                MapFileHeader mapFileHeader2 = new MapFileHeader();
                this.mapFileHeader = mapFileHeader2;
                mapFileHeader2.readHeader(readBuffer, size);
                this.databaseIndexCache = new IndexCache(fileChannel, 64);
                this.timestamp = j;
            } catch (Exception e) {
                closeFileChannel();
                throw new MapFileException(e.getMessage());
            }
        } else {
            throw new MapFileException("mapFileChannel must not be null");
        }
    }

    public MapFile(String str) {
        this(str, (String) null);
    }

    public MapFile(String str, String str2) {
        this(new File(str), str2);
    }

    public BoundingBox boundingBox() {
        return getMapFileInfo().boundingBox;
    }

    public void close() {
        closeFileChannel();
    }

    private void closeFileChannel() {
        try {
            IndexCache indexCache = this.databaseIndexCache;
            if (indexCache != null) {
                indexCache.destroy();
            }
            FileChannel fileChannel = this.inputChannel;
            if (fileChannel != null) {
                fileChannel.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void decodeWayNodesDoubleDelta(LatLong[] latLongArr, double d, double d2, ReadBuffer readBuffer) {
        LatLong[] latLongArr2 = latLongArr;
        double microdegreesToDegrees = d + LatLongUtils.microdegreesToDegrees(readBuffer.readSignedInt());
        double microdegreesToDegrees2 = d2 + LatLongUtils.microdegreesToDegrees(readBuffer.readSignedInt());
        latLongArr2[0] = new LatLong(microdegreesToDegrees, microdegreesToDegrees2);
        double d3 = 0.0d;
        double d4 = 0.0d;
        for (int i = 1; i < latLongArr2.length; i++) {
            d3 += LatLongUtils.microdegreesToDegrees(readBuffer.readSignedInt());
            d4 += LatLongUtils.microdegreesToDegrees(readBuffer.readSignedInt());
            microdegreesToDegrees += d3;
            microdegreesToDegrees2 += d4;
            if (microdegreesToDegrees2 < -180.0d && -180.0d - microdegreesToDegrees2 < 0.001d) {
                microdegreesToDegrees2 = -180.0d;
            } else if (microdegreesToDegrees2 > 180.0d && microdegreesToDegrees2 - 180.0d < 0.001d) {
                microdegreesToDegrees2 = 180.0d;
            }
            latLongArr2[i] = new LatLong(microdegreesToDegrees, microdegreesToDegrees2);
        }
    }

    private void decodeWayNodesSingleDelta(LatLong[] latLongArr, double d, double d2, ReadBuffer readBuffer) {
        LatLong[] latLongArr2 = latLongArr;
        double microdegreesToDegrees = d + LatLongUtils.microdegreesToDegrees(readBuffer.readSignedInt());
        double microdegreesToDegrees2 = d2 + LatLongUtils.microdegreesToDegrees(readBuffer.readSignedInt());
        latLongArr2[0] = new LatLong(microdegreesToDegrees, microdegreesToDegrees2);
        for (int i = 1; i < latLongArr2.length; i++) {
            microdegreesToDegrees += LatLongUtils.microdegreesToDegrees(readBuffer.readSignedInt());
            microdegreesToDegrees2 += LatLongUtils.microdegreesToDegrees(readBuffer.readSignedInt());
            if (microdegreesToDegrees2 < -180.0d && -180.0d - microdegreesToDegrees2 < 0.001d) {
                microdegreesToDegrees2 = -180.0d;
            } else if (microdegreesToDegrees2 > 180.0d && microdegreesToDegrees2 - 180.0d < 0.001d) {
                microdegreesToDegrees2 = 180.0d;
            }
            latLongArr2[i] = new LatLong(microdegreesToDegrees, microdegreesToDegrees2);
        }
    }

    public long getDataTimestamp(Tile tile) {
        return this.timestamp;
    }

    public MapFileHeader getMapFileHeader() {
        return this.mapFileHeader;
    }

    public MapFileInfo getMapFileInfo() {
        return this.mapFileHeader.getMapFileInfo();
    }

    public String[] getMapLanguages() {
        String str = getMapFileInfo().languagesPreference;
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        return str.split(",");
    }

    private PoiWayBundle processBlock(QueryParameters queryParameters, SubFileParameter subFileParameter, BoundingBox boundingBox, double d, double d2, Selector selector, ReadBuffer readBuffer) {
        List<PointOfInterest> list;
        List<Way> list2;
        QueryParameters queryParameters2 = queryParameters;
        SubFileParameter subFileParameter2 = subFileParameter;
        ReadBuffer readBuffer2 = readBuffer;
        if (!processBlockSignature(readBuffer2)) {
            return null;
        }
        int[] iArr = readZoomTable(subFileParameter2, readBuffer2)[queryParameters2.queryZoomLevel - subFileParameter2.zoomLevelMin];
        int i = iArr[0];
        int i2 = iArr[1];
        int readUnsignedInt = readBuffer.readUnsignedInt();
        if (readUnsignedInt < 0) {
            Logger logger = LOGGER;
            logger.warning(INVALID_FIRST_WAY_OFFSET + readUnsignedInt);
            return null;
        }
        int bufferPosition = readUnsignedInt + readBuffer.getBufferPosition();
        if (bufferPosition > readBuffer.getBufferSize()) {
            Logger logger2 = LOGGER;
            logger2.warning(INVALID_FIRST_WAY_OFFSET + bufferPosition);
            return null;
        }
        boolean z = queryParameters2.queryZoomLevel > subFileParameter2.baseZoomLevel;
        List<PointOfInterest> processPOIs = processPOIs(d, d2, i, boundingBox, z, readBuffer);
        if (processPOIs == null) {
            return null;
        }
        if (Selector.POIS == selector) {
            list2 = Collections.emptyList();
            list = processPOIs;
        } else if (readBuffer.getBufferPosition() > bufferPosition) {
            Logger logger3 = LOGGER;
            logger3.warning("invalid buffer position: " + readBuffer.getBufferPosition());
            return null;
        } else {
            readBuffer2.setBufferPosition(bufferPosition);
            list = processPOIs;
            list2 = processWays(queryParameters, i2, boundingBox, z, d, d2, selector, readBuffer);
            if (list2 == null) {
                return null;
            }
        }
        return new PoiWayBundle(list, list2);
    }

    private boolean processBlockSignature(ReadBuffer readBuffer) {
        if (!this.mapFileHeader.getMapFileInfo().debugFile) {
            return true;
        }
        String readUTF8EncodedString = readBuffer.readUTF8EncodedString(32);
        if (readUTF8EncodedString.startsWith("###TileStart")) {
            return true;
        }
        Logger logger = LOGGER;
        logger.warning("invalid block signature: " + readUTF8EncodedString);
        return false;
    }

    private MapReadResult processBlocks(QueryParameters queryParameters, SubFileParameter subFileParameter, BoundingBox boundingBox, Selector selector) throws IOException {
        long j;
        QueryParameters queryParameters2 = queryParameters;
        SubFileParameter subFileParameter2 = subFileParameter;
        MapReadResult mapReadResult = new MapReadResult();
        boolean z = true;
        for (long j2 = queryParameters2.fromBlockY; j2 <= queryParameters2.toBlockY; j2++) {
            long j3 = queryParameters2.fromBlockX;
            while (j3 <= queryParameters2.toBlockX) {
                long j4 = (subFileParameter2.blocksWidth * j2) + j3;
                long indexEntry = this.databaseIndexCache.getIndexEntry(subFileParameter2, j4);
                if (z) {
                    z &= (BITMASK_INDEX_WATER & indexEntry) != 0;
                }
                boolean z2 = z;
                long j5 = indexEntry & BITMASK_INDEX_OFFSET;
                if (j5 < 1 || j5 > subFileParameter2.subFileSize) {
                    Logger logger = LOGGER;
                    logger.warning("invalid current block pointer: " + j5);
                    logger.warning("subFileSize: " + subFileParameter2.subFileSize);
                    return null;
                }
                long j6 = j4 + 1;
                if (j6 == subFileParameter2.numberOfBlocks) {
                    j = subFileParameter2.subFileSize;
                } else {
                    j = this.databaseIndexCache.getIndexEntry(subFileParameter2, j6) & BITMASK_INDEX_OFFSET;
                    if (j > subFileParameter2.subFileSize) {
                        Logger logger2 = LOGGER;
                        logger2.warning("invalid next block pointer: " + j);
                        logger2.warning("sub-file size: " + subFileParameter2.subFileSize);
                        return null;
                    }
                }
                int i = (int) (j - j5);
                if (i < 0) {
                    LOGGER.warning("current block size must not be negative: " + i);
                    return null;
                }
                if (i != 0) {
                    if (i > Parameters.MAXIMUM_BUFFER_SIZE) {
                        LOGGER.warning("current block size too large: " + i);
                    } else if (((long) i) + j5 > this.fileSize) {
                        LOGGER.warning("current block largher than file size: " + i);
                        return null;
                    } else {
                        ReadBuffer readBuffer = new ReadBuffer(this.inputChannel);
                        if (!readBuffer.readFromFile(subFileParameter2.startAddress + j5, i)) {
                            LOGGER.warning("reading current block has failed: " + i);
                            return null;
                        }
                        try {
                            PoiWayBundle processBlock = processBlock(queryParameters, subFileParameter, boundingBox, MercatorProjection.tileYToLatitude(subFileParameter2.boundaryTileTop + j2, subFileParameter2.baseZoomLevel), MercatorProjection.tileXToLongitude(subFileParameter2.boundaryTileLeft + j3, subFileParameter2.baseZoomLevel), selector, readBuffer);
                            if (processBlock != null) {
                                mapReadResult.add(processBlock);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            LOGGER.log(Level.SEVERE, e.getMessage(), e);
                        }
                    }
                }
                j3++;
                z = z2;
            }
        }
        return mapReadResult;
    }

    private List<PointOfInterest> processPOIs(double d, double d2, int i, BoundingBox boundingBox, boolean z, ReadBuffer readBuffer) {
        ReadBuffer readBuffer2 = readBuffer;
        ArrayList arrayList = new ArrayList();
        Tag[] tagArr = this.mapFileHeader.getMapFileInfo().poiTags;
        for (int i2 = i; i2 != 0; i2--) {
            if (this.mapFileHeader.getMapFileInfo().debugFile) {
                String readUTF8EncodedString = readBuffer2.readUTF8EncodedString(32);
                if (!readUTF8EncodedString.startsWith("***POIStart")) {
                    LOGGER.warning("invalid POI signature: " + readUTF8EncodedString);
                    return null;
                }
            }
            double microdegreesToDegrees = d + LatLongUtils.microdegreesToDegrees(readBuffer.readSignedInt());
            double microdegreesToDegrees2 = d2 + LatLongUtils.microdegreesToDegrees(readBuffer.readSignedInt());
            byte readByte = readBuffer.readByte();
            byte b = (byte) ((readByte & 240) >>> 4);
            List<Tag> readTags = readBuffer2.readTags(tagArr, (byte) (readByte & 15));
            if (readTags == null) {
                return null;
            }
            byte readByte2 = readBuffer.readByte();
            boolean z2 = true;
            boolean z3 = (readByte2 & ByteCompanionObject.MIN_VALUE) != 0;
            boolean z4 = (readByte2 & 64) != 0;
            if ((readByte2 & 32) == 0) {
                z2 = false;
            }
            if (z3) {
                readTags.add(new Tag("name", extractLocalized(readBuffer.readUTF8EncodedString())));
            }
            if (z4) {
                readTags.add(new Tag(TAG_KEY_HOUSE_NUMBER, readBuffer.readUTF8EncodedString()));
            }
            if (z2) {
                readTags.add(new Tag(TAG_KEY_ELE, Integer.toString(readBuffer.readSignedInt())));
            }
            LatLong latLong = new LatLong(microdegreesToDegrees, microdegreesToDegrees2);
            BoundingBox boundingBox2 = boundingBox;
            if (!z || boundingBox2.contains(latLong)) {
                arrayList.add(new PointOfInterest(b, readTags, latLong));
            }
        }
        return arrayList;
    }

    private LatLong[][] processWayDataBlock(double d, double d2, boolean z, ReadBuffer readBuffer) {
        int readUnsignedInt = readBuffer.readUnsignedInt();
        if (readUnsignedInt < 1 || readUnsignedInt > 32767) {
            Logger logger = LOGGER;
            logger.warning("invalid number of way coordinate blocks: " + readUnsignedInt);
            LatLong[][] latLongArr = null;
            return null;
        }
        LatLong[][] latLongArr2 = new LatLong[readUnsignedInt][];
        for (int i = 0; i < readUnsignedInt; i++) {
            int readUnsignedInt2 = readBuffer.readUnsignedInt();
            if (readUnsignedInt2 < 2 || readUnsignedInt2 > 32767) {
                Logger logger2 = LOGGER;
                logger2.warning("invalid number of way nodes: " + readUnsignedInt2);
                LatLong[][] latLongArr3 = null;
                return null;
            }
            LatLong[] latLongArr4 = new LatLong[readUnsignedInt2];
            if (z) {
                decodeWayNodesDoubleDelta(latLongArr4, d, d2, readBuffer);
            } else {
                decodeWayNodesSingleDelta(latLongArr4, d, d2, readBuffer);
            }
            latLongArr2[i] = latLongArr4;
        }
        return latLongArr2;
    }

    /* JADX WARNING: type inference failed for: r24v0 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0099  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0098 A[SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<org.mapsforge.map.datastore.Way> processWays(org.mapsforge.map.reader.QueryParameters r28, int r29, org.mapsforge.core.model.BoundingBox r30, boolean r31, double r32, double r34, org.mapsforge.map.reader.MapFile.Selector r36, org.mapsforge.map.reader.ReadBuffer r37) {
        /*
            r27 = this;
            r7 = r27
            r8 = r28
            r9 = r37
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            org.mapsforge.map.reader.header.MapFileHeader r0 = r7.mapFileHeader
            org.mapsforge.map.reader.header.MapFileInfo r0 = r0.getMapFileInfo()
            org.mapsforge.core.model.Tag[] r11 = r0.wayTags
            int r0 = wayFilterDistance
            r1 = r30
            org.mapsforge.core.model.BoundingBox r12 = r1.extendMeters(r0)
            r13 = r29
        L_0x001d:
            if (r13 == 0) goto L_0x01a6
            org.mapsforge.map.reader.header.MapFileHeader r0 = r7.mapFileHeader
            org.mapsforge.map.reader.header.MapFileInfo r0 = r0.getMapFileInfo()
            boolean r0 = r0.debugFile
            r14 = 0
            if (r0 == 0) goto L_0x004f
            r0 = 32
            java.lang.String r0 = r9.readUTF8EncodedString(r0)
            java.lang.String r1 = "---WayStart"
            boolean r1 = r0.startsWith(r1)
            if (r1 != 0) goto L_0x004f
            java.util.logging.Logger r1 = LOGGER
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "invalid way signature: "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            r1.warning(r0)
            return r14
        L_0x004f:
            int r0 = r37.readUnsignedInt()
            if (r0 >= 0) goto L_0x006c
            java.util.logging.Logger r1 = LOGGER
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "invalid way data size: "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            r1.warning(r0)
            return r14
        L_0x006c:
            boolean r1 = r8.useTileBitmask
            if (r1 == 0) goto L_0x0082
            int r1 = r37.readShort()
            int r2 = r8.queryTileBitmask
            r1 = r1 & r2
            if (r1 != 0) goto L_0x0086
            int r0 = r0 + -2
            r9.skipBytes(r0)
        L_0x007e:
            r2 = r36
            goto L_0x01a2
        L_0x0082:
            r0 = 2
            r9.skipBytes(r0)
        L_0x0086:
            byte r0 = r37.readByte()
            r1 = r0 & 240(0xf0, float:3.36E-43)
            int r1 = r1 >>> 4
            byte r15 = (byte) r1
            r0 = r0 & 15
            byte r0 = (byte) r0
            java.util.List r6 = r9.readTags(r11, r0)
            if (r6 != 0) goto L_0x0099
            return r14
        L_0x0099:
            byte r0 = r37.readByte()
            r1 = r0 & 128(0x80, float:1.794E-43)
            r5 = 1
            r16 = 0
            if (r1 == 0) goto L_0x00a7
            r17 = r5
            goto L_0x00a9
        L_0x00a7:
            r17 = r16
        L_0x00a9:
            r1 = r0 & 64
            if (r1 == 0) goto L_0x00b0
            r18 = r5
            goto L_0x00b2
        L_0x00b0:
            r18 = r16
        L_0x00b2:
            r1 = r0 & 32
            if (r1 == 0) goto L_0x00b9
            r19 = r5
            goto L_0x00bb
        L_0x00b9:
            r19 = r16
        L_0x00bb:
            r1 = r0 & 16
            if (r1 == 0) goto L_0x00c1
            r1 = r5
            goto L_0x00c3
        L_0x00c1:
            r1 = r16
        L_0x00c3:
            r2 = r0 & 8
            if (r2 == 0) goto L_0x00c9
            r2 = r5
            goto L_0x00cb
        L_0x00c9:
            r2 = r16
        L_0x00cb:
            r0 = r0 & 4
            if (r0 == 0) goto L_0x00d2
            r20 = r5
            goto L_0x00d4
        L_0x00d2:
            r20 = r16
        L_0x00d4:
            if (r17 == 0) goto L_0x00e8
            org.mapsforge.core.model.Tag r0 = new org.mapsforge.core.model.Tag
            java.lang.String r3 = r37.readUTF8EncodedString()
            java.lang.String r3 = r7.extractLocalized(r3)
            java.lang.String r4 = "name"
            r0.<init>((java.lang.String) r4, (java.lang.String) r3)
            r6.add(r0)
        L_0x00e8:
            if (r18 == 0) goto L_0x00f8
            org.mapsforge.core.model.Tag r0 = new org.mapsforge.core.model.Tag
            java.lang.String r3 = r37.readUTF8EncodedString()
            java.lang.String r4 = "addr:housenumber"
            r0.<init>((java.lang.String) r4, (java.lang.String) r3)
            r6.add(r0)
        L_0x00f8:
            if (r19 == 0) goto L_0x0108
            org.mapsforge.core.model.Tag r0 = new org.mapsforge.core.model.Tag
            java.lang.String r3 = r37.readUTF8EncodedString()
            java.lang.String r4 = "ref"
            r0.<init>((java.lang.String) r4, (java.lang.String) r3)
            r6.add(r0)
        L_0x0108:
            if (r1 == 0) goto L_0x0111
            int[] r0 = r7.readOptionalLabelPosition(r9)
            r21 = r0
            goto L_0x0113
        L_0x0111:
            r21 = r14
        L_0x0113:
            int r3 = r7.readOptionalWayDataBlocksByte(r2, r9)
            if (r3 >= r5) goto L_0x0130
            java.util.logging.Logger r0 = LOGGER
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "invalid number of way data blocks: "
            r1.append(r2)
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            r0.warning(r1)
            return r14
        L_0x0130:
            r4 = r16
        L_0x0132:
            if (r4 >= r3) goto L_0x007e
            r0 = r27
            r1 = r32
            r22 = r3
            r23 = r4
            r3 = r34
            r24 = r5
            r5 = r20
            r14 = r6
            r6 = r37
            org.mapsforge.core.model.LatLong[][] r0 = r0.processWayDataBlock(r1, r3, r5, r6)
            if (r0 == 0) goto L_0x0197
            if (r31 == 0) goto L_0x0158
            boolean r1 = wayFilterEnabled
            if (r1 == 0) goto L_0x0158
            boolean r1 = r12.intersectsArea(r0)
            if (r1 != 0) goto L_0x0158
            goto L_0x0197
        L_0x0158:
            org.mapsforge.map.reader.MapFile$Selector r1 = org.mapsforge.map.reader.MapFile.Selector.ALL
            r2 = r36
            if (r1 == r2) goto L_0x016a
            if (r17 != 0) goto L_0x016a
            if (r18 != 0) goto L_0x016a
            if (r19 != 0) goto L_0x016a
            boolean r1 = r7.wayAsLabelTagFilter(r14)
            if (r1 == 0) goto L_0x0199
        L_0x016a:
            if (r21 == 0) goto L_0x018d
            org.mapsforge.core.model.LatLong r1 = new org.mapsforge.core.model.LatLong
            r3 = r0[r16]
            r3 = r3[r16]
            double r3 = r3.latitude
            r5 = r21[r24]
            double r5 = org.mapsforge.core.util.LatLongUtils.microdegreesToDegrees(r5)
            double r3 = r3 + r5
            r5 = r0[r16]
            r5 = r5[r16]
            double r5 = r5.longitude
            r25 = r21[r16]
            double r25 = org.mapsforge.core.util.LatLongUtils.microdegreesToDegrees(r25)
            double r5 = r5 + r25
            r1.<init>(r3, r5)
            goto L_0x018e
        L_0x018d:
            r1 = 0
        L_0x018e:
            org.mapsforge.map.datastore.Way r3 = new org.mapsforge.map.datastore.Way
            r3.<init>(r15, r14, r0, r1)
            r10.add(r3)
            goto L_0x0199
        L_0x0197:
            r2 = r36
        L_0x0199:
            int r4 = r23 + 1
            r6 = r14
            r3 = r22
            r5 = r24
            r14 = 0
            goto L_0x0132
        L_0x01a2:
            int r13 = r13 + -1
            goto L_0x001d
        L_0x01a6:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.reader.MapFile.processWays(org.mapsforge.map.reader.QueryParameters, int, org.mapsforge.core.model.BoundingBox, boolean, double, double, org.mapsforge.map.reader.MapFile$Selector, org.mapsforge.map.reader.ReadBuffer):java.util.List");
    }

    public MapReadResult readLabels(Tile tile) {
        return readMapData(tile, tile, Selector.LABELS);
    }

    public MapReadResult readLabels(Tile tile, Tile tile2) {
        return readMapData(tile, tile2, Selector.LABELS);
    }

    public MapReadResult readMapData(Tile tile) {
        return readMapData(tile, tile, Selector.ALL);
    }

    public MapReadResult readMapData(Tile tile, Tile tile2) {
        return readMapData(tile, tile2, Selector.ALL);
    }

    private MapReadResult readMapData(Tile tile, Tile tile2, Selector selector) {
        if (tile.tileX > tile2.tileX || tile.tileY > tile2.tileY) {
            new IllegalArgumentException("upperLeft tile must be above and left of lowerRight tile");
        }
        try {
            QueryParameters queryParameters = new QueryParameters();
            queryParameters.queryZoomLevel = this.mapFileHeader.getQueryZoomLevel(tile.zoomLevel);
            SubFileParameter subFileParameter = this.mapFileHeader.getSubFileParameter(queryParameters.queryZoomLevel);
            if (subFileParameter == null) {
                Logger logger = LOGGER;
                logger.warning("no sub-file for zoom level: " + queryParameters.queryZoomLevel);
                return null;
            }
            queryParameters.calculateBaseTiles(tile, tile2, subFileParameter);
            queryParameters.calculateBlocks(subFileParameter);
            return processBlocks(queryParameters, subFileParameter, Tile.getBoundingBox(tile, tile2), selector);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    private int[] readOptionalLabelPosition(ReadBuffer readBuffer) {
        int[] iArr = new int[2];
        iArr[1] = readBuffer.readSignedInt();
        iArr[0] = readBuffer.readSignedInt();
        return iArr;
    }

    private int readOptionalWayDataBlocksByte(boolean z, ReadBuffer readBuffer) {
        if (z) {
            return readBuffer.readUnsignedInt();
        }
        return 1;
    }

    public MapReadResult readPoiData(Tile tile) {
        return readMapData(tile, tile, Selector.POIS);
    }

    public MapReadResult readPoiData(Tile tile, Tile tile2) {
        return readMapData(tile, tile2, Selector.POIS);
    }

    private int[][] readZoomTable(SubFileParameter subFileParameter, ReadBuffer readBuffer) {
        int i = (subFileParameter.zoomLevelMax - subFileParameter.zoomLevelMin) + 1;
        int[] iArr = new int[2];
        iArr[1] = 2;
        iArr[0] = i;
        int[][] iArr2 = (int[][]) Array.newInstance(Integer.TYPE, iArr);
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < i; i4++) {
            i2 += readBuffer.readUnsignedInt();
            i3 += readBuffer.readUnsignedInt();
            int[] iArr3 = iArr2[i4];
            iArr3[0] = i2;
            iArr3[1] = i3;
        }
        return iArr2;
    }

    public void restrictToZoomRange(byte b, byte b2) {
        this.zoomLevelMax = b2;
        this.zoomLevelMin = b;
    }

    public LatLong startPosition() {
        if (getMapFileInfo().startPosition != null) {
            return getMapFileInfo().startPosition;
        }
        return getMapFileInfo().boundingBox.getCenterPoint();
    }

    public Byte startZoomLevel() {
        if (getMapFileInfo().startZoomLevel != null) {
            return getMapFileInfo().startZoomLevel;
        }
        return Byte.valueOf(DEFAULT_START_ZOOM_LEVEL);
    }

    public boolean supportsTile(Tile tile) {
        return tile.getBoundingBox().intersects(getMapFileInfo().boundingBox) && tile.zoomLevel >= this.zoomLevelMin && tile.zoomLevel <= this.zoomLevelMax;
    }
}
