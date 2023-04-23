package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkIDAT */
public class PngChunkIDAT extends PngChunkMultiple {

    /* renamed from: ID */
    public static final String f129ID = "IDAT";

    public ChunkRaw createRawChunk() {
        return null;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
    }

    public PngChunkIDAT(ImageInfo imageInfo) {
        super("IDAT", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.NA;
    }
}
