package p005ar.com.hjg.pngj.chunks;

import kotlin.UByte;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkBKGD */
public class PngChunkBKGD extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f124ID = "bKGD";
    private int blue;
    private int gray;
    private int green;
    private int paletteIndex;
    private int red;

    public PngChunkBKGD(ImageInfo imageInfo) {
        super("bKGD", imageInfo);
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
            ChunkRaw createEmptyChunk2 = createEmptyChunk(1, true);
            createEmptyChunk2.data[0] = (byte) this.paletteIndex;
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
            this.paletteIndex = chunkRaw.data[0] & UByte.MAX_VALUE;
        } else {
            this.red = PngHelperInternal.readInt2fromBytes(chunkRaw.data, 0);
            this.green = PngHelperInternal.readInt2fromBytes(chunkRaw.data, 2);
            this.blue = PngHelperInternal.readInt2fromBytes(chunkRaw.data, 4);
        }
    }

    public void setGray(int i) {
        if (this.imgInfo.greyscale) {
            this.gray = i;
            return;
        }
        throw new PngjException("only gray images support this");
    }

    public int getGray() {
        if (this.imgInfo.greyscale) {
            return this.gray;
        }
        throw new PngjException("only gray images support this");
    }

    public void setPaletteIndex(int i) {
        if (this.imgInfo.indexed) {
            this.paletteIndex = i;
            return;
        }
        throw new PngjException("only indexed (pallete) images support this");
    }

    public int getPaletteIndex() {
        if (this.imgInfo.indexed) {
            return this.paletteIndex;
        }
        throw new PngjException("only indexed (pallete) images support this");
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
}
