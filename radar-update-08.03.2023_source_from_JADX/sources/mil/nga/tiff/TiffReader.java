package mil.nga.tiff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import mil.nga.tiff.p011io.ByteReader;
import mil.nga.tiff.p011io.IOUtils;
import mil.nga.tiff.util.TiffConstants;
import mil.nga.tiff.util.TiffException;

public class TiffReader {
    public static TIFFImage readTiff(File file) throws IOException {
        return readTiff(file, false);
    }

    public static TIFFImage readTiff(File file, boolean z) throws IOException {
        return readTiff(IOUtils.fileBytes(file), z);
    }

    public static TIFFImage readTiff(InputStream inputStream) throws IOException {
        return readTiff(inputStream, false);
    }

    public static TIFFImage readTiff(InputStream inputStream, boolean z) throws IOException {
        return readTiff(IOUtils.streamBytes(inputStream), z);
    }

    public static TIFFImage readTiff(byte[] bArr) {
        return readTiff(bArr, false);
    }

    public static TIFFImage readTiff(byte[] bArr, boolean z) {
        return readTiff(new ByteReader(bArr), z);
    }

    public static TIFFImage readTiff(ByteReader byteReader) {
        return readTiff(byteReader, false);
    }

    public static TIFFImage readTiff(ByteReader byteReader, boolean z) {
        ByteOrder byteOrder;
        try {
            String readString = byteReader.readString(2);
            readString.hashCode();
            if (readString.equals(TiffConstants.BYTE_ORDER_LITTLE_ENDIAN)) {
                byteOrder = ByteOrder.LITTLE_ENDIAN;
            } else if (readString.equals(TiffConstants.BYTE_ORDER_BIG_ENDIAN)) {
                byteOrder = ByteOrder.BIG_ENDIAN;
            } else {
                throw new TiffException("Invalid byte order: " + readString);
            }
            byteReader.setByteOrder(byteOrder);
            if (byteReader.readUnsignedShort() == 42) {
                return parseTIFFImage(byteReader, (int) byteReader.readUnsignedInt(), z);
            }
            throw new TiffException("Invalid file identifier, not a TIFF");
        } catch (UnsupportedEncodingException e) {
            throw new TiffException("Failed to read byte order", e);
        }
    }

    private static TIFFImage parseTIFFImage(ByteReader byteReader, int i, boolean z) {
        TIFFImage tIFFImage = new TIFFImage();
        while (i != 0) {
            byteReader.setNextByte(i);
            TreeSet treeSet = new TreeSet();
            int readUnsignedShort = byteReader.readUnsignedShort();
            for (short s = 0; s < readUnsignedShort; s = (short) (s + 1)) {
                FieldTagType byId = FieldTagType.getById(byteReader.readUnsignedShort());
                FieldType fieldType = FieldType.getFieldType(byteReader.readUnsignedShort());
                long readUnsignedInt = byteReader.readUnsignedInt();
                int nextByte = byteReader.getNextByte();
                treeSet.add(new FileDirectoryEntry(byId, fieldType, readUnsignedInt, readFieldValues(byteReader, byId, fieldType, readUnsignedInt)));
                byteReader.setNextByte(nextByte + 4);
            }
            tIFFImage.add(new FileDirectory(treeSet, byteReader, z));
            i = (int) byteReader.readUnsignedInt();
        }
        return tIFFImage;
    }

    private static Object readFieldValues(ByteReader byteReader, FieldTagType fieldTagType, FieldType fieldType, long j) {
        if (((long) fieldType.getBytes()) * j > 4) {
            byteReader.setNextByte((int) byteReader.readUnsignedInt());
        }
        List<Object> values = getValues(byteReader, fieldType, j);
        return (j != 1 || fieldTagType.isArray() || fieldType == FieldType.RATIONAL || fieldType == FieldType.SRATIONAL) ? values : values.get(0);
    }

    private static List<Object> getValues(ByteReader byteReader, FieldType fieldType, long j) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; ((long) i) < j; i++) {
            switch (C11831.$SwitchMap$mil$nga$tiff$FieldType[fieldType.ordinal()]) {
                case 1:
                    try {
                        arrayList.add(byteReader.readString(1));
                        break;
                    } catch (UnsupportedEncodingException e) {
                        throw new TiffException("Failed to read ASCII character", e);
                    }
                case 2:
                case 3:
                    arrayList.add(Short.valueOf(byteReader.readUnsignedByte()));
                    break;
                case 4:
                    arrayList.add(Byte.valueOf(byteReader.readByte()));
                    break;
                case 5:
                    arrayList.add(Integer.valueOf(byteReader.readUnsignedShort()));
                    break;
                case 6:
                    arrayList.add(Short.valueOf(byteReader.readShort()));
                    break;
                case 7:
                    arrayList.add(Long.valueOf(byteReader.readUnsignedInt()));
                    break;
                case 8:
                    arrayList.add(Integer.valueOf(byteReader.readInt()));
                    break;
                case 9:
                    arrayList.add(Long.valueOf(byteReader.readUnsignedInt()));
                    arrayList.add(Long.valueOf(byteReader.readUnsignedInt()));
                    break;
                case 10:
                    arrayList.add(Integer.valueOf(byteReader.readInt()));
                    arrayList.add(Integer.valueOf(byteReader.readInt()));
                    break;
                case 11:
                    arrayList.add(Float.valueOf(byteReader.readFloat()));
                    break;
                case 12:
                    arrayList.add(Double.valueOf(byteReader.readDouble()));
                    break;
                default:
                    throw new TiffException("Invalid field type: " + fieldType);
            }
        }
        if (fieldType != FieldType.ASCII) {
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList();
        StringBuilder sb = new StringBuilder();
        for (Object next : arrayList) {
            if (next != null) {
                sb.append(next.toString());
            } else if (sb.length() > 0) {
                arrayList2.add(sb.toString());
                sb = new StringBuilder();
            }
        }
        return arrayList2;
    }

    /* renamed from: mil.nga.tiff.TiffReader$1 */
    static /* synthetic */ class C11831 {
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
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.tiff.TiffReader.C11831.<clinit>():void");
        }
    }
}
