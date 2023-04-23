package mil.nga.tiff;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import mil.nga.tiff.compression.CompressionDecoder;
import mil.nga.tiff.compression.DeflateCompression;
import mil.nga.tiff.compression.LZWCompression;
import mil.nga.tiff.compression.PackbitsCompression;
import mil.nga.tiff.compression.RawCompression;
import mil.nga.tiff.p011io.ByteReader;
import mil.nga.tiff.util.TiffException;

public class FileDirectory {
    private Map<Integer, byte[]> cache;
    private CompressionDecoder decoder;
    private final SortedSet<FileDirectoryEntry> entries;
    private final Map<FieldTagType, FileDirectoryEntry> fieldTagTypeMapping;
    private byte[] lastBlock;
    private int lastBlockIndex;
    private int planarConfiguration;
    private ByteReader reader;
    private boolean tiled;
    private Rasters writeRasters;

    public FileDirectory(SortedSet<FileDirectoryEntry> sortedSet, ByteReader byteReader) {
        this(sortedSet, byteReader, false);
    }

    public FileDirectory(SortedSet<FileDirectoryEntry> sortedSet, ByteReader byteReader, boolean z) {
        this.fieldTagTypeMapping = new HashMap();
        this.cache = null;
        this.writeRasters = null;
        this.lastBlockIndex = -1;
        this.entries = sortedSet;
        for (FileDirectoryEntry fileDirectoryEntry : sortedSet) {
            this.fieldTagTypeMapping.put(fileDirectoryEntry.getFieldTag(), fileDirectoryEntry);
        }
        this.reader = byteReader;
        setCache(z);
        this.tiled = getRowsPerStrip() == null;
        Integer planarConfiguration2 = getPlanarConfiguration();
        int intValue = planarConfiguration2 != null ? planarConfiguration2.intValue() : 1;
        this.planarConfiguration = intValue;
        if (intValue == 1 || intValue == 2) {
            Integer compression = getCompression();
            compression = compression == null ? 1 : compression;
            int intValue2 = compression.intValue();
            if (intValue2 != 32773) {
                if (intValue2 != 32946) {
                    switch (intValue2) {
                        case 1:
                            this.decoder = new RawCompression();
                            return;
                        case 2:
                            throw new TiffException("CCITT Huffman compression not supported: " + compression);
                        case 3:
                            throw new TiffException("T4-encoding compression not supported: " + compression);
                        case 4:
                            throw new TiffException("T6-encoding compression not supported: " + compression);
                        case 5:
                            this.decoder = new LZWCompression();
                            return;
                        case 6:
                        case 7:
                            throw new TiffException("JPEG compression not supported: " + compression);
                        case 8:
                            break;
                        default:
                            throw new TiffException("Unknown compression method identifier: " + compression);
                    }
                }
                this.decoder = new DeflateCompression();
                return;
            }
            this.decoder = new PackbitsCompression();
            return;
        }
        throw new TiffException("Invalid planar configuration: " + this.planarConfiguration);
    }

    public FileDirectory() {
        this((Rasters) null);
    }

    public FileDirectory(Rasters rasters) {
        this((SortedSet<FileDirectoryEntry>) new TreeSet(), rasters);
    }

    public FileDirectory(SortedSet<FileDirectoryEntry> sortedSet, Rasters rasters) {
        this.fieldTagTypeMapping = new HashMap();
        this.cache = null;
        this.writeRasters = null;
        this.lastBlockIndex = -1;
        this.entries = sortedSet;
        for (FileDirectoryEntry fileDirectoryEntry : sortedSet) {
            this.fieldTagTypeMapping.put(fileDirectoryEntry.getFieldTag(), fileDirectoryEntry);
        }
        this.writeRasters = rasters;
    }

    public void addEntry(FileDirectoryEntry fileDirectoryEntry) {
        this.entries.remove(fileDirectoryEntry);
        this.entries.add(fileDirectoryEntry);
        this.fieldTagTypeMapping.put(fileDirectoryEntry.getFieldTag(), fileDirectoryEntry);
    }

    public void setCache(boolean z) {
        if (!z) {
            this.cache = null;
        } else if (this.cache == null) {
            this.cache = new HashMap();
        }
    }

    public ByteReader getReader() {
        return this.reader;
    }

    public boolean isTiled() {
        return this.tiled;
    }

    public CompressionDecoder getDecoder() {
        return this.decoder;
    }

    public int numEntries() {
        return this.entries.size();
    }

    public FileDirectoryEntry get(FieldTagType fieldTagType) {
        return this.fieldTagTypeMapping.get(fieldTagType);
    }

    public Set<FileDirectoryEntry> getEntries() {
        return Collections.unmodifiableSet(this.entries);
    }

    public Map<FieldTagType, FileDirectoryEntry> getFieldTagTypeMapping() {
        return Collections.unmodifiableMap(this.fieldTagTypeMapping);
    }

    public Number getImageWidth() {
        return getNumberEntryValue(FieldTagType.ImageWidth);
    }

    public void setImageWidth(int i) {
        setUnsignedIntegerEntryValue(FieldTagType.ImageWidth, i);
    }

    public void setImageWidthAsLong(long j) {
        setUnsignedLongEntryValue(FieldTagType.ImageWidth, j);
    }

    public Number getImageHeight() {
        return getNumberEntryValue(FieldTagType.ImageLength);
    }

    public void setImageHeight(int i) {
        setUnsignedIntegerEntryValue(FieldTagType.ImageLength, i);
    }

    public void setImageHeightAsLong(long j) {
        setUnsignedLongEntryValue(FieldTagType.ImageLength, j);
    }

    public List<Integer> getBitsPerSample() {
        return getIntegerListEntryValue(FieldTagType.BitsPerSample);
    }

    public void setBitsPerSample(List<Integer> list) {
        setUnsignedIntegerListEntryValue(FieldTagType.BitsPerSample, list);
    }

    public void setBitsPerSample(int i) {
        setBitsPerSample(createSingleIntegerList(i));
    }

    public Integer getMaxBitsPerSample() {
        return getMaxIntegerEntryValue(FieldTagType.BitsPerSample);
    }

    public Integer getCompression() {
        return getIntegerEntryValue(FieldTagType.Compression);
    }

    public void setCompression(int i) {
        setUnsignedIntegerEntryValue(FieldTagType.Compression, i);
    }

    public Integer getPhotometricInterpretation() {
        return getIntegerEntryValue(FieldTagType.PhotometricInterpretation);
    }

    public void setPhotometricInterpretation(int i) {
        setUnsignedIntegerEntryValue(FieldTagType.PhotometricInterpretation, i);
    }

    public List<Number> getStripOffsets() {
        return getNumberListEntryValue(FieldTagType.StripOffsets);
    }

    public void setStripOffsets(List<Integer> list) {
        setUnsignedIntegerListEntryValue(FieldTagType.StripOffsets, list);
    }

    public void setStripOffsetsAsLongs(List<Long> list) {
        setUnsignedLongListEntryValue(FieldTagType.StripOffsets, list);
    }

    public void setStripOffsets(int i) {
        setStripOffsets(createSingleIntegerList(i));
    }

    public void setStripOffsets(long j) {
        setStripOffsetsAsLongs(createSingleLongList(j));
    }

    public int getSamplesPerPixel() {
        Integer integerEntryValue = getIntegerEntryValue(FieldTagType.SamplesPerPixel);
        if (integerEntryValue == null) {
            integerEntryValue = 1;
        }
        return integerEntryValue.intValue();
    }

    public void setSamplesPerPixel(int i) {
        setUnsignedIntegerEntryValue(FieldTagType.SamplesPerPixel, i);
    }

    public Number getRowsPerStrip() {
        return getNumberEntryValue(FieldTagType.RowsPerStrip);
    }

    public void setRowsPerStrip(int i) {
        setUnsignedIntegerEntryValue(FieldTagType.RowsPerStrip, i);
    }

    public void setRowsPerStripAsLong(long j) {
        setUnsignedLongEntryValue(FieldTagType.RowsPerStrip, j);
    }

    public List<Number> getStripByteCounts() {
        return getNumberListEntryValue(FieldTagType.StripByteCounts);
    }

    public void setStripByteCounts(List<Integer> list) {
        setUnsignedIntegerListEntryValue(FieldTagType.StripByteCounts, list);
    }

    public void setStripByteCountsAsLongs(List<Long> list) {
        setUnsignedLongListEntryValue(FieldTagType.StripByteCounts, list);
    }

    public void setStripByteCounts(int i) {
        setStripByteCounts(createSingleIntegerList(i));
    }

    public void setStripByteCounts(long j) {
        setStripByteCountsAsLongs(createSingleLongList(j));
    }

    public List<Long> getXResolution() {
        return getLongListEntryValue(FieldTagType.XResolution);
    }

    public void setXResolution(List<Long> list) {
        setUnsignedLongListEntryValue(FieldTagType.XResolution, list);
    }

    public void setXResolution(long j) {
        setXResolution(createSingleLongList(j));
    }

    public List<Long> getYResolution() {
        return getLongListEntryValue(FieldTagType.YResolution);
    }

    public void setYResolution(List<Long> list) {
        setUnsignedLongListEntryValue(FieldTagType.YResolution, list);
    }

    public void setYResolution(long j) {
        setYResolution(createSingleLongList(j));
    }

    public Integer getPlanarConfiguration() {
        return getIntegerEntryValue(FieldTagType.PlanarConfiguration);
    }

    public void setPlanarConfiguration(int i) {
        setUnsignedIntegerEntryValue(FieldTagType.PlanarConfiguration, i);
    }

    public Integer getResolutionUnit() {
        return getIntegerEntryValue(FieldTagType.ResolutionUnit);
    }

    public void setResolutionUnit(int i) {
        setUnsignedIntegerEntryValue(FieldTagType.ResolutionUnit, i);
    }

    public List<Integer> getColorMap() {
        return getIntegerListEntryValue(FieldTagType.ColorMap);
    }

    public void setColorMap(List<Integer> list) {
        setUnsignedIntegerListEntryValue(FieldTagType.ColorMap, list);
    }

    public void setColorMap(int i) {
        setColorMap(createSingleIntegerList(i));
    }

    public Number getTileWidth() {
        if (this.tiled) {
            return getNumberEntryValue(FieldTagType.TileWidth);
        }
        return getImageWidth();
    }

    public void setTileWidth(int i) {
        setUnsignedIntegerEntryValue(FieldTagType.TileWidth, i);
    }

    public void setTileWidthAsLong(long j) {
        setUnsignedLongEntryValue(FieldTagType.TileWidth, j);
    }

    public Number getTileHeight() {
        if (this.tiled) {
            return getNumberEntryValue(FieldTagType.TileLength);
        }
        return getRowsPerStrip();
    }

    public void setTileHeight(int i) {
        setUnsignedIntegerEntryValue(FieldTagType.TileLength, i);
    }

    public void setTileHeightAsLong(long j) {
        setUnsignedLongEntryValue(FieldTagType.TileLength, j);
    }

    public List<Long> getTileOffsets() {
        return getLongListEntryValue(FieldTagType.TileOffsets);
    }

    public void setTileOffsets(List<Long> list) {
        setUnsignedLongListEntryValue(FieldTagType.TileOffsets, list);
    }

    public void setTileOffsets(long j) {
        setTileOffsets(createSingleLongList(j));
    }

    public List<Number> getTileByteCounts() {
        return getNumberListEntryValue(FieldTagType.TileByteCounts);
    }

    public void setTileByteCounts(List<Integer> list) {
        setUnsignedIntegerListEntryValue(FieldTagType.TileByteCounts, list);
    }

    public void setTileByteCountsAsLongs(List<Long> list) {
        setUnsignedLongListEntryValue(FieldTagType.TileByteCounts, list);
    }

    public void setTileByteCounts(int i) {
        setTileByteCounts(createSingleIntegerList(i));
    }

    public void setTileByteCounts(long j) {
        setTileByteCountsAsLongs(createSingleLongList(j));
    }

    public List<Integer> getSampleFormat() {
        return getIntegerListEntryValue(FieldTagType.SampleFormat);
    }

    public void setSampleFormat(List<Integer> list) {
        setUnsignedIntegerListEntryValue(FieldTagType.SampleFormat, list);
    }

    public void setSampleFormat(int i) {
        setSampleFormat(createSingleIntegerList(i));
    }

    public Integer getMaxSampleFormat() {
        return getMaxIntegerEntryValue(FieldTagType.SampleFormat);
    }

    public Rasters getWriteRasters() {
        return this.writeRasters;
    }

    public void setWriteRasters(Rasters rasters) {
        this.writeRasters = rasters;
    }

    public Rasters readRasters() {
        return readRasters(new ImageWindow(this));
    }

    public Rasters readInterleavedRasters() {
        return readInterleavedRasters(new ImageWindow(this));
    }

    public Rasters readRasters(ImageWindow imageWindow) {
        return readRasters(imageWindow, (int[]) null);
    }

    public Rasters readInterleavedRasters(ImageWindow imageWindow) {
        return readInterleavedRasters(imageWindow, (int[]) null);
    }

    public Rasters readRasters(int[] iArr) {
        return readRasters(new ImageWindow(this), iArr);
    }

    public Rasters readInterleavedRasters(int[] iArr) {
        return readInterleavedRasters(new ImageWindow(this), iArr);
    }

    public Rasters readRasters(ImageWindow imageWindow, int[] iArr) {
        return readRasters(imageWindow, iArr, true, false);
    }

    public Rasters readInterleavedRasters(ImageWindow imageWindow, int[] iArr) {
        return readRasters(imageWindow, iArr, false, true);
    }

    public Rasters readRasters(boolean z, boolean z2) {
        return readRasters(new ImageWindow(this), z, z2);
    }

    public Rasters readRasters(ImageWindow imageWindow, boolean z, boolean z2) {
        return readRasters(imageWindow, (int[]) null, z, z2);
    }

    public Rasters readRasters(int[] iArr, boolean z, boolean z2) {
        return readRasters(new ImageWindow(this), iArr, z, z2);
    }

    public Rasters readRasters(ImageWindow imageWindow, int[] iArr, boolean z, boolean z2) {
        ByteBuffer byteBuffer;
        int intValue = getImageWidth().intValue();
        int intValue2 = getImageHeight().intValue();
        if (imageWindow.getMinX() < 0 || imageWindow.getMinY() < 0 || imageWindow.getMaxX() > intValue || imageWindow.getMaxY() > intValue2) {
            throw new TiffException("Window is out of the image bounds. Width: " + intValue + ", Height: " + intValue2 + ", Window: " + imageWindow);
        } else if (imageWindow.getMinX() > imageWindow.getMaxX() || imageWindow.getMinY() > imageWindow.getMaxY()) {
            throw new TiffException("Invalid window range: " + imageWindow);
        } else {
            int maxX = imageWindow.getMaxX() - imageWindow.getMinX();
            int maxY = imageWindow.getMaxY() - imageWindow.getMinY();
            int i = maxX * maxY;
            int samplesPerPixel = getSamplesPerPixel();
            if (iArr == null) {
                iArr = new int[samplesPerPixel];
                for (int i2 = 0; i2 < samplesPerPixel; i2++) {
                    iArr[i2] = i2;
                }
            } else {
                int i3 = 0;
                while (i3 < iArr.length) {
                    if (iArr[i3] < samplesPerPixel) {
                        i3++;
                    } else {
                        throw new TiffException("Invalid sample index: " + iArr[i3]);
                    }
                }
            }
            List<Integer> bitsPerSample = getBitsPerSample();
            int i4 = 0;
            for (int i5 = 0; i5 < samplesPerPixel; i5++) {
                i4 += bitsPerSample.get(i5).intValue() / 8;
            }
            ByteBuffer[] byteBufferArr = null;
            if (z2) {
                ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i4 * i);
                allocateDirect.order(this.reader.getByteOrder());
                byteBuffer = allocateDirect;
            } else {
                byteBuffer = null;
            }
            if (z) {
                ByteBuffer[] byteBufferArr2 = new ByteBuffer[samplesPerPixel];
                for (int i6 = 0; i6 < samplesPerPixel; i6++) {
                    ByteBuffer allocateDirect2 = ByteBuffer.allocateDirect((bitsPerSample.get(i6).intValue() * i) / 8);
                    byteBufferArr2[i6] = allocateDirect2;
                    allocateDirect2.order(this.reader.getByteOrder());
                }
                byteBufferArr = byteBufferArr2;
            }
            FieldType[] fieldTypeArr = new FieldType[iArr.length];
            for (int i7 = 0; i7 < iArr.length; i7++) {
                fieldTypeArr[i7] = getFieldTypeForSample(iArr[i7]);
            }
            Rasters rasters = new Rasters(maxX, maxY, fieldTypeArr, byteBufferArr, byteBuffer);
            readRaster(imageWindow, iArr, rasters);
            return rasters;
        }
    }

    private void readRaster(ImageWindow imageWindow, int[] iArr, Rasters rasters) {
        int i;
        int i2;
        FileDirectory fileDirectory = this;
        int[] iArr2 = iArr;
        Rasters rasters2 = rasters;
        int intValue = getTileWidth().intValue();
        int intValue2 = getTileHeight().intValue();
        int minX = imageWindow.getMinX() / intValue;
        int i3 = 1;
        int maxX = ((imageWindow.getMaxX() + intValue) - 1) / intValue;
        int minY = imageWindow.getMinY() / intValue2;
        int maxY = ((imageWindow.getMaxY() + intValue2) - 1) / intValue2;
        int maxX2 = imageWindow.getMaxX() - imageWindow.getMinX();
        int bytesPerPixel = getBytesPerPixel();
        int[] iArr3 = new int[iArr2.length];
        FieldType[] fieldTypeArr = new FieldType[iArr2.length];
        int i4 = 0;
        while (i4 < iArr2.length) {
            if (fileDirectory.planarConfiguration == i3) {
                i = minX;
                i2 = fileDirectory.sum(getBitsPerSample(), 0, iArr2[i4]) / 8;
            } else {
                i = minX;
                i2 = 0;
            }
            iArr3[i4] = i2;
            fieldTypeArr[i4] = fileDirectory.getFieldTypeForSample(iArr2[i4]);
            i4++;
            minX = i;
            i3 = 1;
        }
        int i5 = minX;
        while (minY < maxY) {
            int i6 = i5;
            while (i6 < maxX) {
                int i7 = minY * intValue2;
                int i8 = i6 * intValue;
                int i9 = (minY + 1) * intValue2;
                int i10 = i6 + 1;
                int i11 = i10 * intValue;
                int i12 = maxX;
                int i13 = bytesPerPixel;
                int i14 = 0;
                while (i14 < iArr2.length) {
                    int i15 = iArr2[i14];
                    int i16 = maxY;
                    if (fileDirectory.planarConfiguration == 2) {
                        i13 = fileDirectory.getSampleByteSize(i15);
                    }
                    ByteReader byteReader = new ByteReader(fileDirectory.getTileOrStrip(i6, minY, i15), fileDirectory.reader.getByteOrder());
                    int max = Math.max(0, imageWindow.getMinY() - i7);
                    while (max < Math.min(intValue2, intValue2 - (i9 - imageWindow.getMaxY()))) {
                        int i17 = intValue2;
                        int max2 = Math.max(0, imageWindow.getMinX() - i8);
                        while (max2 < Math.min(intValue, intValue - (i11 - imageWindow.getMaxX()))) {
                            byteReader.setNextByte((((max * intValue) + max2) * i13) + iArr3[i14]);
                            Number readValue = fileDirectory.readValue(byteReader, fieldTypeArr[i14]);
                            if (rasters.hasInterleaveValues()) {
                                rasters2.addToInterleave(i14, (((max + i7) - imageWindow.getMinY()) * maxX2) + ((max2 + i8) - imageWindow.getMinX()), readValue);
                            }
                            if (rasters.hasSampleValues()) {
                                rasters2.addToSample(i14, (((((max + i7) - imageWindow.getMinY()) * maxX2) + max2) + i8) - imageWindow.getMinX(), readValue);
                            }
                            max2++;
                            fileDirectory = this;
                        }
                        max++;
                        fileDirectory = this;
                        intValue2 = i17;
                    }
                    int i18 = intValue2;
                    i14++;
                    fileDirectory = this;
                    iArr2 = iArr;
                    maxY = i16;
                }
                fileDirectory = this;
                iArr2 = iArr;
                i6 = i10;
                bytesPerPixel = i13;
                maxX = i12;
            }
            int i19 = intValue2;
            int i20 = maxX;
            int i21 = maxY;
            minY++;
            fileDirectory = this;
            iArr2 = iArr;
        }
    }

    /* renamed from: mil.nga.tiff.FileDirectory$1 */
    static /* synthetic */ class C11811 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$tiff$FieldType;

        /* JADX WARNING: Can't wrap try/catch for region: R(18:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|18) */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                mil.nga.tiff.FieldType[] r0 = mil.nga.tiff.FieldType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$mil$nga$tiff$FieldType = r0
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.BYTE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.SHORT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.LONG     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x0033 }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.SBYTE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x003e }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.SSHORT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x0049 }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.SLONG     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x0054 }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.FLOAT     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x0060 }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.DOUBLE     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.tiff.FileDirectory.C11811.<clinit>():void");
        }
    }

    private Number readValue(ByteReader byteReader, FieldType fieldType) {
        switch (C11811.$SwitchMap$mil$nga$tiff$FieldType[fieldType.ordinal()]) {
            case 1:
                return Short.valueOf(byteReader.readUnsignedByte());
            case 2:
                return Integer.valueOf(byteReader.readUnsignedShort());
            case 3:
                return Long.valueOf(byteReader.readUnsignedInt());
            case 4:
                return Byte.valueOf(byteReader.readByte());
            case 5:
                return Short.valueOf(byteReader.readShort());
            case 6:
                return Integer.valueOf(byteReader.readInt());
            case 7:
                return Float.valueOf(byteReader.readFloat());
            case 8:
                return Double.valueOf(byteReader.readDouble());
            default:
                throw new TiffException("Unsupported raster field type: " + fieldType);
        }
    }

    public FieldType getFieldTypeForSample(int i) {
        int i2;
        List<Integer> sampleFormat = getSampleFormat();
        if (sampleFormat == null) {
            i2 = 1;
        } else {
            i2 = sampleFormat.get(i < sampleFormat.size() ? i : 0).intValue();
        }
        return FieldType.getFieldType(i2, getBitsPerSample().get(i).intValue());
    }

    private byte[] getTileOrStrip(int i, int i2, int i3) {
        int i4;
        int i5;
        byte[] bArr;
        int intValue = getImageWidth().intValue();
        int intValue2 = getImageHeight().intValue();
        int intValue3 = getTileWidth().intValue();
        int intValue4 = getTileHeight().intValue();
        int i6 = ((intValue + intValue3) - 1) / intValue3;
        int i7 = ((intValue2 + intValue4) - 1) / intValue4;
        int i8 = this.planarConfiguration;
        int i9 = i8 == 1 ? (i2 * i6) + i : i8 == 2 ? (i3 * i6 * i7) + (i2 * i6) + i : 0;
        Map<Integer, byte[]> map = this.cache;
        if (map != null && map.containsKey(Integer.valueOf(i9))) {
            return this.cache.get(Integer.valueOf(i9));
        }
        if (this.lastBlockIndex == i9 && (bArr = this.lastBlock) != null) {
            return bArr;
        }
        if (this.tiled) {
            i5 = getTileOffsets().get(i9).intValue();
            i4 = getTileByteCounts().get(i9).intValue();
        } else {
            i5 = getStripOffsets().get(i9).intValue();
            i4 = getStripByteCounts().get(i9).intValue();
        }
        this.reader.setNextByte(i5);
        byte[] decode = this.decoder.decode(this.reader.readBytes(i4), this.reader.getByteOrder());
        Map<Integer, byte[]> map2 = this.cache;
        if (map2 != null) {
            map2.put(Integer.valueOf(i9), decode);
            return decode;
        }
        this.lastBlockIndex = i9;
        this.lastBlock = decode;
        return decode;
    }

    private int getSampleByteSize(int i) {
        List<Integer> bitsPerSample = getBitsPerSample();
        if (i < bitsPerSample.size()) {
            int intValue = bitsPerSample.get(i).intValue();
            if (intValue % 8 == 0) {
                return intValue / 8;
            }
            throw new TiffException("Sample bit-width of " + intValue + " is not supported");
        }
        throw new TiffException("Sample index " + i + " is out of range");
    }

    private int getBytesPerPixel() {
        List<Integer> bitsPerSample = getBitsPerSample();
        int i = 0;
        int i2 = 0;
        while (i < bitsPerSample.size()) {
            int intValue = bitsPerSample.get(i).intValue();
            if (intValue % 8 != 0) {
                throw new TiffException("Sample bit-width of " + intValue + " is not supported");
            } else if (intValue == bitsPerSample.get(0).intValue()) {
                i2 += intValue;
                i++;
            } else {
                throw new TiffException("Differing size of samples in a pixel are not supported. sample 0 = " + bitsPerSample.get(0) + ", sample " + i + " = " + intValue);
            }
        }
        return i2 / 8;
    }

    public Integer getIntegerEntryValue(FieldTagType fieldTagType) {
        return (Integer) getEntryValue(fieldTagType);
    }

    public void setUnsignedIntegerEntryValue(FieldTagType fieldTagType, int i) {
        setEntryValue(fieldTagType, FieldType.SHORT, 1, Integer.valueOf(i));
    }

    public Number getNumberEntryValue(FieldTagType fieldTagType) {
        return (Number) getEntryValue(fieldTagType);
    }

    public void setUnsignedLongEntryValue(FieldTagType fieldTagType, long j) {
        setEntryValue(fieldTagType, FieldType.LONG, 1, Long.valueOf(j));
    }

    public String getStringEntryValue(FieldTagType fieldTagType) {
        List list = (List) getEntryValue(fieldTagType);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return (String) list.get(0);
    }

    public void setStringEntryValue(FieldTagType fieldTagType, String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        setEntryValue(fieldTagType, FieldType.ASCII, (long) (str.length() + 1), arrayList);
    }

    public List<Integer> getIntegerListEntryValue(FieldTagType fieldTagType) {
        return (List) getEntryValue(fieldTagType);
    }

    public void setUnsignedIntegerListEntryValue(FieldTagType fieldTagType, List<Integer> list) {
        setEntryValue(fieldTagType, FieldType.SHORT, (long) list.size(), list);
    }

    public Integer getMaxIntegerEntryValue(FieldTagType fieldTagType) {
        List<Integer> integerListEntryValue = getIntegerListEntryValue(fieldTagType);
        if (integerListEntryValue != null) {
            return (Integer) Collections.max(integerListEntryValue);
        }
        return null;
    }

    public List<Number> getNumberListEntryValue(FieldTagType fieldTagType) {
        return (List) getEntryValue(fieldTagType);
    }

    public List<Long> getLongListEntryValue(FieldTagType fieldTagType) {
        return (List) getEntryValue(fieldTagType);
    }

    public void setUnsignedLongListEntryValue(FieldTagType fieldTagType, List<Long> list) {
        setEntryValue(fieldTagType, FieldType.LONG, (long) list.size(), list);
    }

    private <T> T getEntryValue(FieldTagType fieldTagType) {
        FileDirectoryEntry fileDirectoryEntry = this.fieldTagTypeMapping.get(fieldTagType);
        if (fileDirectoryEntry != null) {
            return fileDirectoryEntry.getValues();
        }
        return null;
    }

    private void setEntryValue(FieldTagType fieldTagType, FieldType fieldType, long j, Object obj) {
        addEntry(new FileDirectoryEntry(fieldTagType, fieldType, j, obj));
    }

    private int sum(List<Integer> list, int i, int i2) {
        int i3 = 0;
        while (i < i2) {
            i3 += list.get(i).intValue();
            i++;
        }
        return i3;
    }

    private List<Integer> createSingleIntegerList(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(i));
        return arrayList;
    }

    private List<Long> createSingleLongList(long j) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Long.valueOf(j));
        return arrayList;
    }

    public long size() {
        return (long) ((this.entries.size() * 12) + 2 + 4);
    }

    public long sizeWithValues() {
        long j = 6;
        for (FileDirectoryEntry sizeWithValues : this.entries) {
            j += sizeWithValues.sizeWithValues();
        }
        return j;
    }
}
