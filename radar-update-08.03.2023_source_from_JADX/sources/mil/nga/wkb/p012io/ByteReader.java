package mil.nga.wkb.p012io;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import mil.nga.wkb.util.WkbException;

/* renamed from: mil.nga.wkb.io.ByteReader */
public class ByteReader {
    private static final String CHAR_SET = "UTF-8";
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    private final byte[] bytes;
    private int nextByte = 0;

    public ByteReader(byte[] bArr) {
        this.bytes = bArr;
    }

    public int getNextByte() {
        return this.nextByte;
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public void setByteOrder(ByteOrder byteOrder2) {
        this.byteOrder = byteOrder2;
    }

    public String readString(int i) throws UnsupportedEncodingException {
        verifyRemainingBytes(i);
        String str = new String(this.bytes, this.nextByte, i, CHAR_SET);
        this.nextByte += i;
        return str;
    }

    public byte readByte() {
        verifyRemainingBytes(1);
        byte[] bArr = this.bytes;
        int i = this.nextByte;
        byte b = bArr[i];
        this.nextByte = i + 1;
        return b;
    }

    public int readInt() {
        verifyRemainingBytes(4);
        int i = ByteBuffer.wrap(this.bytes, this.nextByte, 4).order(this.byteOrder).getInt();
        this.nextByte += 4;
        return i;
    }

    public double readDouble() {
        verifyRemainingBytes(8);
        double d = ByteBuffer.wrap(this.bytes, this.nextByte, 8).order(this.byteOrder).getDouble();
        this.nextByte += 8;
        return d;
    }

    private void verifyRemainingBytes(int i) {
        if (this.nextByte + i > this.bytes.length) {
            throw new WkbException("No more remaining bytes to read. Total Bytes: " + this.bytes.length + ", Bytes already read: " + this.nextByte + ", Attempted to read: " + i);
        }
    }
}
