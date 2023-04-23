package p005ar.com.hjg.pngj;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.PngReaderFilter */
public class PngReaderFilter extends FilterInputStream {
    private ChunkSeqReaderPng chunkseq = createChunkSequenceReader();

    public PngReaderFilter(InputStream inputStream) {
        super(inputStream);
    }

    /* access modifiers changed from: protected */
    public ChunkSeqReaderPng createChunkSequenceReader() {
        return new ChunkSeqReaderPng(true) {
            /* access modifiers changed from: protected */
            public boolean shouldCheckCrc(int i, String str) {
                return false;
            }

            public boolean shouldSkipContent(int i, String str) {
                return super.shouldSkipContent(i, str) || str.equals("IDAT");
            }

            /* access modifiers changed from: protected */
            public void postProcessChunk(ChunkReader chunkReader) {
                super.postProcessChunk(chunkReader);
            }
        };
    }

    public void close() throws IOException {
        super.close();
        this.chunkseq.close();
    }

    public int read() throws IOException {
        int read = super.read();
        if (read > 0) {
            this.chunkseq.feedAll(new byte[]{(byte) read}, 0, 1);
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = super.read(bArr, i, i2);
        if (read > 0) {
            this.chunkseq.feedAll(bArr, i, read);
        }
        return read;
    }

    public int read(byte[] bArr) throws IOException {
        int read = super.read(bArr);
        if (read > 0) {
            this.chunkseq.feedAll(bArr, 0, read);
        }
        return read;
    }

    public void readUntilEndAndClose() throws IOException {
        BufferedStreamFeeder bufferedStreamFeeder = new BufferedStreamFeeder(this.in);
        while (!this.chunkseq.isDone() && bufferedStreamFeeder.hasMoreToFeed()) {
            bufferedStreamFeeder.feed(this.chunkseq);
        }
        close();
    }

    public List<PngChunk> getChunksList() {
        return this.chunkseq.getChunks();
    }

    public ChunkSeqReaderPng getChunkseq() {
        return this.chunkseq;
    }
}
