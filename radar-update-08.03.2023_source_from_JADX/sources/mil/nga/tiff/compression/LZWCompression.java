package mil.nga.tiff.compression;

import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.nga.tiff.p011io.ByteReader;
import mil.nga.tiff.util.TiffException;

public class LZWCompression implements CompressionDecoder, CompressionEncoder {
    private static final int CLEAR_CODE = 256;
    private static final int EOI_CODE = 257;
    private static final int MIN_BITS = 9;
    private static final Logger logger = Logger.getLogger(LZWCompression.class.getName());
    private int byteLength;
    private int maxCode;
    private int position;
    private Map<Integer, Integer[]> table = new HashMap();

    public boolean rowEncoding() {
        return false;
    }

    public byte[] decode(byte[] bArr, ByteOrder byteOrder) {
        int i;
        ByteReader byteReader = new ByteReader(bArr, byteOrder);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        initializeTable();
        this.position = 0;
        int nextCode = getNextCode(byteReader);
        int i2 = 0;
        while (i != 257) {
            if (i == 256) {
                initializeTable();
                i = getNextCode(byteReader);
                while (i == 256) {
                    i = getNextCode(byteReader);
                }
                if (i == 257) {
                    break;
                } else if (i <= 256) {
                    writeValue(byteArrayOutputStream, this.table.get(Integer.valueOf(i)));
                } else {
                    throw new TiffException("Corrupted code at scan line: " + i);
                }
            } else {
                Integer[] numArr = this.table.get(Integer.valueOf(i));
                if (numArr != null) {
                    writeValue(byteArrayOutputStream, numArr);
                    addToTable(concat(this.table.get(Integer.valueOf(i2)), this.table.get(Integer.valueOf(i))[0]));
                } else {
                    Integer[] numArr2 = this.table.get(Integer.valueOf(i2));
                    Integer[] concat = concat(numArr2, numArr2[0]);
                    writeValue(byteArrayOutputStream, concat);
                    addToTable(i, concat);
                }
            }
            i2 = i;
            nextCode = getNextCode(byteReader);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void initializeTable() {
        this.table.clear();
        for (int i = 0; i <= 257; i++) {
            this.table.put(Integer.valueOf(i), new Integer[]{Integer.valueOf(i)});
        }
        this.maxCode = 257;
        this.byteLength = 9;
    }

    private void checkByteLength() {
        if (((double) this.maxCode) >= Math.pow(2.0d, (double) this.byteLength) - 2.0d) {
            this.byteLength++;
        }
    }

    private void addToTable(Integer[] numArr) {
        addToTable(this.maxCode + 1, numArr);
    }

    private void addToTable(int i, Integer[] numArr) {
        this.table.put(Integer.valueOf(i), numArr);
        this.maxCode = Math.max(this.maxCode, i);
        checkByteLength();
    }

    private Integer[] concat(Integer[] numArr, Integer num) {
        return concat(numArr, new Integer[]{num});
    }

    private Integer[] concat(Integer[] numArr, Integer[] numArr2) {
        Integer[] numArr3 = new Integer[(numArr.length + numArr2.length)];
        System.arraycopy(numArr, 0, numArr3, 0, numArr.length);
        System.arraycopy(numArr2, 0, numArr3, numArr.length, numArr2.length);
        return numArr3;
    }

    private void writeValue(ByteArrayOutputStream byteArrayOutputStream, Integer[] numArr) {
        for (Integer intValue : numArr) {
            byteArrayOutputStream.write(intValue.intValue());
        }
    }

    private int getNextCode(ByteReader byteReader) {
        int i = getByte(byteReader);
        this.position += this.byteLength;
        return i;
    }

    private int getByte(ByteReader byteReader) {
        int i = this.position;
        int i2 = i % 8;
        int floor = (int) Math.floor(((double) i) / 8.0d);
        int i3 = 8 - i2;
        int i4 = this.position;
        int i5 = this.byteLength;
        int i6 = floor + 1;
        int i7 = (i4 + i5) - (i6 * 8);
        int i8 = floor + 2;
        int i9 = i8 * 8;
        int i10 = i9 - (i5 + i4);
        int i11 = i9 - i4;
        int max = Math.max(0, i10);
        if (floor >= byteReader.byteLength()) {
            logger.log(Level.WARNING, "End of data reached without an end of input code");
            return 257;
        }
        int readUnsignedByte = (byteReader.readUnsignedByte(floor) & ((int) (Math.pow(2.0d, (double) i3) - 1.0d))) << (this.byteLength - i3);
        if (i6 < byteReader.byteLength()) {
            readUnsignedByte += (byteReader.readUnsignedByte(i6) >>> max) << Math.max(0, this.byteLength - i11);
        }
        if (i7 <= 8 || i8 >= byteReader.byteLength()) {
            return readUnsignedByte;
        }
        return readUnsignedByte + (byteReader.readUnsignedByte(i8) >>> (((floor + 3) * 8) - (this.position + this.byteLength)));
    }

    public byte[] encode(byte[] bArr, ByteOrder byteOrder) {
        throw new TiffException("LZW encoder is not yet implemented");
    }
}
