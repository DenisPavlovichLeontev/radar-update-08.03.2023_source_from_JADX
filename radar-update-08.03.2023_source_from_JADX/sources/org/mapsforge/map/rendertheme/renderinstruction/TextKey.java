package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mapsforge.core.model.Tag;

final class TextKey {
    private static final Map<String, TextKey> TEXT_KEYS = new HashMap();
    private final String key;

    static TextKey getInstance(String str) {
        Map<String, TextKey> map = TEXT_KEYS;
        TextKey textKey = map.get(str);
        if (textKey != null) {
            return textKey;
        }
        TextKey textKey2 = new TextKey(str);
        map.put(str, textKey2);
        return textKey2;
    }

    private TextKey(String str) {
        this.key = str;
    }

    /* access modifiers changed from: package-private */
    public String getValue(List<Tag> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (this.key.equals(list.get(i).key)) {
                return list.get(i).value;
            }
        }
        return null;
    }
}
