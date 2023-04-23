package org.mapsforge.map.rendertheme.rule;

import java.util.List;
import org.mapsforge.core.model.Tag;

class NegativeMatcher implements AttributeMatcher {
    private final List<String> keyList;
    private final List<String> valueList;

    public boolean isCoveredBy(AttributeMatcher attributeMatcher) {
        return false;
    }

    NegativeMatcher(List<String> list, List<String> list2) {
        this.keyList = list;
        this.valueList = list2;
    }

    public boolean matches(List<Tag> list) {
        if (keyListDoesNotContainKeys(list)) {
            return true;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (this.valueList.contains(list.get(i).value)) {
                return true;
            }
        }
        return false;
    }

    private boolean keyListDoesNotContainKeys(List<Tag> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (this.keyList.contains(list.get(i).key)) {
                return false;
            }
        }
        return true;
    }
}
