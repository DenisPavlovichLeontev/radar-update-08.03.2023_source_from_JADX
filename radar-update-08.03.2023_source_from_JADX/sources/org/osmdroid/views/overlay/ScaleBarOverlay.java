package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import androidx.core.view.ViewCompat;
import java.util.Locale;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.library.C1340R;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.constants.GeoConstants;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

public class ScaleBarOverlay extends Overlay implements GeoConstants {
    private static final Rect sTextBoundsRect = new Rect();
    private boolean adjustLength;
    protected boolean alignBottom;
    protected boolean alignRight;
    private Paint barPaint;
    protected final Path barPath;
    private Paint bgPaint;
    private boolean centred;
    private Context context;
    private double lastLatitude;
    private double lastZoomLevel;
    boolean latitudeBar;
    protected final Rect latitudeBarRect;
    boolean longitudeBar;
    protected final Rect longitudeBarRect;
    private int mMapHeight;
    private MapView mMapView;
    private int mMapWidth;
    private float maxLength;
    double minZoom;
    public int screenHeight;
    public int screenWidth;
    private Paint textPaint;
    UnitsOfMeasure unitsOfMeasure;
    int xOffset;
    public float xdpi;
    int yOffset;
    public float ydpi;

    public enum UnitsOfMeasure {
        metric,
        imperial,
        nautical
    }

    public ScaleBarOverlay(MapView mapView) {
        this(mapView, mapView.getContext(), 0, 0);
    }

    public ScaleBarOverlay(Context context2, int i, int i2) {
        this((MapView) null, context2, i, i2);
    }

    private ScaleBarOverlay(MapView mapView, Context context2, int i, int i2) {
        this.xOffset = 10;
        this.yOffset = 10;
        this.minZoom = 0.0d;
        this.unitsOfMeasure = UnitsOfMeasure.metric;
        this.latitudeBar = true;
        this.longitudeBar = false;
        this.alignBottom = false;
        this.alignRight = false;
        this.barPath = new Path();
        this.latitudeBarRect = new Rect();
        this.longitudeBarRect = new Rect();
        this.lastZoomLevel = -1.0d;
        this.lastLatitude = 0.0d;
        this.centred = false;
        this.adjustLength = false;
        this.mMapView = mapView;
        this.context = context2;
        this.mMapWidth = i;
        this.mMapHeight = i2;
        DisplayMetrics displayMetrics = context2.getResources().getDisplayMetrics();
        Paint paint = new Paint();
        this.barPaint = paint;
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.barPaint.setAntiAlias(true);
        this.barPaint.setStyle(Paint.Style.STROKE);
        this.barPaint.setAlpha(255);
        this.barPaint.setStrokeWidth(displayMetrics.density * 2.0f);
        String str = null;
        this.bgPaint = null;
        Paint paint2 = new Paint();
        this.textPaint = paint2;
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.textPaint.setAntiAlias(true);
        this.textPaint.setStyle(Paint.Style.FILL);
        this.textPaint.setAlpha(255);
        this.textPaint.setTextSize(displayMetrics.density * 10.0f);
        this.xdpi = displayMetrics.xdpi;
        this.ydpi = displayMetrics.ydpi;
        this.screenWidth = displayMetrics.widthPixels;
        this.screenHeight = displayMetrics.heightPixels;
        try {
            str = (String) Build.class.getField("MANUFACTURER").get((Object) null);
        } catch (Exception unused) {
        }
        if ("motorola".equals(str) && "DROIDX".equals(Build.MODEL)) {
            WindowManager windowManager = (WindowManager) this.context.getSystemService("window");
            if (windowManager == null || windowManager.getDefaultDisplay().getOrientation() <= 0) {
                this.xdpi = (float) (((double) this.screenWidth) / 2.1d);
                this.ydpi = (float) (((double) this.screenHeight) / 3.75d);
            } else {
                this.xdpi = (float) (((double) this.screenWidth) / 3.75d);
                this.ydpi = (float) (((double) this.screenHeight) / 2.1d);
            }
        } else if ("motorola".equals(str) && "Droid".equals(Build.MODEL)) {
            this.xdpi = 264.0f;
            this.ydpi = 264.0f;
        }
        this.maxLength = 2.54f;
    }

    public void setMinZoom(double d) {
        this.minZoom = d;
    }

    public void setScaleBarOffset(int i, int i2) {
        this.xOffset = i;
        this.yOffset = i2;
    }

    public void setLineWidth(float f) {
        this.barPaint.setStrokeWidth(f);
    }

    public void setTextSize(float f) {
        this.textPaint.setTextSize(f);
    }

    public void setUnitsOfMeasure(UnitsOfMeasure unitsOfMeasure2) {
        this.unitsOfMeasure = unitsOfMeasure2;
        this.lastZoomLevel = -1.0d;
    }

    public UnitsOfMeasure getUnitsOfMeasure() {
        return this.unitsOfMeasure;
    }

    public void drawLatitudeScale(boolean z) {
        this.latitudeBar = z;
        this.lastZoomLevel = -1.0d;
    }

    public void drawLongitudeScale(boolean z) {
        this.longitudeBar = z;
        this.lastZoomLevel = -1.0d;
    }

    public void setCentred(boolean z) {
        this.centred = z;
        this.alignBottom = !z;
        this.alignRight = !z;
        this.lastZoomLevel = -1.0d;
    }

    public void setAlignBottom(boolean z) {
        this.centred = false;
        this.alignBottom = z;
        this.lastZoomLevel = -1.0d;
    }

    public void setAlignRight(boolean z) {
        this.centred = false;
        this.alignRight = z;
        this.lastZoomLevel = -1.0d;
    }

    public Paint getBarPaint() {
        return this.barPaint;
    }

    public void setBarPaint(Paint paint) {
        if (paint != null) {
            this.barPaint = paint;
            this.lastZoomLevel = -1.0d;
            return;
        }
        throw new IllegalArgumentException("pBarPaint argument cannot be null");
    }

    public Paint getTextPaint() {
        return this.textPaint;
    }

    public void setTextPaint(Paint paint) {
        if (paint != null) {
            this.textPaint = paint;
            this.lastZoomLevel = -1.0d;
            return;
        }
        throw new IllegalArgumentException("pTextPaint argument cannot be null");
    }

    public void setBackgroundPaint(Paint paint) {
        this.bgPaint = paint;
        this.lastZoomLevel = -1.0d;
    }

    public void setEnableAdjustLength(boolean z) {
        this.adjustLength = z;
        this.lastZoomLevel = -1.0d;
    }

    public void setMaxLength(float f) {
        this.maxLength = f;
        this.lastZoomLevel = -1.0d;
    }

    public void draw(Canvas canvas, Projection projection) {
        Paint paint;
        double zoomLevel = projection.getZoomLevel();
        if (zoomLevel >= this.minZoom) {
            Rect intrinsicScreenRect = projection.getIntrinsicScreenRect();
            int width = intrinsicScreenRect.width();
            int height = intrinsicScreenRect.height();
            int i = 0;
            boolean z = (height == this.screenHeight && width == this.screenWidth) ? false : true;
            this.screenHeight = height;
            this.screenWidth = width;
            IGeoPoint fromPixels = projection.fromPixels(width / 2, height / 2, (GeoPoint) null);
            if (!(zoomLevel == this.lastZoomLevel && fromPixels.getLatitude() == this.lastLatitude && !z)) {
                this.lastZoomLevel = zoomLevel;
                this.lastLatitude = fromPixels.getLatitude();
                rebuildBarPath(projection);
            }
            int i2 = this.xOffset;
            int i3 = this.yOffset;
            if (this.alignBottom) {
                i3 *= -1;
            }
            if (this.alignRight) {
                i2 *= -1;
            }
            if (this.centred && this.latitudeBar) {
                i2 += (-this.latitudeBarRect.width()) / 2;
            }
            if (this.centred && this.longitudeBar) {
                i3 += (-this.longitudeBarRect.height()) / 2;
            }
            projection.save(canvas, false, true);
            canvas.translate((float) i2, (float) i3);
            if (this.latitudeBar && (paint = this.bgPaint) != null) {
                canvas.drawRect(this.latitudeBarRect, paint);
            }
            if (this.longitudeBar && this.bgPaint != null) {
                if (this.latitudeBar) {
                    i = this.latitudeBarRect.height();
                }
                canvas.drawRect((float) this.longitudeBarRect.left, (float) (this.longitudeBarRect.top + i), (float) this.longitudeBarRect.right, (float) this.longitudeBarRect.bottom, this.bgPaint);
            }
            canvas.drawPath(this.barPath, this.barPaint);
            if (this.latitudeBar) {
                drawLatitudeText(canvas, projection);
            }
            if (this.longitudeBar) {
                drawLongitudeText(canvas, projection);
            }
            projection.restore(canvas, true);
        }
    }

    public void disableScaleBar() {
        setEnabled(false);
    }

    public void enableScaleBar() {
        setEnabled(true);
    }

    private void drawLatitudeText(Canvas canvas, Projection projection) {
        int i;
        int i2 = (int) (this.maxLength * ((float) ((int) (((double) this.xdpi) / 2.54d))));
        int i3 = i2 / 2;
        double distanceToAsDouble = ((GeoPoint) projection.fromPixels((this.screenWidth / 2) - i3, this.yOffset, (GeoPoint) null)).distanceToAsDouble(projection.fromPixels((this.screenWidth / 2) + i3, this.yOffset, (GeoPoint) null));
        double adjustScaleBarLength = this.adjustLength ? adjustScaleBarLength(distanceToAsDouble) : distanceToAsDouble;
        int i4 = (int) ((((double) i2) * adjustScaleBarLength) / distanceToAsDouble);
        String scaleBarLengthText = scaleBarLengthText(adjustScaleBarLength);
        Paint paint = this.textPaint;
        int length = scaleBarLengthText.length();
        Rect rect = sTextBoundsRect;
        paint.getTextBounds(scaleBarLengthText, 0, length, rect);
        int height = (int) (((double) rect.height()) / 5.0d);
        float width = (float) ((i4 / 2) - (rect.width() / 2));
        if (this.alignRight) {
            width += (float) (this.screenWidth - i4);
        }
        if (this.alignBottom) {
            i = this.screenHeight - (height * 2);
        } else {
            i = rect.height() + height;
        }
        canvas.drawText(scaleBarLengthText, width, (float) i, this.textPaint);
    }

    private void drawLongitudeText(Canvas canvas, Projection projection) {
        int i;
        int i2 = (int) (this.maxLength * ((float) ((int) (((double) this.ydpi) / 2.54d))));
        int i3 = i2 / 2;
        double distanceToAsDouble = ((GeoPoint) projection.fromPixels(this.screenWidth / 2, (this.screenHeight / 2) - i3, (GeoPoint) null)).distanceToAsDouble(projection.fromPixels(this.screenWidth / 2, (this.screenHeight / 2) + i3, (GeoPoint) null));
        double adjustScaleBarLength = this.adjustLength ? adjustScaleBarLength(distanceToAsDouble) : distanceToAsDouble;
        int i4 = (int) ((((double) i2) * adjustScaleBarLength) / distanceToAsDouble);
        String scaleBarLengthText = scaleBarLengthText(adjustScaleBarLength);
        Paint paint = this.textPaint;
        int length = scaleBarLengthText.length();
        Rect rect = sTextBoundsRect;
        paint.getTextBounds(scaleBarLengthText, 0, length, rect);
        int height = (int) (((double) rect.height()) / 5.0d);
        if (this.alignRight) {
            i = this.screenWidth - (height * 2);
        } else {
            i = rect.height() + height;
        }
        float f = (float) i;
        float width = (float) ((i4 / 2) + (rect.width() / 2));
        if (this.alignBottom) {
            width += (float) (this.screenHeight - i4);
        }
        canvas.save();
        canvas.rotate(-90.0f, f, width);
        canvas.drawText(scaleBarLengthText, f, width, this.textPaint);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void rebuildBarPath(Projection projection) {
        int i;
        float f = this.maxLength;
        int i2 = (int) (((float) ((int) (((double) this.xdpi) / 2.54d))) * f);
        int i3 = (int) (f * ((float) ((int) (((double) this.ydpi) / 2.54d))));
        int i4 = i2 / 2;
        double distanceToAsDouble = ((GeoPoint) projection.fromPixels((this.screenWidth / 2) - i4, this.yOffset, (GeoPoint) null)).distanceToAsDouble(projection.fromPixels((this.screenWidth / 2) + i4, this.yOffset, (GeoPoint) null));
        double adjustScaleBarLength = this.adjustLength ? adjustScaleBarLength(distanceToAsDouble) : distanceToAsDouble;
        int i5 = (int) ((((double) i2) * adjustScaleBarLength) / distanceToAsDouble);
        int i6 = i3 / 2;
        double distanceToAsDouble2 = ((GeoPoint) projection.fromPixels(this.screenWidth / 2, (this.screenHeight / 2) - i6, (GeoPoint) null)).distanceToAsDouble(projection.fromPixels(this.screenWidth / 2, (this.screenHeight / 2) + i6, (GeoPoint) null));
        double adjustScaleBarLength2 = this.adjustLength ? adjustScaleBarLength(distanceToAsDouble2) : distanceToAsDouble2;
        int i7 = (int) ((((double) i3) * adjustScaleBarLength2) / distanceToAsDouble2);
        String scaleBarLengthText = scaleBarLengthText(adjustScaleBarLength);
        Rect rect = new Rect();
        int i8 = 0;
        this.textPaint.getTextBounds(scaleBarLengthText, 0, scaleBarLengthText.length(), rect);
        int height = (int) (((double) rect.height()) / 5.0d);
        String scaleBarLengthText2 = scaleBarLengthText(adjustScaleBarLength2);
        Rect rect2 = new Rect();
        this.textPaint.getTextBounds(scaleBarLengthText2, 0, scaleBarLengthText2.length(), rect2);
        int height2 = (int) (((double) rect2.height()) / 5.0d);
        int height3 = rect.height();
        int height4 = rect2.height();
        this.barPath.rewind();
        if (this.alignBottom) {
            height *= -1;
            height3 *= -1;
            i = getMapHeight();
            i7 = i - i7;
        } else {
            i = 0;
        }
        if (this.alignRight) {
            height2 *= -1;
            height4 *= -1;
            i8 = getMapWidth();
            i5 = i8 - i5;
        }
        if (this.latitudeBar) {
            float f2 = (float) i5;
            int i9 = height3 + i + (height * 2);
            float f3 = (float) i9;
            this.barPath.moveTo(f2, f3);
            float f4 = (float) i;
            this.barPath.lineTo(f2, f4);
            float f5 = (float) i8;
            this.barPath.lineTo(f5, f4);
            if (!this.longitudeBar) {
                this.barPath.lineTo(f5, f3);
            }
            this.latitudeBarRect.set(i8, i, i5, i9);
        }
        if (this.longitudeBar) {
            if (!this.latitudeBar) {
                float f6 = (float) i;
                this.barPath.moveTo((float) (i8 + height4 + (height2 * 2)), f6);
                this.barPath.lineTo((float) i8, f6);
            }
            float f7 = (float) i7;
            this.barPath.lineTo((float) i8, f7);
            int i10 = height4 + i8 + (height2 * 2);
            this.barPath.lineTo((float) i10, f7);
            this.longitudeBarRect.set(i8, i, i10, i7);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0047 A[LOOP:0: B:13:0x003f->B:15:0x0047, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0056 A[LOOP:1: B:16:0x004a->B:20:0x0056, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0059 A[EDGE_INSN: B:39:0x0059->B:21:0x0059 ?: BREAK  
    EDGE_INSN: B:40:0x0059->B:21:0x0059 ?: BREAK  ] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0062  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0070  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private double adjustScaleBarLength(double r22) {
        /*
            r21 = this;
            r0 = r21
            org.osmdroid.views.overlay.ScaleBarOverlay$UnitsOfMeasure r1 = r0.unitsOfMeasure
            org.osmdroid.views.overlay.ScaleBarOverlay$UnitsOfMeasure r2 = org.osmdroid.views.overlay.ScaleBarOverlay.UnitsOfMeasure.imperial
            r3 = 4655859997584916480(0x409cf00000000000, double:1852.0)
            r5 = 4654792785210718028(0x409925604189374c, double:1609.344)
            r7 = 1
            r8 = 4614570213475568536(0x400a3f28fd4f4b98, double:3.2808399)
            r10 = 0
            r12 = 0
            if (r1 != r2) goto L_0x002a
            r1 = 4644369992003103805(0x40741de69ad42c3d, double:321.8688)
            int r1 = (r22 > r1 ? 1 : (r22 == r1 ? 0 : -1))
            if (r1 < 0) goto L_0x0027
            double r1 = r22 / r5
            goto L_0x003e
        L_0x0027:
            double r1 = r22 * r8
            goto L_0x003f
        L_0x002a:
            org.osmdroid.views.overlay.ScaleBarOverlay$UnitsOfMeasure r1 = r0.unitsOfMeasure
            org.osmdroid.views.overlay.ScaleBarOverlay$UnitsOfMeasure r2 = org.osmdroid.views.overlay.ScaleBarOverlay.UnitsOfMeasure.nautical
            if (r1 != r2) goto L_0x003c
            r1 = 4645223761902462566(0x4077266666666666, double:370.4)
            int r1 = (r22 > r1 ? 1 : (r22 == r1 ? 0 : -1))
            if (r1 < 0) goto L_0x0027
            double r1 = r22 / r3
            goto L_0x003e
        L_0x003c:
            r1 = r22
        L_0x003e:
            r7 = r12
        L_0x003f:
            r12 = 4621819117588971520(0x4024000000000000, double:10.0)
            int r14 = (r1 > r12 ? 1 : (r1 == r12 ? 0 : -1))
            r15 = 1
            if (r14 < 0) goto L_0x004a
            long r10 = r10 + r15
            double r1 = r1 / r12
            goto L_0x003f
        L_0x004a:
            r17 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r14 = (r1 > r17 ? 1 : (r1 == r17 ? 0 : -1))
            if (r14 >= 0) goto L_0x0059
            r19 = 0
            int r14 = (r1 > r19 ? 1 : (r1 == r19 ? 0 : -1))
            if (r14 <= 0) goto L_0x0059
            long r10 = r10 - r15
            double r1 = r1 * r12
            goto L_0x004a
        L_0x0059:
            r14 = 4611686018427387904(0x4000000000000000, double:2.0)
            int r16 = (r1 > r14 ? 1 : (r1 == r14 ? 0 : -1))
            r19 = 4617315517961601024(0x4014000000000000, double:5.0)
            if (r16 >= 0) goto L_0x0062
            goto L_0x006b
        L_0x0062:
            int r1 = (r1 > r19 ? 1 : (r1 == r19 ? 0 : -1))
            if (r1 >= 0) goto L_0x0069
            r17 = r14
            goto L_0x006b
        L_0x0069:
            r17 = r19
        L_0x006b:
            if (r7 == 0) goto L_0x0070
            double r17 = r17 / r8
            goto L_0x0081
        L_0x0070:
            org.osmdroid.views.overlay.ScaleBarOverlay$UnitsOfMeasure r1 = r0.unitsOfMeasure
            org.osmdroid.views.overlay.ScaleBarOverlay$UnitsOfMeasure r2 = org.osmdroid.views.overlay.ScaleBarOverlay.UnitsOfMeasure.imperial
            if (r1 != r2) goto L_0x0079
            double r17 = r17 * r5
            goto L_0x0081
        L_0x0079:
            org.osmdroid.views.overlay.ScaleBarOverlay$UnitsOfMeasure r1 = r0.unitsOfMeasure
            org.osmdroid.views.overlay.ScaleBarOverlay$UnitsOfMeasure r2 = org.osmdroid.views.overlay.ScaleBarOverlay.UnitsOfMeasure.nautical
            if (r1 != r2) goto L_0x0081
            double r17 = r17 * r3
        L_0x0081:
            double r1 = (double) r10
            double r1 = java.lang.Math.pow(r12, r1)
            double r17 = r17 * r1
            return r17
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.overlay.ScaleBarOverlay.adjustScaleBarLength(double):double");
    }

    /* renamed from: org.osmdroid.views.overlay.ScaleBarOverlay$1 */
    static /* synthetic */ class C13891 {

        /* renamed from: $SwitchMap$org$osmdroid$views$overlay$ScaleBarOverlay$UnitsOfMeasure */
        static final /* synthetic */ int[] f565xde2f8ad0;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                org.osmdroid.views.overlay.ScaleBarOverlay$UnitsOfMeasure[] r0 = org.osmdroid.views.overlay.ScaleBarOverlay.UnitsOfMeasure.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f565xde2f8ad0 = r0
                org.osmdroid.views.overlay.ScaleBarOverlay$UnitsOfMeasure r1 = org.osmdroid.views.overlay.ScaleBarOverlay.UnitsOfMeasure.metric     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f565xde2f8ad0     // Catch:{ NoSuchFieldError -> 0x001d }
                org.osmdroid.views.overlay.ScaleBarOverlay$UnitsOfMeasure r1 = org.osmdroid.views.overlay.ScaleBarOverlay.UnitsOfMeasure.imperial     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f565xde2f8ad0     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.osmdroid.views.overlay.ScaleBarOverlay$UnitsOfMeasure r1 = org.osmdroid.views.overlay.ScaleBarOverlay.UnitsOfMeasure.nautical     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.overlay.ScaleBarOverlay.C13891.<clinit>():void");
        }
    }

    /* access modifiers changed from: protected */
    public String scaleBarLengthText(double d) {
        int i = C13891.f565xde2f8ad0[this.unitsOfMeasure.ordinal()];
        if (i != 2) {
            if (i != 3) {
                if (d >= 5000.0d) {
                    return getConvertedScaleString(d, GeoConstants.UnitOfMeasure.kilometer, "%.0f");
                }
                if (d >= 200.0d) {
                    return getConvertedScaleString(d, GeoConstants.UnitOfMeasure.kilometer, "%.1f");
                }
                if (d >= 20.0d) {
                    return getConvertedScaleString(d, GeoConstants.UnitOfMeasure.meter, "%.0f");
                }
                return getConvertedScaleString(d, GeoConstants.UnitOfMeasure.meter, "%.2f");
            } else if (d >= 9260.0d) {
                return getConvertedScaleString(d, GeoConstants.UnitOfMeasure.nauticalMile, "%.0f");
            } else {
                if (d >= 370.4d) {
                    return getConvertedScaleString(d, GeoConstants.UnitOfMeasure.nauticalMile, "%.1f");
                }
                return getConvertedScaleString(d, GeoConstants.UnitOfMeasure.foot, "%.0f");
            }
        } else if (d >= 8046.72d) {
            return getConvertedScaleString(d, GeoConstants.UnitOfMeasure.statuteMile, "%.0f");
        } else {
            if (d >= 321.8688d) {
                return getConvertedScaleString(d, GeoConstants.UnitOfMeasure.statuteMile, "%.1f");
            }
            return getConvertedScaleString(d, GeoConstants.UnitOfMeasure.foot, "%.0f");
        }
    }

    public void onDetach(MapView mapView) {
        this.context = null;
        this.mMapView = null;
        this.barPaint = null;
        this.bgPaint = null;
        this.textPaint = null;
    }

    private String getConvertedScaleString(double d, GeoConstants.UnitOfMeasure unitOfMeasure, String str) {
        return getScaleString(this.context, String.format(Locale.getDefault(), str, new Object[]{Double.valueOf(d / unitOfMeasure.getConversionFactorToMeters())}), unitOfMeasure);
    }

    public static String getScaleString(Context context2, String str, GeoConstants.UnitOfMeasure unitOfMeasure) {
        return context2.getString(C1340R.string.format_distance_value_unit, new Object[]{str, context2.getString(unitOfMeasure.getStringResId())});
    }

    private int getMapWidth() {
        MapView mapView = this.mMapView;
        return mapView != null ? mapView.getWidth() : this.mMapWidth;
    }

    private int getMapHeight() {
        MapView mapView = this.mMapView;
        return mapView != null ? mapView.getHeight() : this.mMapHeight;
    }
}
