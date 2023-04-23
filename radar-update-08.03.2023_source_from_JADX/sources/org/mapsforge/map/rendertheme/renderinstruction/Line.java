package org.mapsforge.map.rendertheme.renderinstruction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Cap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Join;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.map.datastore.PointOfInterest;
import org.mapsforge.map.layer.renderer.PolylineContainer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.RenderContext;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;
import org.xmlpull.p018v1.XmlPullParser;
import org.xmlpull.p018v1.XmlPullParserException;

public class Line extends RenderInstruction {
    private static final Pattern SPLIT_PATTERN = Pattern.compile(",");
    private boolean bitmapCreated;

    /* renamed from: dy */
    private float f390dy;
    private final Map<Byte, Float> dyScaled;
    private final int level;
    private final String relativePathPrefix;
    private RenderInstruction.Scale scale = RenderInstruction.Scale.STROKE;
    private Bitmap shaderBitmap;
    private String src;
    private final Paint stroke;
    private float[] strokeDasharray;
    private float strokeWidth;
    private final Map<Byte, Paint> strokes;

    public void destroy() {
    }

    public void renderNode(RenderCallback renderCallback, RenderContext renderContext, PointOfInterest pointOfInterest) {
    }

    public void scaleTextSize(float f, byte b) {
    }

    public Line(GraphicFactory graphicFactory, DisplayModel displayModel, String str, XmlPullParser xmlPullParser, int i, String str2) throws IOException, XmlPullParserException {
        super(graphicFactory, displayModel);
        this.level = i;
        this.relativePathPrefix = str2;
        Paint createPaint = graphicFactory.createPaint();
        this.stroke = createPaint;
        createPaint.setColor(Color.BLACK);
        createPaint.setStyle(Style.STROKE);
        createPaint.setStrokeCap(Cap.ROUND);
        createPaint.setStrokeJoin(Join.ROUND);
        this.strokes = new HashMap();
        this.dyScaled = new HashMap();
        extractValues(graphicFactory, displayModel, str, xmlPullParser);
    }

    private void extractValues(GraphicFactory graphicFactory, DisplayModel displayModel, String str, XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        float[] fArr;
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            String attributeValue = xmlPullParser.getAttributeValue(i);
            if ("src".equals(attributeName)) {
                this.src = attributeValue;
            } else if ("cat".equals(attributeName)) {
                this.category = attributeValue;
            } else if ("dy".equals(attributeName)) {
                this.f390dy = Float.parseFloat(attributeValue) * displayModel.getScaleFactor();
            } else if ("scale".equals(attributeName)) {
                this.scale = scaleFromValue(attributeValue);
            } else if ("stroke".equals(attributeName)) {
                this.stroke.setColor(XmlUtils.getColor(graphicFactory, attributeValue, displayModel.getThemeCallback(), this));
            } else if ("stroke-dasharray".equals(attributeName)) {
                this.strokeDasharray = parseFloatArray(attributeName, attributeValue);
                int i2 = 0;
                while (true) {
                    fArr = this.strokeDasharray;
                    if (i2 >= fArr.length) {
                        break;
                    }
                    fArr[i2] = fArr[i2] * displayModel.getScaleFactor();
                    i2++;
                }
                this.stroke.setDashPathEffect(fArr);
            } else if ("stroke-linecap".equals(attributeName)) {
                this.stroke.setStrokeCap(Cap.fromString(attributeValue));
            } else if ("stroke-linejoin".equals(attributeName)) {
                this.stroke.setStrokeJoin(Join.fromString(attributeValue));
            } else if ("stroke-width".equals(attributeName)) {
                this.strokeWidth = XmlUtils.parseNonNegativeFloat(attributeName, attributeValue) * displayModel.getScaleFactor();
            } else if ("symbol-height".equals(attributeName)) {
                this.height = ((float) XmlUtils.parseNonNegativeInteger(attributeName, attributeValue)) * displayModel.getScaleFactor();
            } else if ("symbol-percent".equals(attributeName)) {
                this.percent = XmlUtils.parseNonNegativeInteger(attributeName, attributeValue);
            } else if ("symbol-scaling".equals(attributeName)) {
                continue;
            } else if ("symbol-width".equals(attributeName)) {
                this.width = ((float) XmlUtils.parseNonNegativeInteger(attributeName, attributeValue)) * displayModel.getScaleFactor();
            } else {
                throw XmlUtils.createXmlPullParserException(str, attributeName, attributeValue, i);
            }
        }
    }

    private Paint getStrokePaint(byte b) {
        Paint paint = this.strokes.get(Byte.valueOf(b));
        return paint == null ? this.stroke : paint;
    }

    private static float[] parseFloatArray(String str, String str2) throws XmlPullParserException {
        String[] split = SPLIT_PATTERN.split(str2);
        float[] fArr = new float[split.length];
        for (int i = 0; i < split.length; i++) {
            fArr[i] = XmlUtils.parseNonNegativeFloat(str, split[i]);
        }
        return fArr;
    }

    public synchronized void renderWay(RenderCallback renderCallback, RenderContext renderContext, PolylineContainer polylineContainer) {
        if (!this.bitmapCreated) {
            try {
                this.shaderBitmap = createBitmap(this.relativePathPrefix, this.src);
            } catch (IOException unused) {
            }
            this.bitmapCreated = true;
        }
        Paint strokePaint = getStrokePaint(renderContext.rendererJob.tile.zoomLevel);
        Bitmap bitmap = this.shaderBitmap;
        if (bitmap != null) {
            strokePaint.setBitmapShader(bitmap);
            strokePaint.setBitmapShaderShift(polylineContainer.getUpperLeft().getOrigin());
        }
        Float f = this.dyScaled.get(Byte.valueOf(renderContext.rendererJob.tile.zoomLevel));
        if (f == null) {
            f = Float.valueOf(this.f390dy);
        }
        renderCallback.renderWay(renderContext, strokePaint, f.floatValue(), this.level, polylineContainer);
    }

    public void scaleStrokeWidth(float f, byte b) {
        if (this.scale == RenderInstruction.Scale.NONE) {
            f = 1.0f;
        }
        if (this.stroke != null) {
            Paint createPaint = this.graphicFactory.createPaint(this.stroke);
            createPaint.setStrokeWidth(this.strokeWidth * f);
            if (this.scale == RenderInstruction.Scale.ALL) {
                float[] fArr = new float[this.strokeDasharray.length];
                int i = 0;
                while (true) {
                    float[] fArr2 = this.strokeDasharray;
                    if (i >= fArr2.length) {
                        break;
                    }
                    fArr[i] = fArr2[i] * f;
                    i++;
                }
                createPaint.setDashPathEffect(fArr);
            }
            this.strokes.put(Byte.valueOf(b), createPaint);
        }
        this.dyScaled.put(Byte.valueOf(b), Float.valueOf(this.f390dy * f));
    }
}
