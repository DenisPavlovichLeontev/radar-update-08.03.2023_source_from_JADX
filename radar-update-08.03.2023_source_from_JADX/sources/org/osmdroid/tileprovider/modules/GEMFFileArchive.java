package org.osmdroid.tileprovider.modules;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.GEMFFile;
import org.osmdroid.util.MapTileIndex;

public class GEMFFileArchive implements IArchiveFile {
    private GEMFFile mFile;

    public void setIgnoreTileSource(boolean z) {
    }

    public GEMFFileArchive() {
    }

    private GEMFFileArchive(File file) throws IOException {
        this.mFile = new GEMFFile(file);
    }

    public static GEMFFileArchive getGEMFFileArchive(File file) throws IOException {
        return new GEMFFileArchive(file);
    }

    public void init(File file) throws Exception {
        this.mFile = new GEMFFile(file);
    }

    public InputStream getInputStream(ITileSource iTileSource, long j) {
        return this.mFile.getInputStream(MapTileIndex.getX(j), MapTileIndex.getY(j), MapTileIndex.getZoom(j));
    }

    public Set<String> getTileSources() {
        HashSet hashSet = new HashSet();
        try {
            hashSet.addAll(this.mFile.getSources().values());
        } catch (Exception e) {
            Log.w(IMapView.LOGTAG, "Error getting tile sources: ", e);
        }
        return hashSet;
    }

    public void close() {
        try {
            this.mFile.close();
        } catch (IOException unused) {
        }
    }

    public String toString() {
        return "GEMFFileArchive [mGEMFFile=" + this.mFile.getName() + "]";
    }
}
