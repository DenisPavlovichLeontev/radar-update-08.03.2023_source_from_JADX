package p005ar.com.hjg.pngj.chunks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkSPLT */
public class PngChunkSPLT extends PngChunkMultiple {

    /* renamed from: ID */
    public static final String f137ID = "sPLT";
    private String palName;
    private int[] palette;
    private int sampledepth;

    public PngChunkSPLT(ImageInfo imageInfo) {
        super("sPLT", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_IDAT;
    }

    public ChunkRaw createRawChunk() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(ChunkHelper.toBytes(this.palName));
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write((byte) this.sampledepth);
            int nentries = getNentries();
            for (int i = 0; i < nentries; i++) {
                for (int i2 = 0; i2 < 4; i2++) {
                    if (this.sampledepth == 8) {
                        PngHelperInternal.writeByte((OutputStream) byteArrayOutputStream, (byte) this.palette[(i * 5) + i2]);
                    } else {
                        PngHelperInternal.writeInt2(byteArrayOutputStream, this.palette[(i * 5) + i2]);
                    }
                }
                PngHelperInternal.writeInt2(byteArrayOutputStream, this.palette[(i * 5) + 4]);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            ChunkRaw createEmptyChunk = createEmptyChunk(byteArray.length, false);
            createEmptyChunk.data = byteArray;
            return createEmptyChunk;
        } catch (IOException e) {
            throw new PngjException((Throwable) e);
        }
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6 = 0;
        int i7 = 0;
        while (true) {
            if (i7 >= chunkRaw.data.length) {
                i7 = -1;
                break;
            } else if (chunkRaw.data[i7] == 0) {
                break;
            } else {
                i7++;
            }
        }
        if (i7 <= 0 || i7 > chunkRaw.data.length - 2) {
            throw new PngjException("bad sPLT chunk: no separator found");
        }
        this.palName = ChunkHelper.toString(chunkRaw.data, 0, i7);
        this.sampledepth = PngHelperInternal.readInt1fromByte(chunkRaw.data, i7 + 1);
        int i8 = i7 + 2;
        int length = (chunkRaw.data.length - i8) / (this.sampledepth == 8 ? 6 : 10);
        this.palette = new int[(length * 5)];
        int i9 = i8;
        int i10 = 0;
        while (i6 < length) {
            if (this.sampledepth == 8) {
                int i11 = i9 + 1;
                i5 = PngHelperInternal.readInt1fromByte(chunkRaw.data, i9);
                int i12 = i11 + 1;
                i4 = PngHelperInternal.readInt1fromByte(chunkRaw.data, i11);
                int i13 = i12 + 1;
                i3 = PngHelperInternal.readInt1fromByte(chunkRaw.data, i12);
                i = i13 + 1;
                i2 = PngHelperInternal.readInt1fromByte(chunkRaw.data, i13);
            } else {
                int readInt2fromBytes = PngHelperInternal.readInt2fromBytes(chunkRaw.data, i9);
                int i14 = i9 + 2;
                int readInt2fromBytes2 = PngHelperInternal.readInt2fromBytes(chunkRaw.data, i14);
                int i15 = i14 + 2;
                int readInt2fromBytes3 = PngHelperInternal.readInt2fromBytes(chunkRaw.data, i15);
                int i16 = i15 + 2;
                int readInt2fromBytes4 = PngHelperInternal.readInt2fromBytes(chunkRaw.data, i16);
                i = i16 + 2;
                i5 = readInt2fromBytes;
                i4 = readInt2fromBytes2;
                i3 = readInt2fromBytes3;
                i2 = readInt2fromBytes4;
            }
            int readInt2fromBytes5 = PngHelperInternal.readInt2fromBytes(chunkRaw.data, i);
            int[] iArr = this.palette;
            int i17 = i10 + 1;
            iArr[i10] = i5;
            int i18 = i17 + 1;
            iArr[i17] = i4;
            int i19 = i18 + 1;
            iArr[i18] = i3;
            int i20 = i19 + 1;
            iArr[i19] = i2;
            iArr[i20] = readInt2fromBytes5;
            i6++;
            i10 = i20 + 1;
            i9 = i + 2;
        }
    }

    public int getNentries() {
        return this.palette.length / 5;
    }

    public String getPalName() {
        return this.palName;
    }

    public void setPalName(String str) {
        this.palName = str;
    }

    public int getSampledepth() {
        return this.sampledepth;
    }

    public void setSampledepth(int i) {
        this.sampledepth = i;
    }

    public int[] getPalette() {
        return this.palette;
    }

    public void setPalette(int[] iArr) {
        this.palette = iArr;
    }
}
