package p005ar.com.hjg.pngj.chunks;

import java.util.ArrayList;
import java.util.List;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngjException;

/* renamed from: ar.com.hjg.pngj.chunks.ChunksList */
public class ChunksList {
    public static final int CHUNK_GROUP_0_IDHR = 0;
    public static final int CHUNK_GROUP_1_AFTERIDHR = 1;
    public static final int CHUNK_GROUP_2_PLTE = 2;
    public static final int CHUNK_GROUP_3_AFTERPLTE = 3;
    public static final int CHUNK_GROUP_4_IDAT = 4;
    public static final int CHUNK_GROUP_5_AFTERIDAT = 5;
    public static final int CHUNK_GROUP_6_END = 6;
    List<PngChunk> chunks = new ArrayList();
    final ImageInfo imageInfo;
    boolean withPlte = false;

    public ChunksList(ImageInfo imageInfo2) {
        this.imageInfo = imageInfo2;
    }

    public List<PngChunk> getChunks() {
        return this.chunks;
    }

    protected static List<PngChunk> getXById(List<PngChunk> list, final String str, final String str2) {
        if (str2 == null) {
            return ChunkHelper.filterList(list, new ChunkPredicate() {
                public boolean match(PngChunk pngChunk) {
                    return pngChunk.f122id.equals(str);
                }
            });
        }
        return ChunkHelper.filterList(list, new ChunkPredicate() {
            public boolean match(PngChunk pngChunk) {
                if (!pngChunk.f122id.equals(str)) {
                    return false;
                }
                if ((pngChunk instanceof PngChunkTextVar) && !((PngChunkTextVar) pngChunk).getKey().equals(str2)) {
                    return false;
                }
                if (!(pngChunk instanceof PngChunkSPLT) || ((PngChunkSPLT) pngChunk).getPalName().equals(str2)) {
                    return true;
                }
                return false;
            }
        });
    }

    public void appendReadChunk(PngChunk pngChunk, int i) {
        pngChunk.setChunkGroup(i);
        this.chunks.add(pngChunk);
        if (pngChunk.f122id.equals("PLTE")) {
            this.withPlte = true;
        }
    }

    public List<? extends PngChunk> getById(String str) {
        return getById(str, (String) null);
    }

    public List<? extends PngChunk> getById(String str, String str2) {
        return getXById(this.chunks, str, str2);
    }

    public PngChunk getById1(String str) {
        return getById1(str, false);
    }

    public PngChunk getById1(String str, boolean z) {
        return getById1(str, (String) null, z);
    }

    public PngChunk getById1(String str, String str2, boolean z) {
        List<? extends PngChunk> byId = getById(str, str2);
        if (byId.isEmpty()) {
            return null;
        }
        if (byId.size() <= 1 || (!z && ((PngChunk) byId.get(0)).allowsMultiple())) {
            return (PngChunk) byId.get(byId.size() - 1);
        }
        throw new PngjException("unexpected multiple chunks id=" + str);
    }

    public List<PngChunk> getEquivalent(final PngChunk pngChunk) {
        return ChunkHelper.filterList(this.chunks, new ChunkPredicate() {
            public boolean match(PngChunk pngChunk) {
                return ChunkHelper.equivalent(pngChunk, pngChunk);
            }
        });
    }

    public String toString() {
        return "ChunkList: read: " + this.chunks.size();
    }

    public String toStringFull() {
        StringBuilder sb = new StringBuilder(toString());
        sb.append("\n Read:\n");
        for (PngChunk next : this.chunks) {
            sb.append(next);
            sb.append(" G=" + next.getChunkGroup() + "\n");
        }
        return sb.toString();
    }
}
