package org.mapsforge.map.android.graphics;

import android.graphics.BitmapShader;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import org.mapsforge.core.graphics.Align;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Cap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.FontFamily;
import org.mapsforge.core.graphics.FontStyle;
import org.mapsforge.core.graphics.Join;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.Point;

class AndroidPaint implements Paint {
    final android.graphics.Paint paint;
    private final Rect rect = new Rect();
    private int shaderHeight;
    private int shaderWidth;

    private static Paint.Align getAndroidAlign(Align align) {
        int i = C13111.$SwitchMap$org$mapsforge$core$graphics$Align[align.ordinal()];
        if (i == 1) {
            return Paint.Align.CENTER;
        }
        if (i == 2) {
            return Paint.Align.LEFT;
        }
        if (i == 3) {
            return Paint.Align.RIGHT;
        }
        throw new IllegalArgumentException("unknown align: " + align);
    }

    private static Paint.Cap getAndroidCap(Cap cap) {
        int i = C13111.$SwitchMap$org$mapsforge$core$graphics$Cap[cap.ordinal()];
        if (i == 1) {
            return Paint.Cap.BUTT;
        }
        if (i == 2) {
            return Paint.Cap.ROUND;
        }
        if (i == 3) {
            return Paint.Cap.SQUARE;
        }
        throw new IllegalArgumentException("unknown cap: " + cap);
    }

    private static Paint.Join getAndroidJoin(Join join) {
        int i = C13111.$SwitchMap$org$mapsforge$core$graphics$Join[join.ordinal()];
        if (i == 1) {
            return Paint.Join.BEVEL;
        }
        if (i == 2) {
            return Paint.Join.ROUND;
        }
        if (i == 3) {
            return Paint.Join.MITER;
        }
        throw new IllegalArgumentException("unknown join: " + join);
    }

    private static Paint.Style getAndroidStyle(Style style) {
        int i = C13111.$SwitchMap$org$mapsforge$core$graphics$Style[style.ordinal()];
        if (i == 1) {
            return Paint.Style.FILL;
        }
        if (i == 2) {
            return Paint.Style.STROKE;
        }
        throw new IllegalArgumentException("unknown style: " + style);
    }

    private static int getFontStyle(FontStyle fontStyle) {
        int i = C13111.$SwitchMap$org$mapsforge$core$graphics$FontStyle[fontStyle.ordinal()];
        int i2 = 1;
        if (i != 1) {
            i2 = 3;
            if (i != 2) {
                if (i == 3) {
                    return 2;
                }
                if (i == 4) {
                    return 0;
                }
                throw new IllegalArgumentException("unknown font style: " + fontStyle);
            }
        }
        return i2;
    }

    /* renamed from: org.mapsforge.map.android.graphics.AndroidPaint$1 */
    static /* synthetic */ class C13111 {
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$core$graphics$Align;
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$core$graphics$Cap;
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$core$graphics$FontFamily;
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$core$graphics$FontStyle;
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$core$graphics$Join;
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$core$graphics$Style;

        /* JADX WARNING: Can't wrap try/catch for region: R(42:0|(2:1|2)|3|(2:5|6)|7|9|10|11|(2:13|14)|15|17|18|19|20|21|22|(2:23|24)|25|27|28|(2:29|30)|31|33|34|35|36|(2:37|38)|39|41|42|43|44|45|46|47|49|50|51|52|53|54|56) */
        /* JADX WARNING: Can't wrap try/catch for region: R(45:0|(2:1|2)|3|5|6|7|9|10|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|27|28|(2:29|30)|31|33|34|35|36|37|38|39|41|42|43|44|45|46|47|49|50|51|52|53|54|56) */
        /* JADX WARNING: Can't wrap try/catch for region: R(46:0|(2:1|2)|3|5|6|7|9|10|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|33|34|35|36|37|38|39|41|42|43|44|45|46|47|49|50|51|52|53|54|56) */
        /* JADX WARNING: Can't wrap try/catch for region: R(48:0|1|2|3|5|6|7|9|10|11|13|14|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|33|34|35|36|37|38|39|41|42|43|44|45|46|47|49|50|51|52|53|54|56) */
        /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0044 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x004e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0058 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0073 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x008e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x0098 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00b3 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x00bd */
        /* JADX WARNING: Missing exception handler attribute for start block: B:51:0x00d8 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x00e2 */
        static {
            /*
                org.mapsforge.core.graphics.FontFamily[] r0 = org.mapsforge.core.graphics.FontFamily.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$mapsforge$core$graphics$FontFamily = r0
                r1 = 1
                org.mapsforge.core.graphics.FontFamily r2 = org.mapsforge.core.graphics.FontFamily.DEFAULT     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$org$mapsforge$core$graphics$FontFamily     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.core.graphics.FontFamily r3 = org.mapsforge.core.graphics.FontFamily.MONOSPACE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = $SwitchMap$org$mapsforge$core$graphics$FontFamily     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.mapsforge.core.graphics.FontFamily r4 = org.mapsforge.core.graphics.FontFamily.SANS_SERIF     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                r3 = 4
                int[] r4 = $SwitchMap$org$mapsforge$core$graphics$FontFamily     // Catch:{ NoSuchFieldError -> 0x0033 }
                org.mapsforge.core.graphics.FontFamily r5 = org.mapsforge.core.graphics.FontFamily.SERIF     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                org.mapsforge.core.graphics.FontStyle[] r4 = org.mapsforge.core.graphics.FontStyle.values()
                int r4 = r4.length
                int[] r4 = new int[r4]
                $SwitchMap$org$mapsforge$core$graphics$FontStyle = r4
                org.mapsforge.core.graphics.FontStyle r5 = org.mapsforge.core.graphics.FontStyle.BOLD     // Catch:{ NoSuchFieldError -> 0x0044 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0044 }
                r4[r5] = r1     // Catch:{ NoSuchFieldError -> 0x0044 }
            L_0x0044:
                int[] r4 = $SwitchMap$org$mapsforge$core$graphics$FontStyle     // Catch:{ NoSuchFieldError -> 0x004e }
                org.mapsforge.core.graphics.FontStyle r5 = org.mapsforge.core.graphics.FontStyle.BOLD_ITALIC     // Catch:{ NoSuchFieldError -> 0x004e }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x004e }
                r4[r5] = r0     // Catch:{ NoSuchFieldError -> 0x004e }
            L_0x004e:
                int[] r4 = $SwitchMap$org$mapsforge$core$graphics$FontStyle     // Catch:{ NoSuchFieldError -> 0x0058 }
                org.mapsforge.core.graphics.FontStyle r5 = org.mapsforge.core.graphics.FontStyle.ITALIC     // Catch:{ NoSuchFieldError -> 0x0058 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0058 }
                r4[r5] = r2     // Catch:{ NoSuchFieldError -> 0x0058 }
            L_0x0058:
                int[] r4 = $SwitchMap$org$mapsforge$core$graphics$FontStyle     // Catch:{ NoSuchFieldError -> 0x0062 }
                org.mapsforge.core.graphics.FontStyle r5 = org.mapsforge.core.graphics.FontStyle.NORMAL     // Catch:{ NoSuchFieldError -> 0x0062 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0062 }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x0062 }
            L_0x0062:
                org.mapsforge.core.graphics.Style[] r3 = org.mapsforge.core.graphics.Style.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                $SwitchMap$org$mapsforge$core$graphics$Style = r3
                org.mapsforge.core.graphics.Style r4 = org.mapsforge.core.graphics.Style.FILL     // Catch:{ NoSuchFieldError -> 0x0073 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0073 }
                r3[r4] = r1     // Catch:{ NoSuchFieldError -> 0x0073 }
            L_0x0073:
                int[] r3 = $SwitchMap$org$mapsforge$core$graphics$Style     // Catch:{ NoSuchFieldError -> 0x007d }
                org.mapsforge.core.graphics.Style r4 = org.mapsforge.core.graphics.Style.STROKE     // Catch:{ NoSuchFieldError -> 0x007d }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x007d }
                r3[r4] = r0     // Catch:{ NoSuchFieldError -> 0x007d }
            L_0x007d:
                org.mapsforge.core.graphics.Join[] r3 = org.mapsforge.core.graphics.Join.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                $SwitchMap$org$mapsforge$core$graphics$Join = r3
                org.mapsforge.core.graphics.Join r4 = org.mapsforge.core.graphics.Join.BEVEL     // Catch:{ NoSuchFieldError -> 0x008e }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x008e }
                r3[r4] = r1     // Catch:{ NoSuchFieldError -> 0x008e }
            L_0x008e:
                int[] r3 = $SwitchMap$org$mapsforge$core$graphics$Join     // Catch:{ NoSuchFieldError -> 0x0098 }
                org.mapsforge.core.graphics.Join r4 = org.mapsforge.core.graphics.Join.ROUND     // Catch:{ NoSuchFieldError -> 0x0098 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0098 }
                r3[r4] = r0     // Catch:{ NoSuchFieldError -> 0x0098 }
            L_0x0098:
                int[] r3 = $SwitchMap$org$mapsforge$core$graphics$Join     // Catch:{ NoSuchFieldError -> 0x00a2 }
                org.mapsforge.core.graphics.Join r4 = org.mapsforge.core.graphics.Join.MITER     // Catch:{ NoSuchFieldError -> 0x00a2 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x00a2 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x00a2 }
            L_0x00a2:
                org.mapsforge.core.graphics.Cap[] r3 = org.mapsforge.core.graphics.Cap.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                $SwitchMap$org$mapsforge$core$graphics$Cap = r3
                org.mapsforge.core.graphics.Cap r4 = org.mapsforge.core.graphics.Cap.BUTT     // Catch:{ NoSuchFieldError -> 0x00b3 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x00b3 }
                r3[r4] = r1     // Catch:{ NoSuchFieldError -> 0x00b3 }
            L_0x00b3:
                int[] r3 = $SwitchMap$org$mapsforge$core$graphics$Cap     // Catch:{ NoSuchFieldError -> 0x00bd }
                org.mapsforge.core.graphics.Cap r4 = org.mapsforge.core.graphics.Cap.ROUND     // Catch:{ NoSuchFieldError -> 0x00bd }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x00bd }
                r3[r4] = r0     // Catch:{ NoSuchFieldError -> 0x00bd }
            L_0x00bd:
                int[] r3 = $SwitchMap$org$mapsforge$core$graphics$Cap     // Catch:{ NoSuchFieldError -> 0x00c7 }
                org.mapsforge.core.graphics.Cap r4 = org.mapsforge.core.graphics.Cap.SQUARE     // Catch:{ NoSuchFieldError -> 0x00c7 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x00c7 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x00c7 }
            L_0x00c7:
                org.mapsforge.core.graphics.Align[] r3 = org.mapsforge.core.graphics.Align.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                $SwitchMap$org$mapsforge$core$graphics$Align = r3
                org.mapsforge.core.graphics.Align r4 = org.mapsforge.core.graphics.Align.CENTER     // Catch:{ NoSuchFieldError -> 0x00d8 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x00d8 }
                r3[r4] = r1     // Catch:{ NoSuchFieldError -> 0x00d8 }
            L_0x00d8:
                int[] r1 = $SwitchMap$org$mapsforge$core$graphics$Align     // Catch:{ NoSuchFieldError -> 0x00e2 }
                org.mapsforge.core.graphics.Align r3 = org.mapsforge.core.graphics.Align.LEFT     // Catch:{ NoSuchFieldError -> 0x00e2 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x00e2 }
                r1[r3] = r0     // Catch:{ NoSuchFieldError -> 0x00e2 }
            L_0x00e2:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Align     // Catch:{ NoSuchFieldError -> 0x00ec }
                org.mapsforge.core.graphics.Align r1 = org.mapsforge.core.graphics.Align.RIGHT     // Catch:{ NoSuchFieldError -> 0x00ec }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00ec }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00ec }
            L_0x00ec:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.android.graphics.AndroidPaint.C13111.<clinit>():void");
        }
    }

    private static Typeface getTypeface(FontFamily fontFamily) {
        int i = C13111.$SwitchMap$org$mapsforge$core$graphics$FontFamily[fontFamily.ordinal()];
        if (i == 1) {
            return Typeface.DEFAULT;
        }
        if (i == 2) {
            return Typeface.MONOSPACE;
        }
        if (i == 3) {
            return Typeface.SANS_SERIF;
        }
        if (i == 4) {
            return Typeface.SERIF;
        }
        throw new IllegalArgumentException("unknown font family: " + fontFamily);
    }

    AndroidPaint() {
        android.graphics.Paint paint2 = new android.graphics.Paint();
        this.paint = paint2;
        paint2.setAntiAlias(true);
        paint2.setStrokeCap(getAndroidCap(Cap.ROUND));
        paint2.setStrokeJoin(Paint.Join.ROUND);
        paint2.setStyle(getAndroidStyle(Style.FILL));
    }

    AndroidPaint(org.mapsforge.core.graphics.Paint paint2) {
        this.paint = new android.graphics.Paint(((AndroidPaint) paint2).paint);
    }

    public int getColor() {
        return this.paint.getColor();
    }

    public float getStrokeWidth() {
        return this.paint.getStrokeWidth();
    }

    public int getTextHeight(String str) {
        this.paint.getTextBounds(str, 0, str.length(), this.rect);
        return this.rect.height();
    }

    public int getTextWidth(String str) {
        return (int) this.paint.measureText(str);
    }

    public boolean isTransparent() {
        return this.paint.getShader() == null && this.paint.getAlpha() == 0;
    }

    public void setBitmapShader(Bitmap bitmap) {
        android.graphics.Bitmap bitmap2;
        if (bitmap != null && (bitmap2 = AndroidGraphicFactory.getBitmap(bitmap)) != null) {
            this.shaderWidth = bitmap.getWidth();
            this.shaderHeight = bitmap.getHeight();
            this.paint.setColor(AndroidGraphicFactory.getColor(Color.WHITE));
            this.paint.setShader(new BitmapShader(bitmap2, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        }
    }

    public void setBitmapShaderShift(Point point) {
        Shader shader = this.paint.getShader();
        if (shader != null) {
            int i = ((int) (-point.f381x)) % this.shaderWidth;
            int i2 = ((int) (-point.f382y)) % this.shaderHeight;
            Matrix matrix = new Matrix();
            matrix.setTranslate((float) i, (float) i2);
            shader.setLocalMatrix(matrix);
        }
    }

    public void setColor(Color color) {
        this.paint.setColor(AndroidGraphicFactory.getColor(color));
    }

    public void setColor(int i) {
        this.paint.setColor(i);
    }

    public void setDashPathEffect(float[] fArr) {
        this.paint.setPathEffect(new DashPathEffect(fArr, 0.0f));
    }

    public void setStrokeCap(Cap cap) {
        this.paint.setStrokeCap(getAndroidCap(cap));
    }

    public void setStrokeJoin(Join join) {
        this.paint.setStrokeJoin(getAndroidJoin(join));
    }

    public void setStrokeWidth(float f) {
        this.paint.setStrokeWidth(f);
    }

    public void setStyle(Style style) {
        this.paint.setStyle(getAndroidStyle(style));
    }

    public void setTextAlign(Align align) {
        this.paint.setTextAlign(getAndroidAlign(align));
    }

    public void setTextSize(float f) {
        this.paint.setTextSize(f);
    }

    public void setTypeface(FontFamily fontFamily, FontStyle fontStyle) {
        this.paint.setTypeface(Typeface.create(getTypeface(fontFamily), getFontStyle(fontStyle)));
    }
}
