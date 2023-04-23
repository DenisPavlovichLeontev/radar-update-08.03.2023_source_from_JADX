package org.mapsforge.map.android.graphics;

import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.core.mapelements.PointTextContainer;
import org.mapsforge.core.mapelements.SymbolContainer;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Rectangle;

public class AndroidPointTextContainer extends PointTextContainer {
    private StaticLayout backLayout;
    private StaticLayout frontLayout;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    AndroidPointTextContainer(Point point, Display display, int i, String str, Paint paint, Paint paint2, SymbolContainer symbolContainer, Position position, int i2) {
        super(point, display, i, str, paint, paint2, symbolContainer, position, i2);
        int i3;
        float f;
        if (this.textWidth > this.maxTextWidth) {
            TextPaint textPaint = new TextPaint(AndroidGraphicFactory.getPaint(this.paintFront));
            TextPaint textPaint2 = this.paintBack != null ? new TextPaint(AndroidGraphicFactory.getPaint(this.paintBack)) : null;
            Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
            if (Position.LEFT == this.position || Position.BELOW_LEFT == this.position || Position.ABOVE_LEFT == this.position) {
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
            } else if (Position.RIGHT == this.position || Position.BELOW_RIGHT == this.position || Position.ABOVE_RIGHT == this.position) {
                alignment = Layout.Alignment.ALIGN_NORMAL;
            }
            textPaint.setTextAlign(Paint.Align.LEFT);
            if (this.paintBack != null) {
                textPaint2.setTextAlign(Paint.Align.LEFT);
            }
            this.frontLayout = new StaticLayout(this.text, textPaint, this.maxTextWidth, alignment, 1.0f, 0.0f, false);
            this.backLayout = null;
            if (this.paintBack != null) {
                this.backLayout = new StaticLayout(this.text, textPaint2, this.maxTextWidth, alignment, 1.0f, 0.0f, false);
            }
            f = (float) this.frontLayout.getWidth();
            i3 = this.frontLayout.getHeight();
        } else {
            f = (float) this.textWidth;
            i3 = this.textHeight;
        }
        float f2 = (float) i3;
        switch (C13131.$SwitchMap$org$mapsforge$core$graphics$Position[this.position.ordinal()]) {
            case 1:
                this.boundary = new Rectangle((double) ((-f) / 2.0f), (double) ((-f2) / 2.0f), (double) (f / 2.0f), (double) (f2 / 2.0f));
                return;
            case 2:
                this.boundary = new Rectangle((double) ((-f) / 2.0f), 0.0d, (double) (f / 2.0f), (double) f2);
                return;
            case 3:
                this.boundary = new Rectangle((double) (-f), 0.0d, 0.0d, (double) f2);
                return;
            case 4:
                this.boundary = new Rectangle(0.0d, 0.0d, (double) f, (double) f2);
                return;
            case 5:
                this.boundary = new Rectangle((double) ((-f) / 2.0f), (double) (-f2), (double) (f / 2.0f), 0.0d);
                return;
            case 6:
                this.boundary = new Rectangle((double) (-f), (double) (-f2), 0.0d, 0.0d);
                return;
            case 7:
                this.boundary = new Rectangle(0.0d, (double) (-f2), (double) f, 0.0d);
                return;
            case 8:
                this.boundary = new Rectangle((double) (-f), (double) ((-f2) / 2.0f), 0.0d, (double) (f2 / 2.0f));
                return;
            case 9:
                this.boundary = new Rectangle(0.0d, (double) ((-f2) / 2.0f), (double) f, (double) (f2 / 2.0f));
                return;
            default:
                return;
        }
    }

    /* renamed from: org.mapsforge.map.android.graphics.AndroidPointTextContainer$1 */
    static /* synthetic */ class C13131 {
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$core$graphics$Position;

        /* JADX WARNING: Can't wrap try/catch for region: R(18:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|(3:17|18|20)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                org.mapsforge.core.graphics.Position[] r0 = org.mapsforge.core.graphics.Position.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$mapsforge$core$graphics$Position = r0
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.CENTER     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.BELOW     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.BELOW_LEFT     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x0033 }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.BELOW_RIGHT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x003e }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.ABOVE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x0049 }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.ABOVE_LEFT     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x0054 }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.ABOVE_RIGHT     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x0060 }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.LEFT     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x006c }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.RIGHT     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.android.graphics.AndroidPointTextContainer.C13131.<clinit>():void");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00ad, code lost:
        if (r0 != 9) goto L_0x00ba;
     */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00cf  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0100  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0118  */
    /* JADX WARNING: Removed duplicated region for block: B:50:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void draw(org.mapsforge.core.graphics.Canvas r6, org.mapsforge.core.model.Point r7, org.mapsforge.core.graphics.Matrix r8, org.mapsforge.core.graphics.Filter r9) {
        /*
            r5 = this;
            boolean r8 = r5.isVisible
            if (r8 != 0) goto L_0x0005
            return
        L_0x0005:
            android.graphics.Canvas r6 = org.mapsforge.map.android.graphics.AndroidGraphicFactory.getCanvas(r6)
            int r8 = r5.textWidth
            int r0 = r5.maxTextWidth
            if (r8 <= r0) goto L_0x0090
            r6.save()
            org.mapsforge.core.model.Point r8 = r5.f380xy
            double r0 = r8.f381x
            double r2 = r7.f381x
            double r0 = r0 - r2
            org.mapsforge.core.model.Rectangle r8 = r5.boundary
            double r2 = r8.left
            double r0 = r0 + r2
            float r8 = (float) r0
            org.mapsforge.core.model.Point r0 = r5.f380xy
            double r0 = r0.f382y
            double r2 = r7.f382y
            double r0 = r0 - r2
            org.mapsforge.core.model.Rectangle r7 = r5.boundary
            double r2 = r7.top
            double r0 = r0 + r2
            float r7 = (float) r0
            r6.translate(r8, r7)
            android.text.StaticLayout r7 = r5.backLayout
            if (r7 == 0) goto L_0x005e
            android.text.TextPaint r7 = r7.getPaint()
            int r7 = r7.getColor()
            org.mapsforge.core.graphics.Filter r8 = org.mapsforge.core.graphics.Filter.NONE
            if (r9 == r8) goto L_0x004c
            android.text.StaticLayout r8 = r5.backLayout
            android.text.TextPaint r8 = r8.getPaint()
            int r0 = org.mapsforge.core.graphics.GraphicUtils.filterColor(r7, r9)
            r8.setColor(r0)
        L_0x004c:
            android.text.StaticLayout r8 = r5.backLayout
            r8.draw(r6)
            org.mapsforge.core.graphics.Filter r8 = org.mapsforge.core.graphics.Filter.NONE
            if (r9 == r8) goto L_0x005e
            android.text.StaticLayout r8 = r5.backLayout
            android.text.TextPaint r8 = r8.getPaint()
            r8.setColor(r7)
        L_0x005e:
            android.text.StaticLayout r7 = r5.frontLayout
            android.text.TextPaint r7 = r7.getPaint()
            int r7 = r7.getColor()
            org.mapsforge.core.graphics.Filter r8 = org.mapsforge.core.graphics.Filter.NONE
            if (r9 == r8) goto L_0x0079
            android.text.StaticLayout r8 = r5.frontLayout
            android.text.TextPaint r8 = r8.getPaint()
            int r0 = org.mapsforge.core.graphics.GraphicUtils.filterColor(r7, r9)
            r8.setColor(r0)
        L_0x0079:
            android.text.StaticLayout r8 = r5.frontLayout
            r8.draw(r6)
            org.mapsforge.core.graphics.Filter r8 = org.mapsforge.core.graphics.Filter.NONE
            if (r9 == r8) goto L_0x008b
            android.text.StaticLayout r8 = r5.frontLayout
            android.text.TextPaint r8 = r8.getPaint()
            r8.setColor(r7)
        L_0x008b:
            r6.restore()
            goto L_0x011d
        L_0x0090:
            r8 = 0
            int[] r0 = org.mapsforge.map.android.graphics.AndroidPointTextContainer.C13131.$SwitchMap$org$mapsforge$core$graphics$Position
            org.mapsforge.core.graphics.Position r1 = r5.position
            int r1 = r1.ordinal()
            r0 = r0[r1]
            r1 = 1
            if (r0 == r1) goto L_0x00b4
            r1 = 2
            if (r0 == r1) goto L_0x00b0
            r1 = 3
            if (r0 == r1) goto L_0x00b0
            r1 = 4
            if (r0 == r1) goto L_0x00b0
            r1 = 8
            if (r0 == r1) goto L_0x00b4
            r1 = 9
            if (r0 == r1) goto L_0x00b4
            goto L_0x00ba
        L_0x00b0:
            int r8 = r5.textHeight
            float r8 = (float) r8
            goto L_0x00ba
        L_0x00b4:
            int r8 = r5.textHeight
            float r8 = (float) r8
            r0 = 1073741824(0x40000000, float:2.0)
            float r8 = r8 / r0
        L_0x00ba:
            org.mapsforge.core.model.Point r0 = r5.f380xy
            double r0 = r0.f381x
            double r2 = r7.f381x
            double r0 = r0 - r2
            float r0 = (float) r0
            org.mapsforge.core.model.Point r1 = r5.f380xy
            double r1 = r1.f382y
            double r3 = r7.f382y
            double r1 = r1 - r3
            float r7 = (float) r1
            float r7 = r7 + r8
            org.mapsforge.core.graphics.Paint r8 = r5.paintBack
            if (r8 == 0) goto L_0x00f6
            org.mapsforge.core.graphics.Paint r8 = r5.paintBack
            int r8 = r8.getColor()
            org.mapsforge.core.graphics.Filter r1 = org.mapsforge.core.graphics.Filter.NONE
            if (r9 == r1) goto L_0x00e2
            org.mapsforge.core.graphics.Paint r1 = r5.paintBack
            int r2 = org.mapsforge.core.graphics.GraphicUtils.filterColor(r8, r9)
            r1.setColor((int) r2)
        L_0x00e2:
            java.lang.String r1 = r5.text
            org.mapsforge.core.graphics.Paint r2 = r5.paintBack
            android.graphics.Paint r2 = org.mapsforge.map.android.graphics.AndroidGraphicFactory.getPaint(r2)
            r6.drawText(r1, r0, r7, r2)
            org.mapsforge.core.graphics.Filter r1 = org.mapsforge.core.graphics.Filter.NONE
            if (r9 == r1) goto L_0x00f6
            org.mapsforge.core.graphics.Paint r1 = r5.paintBack
            r1.setColor((int) r8)
        L_0x00f6:
            org.mapsforge.core.graphics.Paint r8 = r5.paintFront
            int r8 = r8.getColor()
            org.mapsforge.core.graphics.Filter r1 = org.mapsforge.core.graphics.Filter.NONE
            if (r9 == r1) goto L_0x0109
            org.mapsforge.core.graphics.Paint r1 = r5.paintFront
            int r2 = org.mapsforge.core.graphics.GraphicUtils.filterColor(r8, r9)
            r1.setColor((int) r2)
        L_0x0109:
            java.lang.String r1 = r5.text
            org.mapsforge.core.graphics.Paint r2 = r5.paintFront
            android.graphics.Paint r2 = org.mapsforge.map.android.graphics.AndroidGraphicFactory.getPaint(r2)
            r6.drawText(r1, r0, r7, r2)
            org.mapsforge.core.graphics.Filter r6 = org.mapsforge.core.graphics.Filter.NONE
            if (r9 == r6) goto L_0x011d
            org.mapsforge.core.graphics.Paint r6 = r5.paintFront
            r6.setColor((int) r8)
        L_0x011d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.android.graphics.AndroidPointTextContainer.draw(org.mapsforge.core.graphics.Canvas, org.mapsforge.core.model.Point, org.mapsforge.core.graphics.Matrix, org.mapsforge.core.graphics.Filter):void");
    }
}
