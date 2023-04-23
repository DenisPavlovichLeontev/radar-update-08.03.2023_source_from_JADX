package org.mapsforge.map.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Looper;
import android.os.StatFs;
import android.view.Display;
import android.view.WindowManager;
import java.io.File;
import java.util.logging.Logger;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.controller.FrameBufferController;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.FileSystemTileCache;
import org.mapsforge.map.layer.cache.InMemoryTileCache;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.cache.TwoLevelTileCache;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.scalebar.DefaultMapScaleBar;
import org.mapsforge.map.scalebar.DistanceUnitAdapter;
import org.mapsforge.map.scalebar.MapScaleBar;

public final class AndroidUtil {
    private static final Logger LOGGER = Logger.getLogger(AndroidUtil.class.getName());

    public static TileCache createExternalStorageTileCache(Context context, String str, int i, int i2) {
        return createExternalStorageTileCache(context, str, i, i2, false);
    }

    public static TileCache createExternalStorageTileCache(Context context, String str, int i, int i2, boolean z) {
        return createExternalStorageTileCache(context.getExternalCacheDir(), str, i, i2, z);
    }

    public static TileCache createExternalStorageTileCache(File file, String str, int i, int i2, boolean z) {
        Logger logger = LOGGER;
        logger.info("TILECACHE INMEMORY SIZE: " + Integer.toString(i));
        InMemoryTileCache inMemoryTileCache = new InMemoryTileCache(i);
        if (file != null) {
            String str2 = file.getAbsolutePath() + File.separator + str;
            File file2 = new File(str2);
            if (file2.exists() || file2.mkdirs()) {
                int estimateSizeOfFileSystemCache = estimateSizeOfFileSystemCache(str2, i, i2);
                if (file2.canWrite() && estimateSizeOfFileSystemCache > 0) {
                    try {
                        logger.info("TILECACHE FILE SIZE: " + Integer.toString(estimateSizeOfFileSystemCache));
                        return new TwoLevelTileCache(inMemoryTileCache, new FileSystemTileCache(estimateSizeOfFileSystemCache, file2, AndroidGraphicFactory.INSTANCE, z));
                    } catch (IllegalArgumentException e) {
                        LOGGER.warning(e.getMessage());
                    }
                }
            }
        }
        return inMemoryTileCache;
    }

    public static TileCache createTileCache(Context context, File file, String str, int i, float f, double d, boolean z) {
        return createExternalStorageTileCache(file, str, getMinimumCacheSize(context, i, d, f), i, z);
    }

    public static TileCache createTileCache(Context context, String str, int i, float f, double d, boolean z) {
        return createExternalStorageTileCache(context, str, getMinimumCacheSize(context, i, d, f), i, z);
    }

    public static TileCache createTileCache(Context context, String str, int i, float f, double d) {
        return createTileCache(context, str, i, f, d, false);
    }

    public static TileCache createTileCache(File file, String str, int i, int i2, int i3, double d, boolean z) {
        return createExternalStorageTileCache(file, str, getMinimumCacheSize(i, d, i2, i3), i, z);
    }

    public static TileCache createTileCache(Context context, String str, int i, int i2, int i3, double d, boolean z) {
        return createExternalStorageTileCache(context, str, getMinimumCacheSize(i, d, i2, i3), i, z);
    }

    public static TileCache createTileCache(Context context, String str, int i, int i2, int i3, double d) {
        return createTileCache(context, str, i, i2, i3, d, false);
    }

    public static TileRendererLayer createTileRendererLayer(TileCache tileCache, IMapViewPosition iMapViewPosition, MapDataStore mapDataStore, XmlRenderTheme xmlRenderTheme) {
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, iMapViewPosition, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setXmlRenderTheme(xmlRenderTheme);
        return tileRendererLayer;
    }

    public static TileRendererLayer createTileRendererLayer(TileCache tileCache, IMapViewPosition iMapViewPosition, MapDataStore mapDataStore, XmlRenderTheme xmlRenderTheme, boolean z, boolean z2, boolean z3) {
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, iMapViewPosition, z, z2, z3, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setXmlRenderTheme(xmlRenderTheme);
        return tileRendererLayer;
    }

    public static TileRendererLayer createTileRendererLayer(TileCache tileCache, IMapViewPosition iMapViewPosition, MapDataStore mapDataStore, XmlRenderTheme xmlRenderTheme, boolean z, boolean z2, boolean z3, HillsRenderConfig hillsRenderConfig) {
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, iMapViewPosition, z, z2, z3, AndroidGraphicFactory.INSTANCE, hillsRenderConfig);
        XmlRenderTheme xmlRenderTheme2 = xmlRenderTheme;
        tileRendererLayer.setXmlRenderTheme(xmlRenderTheme);
        return tileRendererLayer;
    }

    public static boolean currentThreadIsUiThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static int estimateSizeOfFileSystemCache(String str, int i, int i2) {
        int min = (int) Math.min(2000, getAvailableCacheSlots(str, i2 * 4 * i2));
        if (i > min) {
            return 0;
        }
        return min;
    }

    public static long getAvailableCacheSlots(String str, int i) {
        StatFs statFs = new StatFs(str);
        if (Build.VERSION.SDK_INT >= 18) {
            return statFs.getAvailableBytes() / ((long) i);
        }
        return (long) (statFs.getAvailableBlocks() / Math.max(i / statFs.getBlockSize(), 1));
    }

    public static int getMinimumCacheSize(Context context, int i, double d, float f) {
        int i2;
        int i3;
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            Point point = new Point();
            defaultDisplay.getSize(point);
            i2 = point.y;
            i3 = point.x;
        } else {
            int height = defaultDisplay.getHeight();
            i3 = defaultDisplay.getWidth();
            i2 = height;
        }
        Dimension calculateFrameBufferDimension = FrameBufferController.calculateFrameBufferDimension(new Dimension(i3, i2), d);
        return (int) Math.max(4.0f, f * ((float) ((calculateFrameBufferDimension.height / i) + 2)) * ((float) ((calculateFrameBufferDimension.width / i) + 2)));
    }

    public static int getMinimumCacheSize(int i, double d, int i2, int i3) {
        Dimension calculateFrameBufferDimension = FrameBufferController.calculateFrameBufferDimension(new Dimension(i2, i3), d);
        return Math.max(4, ((calculateFrameBufferDimension.height / i) + 2) * ((calculateFrameBufferDimension.width / i) + 2));
    }

    public static void restartActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
            return;
        }
        Intent intent = activity.getIntent();
        intent.addFlags(65536);
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public static void setMapScaleBar(MapView mapView, DistanceUnitAdapter distanceUnitAdapter, DistanceUnitAdapter distanceUnitAdapter2) {
        if (distanceUnitAdapter == null && distanceUnitAdapter2 == null) {
            mapView.setMapScaleBar((MapScaleBar) null);
            return;
        }
        MapScaleBar mapScaleBar = mapView.getMapScaleBar();
        if (mapScaleBar == null) {
            mapScaleBar = new DefaultMapScaleBar(mapView.getModel().mapViewPosition, mapView.getModel().mapViewDimension, AndroidGraphicFactory.INSTANCE, mapView.getModel().displayModel);
            mapView.setMapScaleBar(mapScaleBar);
        }
        if (mapScaleBar instanceof DefaultMapScaleBar) {
            if (distanceUnitAdapter2 != null) {
                DefaultMapScaleBar defaultMapScaleBar = (DefaultMapScaleBar) mapScaleBar;
                defaultMapScaleBar.setScaleBarMode(DefaultMapScaleBar.ScaleBarMode.BOTH);
                defaultMapScaleBar.setSecondaryDistanceUnitAdapter(distanceUnitAdapter2);
            } else {
                ((DefaultMapScaleBar) mapScaleBar).setScaleBarMode(DefaultMapScaleBar.ScaleBarMode.SINGLE);
            }
        }
        mapScaleBar.setDistanceUnitAdapter(distanceUnitAdapter);
    }

    private AndroidUtil() {
    }
}
