package p005ar.com.hjg.pngj.chunks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngjException;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkITXT */
public class PngChunkITXT extends PngChunkTextVar {

    /* renamed from: ID */
    public static final String f132ID = "iTXt";
    private boolean compressed = false;
    private String langTag = "";
    private String translatedTag = "";

    public PngChunkITXT(ImageInfo imageInfo) {
        super("iTXt", imageInfo);
    }

    public ChunkRaw createRawChunk() {
        if (this.key == null || this.key.trim().length() == 0) {
            throw new PngjException("Text chunk key must be non empty");
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(ChunkHelper.toBytes(this.key));
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(this.compressed ? 1 : 0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(ChunkHelper.toBytes(this.langTag));
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(ChunkHelper.toBytesUTF8(this.translatedTag));
            byteArrayOutputStream.write(0);
            byte[] bytesUTF8 = ChunkHelper.toBytesUTF8(this.val);
            if (this.compressed) {
                bytesUTF8 = ChunkHelper.compressBytes(bytesUTF8, true);
            }
            byteArrayOutputStream.write(bytesUTF8);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            ChunkRaw createEmptyChunk = createEmptyChunk(byteArray.length, false);
            createEmptyChunk.data = byteArray;
            return createEmptyChunk;
        } catch (IOException e) {
            throw new PngjException((Throwable) e);
        }
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        int[] iArr = new int[3];
        int i = 0;
        int i2 = 0;
        while (i < chunkRaw.data.length) {
            if (chunkRaw.data[i] == 0) {
                iArr[i2] = i;
                i2++;
                if (i2 == 1) {
                    i += 2;
                }
                if (i2 == 3) {
                    break;
                }
            }
            i++;
        }
        if (i2 == 3) {
            this.key = ChunkHelper.toString(chunkRaw.data, 0, iArr[0]);
            int i3 = iArr[0] + 1;
            boolean z = chunkRaw.data[i3] != 0;
            this.compressed = z;
            int i4 = i3 + 1;
            if (!z || chunkRaw.data[i4] == 0) {
                this.langTag = ChunkHelper.toString(chunkRaw.data, i4, iArr[1] - i4);
                byte[] bArr = chunkRaw.data;
                int i5 = iArr[1];
                this.translatedTag = ChunkHelper.toStringUTF8(bArr, i5 + 1, (iArr[2] - i5) - 1);
                int i6 = iArr[2] + 1;
                if (this.compressed) {
                    this.val = ChunkHelper.toStringUTF8(ChunkHelper.compressBytes(chunkRaw.data, i6, chunkRaw.data.length - i6, false));
                } else {
                    this.val = ChunkHelper.toStringUTF8(chunkRaw.data, i6, chunkRaw.data.length - i6);
                }
            } else {
                throw new PngjException("Bad formed PngChunkITXT chunk - bad compression method ");
            }
        } else {
            throw new PngjException("Bad formed PngChunkITXT chunk");
        }
    }

    public boolean isCompressed() {
        return this.compressed;
    }

    public void setCompressed(boolean z) {
        this.compressed = z;
    }

    public String getLangtag() {
        return this.langTag;
    }

    public void setLangtag(String str) {
        this.langTag = str;
    }

    public String getTranslatedTag() {
        return this.translatedTag;
    }

    public void setTranslatedTag(String str) {
        this.translatedTag = str;
    }
}
