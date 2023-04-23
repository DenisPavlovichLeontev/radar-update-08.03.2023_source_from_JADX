package p005ar.com.hjg.pngj.pixels;

import java.io.OutputStream;
import java.util.zip.Deflater;
import p005ar.com.hjg.pngj.PngjOutputException;

/* renamed from: ar.com.hjg.pngj.pixels.CompressorStreamLz4 */
public class CompressorStreamLz4 extends CompressorStream {
    private static final int MAX_BUFFER_SIZE = 16000;
    private byte[] buf;
    private final int buffer_size;
    private int inbuf;
    private final DeflaterEstimatorLz4 lz4;

    public CompressorStreamLz4(OutputStream outputStream, int i, long j) {
        super(outputStream, i, j);
        this.inbuf = 0;
        this.lz4 = new DeflaterEstimatorLz4();
        this.buffer_size = (int) (j > 16000 ? 16000 : j);
    }

    public CompressorStreamLz4(OutputStream outputStream, int i, long j, Deflater deflater) {
        this(outputStream, i, j);
    }

    public CompressorStreamLz4(OutputStream outputStream, int i, long j, int i2, int i3) {
        this(outputStream, i, j);
    }

    public void mywrite(byte[] bArr, int i, int i2) {
        if (i2 != 0) {
            if (this.done || this.closed) {
                throw new PngjOutputException("write beyond end of stream");
            }
            this.bytesIn += (long) i2;
            while (i2 > 0) {
                if (this.inbuf != 0 || (i2 < MAX_BUFFER_SIZE && this.bytesIn != this.totalbytes)) {
                    if (this.buf == null) {
                        this.buf = new byte[this.buffer_size];
                    }
                    int i3 = this.inbuf;
                    int i4 = i3 + i2;
                    int i5 = this.buffer_size;
                    int i6 = i4 <= i5 ? i2 : i5 - i3;
                    if (i6 > 0) {
                        System.arraycopy(bArr, i, this.buf, i3, i6);
                    }
                    int i7 = this.inbuf + i6;
                    this.inbuf = i7;
                    i2 -= i6;
                    i += i6;
                    if (i7 == this.buffer_size) {
                        compressFromBuffer();
                    }
                } else {
                    this.bytesOut += (long) this.lz4.compressEstim(bArr, i, i2);
                    i2 = 0;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void compressFromBuffer() {
        if (this.inbuf > 0) {
            this.bytesOut += (long) this.lz4.compressEstim(this.buf, 0, this.inbuf);
            this.inbuf = 0;
        }
    }

    public void done() {
        if (!this.done) {
            compressFromBuffer();
            this.done = true;
            flush();
        }
    }

    public void close() {
        done();
        if (!this.closed) {
            super.close();
            this.buf = null;
        }
    }

    public void reset() {
        done();
        super.reset();
    }
}
