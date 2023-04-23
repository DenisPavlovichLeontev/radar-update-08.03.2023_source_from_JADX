package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkCHRM */
public class PngChunkCHRM extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f125ID = "cHRM";
    private double bluex;
    private double bluey;
    private double greenx;
    private double greeny;
    private double redx;
    private double redy;
    private double whitex;
    private double whitey;

    public PngChunkCHRM(ImageInfo imageInfo) {
        super("cHRM", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.AFTER_PLTE_BEFORE_IDAT;
    }

    public ChunkRaw createRawChunk() {
        ChunkRaw createEmptyChunk = createEmptyChunk(32, true);
        PngHelperInternal.writeInt4tobytes(PngHelperInternal.doubleToInt100000(this.whitex), createEmptyChunk.data, 0);
        PngHelperInternal.writeInt4tobytes(PngHelperInternal.doubleToInt100000(this.whitey), createEmptyChunk.data, 4);
        PngHelperInternal.writeInt4tobytes(PngHelperInternal.doubleToInt100000(this.redx), createEmptyChunk.data, 8);
        PngHelperInternal.writeInt4tobytes(PngHelperInternal.doubleToInt100000(this.redy), createEmptyChunk.data, 12);
        PngHelperInternal.writeInt4tobytes(PngHelperInternal.doubleToInt100000(this.greenx), createEmptyChunk.data, 16);
        PngHelperInternal.writeInt4tobytes(PngHelperInternal.doubleToInt100000(this.greeny), createEmptyChunk.data, 20);
        PngHelperInternal.writeInt4tobytes(PngHelperInternal.doubleToInt100000(this.bluex), createEmptyChunk.data, 24);
        PngHelperInternal.writeInt4tobytes(PngHelperInternal.doubleToInt100000(this.bluey), createEmptyChunk.data, 28);
        return createEmptyChunk;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        if (chunkRaw.len == 32) {
            this.whitex = PngHelperInternal.intToDouble100000(PngHelperInternal.readInt4fromBytes(chunkRaw.data, 0));
            this.whitey = PngHelperInternal.intToDouble100000(PngHelperInternal.readInt4fromBytes(chunkRaw.data, 4));
            this.redx = PngHelperInternal.intToDouble100000(PngHelperInternal.readInt4fromBytes(chunkRaw.data, 8));
            this.redy = PngHelperInternal.intToDouble100000(PngHelperInternal.readInt4fromBytes(chunkRaw.data, 12));
            this.greenx = PngHelperInternal.intToDouble100000(PngHelperInternal.readInt4fromBytes(chunkRaw.data, 16));
            this.greeny = PngHelperInternal.intToDouble100000(PngHelperInternal.readInt4fromBytes(chunkRaw.data, 20));
            this.bluex = PngHelperInternal.intToDouble100000(PngHelperInternal.readInt4fromBytes(chunkRaw.data, 24));
            this.bluey = PngHelperInternal.intToDouble100000(PngHelperInternal.readInt4fromBytes(chunkRaw.data, 28));
            return;
        }
        throw new PngjException("bad chunk " + chunkRaw);
    }

    public void setChromaticities(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        this.whitex = d;
        this.redx = d3;
        this.greenx = d5;
        this.bluex = d7;
        this.whitey = d2;
        this.redy = d4;
        this.greeny = d6;
        this.bluey = d8;
    }

    public double[] getChromaticities() {
        return new double[]{this.whitex, this.whitey, this.redx, this.redy, this.greenx, this.greeny, this.bluex, this.bluey};
    }
}
