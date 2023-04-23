package org.osmdroid.tileprovider.modules;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class ZipFileArchive implements IArchiveFile {
    private boolean mIgnoreTileSource = false;
    protected ZipFile mZipFile;

    public ZipFileArchive() {
    }

    private ZipFileArchive(ZipFile zipFile) {
        this.mZipFile = zipFile;
    }

    public static ZipFileArchive getZipFileArchive(File file) throws ZipException, IOException {
        return new ZipFileArchive(new ZipFile(file));
    }

    public void setIgnoreTileSource(boolean z) {
        this.mIgnoreTileSource = z;
    }

    public void init(File file) throws Exception {
        this.mZipFile = new ZipFile(file);
    }

    public InputStream getInputStream(ITileSource iTileSource, long j) {
        ZipEntry entry;
        try {
            if (!this.mIgnoreTileSource) {
                ZipEntry entry2 = this.mZipFile.getEntry(iTileSource.getTileRelativeFilenameString(j));
                if (entry2 != null) {
                    return this.mZipFile.getInputStream(entry2);
                }
                return null;
            }
            Enumeration<? extends ZipEntry> entries = this.mZipFile.entries();
            while (entries.hasMoreElements()) {
                String name = ((ZipEntry) entries.nextElement()).getName();
                if (name.contains("/") && (entry = this.mZipFile.getEntry(getTileRelativeFilenameString(j, name.split("/")[0]))) != null) {
                    return this.mZipFile.getInputStream(entry);
                }
            }
            return null;
        } catch (IOException e) {
            Log.w(IMapView.LOGTAG, "Error getting zip stream: " + MapTileIndex.toString(j), e);
            return null;
        }
    }

    private String getTileRelativeFilenameString(long j, String str) {
        return str + '/' + MapTileIndex.getZoom(j) + '/' + MapTileIndex.getX(j) + '/' + MapTileIndex.getY(j) + ".png";
    }

    public Set<String> getTileSources() {
        HashSet hashSet = new HashSet();
        try {
            Enumeration<? extends ZipEntry> entries = this.mZipFile.entries();
            while (entries.hasMoreElements()) {
                String name = ((ZipEntry) entries.nextElement()).getName();
                if (name.contains("/")) {
                    hashSet.add(name.split("/")[0]);
                }
            }
        } catch (Exception e) {
            Log.w(IMapView.LOGTAG, "Error getting tile sources: ", e);
        }
        return hashSet;
    }

    public void close() {
        try {
            this.mZipFile.close();
        } catch (IOException unused) {
        }
    }

    public String toString() {
        return "ZipFileArchive [mZipFile=" + this.mZipFile.getName() + "]";
    }
}
