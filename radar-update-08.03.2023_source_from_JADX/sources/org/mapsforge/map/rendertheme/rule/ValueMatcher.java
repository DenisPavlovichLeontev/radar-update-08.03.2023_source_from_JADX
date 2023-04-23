package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.List;
import org.mapsforge.core.model.Tag;

class ValueMatcher implements AttributeMatcher {
    private final List<String> values;

    ValueMatcher(List<String> list) {
        this.values = list;
    }

    public boolean isCoveredBy(AttributeMatcher attributeMatcher) {
        if (attributeMatcher == this) {
            return true;
        }
        ArrayList arrayList = new ArrayList(this.values.size());
        int size = this.values.size();
        for (int i = 0; i < size; i++) {
            arrayList.add(new Tag((String) null, this.values.get(i)));
        }
        return attributeMatcher.matches(arrayList);
    }

    public boolean matches(List<Tag> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (this.values.contains(list.get(i).value)) {
                return true;
            }
        }
        return false;
    }
}
