package org.osmdroid.mapsforge;

import android.app.Application;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.graphics.AndroidTileBitmap;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.renderer.DirectRenderer;
import org.mapsforge.map.layer.renderer.RendererJob;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.views.MapView;

public class MapsForgeTileSource extends BitmapTileSourceBase {
    public static int MAX_ZOOM = 29;
    public static int MIN_ZOOM = 3;
    public static final int TILE_SIZE_PIXELS = 256;
    private HillsRenderConfig hillsRenderConfig;
    private XmlRenderTheme mXmlRenderTheme;
    private MultiMapDataStore mapDatabase;
    private final DisplayModel model;
    private DirectRenderer renderer;
    private final float scale;
    private RenderThemeFuture theme;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    protected MapsForgeTileSource(String str, int i, int i2, int i3, File[] fileArr, XmlRenderTheme xmlRenderTheme, MultiMapDataStore.DataPolicy dataPolicy, HillsRenderConfig hillsRenderConfig2, String str2) {
        super(str, i, i2, i3, ".png", "© OpenStreetMap contributors");
        File[] fileArr2 = fileArr;
        this.model = new DisplayModel();
        this.scale = DisplayModel.getDefaultUserScaleFactor();
        this.theme = null;
        this.mXmlRenderTheme = null;
        this.mapDatabase = new MultiMapDataStore(dataPolicy);
        for (File mapFile : fileArr2) {
            this.mapDatabase.addMapDataStore(new MapFile(mapFile, str2), false, false);
        }
        if (AndroidGraphicFactory.INSTANCE != null) {
            DirectRenderer directRenderer = new DirectRenderer(this.mapDatabase, AndroidGraphicFactory.INSTANCE, true, hillsRenderConfig2);
            this.renderer = directRenderer;
            int i4 = MIN_ZOOM;
            byte zoomLevelMax = directRenderer.getZoomLevelMax();
            StringBuilder sb = new StringBuilder();
            sb.append("min=");
            sb.append(i4);
            sb.append(" max=");
            sb.append(zoomLevelMax);
            sb.append(" tilesize=");
            int i5 = i3;
            sb.append(i3);
            Log.d(IMapView.LOGTAG, sb.toString());
            XmlRenderTheme xmlRenderTheme2 = xmlRenderTheme == null ? InternalRenderTheme.OSMARENDER : xmlRenderTheme;
            if (xmlRenderTheme2 != this.mXmlRenderTheme || this.theme == null) {
                this.theme = new RenderThemeFuture(AndroidGraphicFactory.INSTANCE, xmlRenderTheme2, this.model);
                new Thread(this.theme).start();
                return;
            }
            return;
        }
        throw new RuntimeException("Must call MapsForgeTileSource.createInstance(context.getApplication()); once before MapsForgeTileSource.createFromFiles().");
    }

    protected MapsForgeTileSource(String str, int i, int i2, int i3, File[] fileArr, XmlRenderTheme xmlRenderTheme, MultiMapDataStore.DataPolicy dataPolicy, HillsRenderConfig hillsRenderConfig2) {
        this(str, i, i2, i3, fileArr, xmlRenderTheme, dataPolicy, hillsRenderConfig2, (String) null);
    }

    public BoundingBox getBounds() {
        return this.mapDatabase.boundingBox();
    }

    public org.osmdroid.util.BoundingBox getBoundsOsmdroid() {
        BoundingBox boundingBox = this.mapDatabase.boundingBox();
        return new org.osmdroid.util.BoundingBox(Math.min(MapView.getTileSystem().getMaxLatitude(), boundingBox.maxLatitude), boundingBox.maxLongitude, Math.max(MapView.getTileSystem().getMinLatitude(), boundingBox.minLatitude), boundingBox.minLongitude);
    }

    public static MapsForgeTileSource createFromFiles(File[] fileArr) {
        return new MapsForgeTileSource(InternalRenderTheme.OSMARENDER.name(), MIN_ZOOM, MAX_ZOOM, 256, fileArr, InternalRenderTheme.OSMARENDER, MultiMapDataStore.DataPolicy.RETURN_ALL, (HillsRenderConfig) null, (String) null);
    }

    public static MapsForgeTileSource createFromFiles(File[] fileArr, XmlRenderTheme xmlRenderTheme, String str) {
        return new MapsForgeTileSource(str, MIN_ZOOM, MAX_ZOOM, 256, fileArr, xmlRenderTheme, MultiMapDataStore.DataPolicy.RETURN_ALL, (HillsRenderConfig) null, (String) null);
    }

    public static MapsForgeTileSource createFromFiles(File[] fileArr, XmlRenderTheme xmlRenderTheme, String str, String str2) {
        return new MapsForgeTileSource(str, MIN_ZOOM, MAX_ZOOM, 256, fileArr, xmlRenderTheme, MultiMapDataStore.DataPolicy.RETURN_ALL, (HillsRenderConfig) null, str2);
    }

    public static MapsForgeTileSource createFromFiles(File[] fileArr, XmlRenderTheme xmlRenderTheme, String str, MultiMapDataStore.DataPolicy dataPolicy, HillsRenderConfig hillsRenderConfig2) {
        return new MapsForgeTileSource(str, MIN_ZOOM, MAX_ZOOM, 256, fileArr, xmlRenderTheme, dataPolicy, hillsRenderConfig2, (String) null);
    }

    public static MapsForgeTileSource createFromFiles(File[] fileArr, XmlRenderTheme xmlRenderTheme, String str, MultiMapDataStore.DataPolicy dataPolicy, HillsRenderConfig hillsRenderConfig2, String str2) {
        return new MapsForgeTileSource(str, MIN_ZOOM, MAX_ZOOM, 256, fileArr, xmlRenderTheme, dataPolicy, hillsRenderConfig2, str2);
    }

    public synchronized Drawable renderTile(long j) {
        Tile tile = new Tile(MapTileIndex.getX(j), MapTileIndex.getY(j), (byte) MapTileIndex.getZoom(j), 256);
        this.model.setFixedTileSize(256);
        if (this.mapDatabase == null) {
            return null;
        }
        try {
            AndroidTileBitmap androidTileBitmap = (AndroidTileBitmap) this.renderer.executeJob(new RendererJob(tile, this.mapDatabase, this.theme, this.model, this.scale, false, false));
            if (androidTileBitmap != null) {
                return new BitmapDrawable(AndroidGraphicFactory.getBitmap(androidTileBitmap));
            }
        } catch (Exception e) {
            Log.d(IMapView.LOGTAG, "###################### Mapsforge tile generation failed", e);
        }
        return null;
    }

    public static void createInstance(Application application) {
        AndroidGraphicFactory.createInstance(application);
    }

    public void dispose() {
        this.theme.decrementRefCount();
        this.theme = null;
        this.renderer = null;
        MultiMapDataStore multiMapDataStore = this.mapDatabase;
        if (multiMapDataStore != null) {
            multiMapDataStore.close();
        }
        this.mapDatabase = null;
    }

    public void addTileRefresher(DirectRenderer.TileRefresher tileRefresher) {
        if (tileRefresher != null) {
            this.renderer.addTileRefresher(tileRefresher);
        }
    }
}
