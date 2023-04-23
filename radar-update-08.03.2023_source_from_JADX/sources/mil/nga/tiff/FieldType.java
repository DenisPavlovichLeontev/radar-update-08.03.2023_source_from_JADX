package mil.nga.tiff;

import mil.nga.tiff.util.TiffException;

public enum FieldType {
    BYTE(1),
    ASCII(1),
    SHORT(2),
    LONG(4),
    RATIONAL(8),
    SBYTE(1),
    UNDEFINED(1),
    SSHORT(2),
    SLONG(4),
    SRATIONAL(8),
    FLOAT(4),
    DOUBLE(8);
    
    private final int bytes;

    private FieldType(int i) {
        this.bytes = i;
    }

    public int getValue() {
        return ordinal() + 1;
    }

    public int getBytes() {
        return this.bytes;
    }

    public int getBits() {
        return this.bytes * 8;
    }

    public static FieldType getFieldType(int i) {
        return values()[i - 1];
    }

    public static FieldType getFieldType(int i, int i2) {
        FieldType fieldType = null;
        if (i != 1) {
            if (i != 2) {
                if (i == 3) {
                    if (i2 == 32) {
                        fieldType = FLOAT;
                    } else if (i2 == 64) {
                        fieldType = DOUBLE;
                    }
                }
            } else if (i2 == 8) {
                fieldType = SBYTE;
            } else if (i2 == 16) {
                fieldType = SSHORT;
            } else if (i2 == 32) {
                fieldType = SLONG;
            }
        } else if (i2 == 8) {
            fieldType = BYTE;
        } else if (i2 == 16) {
            fieldType = SHORT;
        } else if (i2 == 32) {
            fieldType = LONG;
        }
        if (fieldType != null) {
            return fieldType;
        }
        throw new TiffException("Unsupported field type for sample format: " + i + ", bits per sample: " + i2);
    }

    /* renamed from: mil.nga.tiff.FieldType$1 */
    static /* synthetic */ class C11801 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$tiff$FieldType = null;

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
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.tiff.FieldType.C11801.<clinit>():void");
        }
    }

    public static int getSampleFormat(FieldType fieldType) {
        switch (C11801.$SwitchMap$mil$nga$tiff$FieldType[fieldType.ordinal()]) {
            case 1:
            case 2:
            case 3:
                return 1;
            case 4:
            case 5:
            case 6:
                return 2;
            case 7:
            case 8:
                return 3;
            default:
                throw new TiffException("Unsupported sample format for field type: " + fieldType);
        }
    }
}
