package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkSBIT */
public class PngChunkSBIT extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f136ID = "sBIT";
    private int alphasb;
    private int bluesb;
    private int graysb;
    private int greensb;
    private int redsb;

    public PngChunkSBIT(ImageInfo imageInfo) {
        super("sBIT", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_PLTE_AND_IDAT;
    }

    private int getCLen() {
        int i = this.imgInfo.greyscale ? 1 : 3;
        return this.imgInfo.alpha ? i + 1 : i;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        if (chunkRaw.len != getCLen()) {
            throw new PngjException("bad chunk length " + chunkRaw);
        } else if (this.imgInfo.greyscale) {
            this.graysb = PngHelperInternal.readInt1fromByte(chunkRaw.data, 0);
            if (this.imgInfo.alpha) {
                this.alphasb = PngHelperInternal.readInt1fromByte(chunkRaw.data, 1);
            }
        } else {
            this.redsb = PngHelperInternal.readInt1fromByte(chunkRaw.data, 0);
            this.greensb = PngHelperInternal.readInt1fromByte(chunkRaw.data, 1);
            this.bluesb = PngHelperInternal.readInt1fromByte(chunkRaw.data, 2);
            if (this.imgInfo.alpha) {
                this.alphasb = PngHelperInternal.readInt1fromByte(chunkRaw.data, 3);
            }
        }
    }

    public ChunkRaw createRawChunk() {
        ChunkRaw createEmptyChunk = createEmptyChunk(getCLen(), true);
        if (this.imgInfo.greyscale) {
            createEmptyChunk.data[0] = (byte) this.graysb;
            if (this.imgInfo.alpha) {
                createEmptyChunk.data[1] = (byte) this.alphasb;
            }
        } else {
            createEmptyChunk.data[0] = (byte) this.redsb;
            createEmptyChunk.data[1] = (byte) this.greensb;
            createEmptyChunk.data[2] = (byte) this.bluesb;
            if (this.imgInfo.alpha) {
                createEmptyChunk.data[3] = (byte) this.alphasb;
            }
        }
        return createEmptyChunk;
    }

    public void setGraysb(int i) {
        if (this.imgInfo.greyscale) {
            this.graysb = i;
            return;
        }
        throw new PngjException("only greyscale images support this");
    }

    public int getGraysb() {
        if (this.imgInfo.greyscale) {
            return this.graysb;
        }
        throw new PngjException("only greyscale images support this");
    }

    public void setAlphasb(int i) {
        if (this.imgInfo.alpha) {
            this.alphasb = i;
            return;
        }
        throw new PngjException("only images with alpha support this");
    }

    public int getAlphasb() {
        if (this.imgInfo.alpha) {
            return this.alphasb;
        }
        throw new PngjException("only images with alpha support this");
    }

    public void setRGB(int i, int i2, int i3) {
        if (this.imgInfo.greyscale || this.imgInfo.indexed) {
            throw new PngjException("only rgb or rgba images support this");
        }
        this.redsb = i;
        this.greensb = i2;
        this.bluesb = i3;
    }

    public int[] getRGB() {
        if (this.imgInfo.greyscale || this.imgInfo.indexed) {
            throw new PngjException("only rgb or rgba images support this");
        }
        return new int[]{this.redsb, this.greensb, this.bluesb};
    }
}
