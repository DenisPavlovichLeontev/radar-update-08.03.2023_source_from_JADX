package p005ar.com.hjg.pngj;

import java.util.ArrayList;
import java.util.List;
import p005ar.com.hjg.pngj.ChunkReader;
import p005ar.com.hjg.pngj.chunks.ChunkRaw;

/* renamed from: ar.com.hjg.pngj.ChunkSeqSkipping */
public class ChunkSeqSkipping extends ChunkSeqReader {
    private List<ChunkRaw> chunks;
    private boolean skip;

    /* access modifiers changed from: protected */
    public boolean isIdatKind(String str) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void processChunkContent(ChunkRaw chunkRaw, int i, byte[] bArr, int i2, int i3) {
    }

    public ChunkSeqSkipping(boolean z) {
        super(true);
        this.chunks = new ArrayList();
        this.skip = z;
    }

    public ChunkSeqSkipping() {
        this(true);
    }

    /* access modifiers changed from: protected */
    public ChunkReader createChunkReaderForNewChunk(String str, int i, long j, boolean z) {
        return new ChunkReader(i, str, j, z ? ChunkReader.ChunkReaderMode.SKIP : ChunkReader.ChunkReaderMode.PROCESS) {
            /* access modifiers changed from: protected */
            public void chunkDone() {
                ChunkSeqSkipping.this.postProcessChunk(this);
            }

            /* access modifiers changed from: protected */
            public void processData(int i, byte[] bArr, int i2, int i3) {
                ChunkSeqSkipping.this.processChunkContent(getChunkRaw(), i, bArr, i2, i3);
            }
        };
    }

    /* access modifiers changed from: protected */
    public void postProcessChunk(ChunkReader chunkReader) {
        super.postProcessChunk(chunkReader);
        this.chunks.add(chunkReader.getChunkRaw());
    }

    /* access modifiers changed from: protected */
    public boolean shouldSkipContent(int i, String str) {
        return this.skip;
    }

    public List<ChunkRaw> getChunks() {
        return this.chunks;
    }
}
