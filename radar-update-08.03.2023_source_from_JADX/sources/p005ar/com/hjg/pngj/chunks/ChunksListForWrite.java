package p005ar.com.hjg.pngj.chunks;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.PngjOutputException;

/* renamed from: ar.com.hjg.pngj.chunks.ChunksListForWrite */
public class ChunksListForWrite extends ChunksList {
    private HashMap<String, Integer> alreadyWrittenKeys = new HashMap<>();
    private final List<PngChunk> queuedChunks = new ArrayList();

    public ChunksListForWrite(ImageInfo imageInfo) {
        super(imageInfo);
    }

    public List<? extends PngChunk> getQueuedById(String str) {
        return getQueuedById(str, (String) null);
    }

    public List<? extends PngChunk> getQueuedById(String str, String str2) {
        return getXById(this.queuedChunks, str, str2);
    }

    public PngChunk getQueuedById1(String str, String str2, boolean z) {
        List<? extends PngChunk> queuedById = getQueuedById(str, str2);
        if (queuedById.isEmpty()) {
            return null;
        }
        if (queuedById.size() <= 1 || (!z && ((PngChunk) queuedById.get(0)).allowsMultiple())) {
            return (PngChunk) queuedById.get(queuedById.size() - 1);
        }
        throw new PngjException("unexpected multiple chunks id=" + str);
    }

    public PngChunk getQueuedById1(String str, boolean z) {
        return getQueuedById1(str, (String) null, z);
    }

    public PngChunk getQueuedById1(String str) {
        return getQueuedById1(str, false);
    }

    public List<PngChunk> getQueuedEquivalent(final PngChunk pngChunk) {
        return ChunkHelper.filterList(this.queuedChunks, new ChunkPredicate() {
            public boolean match(PngChunk pngChunk) {
                return ChunkHelper.equivalent(pngChunk, pngChunk);
            }
        });
    }

    public boolean removeChunk(PngChunk pngChunk) {
        if (pngChunk == null) {
            return false;
        }
        return this.queuedChunks.remove(pngChunk);
    }

    public boolean queue(PngChunk pngChunk) {
        this.queuedChunks.add(pngChunk);
        return true;
    }

    private static boolean shouldWrite(PngChunk pngChunk, int i) {
        int i2;
        if (i == 2) {
            return pngChunk.f122id.equals("PLTE");
        }
        if (i % 2 != 0) {
            int i3 = 3;
            if (pngChunk.getOrderingConstraint().mustGoBeforePLTE()) {
                i2 = 1;
                i3 = 1;
            } else {
                if (!pngChunk.getOrderingConstraint().mustGoBeforeIDAT()) {
                    i3 = 5;
                } else if (pngChunk.getOrderingConstraint().mustGoAfterPLTE()) {
                    i2 = 3;
                }
                i2 = 1;
            }
            if (!pngChunk.hasPriority()) {
                i2 = i3;
            }
            if (ChunkHelper.isUnknown(pngChunk) && pngChunk.getChunkGroup() > 0) {
                i2 = pngChunk.getChunkGroup();
            }
            if (i == i2) {
                return true;
            }
            if (i <= i2 || i > i3) {
                return false;
            }
            return true;
        }
        throw new PngjOutputException("bad chunk group?");
    }

    public int writeChunks(OutputStream outputStream, int i) {
        Iterator<PngChunk> it = this.queuedChunks.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            PngChunk next = it.next();
            if (shouldWrite(next, i)) {
                if (ChunkHelper.isCritical(next.f122id) && !next.f122id.equals("PLTE")) {
                    throw new PngjOutputException("bad chunk queued: " + next);
                } else if (!this.alreadyWrittenKeys.containsKey(next.f122id) || next.allowsMultiple()) {
                    next.write(outputStream);
                    this.chunks.add(next);
                    HashMap<String, Integer> hashMap = this.alreadyWrittenKeys;
                    String str = next.f122id;
                    int i3 = 1;
                    if (this.alreadyWrittenKeys.containsKey(next.f122id)) {
                        i3 = 1 + this.alreadyWrittenKeys.get(next.f122id).intValue();
                    }
                    hashMap.put(str, Integer.valueOf(i3));
                    next.setChunkGroup(i);
                    it.remove();
                    i2++;
                } else {
                    throw new PngjOutputException("duplicated chunk does not allow multiple: " + next);
                }
            }
        }
        return i2;
    }

    public List<PngChunk> getQueuedChunks() {
        return this.queuedChunks;
    }

    public String toString() {
        return "ChunkList: written: " + getChunks().size() + " queue: " + this.queuedChunks.size();
    }

    public String toStringFull() {
        StringBuilder sb = new StringBuilder(toString());
        sb.append("\n Written:\n");
        for (PngChunk next : getChunks()) {
            sb.append(next);
            sb.append(" G=" + next.getChunkGroup() + "\n");
        }
        if (!this.queuedChunks.isEmpty()) {
            sb.append(" Queued:\n");
            for (PngChunk append : this.queuedChunks) {
                sb.append(append);
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
