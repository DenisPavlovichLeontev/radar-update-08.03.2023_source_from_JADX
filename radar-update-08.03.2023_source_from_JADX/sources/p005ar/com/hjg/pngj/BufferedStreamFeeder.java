package p005ar.com.hjg.pngj;

import java.io.IOException;
import java.io.InputStream;

/* renamed from: ar.com.hjg.pngj.BufferedStreamFeeder */
public class BufferedStreamFeeder {
    private static final int DEFAULTSIZE = 8192;
    private byte[] buf;
    private boolean closeStream;
    private boolean eof;
    private boolean failIfNoFeed;
    private int offset;
    private int pendinglen;
    private InputStream stream;

    public BufferedStreamFeeder(InputStream inputStream) {
        this(inputStream, 8192);
    }

    public BufferedStreamFeeder(InputStream inputStream, int i) {
        this.eof = false;
        this.closeStream = true;
        this.failIfNoFeed = false;
        this.stream = inputStream;
        this.buf = new byte[(i < 1 ? 8192 : i)];
    }

    public InputStream getStream() {
        return this.stream;
    }

    public int feed(IBytesConsumer iBytesConsumer) {
        return feed(iBytesConsumer, -1);
    }

    public int feed(IBytesConsumer iBytesConsumer, int i) {
        int i2;
        if (this.pendinglen == 0) {
            refillBuffer();
        }
        if (i <= 0 || i >= this.pendinglen) {
            i = this.pendinglen;
        }
        if (i > 0) {
            i2 = iBytesConsumer.consume(this.buf, this.offset, i);
            if (i2 > 0) {
                this.offset += i2;
                this.pendinglen -= i2;
            }
        } else {
            i2 = 0;
        }
        if (i2 >= 1 || !this.failIfNoFeed) {
            return i2;
        }
        throw new PngjInputException("failed feed bytes");
    }

    public boolean feedFixed(IBytesConsumer iBytesConsumer, int i) {
        while (i > 0) {
            int feed = feed(iBytesConsumer, i);
            if (feed < 1) {
                return false;
            }
            i -= feed;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void refillBuffer() {
        if (this.pendinglen <= 0 && !this.eof) {
            try {
                this.offset = 0;
                int read = this.stream.read(this.buf);
                this.pendinglen = read;
                if (read < 0) {
                    close();
                }
            } catch (IOException e) {
                throw new PngjInputException((Throwable) e);
            }
        }
    }

    public boolean hasMoreToFeed() {
        if (!this.eof) {
            refillBuffer();
            if (this.pendinglen > 0) {
                return true;
            }
            return false;
        } else if (this.pendinglen > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setCloseStream(boolean z) {
        this.closeStream = z;
    }

    public void close() {
        this.eof = true;
        this.buf = null;
        this.pendinglen = 0;
        this.offset = 0;
        InputStream inputStream = this.stream;
        if (inputStream != null && this.closeStream) {
            try {
                inputStream.close();
            } catch (Exception unused) {
            }
        }
        this.stream = null;
    }

    public void setInputStream(InputStream inputStream) {
        this.stream = inputStream;
        this.eof = false;
    }

    public boolean isEof() {
        return this.eof;
    }

    public void setFailIfNoFeed(boolean z) {
        this.failIfNoFeed = z;
    }
}
