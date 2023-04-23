package mil.nga.tiff;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.UByte;
import kotlin.UShort;
import mil.nga.tiff.util.TiffConstants;
import mil.nga.tiff.util.TiffException;

public class Rasters {
    private List<Integer> bitsPerSample;
    private final FieldType[] fieldTypes;
    private final int height;
    private ByteBuffer interleaveValues;
    private Integer pixelSize;
    private List<Integer> sampleFormat;
    private ByteBuffer[] sampleValues;
    private final int width;

    public Rasters(int i, int i2, FieldType[] fieldTypeArr, ByteBuffer[] byteBufferArr) {
        this(i, i2, fieldTypeArr, byteBufferArr, (ByteBuffer) null);
    }

    public Rasters(int i, int i2, FieldType[] fieldTypeArr, ByteBuffer byteBuffer) {
        this(i, i2, fieldTypeArr, (ByteBuffer[]) null, byteBuffer);
    }

    public Rasters(int i, int i2, FieldType[] fieldTypeArr, ByteBuffer[] byteBufferArr, ByteBuffer byteBuffer) {
        this.width = i;
        this.height = i2;
        this.fieldTypes = fieldTypeArr;
        this.sampleValues = byteBufferArr;
        this.interleaveValues = byteBuffer;
        validateValues();
    }

    public Rasters(int i, int i2, int i3, FieldType fieldType) {
        this(i, i2, createFieldTypeArray(i3, fieldType));
    }

    public Rasters(int i, int i2, int i3, FieldType fieldType, ByteOrder byteOrder) {
        this(i, i2, createFieldTypeArray(i3, fieldType), byteOrder);
    }

    public Rasters(int i, int i2, int[] iArr, int[] iArr2) {
        this(i, i2, createFieldTypeArray(iArr, iArr2));
    }

    public Rasters(int i, int i2, int[] iArr, int[] iArr2, ByteOrder byteOrder) {
        this(i, i2, createFieldTypeArray(iArr, iArr2), byteOrder);
    }

    public Rasters(int i, int i2, int i3, int i4, int i5) {
        this(i, i2, i3, FieldType.getFieldType(i5, i4));
    }

    public Rasters(int i, int i2, int i3, int i4, int i5, ByteOrder byteOrder) {
        this(i, i2, i3, FieldType.getFieldType(i5, i4), byteOrder);
    }

    public Rasters(int i, int i2, FieldType[] fieldTypeArr) {
        this(i, i2, fieldTypeArr, ByteOrder.nativeOrder());
    }

    public Rasters(int i, int i2, FieldType[] fieldTypeArr, ByteOrder byteOrder) {
        this(i, i2, fieldTypeArr, new ByteBuffer[fieldTypeArr.length]);
        int i3 = 0;
        while (true) {
            ByteBuffer[] byteBufferArr = this.sampleValues;
            if (i3 < byteBufferArr.length) {
                byteBufferArr[i3] = ByteBuffer.allocateDirect(i * i2 * fieldTypeArr[i3].getBytes()).order(byteOrder);
                i3++;
            } else {
                return;
            }
        }
    }

    private void validateValues() {
        if (this.sampleValues == null && this.interleaveValues == null) {
            throw new TiffException("Results must be sample and/or interleave based");
        }
    }

    private static FieldType[] createFieldTypeArray(int i, FieldType fieldType) {
        FieldType[] fieldTypeArr = new FieldType[i];
        Arrays.fill(fieldTypeArr, fieldType);
        return fieldTypeArr;
    }

    private static FieldType[] createFieldTypeArray(int[] iArr, int[] iArr2) {
        if (iArr.length == iArr2.length) {
            FieldType[] fieldTypeArr = new FieldType[iArr.length];
            for (int i = 0; i < iArr.length; i++) {
                fieldTypeArr[i] = FieldType.getFieldType(iArr2[i], iArr[i]);
            }
            return fieldTypeArr;
        }
        throw new TiffException("Equal number of bits per samples and sample formats expected. Bits Per Samples: " + iArr + ", Sample Formats: " + iArr2);
    }

    public boolean hasSampleValues() {
        return this.sampleValues != null;
    }

    public boolean hasInterleaveValues() {
        return this.interleaveValues != null;
    }

    private void updateSampleInByteBuffer(ByteBuffer byteBuffer, int i, int i2, Number number) {
        if (i < 0 || i >= byteBuffer.capacity()) {
            throw new IndexOutOfBoundsException("index: " + i + ". Buffer capacity: " + byteBuffer.capacity());
        }
        byteBuffer.position(i);
        writeSample(byteBuffer, this.fieldTypes[i2], number);
    }

    private Number getSampleFromByteBuffer(ByteBuffer byteBuffer, int i, int i2) {
        if (i < 0 || i >= byteBuffer.capacity()) {
            throw new IndexOutOfBoundsException("Requested index: " + i + ", but size of buffer is: " + byteBuffer.capacity());
        }
        byteBuffer.position(i);
        return readSample(byteBuffer, this.fieldTypes[i2]);
    }

    public void addToSample(int i, int i2, Number number) {
        updateSampleInByteBuffer(this.sampleValues[i], i2 * this.fieldTypes[i].getBytes(), i, number);
    }

    public void addToInterleave(int i, int i2, Number number) {
        int sizePixel = i2 * sizePixel();
        for (int i3 = 0; i3 < i; i3++) {
            sizePixel += this.fieldTypes[i3].getBytes();
        }
        updateSampleInByteBuffer(this.interleaveValues, sizePixel, i, number);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getNumPixels() {
        return this.width * this.height;
    }

    public int getSamplesPerPixel() {
        return this.fieldTypes.length;
    }

    public List<Integer> getBitsPerSample() {
        List<Integer> list = this.bitsPerSample;
        if (list == null) {
            list = new ArrayList<>(this.fieldTypes.length);
            for (FieldType bits : this.fieldTypes) {
                list.add(Integer.valueOf(bits.getBits()));
            }
            this.bitsPerSample = list;
        }
        return list;
    }

    public List<Integer> getSampleFormat() {
        List<Integer> list = this.sampleFormat;
        if (list == null) {
            list = new ArrayList<>(this.fieldTypes.length);
            for (FieldType sampleFormat2 : this.fieldTypes) {
                list.add(Integer.valueOf(FieldType.getSampleFormat(sampleFormat2)));
            }
            this.sampleFormat = list;
        }
        return list;
    }

    public ByteBuffer[] getSampleValues() {
        int i = 0;
        while (true) {
            ByteBuffer[] byteBufferArr = this.sampleValues;
            if (i >= byteBufferArr.length) {
                return byteBufferArr;
            }
            byteBufferArr[i].rewind();
            i++;
        }
    }

    public void setSampleValues(ByteBuffer[] byteBufferArr) {
        this.sampleValues = byteBufferArr;
        this.sampleFormat = null;
        this.bitsPerSample = null;
        this.pixelSize = null;
        validateValues();
    }

    public ByteBuffer getInterleaveValues() {
        this.interleaveValues.rewind();
        return this.interleaveValues;
    }

    public void setInterleaveValues(ByteBuffer byteBuffer) {
        this.interleaveValues = byteBuffer;
        validateValues();
    }

    public Number[] getPixel(int i, int i2) {
        validateCoordinates(i, i2);
        Number[] numberArr = new Number[getSamplesPerPixel()];
        int i3 = 0;
        if (this.sampleValues != null) {
            int sampleIndex = getSampleIndex(i, i2);
            while (i3 < getSamplesPerPixel()) {
                numberArr[i3] = getSampleFromByteBuffer(this.sampleValues[i3], this.fieldTypes[i3].getBytes() * sampleIndex, i3);
                i3++;
            }
        } else {
            int interleaveIndex = getInterleaveIndex(i, i2);
            while (i3 < getSamplesPerPixel()) {
                numberArr[i3] = getSampleFromByteBuffer(this.interleaveValues, interleaveIndex, i3);
                interleaveIndex += this.fieldTypes[i3].getBytes();
                i3++;
            }
        }
        return numberArr;
    }

    public void setPixel(int i, int i2, Number[] numberArr) {
        validateCoordinates(i, i2);
        validateSample(numberArr.length + 1);
        int i3 = 0;
        if (this.sampleValues != null) {
            while (i3 < getSamplesPerPixel()) {
                updateSampleInByteBuffer(this.sampleValues[i3], getSampleIndex(i, i2) * this.fieldTypes[i3].getBytes(), i3, numberArr[i3]);
                i3++;
            }
            return;
        }
        int sampleIndex = getSampleIndex(i, i2) * sizePixel();
        while (i3 < getSamplesPerPixel()) {
            updateSampleInByteBuffer(this.interleaveValues, sampleIndex, i3, numberArr[i3]);
            sampleIndex += this.fieldTypes[i3].getBytes();
            i3++;
        }
    }

    public byte[] getPixelRow(int i, ByteOrder byteOrder) {
        ByteBuffer allocate = ByteBuffer.allocate(getWidth() * sizePixel());
        allocate.order(byteOrder);
        if (this.sampleValues != null) {
            for (int i2 = 0; i2 < getSamplesPerPixel(); i2++) {
                this.sampleValues[i2].position(getWidth() * i * this.fieldTypes[i2].getBytes());
            }
            for (int i3 = 0; i3 < getWidth(); i3++) {
                for (int i4 = 0; i4 < getSamplesPerPixel(); i4++) {
                    writeSample(allocate, this.sampleValues[i4], this.fieldTypes[i4]);
                }
            }
        } else {
            this.interleaveValues.position(i * getWidth() * sizePixel());
            for (int i5 = 0; i5 < getWidth(); i5++) {
                for (int i6 = 0; i6 < getSamplesPerPixel(); i6++) {
                    writeSample(allocate, this.interleaveValues, this.fieldTypes[i6]);
                }
            }
        }
        return allocate.array();
    }

    public byte[] getSampleRow(int i, int i2, ByteOrder byteOrder) {
        ByteBuffer allocate = ByteBuffer.allocate(getWidth() * this.fieldTypes[i2].getBytes());
        allocate.order(byteOrder);
        ByteBuffer[] byteBufferArr = this.sampleValues;
        int i3 = 0;
        if (byteBufferArr != null) {
            byteBufferArr[i2].position(i * getWidth() * this.fieldTypes[i2].getBytes());
            while (i3 < getWidth()) {
                writeSample(allocate, this.sampleValues[i2], this.fieldTypes[i2]);
                i3++;
            }
        } else {
            int i4 = 0;
            for (int i5 = 0; i5 < i2; i5++) {
                i4 += this.fieldTypes[i2].getBytes();
            }
            while (i3 < getWidth()) {
                this.interleaveValues.position((((getWidth() * i) + i3) * sizePixel()) + i4);
                writeSample(allocate, this.interleaveValues, this.fieldTypes[i2]);
                i3++;
            }
        }
        return allocate.array();
    }

    public Number getPixelSample(int i, int i2, int i3) {
        validateCoordinates(i2, i3);
        validateSample(i);
        if (this.sampleValues != null) {
            return getSampleFromByteBuffer(this.sampleValues[i], getSampleIndex(i2, i3) * this.fieldTypes[i].getBytes(), i);
        }
        int interleaveIndex = getInterleaveIndex(i2, i3);
        for (int i4 = 0; i4 < i; i4++) {
            interleaveIndex += this.fieldTypes[i].getBytes();
        }
        return getSampleFromByteBuffer(this.interleaveValues, interleaveIndex, i);
    }

    public void setPixelSample(int i, int i2, int i3, Number number) {
        validateCoordinates(i2, i3);
        validateSample(i);
        if (this.sampleValues != null) {
            updateSampleInByteBuffer(this.sampleValues[i], getSampleIndex(i2, i3) * this.fieldTypes[i].getBytes(), i, number);
        }
        if (this.interleaveValues != null) {
            int sampleIndex = getSampleIndex(i2, i3) * sizePixel();
            for (int i4 = 0; i4 < i; i4++) {
                sampleIndex += this.fieldTypes[i].getBytes();
            }
            updateSampleInByteBuffer(this.interleaveValues, sampleIndex, i, number);
        }
    }

    public Number getFirstPixelSample(int i, int i2) {
        return getPixelSample(0, i, i2);
    }

    public void setFirstPixelSample(int i, int i2, Number number) {
        setPixelSample(0, i, i2, number);
    }

    public int getSampleIndex(int i, int i2) {
        return (i2 * this.width) + i;
    }

    public int getInterleaveIndex(int i, int i2) {
        return (i2 * this.width * sizePixel()) + (i * sizePixel());
    }

    public int size() {
        return getNumPixels() * sizePixel();
    }

    public int sizePixel() {
        Integer num = this.pixelSize;
        if (num != null) {
            return num.intValue();
        }
        int i = 0;
        for (int i2 = 0; i2 < getSamplesPerPixel(); i2++) {
            i += this.fieldTypes[i2].getBytes();
        }
        this.pixelSize = Integer.valueOf(i);
        return i;
    }

    private void validateCoordinates(int i, int i2) {
        if (i < 0 || i >= this.width || i2 < 0 || i2 > this.height) {
            throw new TiffException("Pixel oustide of raster range. Width: " + this.width + ", Height: " + this.height + ", x: " + i + ", y: " + i2);
        }
    }

    private void validateSample(int i) {
        if (i < 0 || i >= getSamplesPerPixel()) {
            throw new TiffException("Pixel sample out of bounds. sample: " + i + ", samples per pixel: " + getSamplesPerPixel());
        }
    }

    public int calculateRowsPerStrip(int i) {
        return calculateRowsPerStrip(i, TiffConstants.DEFAULT_MAX_BYTES_PER_STRIP);
    }

    public int calculateRowsPerStrip(int i, int i2) {
        Integer num;
        if (i == 1) {
            num = Integer.valueOf(rowsPerStrip(sizePixel(), i2));
        } else {
            Integer num2 = null;
            for (int i3 = 0; i3 < getSamplesPerPixel(); i3++) {
                int rowsPerStrip = rowsPerStrip(this.fieldTypes[i3].getBytes(), i2);
                if (num2 == null || rowsPerStrip < num2.intValue()) {
                    num2 = Integer.valueOf(rowsPerStrip);
                }
            }
            num = num2;
        }
        return num.intValue();
    }

    private int rowsPerStrip(int i, int i2) {
        return Math.max(1, i2 / (i * this.width));
    }

    /* renamed from: mil.nga.tiff.Rasters$1 */
    static /* synthetic */ class C11821 {
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
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.tiff.Rasters.C11821.<clinit>():void");
        }
    }

    private Number readSample(ByteBuffer byteBuffer, FieldType fieldType) {
        switch (C11821.$SwitchMap$mil$nga$tiff$FieldType[fieldType.ordinal()]) {
            case 1:
                return Short.valueOf((short) (byteBuffer.get() & UByte.MAX_VALUE));
            case 2:
                return Integer.valueOf(byteBuffer.getShort() & UShort.MAX_VALUE);
            case 3:
                return Long.valueOf(((long) byteBuffer.getInt()) & 4294967295L);
            case 4:
                return Byte.valueOf(byteBuffer.get());
            case 5:
                return Short.valueOf(byteBuffer.getShort());
            case 6:
                return Integer.valueOf(byteBuffer.getInt());
            case 7:
                return Float.valueOf(byteBuffer.getFloat());
            case 8:
                return Double.valueOf(byteBuffer.getDouble());
            default:
                throw new TiffException("Unsupported raster field type: " + fieldType);
        }
    }

    private void writeSample(ByteBuffer byteBuffer, FieldType fieldType, Number number) {
        switch (C11821.$SwitchMap$mil$nga$tiff$FieldType[fieldType.ordinal()]) {
            case 1:
            case 4:
                byteBuffer.put(number.byteValue());
                return;
            case 2:
            case 5:
                byteBuffer.putShort(number.shortValue());
                return;
            case 3:
            case 6:
                byteBuffer.putInt(number.intValue());
                return;
            case 7:
                byteBuffer.putFloat(number.floatValue());
                return;
            case 8:
                byteBuffer.putDouble(number.doubleValue());
                return;
            default:
                throw new TiffException("Unsupported raster field type: " + fieldType);
        }
    }

    private void writeSample(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, FieldType fieldType) {
        switch (C11821.$SwitchMap$mil$nga$tiff$FieldType[fieldType.ordinal()]) {
            case 1:
            case 4:
                byteBuffer.put(byteBuffer2.get());
                return;
            case 2:
            case 5:
                byteBuffer.putShort(byteBuffer2.getShort());
                return;
            case 3:
            case 6:
                byteBuffer.putInt(byteBuffer2.getInt());
                return;
            case 7:
                byteBuffer.putFloat(byteBuffer2.getFloat());
                return;
            case 8:
                byteBuffer.putDouble(byteBuffer2.getDouble());
                return;
            default:
                throw new TiffException("Unsupported raster field type: " + fieldType);
        }
    }

    public FieldType[] getFieldTypes() {
        return this.fieldTypes;
    }
}
