package p005ar.com.hjg.pngj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import p005ar.com.hjg.pngj.ChunkReader;
import p005ar.com.hjg.pngj.chunks.ChunkHelper;

/* renamed from: ar.com.hjg.pngj.ChunkSeqReader */
public class ChunkSeqReader implements IBytesConsumer {
    protected static final int SIGNATURE_LEN = 8;
    private byte[] buf0;
    private int buf0len;
    private long bytesCount;
    private int chunkCount;
    private ChunkReader curChunkReader;
    private DeflatedChunksSet curReaderDeflatedSet;
    private boolean done;
    private long idatBytes;
    private boolean signatureDone;
    protected final boolean withSignature;

    /* access modifiers changed from: protected */
    public String endChunkId() {
        return "IEND";
    }

    /* access modifiers changed from: protected */
    public String firstChunkId() {
        return "IHDR";
    }

    /* access modifiers changed from: protected */
    public boolean isIdatKind(String str) {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean shouldCheckCrc(int i, String str) {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean shouldSkipContent(int i, String str) {
        return false;
    }

    public ChunkSeqReader() {
        this(true);
    }

    public ChunkSeqReader(boolean z) {
        this.buf0 = new byte[8];
        this.buf0len = 0;
        this.done = false;
        this.chunkCount = 0;
        this.bytesCount = 0;
        this.withSignature = z;
        this.signatureDone = !z;
    }

    public int consume(byte[] bArr, int i, int i2) {
        if (this.done) {
            return -1;
        }
        if (i2 == 0) {
            return 0;
        }
        if (i2 < 0) {
            throw new PngjInputException("Bad len: " + i2);
        } else if (this.signatureDone) {
            ChunkReader chunkReader = this.curChunkReader;
            if (chunkReader == null || chunkReader.isDone()) {
                int i3 = this.buf0len;
                int i4 = 8 - i3;
                if (i4 <= i2) {
                    i2 = i4;
                }
                System.arraycopy(bArr, i, this.buf0, i3, i2);
                int i5 = this.buf0len + i2;
                this.buf0len = i5;
                int i6 = 0 + i2;
                this.bytesCount += (long) i2;
                if (i5 != 8) {
                    return i6;
                }
                this.chunkCount++;
                startNewChunk(PngHelperInternal.readInt4fromBytes(this.buf0, 0), ChunkHelper.toString(this.buf0, 4, 4), this.bytesCount - 8);
                this.buf0len = 0;
                return i6;
            }
            int feedBytes = this.curChunkReader.feedBytes(bArr, i, i2);
            int i7 = feedBytes + 0;
            this.bytesCount += (long) feedBytes;
            return i7;
        } else {
            int i8 = this.buf0len;
            int i9 = 8 - i8;
            if (i9 <= i2) {
                i2 = i9;
            }
            System.arraycopy(bArr, i, this.buf0, i8, i2);
            int i10 = this.buf0len + i2;
            this.buf0len = i10;
            if (i10 == 8) {
                checkSignature(this.buf0);
                this.buf0len = 0;
                this.signatureDone = true;
            }
            int i11 = 0 + i2;
            this.bytesCount += (long) i2;
            return i11;
        }
    }

    public boolean feedAll(byte[] bArr, int i, int i2) {
        while (i2 > 0) {
            int consume = consume(bArr, i, i2);
            if (consume < 1) {
                return false;
            }
            i2 -= consume;
            i += consume;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void startNewChunk(int i, String str, long j) {
        if (str.equals("IDAT")) {
            this.idatBytes += (long) i;
        }
        boolean shouldCheckCrc = shouldCheckCrc(i, str);
        boolean shouldSkipContent = shouldSkipContent(i, str);
        boolean isIdatKind = isIdatKind(str);
        DeflatedChunksSet deflatedChunksSet = this.curReaderDeflatedSet;
        boolean ackNextChunkId = deflatedChunksSet != null ? deflatedChunksSet.ackNextChunkId(str) : false;
        if (!isIdatKind || shouldSkipContent) {
            ChunkReader createChunkReaderForNewChunk = createChunkReaderForNewChunk(str, i, j, shouldSkipContent);
            this.curChunkReader = createChunkReaderForNewChunk;
            if (!shouldCheckCrc) {
                createChunkReaderForNewChunk.setCrcCheck(false);
                return;
            }
            return;
        }
        if (!ackNextChunkId) {
            if (this.curReaderDeflatedSet == null) {
                this.curReaderDeflatedSet = createIdatSet(str);
            } else {
                throw new PngjInputException("too many IDAT (or idatlike) chunks");
            }
        }
        this.curChunkReader = new DeflatedChunkReader(i, str, shouldCheckCrc, j, this.curReaderDeflatedSet) {
            /* access modifiers changed from: protected */
            public void chunkDone() {
                ChunkSeqReader.this.postProcessChunk(this);
            }
        };
    }

    /* access modifiers changed from: protected */
    public ChunkReader createChunkReaderForNewChunk(String str, int i, long j, boolean z) {
        return new ChunkReader(i, str, j, z ? ChunkReader.ChunkReaderMode.SKIP : ChunkReader.ChunkReaderMode.BUFFER) {
            /* access modifiers changed from: protected */
            public void chunkDone() {
                ChunkSeqReader.this.postProcessChunk(this);
            }

            /* access modifiers changed from: protected */
            public void processData(int i, byte[] bArr, int i2, int i3) {
                throw new PngjExceptionInternal("should never happen");
            }
        };
    }

    /* access modifiers changed from: protected */
    public void postProcessChunk(ChunkReader chunkReader) {
        String firstChunkId;
        if (this.chunkCount == 1 && (firstChunkId = firstChunkId()) != null && !firstChunkId.equals(chunkReader.getChunkRaw().f121id)) {
            throw new PngjInputException("Bad first chunk: " + chunkReader.getChunkRaw().f121id + " expected: " + firstChunkId());
        } else if (chunkReader.getChunkRaw().f121id.equals(endChunkId())) {
            this.done = true;
        }
    }

    /* access modifiers changed from: protected */
    public DeflatedChunksSet createIdatSet(String str) {
        return new DeflatedChunksSet(str, 1024, 1024);
    }

    /* access modifiers changed from: protected */
    public void checkSignature(byte[] bArr) {
        if (!Arrays.equals(bArr, PngHelperInternal.getPngIdSignature())) {
            throw new PngjInputException("Bad PNG signature");
        }
    }

    public boolean isSignatureDone() {
        return this.signatureDone;
    }

    public boolean isDone() {
        return this.done;
    }

    public long getBytesCount() {
        return this.bytesCount;
    }

    public int getChunkCount() {
        return this.chunkCount;
    }

    public ChunkReader getCurChunkReader() {
        return this.curChunkReader;
    }

    public DeflatedChunksSet getCurReaderDeflatedSet() {
        return this.curReaderDeflatedSet;
    }

    public void close() {
        DeflatedChunksSet deflatedChunksSet = this.curReaderDeflatedSet;
        if (deflatedChunksSet != null) {
            deflatedChunksSet.close();
        }
        this.done = true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0012, code lost:
        r0 = r4.curChunkReader;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isAtChunkBoundary() {
        /*
            r4 = this;
            long r0 = r4.bytesCount
            r2 = 0
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 == 0) goto L_0x001f
            r2 = 8
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 == 0) goto L_0x001f
            boolean r0 = r4.done
            if (r0 != 0) goto L_0x001f
            ar.com.hjg.pngj.ChunkReader r0 = r4.curChunkReader
            if (r0 == 0) goto L_0x001f
            boolean r0 = r0.isDone()
            if (r0 == 0) goto L_0x001d
            goto L_0x001f
        L_0x001d:
            r0 = 0
            goto L_0x0020
        L_0x001f:
            r0 = 1
        L_0x0020:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.ChunkSeqReader.isAtChunkBoundary():boolean");
    }

    public long getIdatBytes() {
        return this.idatBytes;
    }

    public void feedFromFile(File file) {
        try {
            feedFromInputStream(new FileInputStream(file), true);
        } catch (FileNotFoundException e) {
            throw new PngjInputException(e.getMessage());
        }
    }

    public void feedFromInputStream(InputStream inputStream, boolean z) {
        BufferedStreamFeeder bufferedStreamFeeder = new BufferedStreamFeeder(inputStream);
        bufferedStreamFeeder.setCloseStream(z);
        while (bufferedStreamFeeder.hasMoreToFeed()) {
            try {
                bufferedStreamFeeder.feed(this);
            } finally {
                close();
                bufferedStreamFeeder.close();
            }
        }
    }

    public void feedFromInputStream(InputStream inputStream) {
        feedFromInputStream(inputStream, true);
    }
}
