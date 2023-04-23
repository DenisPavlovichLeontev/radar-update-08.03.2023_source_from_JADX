package org.osmdroid.tileprovider.cachemanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.C1340R;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.modules.CantContinueException;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.modules.TileDownloader;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourcePolicyException;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.IterableWithSize;
import org.osmdroid.util.MapTileArea;
import org.osmdroid.util.MapTileAreaList;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.MyMath;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;

public class CacheManager {
    protected final int mMaxZoomLevel;
    protected final int mMinZoomLevel;
    protected Set<CacheManagerTask> mPendingTasks;
    private TileDownloader mTileDownloader;
    protected final ITileSource mTileSource;
    protected final IFilesystemCache mTileWriter;
    protected boolean verifyCancel;

    public interface CacheManagerAction {
        int getProgressModulo();

        boolean preCheck();

        boolean tileAction(long j);
    }

    public interface CacheManagerCallback {
        void downloadStarted();

        void onTaskComplete();

        void onTaskFailed(int i);

        void setPossibleTilesInArea(int i);

        void updateProgress(int i, int i2, int i3, int i4);
    }

    public CacheManager(MapView mapView) throws TileSourcePolicyException {
        this(mapView, mapView.getTileProvider().getTileWriter());
    }

    public CacheManager(MapView mapView, IFilesystemCache iFilesystemCache) throws TileSourcePolicyException {
        this(mapView.getTileProvider(), iFilesystemCache, (int) mapView.getMinZoomLevel(), (int) mapView.getMaxZoomLevel());
    }

    public CacheManager(MapTileProviderBase mapTileProviderBase, IFilesystemCache iFilesystemCache, int i, int i2) throws TileSourcePolicyException {
        this(mapTileProviderBase.getTileSource(), iFilesystemCache, i, i2);
    }

    public CacheManager(ITileSource iTileSource, IFilesystemCache iFilesystemCache, int i, int i2) throws TileSourcePolicyException {
        this.mTileDownloader = new TileDownloader();
        this.mPendingTasks = new HashSet();
        this.verifyCancel = true;
        this.mTileSource = iTileSource;
        this.mTileWriter = iFilesystemCache;
        this.mMinZoomLevel = i;
        this.mMaxZoomLevel = i2;
    }

    public int getPendingJobs() {
        return this.mPendingTasks.size();
    }

    @Deprecated
    public static Point getMapTileFromCoordinates(double d, double d2, int i) {
        return new Point(MapView.getTileSystem().getTileXFromLongitude(d2, i), MapView.getTileSystem().getTileYFromLatitude(d, i));
    }

    @Deprecated
    public static GeoPoint getCoordinatesFromMapTile(int i, int i2, int i3) {
        return new GeoPoint(MapView.getTileSystem().getLatitudeFromTileY(i2, i3), MapView.getTileSystem().getLongitudeFromTileX(i, i3));
    }

    public static File getFileName(ITileSource iTileSource, long j) {
        File osmdroidTileCache = Configuration.getInstance().getOsmdroidTileCache();
        return new File(osmdroidTileCache, iTileSource.getTileRelativeFilenameString(j) + OpenStreetMapTileProviderConstants.TILE_PATH_EXTENSION);
    }

    public boolean loadTile(OnlineTileSourceBase onlineTileSourceBase, long j) {
        if (!getFileName(onlineTileSourceBase, j).exists() && !this.mTileWriter.exists(onlineTileSourceBase, j)) {
            return forceLoadTile(onlineTileSourceBase, j);
        }
        return true;
    }

    public boolean forceLoadTile(OnlineTileSourceBase onlineTileSourceBase, long j) {
        try {
            return this.mTileDownloader.downloadTile(j, this.mTileWriter, onlineTileSourceBase) != null;
        } catch (CantContinueException unused) {
            return false;
        }
    }

    public boolean deleteTile(long j) {
        return this.mTileWriter.exists(this.mTileSource, j) && this.mTileWriter.remove(this.mTileSource, j);
    }

    public boolean checkTile(long j) {
        return this.mTileWriter.exists(this.mTileSource, j);
    }

    public boolean isTileToBeDownloaded(ITileSource iTileSource, long j) {
        Long expirationTimestamp = this.mTileWriter.getExpirationTimestamp(iTileSource, j);
        if (expirationTimestamp != null && System.currentTimeMillis() <= expirationTimestamp.longValue()) {
            return false;
        }
        return true;
    }

    public static List<Long> getTilesCoverage(BoundingBox boundingBox, int i, int i2) {
        ArrayList arrayList = new ArrayList();
        while (i <= i2) {
            arrayList.addAll(getTilesCoverage(boundingBox, i));
            i++;
        }
        return arrayList;
    }

    public static Collection<Long> getTilesCoverage(BoundingBox boundingBox, int i) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Long add : getTilesCoverageIterable(boundingBox, i, i)) {
            linkedHashSet.add(add);
        }
        return linkedHashSet;
    }

    static IterableWithSize<Long> getTilesCoverageIterable(BoundingBox boundingBox, int i, int i2) {
        MapTileAreaList mapTileAreaList = new MapTileAreaList();
        while (i <= i2) {
            mapTileAreaList.getList().add(new MapTileArea().set(i, getTilesRect(boundingBox, i)));
            i++;
        }
        return mapTileAreaList;
    }

    public static Rect getTilesRect(BoundingBox boundingBox, int i) {
        int i2 = 1 << i;
        int tileXFromLongitude = MapView.getTileSystem().getTileXFromLongitude(boundingBox.getLonEast(), i);
        int tileYFromLatitude = MapView.getTileSystem().getTileYFromLatitude(boundingBox.getLatSouth(), i);
        int tileXFromLongitude2 = MapView.getTileSystem().getTileXFromLongitude(boundingBox.getLonWest(), i);
        int tileYFromLatitude2 = MapView.getTileSystem().getTileYFromLatitude(boundingBox.getLatNorth(), i);
        int i3 = (tileXFromLongitude - tileXFromLongitude2) + 1;
        if (i3 <= 0) {
            i3 += i2;
        }
        int i4 = (tileYFromLatitude - tileYFromLatitude2) + 1;
        if (i4 <= 0) {
            i4 += i2;
        }
        return new Rect(tileXFromLongitude2, tileYFromLatitude2, (i3 + tileXFromLongitude2) - 1, (i4 + tileYFromLatitude2) - 1);
    }

    public static List<Long> getTilesCoverage(ArrayList<GeoPoint> arrayList, int i, int i2) {
        ArrayList arrayList2 = new ArrayList();
        while (i <= i2) {
            arrayList2.addAll(getTilesCoverage(arrayList, i));
            i++;
        }
        return arrayList2;
    }

    public static Collection<Long> getTilesCoverage(ArrayList<GeoPoint> arrayList, int i) {
        boolean z;
        double d;
        double d2;
        int i2;
        int i3 = i;
        HashSet hashSet = new HashSet();
        boolean z2 = true;
        int i4 = 1 << i3;
        Iterator<GeoPoint> it = arrayList.iterator();
        GeoPoint geoPoint = null;
        Point point = null;
        while (it.hasNext()) {
            GeoPoint next = it.next();
            double GroundResolution = TileSystem.GroundResolution(next.getLatitude(), i3);
            if (hashSet.size() == 0) {
                Point point2 = new Point(MapView.getTileSystem().getTileXFromLongitude(next.getLongitude(), i3), MapView.getTileSystem().getTileYFromLatitude(next.getLatitude(), i3));
                int i5 = point2.x >= 0 ? 0 : -point2.x;
                int i6 = point2.y >= 0 ? 0 : -point2.y;
                int i7 = point2.x + i5;
                while (true) {
                    z = true;
                    if (i7 > point2.x + 1 + i5) {
                        break;
                    }
                    for (int i8 = point2.y + i6; i8 <= point2.y + 1 + i6; i8++) {
                        hashSet.add(Long.valueOf(MapTileIndex.getTileIndex(i3, MyMath.mod(i7, i4), MyMath.mod(i8, i4))));
                    }
                    i7++;
                }
                point = point2;
            } else if (geoPoint != null) {
                double latitude = (next.getLatitude() - geoPoint.getLatitude()) / (next.getLongitude() - geoPoint.getLongitude());
                if (next.getLongitude() > geoPoint.getLongitude()) {
                    d = 1.5707963267948966d;
                    d2 = Math.atan(latitude);
                } else {
                    d = 4.71238898038469d;
                    d2 = Math.atan(latitude);
                }
                double d3 = d - d2;
                int i9 = i4;
                GeoPoint geoPoint2 = new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude());
                while (true) {
                    if (((next.getLatitude() <= geoPoint.getLatitude() || geoPoint2.getLatitude() >= next.getLatitude()) && (next.getLatitude() >= geoPoint.getLatitude() || geoPoint2.getLatitude() <= next.getLatitude())) || ((next.getLongitude() <= geoPoint.getLongitude() || geoPoint2.getLongitude() >= next.getLongitude()) && (next.getLongitude() >= geoPoint.getLongitude() || geoPoint2.getLongitude() <= next.getLongitude()))) {
                        i4 = i9;
                        z = true;
                    } else {
                        double latitude2 = (geoPoint2.getLatitude() * 3.141592653589793d) / 180.0d;
                        double d4 = GroundResolution / 6378137.0d;
                        double asin = Math.asin((Math.sin(latitude2) * Math.cos(d4)) + (Math.cos(latitude2) * Math.sin(d4) * Math.cos(d3)));
                        geoPoint2.setLatitude((asin * 180.0d) / 3.141592653589793d);
                        geoPoint2.setLongitude(((((geoPoint2.getLongitude() * 3.141592653589793d) / 180.0d) + Math.atan2((Math.sin(d3) * Math.sin(d4)) * Math.cos(latitude2), Math.cos(d4) - (Math.sin(latitude2) * Math.sin(asin)))) * 180.0d) / 3.141592653589793d);
                        Point point3 = new Point(MapView.getTileSystem().getTileXFromLongitude(geoPoint2.getLongitude(), i3), MapView.getTileSystem().getTileYFromLatitude(geoPoint2.getLatitude(), i3));
                        if (!point3.equals(point)) {
                            int i10 = point3.x >= 0 ? 0 : -point3.x;
                            int i11 = point3.y >= 0 ? 0 : -point3.y;
                            int i12 = point3.x + i10;
                            while (true) {
                                int i13 = 1;
                                if (i12 > point3.x + 1 + i10) {
                                    break;
                                }
                                int i14 = point3.y + i11;
                                int i15 = i10;
                                while (i14 <= point3.y + i13 + i11) {
                                    int i16 = i9;
                                    hashSet.add(Long.valueOf(MapTileIndex.getTileIndex(i3, MyMath.mod(i12, i16), MyMath.mod(i14, i16))));
                                    i14++;
                                    point3 = point3;
                                    i13 = 1;
                                    i9 = i16;
                                }
                                int i17 = i9;
                                Point point4 = point3;
                                i12++;
                                i9 = i17;
                                i10 = i15;
                            }
                            i2 = i9;
                            point = point3;
                        } else {
                            i2 = i9;
                        }
                        i9 = i2;
                    }
                }
                i4 = i9;
                z = true;
            } else {
                z = z2;
            }
            geoPoint = next;
            z2 = z;
        }
        return hashSet;
    }

    public int possibleTilesInArea(BoundingBox boundingBox, int i, int i2) {
        return getTilesCoverageIterable(boundingBox, i, i2).size();
    }

    public int possibleTilesCovered(ArrayList<GeoPoint> arrayList, int i, int i2) {
        return getTilesCoverage(arrayList, i, i2).size();
    }

    public CacheManagerTask execute(CacheManagerTask cacheManagerTask) {
        cacheManagerTask.execute(new Object[0]);
        this.mPendingTasks.add(cacheManagerTask);
        return cacheManagerTask;
    }

    public CacheManagerTask downloadAreaAsync(Context context, BoundingBox boundingBox, int i, int i2) {
        CacheManagerTask cacheManagerTask = new CacheManagerTask(this, getDownloadingAction(context), boundingBox, i, i2);
        cacheManagerTask.addCallback(getDownloadingDialog(context, cacheManagerTask));
        return execute(cacheManagerTask);
    }

    public CacheManagerTask downloadAreaAsync(Context context, ArrayList<GeoPoint> arrayList, int i, int i2) {
        CacheManagerTask cacheManagerTask = new CacheManagerTask(this, getDownloadingAction(context), arrayList, i, i2);
        cacheManagerTask.addCallback(getDownloadingDialog(context, cacheManagerTask));
        return execute(cacheManagerTask);
    }

    public CacheManagerTask downloadAreaAsync(Context context, BoundingBox boundingBox, int i, int i2, CacheManagerCallback cacheManagerCallback) {
        CacheManagerTask cacheManagerTask = new CacheManagerTask(this, getDownloadingAction(context), boundingBox, i, i2);
        cacheManagerTask.addCallback(cacheManagerCallback);
        cacheManagerTask.addCallback(getDownloadingDialog(context, cacheManagerTask));
        return execute(cacheManagerTask);
    }

    public CacheManagerTask downloadAreaAsync(Context context, ArrayList<GeoPoint> arrayList, int i, int i2, CacheManagerCallback cacheManagerCallback) {
        CacheManagerTask cacheManagerTask = new CacheManagerTask(this, getDownloadingAction(context), arrayList, i, i2);
        cacheManagerTask.addCallback(cacheManagerCallback);
        cacheManagerTask.addCallback(getDownloadingDialog(context, cacheManagerTask));
        return execute(cacheManagerTask);
    }

    public CacheManagerTask downloadAreaAsyncNoUI(Context context, ArrayList<GeoPoint> arrayList, int i, int i2, CacheManagerCallback cacheManagerCallback) {
        CacheManagerTask cacheManagerTask = new CacheManagerTask(this, getDownloadingAction(context), arrayList, i, i2);
        cacheManagerTask.addCallback(cacheManagerCallback);
        return execute(cacheManagerTask);
    }

    public CacheManagerTask downloadAreaAsyncNoUI(Context context, BoundingBox boundingBox, int i, int i2, CacheManagerCallback cacheManagerCallback) {
        CacheManagerTask cacheManagerTask = new CacheManagerTask(this, getDownloadingAction(context), boundingBox, i, i2);
        cacheManagerTask.addCallback(cacheManagerCallback);
        execute(cacheManagerTask);
        return cacheManagerTask;
    }

    public void cancelAllJobs() {
        for (CacheManagerTask cancel : this.mPendingTasks) {
            cancel.cancel(true);
        }
        this.mPendingTasks.clear();
    }

    public CacheManagerTask downloadAreaAsync(Context context, List<Long> list, int i, int i2) {
        CacheManagerTask cacheManagerTask = new CacheManagerTask(this, getDownloadingAction(context), list, i, i2);
        cacheManagerTask.addCallback(getDownloadingDialog(context, cacheManagerTask));
        return execute(cacheManagerTask);
    }

    public void setVerifyCancel(boolean z) {
        this.verifyCancel = z;
    }

    public boolean getVerifyCancel() {
        return this.verifyCancel;
    }

    public static abstract class CacheManagerDialog implements CacheManagerCallback {
        private String handleMessage;
        /* access modifiers changed from: private */
        public final ProgressDialog mProgressDialog;
        /* access modifiers changed from: private */
        public final CacheManagerTask mTask;

        /* access modifiers changed from: protected */
        public abstract String getUITitle();

        public CacheManagerDialog(final Context context, CacheManagerTask cacheManagerTask) {
            this.mTask = cacheManagerTask;
            this.handleMessage = context.getString(C1340R.string.cacheManagerHandlingMessage);
            ProgressDialog progressDialog = new ProgressDialog(context);
            this.mProgressDialog = progressDialog;
            progressDialog.setProgressStyle(1);
            progressDialog.setCancelable(true);
            if (cacheManagerTask.mManager.getVerifyCancel()) {
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(context.getString(C1340R.string.cacheManagerCancelTitle));
                        builder.setMessage(context.getString(C1340R.string.cacheManagerCancelBody));
                        builder.setPositiveButton(context.getString(C1340R.string.cacheManagerYes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CacheManagerDialog.this.mTask.cancel(true);
                            }
                        });
                        builder.setNegativeButton(context.getString(C1340R.string.cacheManagerNo), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                CacheManagerDialog.this.mProgressDialog.show();
                            }
                        });
                        builder.show();
                    }
                });
            } else {
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        CacheManagerDialog.this.mTask.cancel(true);
                    }
                });
            }
        }

        /* access modifiers changed from: protected */
        public String zoomMessage(int i, int i2, int i3) {
            return String.format(this.handleMessage, new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)});
        }

        public void updateProgress(int i, int i2, int i3, int i4) {
            this.mProgressDialog.setProgress(i);
            this.mProgressDialog.setMessage(zoomMessage(i2, i3, i4));
        }

        public void downloadStarted() {
            this.mProgressDialog.setTitle(getUITitle());
            this.mProgressDialog.show();
        }

        public void setPossibleTilesInArea(int i) {
            this.mProgressDialog.setMax(i);
        }

        public void onTaskComplete() {
            dismiss();
        }

        public void onTaskFailed(int i) {
            dismiss();
        }

        private void dismiss() {
            if (this.mProgressDialog.isShowing()) {
                this.mProgressDialog.dismiss();
            }
        }
    }

    public static class CacheManagerTask extends AsyncTask<Object, Integer, Integer> {
        private final CacheManagerAction mAction;
        private final ArrayList<CacheManagerCallback> mCallbacks;
        /* access modifiers changed from: private */
        public final CacheManager mManager;
        private final IterableWithSize<Long> mTiles;
        private final int mZoomMax;
        private final int mZoomMin;

        private CacheManagerTask(CacheManager cacheManager, CacheManagerAction cacheManagerAction, IterableWithSize<Long> iterableWithSize, int i, int i2) {
            this.mCallbacks = new ArrayList<>();
            this.mManager = cacheManager;
            this.mAction = cacheManagerAction;
            this.mTiles = iterableWithSize;
            this.mZoomMin = Math.max(i, cacheManager.mMinZoomLevel);
            this.mZoomMax = Math.min(i2, cacheManager.mMaxZoomLevel);
        }

        public CacheManagerTask(CacheManager cacheManager, CacheManagerAction cacheManagerAction, List<Long> list, int i, int i2) {
            this(cacheManager, cacheManagerAction, (IterableWithSize<Long>) new ListWrapper(list), i, i2);
        }

        public CacheManagerTask(CacheManager cacheManager, CacheManagerAction cacheManagerAction, ArrayList<GeoPoint> arrayList, int i, int i2) {
            this(cacheManager, cacheManagerAction, CacheManager.getTilesCoverage(arrayList, i, i2), i, i2);
        }

        public CacheManagerTask(CacheManager cacheManager, CacheManagerAction cacheManagerAction, BoundingBox boundingBox, int i, int i2) {
            this(cacheManager, cacheManagerAction, CacheManager.getTilesCoverageIterable(boundingBox, i, i2), i, i2);
        }

        public void addCallback(CacheManagerCallback cacheManagerCallback) {
            if (cacheManagerCallback != null) {
                this.mCallbacks.add(cacheManagerCallback);
            }
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            int size = this.mTiles.size();
            Iterator<CacheManagerCallback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                CacheManagerCallback next = it.next();
                try {
                    next.setPossibleTilesInArea(size);
                    next.downloadStarted();
                    int i = this.mZoomMin;
                    next.updateProgress(0, i, i, this.mZoomMax);
                } catch (Throwable th) {
                    logFaultyCallback(th);
                }
            }
        }

        private void logFaultyCallback(Throwable th) {
            Log.w(IMapView.LOGTAG, "Error caught processing cachemanager callback, your implementation is faulty", th);
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(Integer... numArr) {
            Iterator<CacheManagerCallback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                try {
                    it.next().updateProgress(numArr[0].intValue(), numArr[1].intValue(), this.mZoomMin, this.mZoomMax);
                } catch (Throwable th) {
                    logFaultyCallback(th);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            this.mManager.mPendingTasks.remove(this);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Integer num) {
            this.mManager.mPendingTasks.remove(this);
            Iterator<CacheManagerCallback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                CacheManagerCallback next = it.next();
                try {
                    if (num.intValue() == 0) {
                        next.onTaskComplete();
                    } else {
                        next.onTaskFailed(num.intValue());
                    }
                } catch (Throwable th) {
                    logFaultyCallback(th);
                }
            }
        }

        /* access modifiers changed from: protected */
        public Integer doInBackground(Object... objArr) {
            if (!this.mAction.preCheck()) {
                return 0;
            }
            int i = 0;
            int i2 = 0;
            for (Long longValue : this.mTiles) {
                long longValue2 = longValue.longValue();
                int zoom = MapTileIndex.getZoom(longValue2);
                if (zoom >= this.mZoomMin && zoom <= this.mZoomMax && this.mAction.tileAction(longValue2)) {
                    i++;
                }
                i2++;
                if (i2 % this.mAction.getProgressModulo() == 0) {
                    if (isCancelled()) {
                        return Integer.valueOf(i);
                    }
                    publishProgress(new Integer[]{Integer.valueOf(i2), Integer.valueOf(MapTileIndex.getZoom(longValue2))});
                }
            }
            return Integer.valueOf(i);
        }
    }

    public CacheManagerDialog getDownloadingDialog(final Context context, CacheManagerTask cacheManagerTask) {
        return new CacheManagerDialog(cacheManagerTask, context) {
            /* access modifiers changed from: protected */
            public String getUITitle() {
                return context.getString(C1340R.string.cacheManagerDownloadingTitle);
            }

            public void onTaskFailed(int i) {
                super.onTaskFailed(i);
                Context context = context;
                String string = context.getString(C1340R.string.cacheManagerFailed);
                Toast.makeText(context, String.format(string, new Object[]{i + ""}), 0).show();
            }
        };
    }

    public CacheManagerDialog getCleaningDialog(final Context context, CacheManagerTask cacheManagerTask) {
        return new CacheManagerDialog(cacheManagerTask, context) {
            /* access modifiers changed from: protected */
            public String getUITitle() {
                return context.getString(C1340R.string.cacheManagerCleaningTitle);
            }

            public void onTaskFailed(int i) {
                super.onTaskFailed(i);
                Context context = context;
                String string = context.getString(C1340R.string.cacheManagerCleanFailed);
                Toast.makeText(context, String.format(string, new Object[]{i + ""}), 0).show();
            }
        };
    }

    private static class ListWrapper<T> implements IterableWithSize<T> {
        private final List<T> list;

        private ListWrapper(List<T> list2) {
            this.list = list2;
        }

        public int size() {
            return this.list.size();
        }

        public Iterator<T> iterator() {
            return this.list.iterator();
        }
    }

    public CacheManagerAction getDownloadingAction(final Context context) {
        return new CacheManagerAction() {
            public int getProgressModulo() {
                return 10;
            }

            public boolean preCheck() {
                if (!(CacheManager.this.mTileSource instanceof OnlineTileSourceBase)) {
                    Log.e(IMapView.LOGTAG, "TileSource is not an online tile source");
                    return false;
                } else if (((OnlineTileSourceBase) CacheManager.this.mTileSource).getTileSourcePolicy().acceptsBulkDownload()) {
                    return true;
                } else {
                    throw new TileSourcePolicyException(context.getString(C1340R.string.cacheManagerUnsupportedSource));
                }
            }

            public boolean tileAction(long j) {
                CacheManager cacheManager = CacheManager.this;
                return !cacheManager.loadTile((OnlineTileSourceBase) cacheManager.mTileSource, j);
            }
        };
    }

    public CacheManagerAction getCleaningAction() {
        return new CacheManagerAction() {
            public int getProgressModulo() {
                return 1000;
            }

            public boolean preCheck() {
                return true;
            }

            public boolean tileAction(long j) {
                return CacheManager.this.deleteTile(j);
            }
        };
    }

    public CacheManagerTask cleanAreaAsync(Context context, BoundingBox boundingBox, int i, int i2) {
        CacheManagerTask cacheManagerTask = new CacheManagerTask(this, getCleaningAction(), boundingBox, i, i2);
        cacheManagerTask.addCallback(getCleaningDialog(context, cacheManagerTask));
        return execute(cacheManagerTask);
    }

    public CacheManagerTask cleanAreaAsync(Context context, ArrayList<GeoPoint> arrayList, int i, int i2) {
        return cleanAreaAsync(context, extendedBoundsFromGeoPoints(arrayList, i), i, i2);
    }

    public CacheManagerTask cleanAreaAsync(Context context, List<Long> list, int i, int i2) {
        CacheManagerTask cacheManagerTask = new CacheManagerTask(this, getCleaningAction(), list, i, i2);
        cacheManagerTask.addCallback(getCleaningDialog(context, cacheManagerTask));
        return execute(cacheManagerTask);
    }

    public BoundingBox extendedBoundsFromGeoPoints(ArrayList<GeoPoint> arrayList, int i) {
        BoundingBox fromGeoPoints = BoundingBox.fromGeoPoints(arrayList);
        int tileXFromLongitude = MapView.getTileSystem().getTileXFromLongitude(fromGeoPoints.getLonEast(), i);
        int tileYFromLatitude = MapView.getTileSystem().getTileYFromLatitude(fromGeoPoints.getLatSouth(), i);
        int tileXFromLongitude2 = MapView.getTileSystem().getTileXFromLongitude(fromGeoPoints.getLonWest(), i);
        return new BoundingBox(MapView.getTileSystem().getLatitudeFromTileY(MapView.getTileSystem().getTileYFromLatitude(fromGeoPoints.getLatNorth(), i) - 1, i), MapView.getTileSystem().getLongitudeFromTileX(tileXFromLongitude + 1, i), MapView.getTileSystem().getLatitudeFromTileY(tileYFromLatitude + 1, i), MapView.getTileSystem().getLongitudeFromTileX(tileXFromLongitude2 - 1, i));
    }

    public long currentCacheUsage() {
        return directorySize(Configuration.getInstance().getOsmdroidTileCache());
    }

    public long cacheCapacity() {
        return Configuration.getInstance().getTileFileSystemCacheMaxBytes();
    }

    public long directorySize(File file) {
        long directorySize;
        File[] listFiles = file.listFiles();
        long j = 0;
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.isFile()) {
                    directorySize = file2.length();
                } else if (file2.isDirectory()) {
                    directorySize = directorySize(file2);
                }
                j += directorySize;
            }
        }
        return j;
    }

    public void setTileDownloader(TileDownloader tileDownloader) {
        this.mTileDownloader = tileDownloader;
    }
}
