package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkUNKNOWN */
public class PngChunkUNKNOWN extends PngChunkMultiple {
    public void parseFromRaw(ChunkRaw chunkRaw) {
    }

    public PngChunkUNKNOWN(String str, ImageInfo imageInfo) {
        super(str, imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.NONE;
    }

    public ChunkRaw createRawChunk() {
        return this.raw;
    }

    public byte[] getData() {
        return this.raw.data;
    }

    public void setData(byte[] bArr) {
        this.raw.data = bArr;
    }
}
