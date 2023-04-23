package p005ar.com.hjg.pngj;

/* renamed from: ar.com.hjg.pngj.ChunkSeqBuffering */
public class ChunkSeqBuffering extends ChunkSeqReader {
    protected boolean checkCrc = true;

    /* access modifiers changed from: protected */
    public boolean isIdatKind(String str) {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean shouldCheckCrc(int i, String str) {
        return this.checkCrc;
    }

    public void setCheckCrc(boolean z) {
        this.checkCrc = z;
    }
}
