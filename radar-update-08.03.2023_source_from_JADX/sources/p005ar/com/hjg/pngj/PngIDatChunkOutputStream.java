package p005ar.com.hjg.pngj;

import java.io.OutputStream;
import p005ar.com.hjg.pngj.chunks.ChunkHelper;
import p005ar.com.hjg.pngj.chunks.ChunkRaw;

/* renamed from: ar.com.hjg.pngj.PngIDatChunkOutputStream */
public class PngIDatChunkOutputStream extends ProgressiveOutputStream {
    private static final int SIZE_DEFAULT = 32768;
    private final OutputStream outputStream;
    private byte[] prefix;

    public /* bridge */ /* synthetic */ long getCountFlushed() {
        return super.getCountFlushed();
    }

    public /* bridge */ /* synthetic */ void setSize(int i) {
        super.setSize(i);
    }

    public PngIDatChunkOutputStream(OutputStream outputStream2) {
        this(outputStream2, 0);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PngIDatChunkOutputStream(OutputStream outputStream2, int i) {
        super(i <= 0 ? 32768 : i);
        this.prefix = null;
        this.outputStream = outputStream2;
    }

    /* access modifiers changed from: protected */
    public final void flushBuffer(byte[] bArr, int i) {
        byte[] bArr2 = this.prefix;
        int length = bArr2 == null ? i : bArr2.length + i;
        ChunkRaw chunkRaw = new ChunkRaw(length, ChunkHelper.b_IDAT, false);
        if (i == length) {
            chunkRaw.data = bArr;
        }
        chunkRaw.writeChunk(this.outputStream);
    }

    /* access modifiers changed from: package-private */
    public void setPrefix(byte[] bArr) {
        if (bArr == null) {
            this.prefix = null;
            return;
        }
        byte[] bArr2 = new byte[bArr.length];
        this.prefix = bArr2;
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
    }
}
