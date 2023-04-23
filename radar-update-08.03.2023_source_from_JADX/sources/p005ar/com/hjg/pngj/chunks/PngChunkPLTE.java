package p005ar.com.hjg.pngj.chunks;

import kotlin.UByte;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkPLTE */
public class PngChunkPLTE extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f135ID = "PLTE";
    private int[] entries;
    private int nentries = 0;

    public PngChunkPLTE(ImageInfo imageInfo) {
        super("PLTE", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.NA;
    }

    public ChunkRaw createRawChunk() {
        int[] iArr = new int[3];
        ChunkRaw createEmptyChunk = createEmptyChunk(this.nentries * 3, true);
        int i = 0;
        int i2 = 0;
        while (i < this.nentries) {
            getEntryRgb(i, iArr);
            int i3 = i2 + 1;
            createEmptyChunk.data[i2] = (byte) iArr[0];
            int i4 = i3 + 1;
            createEmptyChunk.data[i3] = (byte) iArr[1];
            createEmptyChunk.data[i4] = (byte) iArr[2];
            i++;
            i2 = i4 + 1;
        }
        return createEmptyChunk;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        setNentries(chunkRaw.len / 3);
        int i = 0;
        int i2 = 0;
        while (i < this.nentries) {
            int i3 = i2 + 1;
            int i4 = i3 + 1;
            setEntry(i, chunkRaw.data[i2] & UByte.MAX_VALUE, chunkRaw.data[i3] & UByte.MAX_VALUE, chunkRaw.data[i4] & UByte.MAX_VALUE);
            i++;
            i2 = i4 + 1;
        }
    }

    public void setNentries(int i) {
        this.nentries = i;
        if (i < 1 || i > 256) {
            throw new PngjException("invalid pallette - nentries=" + this.nentries);
        }
        int[] iArr = this.entries;
        if (iArr == null || iArr.length != i) {
            this.entries = new int[i];
        }
    }

    public int getNentries() {
        return this.nentries;
    }

    public void setEntry(int i, int i2, int i3, int i4) {
        this.entries[i] = (i2 << 16) | (i3 << 8) | i4;
    }

    public int getEntry(int i) {
        return this.entries[i];
    }

    public void getEntryRgb(int i, int[] iArr) {
        getEntryRgb(i, iArr, 0);
    }

    public void getEntryRgb(int i, int[] iArr, int i2) {
        int i3 = this.entries[i];
        iArr[i2 + 0] = (16711680 & i3) >> 16;
        iArr[i2 + 1] = (65280 & i3) >> 8;
        iArr[i2 + 2] = i3 & 255;
    }

    public int minBitDepth() {
        int i = this.nentries;
        if (i <= 2) {
            return 1;
        }
        if (i <= 4) {
            return 2;
        }
        return i <= 16 ? 4 : 8;
    }
}
