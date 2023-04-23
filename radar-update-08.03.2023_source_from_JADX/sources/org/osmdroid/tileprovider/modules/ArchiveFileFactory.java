package org.osmdroid.tileprovider.modules;

import android.os.Build;
import android.util.Log;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mil.nga.geopackage.property.PropertyConstants;
import org.osmdroid.api.IMapView;

public class ArchiveFileFactory {
    static Map<String, Class<? extends IArchiveFile>> extensionMap;

    static {
        HashMap hashMap = new HashMap();
        extensionMap = hashMap;
        hashMap.put("zip", ZipFileArchive.class);
        if (Build.VERSION.SDK_INT >= 10) {
            extensionMap.put("sqlite", DatabaseFileArchive.class);
            extensionMap.put("mbtiles", MBTilesFileArchive.class);
            extensionMap.put("gemf", GEMFFileArchive.class);
        }
    }

    public static boolean isFileExtensionRegistered(String str) {
        return extensionMap.containsKey(str);
    }

    public static void registerArchiveFileProvider(Class<? extends IArchiveFile> cls, String str) {
        extensionMap.put(str, cls);
    }

    public static IArchiveFile getArchiveFile(File file) {
        String name = file.getName();
        if (name.contains(PropertyConstants.PROPERTY_DIVIDER)) {
            try {
                name = name.substring(name.lastIndexOf(PropertyConstants.PROPERTY_DIVIDER) + 1);
            } catch (Exception unused) {
            }
        }
        Class cls = extensionMap.get(name.toLowerCase());
        if (cls == null) {
            return null;
        }
        try {
            IArchiveFile iArchiveFile = (IArchiveFile) cls.newInstance();
            iArchiveFile.init(file);
            return iArchiveFile;
        } catch (InstantiationException e) {
            Log.e(IMapView.LOGTAG, "Error initializing archive file provider " + file.getAbsolutePath(), e);
            return null;
        } catch (IllegalAccessException e2) {
            Log.e(IMapView.LOGTAG, "Error initializing archive file provider " + file.getAbsolutePath(), e2);
            return null;
        } catch (Exception e3) {
            Log.e(IMapView.LOGTAG, "Error opening archive file " + file.getAbsolutePath(), e3);
            return null;
        }
    }

    public static Set<String> getRegisteredExtensions() {
        HashSet hashSet = new HashSet();
        hashSet.addAll(extensionMap.keySet());
        return hashSet;
    }
}
