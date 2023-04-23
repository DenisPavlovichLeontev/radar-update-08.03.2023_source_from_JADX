package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkMultiple */
public abstract class PngChunkMultiple extends PngChunk {
    public final boolean allowsMultiple() {
        return true;
    }

    protected PngChunkMultiple(String str, ImageInfo imageInfo) {
        super(str, imageInfo);
    }
}
