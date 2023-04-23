package mil.nga.tiff;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mil.nga.tiff.compression.CompressionEncoder;
import mil.nga.tiff.compression.DeflateCompression;
import mil.nga.tiff.compression.LZWCompression;
import mil.nga.tiff.compression.PackbitsCompression;
import mil.nga.tiff.compression.RawCompression;
import mil.nga.tiff.p011io.ByteWriter;
import mil.nga.tiff.p011io.IOUtils;
import mil.nga.tiff.util.TiffConstants;
import mil.nga.tiff.util.TiffException;

public class TiffWriter {
    public static void writeTiff(File file, TIFFImage tIFFImage) throws IOException {
        ByteWriter byteWriter = new ByteWriter();
        writeTiff(file, byteWriter, tIFFImage);
        byteWriter.close();
    }

    public static void writeTiff(File file, ByteWriter byteWriter, TIFFImage tIFFImage) throws IOException {
        IOUtils.copyStream((InputStream) new ByteArrayInputStream(writeTiffToBytes(byteWriter, tIFFImage)), file);
    }

    public static byte[] writeTiffToBytes(TIFFImage tIFFImage) throws IOException {
        ByteWriter byteWriter = new ByteWriter();
        byte[] writeTiffToBytes = writeTiffToBytes(byteWriter, tIFFImage);
        byteWriter.close();
        return writeTiffToBytes;
    }

    public static byte[] writeTiffToBytes(ByteWriter byteWriter, TIFFImage tIFFImage) throws IOException {
        writeTiff(byteWriter, tIFFImage);
        return byteWriter.getBytes();
    }

    public static void writeTiff(ByteWriter byteWriter, TIFFImage tIFFImage) throws IOException {
        byteWriter.writeString(byteWriter.getByteOrder() == ByteOrder.BIG_ENDIAN ? TiffConstants.BYTE_ORDER_BIG_ENDIAN : TiffConstants.BYTE_ORDER_LITTLE_ENDIAN);
        byteWriter.writeUnsignedShort(42);
        byteWriter.writeUnsignedInt(8);
        writeImageFileDirectories(byteWriter, tIFFImage);
    }

    private static void writeImageFileDirectories(ByteWriter byteWriter, TIFFImage tIFFImage) throws IOException {
        ByteWriter byteWriter2 = byteWriter;
        int i = 0;
        while (i < tIFFImage.getFileDirectories().size()) {
            FileDirectory fileDirectory = tIFFImage.getFileDirectories().get(i);
            populateRasterEntries(fileDirectory);
            long size = (long) byteWriter.size();
            long size2 = fileDirectory.size() + size;
            long sizeWithValues = size + fileDirectory.sizeWithValues();
            byteWriter2.writeUnsignedShort(fileDirectory.numEntries());
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            if (!fileDirectory.isTiled()) {
                byte[] writeRasters = writeRasters(byteWriter.getByteOrder(), fileDirectory, sizeWithValues);
                for (FileDirectoryEntry next : fileDirectory.getEntries()) {
                    byteWriter2.writeUnsignedShort(next.getFieldTag().getId());
                    byteWriter2.writeUnsignedShort(next.getFieldType().getValue());
                    byteWriter2.writeUnsignedInt(next.getTypeCount());
                    long bytes = ((long) next.getFieldType().getBytes()) * next.getTypeCount();
                    if (bytes > 4) {
                        arrayList.add(next);
                        byteWriter2.writeUnsignedInt(size2);
                        arrayList2.add(Long.valueOf(size2));
                        size2 += next.sizeOfValues();
                    } else {
                        int writeValues = writeValues(byteWriter2, next);
                        long j = size2;
                        if (((long) writeValues) == bytes) {
                            writeFillerBytes(byteWriter2, 4 - bytes);
                            size2 = j;
                        } else {
                            throw new TiffException("Unexpected bytes written. Expected: " + bytes + ", Actual: " + writeValues);
                        }
                    }
                }
                i++;
                if (i == tIFFImage.getFileDirectories().size()) {
                    writeFillerBytes(byteWriter2, 4);
                } else {
                    byteWriter2.writeUnsignedInt(sizeWithValues + ((long) writeRasters.length));
                }
                int i2 = 0;
                while (i2 < arrayList.size()) {
                    FileDirectoryEntry fileDirectoryEntry = (FileDirectoryEntry) arrayList.get(i2);
                    long longValue = ((Long) arrayList2.get(i2)).longValue();
                    if (longValue == ((long) byteWriter.size())) {
                        int writeValues2 = writeValues(byteWriter2, fileDirectoryEntry);
                        long bytes2 = ((long) fileDirectoryEntry.getFieldType().getBytes()) * fileDirectoryEntry.getTypeCount();
                        if (((long) writeValues2) == bytes2) {
                            i2++;
                        } else {
                            throw new TiffException("Unexpected bytes written. Expected: " + bytes2 + ", Actual: " + writeValues2);
                        }
                    } else {
                        throw new TiffException("Entry values byte does not match the write location. Entry Values Byte: " + longValue + ", Current Byte: " + byteWriter.size());
                    }
                }
                byteWriter2.writeBytes(writeRasters);
            } else {
                throw new TiffException("Tiled images are not supported");
            }
        }
    }

    private static void populateRasterEntries(FileDirectory fileDirectory) {
        if (fileDirectory.getWriteRasters() == null) {
            throw new TiffException("File Directory Writer Rasters is required to create a TIFF");
        } else if (!fileDirectory.isTiled()) {
            populateStripEntries(fileDirectory);
        } else {
            throw new TiffException("Tiled images are not supported");
        }
    }

    private static void populateStripEntries(FileDirectory fileDirectory) {
        int intValue = fileDirectory.getRowsPerStrip().intValue();
        int intValue2 = ((fileDirectory.getImageHeight().intValue() + intValue) - 1) / intValue;
        if (fileDirectory.getPlanarConfiguration().intValue() == 2) {
            intValue2 *= fileDirectory.getSamplesPerPixel();
        }
        fileDirectory.setStripOffsetsAsLongs(new ArrayList(Collections.nCopies(intValue2, 0L)));
        fileDirectory.setStripByteCounts((List<Integer>) new ArrayList(Collections.nCopies(intValue2, 0)));
    }

    private static byte[] writeRasters(ByteOrder byteOrder, FileDirectory fileDirectory, long j) throws IOException {
        if (fileDirectory.getWriteRasters() != null) {
            CompressionEncoder encoder = getEncoder(fileDirectory);
            ByteWriter byteWriter = new ByteWriter(byteOrder);
            if (!fileDirectory.isTiled()) {
                writeStripRasters(byteWriter, fileDirectory, j, encoder);
                byte[] bytes = byteWriter.getBytes();
                byteWriter.close();
                return bytes;
            }
            throw new TiffException("Tiled images are not supported");
        }
        throw new TiffException("File Directory Writer Rasters is required to create a TIFF");
    }

    private static void writeStripRasters(ByteWriter byteWriter, FileDirectory fileDirectory, long j, CompressionEncoder compressionEncoder) throws IOException {
        int i;
        int i2;
        int i3;
        byte[] bArr;
        FileDirectory fileDirectory2 = fileDirectory;
        CompressionEncoder compressionEncoder2 = compressionEncoder;
        Rasters writeRasters = fileDirectory.getWriteRasters();
        int intValue = fileDirectory.getRowsPerStrip().intValue();
        int intValue2 = fileDirectory.getImageHeight().intValue();
        int i4 = ((intValue2 + intValue) - 1) / intValue;
        int i5 = 2;
        int samplesPerPixel = fileDirectory.getPlanarConfiguration().intValue() == 2 ? fileDirectory.getSamplesPerPixel() * i4 : i4;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i6 = 0;
        long j2 = j;
        while (i6 < samplesPerPixel) {
            Integer num = null;
            if (fileDirectory.getPlanarConfiguration().intValue() == i5) {
                num = Integer.valueOf(i6 / i4);
                i = (i6 % i4) * intValue;
            } else {
                i = i6 * intValue;
            }
            ByteWriter byteWriter2 = new ByteWriter(byteWriter.getByteOrder());
            int min = Math.min(i + intValue, intValue2);
            while (i < min) {
                if (num != null) {
                    i3 = intValue;
                    i2 = intValue2;
                    bArr = writeRasters.getSampleRow(i, num.intValue(), byteWriter.getByteOrder());
                } else {
                    i3 = intValue;
                    i2 = intValue2;
                    bArr = writeRasters.getPixelRow(i, byteWriter.getByteOrder());
                }
                if (compressionEncoder.rowEncoding()) {
                    bArr = compressionEncoder2.encode(bArr, byteWriter.getByteOrder());
                }
                byteWriter2.writeBytes(bArr);
                i++;
                intValue = i3;
                intValue2 = i2;
            }
            int i7 = intValue;
            int i8 = intValue2;
            byte[] bytes = byteWriter2.getBytes();
            byteWriter2.close();
            if (!compressionEncoder.rowEncoding()) {
                bytes = compressionEncoder2.encode(bytes, byteWriter.getByteOrder());
            }
            byteWriter.writeBytes(bytes);
            int length = bytes.length;
            arrayList2.add(Integer.valueOf(length));
            arrayList.add(Long.valueOf(j2));
            j2 += (long) length;
            i6++;
            intValue = i7;
            intValue2 = i8;
            i5 = 2;
        }
        fileDirectory2.setStripOffsetsAsLongs(arrayList);
        fileDirectory2.setStripByteCounts((List<Integer>) arrayList2);
    }

    private static CompressionEncoder getEncoder(FileDirectory fileDirectory) {
        Integer compression = fileDirectory.getCompression();
        if (compression == null) {
            compression = 1;
        }
        int intValue = compression.intValue();
        if (intValue == 32773) {
            return new PackbitsCompression();
        }
        if (intValue != 32946) {
            switch (intValue) {
                case 1:
                    return new RawCompression();
                case 2:
                    throw new TiffException("CCITT Huffman compression not supported: " + compression);
                case 3:
                    throw new TiffException("T4-encoding compression not supported: " + compression);
                case 4:
                    throw new TiffException("T6-encoding compression not supported: " + compression);
                case 5:
                    return new LZWCompression();
                case 6:
                case 7:
                    throw new TiffException("JPEG compression not supported: " + compression);
                case 8:
                    break;
                default:
                    throw new TiffException("Unknown compression method identifier: " + compression);
            }
        }
        return new DeflateCompression();
    }

    private static void writeFillerBytes(ByteWriter byteWriter, long j) {
        for (long j2 = 0; j2 < j; j2++) {
            byteWriter.writeUnsignedByte(0);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00ad, code lost:
        r1 = r1 + 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00c3, code lost:
        r1 = r1 + 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00da, code lost:
        r1 = r1 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int writeValues(mil.nga.tiff.p011io.ByteWriter r6, mil.nga.tiff.FileDirectoryEntry r7) throws java.io.IOException {
        /*
            long r0 = r7.getTypeCount()
            r2 = 1
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x0031
            mil.nga.tiff.FieldTagType r0 = r7.getFieldTag()
            boolean r0 = r0.isArray()
            if (r0 != 0) goto L_0x0031
            mil.nga.tiff.FieldType r0 = r7.getFieldType()
            mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.RATIONAL
            if (r0 == r1) goto L_0x0031
            mil.nga.tiff.FieldType r0 = r7.getFieldType()
            mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.SRATIONAL
            if (r0 == r1) goto L_0x0031
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.lang.Object r1 = r7.getValues()
            r0.add(r1)
            goto L_0x0037
        L_0x0031:
            java.lang.Object r0 = r7.getValues()
            java.util.List r0 = (java.util.List) r0
        L_0x0037:
            r1 = 0
            java.util.Iterator r0 = r0.iterator()
        L_0x003c:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x00fa
            java.lang.Object r2 = r0.next()
            int[] r3 = mil.nga.tiff.TiffWriter.C11841.$SwitchMap$mil$nga$tiff$FieldType
            mil.nga.tiff.FieldType r4 = r7.getFieldType()
            int r4 = r4.ordinal()
            r3 = r3[r4]
            switch(r3) {
                case 1: goto L_0x00de;
                case 2: goto L_0x00d1;
                case 3: goto L_0x00d1;
                case 4: goto L_0x00c7;
                case 5: goto L_0x00ba;
                case 6: goto L_0x00b0;
                case 7: goto L_0x00a4;
                case 8: goto L_0x009a;
                case 9: goto L_0x0090;
                case 10: goto L_0x0086;
                case 11: goto L_0x007c;
                case 12: goto L_0x0070;
                default: goto L_0x0055;
            }
        L_0x0055:
            mil.nga.tiff.util.TiffException r6 = new mil.nga.tiff.util.TiffException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Invalid field type: "
            r0.append(r1)
            mil.nga.tiff.FieldType r7 = r7.getFieldType()
            r0.append(r7)
            java.lang.String r7 = r0.toString()
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x0070:
            java.lang.Double r2 = (java.lang.Double) r2
            double r2 = r2.doubleValue()
            r6.writeDouble(r2)
            int r1 = r1 + 8
            goto L_0x003c
        L_0x007c:
            java.lang.Float r2 = (java.lang.Float) r2
            float r2 = r2.floatValue()
            r6.writeFloat(r2)
            goto L_0x00ad
        L_0x0086:
            java.lang.Integer r2 = (java.lang.Integer) r2
            int r2 = r2.intValue()
            r6.writeInt(r2)
            goto L_0x00ad
        L_0x0090:
            java.lang.Long r2 = (java.lang.Long) r2
            long r2 = r2.longValue()
            r6.writeUnsignedInt(r2)
            goto L_0x00ad
        L_0x009a:
            java.lang.Integer r2 = (java.lang.Integer) r2
            int r2 = r2.intValue()
            r6.writeInt(r2)
            goto L_0x00ad
        L_0x00a4:
            java.lang.Long r2 = (java.lang.Long) r2
            long r2 = r2.longValue()
            r6.writeUnsignedInt(r2)
        L_0x00ad:
            int r1 = r1 + 4
            goto L_0x003c
        L_0x00b0:
            java.lang.Short r2 = (java.lang.Short) r2
            short r2 = r2.shortValue()
            r6.writeShort(r2)
            goto L_0x00c3
        L_0x00ba:
            java.lang.Integer r2 = (java.lang.Integer) r2
            int r2 = r2.intValue()
            r6.writeUnsignedShort(r2)
        L_0x00c3:
            int r1 = r1 + 2
            goto L_0x003c
        L_0x00c7:
            java.lang.Byte r2 = (java.lang.Byte) r2
            byte r2 = r2.byteValue()
            r6.writeByte(r2)
            goto L_0x00da
        L_0x00d1:
            java.lang.Short r2 = (java.lang.Short) r2
            short r2 = r2.shortValue()
            r6.writeUnsignedByte(r2)
        L_0x00da:
            int r1 = r1 + 1
            goto L_0x003c
        L_0x00de:
            java.lang.String r2 = (java.lang.String) r2
            int r2 = r6.writeString(r2)
            int r1 = r1 + r2
            long r2 = (long) r1
            long r4 = r7.getTypeCount()
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 >= 0) goto L_0x003c
            long r4 = r7.getTypeCount()
            long r4 = r4 - r2
            writeFillerBytes(r6, r4)
            long r2 = r2 + r4
            int r1 = (int) r2
            goto L_0x003c
        L_0x00fa:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.tiff.TiffWriter.writeValues(mil.nga.tiff.io.ByteWriter, mil.nga.tiff.FileDirectoryEntry):int");
    }

    /* renamed from: mil.nga.tiff.TiffWriter$1 */
    static /* synthetic */ class C11841 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$tiff$FieldType;

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|26) */
        /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0084 */
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
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.ASCII     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.BYTE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.UNDEFINED     // Catch:{ NoSuchFieldError -> 0x0028 }
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
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.SHORT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x0049 }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.SSHORT     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x0054 }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.LONG     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x0060 }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.SLONG     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x006c }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.RATIONAL     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x0078 }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.SRATIONAL     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x0084 }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.FLOAT     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = $SwitchMap$mil$nga$tiff$FieldType     // Catch:{ NoSuchFieldError -> 0x0090 }
                mil.nga.tiff.FieldType r1 = mil.nga.tiff.FieldType.DOUBLE     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.tiff.TiffWriter.C11841.<clinit>():void");
        }
    }
}
