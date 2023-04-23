package com.j256.ormlite.dao;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.misc.IOUtils;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class LazyForeignCollection<T, ID> extends BaseForeignCollection<T, ID> implements Serializable {
    private static final long serialVersionUID = -5460708106909626233L;
    private transient CloseableIterator<T> lastIterator;

    public boolean isEager() {
        return false;
    }

    public int refreshCollection() {
        return 0;
    }

    public LazyForeignCollection(Dao<T, ID> dao, Object obj, Object obj2, FieldType fieldType, String str, boolean z) {
        super(dao, obj, obj2, fieldType, str, z);
    }

    public CloseableIterator<T> iterator() {
        return closeableIterator(-1);
    }

    public CloseableIterator<T> iterator(int i) {
        return closeableIterator(i);
    }

    public CloseableIterator<T> closeableIterator() {
        return closeableIterator(-1);
    }

    public CloseableIterator<T> closeableIterator(int i) {
        try {
            return iteratorThrow(i);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not build lazy iterator for " + this.dao.getDataClass(), e);
        }
    }

    public CloseableIterator<T> iteratorThrow() throws SQLException {
        return iteratorThrow(-1);
    }

    public CloseableIterator<T> iteratorThrow(int i) throws SQLException {
        CloseableIterator<T> seperateIteratorThrow = seperateIteratorThrow(i);
        this.lastIterator = seperateIteratorThrow;
        return seperateIteratorThrow;
    }

    public CloseableWrappedIterable<T> getWrappedIterable() {
        return getWrappedIterable(-1);
    }

    public CloseableWrappedIterable<T> getWrappedIterable(final int i) {
        return new CloseableWrappedIterableImpl(new CloseableIterable<T>() {
            public CloseableIterator<T> iterator() {
                return closeableIterator();
            }

            public CloseableIterator<T> closeableIterator() {
                try {
                    return LazyForeignCollection.this.seperateIteratorThrow(i);
                } catch (Exception e) {
                    throw new IllegalStateException("Could not build lazy iterator for " + LazyForeignCollection.this.dao.getDataClass(), e);
                }
            }
        });
    }

    public void closeLastIterator() throws IOException {
        CloseableIterator<T> closeableIterator = this.lastIterator;
        if (closeableIterator != null) {
            closeableIterator.close();
            this.lastIterator = null;
        }
    }

    public int size() {
        CloseableIterator it = iterator();
        int i = 0;
        while (it.hasNext()) {
            try {
                it.moveToNext();
                i++;
            } finally {
                IOUtils.closeQuietly(it);
            }
        }
        return i;
    }

    public boolean isEmpty() {
        CloseableIterator it = iterator();
        try {
            return !it.hasNext();
        } finally {
            IOUtils.closeQuietly(it);
        }
    }

    public boolean contains(Object obj) {
        boolean z;
        CloseableIterator it = iterator();
        while (true) {
            try {
                if (it.hasNext()) {
                    if (it.next().equals(obj)) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            } finally {
                IOUtils.closeQuietly(it);
            }
        }
        return z;
    }

    public boolean containsAll(Collection<?> collection) {
        HashSet hashSet = new HashSet(collection);
        CloseableIterator it = iterator();
        while (it.hasNext()) {
            try {
                hashSet.remove(it.next());
            } finally {
                IOUtils.closeQuietly(it);
            }
        }
        return hashSet.isEmpty();
    }

    public boolean remove(Object obj) {
        boolean z;
        CloseableIterator it = iterator();
        while (true) {
            try {
                if (it.hasNext()) {
                    if (it.next().equals(obj)) {
                        it.remove();
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            } finally {
                IOUtils.closeQuietly(it);
            }
        }
        return z;
    }

    public boolean removeAll(Collection<?> collection) {
        CloseableIterator it = iterator();
        boolean z = false;
        while (it.hasNext()) {
            try {
                if (collection.contains(it.next())) {
                    it.remove();
                    z = true;
                }
            } finally {
                IOUtils.closeQuietly(it);
            }
        }
        return z;
    }

    public Object[] toArray() {
        ArrayList arrayList = new ArrayList();
        CloseableIterator it = iterator();
        while (it.hasNext()) {
            try {
                arrayList.add(it.next());
            } finally {
                IOUtils.closeQuietly(it);
            }
        }
        return arrayList.toArray();
    }

    public <E> E[] toArray(E[] eArr) {
        CloseableIterator it = iterator();
        int i = 0;
        ArrayList arrayList = null;
        while (it.hasNext()) {
            try {
                E next = it.next();
                if (i >= eArr.length) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                        for (E add : eArr) {
                            arrayList.add(add);
                        }
                    }
                    arrayList.add(next);
                } else {
                    eArr[i] = next;
                }
                i++;
            } finally {
                IOUtils.closeQuietly(it);
            }
        }
        if (arrayList != null) {
            return arrayList.toArray(eArr);
        }
        if (i < eArr.length - 1) {
            eArr[i] = null;
        }
        return eArr;
    }

    public int updateAll() {
        throw new UnsupportedOperationException("Cannot call updateAll() on a lazy collection.");
    }

    public int refreshAll() {
        throw new UnsupportedOperationException("Cannot call updateAll() on a lazy collection.");
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    /* access modifiers changed from: private */
    public CloseableIterator<T> seperateIteratorThrow(int i) throws SQLException {
        if (this.dao != null) {
            return this.dao.iterator(getPreparedQuery(), i);
        }
        throw new IllegalStateException("Internal DAO object is null.  Maybe the collection was deserialized or otherwise constructed wrongly.  Use dao.assignEmptyForeignCollection(...) or dao.getEmptyForeignCollection(...) instead");
    }
}
