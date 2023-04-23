package p005ar.com.hjg.pngj.pixels;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import p005ar.com.hjg.pngj.PngjOutputException;

/* renamed from: ar.com.hjg.pngj.pixels.CompressorStream */
public abstract class CompressorStream extends FilterOutputStream {
    protected int block = -1;
    public final int blockLen;
    protected long bytesIn = 0;
    protected long bytesOut = 0;
    boolean closed = false;
    protected boolean done = false;
    private byte[] firstBytes;

    /* renamed from: os */
    protected OutputStream f144os;
    protected boolean storeFirstByte = false;
    public final long totalbytes;

    public abstract void done();

    /* access modifiers changed from: protected */
    public abstract void mywrite(byte[] bArr, int i, int i2);

    public CompressorStream(OutputStream outputStream, int i, long j) {
        super(outputStream);
        i = i < 0 ? 4096 : i;
        j = j < 0 ? Long.MAX_VALUE : j;
        if (i < 1 || j < 1) {
            throw new RuntimeException(" maxBlockLen or totalLen invalid");
        }
        this.f144os = outputStream;
        this.blockLen = i;
        this.totalbytes = j;
    }

    public void close() {
        done();
        this.closed = true;
    }

    public final void write(byte[] bArr, int i, int i2) {
        this.block++;
        if (i2 <= this.blockLen) {
            mywrite(bArr, i, i2);
            if (this.storeFirstByte) {
                int i3 = this.block;
                byte[] bArr2 = this.firstBytes;
                if (i3 < bArr2.length) {
                    bArr2[i3] = bArr[i];
                }
            }
        } else {
            while (i2 > 0) {
                mywrite(bArr, i, this.blockLen);
                int i4 = this.blockLen;
                i += i4;
                i2 -= i4;
            }
        }
        if (this.bytesIn >= this.totalbytes) {
            done();
        }
    }

    public final void write(byte[] bArr) {
        write(bArr, 0, bArr.length);
    }

    public void write(int i) throws IOException {
        throw new PngjOutputException("should not be used");
    }

    public void reset() {
        reset(this.f144os);
    }

    public void reset(OutputStream outputStream) {
        if (!this.closed) {
            done();
            this.bytesIn = 0;
            this.bytesOut = 0;
            this.block = -1;
            this.done = false;
            this.f144os = outputStream;
            return;
        }
        throw new PngjOutputException("cannot reset, discarded object");
    }

    public final double getCompressionRatio() {
        long j = this.bytesOut;
        if (j == 0) {
            return 1.0d;
        }
        return ((double) j) / ((double) this.bytesIn);
    }

    public final long getBytesRaw() {
        return this.bytesIn;
    }

    public final long getBytesCompressed() {
        return this.bytesOut;
    }

    public OutputStream getOs() {
        return this.f144os;
    }

    public void flush() {
        OutputStream outputStream = this.f144os;
        if (outputStream != null) {
            try {
                outputStream.flush();
            } catch (IOException e) {
                throw new PngjOutputException((Throwable) e);
            }
        }
    }

    public boolean isClosed() {
        return this.closed;
    }

    public boolean isDone() {
        return this.done;
    }

    public byte[] getFirstBytes() {
        return this.firstBytes;
    }

    public void setStoreFirstByte(boolean z, int i) {
        this.storeFirstByte = z;
        if (z) {
            byte[] bArr = this.firstBytes;
            if (bArr == null || bArr.length < i) {
                this.firstBytes = new byte[i];
                return;
            }
            return;
        }
        this.firstBytes = null;
    }
}
