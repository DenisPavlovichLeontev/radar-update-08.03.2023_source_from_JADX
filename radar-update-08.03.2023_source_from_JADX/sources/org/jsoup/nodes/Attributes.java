package org.jsoup.nodes;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseSettings;

public class Attributes implements Iterable<Attribute>, Cloneable {
    private static final String EmptyString = "";
    private static final int GrowthFactor = 2;
    private static final int InitialCapacity = 3;
    static final char InternalPrefix = '/';
    static final int NotFound = -1;
    protected static final String dataPrefix = "data-";
    String[] keys = new String[3];
    /* access modifiers changed from: private */
    public int size = 0;
    Object[] vals = new Object[3];

    private void checkCapacity(int i) {
        Validate.isTrue(i >= this.size);
        String[] strArr = this.keys;
        int length = strArr.length;
        if (length < i) {
            int i2 = 3;
            if (length >= 3) {
                i2 = this.size * 2;
            }
            if (i <= i2) {
                i = i2;
            }
            this.keys = (String[]) Arrays.copyOf(strArr, i);
            this.vals = Arrays.copyOf(this.vals, i);
        }
    }

    /* access modifiers changed from: package-private */
    public int indexOfKey(String str) {
        Validate.notNull(str);
        for (int i = 0; i < this.size; i++) {
            if (str.equals(this.keys[i])) {
                return i;
            }
        }
        return -1;
    }

    private int indexOfKeyIgnoreCase(String str) {
        Validate.notNull(str);
        for (int i = 0; i < this.size; i++) {
            if (str.equalsIgnoreCase(this.keys[i])) {
                return i;
            }
        }
        return -1;
    }

    static String checkNotNull(@Nullable Object obj) {
        return obj == null ? "" : (String) obj;
    }

    public String get(String str) {
        int indexOfKey = indexOfKey(str);
        if (indexOfKey == -1) {
            return "";
        }
        return checkNotNull(this.vals[indexOfKey]);
    }

    public String getIgnoreCase(String str) {
        int indexOfKeyIgnoreCase = indexOfKeyIgnoreCase(str);
        if (indexOfKeyIgnoreCase == -1) {
            return "";
        }
        return checkNotNull(this.vals[indexOfKeyIgnoreCase]);
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public Object getUserData(String str) {
        Validate.notNull(str);
        if (!isInternalKey(str)) {
            str = internalKey(str);
        }
        int indexOfKeyIgnoreCase = indexOfKeyIgnoreCase(str);
        if (indexOfKeyIgnoreCase == -1) {
            return null;
        }
        return this.vals[indexOfKeyIgnoreCase];
    }

    public Attributes add(String str, @Nullable String str2) {
        addObject(str, str2);
        return this;
    }

    private void addObject(String str, @Nullable Object obj) {
        checkCapacity(this.size + 1);
        String[] strArr = this.keys;
        int i = this.size;
        strArr[i] = str;
        this.vals[i] = obj;
        this.size = i + 1;
    }

    public Attributes put(String str, @Nullable String str2) {
        Validate.notNull(str);
        int indexOfKey = indexOfKey(str);
        if (indexOfKey != -1) {
            this.vals[indexOfKey] = str2;
        } else {
            add(str, str2);
        }
        return this;
    }

    /* access modifiers changed from: package-private */
    public Attributes putUserData(String str, Object obj) {
        Validate.notNull(str);
        if (!isInternalKey(str)) {
            str = internalKey(str);
        }
        Validate.notNull(obj);
        int indexOfKey = indexOfKey(str);
        if (indexOfKey != -1) {
            this.vals[indexOfKey] = obj;
        } else {
            addObject(str, obj);
        }
        return this;
    }

    /* access modifiers changed from: package-private */
    public void putIgnoreCase(String str, @Nullable String str2) {
        int indexOfKeyIgnoreCase = indexOfKeyIgnoreCase(str);
        if (indexOfKeyIgnoreCase != -1) {
            this.vals[indexOfKeyIgnoreCase] = str2;
            if (!this.keys[indexOfKeyIgnoreCase].equals(str)) {
                this.keys[indexOfKeyIgnoreCase] = str;
                return;
            }
            return;
        }
        add(str, str2);
    }

    public Attributes put(String str, boolean z) {
        if (z) {
            putIgnoreCase(str, (String) null);
        } else {
            remove(str);
        }
        return this;
    }

    public Attributes put(Attribute attribute) {
        Validate.notNull(attribute);
        put(attribute.getKey(), attribute.getValue());
        attribute.parent = this;
        return this;
    }

    /* access modifiers changed from: private */
    public void remove(int i) {
        Validate.isFalse(i >= this.size);
        int i2 = (this.size - i) - 1;
        if (i2 > 0) {
            String[] strArr = this.keys;
            int i3 = i + 1;
            System.arraycopy(strArr, i3, strArr, i, i2);
            Object[] objArr = this.vals;
            System.arraycopy(objArr, i3, objArr, i, i2);
        }
        int i4 = this.size - 1;
        this.size = i4;
        this.keys[i4] = null;
        this.vals[i4] = null;
    }

    public void remove(String str) {
        int indexOfKey = indexOfKey(str);
        if (indexOfKey != -1) {
            remove(indexOfKey);
        }
    }

    public void removeIgnoreCase(String str) {
        int indexOfKeyIgnoreCase = indexOfKeyIgnoreCase(str);
        if (indexOfKeyIgnoreCase != -1) {
            remove(indexOfKeyIgnoreCase);
        }
    }

    public boolean hasKey(String str) {
        return indexOfKey(str) != -1;
    }

    public boolean hasKeyIgnoreCase(String str) {
        return indexOfKeyIgnoreCase(str) != -1;
    }

    public boolean hasDeclaredValueForKey(String str) {
        int indexOfKey = indexOfKey(str);
        return (indexOfKey == -1 || this.vals[indexOfKey] == null) ? false : true;
    }

    public boolean hasDeclaredValueForKeyIgnoreCase(String str) {
        int indexOfKeyIgnoreCase = indexOfKeyIgnoreCase(str);
        return (indexOfKeyIgnoreCase == -1 || this.vals[indexOfKeyIgnoreCase] == null) ? false : true;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void addAll(Attributes attributes) {
        if (attributes.size() != 0) {
            checkCapacity(this.size + attributes.size);
            boolean z = this.size != 0;
            Iterator<Attribute> it = attributes.iterator();
            while (it.hasNext()) {
                Attribute next = it.next();
                if (z) {
                    put(next);
                } else {
                    add(next.getKey(), next.getValue());
                }
            }
        }
    }

    public Iterator<Attribute> iterator() {
        return new Iterator<Attribute>() {

            /* renamed from: i */
            int f372i = 0;

            public boolean hasNext() {
                while (this.f372i < Attributes.this.size) {
                    Attributes attributes = Attributes.this;
                    if (!attributes.isInternalKey(attributes.keys[this.f372i])) {
                        break;
                    }
                    this.f372i++;
                }
                if (this.f372i < Attributes.this.size) {
                    return true;
                }
                return false;
            }

            public Attribute next() {
                Attribute attribute = new Attribute(Attributes.this.keys[this.f372i], (String) Attributes.this.vals[this.f372i], Attributes.this);
                this.f372i++;
                return attribute;
            }

            public void remove() {
                Attributes attributes = Attributes.this;
                int i = this.f372i - 1;
                this.f372i = i;
                attributes.remove(i);
            }
        };
    }

    public List<Attribute> asList() {
        ArrayList arrayList = new ArrayList(this.size);
        for (int i = 0; i < this.size; i++) {
            if (!isInternalKey(this.keys[i])) {
                arrayList.add(new Attribute(this.keys[i], (String) this.vals[i], this));
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    public Map<String, String> dataset() {
        return new Dataset();
    }

    public String html() {
        StringBuilder borrowBuilder = StringUtil.borrowBuilder();
        try {
            html(borrowBuilder, new Document("").outputSettings());
            return StringUtil.releaseBuilder(borrowBuilder);
        } catch (IOException e) {
            throw new SerializationException((Throwable) e);
        }
    }

    /* access modifiers changed from: package-private */
    public final void html(Appendable appendable, Document.OutputSettings outputSettings) throws IOException {
        String validKey;
        int i = this.size;
        for (int i2 = 0; i2 < i; i2++) {
            if (!isInternalKey(this.keys[i2]) && (validKey = Attribute.getValidKey(this.keys[i2], outputSettings.syntax())) != null) {
                Attribute.htmlNoValidate(validKey, (String) this.vals[i2], appendable.append(' '), outputSettings);
            }
        }
    }

    public String toString() {
        return html();
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Attributes attributes = (Attributes) obj;
        if (this.size != attributes.size) {
            return false;
        }
        for (int i = 0; i < this.size; i++) {
            int indexOfKey = attributes.indexOfKey(this.keys[i]);
            if (indexOfKey == -1) {
                return false;
            }
            Object obj2 = this.vals[i];
            Object obj3 = attributes.vals[indexOfKey];
            if (obj2 == null) {
                if (obj3 != null) {
                    return false;
                }
            } else if (!obj2.equals(obj3)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return (((this.size * 31) + Arrays.hashCode(this.keys)) * 31) + Arrays.hashCode(this.vals);
    }

    public Attributes clone() {
        try {
            Attributes attributes = (Attributes) super.clone();
            attributes.size = this.size;
            attributes.keys = (String[]) Arrays.copyOf(this.keys, this.size);
            attributes.vals = Arrays.copyOf(this.vals, this.size);
            return attributes;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void normalize() {
        for (int i = 0; i < this.size; i++) {
            String[] strArr = this.keys;
            strArr[i] = Normalizer.lowerCase(strArr[i]);
        }
    }

    public int deduplicate(ParseSettings parseSettings) {
        String str;
        int i = 0;
        if (isEmpty()) {
            return 0;
        }
        boolean preserveAttributeCase = parseSettings.preserveAttributeCase();
        int i2 = 0;
        while (i < this.keys.length) {
            int i3 = i + 1;
            int i4 = i3;
            while (true) {
                String[] strArr = this.keys;
                if (i4 >= strArr.length || (str = strArr[i4]) == null) {
                    i = i3;
                } else {
                    if (!preserveAttributeCase || !strArr[i].equals(str)) {
                        if (!preserveAttributeCase) {
                            String[] strArr2 = this.keys;
                            if (!strArr2[i].equalsIgnoreCase(strArr2[i4])) {
                            }
                        }
                        i4++;
                    }
                    i2++;
                    remove(i4);
                    i4--;
                    i4++;
                }
            }
            i = i3;
        }
        return i2;
    }

    private static class Dataset extends AbstractMap<String, String> {
        /* access modifiers changed from: private */
        public final Attributes attributes;

        private Dataset(Attributes attributes2) {
            this.attributes = attributes2;
        }

        public Set<Map.Entry<String, String>> entrySet() {
            return new EntrySet();
        }

        public String put(String str, String str2) {
            String access$500 = Attributes.dataKey(str);
            String str3 = this.attributes.hasKey(access$500) ? this.attributes.get(access$500) : null;
            this.attributes.put(access$500, str2);
            return str3;
        }

        private class EntrySet extends AbstractSet<Map.Entry<String, String>> {
            private EntrySet() {
            }

            public Iterator<Map.Entry<String, String>> iterator() {
                return new DatasetIterator();
            }

            public int size() {
                int i = 0;
                while (new DatasetIterator().hasNext()) {
                    i++;
                }
                return i;
            }
        }

        private class DatasetIterator implements Iterator<Map.Entry<String, String>> {
            private Attribute attr;
            private Iterator<Attribute> attrIter;

            private DatasetIterator() {
                this.attrIter = Dataset.this.attributes.iterator();
            }

            public boolean hasNext() {
                while (this.attrIter.hasNext()) {
                    Attribute next = this.attrIter.next();
                    this.attr = next;
                    if (next.isDataAttribute()) {
                        return true;
                    }
                }
                return false;
            }

            public Map.Entry<String, String> next() {
                return new Attribute(this.attr.getKey().substring(5), this.attr.getValue());
            }

            public void remove() {
                Dataset.this.attributes.remove(this.attr.getKey());
            }
        }
    }

    /* access modifiers changed from: private */
    public static String dataKey(String str) {
        return dataPrefix + str;
    }

    static String internalKey(String str) {
        return InternalPrefix + str;
    }

    /* access modifiers changed from: private */
    public boolean isInternalKey(String str) {
        return str != null && str.length() > 1 && str.charAt(0) == '/';
    }
}
