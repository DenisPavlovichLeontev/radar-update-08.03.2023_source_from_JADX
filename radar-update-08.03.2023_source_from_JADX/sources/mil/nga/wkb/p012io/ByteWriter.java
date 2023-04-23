package mil.nga.wkb.p012io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* renamed from: mil.nga.wkb.io.ByteWriter */
public class ByteWriter {
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

    /* renamed from: os */
    private final ByteArrayOutputStream f364os = new ByteArrayOutputStream();

    public void close() {
        try {
            this.f364os.close();
        } catch (IOException unused) {
        }
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public void setByteOrder(ByteOrder byteOrder2) {
        this.byteOrder = byteOrder2;
    }

    public byte[] getBytes() {
        return this.f364os.toByteArray();
    }

    public int size() {
        return this.f364os.size();
    }

    public void writeString(String str) throws IOException {
        this.f364os.write(str.getBytes());
    }

    public void writeByte(byte b) {
        this.f364os.write(b);
    }

    public void writeInt(int i) throws IOException {
        byte[] bArr = new byte[4];
        ByteBuffer putInt = ByteBuffer.allocate(4).order(this.byteOrder).putInt(i);
        putInt.flip();
        putInt.get(bArr);
        this.f364os.write(bArr);
    }

    public void writeDouble(double d) throws IOException {
        byte[] bArr = new byte[8];
        ByteBuffer putDouble = ByteBuffer.allocate(8).order(this.byteOrder).putDouble(d);
        putDouble.flip();
        putDouble.get(bArr);
        this.f364os.write(bArr);
    }
}
