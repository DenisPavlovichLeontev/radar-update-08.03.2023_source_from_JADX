package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkSTER */
public class PngChunkSTER extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f139ID = "sTER";
    private byte mode;

    public PngChunkSTER(ImageInfo imageInfo) {
        super(f139ID, imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_IDAT;
    }

    public ChunkRaw createRawChunk() {
        ChunkRaw createEmptyChunk = createEmptyChunk(1, true);
        createEmptyChunk.data[0] = this.mode;
        return createEmptyChunk;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        if (chunkRaw.len == 1) {
            this.mode = chunkRaw.data[0];
            return;
        }
        throw new PngjException("bad chunk length " + chunkRaw);
    }

    public byte getMode() {
        return this.mode;
    }

    public void setMode(byte b) {
        this.mode = b;
    }
}
