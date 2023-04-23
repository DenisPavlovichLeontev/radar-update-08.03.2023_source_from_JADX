package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.HashMap;
import java.util.Map;
import org.mapsforge.core.graphics.Align;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.FontFamily;
import org.mapsforge.core.graphics.FontStyle;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.map.datastore.PointOfInterest;
import org.mapsforge.map.layer.renderer.PolylineContainer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.RenderContext;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.osgeo.proj4j.parser.Proj4Keyword;
import org.xmlpull.p018v1.XmlPullParser;
import org.xmlpull.p018v1.XmlPullParserException;

public class Caption extends RenderInstruction {
    public static final float DEFAULT_GAP = 5.0f;
    private Bitmap bitmap;
    private Display display;

    /* renamed from: dy */
    private float f389dy;
    private final Map<Byte, Float> dyScaled;
    private final Paint fill;
    private final Map<Byte, Paint> fills = new HashMap();
    private float fontSize;
    private final float gap;
    private final int maxTextWidth;
    private Position position;
    private int priority;
    private final Paint stroke;
    private final Map<Byte, Paint> strokes;
    private String symbolId;
    private TextKey textKey;

    public void destroy() {
    }

    public void scaleStrokeWidth(float f, byte b) {
    }

    public Caption(GraphicFactory graphicFactory, DisplayModel displayModel, String str, XmlPullParser xmlPullParser, Map<String, Symbol> map) throws XmlPullParserException {
        super(graphicFactory, displayModel);
        Symbol symbol;
        Paint createPaint = graphicFactory.createPaint();
        this.fill = createPaint;
        createPaint.setColor(Color.BLACK);
        createPaint.setStyle(Style.FILL);
        Paint createPaint2 = graphicFactory.createPaint();
        this.stroke = createPaint2;
        createPaint2.setColor(Color.BLACK);
        createPaint2.setStyle(Style.STROKE);
        this.strokes = new HashMap();
        this.dyScaled = new HashMap();
        this.display = Display.IFSPACE;
        this.gap = displayModel.getScaleFactor() * 5.0f;
        extractValues(graphicFactory, displayModel, str, xmlPullParser);
        String str2 = this.symbolId;
        if (!(str2 == null || (symbol = map.get(str2)) == null)) {
            this.bitmap = symbol.getBitmap();
        }
        if (this.position == null) {
            if (this.bitmap == null) {
                this.position = Position.CENTER;
            } else {
                this.position = Position.BELOW;
            }
        }
        switch (C13291.$SwitchMap$org$mapsforge$core$graphics$Position[this.position.ordinal()]) {
            case 1:
            case 2:
            case 3:
                createPaint2.setTextAlign(Align.CENTER);
                createPaint.setTextAlign(Align.CENTER);
                break;
            case 4:
            case 5:
            case 6:
                createPaint2.setTextAlign(Align.RIGHT);
                createPaint.setTextAlign(Align.RIGHT);
                break;
            case 7:
            case 8:
            case 9:
                createPaint2.setTextAlign(Align.LEFT);
                createPaint.setTextAlign(Align.LEFT);
                break;
            default:
                throw new IllegalArgumentException("Position invalid");
        }
        this.maxTextWidth = displayModel.getMaxTextWidth();
    }

    /* renamed from: org.mapsforge.map.rendertheme.renderinstruction.Caption$1 */
    static /* synthetic */ class C13291 {
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
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.ABOVE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x0033 }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.BELOW_LEFT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x003e }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.ABOVE_LEFT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x0049 }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.LEFT     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x0054 }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.BELOW_RIGHT     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Position     // Catch:{ NoSuchFieldError -> 0x0060 }
                org.mapsforge.core.graphics.Position r1 = org.mapsforge.core.graphics.Position.ABOVE_RIGHT     // Catch:{ NoSuchFieldError -> 0x0060 }
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
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.rendertheme.renderinstruction.Caption.C13291.<clinit>():void");
        }
    }

    private float computeHorizontalOffset() {
        if (Position.RIGHT != this.position && Position.LEFT != this.position && Position.BELOW_RIGHT != this.position && Position.BELOW_LEFT != this.position && Position.ABOVE_RIGHT != this.position && Position.ABOVE_LEFT != this.position) {
            return 0.0f;
        }
        float width = (((float) this.bitmap.getWidth()) / 2.0f) + this.gap;
        return (Position.LEFT == this.position || Position.BELOW_LEFT == this.position || Position.ABOVE_LEFT == this.position) ? width * -1.0f : width;
    }

    private float computeVerticalOffset(byte b) {
        float floatValue = this.dyScaled.get(Byte.valueOf(b)).floatValue();
        if (Position.ABOVE == this.position || Position.ABOVE_LEFT == this.position || Position.ABOVE_RIGHT == this.position) {
            return floatValue - ((((float) this.bitmap.getHeight()) / 2.0f) + this.gap);
        }
        if (Position.BELOW == this.position || Position.BELOW_LEFT == this.position || Position.BELOW_RIGHT == this.position) {
            return floatValue + (((float) this.bitmap.getHeight()) / 2.0f) + this.gap;
        }
        return floatValue;
    }

    private void extractValues(GraphicFactory graphicFactory, DisplayModel displayModel, String str, XmlPullParser xmlPullParser) throws XmlPullParserException {
        FontFamily fontFamily = FontFamily.DEFAULT;
        FontStyle fontStyle = FontStyle.NORMAL;
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            String attributeValue = xmlPullParser.getAttributeValue(i);
            if (Proj4Keyword.f424k.equals(attributeName)) {
                this.textKey = TextKey.getInstance(attributeValue);
            } else if ("cat".equals(attributeName)) {
                this.category = attributeValue;
            } else if ("display".equals(attributeName)) {
                this.display = Display.fromString(attributeValue);
            } else if ("dy".equals(attributeName)) {
                this.f389dy = Float.parseFloat(attributeValue) * displayModel.getScaleFactor();
            } else if ("fill".equals(attributeName)) {
                this.fill.setColor(XmlUtils.getColor(graphicFactory, attributeValue, displayModel.getThemeCallback(), this));
            } else if ("font-family".equals(attributeName)) {
                fontFamily = FontFamily.fromString(attributeValue);
            } else if ("font-size".equals(attributeName)) {
                this.fontSize = XmlUtils.parseNonNegativeFloat(attributeName, attributeValue) * displayModel.getScaleFactor();
            } else if ("font-style".equals(attributeName)) {
                fontStyle = FontStyle.fromString(attributeValue);
            } else if ("position".equals(attributeName)) {
                this.position = Position.fromString(attributeValue);
            } else if ("priority".equals(attributeName)) {
                this.priority = Integer.parseInt(attributeValue);
            } else if ("stroke".equals(attributeName)) {
                this.stroke.setColor(XmlUtils.getColor(graphicFactory, attributeValue, displayModel.getThemeCallback(), this));
            } else if ("stroke-width".equals(attributeName)) {
                this.stroke.setStrokeWidth(XmlUtils.parseNonNegativeFloat(attributeName, attributeValue) * displayModel.getScaleFactor());
            } else if ("symbol-id".equals(attributeName)) {
                this.symbolId = attributeValue;
            } else {
                throw XmlUtils.createXmlPullParserException(str, attributeName, attributeValue, i);
            }
        }
        this.fill.setTypeface(fontFamily, fontStyle);
        this.stroke.setTypeface(fontFamily, fontStyle);
        XmlUtils.checkMandatoryAttribute(str, Proj4Keyword.f424k, this.textKey);
    }

    private Paint getFillPaint(byte b) {
        Paint paint = this.fills.get(Byte.valueOf(b));
        return paint == null ? this.fill : paint;
    }

    private Paint getStrokePaint(byte b) {
        Paint paint = this.strokes.get(Byte.valueOf(b));
        return paint == null ? this.stroke : paint;
    }

    public void renderNode(RenderCallback renderCallback, RenderContext renderContext, PointOfInterest pointOfInterest) {
        String value;
        RenderContext renderContext2 = renderContext;
        if (Display.NEVER != this.display && (value = this.textKey.getValue(pointOfInterest.tags)) != null) {
            float f = 0.0f;
            Float f2 = this.dyScaled.get(Byte.valueOf(renderContext2.rendererJob.tile.zoomLevel));
            if (f2 == null) {
                f2 = Float.valueOf(this.f389dy);
            }
            if (this.bitmap != null) {
                f = computeHorizontalOffset();
                f2 = Float.valueOf(computeVerticalOffset(renderContext2.rendererJob.tile.zoomLevel));
            }
            Display display2 = this.display;
            int i = this.priority;
            renderCallback.renderPointOfInterestCaption(renderContext, display2, i, value, f, f2.floatValue(), getFillPaint(renderContext2.rendererJob.tile.zoomLevel), getStrokePaint(renderContext2.rendererJob.tile.zoomLevel), this.position, this.maxTextWidth, pointOfInterest);
        }
    }

    public void renderWay(RenderCallback renderCallback, RenderContext renderContext, PolylineContainer polylineContainer) {
        String value;
        RenderContext renderContext2 = renderContext;
        if (Display.NEVER != this.display && (value = this.textKey.getValue(polylineContainer.getTags())) != null) {
            float f = 0.0f;
            Float f2 = this.dyScaled.get(Byte.valueOf(renderContext2.rendererJob.tile.zoomLevel));
            if (f2 == null) {
                f2 = Float.valueOf(this.f389dy);
            }
            if (this.bitmap != null) {
                f = computeHorizontalOffset();
                f2 = Float.valueOf(computeVerticalOffset(renderContext2.rendererJob.tile.zoomLevel));
            }
            Display display2 = this.display;
            int i = this.priority;
            renderCallback.renderAreaCaption(renderContext, display2, i, value, f, f2.floatValue(), getFillPaint(renderContext2.rendererJob.tile.zoomLevel), getStrokePaint(renderContext2.rendererJob.tile.zoomLevel), this.position, this.maxTextWidth, polylineContainer);
        }
    }

    public void scaleTextSize(float f, byte b) {
        Paint createPaint = this.graphicFactory.createPaint(this.fill);
        createPaint.setTextSize(this.fontSize * f);
        this.fills.put(Byte.valueOf(b), createPaint);
        Paint createPaint2 = this.graphicFactory.createPaint(this.stroke);
        createPaint2.setTextSize(this.fontSize * f);
        this.strokes.put(Byte.valueOf(b), createPaint2);
        this.dyScaled.put(Byte.valueOf(b), Float.valueOf(this.f389dy * f));
    }
}
