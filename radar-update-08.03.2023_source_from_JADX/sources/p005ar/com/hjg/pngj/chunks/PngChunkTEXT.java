package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngjException;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkTEXT */
public class PngChunkTEXT extends PngChunkTextVar {

    /* renamed from: ID */
    public static final String f140ID = "tEXt";

    public PngChunkTEXT(ImageInfo imageInfo) {
        super("tEXt", imageInfo);
    }

    public ChunkRaw createRawChunk() {
        if (this.key == null || this.key.trim().length() == 0) {
            throw new PngjException("Text chunk key must be non empty");
        }
        byte[] bytes = ChunkHelper.toBytes(this.key + "\u0000" + this.val);
        ChunkRaw createEmptyChunk = createEmptyChunk(bytes.length, false);
        createEmptyChunk.data = bytes;
        return createEmptyChunk;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        int i = 0;
        while (i < chunkRaw.data.length && chunkRaw.data[i] != 0) {
            i++;
        }
        this.key = ChunkHelper.toString(chunkRaw.data, 0, i);
        int i2 = i + 1;
        this.val = i2 < chunkRaw.data.length ? ChunkHelper.toString(chunkRaw.data, i2, chunkRaw.data.length - i2) : "";
    }
}
