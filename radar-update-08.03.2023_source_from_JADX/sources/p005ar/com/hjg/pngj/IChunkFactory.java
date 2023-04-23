package p005ar.com.hjg.pngj;

import p005ar.com.hjg.pngj.chunks.ChunkRaw;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.IChunkFactory */
public interface IChunkFactory {
    PngChunk createChunk(ChunkRaw chunkRaw, ImageInfo imageInfo);
}
