package p005ar.com.hjg.pngj.chunks;

import java.util.Calendar;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkTIME */
public class PngChunkTIME extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f141ID = "tIME";
    private int day;
    private int hour;
    private int min;
    private int mon;
    private int sec;
    private int year;

    public PngChunkTIME(ImageInfo imageInfo) {
        super("tIME", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.NONE;
    }

    public ChunkRaw createRawChunk() {
        ChunkRaw createEmptyChunk = createEmptyChunk(7, true);
        PngHelperInternal.writeInt2tobytes(this.year, createEmptyChunk.data, 0);
        createEmptyChunk.data[2] = (byte) this.mon;
        createEmptyChunk.data[3] = (byte) this.day;
        createEmptyChunk.data[4] = (byte) this.hour;
        createEmptyChunk.data[5] = (byte) this.min;
        createEmptyChunk.data[6] = (byte) this.sec;
        return createEmptyChunk;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        if (chunkRaw.len == 7) {
            this.year = PngHelperInternal.readInt2fromBytes(chunkRaw.data, 0);
            this.mon = PngHelperInternal.readInt1fromByte(chunkRaw.data, 2);
            this.day = PngHelperInternal.readInt1fromByte(chunkRaw.data, 3);
            this.hour = PngHelperInternal.readInt1fromByte(chunkRaw.data, 4);
            this.min = PngHelperInternal.readInt1fromByte(chunkRaw.data, 5);
            this.sec = PngHelperInternal.readInt1fromByte(chunkRaw.data, 6);
            return;
        }
        throw new PngjException("bad chunk " + chunkRaw);
    }

    public void setNow(int i) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis() - (((long) i) * 1000));
        this.year = instance.get(1);
        this.mon = instance.get(2) + 1;
        this.day = instance.get(5);
        this.hour = instance.get(11);
        this.min = instance.get(12);
        this.sec = instance.get(13);
    }

    public void setYMDHMS(int i, int i2, int i3, int i4, int i5, int i6) {
        this.year = i;
        this.mon = i2;
        this.day = i3;
        this.hour = i4;
        this.min = i5;
        this.sec = i6;
    }

    public int[] getYMDHMS() {
        return new int[]{this.year, this.mon, this.day, this.hour, this.min, this.sec};
    }

    public String getAsString() {
        return String.format("%04d/%02d/%02d %02d:%02d:%02d", new Object[]{Integer.valueOf(this.year), Integer.valueOf(this.mon), Integer.valueOf(this.day), Integer.valueOf(this.hour), Integer.valueOf(this.min), Integer.valueOf(this.sec)});
    }
}
