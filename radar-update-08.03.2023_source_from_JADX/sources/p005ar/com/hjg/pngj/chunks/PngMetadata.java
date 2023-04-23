package p005ar.com.hjg.pngj.chunks;

import java.util.ArrayList;
import java.util.List;
import p005ar.com.hjg.pngj.PngjException;

/* renamed from: ar.com.hjg.pngj.chunks.PngMetadata */
public class PngMetadata {
    private final ChunksList chunkList;
    private final boolean readonly;

    public PngMetadata(ChunksList chunksList) {
        this.chunkList = chunksList;
        if (chunksList instanceof ChunksListForWrite) {
            this.readonly = false;
        } else {
            this.readonly = true;
        }
    }

    public void queueChunk(final PngChunk pngChunk, boolean z) {
        ChunksListForWrite chunkListW = getChunkListW();
        if (!this.readonly) {
            if (z) {
                ChunkHelper.trimList(chunkListW.getQueuedChunks(), new ChunkPredicate() {
                    public boolean match(PngChunk pngChunk) {
                        return ChunkHelper.equivalent(pngChunk, pngChunk);
                    }
                });
            }
            chunkListW.queue(pngChunk);
            return;
        }
        throw new PngjException("cannot set chunk : readonly metadata");
    }

    public void queueChunk(PngChunk pngChunk) {
        queueChunk(pngChunk, true);
    }

    private ChunksListForWrite getChunkListW() {
        return (ChunksListForWrite) this.chunkList;
    }

    public double[] getDpi() {
        PngChunk byId1 = this.chunkList.getById1("pHYs", true);
        if (byId1 == null) {
            return new double[]{-1.0d, -1.0d};
        }
        return ((PngChunkPHYS) byId1).getAsDpi2();
    }

    public void setDpi(double d) {
        setDpi(d, d);
    }

    public void setDpi(double d, double d2) {
        PngChunkPHYS pngChunkPHYS = new PngChunkPHYS(this.chunkList.imageInfo);
        pngChunkPHYS.setAsDpi2(d, d2);
        queueChunk(pngChunkPHYS);
    }

    public PngChunkTIME setTimeNow(int i) {
        PngChunkTIME pngChunkTIME = new PngChunkTIME(this.chunkList.imageInfo);
        pngChunkTIME.setNow(i);
        queueChunk(pngChunkTIME);
        return pngChunkTIME;
    }

    public PngChunkTIME setTimeNow() {
        return setTimeNow(0);
    }

    public PngChunkTIME setTimeYMDHMS(int i, int i2, int i3, int i4, int i5, int i6) {
        PngChunkTIME pngChunkTIME = new PngChunkTIME(this.chunkList.imageInfo);
        pngChunkTIME.setYMDHMS(i, i2, i3, i4, i5, i6);
        queueChunk(pngChunkTIME, true);
        return pngChunkTIME;
    }

    public PngChunkTIME getTime() {
        return (PngChunkTIME) this.chunkList.getById1("tIME");
    }

    public String getTimeAsString() {
        PngChunkTIME time = getTime();
        if (time == null) {
            return "";
        }
        return time.getAsString();
    }

    public PngChunkTextVar setText(String str, String str2, boolean z, boolean z2) {
        PngChunkZTXT pngChunkZTXT;
        if (!z2 || z) {
            if (!z) {
                PngChunkITXT pngChunkITXT = new PngChunkITXT(this.chunkList.imageInfo);
                PngChunkITXT pngChunkITXT2 = pngChunkITXT;
                pngChunkITXT.setLangtag(str);
                pngChunkZTXT = pngChunkITXT;
            } else if (z2) {
                pngChunkZTXT = new PngChunkZTXT(this.chunkList.imageInfo);
            } else {
                pngChunkZTXT = new PngChunkTEXT(this.chunkList.imageInfo);
            }
            pngChunkZTXT.setKeyVal(str, str2);
            queueChunk(pngChunkZTXT, true);
            return pngChunkZTXT;
        }
        throw new PngjException("cannot compress non latin text");
    }

    public PngChunkTextVar setText(String str, String str2) {
        return setText(str, str2, false, false);
    }

    public List<? extends PngChunkTextVar> getTxtsForKey(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.chunkList.getById("tEXt", str));
        arrayList.addAll(this.chunkList.getById("zTXt", str));
        arrayList.addAll(this.chunkList.getById("iTXt", str));
        return arrayList;
    }

    public String getTxtForKey(String str) {
        List<? extends PngChunkTextVar> txtsForKey = getTxtsForKey(str);
        if (txtsForKey.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (PngChunkTextVar val : txtsForKey) {
            sb.append(val.getVal());
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    public PngChunkPLTE getPLTE() {
        return (PngChunkPLTE) this.chunkList.getById1("PLTE");
    }

    public PngChunkPLTE createPLTEChunk() {
        PngChunkPLTE pngChunkPLTE = new PngChunkPLTE(this.chunkList.imageInfo);
        queueChunk(pngChunkPLTE);
        return pngChunkPLTE;
    }

    public PngChunkTRNS getTRNS() {
        return (PngChunkTRNS) this.chunkList.getById1("tRNS");
    }

    public PngChunkTRNS createTRNSChunk() {
        PngChunkTRNS pngChunkTRNS = new PngChunkTRNS(this.chunkList.imageInfo);
        queueChunk(pngChunkTRNS);
        return pngChunkTRNS;
    }
}
