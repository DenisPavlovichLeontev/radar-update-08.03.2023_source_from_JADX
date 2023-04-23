package org.osmdroid.tileprovider.tilesource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import java.io.File;
import java.io.InputStream;
import java.util.Random;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.ReusableBitmapDrawable;
import org.osmdroid.tileprovider.util.Counters;
import org.osmdroid.util.MapTileIndex;

public abstract class BitmapTileSourceBase implements ITileSource {
    private static int globalOrdinal;
    protected String mCopyright;
    protected final String mImageFilenameEnding;
    private final int mMaximumZoomLevel;
    private final int mMinimumZoomLevel;
    protected String mName;
    private final int mOrdinal;
    private final int mTileSizePixels;
    protected final Random random;

    public BitmapTileSourceBase(String str, int i, int i2, int i3, String str2) {
        this(str, i, i2, i3, str2, (String) null);
    }

    public BitmapTileSourceBase(String str, int i, int i2, int i3, String str2, String str3) {
        this.random = new Random();
        int i4 = globalOrdinal;
        globalOrdinal = i4 + 1;
        this.mOrdinal = i4;
        this.mName = str;
        this.mMinimumZoomLevel = i;
        this.mMaximumZoomLevel = i2;
        this.mTileSizePixels = i3;
        this.mImageFilenameEnding = str2;
        this.mCopyright = str3;
    }

    public int ordinal() {
        return this.mOrdinal;
    }

    public String name() {
        return this.mName;
    }

    public String pathBase() {
        return this.mName;
    }

    public String imageFilenameEnding() {
        return this.mImageFilenameEnding;
    }

    public int getMinimumZoomLevel() {
        return this.mMinimumZoomLevel;
    }

    public int getMaximumZoomLevel() {
        return this.mMaximumZoomLevel;
    }

    public int getTileSizePixels() {
        return this.mTileSizePixels;
    }

    public String toString() {
        return name();
    }

    public Drawable getDrawable(String str) throws LowMemoryException {
        Bitmap bitmap;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            int i = options.outHeight;
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            BitmapPool.getInstance().applyReusableOptions(options2, i, i);
            if (Build.VERSION.SDK_INT == 15) {
                bitmap = BitmapFactory.decodeFile(str);
            } else {
                bitmap = BitmapFactory.decodeFile(str, options2);
            }
            if (bitmap != null) {
                return new ReusableBitmapDrawable(bitmap);
            }
            if (new File(str).exists()) {
                Log.d(IMapView.LOGTAG, str + " is an invalid image file, deleting...");
                try {
                    new File(str).delete();
                    return null;
                } catch (Throwable th) {
                    Log.e(IMapView.LOGTAG, "Error deleting invalid file: " + str, th);
                    return null;
                }
            } else {
                Log.d(IMapView.LOGTAG, "Request tile: " + str + " does not exist");
                return null;
            }
        } catch (OutOfMemoryError e) {
            Log.e(IMapView.LOGTAG, "OutOfMemoryError loading bitmap: " + str);
            System.gc();
            throw new LowMemoryException((Throwable) e);
        } catch (Exception e2) {
            Log.e(IMapView.LOGTAG, "Unexpected error loading bitmap: " + str, e2);
            Counters.tileDownloadErrors = Counters.tileDownloadErrors + 1;
            System.gc();
            return null;
        }
    }

    public String getTileRelativeFilenameString(long j) {
        return pathBase() + '/' + MapTileIndex.getZoom(j) + '/' + MapTileIndex.getX(j) + '/' + MapTileIndex.getY(j) + imageFilenameEnding();
    }

    public Drawable getDrawable(InputStream inputStream) throws LowMemoryException {
        try {
            int i = this.mTileSizePixels;
            if (inputStream.markSupported()) {
                inputStream.mark(1048576);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, (Rect) null, options);
                i = options.outHeight;
                inputStream.reset();
            }
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            BitmapPool.getInstance().applyReusableOptions(options2, i, i);
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, (Rect) null, options2);
            if (decodeStream != null) {
                return new ReusableBitmapDrawable(decodeStream);
            }
        } catch (OutOfMemoryError e) {
            Log.e(IMapView.LOGTAG, "OutOfMemoryError loading bitmap");
            System.gc();
            throw new LowMemoryException((Throwable) e);
        } catch (Exception e2) {
            Log.w(IMapView.LOGTAG, "#547 Error loading bitmap" + pathBase(), e2);
        }
        return null;
    }

    public static final class LowMemoryException extends Exception {
        private static final long serialVersionUID = 146526524087765134L;

        public LowMemoryException(String str) {
            super(str);
        }

        public LowMemoryException(Throwable th) {
            super(th);
        }
    }

    public String getCopyrightNotice() {
        return this.mCopyright;
    }
}
