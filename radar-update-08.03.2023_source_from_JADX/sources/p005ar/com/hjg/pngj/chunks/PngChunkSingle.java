package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkSingle */
public abstract class PngChunkSingle extends PngChunk {
    public final boolean allowsMultiple() {
        return false;
    }

    protected PngChunkSingle(String str, ImageInfo imageInfo) {
        super(str, imageInfo);
    }

    public int hashCode() {
        return 31 + (this.f122id == null ? 0 : this.f122id.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PngChunkSingle pngChunkSingle = (PngChunkSingle) obj;
        if (this.f122id == null) {
            if (pngChunkSingle.f122id != null) {
                return false;
            }
        } else if (!this.f122id.equals(pngChunkSingle.f122id)) {
            return false;
        }
        return true;
    }
}
