package org.mapsforge.map.android.util;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.model.common.PreferencesFacade;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderThemeStyleMenu;

public abstract class MapViewerTemplate extends Activity {
    protected MapView mapView;
    protected PreferencesFacade preferencesFacade;
    protected XmlRenderThemeStyleMenu renderThemeStyleMenu;
    protected List<TileCache> tileCaches = new ArrayList();

    /* access modifiers changed from: protected */
    public abstract void createLayers();

    /* access modifiers changed from: protected */
    public abstract void createTileCaches();

    /* access modifiers changed from: protected */
    public HillsRenderConfig getHillsRenderConfig() {
        return null;
    }

    /* access modifiers changed from: protected */
    public abstract int getLayoutId();

    /* access modifiers changed from: protected */
    public abstract String getMapFileName();

    /* access modifiers changed from: protected */
    public abstract int getMapViewId();

    /* access modifiers changed from: protected */
    public float getMaxTextWidthFactor() {
        return 0.7f;
    }

    /* access modifiers changed from: protected */
    public abstract XmlRenderTheme getRenderTheme();

    /* access modifiers changed from: protected */
    public float getScreenRatio() {
        return 1.0f;
    }

    /* access modifiers changed from: protected */
    public byte getZoomLevelDefault() {
        return 12;
    }

    /* access modifiers changed from: protected */
    public byte getZoomLevelMax() {
        return 24;
    }

    /* access modifiers changed from: protected */
    public byte getZoomLevelMin() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public boolean hasZoomControls() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isZoomControlsAutoHide() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void createControls() {
        initializePosition(this.mapView.getModel().mapViewPosition);
    }

    /* access modifiers changed from: protected */
    public void createMapViews() {
        MapView mapView2 = getMapView();
        this.mapView = mapView2;
        mapView2.getModel().init(this.preferencesFacade);
        this.mapView.setClickable(true);
        this.mapView.getMapScaleBar().setVisible(true);
        this.mapView.setBuiltInZoomControls(hasZoomControls());
        this.mapView.getMapZoomControls().setAutoHide(isZoomControlsAutoHide());
        this.mapView.getMapZoomControls().setZoomLevelMin(getZoomLevelMin());
        this.mapView.getMapZoomControls().setZoomLevelMax(getZoomLevelMax());
    }

    /* access modifiers changed from: protected */
    public void createSharedPreferences() {
        this.preferencesFacade = new AndroidPreferences(getSharedPreferences(getPersistableId(), 0));
    }

    /* access modifiers changed from: protected */
    public MapPosition getDefaultInitialPosition() {
        return new MapPosition(new LatLong(0.0d, 0.0d), getZoomLevelDefault());
    }

    /* access modifiers changed from: protected */
    public MapPosition getInitialPosition() {
        MapDataStore mapFile = getMapFile();
        if (mapFile.startPosition() == null) {
            return getDefaultInitialPosition();
        }
        Byte startZoomLevel = mapFile.startZoomLevel();
        if (startZoomLevel == null) {
            startZoomLevel = new Byte((byte) 12);
        }
        return new MapPosition(mapFile.startPosition(), startZoomLevel.byteValue());
    }

    /* access modifiers changed from: protected */
    public File getMapFileDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    /* access modifiers changed from: protected */
    public MapDataStore getMapFile() {
        return new MapFile(new File(getMapFileDirectory(), getMapFileName()));
    }

    /* access modifiers changed from: protected */
    public String getPersistableId() {
        return getClass().getSimpleName();
    }

    /* access modifiers changed from: protected */
    public IMapViewPosition initializePosition(IMapViewPosition iMapViewPosition) {
        if (iMapViewPosition.getCenter().equals(new LatLong(0.0d, 0.0d))) {
            iMapViewPosition.setMapPosition(getInitialPosition());
        }
        iMapViewPosition.setZoomLevelMax(getZoomLevelMax());
        iMapViewPosition.setZoomLevelMin(getZoomLevelMin());
        return iMapViewPosition;
    }

    /* access modifiers changed from: protected */
    public void checkPermissionsAndCreateLayersAndControls() {
        createLayers();
        createControls();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        createSharedPreferences();
        createMapViews();
        createTileCaches();
        checkPermissionsAndCreateLayersAndControls();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.mapView.getModel().save(this.preferencesFacade);
        this.preferencesFacade.save();
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
        this.tileCaches.clear();
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void purgeTileCaches() {
        for (TileCache purge : this.tileCaches) {
            purge.purge();
        }
        this.tileCaches.clear();
    }

    /* access modifiers changed from: protected */
    public void redrawLayers() {
        this.mapView.getLayerManager().redrawLayers();
    }

    /* access modifiers changed from: protected */
    public void setContentView() {
        setContentView(this.mapView);
    }

    /* access modifiers changed from: protected */
    public MapView getMapView() {
        setContentView(getLayoutId());
        return (MapView) findViewById(getMapViewId());
    }
}
