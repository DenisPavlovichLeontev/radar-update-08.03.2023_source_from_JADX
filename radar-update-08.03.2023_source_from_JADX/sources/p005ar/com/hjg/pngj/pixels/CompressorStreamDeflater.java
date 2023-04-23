package p005ar.com.hjg.pngj.pixels;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import p005ar.com.hjg.pngj.PngjOutputException;

/* renamed from: ar.com.hjg.pngj.pixels.CompressorStreamDeflater */
public class CompressorStreamDeflater extends CompressorStream {
    protected byte[] buf;
    protected Deflater deflater;
    protected boolean deflaterIsOwn;

    public CompressorStreamDeflater(OutputStream outputStream, int i, long j) {
        this(outputStream, i, j, (Deflater) null);
    }

    public CompressorStreamDeflater(OutputStream outputStream, int i, long j, Deflater deflater2) {
        super(outputStream, i, j);
        this.buf = new byte[4092];
        boolean z = true;
        this.deflaterIsOwn = true;
        this.deflater = deflater2 == null ? new Deflater() : deflater2;
        this.deflaterIsOwn = deflater2 != null ? false : z;
    }

    public CompressorStreamDeflater(OutputStream outputStream, int i, long j, int i2, int i3) {
        this(outputStream, i, j, new Deflater(i2));
        this.deflaterIsOwn = true;
        this.deflater.setStrategy(i3);
    }

    public void mywrite(byte[] bArr, int i, int i2) {
        if (this.deflater.finished() || this.done || this.closed) {
            throw new PngjOutputException("write beyond end of stream");
        }
        this.deflater.setInput(bArr, i, i2);
        this.bytesIn += (long) i2;
        while (!this.deflater.needsInput()) {
            deflate();
        }
    }

    /* access modifiers changed from: protected */
    public void deflate() {
        Deflater deflater2 = this.deflater;
        byte[] bArr = this.buf;
        int deflate = deflater2.deflate(bArr, 0, bArr.length);
        if (deflate > 0) {
            this.bytesOut += (long) deflate;
            try {
                if (this.f144os != null) {
                    this.f144os.write(this.buf, 0, deflate);
                }
            } catch (IOException e) {
                throw new PngjOutputException((Throwable) e);
            }
        }
    }

    public void done() {
        if (!this.done) {
            if (!this.deflater.finished()) {
                this.deflater.finish();
                while (!this.deflater.finished()) {
                    deflate();
                }
            }
            this.done = true;
            flush();
        }
    }

    public void close() {
        done();
        try {
            if (this.deflaterIsOwn) {
                this.deflater.end();
            }
        } catch (Exception unused) {
        }
        super.close();
    }

    public void reset() {
        super.reset();
        this.deflater.reset();
    }
}
