package mil.nga.tiff.p011io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* renamed from: mil.nga.tiff.io.ByteWriter */
public class ByteWriter {
    private ByteOrder byteOrder;

    /* renamed from: os */
    private final ByteArrayOutputStream f359os;

    public ByteWriter() {
        this(ByteOrder.nativeOrder());
    }

    public ByteWriter(ByteOrder byteOrder2) {
        this.f359os = new ByteArrayOutputStream();
        this.byteOrder = byteOrder2;
    }

    public void close() {
        try {
            this.f359os.close();
        } catch (IOException unused) {
        }
    }

    public ByteArrayOutputStream getOutputStream() {
        return this.f359os;
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public void setByteOrder(ByteOrder byteOrder2) {
        this.byteOrder = byteOrder2;
    }

    public byte[] getBytes() {
        return this.f359os.toByteArray();
    }

    public int size() {
        return this.f359os.size();
    }

    public int writeString(String str) throws IOException {
        byte[] bytes = str.getBytes();
        this.f359os.write(bytes);
        return bytes.length;
    }

    public void writeByte(byte b) {
        this.f359os.write(b);
    }

    public void writeUnsignedByte(short s) {
        this.f359os.write((byte) (s & 255));
    }

    public void writeBytes(byte[] bArr) throws IOException {
        this.f359os.write(bArr);
    }

    public void writeShort(short s) throws IOException {
        byte[] bArr = new byte[2];
        ByteBuffer putShort = ByteBuffer.allocate(2).order(this.byteOrder).putShort(s);
        putShort.flip();
        putShort.get(bArr);
        this.f359os.write(bArr);
    }

    public void writeUnsignedShort(int i) throws IOException {
        byte[] bArr = new byte[2];
        ByteBuffer putShort = ByteBuffer.allocate(2).order(this.byteOrder).putShort((short) (i & 65535));
        putShort.flip();
        putShort.get(bArr);
        this.f359os.write(bArr);
    }

    public void writeInt(int i) throws IOException {
        byte[] bArr = new byte[4];
        ByteBuffer putInt = ByteBuffer.allocate(4).order(this.byteOrder).putInt(i);
        putInt.flip();
        putInt.get(bArr);
        this.f359os.write(bArr);
    }

    public void writeUnsignedInt(long j) throws IOException {
        byte[] bArr = new byte[4];
        ByteBuffer putInt = ByteBuffer.allocate(4).order(this.byteOrder).putInt((int) (j & 4294967295L));
        putInt.flip();
        putInt.get(bArr);
        this.f359os.write(bArr);
    }

    public void writeFloat(float f) throws IOException {
        byte[] bArr = new byte[4];
        ByteBuffer putFloat = ByteBuffer.allocate(4).order(this.byteOrder).putFloat(f);
        putFloat.flip();
        putFloat.get(bArr);
        this.f359os.write(bArr);
    }

    public void writeDouble(double d) throws IOException {
        byte[] bArr = new byte[8];
        ByteBuffer putDouble = ByteBuffer.allocate(8).order(this.byteOrder).putDouble(d);
        putDouble.flip();
        putDouble.get(bArr);
        this.f359os.write(bArr);
    }
}
