package p005ar.com.hjg.pngj.chunks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngjException;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkZTXT */
public class PngChunkZTXT extends PngChunkTextVar {

    /* renamed from: ID */
    public static final String f143ID = "zTXt";

    public PngChunkZTXT(ImageInfo imageInfo) {
        super("zTXt", imageInfo);
    }

    public ChunkRaw createRawChunk() {
        if (this.key == null || this.key.trim().length() == 0) {
            throw new PngjException("Text chunk key must be non empty");
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(ChunkHelper.toBytes(this.key));
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(ChunkHelper.compressBytes(ChunkHelper.toBytes(this.val), true));
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            ChunkRaw createEmptyChunk = createEmptyChunk(byteArray.length, false);
            createEmptyChunk.data = byteArray;
            return createEmptyChunk;
        } catch (IOException e) {
            throw new PngjException((Throwable) e);
        }
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        int i = 0;
        while (true) {
            if (i < chunkRaw.data.length) {
                if (chunkRaw.data[i] == 0) {
                    break;
                }
                i++;
            } else {
                i = -1;
                break;
            }
        }
        if (i < 0 || i > chunkRaw.data.length - 2) {
            throw new PngjException("bad zTXt chunk: no separator found");
        }
        this.key = ChunkHelper.toString(chunkRaw.data, 0, i);
        if (chunkRaw.data[i + 1] == 0) {
            this.val = ChunkHelper.toString(ChunkHelper.compressBytes(chunkRaw.data, i + 2, (chunkRaw.data.length - i) - 2, false));
            return;
        }
        throw new PngjException("bad zTXt chunk: unknown compression method");
    }
}
