package org.mapsforge.map.reader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import org.mapsforge.core.util.LRUCache;
import org.mapsforge.map.reader.header.SubFileParameter;

class IndexCache {
    private static final int INDEX_ENTRIES_PER_BLOCK = 128;
    private static final int SIZE_OF_INDEX_BLOCK = 640;
    private final FileChannel fileChannel;
    private final Map<IndexCacheEntryKey, byte[]> map;

    IndexCache(FileChannel fileChannel2, int i) {
        this.fileChannel = fileChannel2;
        this.map = new LRUCache(i);
    }

    /* access modifiers changed from: package-private */
    public void destroy() {
        this.map.clear();
    }

    /* access modifiers changed from: package-private */
    public long getIndexEntry(SubFileParameter subFileParameter, long j) throws IOException {
        if (j < subFileParameter.numberOfBlocks) {
            long j2 = j / 128;
            IndexCacheEntryKey indexCacheEntryKey = new IndexCacheEntryKey(subFileParameter, j2);
            byte[] bArr = this.map.get(indexCacheEntryKey);
            if (bArr == null) {
                long j3 = subFileParameter.indexStartAddress + (j2 * 640);
                int min = Math.min(SIZE_OF_INDEX_BLOCK, (int) (subFileParameter.indexEndAddress - j3));
                byte[] bArr2 = new byte[min];
                ByteBuffer wrap = ByteBuffer.wrap(bArr2, 0, min);
                synchronized (this.fileChannel) {
                    this.fileChannel.position(j3);
                    if (this.fileChannel.read(wrap) != min) {
                        throw new IOException("could not read index block with size: " + min);
                    }
                }
                this.map.put(indexCacheEntryKey, bArr2);
                bArr = bArr2;
            }
            return Deserializer.getFiveBytesLong(bArr, (int) ((j % 128) * 5));
        }
        throw new IOException("invalid block number: " + j);
    }
}
