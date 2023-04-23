package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;

/* renamed from: ar.com.hjg.pngj.chunks.ChunkCopyBehaviour */
public class ChunkCopyBehaviour {
    public static final int COPY_ALL = 8;
    public static final int COPY_ALL_SAFE = 4;
    public static final int COPY_ALMOSTALL = 256;
    public static final int COPY_NONE = 0;
    public static final int COPY_PALETTE = 1;
    public static final int COPY_PHYS = 16;
    public static final int COPY_TEXTUAL = 32;
    public static final int COPY_TRANSPARENCY = 64;
    public static final int COPY_UNKNOWN = 128;

    /* access modifiers changed from: private */
    public static boolean maskMatch(int i, int i2) {
        return (i & i2) != 0;
    }

    public static ChunkPredicate createPredicate(final int i, final ImageInfo imageInfo) {
        return new ChunkPredicate() {
            public boolean match(PngChunk pngChunk) {
                if (!pngChunk.crit) {
                    boolean z = pngChunk instanceof PngChunkTextVar;
                    boolean z2 = pngChunk.safe;
                    if (ChunkCopyBehaviour.maskMatch(i, 8)) {
                        return true;
                    }
                    if (z2 && ChunkCopyBehaviour.maskMatch(i, 4)) {
                        return true;
                    }
                    if (pngChunk.f122id.equals("tRNS") && ChunkCopyBehaviour.maskMatch(i, 64)) {
                        return true;
                    }
                    if (pngChunk.f122id.equals("pHYs") && ChunkCopyBehaviour.maskMatch(i, 16)) {
                        return true;
                    }
                    if (z && ChunkCopyBehaviour.maskMatch(i, 32)) {
                        return true;
                    }
                    if (ChunkCopyBehaviour.maskMatch(i, 256) && !ChunkHelper.isUnknown(pngChunk) && !z && !pngChunk.f122id.equals("hIST") && !pngChunk.f122id.equals("tIME")) {
                        return true;
                    }
                    if (!ChunkCopyBehaviour.maskMatch(i, 128) || !ChunkHelper.isUnknown(pngChunk)) {
                        return false;
                    }
                    return true;
                } else if (!pngChunk.f122id.equals("PLTE")) {
                    return false;
                } else {
                    if (imageInfo.indexed && ChunkCopyBehaviour.maskMatch(i, 1)) {
                        return true;
                    }
                    if (imageInfo.greyscale || !ChunkCopyBehaviour.maskMatch(i, 8)) {
                        return false;
                    }
                    return true;
                }
            }
        };
    }
}
