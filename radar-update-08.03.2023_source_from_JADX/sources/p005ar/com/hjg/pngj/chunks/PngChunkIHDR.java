package p005ar.com.hjg.pngj.chunks;

import java.io.ByteArrayInputStream;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.PngjInputException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkIHDR */
public class PngChunkIHDR extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f131ID = "IHDR";
    private int bitspc;
    private int colormodel;
    private int cols;
    private int compmeth;
    private int filmeth;
    private int interlaced;
    private int rows;

    public PngChunkIHDR(ImageInfo imageInfo) {
        super("IHDR", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.NA;
    }

    public ChunkRaw createRawChunk() {
        ChunkRaw chunkRaw = new ChunkRaw(13, ChunkHelper.b_IHDR, true);
        PngHelperInternal.writeInt4tobytes(this.cols, chunkRaw.data, 0);
        PngHelperInternal.writeInt4tobytes(this.rows, chunkRaw.data, 4);
        chunkRaw.data[8] = (byte) this.bitspc;
        chunkRaw.data[9] = (byte) this.colormodel;
        chunkRaw.data[10] = (byte) this.compmeth;
        chunkRaw.data[11] = (byte) this.filmeth;
        chunkRaw.data[12] = (byte) this.interlaced;
        return chunkRaw;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        if (chunkRaw.len == 13) {
            ByteArrayInputStream asByteStream = chunkRaw.getAsByteStream();
            this.cols = PngHelperInternal.readInt4(asByteStream);
            this.rows = PngHelperInternal.readInt4(asByteStream);
            this.bitspc = PngHelperInternal.readByte(asByteStream);
            this.colormodel = PngHelperInternal.readByte(asByteStream);
            this.compmeth = PngHelperInternal.readByte(asByteStream);
            this.filmeth = PngHelperInternal.readByte(asByteStream);
            this.interlaced = PngHelperInternal.readByte(asByteStream);
            return;
        }
        throw new PngjException("Bad IDHR len " + chunkRaw.len);
    }

    public int getCols() {
        return this.cols;
    }

    public void setCols(int i) {
        this.cols = i;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int i) {
        this.rows = i;
    }

    public int getBitspc() {
        return this.bitspc;
    }

    public void setBitspc(int i) {
        this.bitspc = i;
    }

    public int getColormodel() {
        return this.colormodel;
    }

    public void setColormodel(int i) {
        this.colormodel = i;
    }

    public int getCompmeth() {
        return this.compmeth;
    }

    public void setCompmeth(int i) {
        this.compmeth = i;
    }

    public int getFilmeth() {
        return this.filmeth;
    }

    public void setFilmeth(int i) {
        this.filmeth = i;
    }

    public int getInterlaced() {
        return this.interlaced;
    }

    public void setInterlaced(int i) {
        this.interlaced = i;
    }

    public boolean isInterlaced() {
        return getInterlaced() == 1;
    }

    public ImageInfo createImageInfo() {
        check();
        return new ImageInfo(getCols(), getRows(), getBitspc(), (getColormodel() & 4) != 0, getColormodel() == 0 || getColormodel() == 4, (getColormodel() & 1) != 0);
    }

    public void check() {
        if (this.cols < 1 || this.rows < 1 || this.compmeth != 0 || this.filmeth != 0) {
            throw new PngjInputException("bad IHDR: col/row/compmethod/filmethod invalid");
        }
        int i = this.bitspc;
        if (i == 1 || i == 2 || i == 4 || i == 8 || i == 16) {
            int i2 = this.interlaced;
            if (i2 < 0 || i2 > 1) {
                throw new PngjInputException("bad IHDR: interlace invalid");
            }
            int i3 = this.colormodel;
            if (i3 != 0) {
                if (!(i3 == 6 || i3 == 2)) {
                    if (i3 != 3) {
                        if (i3 != 4) {
                            throw new PngjInputException("bad IHDR: invalid colormodel");
                        }
                    } else if (i == 16) {
                        throw new PngjInputException("bad IHDR: bitdepth invalid");
                    } else {
                        return;
                    }
                }
                if (i != 8 && i != 16) {
                    throw new PngjInputException("bad IHDR: bitdepth invalid");
                }
                return;
            }
            return;
        }
        throw new PngjInputException("bad IHDR: bitdepth invalid");
    }
}
