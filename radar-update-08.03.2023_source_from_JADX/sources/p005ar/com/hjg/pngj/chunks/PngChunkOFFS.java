package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkOFFS */
public class PngChunkOFFS extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f133ID = "oFFs";
    private long posX;
    private long posY;
    private int units;

    public PngChunkOFFS(ImageInfo imageInfo) {
        super(f133ID, imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_IDAT;
    }

    public ChunkRaw createRawChunk() {
        ChunkRaw createEmptyChunk = createEmptyChunk(9, true);
        PngHelperInternal.writeInt4tobytes((int) this.posX, createEmptyChunk.data, 0);
        PngHelperInternal.writeInt4tobytes((int) this.posY, createEmptyChunk.data, 4);
        createEmptyChunk.data[8] = (byte) this.units;
        return createEmptyChunk;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        if (chunkRaw.len == 9) {
            long readInt4fromBytes = (long) PngHelperInternal.readInt4fromBytes(chunkRaw.data, 0);
            this.posX = readInt4fromBytes;
            if (readInt4fromBytes < 0) {
                this.posX = readInt4fromBytes + 4294967296L;
            }
            long readInt4fromBytes2 = (long) PngHelperInternal.readInt4fromBytes(chunkRaw.data, 4);
            this.posY = readInt4fromBytes2;
            if (readInt4fromBytes2 < 0) {
                this.posY = readInt4fromBytes2 + 4294967296L;
            }
            this.units = PngHelperInternal.readInt1fromByte(chunkRaw.data, 8);
            return;
        }
        throw new PngjException("bad chunk length " + chunkRaw);
    }

    public int getUnits() {
        return this.units;
    }

    public void setUnits(int i) {
        this.units = i;
    }

    public long getPosX() {
        return this.posX;
    }

    public void setPosX(long j) {
        this.posX = j;
    }

    public long getPosY() {
        return this.posY;
    }

    public void setPosY(long j) {
        this.posY = j;
    }
}
