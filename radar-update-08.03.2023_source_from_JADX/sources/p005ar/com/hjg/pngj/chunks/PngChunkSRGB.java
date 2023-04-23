package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkSRGB */
public class PngChunkSRGB extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f138ID = "sRGB";
    public static final int RENDER_INTENT_Absolute_colorimetric = 3;
    public static final int RENDER_INTENT_Perceptual = 0;
    public static final int RENDER_INTENT_Relative_colorimetric = 1;
    public static final int RENDER_INTENT_Saturation = 2;
    private int intent;

    public PngChunkSRGB(ImageInfo imageInfo) {
        super("sRGB", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_PLTE_AND_IDAT;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        if (chunkRaw.len == 1) {
            this.intent = PngHelperInternal.readInt1fromByte(chunkRaw.data, 0);
            return;
        }
        throw new PngjException("bad chunk length " + chunkRaw);
    }

    public ChunkRaw createRawChunk() {
        ChunkRaw createEmptyChunk = createEmptyChunk(1, true);
        createEmptyChunk.data[0] = (byte) this.intent;
        return createEmptyChunk;
    }

    public int getIntent() {
        return this.intent;
    }

    public void setIntent(int i) {
        this.intent = i;
    }
}
