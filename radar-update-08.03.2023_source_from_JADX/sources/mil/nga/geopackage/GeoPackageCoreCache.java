package mil.nga.geopackage;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mil.nga.geopackage.GeoPackageCore;

public abstract class GeoPackageCoreCache<T extends GeoPackageCore> {
    private Map<String, T> cache = new HashMap();

    public Set<String> getNames() {
        return this.cache.keySet();
    }

    public Collection<T> getGeoPackages() {
        return this.cache.values();
    }

    public T get(String str) {
        return (GeoPackageCore) this.cache.get(str);
    }

    public boolean exists(String str) {
        return this.cache.containsKey(str);
    }

    public void closeAll() {
        for (T close : this.cache.values()) {
            close.close();
        }
        this.cache.clear();
    }

    public void add(T t) {
        this.cache.put(t.getName(), t);
    }

    public T remove(String str) {
        return (GeoPackageCore) this.cache.remove(str);
    }

    public void clear() {
        this.cache.clear();
    }

    public boolean removeAndClose(String str) {
        return close(str);
    }

    public boolean close(String str) {
        GeoPackageCore remove = remove(str);
        if (remove != null) {
            remove.close();
        }
        return remove != null;
    }

    public void closeRetain(Collection<String> collection) {
        HashSet<String> hashSet = new HashSet<>(this.cache.keySet());
        hashSet.removeAll(collection);
        for (String close : hashSet) {
            close(close);
        }
    }

    public void close(Collection<String> collection) {
        for (String close : collection) {
            close(close);
        }
    }
}
