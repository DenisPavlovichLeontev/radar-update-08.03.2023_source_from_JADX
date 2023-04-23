package p005ar.com.hjg.pngj.chunks;

import java.io.OutputStream;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngjExceptionInternal;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunk */
public abstract class PngChunk {
    protected int chunkGroup = -1;
    public final boolean crit;

    /* renamed from: id */
    public final String f122id;
    protected final ImageInfo imgInfo;
    private boolean priority = false;
    public final boolean pub;
    protected ChunkRaw raw;
    public final boolean safe;

    /* access modifiers changed from: protected */
    public abstract boolean allowsMultiple();

    /* access modifiers changed from: protected */
    public abstract ChunkRaw createRawChunk();

    public abstract ChunkOrderingConstraint getOrderingConstraint();

    /* access modifiers changed from: protected */
    public abstract void parseFromRaw(ChunkRaw chunkRaw);

    /* renamed from: ar.com.hjg.pngj.chunks.PngChunk$ChunkOrderingConstraint */
    public enum ChunkOrderingConstraint {
        NONE,
        BEFORE_PLTE_AND_IDAT,
        AFTER_PLTE_BEFORE_IDAT,
        AFTER_PLTE_BEFORE_IDAT_PLTE_REQUIRED,
        BEFORE_IDAT,
        NA;

        public boolean mustGoBeforePLTE() {
            return this == BEFORE_PLTE_AND_IDAT;
        }

        public boolean mustGoBeforeIDAT() {
            return this == BEFORE_IDAT || this == BEFORE_PLTE_AND_IDAT || this == AFTER_PLTE_BEFORE_IDAT;
        }

        public boolean mustGoAfterPLTE() {
            return this == AFTER_PLTE_BEFORE_IDAT || this == AFTER_PLTE_BEFORE_IDAT_PLTE_REQUIRED;
        }

        public boolean isOk(int i, boolean z) {
            if (this == NONE) {
                return true;
            }
            if (this == BEFORE_IDAT) {
                if (i < 4) {
                    return true;
                }
                return false;
            } else if (this == BEFORE_PLTE_AND_IDAT) {
                if (i < 2) {
                    return true;
                }
                return false;
            } else if (this != AFTER_PLTE_BEFORE_IDAT) {
                return false;
            } else {
                if (z) {
                    if (i < 4) {
                        return true;
                    }
                } else if (i < 4 && i > 2) {
                    return true;
                }
                return false;
            }
        }
    }

    public PngChunk(String str, ImageInfo imageInfo) {
        this.f122id = str;
        this.imgInfo = imageInfo;
        this.crit = ChunkHelper.isCritical(str);
        this.pub = ChunkHelper.isPublic(str);
        this.safe = ChunkHelper.isSafeToCopy(str);
    }

    /* access modifiers changed from: protected */
    public final ChunkRaw createEmptyChunk(int i, boolean z) {
        return new ChunkRaw(i, ChunkHelper.toBytes(this.f122id), z);
    }

    public final int getChunkGroup() {
        return this.chunkGroup;
    }

    /* access modifiers changed from: package-private */
    public final void setChunkGroup(int i) {
        this.chunkGroup = i;
    }

    public boolean hasPriority() {
        return this.priority;
    }

    public void setPriority(boolean z) {
        this.priority = z;
    }

    /* access modifiers changed from: package-private */
    public final void write(OutputStream outputStream) {
        ChunkRaw chunkRaw = this.raw;
        if (chunkRaw == null || chunkRaw.data == null) {
            this.raw = createRawChunk();
        }
        ChunkRaw chunkRaw2 = this.raw;
        if (chunkRaw2 != null) {
            chunkRaw2.writeChunk(outputStream);
            return;
        }
        throw new PngjExceptionInternal("null chunk ! creation failed for " + this);
    }

    public ChunkRaw getRaw() {
        return this.raw;
    }

    /* access modifiers changed from: package-private */
    public void setRaw(ChunkRaw chunkRaw) {
        this.raw = chunkRaw;
    }

    public int getLen() {
        ChunkRaw chunkRaw = this.raw;
        if (chunkRaw != null) {
            return chunkRaw.len;
        }
        return -1;
    }

    public long getOffset() {
        ChunkRaw chunkRaw = this.raw;
        if (chunkRaw != null) {
            return chunkRaw.getOffset();
        }
        return -1;
    }

    public void invalidateRawData() {
        this.raw = null;
    }

    public String toString() {
        return "chunk id= " + this.f122id + " (len=" + getLen() + " offset=" + getOffset() + ")";
    }
}
