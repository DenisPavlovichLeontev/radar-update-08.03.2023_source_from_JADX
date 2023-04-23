package p005ar.com.hjg.pngj;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/* renamed from: ar.com.hjg.pngj.ProgressiveOutputStream */
abstract class ProgressiveOutputStream extends ByteArrayOutputStream {
    private long countFlushed = 0;
    private int size;

    /* access modifiers changed from: protected */
    public abstract void flushBuffer(byte[] bArr, int i);

    public ProgressiveOutputStream(int i) {
        this.size = i;
    }

    public final void close() throws IOException {
        try {
            flush();
        } catch (Exception unused) {
        }
        super.close();
    }

    public final void flush() throws IOException {
        super.flush();
        checkFlushBuffer(true);
    }

    public final void write(byte[] bArr, int i, int i2) {
        super.write(bArr, i, i2);
        checkFlushBuffer(false);
    }

    public final void write(byte[] bArr) throws IOException {
        super.write(bArr);
        checkFlushBuffer(false);
    }

    public final void write(int i) {
        super.write(i);
        checkFlushBuffer(false);
    }

    public final synchronized void reset() {
        super.reset();
    }

    private final void checkFlushBuffer(boolean z) {
        while (true) {
            if (z || this.count >= this.size) {
                int i = this.size;
                if (i > this.count) {
                    i = this.count;
                }
                if (i != 0) {
                    flushBuffer(this.buf, i);
                    this.countFlushed += (long) i;
                    int i2 = this.count - i;
                    this.count = i2;
                    if (i2 > 0) {
                        System.arraycopy(this.buf, i, this.buf, 0, i2);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void setSize(int i) {
        this.size = i;
        PrintStream printStream = System.out;
        printStream.println("setting size: " + i + " count" + this.count);
        checkFlushBuffer(false);
    }

    public long getCountFlushed() {
        return this.countFlushed;
    }
}
