package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.HashMap;
import java.util.Map;
import org.mapsforge.core.graphics.Align;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.FontFamily;
import org.mapsforge.core.graphics.FontStyle;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.map.datastore.PointOfInterest;
import org.mapsforge.map.layer.renderer.PolylineContainer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.RenderContext;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;
import org.osgeo.proj4j.parser.Proj4Keyword;
import org.xmlpull.p018v1.XmlPullParser;
import org.xmlpull.p018v1.XmlPullParserException;

public class PathText extends RenderInstruction {
    private static final float REPEAT_GAP_DEFAULT = 50.0f;
    private static final float REPEAT_START_DEFAULT = 10.0f;
    private Display display;

    /* renamed from: dy */
    private float f392dy;
    private final Map<Byte, Float> dyScaled;
    private final Paint fill;
    private final Map<Byte, Paint> fills;
    private float fontSize;
    private int priority;
    private boolean repeat;
    private float repeatGap;
    private float repeatStart;
    private boolean rotate;
    private RenderInstruction.Scale scale = RenderInstruction.Scale.STROKE;
    private final Paint stroke;
    private final Map<Byte, Paint> strokes;
    private TextKey textKey;

    public void destroy() {
    }

    public void renderNode(RenderCallback renderCallback, RenderContext renderContext, PointOfInterest pointOfInterest) {
    }

    public PathText(GraphicFactory graphicFactory, DisplayModel displayModel, String str, XmlPullParser xmlPullParser) throws XmlPullParserException {
        super(graphicFactory, displayModel);
        Paint createPaint = graphicFactory.createPaint();
        this.fill = createPaint;
        createPaint.setColor(Color.BLACK);
        createPaint.setStyle(Style.FILL);
        createPaint.setTextAlign(Align.CENTER);
        this.fills = new HashMap();
        this.rotate = true;
        this.repeat = true;
        Paint createPaint2 = graphicFactory.createPaint();
        this.stroke = createPaint2;
        createPaint2.setColor(Color.BLACK);
        createPaint2.setStyle(Style.STROKE);
        createPaint2.setTextAlign(Align.CENTER);
        this.strokes = new HashMap();
        this.dyScaled = new HashMap();
        this.display = Display.IFSPACE;
        extractValues(graphicFactory, displayModel, str, xmlPullParser);
    }

    private void extractValues(GraphicFactory graphicFactory, DisplayModel displayModel, String str, XmlPullParser xmlPullParser) throws XmlPullParserException {
        this.repeatGap = displayModel.getScaleFactor() * 50.0f;
        this.repeatStart = displayModel.getScaleFactor() * REPEAT_START_DEFAULT;
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
                this.f392dy = Float.parseFloat(attributeValue) * displayModel.getScaleFactor();
            } else if ("fill".equals(attributeName)) {
                this.fill.setColor(XmlUtils.getColor(graphicFactory, attributeValue, displayModel.getThemeCallback(), this));
            } else if ("font-family".equals(attributeName)) {
                fontFamily = FontFamily.fromString(attributeValue);
            } else if ("font-size".equals(attributeName)) {
                this.fontSize = XmlUtils.parseNonNegativeFloat(attributeName, attributeValue) * displayModel.getScaleFactor();
            } else if ("font-style".equals(attributeName)) {
                fontStyle = FontStyle.fromString(attributeValue);
            } else if ("repeat".equals(attributeName)) {
                this.repeat = Boolean.parseBoolean(attributeValue);
            } else if ("repeat-gap".equals(attributeName)) {
                this.repeatGap = Float.parseFloat(attributeValue) * displayModel.getScaleFactor();
            } else if ("repeat-start".equals(attributeName)) {
                this.repeatStart = Float.parseFloat(attributeValue) * displayModel.getScaleFactor();
            } else if ("rotate".equals(attributeName)) {
                this.rotate = Boolean.parseBoolean(attributeValue);
            } else if ("priority".equals(attributeName)) {
                this.priority = Integer.parseInt(attributeValue);
            } else if ("scale".equals(attributeName)) {
                this.scale = scaleFromValue(attributeValue);
            } else if ("stroke".equals(attributeName)) {
                this.stroke.setColor(XmlUtils.getColor(graphicFactory, attributeValue, displayModel.getThemeCallback(), this));
            } else if ("stroke-width".equals(attributeName)) {
                this.stroke.setStrokeWidth(XmlUtils.parseNonNegativeFloat(attributeName, attributeValue) * displayModel.getScaleFactor());
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

    public void renderWay(RenderCallback renderCallback, RenderContext renderContext, PolylineContainer polylineContainer) {
        String value;
        RenderContext renderContext2 = renderContext;
        if (Display.NEVER != this.display && (value = this.textKey.getValue(polylineContainer.getTags())) != null) {
            Float f = this.dyScaled.get(Byte.valueOf(renderContext2.rendererJob.tile.zoomLevel));
            if (f == null) {
                f = Float.valueOf(this.f392dy);
            }
            renderCallback.renderWayText(renderContext, this.display, this.priority, value, f.floatValue(), getFillPaint(renderContext2.rendererJob.tile.zoomLevel), getStrokePaint(renderContext2.rendererJob.tile.zoomLevel), this.repeat, this.repeatGap, this.repeatStart, this.rotate, polylineContainer);
        }
    }

    public void scaleStrokeWidth(float f, byte b) {
        if (this.scale == RenderInstruction.Scale.NONE) {
            f = 1.0f;
        }
        this.dyScaled.put(Byte.valueOf(b), Float.valueOf(this.f392dy * f));
    }

    public void scaleTextSize(float f, byte b) {
        Paint createPaint = this.graphicFactory.createPaint(this.fill);
        createPaint.setTextSize(this.fontSize * f);
        this.fills.put(Byte.valueOf(b), createPaint);
        Paint createPaint2 = this.graphicFactory.createPaint(this.stroke);
        createPaint2.setTextSize(this.fontSize * f);
        this.strokes.put(Byte.valueOf(b), createPaint2);
    }
}
