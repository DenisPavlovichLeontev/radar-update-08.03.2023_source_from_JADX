package org.mapsforge.map.reader;

import org.mapsforge.map.reader.header.SubFileParameter;

class IndexCacheEntryKey {
    private final int hashCodeValue = calculateHashCode();
    private final long indexBlockNumber;
    private final SubFileParameter subFileParameter;

    IndexCacheEntryKey(SubFileParameter subFileParameter2, long j) {
        this.subFileParameter = subFileParameter2;
        this.indexBlockNumber = j;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IndexCacheEntryKey)) {
            return false;
        }
        IndexCacheEntryKey indexCacheEntryKey = (IndexCacheEntryKey) obj;
        SubFileParameter subFileParameter2 = this.subFileParameter;
        if (subFileParameter2 != null || indexCacheEntryKey.subFileParameter == null) {
            return (subFileParameter2 == null || subFileParameter2.equals(indexCacheEntryKey.subFileParameter)) && this.indexBlockNumber == indexCacheEntryKey.indexBlockNumber;
        }
        return false;
    }

    public int hashCode() {
        return this.hashCodeValue;
    }

    private int calculateHashCode() {
        SubFileParameter subFileParameter2 = this.subFileParameter;
        int hashCode = subFileParameter2 == null ? 0 : subFileParameter2.hashCode();
        long j = this.indexBlockNumber;
        return ((217 + hashCode) * 31) + ((int) (j ^ (j >>> 32)));
    }
}
