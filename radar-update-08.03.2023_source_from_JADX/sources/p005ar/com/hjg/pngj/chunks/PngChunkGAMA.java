package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkGAMA */
public class PngChunkGAMA extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f126ID = "gAMA";
    private double gamma;

    public PngChunkGAMA(ImageInfo imageInfo) {
        super("gAMA", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_PLTE_AND_IDAT;
    }

    public ChunkRaw createRawChunk() {
        ChunkRaw createEmptyChunk = createEmptyChunk(4, true);
        PngHelperInternal.writeInt4tobytes((int) ((this.gamma * 100000.0d) + 0.5d), createEmptyChunk.data, 0);
        return createEmptyChunk;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        if (chunkRaw.len == 4) {
            this.gamma = ((double) PngHelperInternal.readInt4fromBytes(chunkRaw.data, 0)) / 100000.0d;
            return;
        }
        throw new PngjException("bad chunk " + chunkRaw);
    }

    public double getGamma() {
        return this.gamma;
    }

    public void setGamma(double d) {
        this.gamma = d;
    }
}
