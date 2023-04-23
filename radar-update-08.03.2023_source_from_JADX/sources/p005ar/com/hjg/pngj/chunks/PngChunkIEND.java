package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkIEND */
public class PngChunkIEND extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f130ID = "IEND";

    public void parseFromRaw(ChunkRaw chunkRaw) {
    }

    public PngChunkIEND(ImageInfo imageInfo) {
        super("IEND", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.NA;
    }

    public ChunkRaw createRawChunk() {
        return new ChunkRaw(0, ChunkHelper.b_IEND, false);
    }
}
