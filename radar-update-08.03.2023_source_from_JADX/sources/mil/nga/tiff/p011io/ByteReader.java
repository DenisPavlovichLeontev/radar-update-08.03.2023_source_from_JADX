package mil.nga.tiff.p011io;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import kotlin.UByte;
import kotlin.UShort;

/* renamed from: mil.nga.tiff.io.ByteReader */
public class ByteReader {
    private ByteOrder byteOrder;
    private final byte[] bytes;
    private int nextByte;

    public ByteReader(byte[] bArr) {
        this(bArr, ByteOrder.nativeOrder());
    }

    public ByteReader(byte[] bArr, ByteOrder byteOrder2) {
        this.nextByte = 0;
        this.bytes = bArr;
        this.byteOrder = byteOrder2;
    }

    public int getNextByte() {
        return this.nextByte;
    }

    public void setNextByte(int i) {
        this.nextByte = i;
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public void setByteOrder(ByteOrder byteOrder2) {
        this.byteOrder = byteOrder2;
    }

    public boolean hasByte() {
        return hasBytes(1);
    }

    public boolean hasByte(int i) {
        return hasBytes(i, 1);
    }

    public boolean hasBytes(int i) {
        return hasBytes(this.nextByte, i);
    }

    public boolean hasBytes(int i, int i2) {
        return i + i2 <= this.bytes.length;
    }

    public String readString(int i) throws UnsupportedEncodingException {
        String readString = readString(this.nextByte, i);
        this.nextByte += i;
        return readString;
    }

    public String readString(int i, int i2) throws UnsupportedEncodingException {
        verifyRemainingBytes(i, i2);
        if (i2 == 1 && this.bytes[i] == 0) {
            return null;
        }
        return new String(this.bytes, i, i2, StandardCharsets.US_ASCII);
    }

    public byte readByte() {
        byte readByte = readByte(this.nextByte);
        this.nextByte++;
        return readByte;
    }

    public byte readByte(int i) {
        verifyRemainingBytes(i, 1);
        return this.bytes[i];
    }

    public short readUnsignedByte() {
        short readUnsignedByte = readUnsignedByte(this.nextByte);
        this.nextByte++;
        return readUnsignedByte;
    }

    public short readUnsignedByte(int i) {
        return (short) (readByte(i) & UByte.MAX_VALUE);
    }

    public byte[] readBytes(int i) {
        byte[] readBytes = readBytes(this.nextByte, i);
        this.nextByte += i;
        return readBytes;
    }

    public byte[] readBytes(int i, int i2) {
        verifyRemainingBytes(i, i2);
        return Arrays.copyOfRange(this.bytes, i, i2 + i);
    }

    public short readShort() {
        short readShort = readShort(this.nextByte);
        this.nextByte += 2;
        return readShort;
    }

    public short readShort(int i) {
        verifyRemainingBytes(i, 2);
        return ByteBuffer.wrap(this.bytes, i, 2).order(this.byteOrder).getShort();
    }

    public int readUnsignedShort() {
        int readUnsignedShort = readUnsignedShort(this.nextByte);
        this.nextByte += 2;
        return readUnsignedShort;
    }

    public int readUnsignedShort(int i) {
        return readShort(i) & UShort.MAX_VALUE;
    }

    public int readInt() {
        int readInt = readInt(this.nextByte);
        this.nextByte += 4;
        return readInt;
    }

    public int readInt(int i) {
        verifyRemainingBytes(i, 4);
        return ByteBuffer.wrap(this.bytes, i, 4).order(this.byteOrder).getInt();
    }

    public long readUnsignedInt() {
        long readUnsignedInt = readUnsignedInt(this.nextByte);
        this.nextByte += 4;
        return readUnsignedInt;
    }

    public long readUnsignedInt(int i) {
        return ((long) readInt(i)) & 4294967295L;
    }

    public float readFloat() {
        float readFloat = readFloat(this.nextByte);
        this.nextByte += 4;
        return readFloat;
    }

    public float readFloat(int i) {
        verifyRemainingBytes(i, 4);
        return ByteBuffer.wrap(this.bytes, i, 4).order(this.byteOrder).getFloat();
    }

    public double readDouble() {
        double readDouble = readDouble(this.nextByte);
        this.nextByte += 8;
        return readDouble;
    }

    public double readDouble(int i) {
        verifyRemainingBytes(i, 8);
        return ByteBuffer.wrap(this.bytes, i, 8).order(this.byteOrder).getDouble();
    }

    public int byteLength() {
        return this.bytes.length;
    }

    private void verifyRemainingBytes(int i, int i2) {
        if (i + i2 > this.bytes.length) {
            throw new IllegalStateException("No more remaining bytes to read. Total Bytes: " + this.bytes.length + ", Byte offset: " + i + ", Attempted to read: " + i2);
        }
    }
}
