package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkPHYS */
public class PngChunkPHYS extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f134ID = "pHYs";
    private long pixelsxUnitX;
    private long pixelsxUnitY;
    private int units;

    public PngChunkPHYS(ImageInfo imageInfo) {
        super("pHYs", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_IDAT;
    }

    public ChunkRaw createRawChunk() {
        ChunkRaw createEmptyChunk = createEmptyChunk(9, true);
        PngHelperInternal.writeInt4tobytes((int) this.pixelsxUnitX, createEmptyChunk.data, 0);
        PngHelperInternal.writeInt4tobytes((int) this.pixelsxUnitY, createEmptyChunk.data, 4);
        createEmptyChunk.data[8] = (byte) this.units;
        return createEmptyChunk;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        if (chunkRaw.len == 9) {
            long readInt4fromBytes = (long) PngHelperInternal.readInt4fromBytes(chunkRaw.data, 0);
            this.pixelsxUnitX = readInt4fromBytes;
            if (readInt4fromBytes < 0) {
                this.pixelsxUnitX = readInt4fromBytes + 4294967296L;
            }
            long readInt4fromBytes2 = (long) PngHelperInternal.readInt4fromBytes(chunkRaw.data, 4);
            this.pixelsxUnitY = readInt4fromBytes2;
            if (readInt4fromBytes2 < 0) {
                this.pixelsxUnitY = readInt4fromBytes2 + 4294967296L;
            }
            this.units = PngHelperInternal.readInt1fromByte(chunkRaw.data, 8);
            return;
        }
        throw new PngjException("bad chunk length " + chunkRaw);
    }

    public long getPixelsxUnitX() {
        return this.pixelsxUnitX;
    }

    public void setPixelsxUnitX(long j) {
        this.pixelsxUnitX = j;
    }

    public long getPixelsxUnitY() {
        return this.pixelsxUnitY;
    }

    public void setPixelsxUnitY(long j) {
        this.pixelsxUnitY = j;
    }

    public int getUnits() {
        return this.units;
    }

    public void setUnits(int i) {
        this.units = i;
    }

    public double getAsDpi() {
        if (this.units != 1) {
            return -1.0d;
        }
        long j = this.pixelsxUnitX;
        if (j != this.pixelsxUnitY) {
            return -1.0d;
        }
        return ((double) j) * 0.0254d;
    }

    public double[] getAsDpi2() {
        if (this.units != 1) {
            return new double[]{-1.0d, -1.0d};
        }
        return new double[]{((double) this.pixelsxUnitX) * 0.0254d, ((double) this.pixelsxUnitY) * 0.0254d};
    }

    public void setAsDpi(double d) {
        this.units = 1;
        long j = (long) ((d / 0.0254d) + 0.5d);
        this.pixelsxUnitX = j;
        this.pixelsxUnitY = j;
    }

    public void setAsDpi2(double d, double d2) {
        this.units = 1;
        this.pixelsxUnitX = (long) ((d / 0.0254d) + 0.5d);
        this.pixelsxUnitY = (long) ((d2 / 0.0254d) + 0.5d);
    }
}
