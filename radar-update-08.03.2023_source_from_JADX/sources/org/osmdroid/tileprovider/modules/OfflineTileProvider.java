package org.osmdroid.tileprovider.modules;

import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.IMapTileProviderCallback;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.tilesource.FileBasedTileSource;

public class OfflineTileProvider extends MapTileProviderArray implements IMapTileProviderCallback {
    private IArchiveFile[] archives;

    /* access modifiers changed from: protected */
    public boolean isDowngradedMode(long j) {
        return true;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public OfflineTileProvider(IRegisterReceiver iRegisterReceiver, File[] fileArr) {
        super(FileBasedTileSource.getSource(fileArr[0].getName()), iRegisterReceiver);
        ArrayList arrayList = new ArrayList();
        for (File file : fileArr) {
            IArchiveFile archiveFile = ArchiveFileFactory.getArchiveFile(file);
            if (archiveFile != null) {
                arrayList.add(archiveFile);
            } else {
                Log.w(IMapView.LOGTAG, "Skipping " + file + ", no tile provider is registered to handle the file extension");
            }
        }
        IArchiveFile[] iArchiveFileArr = new IArchiveFile[arrayList.size()];
        this.archives = iArchiveFileArr;
        this.archives = (IArchiveFile[]) arrayList.toArray(iArchiveFileArr);
        MapTileFileArchiveProvider mapTileFileArchiveProvider = new MapTileFileArchiveProvider(iRegisterReceiver, getTileSource(), this.archives);
        this.mTileProviderList.add(mapTileFileArchiveProvider);
        MapTileApproximater mapTileApproximater = new MapTileApproximater();
        this.mTileProviderList.add(mapTileApproximater);
        mapTileApproximater.addProvider(mapTileFileArchiveProvider);
    }

    public IArchiveFile[] getArchives() {
        return this.archives;
    }

    public void detach() {
        IArchiveFile[] iArchiveFileArr = this.archives;
        if (iArchiveFileArr != null) {
            for (IArchiveFile close : iArchiveFileArr) {
                close.close();
            }
        }
        super.detach();
    }
}
