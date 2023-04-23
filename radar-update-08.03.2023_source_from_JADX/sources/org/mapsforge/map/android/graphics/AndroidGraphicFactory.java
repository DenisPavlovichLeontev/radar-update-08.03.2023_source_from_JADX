package org.mapsforge.map.android.graphics;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.core.graphics.ResourceBitmap;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.mapelements.PointTextContainer;
import org.mapsforge.core.mapelements.SymbolContainer;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.model.DisplayModel;

public final class AndroidGraphicFactory implements GraphicFactory {
    public static final boolean DEBUG_BITMAPS = false;
    public static AndroidGraphicFactory INSTANCE = new AndroidGraphicFactory((Application) null);
    public static final boolean KEEP_RESOURCE_BITMAPS = true;
    public static final Bitmap.Config MONO_ALPHA_BITMAP = Bitmap.Config.ALPHA_8;
    public static final Bitmap.Config NON_TRANSPARENT_BITMAP = Bitmap.Config.RGB_565;
    public static final Bitmap.Config TRANSPARENT_BITMAP = Bitmap.Config.ARGB_8888;
    private final Application application;
    private File svgCacheDir;

    public static Bitmap convertToAndroidBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, TRANSPARENT_BITMAP);
        Canvas canvas = new Canvas(createBitmap);
        Rect bounds = drawable.getBounds();
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        drawable.setBounds(bounds);
        return createBitmap;
    }

    public static org.mapsforge.core.graphics.Bitmap convertToBitmap(Drawable drawable) {
        return new AndroidBitmap(convertToAndroidBitmap(drawable));
    }

    public static org.mapsforge.core.graphics.Bitmap convertToBitmap(Drawable drawable, Paint paint) {
        Bitmap copy = convertToAndroidBitmap(drawable).copy(TRANSPARENT_BITMAP, true);
        new Canvas(copy).drawBitmap(copy, 0.0f, 0.0f, paint);
        return new AndroidBitmap(copy);
    }

    public static org.mapsforge.core.graphics.Canvas createGraphicContext(Canvas canvas) {
        return new AndroidCanvas(canvas);
    }

    public static void createInstance(Application application2) {
        INSTANCE = new AndroidGraphicFactory(application2);
    }

    public static int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        }
        if (config == Bitmap.Config.RGB_565 || config == Bitmap.Config.ARGB_4444) {
            return 2;
        }
        Bitmap.Config config2 = Bitmap.Config.ALPHA_8;
        return 1;
    }

    public static Canvas getCanvas(org.mapsforge.core.graphics.Canvas canvas) {
        return ((AndroidCanvas) canvas).canvas;
    }

    public static Paint getPaint(org.mapsforge.core.graphics.Paint paint) {
        return ((AndroidPaint) paint).paint;
    }

    public static Bitmap getBitmap(org.mapsforge.core.graphics.Bitmap bitmap) {
        return ((AndroidBitmap) bitmap).bitmap;
    }

    /* renamed from: org.mapsforge.map.android.graphics.AndroidGraphicFactory$1 */
    static /* synthetic */ class C13101 {
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$core$graphics$Color;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                org.mapsforge.core.graphics.Color[] r0 = org.mapsforge.core.graphics.Color.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$mapsforge$core$graphics$Color = r0
                org.mapsforge.core.graphics.Color r1 = org.mapsforge.core.graphics.Color.BLACK     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Color     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.core.graphics.Color r1 = org.mapsforge.core.graphics.Color.BLUE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Color     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.mapsforge.core.graphics.Color r1 = org.mapsforge.core.graphics.Color.GREEN     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Color     // Catch:{ NoSuchFieldError -> 0x0033 }
                org.mapsforge.core.graphics.Color r1 = org.mapsforge.core.graphics.Color.RED     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Color     // Catch:{ NoSuchFieldError -> 0x003e }
                org.mapsforge.core.graphics.Color r1 = org.mapsforge.core.graphics.Color.TRANSPARENT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Color     // Catch:{ NoSuchFieldError -> 0x0049 }
                org.mapsforge.core.graphics.Color r1 = org.mapsforge.core.graphics.Color.WHITE     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.android.graphics.AndroidGraphicFactory.C13101.<clinit>():void");
        }
    }

    static int getColor(Color color) {
        switch (C13101.$SwitchMap$org$mapsforge$core$graphics$Color[color.ordinal()]) {
            case 1:
                return ViewCompat.MEASURED_STATE_MASK;
            case 2:
                return -16776961;
            case 3:
                return -16711936;
            case 4:
                return SupportMenu.CATEGORY_MASK;
            case 5:
                return 0;
            case 6:
                return -1;
            default:
                throw new IllegalArgumentException("unknown color: " + color);
        }
    }

    static Matrix getMatrix(org.mapsforge.core.graphics.Matrix matrix) {
        return ((AndroidMatrix) matrix).matrix;
    }

    static Path getPath(org.mapsforge.core.graphics.Path path) {
        return ((AndroidPath) path).path;
    }

    private AndroidGraphicFactory(Application application2) {
        this.application = application2;
        if (application2 != null) {
            DisplayModel.setDeviceScaleFactor(application2.getResources().getDisplayMetrics().scaledDensity);
        }
    }

    public static void clearResourceFileCache() {
        AndroidSvgBitmapStore.clear();
    }

    public static void clearResourceMemoryCache() {
        AndroidResourceBitmap.clearResourceBitmaps();
    }

    public org.mapsforge.core.graphics.Bitmap createBitmap(int i, int i2) {
        return new AndroidBitmap(i, i2, TRANSPARENT_BITMAP);
    }

    public org.mapsforge.core.graphics.Bitmap createBitmap(int i, int i2, boolean z) {
        if (z) {
            return new AndroidBitmap(i, i2, TRANSPARENT_BITMAP);
        }
        return new AndroidBitmap(i, i2, NON_TRANSPARENT_BITMAP);
    }

    public org.mapsforge.core.graphics.Canvas createCanvas() {
        return new AndroidCanvas();
    }

    public int createColor(Color color) {
        return getColor(color);
    }

    public int createColor(int i, int i2, int i3, int i4) {
        return android.graphics.Color.argb(i, i2, i3, i4);
    }

    public org.mapsforge.core.graphics.Matrix createMatrix() {
        return new AndroidMatrix();
    }

    public AndroidHillshadingBitmap createMonoBitmap(int i, int i2, byte[] bArr, int i3, BoundingBox boundingBox) {
        int i4 = i3 * 2;
        AndroidHillshadingBitmap androidHillshadingBitmap = new AndroidHillshadingBitmap(i + i4, i2 + i4, i3, boundingBox);
        if (bArr != null) {
            androidHillshadingBitmap.bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bArr));
        }
        return androidHillshadingBitmap;
    }

    public org.mapsforge.core.graphics.Paint createPaint() {
        return new AndroidPaint();
    }

    public org.mapsforge.core.graphics.Paint createPaint(org.mapsforge.core.graphics.Paint paint) {
        return new AndroidPaint(paint);
    }

    public org.mapsforge.core.graphics.Path createPath() {
        return new AndroidPath();
    }

    public PointTextContainer createPointTextContainer(Point point, Display display, int i, String str, org.mapsforge.core.graphics.Paint paint, org.mapsforge.core.graphics.Paint paint2, SymbolContainer symbolContainer, Position position, int i2) {
        return new AndroidPointTextContainer(point, display, i, str, paint, paint2, symbolContainer, position, i2);
    }

    public ResourceBitmap createResourceBitmap(InputStream inputStream, float f, int i, int i2, int i3, int i4) throws IOException {
        return new AndroidResourceBitmap(inputStream, f, i, i2, i3, i4);
    }

    public TileBitmap createTileBitmap(InputStream inputStream, int i, boolean z) {
        return new AndroidTileBitmap(inputStream, i, z);
    }

    public TileBitmap createTileBitmap(int i, boolean z) {
        return new AndroidTileBitmap(i, z);
    }

    public boolean deleteFile(String str) {
        if (this.svgCacheDir != null) {
            return new File(this.svgCacheDir, str).delete();
        }
        return this.application.deleteFile(str);
    }

    public String[] fileList() {
        File file = this.svgCacheDir;
        if (file != null) {
            return file.list();
        }
        return this.application.fileList();
    }

    public FileInputStream openFileInput(String str) throws FileNotFoundException {
        if (this.svgCacheDir != null) {
            return new FileInputStream(new File(this.svgCacheDir, str));
        }
        return this.application.openFileInput(str);
    }

    public FileOutputStream openFileOutput(String str, int i) throws FileNotFoundException {
        if (this.svgCacheDir == null) {
            return this.application.openFileOutput(str, i);
        }
        return new FileOutputStream(new File(this.svgCacheDir, str), i == 32768);
    }

    public InputStream platformSpecificSources(String str, String str2) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        sb.append(str);
        sb.append(str2);
        String sb2 = sb.toString();
        try {
            return this.application.getAssets().open(sb2);
        } catch (IOException unused) {
            throw new FileNotFoundException("invalid resource: " + sb2);
        }
    }

    public ResourceBitmap renderSvg(InputStream inputStream, float f, int i, int i2, int i3, int i4) throws IOException {
        return new AndroidSvgBitmap(inputStream, i4, f, i, i2, i3);
    }

    public void setSvgCacheDir(File file) {
        this.svgCacheDir = file;
    }
}
