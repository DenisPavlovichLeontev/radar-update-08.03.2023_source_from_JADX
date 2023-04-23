package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.List;
import org.mapsforge.core.model.Tag;

class KeyMatcher implements AttributeMatcher {
    private final List<String> keys;

    KeyMatcher(List<String> list) {
        this.keys = list;
    }

    public boolean isCoveredBy(AttributeMatcher attributeMatcher) {
        if (attributeMatcher == this) {
            return true;
        }
        ArrayList arrayList = new ArrayList(this.keys.size());
        int size = this.keys.size();
        for (int i = 0; i < size; i++) {
            arrayList.add(new Tag(this.keys.get(i), (String) null));
        }
        return attributeMatcher.matches(arrayList);
    }

    public boolean matches(List<Tag> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (this.keys.contains(list.get(i).key)) {
                return true;
            }
        }
        return false;
    }
}
