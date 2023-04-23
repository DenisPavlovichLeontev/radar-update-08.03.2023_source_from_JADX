package org.osmdroid.views.overlay.gridlines;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import androidx.core.view.ViewCompat;
import java.text.DecimalFormat;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

public class LatLonGridlineOverlay2 extends Overlay {
    protected DecimalFormat mDecimalFormatter = new DecimalFormat("#.#####");
    protected Paint mLinePaint = new Paint();
    protected float mMultiplier = 1.0f;
    protected GeoPoint mOptimizationGeoPoint = new GeoPoint(0.0d, 0.0d);
    protected Point mOptimizationPoint = new Point();
    protected Paint mTextBackgroundPaint = new Paint();
    protected Paint mTextPaint = new Paint();

    public LatLonGridlineOverlay2() {
        this.mLinePaint.setAntiAlias(true);
        this.mLinePaint.setStyle(Paint.Style.STROKE);
        this.mTextBackgroundPaint.setStyle(Paint.Style.FILL);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setStyle(Paint.Style.STROKE);
        this.mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
        setLineColor(ViewCompat.MEASURED_STATE_MASK);
        setFontColor(-1);
        setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        setLineWidth(1.0f);
        setFontSizeDp(32);
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x0125  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0182  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x01b4  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x01d1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x01f3  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x01f6  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x01fa  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void draw(android.graphics.Canvas r49, org.osmdroid.views.Projection r50) {
        /*
            r48 = this;
            r0 = r48
            r7 = r49
            boolean r1 = r48.isEnabled()
            if (r1 != 0) goto L_0x000b
            return
        L_0x000b:
            double r1 = r50.getZoomLevel()
            int r1 = (int) r1
            double r8 = r0.getIncrementor(r1)
            org.osmdroid.util.GeoPoint r1 = r50.getCurrentCenter()
            double r2 = r1.getLongitude()
            double r2 = r2 / r8
            long r2 = java.lang.Math.round(r2)
            double r2 = (double) r2
            double r10 = r8 * r2
            double r1 = r1.getLatitude()
            double r12 = r0.computeStartLatitude(r1, r8)
            double r14 = r50.getWorldMapSize()
            int r1 = r50.getWidth()
            float r1 = (float) r1
            int r2 = r50.getHeight()
            float r5 = (float) r2
            r16 = 1073741824(0x40000000, float:2.0)
            float r17 = r1 / r16
            float r18 = r5 / r16
            float r2 = r1 * r1
            float r3 = r5 * r5
            float r2 = r2 + r3
            double r2 = (double) r2
            double r2 = java.lang.Math.sqrt(r2)
            float r6 = (float) r2
            float r2 = r6 / r16
            double r2 = (double) r2
            double r19 = r2 * r2
            r2 = 1084227584(0x40a00000, float:5.0)
            float r21 = r1 / r2
            float r22 = r5 / r2
            android.graphics.Paint r1 = r0.mTextPaint
            float r1 = r1.ascent()
            float r1 = -r1
            r23 = 1056964608(0x3f000000, float:0.5)
            float r1 = r1 + r23
            android.graphics.Paint r2 = r0.mTextPaint
            float r2 = r2.descent()
            float r24 = r2 + r23
            float r25 = r1 + r24
            r26 = 0
            r3 = r26
        L_0x006f:
            r4 = 1
            if (r3 > r4) goto L_0x02b3
            r1 = r26
        L_0x0074:
            if (r1 > r4) goto L_0x02a5
            float r2 = r50.getOrientation()
            float r2 = -r2
            if (r1 != 0) goto L_0x0080
            r4 = r26
            goto L_0x0084
        L_0x0080:
            r27 = 90
            r4 = r27
        L_0x0084:
            float r4 = (float) r4
            float r4 = r4 + r2
            r27 = r3
            r2 = r26
        L_0x008a:
            r3 = 1
            if (r2 > r3) goto L_0x0294
            r29 = r2
            if (r2 != 0) goto L_0x0094
            r30 = r8
            goto L_0x0097
        L_0x0094:
            double r2 = -r8
            r30 = r2
        L_0x0097:
            if (r1 != 0) goto L_0x009e
            int r2 = java.lang.Math.round(r18)
            goto L_0x00a2
        L_0x009e:
            int r2 = java.lang.Math.round(r17)
        L_0x00a2:
            r3 = r2
            r36 = r10
            r34 = r12
            r33 = r26
            r32 = 1
        L_0x00ab:
            if (r32 == 0) goto L_0x027f
            if (r33 <= 0) goto L_0x0109
            r2 = 1
            if (r1 != r2) goto L_0x00d1
            double r36 = r36 + r30
        L_0x00b4:
            r38 = -4582834833314545664(0xc066800000000000, double:-180.0)
            int r28 = (r36 > r38 ? 1 : (r36 == r38 ? 0 : -1))
            r38 = 4645040803167600640(0x4076800000000000, double:360.0)
            if (r28 >= 0) goto L_0x00c5
            double r36 = r36 + r38
            goto L_0x00b4
        L_0x00c5:
            r40 = 4640537203540230144(0x4066800000000000, double:180.0)
            int r28 = (r36 > r40 ? 1 : (r36 == r40 ? 0 : -1))
            if (r28 <= 0) goto L_0x0109
            double r36 = r36 - r38
            goto L_0x00c5
        L_0x00d1:
            double r34 = r34 + r30
            org.osmdroid.util.TileSystem r28 = org.osmdroid.views.MapView.getTileSystem()
            double r38 = r28.getMaxLatitude()
            int r28 = (r34 > r38 ? 1 : (r34 == r38 ? 0 : -1))
            if (r28 <= 0) goto L_0x00ee
            org.osmdroid.util.TileSystem r28 = org.osmdroid.views.MapView.getTileSystem()
            r38 = r3
            double r2 = r28.getMinLatitude()
            double r34 = r0.computeStartLatitude(r2, r8)
            goto L_0x010b
        L_0x00ee:
            r38 = r3
            org.osmdroid.util.TileSystem r2 = org.osmdroid.views.MapView.getTileSystem()
            double r2 = r2.getMinLatitude()
            int r2 = (r34 > r2 ? 1 : (r34 == r2 ? 0 : -1))
            if (r2 >= 0) goto L_0x010b
            org.osmdroid.util.TileSystem r2 = org.osmdroid.views.MapView.getTileSystem()
            double r2 = r2.getMaxLatitude()
            double r34 = r0.computeStartLatitude(r2, r8)
            goto L_0x010b
        L_0x0109:
            r38 = r3
        L_0x010b:
            r28 = r4
            r2 = r34
            r34 = r8
            r8 = r36
            org.osmdroid.util.GeoPoint r4 = r0.mOptimizationGeoPoint
            r4.setCoords(r2, r8)
            org.osmdroid.util.GeoPoint r4 = r0.mOptimizationGeoPoint
            r36 = r2
            android.graphics.Point r2 = r0.mOptimizationPoint
            r3 = r50
            r3.toPixels(r4, r2)
            if (r1 != 0) goto L_0x0182
            android.graphics.Point r4 = r0.mOptimizationPoint
            r40 = r6
            r2 = r38
            r38 = r5
            double r5 = (double) r2
            r41 = r1
            int r1 = r4.y
            r42 = r4
            double r3 = (double) r1
            r43 = r8
            r8 = r29
            r29 = r41
            r39 = 1
            r9 = r2
            r1 = r5
            r5 = r28
            r6 = r42
            r39 = r40
            r40 = r10
            r10 = r5
            r11 = r6
            r5 = r14
            double r1 = org.osmdroid.views.overlay.LinearRing.getCloserValue(r1, r3, r5)
            long r1 = java.lang.Math.round(r1)
            int r1 = (int) r1
            r11.y = r1
            if (r33 <= 0) goto L_0x017d
            r1 = 0
            int r1 = (r30 > r1 ? 1 : (r30 == r1 ? 0 : -1))
            if (r1 >= 0) goto L_0x016d
        L_0x015d:
            android.graphics.Point r1 = r0.mOptimizationPoint
            int r1 = r1.y
            if (r1 >= r9) goto L_0x017d
            android.graphics.Point r1 = r0.mOptimizationPoint
            int r2 = r1.y
            double r2 = (double) r2
            double r2 = r2 + r14
            int r2 = (int) r2
            r1.y = r2
            goto L_0x015d
        L_0x016d:
            android.graphics.Point r1 = r0.mOptimizationPoint
            int r1 = r1.y
            if (r1 <= r9) goto L_0x017d
            android.graphics.Point r1 = r0.mOptimizationPoint
            int r2 = r1.y
            double r2 = (double) r2
            double r2 = r2 - r14
            int r2 = (int) r2
            r1.y = r2
            goto L_0x016d
        L_0x017d:
            android.graphics.Point r1 = r0.mOptimizationPoint
            int r1 = r1.y
            goto L_0x01a8
        L_0x0182:
            r39 = r6
            r43 = r8
            r40 = r10
            r10 = r28
            r8 = r29
            r9 = r38
            r29 = r1
            r38 = r5
            android.graphics.Point r11 = r0.mOptimizationPoint
            double r1 = (double) r9
            int r3 = r11.x
            double r3 = (double) r3
            r5 = r14
            double r1 = org.osmdroid.views.overlay.LinearRing.getCloserValue(r1, r3, r5)
            long r1 = java.lang.Math.round(r1)
            int r1 = (int) r1
            r11.x = r1
            android.graphics.Point r1 = r0.mOptimizationPoint
            int r1 = r1.x
        L_0x01a8:
            r9 = r1
            r11 = 1
            if (r33 != 0) goto L_0x01b2
            if (r8 != r11) goto L_0x01b2
        L_0x01ae:
            r47 = r9
            goto L_0x0268
        L_0x01b2:
            if (r29 != 0) goto L_0x01d1
            android.graphics.Point r1 = r0.mOptimizationPoint
            int r1 = r1.y
            float r1 = (float) r1
            float r2 = r17 - r39
            float r6 = r17 + r39
            android.graphics.Point r3 = r0.mOptimizationPoint
            int r3 = r3.y
            float r3 = (float) r3
            float r3 = r3 - r18
            android.graphics.Point r4 = r0.mOptimizationPoint
            int r4 = r4.y
            float r4 = (float) r4
            float r4 = r4 - r18
            float r3 = r3 * r4
            double r3 = (double) r3
            r5 = r1
            r28 = r5
            goto L_0x01ef
        L_0x01d1:
            android.graphics.Point r1 = r0.mOptimizationPoint
            int r1 = r1.x
            float r1 = (float) r1
            float r2 = r18 - r39
            float r6 = r18 + r39
            android.graphics.Point r3 = r0.mOptimizationPoint
            int r3 = r3.x
            float r3 = (float) r3
            float r3 = r3 - r17
            android.graphics.Point r4 = r0.mOptimizationPoint
            int r4 = r4.x
            float r4 = (float) r4
            float r4 = r4 - r17
            float r3 = r3 * r4
            double r3 = (double) r3
            r5 = r2
            r28 = r6
            r2 = r1
            r6 = r2
        L_0x01ef:
            int r1 = (r3 > r19 ? 1 : (r3 == r19 ? 0 : -1))
            if (r1 > 0) goto L_0x01f6
            r32 = r11
            goto L_0x01f8
        L_0x01f6:
            r32 = r26
        L_0x01f8:
            if (r32 == 0) goto L_0x01ae
            if (r27 != 0) goto L_0x020c
            android.graphics.Paint r4 = r0.mLinePaint
            r1 = r49
            r3 = r5
            r42 = r4
            r4 = r6
            r5 = r28
            r6 = r42
            r1.drawLine(r2, r3, r4, r5, r6)
            goto L_0x01ae
        L_0x020c:
            if (r29 != 0) goto L_0x0211
            r3 = r36
            goto L_0x0213
        L_0x0211:
            r3 = r43
        L_0x0213:
            if (r29 != 0) goto L_0x0217
            r1 = r11
            goto L_0x0219
        L_0x0217:
            r1 = r26
        L_0x0219:
            java.lang.String r6 = r0.formatCoordinate(r3, r1)
            if (r29 != 0) goto L_0x0222
            r4 = r21
            goto L_0x0223
        L_0x0222:
            r4 = r2
        L_0x0223:
            if (r29 != 0) goto L_0x0226
            goto L_0x0228
        L_0x0226:
            float r5 = r38 - r22
        L_0x0228:
            android.graphics.Paint r1 = r0.mTextPaint
            float r1 = r1.measureText(r6)
            float r1 = r1 + r23
            r2 = 0
            int r28 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
            if (r28 == 0) goto L_0x023b
            r49.save()
            r7.rotate(r10, r4, r5)
        L_0x023b:
            float r1 = r1 / r16
            float r2 = r4 - r1
            float r3 = r25 / r16
            float r42 = r5 - r3
            float r45 = r4 + r1
            float r46 = r5 + r3
            android.graphics.Paint r5 = r0.mTextBackgroundPaint
            r1 = r49
            r3 = r42
            r11 = r4
            r4 = r45
            r45 = r5
            r5 = r46
            r47 = r9
            r9 = r6
            r6 = r45
            r1.drawRect(r2, r3, r4, r5, r6)
            float r1 = r46 - r24
            android.graphics.Paint r2 = r0.mTextPaint
            r7.drawText(r9, r11, r1, r2)
            if (r28 == 0) goto L_0x0268
            r49.restore()
        L_0x0268:
            int r33 = r33 + 1
            r4 = r10
            r1 = r29
            r5 = r38
            r6 = r39
            r10 = r40
            r3 = r47
            r29 = r8
            r8 = r34
            r34 = r36
            r36 = r43
            goto L_0x00ab
        L_0x027f:
            r38 = r5
            r39 = r6
            r34 = r8
            r40 = r10
            r8 = r29
            r29 = r1
            r10 = r4
            int r2 = r8 + 1
            r8 = r34
            r10 = r40
            goto L_0x008a
        L_0x0294:
            r29 = r1
            r38 = r5
            r39 = r6
            r34 = r8
            r40 = r10
            int r1 = r29 + 1
            r3 = r27
            r4 = 1
            goto L_0x0074
        L_0x02a5:
            r27 = r3
            r38 = r5
            r39 = r6
            r34 = r8
            r40 = r10
            int r3 = r27 + 1
            goto L_0x006f
        L_0x02b3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2.draw(android.graphics.Canvas, org.osmdroid.views.Projection):void");
    }

    public void setDecimalFormatter(DecimalFormat decimalFormat) {
        this.mDecimalFormatter = decimalFormat;
    }

    public void setLineColor(int i) {
        this.mLinePaint.setColor(i);
    }

    public void setFontColor(int i) {
        this.mTextPaint.setColor(i);
    }

    public void setFontSizeDp(short s) {
        this.mTextPaint.setTextSize((float) s);
    }

    public void setTextStyle(Paint.Style style) {
        this.mTextPaint.setStyle(style);
    }

    public void setTextPaint(Paint paint) {
        this.mTextPaint = paint;
    }

    public Paint getTextPaint() {
        return this.mTextPaint;
    }

    public void setBackgroundColor(int i) {
        this.mTextBackgroundPaint.setColor(i);
    }

    public void setLineWidth(float f) {
        this.mLinePaint.setStrokeWidth(f);
    }

    public void setMultiplier(float f) {
        this.mMultiplier = f;
    }

    /* access modifiers changed from: protected */
    public double getIncrementor(int i) {
        double d;
        float f;
        switch (i) {
            case 0:
            case 1:
                d = 30.0d;
                f = this.mMultiplier;
                break;
            case 2:
                d = 15.0d;
                f = this.mMultiplier;
                break;
            case 3:
                d = 9.0d;
                f = this.mMultiplier;
                break;
            case 4:
                d = 6.0d;
                f = this.mMultiplier;
                break;
            case 5:
                d = 3.0d;
                f = this.mMultiplier;
                break;
            case 6:
                d = 2.0d;
                f = this.mMultiplier;
                break;
            case 7:
                d = 1.0d;
                f = this.mMultiplier;
                break;
            case 8:
                d = 0.5d;
                f = this.mMultiplier;
                break;
            case 9:
                d = 0.25d;
                f = this.mMultiplier;
                break;
            case 10:
                d = 0.1d;
                f = this.mMultiplier;
                break;
            case 11:
                d = 0.05d;
                f = this.mMultiplier;
                break;
            case 12:
                d = 0.025d;
                f = this.mMultiplier;
                break;
            case 13:
                d = 0.0125d;
                f = this.mMultiplier;
                break;
            case 14:
                d = 0.00625d;
                f = this.mMultiplier;
                break;
            case 15:
                d = 0.003125d;
                f = this.mMultiplier;
                break;
            case 16:
                d = 0.0015625d;
                f = this.mMultiplier;
                break;
            case 17:
                d = 7.8125E-4d;
                f = this.mMultiplier;
                break;
            case 18:
                d = 3.90625E-4d;
                f = this.mMultiplier;
                break;
            case 19:
                d = 1.953125E-4d;
                f = this.mMultiplier;
                break;
            case 20:
                d = 9.765625E-5d;
                f = this.mMultiplier;
                break;
            case 21:
                d = 4.8828125E-5d;
                f = this.mMultiplier;
                break;
            case 22:
                d = 2.44140625E-5d;
                f = this.mMultiplier;
                break;
            case 23:
                d = 1.220703125E-5d;
                f = this.mMultiplier;
                break;
            case 24:
                d = 6.103515625E-6d;
                f = this.mMultiplier;
                break;
            case 25:
                d = 3.0517578125E-6d;
                f = this.mMultiplier;
                break;
            case 26:
                d = 1.52587890625E-6d;
                f = this.mMultiplier;
                break;
            case 27:
                d = 7.62939453125E-7d;
                f = this.mMultiplier;
                break;
            case 28:
                d = 3.814697265625E-7d;
                f = this.mMultiplier;
                break;
            default:
                d = 1.9073486328125E-7d;
                f = this.mMultiplier;
                break;
        }
        return ((double) f) * d;
    }

    private double computeStartLatitude(double d, double d2) {
        double round = ((double) Math.round(d / d2)) * d2;
        while (round > MapView.getTileSystem().getMaxLatitude()) {
            round -= d2;
        }
        while (round < MapView.getTileSystem().getMinLatitude()) {
            round += d2;
        }
        return round;
    }

    private String formatCoordinate(double d, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mDecimalFormatter.format(d));
        int i = (d > 0.0d ? 1 : (d == 0.0d ? 0 : -1));
        sb.append(i == 0 ? "" : i > 0 ? z ? "N" : "E" : z ? "S" : "W");
        return sb.toString();
    }
}
