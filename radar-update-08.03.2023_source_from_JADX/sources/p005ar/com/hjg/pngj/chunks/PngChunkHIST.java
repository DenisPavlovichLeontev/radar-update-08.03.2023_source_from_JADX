package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkHIST */
public class PngChunkHIST extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f127ID = "hIST";
    private int[] hist = new int[0];

    public PngChunkHIST(ImageInfo imageInfo) {
        super("hIST", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.AFTER_PLTE_BEFORE_IDAT;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        if (this.imgInfo.indexed) {
            this.hist = new int[(chunkRaw.data.length / 2)];
            int i = 0;
            while (true) {
                int[] iArr = this.hist;
                if (i < iArr.length) {
                    iArr[i] = PngHelperInternal.readInt2fromBytes(chunkRaw.data, i * 2);
                    i++;
                } else {
                    return;
                }
            }
        } else {
            throw new PngjException("only indexed images accept a HIST chunk");
        }
    }

    public ChunkRaw createRawChunk() {
        if (this.imgInfo.indexed) {
            ChunkRaw createEmptyChunk = createEmptyChunk(this.hist.length * 2, true);
            int i = 0;
            while (true) {
                int[] iArr = this.hist;
                if (i >= iArr.length) {
                    return createEmptyChunk;
                }
                PngHelperInternal.writeInt2tobytes(iArr[i], createEmptyChunk.data, i * 2);
                i++;
            }
        } else {
            throw new PngjException("only indexed images accept a HIST chunk");
        }
    }

    public int[] getHist() {
        return this.hist;
    }

    public void setHist(int[] iArr) {
        this.hist = iArr;
    }
}
