package p005ar.com.hjg.pngj;

import p005ar.com.hjg.pngj.ChunkReader;

/* renamed from: ar.com.hjg.pngj.DeflatedChunkReader */
public class DeflatedChunkReader extends ChunkReader {
    protected boolean alsoBuffer = false;
    protected final DeflatedChunksSet deflatedChunksSet;

    /* access modifiers changed from: protected */
    public void chunkDone() {
    }

    public DeflatedChunkReader(int i, String str, boolean z, long j, DeflatedChunksSet deflatedChunksSet2) {
        super(i, str, j, ChunkReader.ChunkReaderMode.PROCESS);
        this.deflatedChunksSet = deflatedChunksSet2;
        deflatedChunksSet2.appendNewChunk(this);
    }

    /* access modifiers changed from: protected */
    public void processData(int i, byte[] bArr, int i2, int i3) {
        if (i3 > 0) {
            this.deflatedChunksSet.processBytes(bArr, i2, i3);
            if (this.alsoBuffer) {
                System.arraycopy(bArr, i2, getChunkRaw().data, this.read, i3);
            }
        }
    }

    public void setAlsoBuffer() {
        if (this.read <= 0) {
            this.alsoBuffer = true;
            getChunkRaw().allocData();
            return;
        }
        throw new RuntimeException("too late");
    }
}
