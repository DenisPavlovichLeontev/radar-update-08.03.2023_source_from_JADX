package org.mapsforge.core.model;

import java.io.Serializable;

public class Tag implements Comparable<Tag>, Serializable {
    public static final char KEY_VALUE_SEPARATOR = '=';
    private static final long serialVersionUID = 1;
    public final String key;
    public final String value;

    public Tag(String str) {
        this(str, str.indexOf(61));
    }

    public Tag(String str, String str2) {
        this.key = str;
        this.value = str2;
    }

    private Tag(String str, int i) {
        this(str.substring(0, i), str.substring(i + 1));
    }

    public int compareTo(Tag tag) {
        int compareTo = this.key.compareTo(tag.key);
        if (compareTo != 0) {
            return compareTo;
        }
        return this.value.compareTo(tag.value);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag) obj;
        String str = this.key;
        if (str == null) {
            if (tag.key != null) {
                return false;
            }
        } else if (!str.equals(tag.key)) {
            return false;
        } else {
            String str2 = this.value;
            if (str2 == null) {
                if (tag.value != null) {
                    return false;
                }
            } else if (!str2.equals(tag.value)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        String str = this.key;
        int i = 0;
        int hashCode = ((str == null ? 0 : str.hashCode()) + 31) * 31;
        String str2 = this.value;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        return "key=" + this.key + ", value=" + this.value;
    }
}
