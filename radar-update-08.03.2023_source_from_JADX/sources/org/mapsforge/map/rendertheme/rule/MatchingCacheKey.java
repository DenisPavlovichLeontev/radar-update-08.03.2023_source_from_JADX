package org.mapsforge.map.rendertheme.rule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mapsforge.core.model.Tag;

class MatchingCacheKey {
    private final Closed closed;
    private final List<Tag> tags;
    private final Set<Tag> tagsWithoutName = new HashSet();
    private final byte zoomLevel;

    MatchingCacheKey(List<Tag> list, byte b, Closed closed2) {
        this.tags = list;
        this.zoomLevel = b;
        this.closed = closed2;
        if (list != null) {
            for (Tag next : list) {
                if (!"name".equals(next.key)) {
                    this.tagsWithoutName.add(next);
                }
            }
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MatchingCacheKey)) {
            return false;
        }
        MatchingCacheKey matchingCacheKey = (MatchingCacheKey) obj;
        if (this.closed != matchingCacheKey.closed) {
            return false;
        }
        Set<Tag> set = this.tagsWithoutName;
        return (set != null || matchingCacheKey.tagsWithoutName == null) && set.equals(matchingCacheKey.tagsWithoutName) && this.zoomLevel == matchingCacheKey.zoomLevel;
    }

    public int hashCode() {
        Closed closed2 = this.closed;
        return (((((closed2 == null ? 0 : closed2.hashCode()) + 31) * 31) + this.tagsWithoutName.hashCode()) * 31) + this.zoomLevel;
    }
}
