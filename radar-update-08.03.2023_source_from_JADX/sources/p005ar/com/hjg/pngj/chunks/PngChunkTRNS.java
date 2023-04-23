package p005ar.com.hjg.pngj.chunks;

import kotlin.UByte;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkTRNS */
public class PngChunkTRNS extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f142ID = "tRNS";
    private int blue;
    private int gray;
    private int green;
    private int[] paletteAlpha = new int[0];
    private int red;

    public PngChunkTRNS(ImageInfo imageInfo) {
        super("tRNS", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.AFTER_PLTE_BEFORE_IDAT;
    }

    public ChunkRaw createRawChunk() {
        if (this.imgInfo.greyscale) {
            ChunkRaw createEmptyChunk = createEmptyChunk(2, true);
            PngHelperInternal.writeInt2tobytes(this.gray, createEmptyChunk.data, 0);
            return createEmptyChunk;
        } else if (this.imgInfo.indexed) {
            ChunkRaw createEmptyChunk2 = createEmptyChunk(this.paletteAlpha.length, true);
            for (int i = 0; i < createEmptyChunk2.len; i++) {
                createEmptyChunk2.data[i] = (byte) this.paletteAlpha[i];
            }
            return createEmptyChunk2;
        } else {
            ChunkRaw createEmptyChunk3 = createEmptyChunk(6, true);
            PngHelperInternal.writeInt2tobytes(this.red, createEmptyChunk3.data, 0);
            PngHelperInternal.writeInt2tobytes(this.green, createEmptyChunk3.data, 0);
            PngHelperInternal.writeInt2tobytes(this.blue, createEmptyChunk3.data, 0);
            return createEmptyChunk3;
        }
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        if (this.imgInfo.greyscale) {
            this.gray = PngHelperInternal.readInt2fromBytes(chunkRaw.data, 0);
        } else if (this.imgInfo.indexed) {
            int length = chunkRaw.data.length;
            this.paletteAlpha = new int[length];
            for (int i = 0; i < length; i++) {
                this.paletteAlpha[i] = chunkRaw.data[i] & UByte.MAX_VALUE;
            }
        } else {
            this.red = PngHelperInternal.readInt2fromBytes(chunkRaw.data, 0);
            this.green = PngHelperInternal.readInt2fromBytes(chunkRaw.data, 2);
            this.blue = PngHelperInternal.readInt2fromBytes(chunkRaw.data, 4);
        }
    }

    public void setRGB(int i, int i2, int i3) {
        if (this.imgInfo.greyscale || this.imgInfo.indexed) {
            throw new PngjException("only rgb or rgba images support this");
        }
        this.red = i;
        this.green = i2;
        this.blue = i3;
    }

    public int[] getRGB() {
        if (this.imgInfo.greyscale || this.imgInfo.indexed) {
            throw new PngjException("only rgb or rgba images support this");
        }
        return new int[]{this.red, this.green, this.blue};
    }

    public int getRGB888() {
        if (!this.imgInfo.greyscale && !this.imgInfo.indexed) {
            return (this.red << 16) | (this.green << 8) | this.blue;
        }
        throw new PngjException("only rgb or rgba images support this");
    }

    public void setGray(int i) {
        if (this.imgInfo.greyscale) {
            this.gray = i;
            return;
        }
        throw new PngjException("only grayscale images support this");
    }

    public int getGray() {
        if (this.imgInfo.greyscale) {
            return this.gray;
        }
        throw new PngjException("only grayscale images support this");
    }

    public void setPalletteAlpha(int[] iArr) {
        if (this.imgInfo.indexed) {
            this.paletteAlpha = iArr;
            return;
        }
        throw new PngjException("only indexed images support this");
    }

    public void setIndexEntryAsTransparent(int i) {
        if (this.imgInfo.indexed) {
            this.paletteAlpha = new int[]{i + 1};
            for (int i2 = 0; i2 < i; i2++) {
                this.paletteAlpha[i2] = 255;
            }
            this.paletteAlpha[i] = 0;
            return;
        }
        throw new PngjException("only indexed images support this");
    }

    public int[] getPalletteAlpha() {
        return this.paletteAlpha;
    }
}
