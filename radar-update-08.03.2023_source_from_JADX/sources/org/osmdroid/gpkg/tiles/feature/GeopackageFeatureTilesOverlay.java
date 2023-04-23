package org.osmdroid.gpkg.tiles.feature;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.GeoPackageManager;
import mil.nga.geopackage.factory.GeoPackageFactory;
import mil.nga.geopackage.features.index.FeatureIndexManager;
import mil.nga.geopackage.features.index.FeatureIndexType;
import mil.nga.geopackage.features.user.FeatureDao;
import mil.nga.geopackage.tiles.features.DefaultFeatureTiles;
import mil.nga.geopackage.tiles.features.FeatureTiles;
import mil.nga.geopackage.tiles.features.custom.NumberFeaturesTile;
import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

public class GeopackageFeatureTilesOverlay extends TilesOverlay {
    protected Context ctx;
    protected List<String> databases;
    protected FeatureDao featureDao = null;
    protected FeatureTiles featureTiles = null;
    protected GeoPackage geoPackage = null;
    protected GeoPackageManager manager;
    protected GeoPackageFeatureTileProvider provider;

    public GeopackageFeatureTilesOverlay(GeoPackageFeatureTileProvider geoPackageFeatureTileProvider, Context context) {
        super(geoPackageFeatureTileProvider, context);
        Log.i(IMapView.LOGTAG, "Geopackage support is BETA. Please report any issues");
        this.ctx = context;
        this.provider = geoPackageFeatureTileProvider;
        GeoPackageManager manager2 = GeoPackageFactory.getManager(context);
        this.manager = manager2;
        this.databases = manager2.databases();
    }

    public List<String> getDatabases() {
        return this.databases;
    }

    public List<String> getFeatureTable(String str) throws Exception {
        new ArrayList();
        GeoPackage geoPackage2 = null;
        try {
            GeoPackage open = this.manager.open(str);
            List<String> featureTables = open.getFeatureTables();
            if (open != null) {
                open.close();
            }
            return featureTables;
        } catch (Exception e) {
            throw e;
        } catch (Throwable th) {
            if (geoPackage2 != null) {
                geoPackage2.close();
            }
            throw th;
        }
    }

    public void setDatabaseAndFeatureTable(String str, String str2) {
        if (this.featureDao != null) {
            this.featureDao = null;
        }
        GeoPackage geoPackage2 = this.geoPackage;
        if (geoPackage2 != null) {
            geoPackage2.close();
            this.geoPackage = null;
        }
        GeoPackage open = this.manager.open(str);
        this.geoPackage = open;
        FeatureDao featureDao2 = open.getFeatureDao(str2);
        FeatureIndexManager featureIndexManager = new FeatureIndexManager(this.ctx, this.geoPackage, featureDao2);
        featureIndexManager.setIndexLocation(FeatureIndexType.GEOPACKAGE);
        featureIndexManager.index();
        DefaultFeatureTiles defaultFeatureTiles = new DefaultFeatureTiles(this.ctx, featureDao2);
        this.featureTiles = defaultFeatureTiles;
        defaultFeatureTiles.setMaxFeaturesPerTile(1000);
        this.featureTiles.setMaxFeaturesTileDraw(new NumberFeaturesTile(this.ctx));
        this.featureTiles.setIndexManager(featureIndexManager);
        this.provider.set(featureDao2.getZoomLevel(), this.featureTiles);
    }

    public void onDetach(MapView mapView) {
        super.onDetach(mapView);
        GeoPackage geoPackage2 = this.geoPackage;
        if (geoPackage2 != null) {
            geoPackage2.close();
            this.geoPackage = null;
        }
        this.featureDao = null;
        this.featureTiles = null;
    }
}
