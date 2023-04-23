package org.osmdroid.views.drawing;

import android.os.Handler;
import android.os.Message;

public class MapSnapshotHandler extends Handler {
    private MapSnapshot mMapSnapshot;

    public MapSnapshotHandler(MapSnapshot mapSnapshot) {
        this.mMapSnapshot = mapSnapshot;
    }

    public void handleMessage(Message message) {
        MapSnapshot mapSnapshot;
        if (message.what == 0 && (mapSnapshot = this.mMapSnapshot) != null) {
            mapSnapshot.refreshASAP();
        }
    }

    public void destroy() {
        this.mMapSnapshot = null;
    }
}
