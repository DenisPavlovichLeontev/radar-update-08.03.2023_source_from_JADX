package org.mapsforge.map.rendertheme.renderinstruction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.map.datastore.PointOfInterest;
import org.mapsforge.map.layer.renderer.PolylineContainer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.RenderContext;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;
import org.xmlpull.p018v1.XmlPullParser;
import org.xmlpull.p018v1.XmlPullParserException;

public class LineSymbol extends RenderInstruction {
    private static final float REPEAT_GAP_DEFAULT = 200.0f;
    private static final float REPEAT_START_DEFAULT = 30.0f;
    private boolean alignCenter;
    private Bitmap bitmap;
    private boolean bitmapInvalid;
    private Display display = Display.IFSPACE;

    /* renamed from: dy */
    private float f391dy;
    private final Map<Byte, Float> dyScaled;
    private int priority;
    private final String relativePathPrefix;
    private boolean repeat;
    private float repeatGap;
    private float repeatStart;
    private boolean rotate = true;
    private RenderInstruction.Scale scale = RenderInstruction.Scale.STROKE;
    private String src;

    public void renderNode(RenderCallback renderCallback, RenderContext renderContext, PointOfInterest pointOfInterest) {
    }

    public void scaleTextSize(float f, byte b) {
    }

    public LineSymbol(GraphicFactory graphicFactory, DisplayModel displayModel, String str, XmlPullParser xmlPullParser, String str2) throws IOException, XmlPullParserException {
        super(graphicFactory, displayModel);
        this.relativePathPrefix = str2;
        this.dyScaled = new HashMap();
        extractValues(str, xmlPullParser);
    }

    public void destroy() {
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null) {
            bitmap2.decrementRefCount();
        }
    }

    private void extractValues(String str, XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        this.repeatGap = this.displayModel.getScaleFactor() * 200.0f;
        this.repeatStart = this.displayModel.getScaleFactor() * REPEAT_START_DEFAULT;
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            String attributeValue = xmlPullParser.getAttributeValue(i);
            if ("src".equals(attributeName)) {
                this.src = attributeValue;
            } else if ("align-center".equals(attributeName)) {
                this.alignCenter = Boolean.parseBoolean(attributeValue);
            } else if ("cat".equals(attributeName)) {
                this.category = attributeValue;
            } else if ("display".equals(attributeName)) {
                this.display = Display.fromString(attributeValue);
            } else if ("dy".equals(attributeName)) {
                this.f391dy = Float.parseFloat(attributeValue) * this.displayModel.getScaleFactor();
            } else if ("priority".equals(attributeName)) {
                this.priority = Integer.parseInt(attributeValue);
            } else if ("repeat".equals(attributeName)) {
                this.repeat = Boolean.parseBoolean(attributeValue);
            } else if ("repeat-gap".equals(attributeName)) {
                this.repeatGap = Float.parseFloat(attributeValue) * this.displayModel.getScaleFactor();
            } else if ("repeat-start".equals(attributeName)) {
                this.repeatStart = Float.parseFloat(attributeValue) * this.displayModel.getScaleFactor();
            } else if ("rotate".equals(attributeName)) {
                this.rotate = Boolean.parseBoolean(attributeValue);
            } else if ("scale".equals(attributeName)) {
                this.scale = scaleFromValue(attributeValue);
            } else if ("symbol-height".equals(attributeName)) {
                this.height = ((float) XmlUtils.parseNonNegativeInteger(attributeName, attributeValue)) * this.displayModel.getScaleFactor();
            } else if ("symbol-percent".equals(attributeName)) {
                this.percent = XmlUtils.parseNonNegativeInteger(attributeName, attributeValue);
            } else if ("symbol-scaling".equals(attributeName)) {
                continue;
            } else if ("symbol-width".equals(attributeName)) {
                this.width = ((float) XmlUtils.parseNonNegativeInteger(attributeName, attributeValue)) * this.displayModel.getScaleFactor();
            } else {
                throw XmlUtils.createXmlPullParserException(str, attributeName, attributeValue, i);
            }
        }
    }

    public void renderWay(RenderCallback renderCallback, RenderContext renderContext, PolylineContainer polylineContainer) {
        if (Display.NEVER != this.display) {
            if (this.bitmap == null && !this.bitmapInvalid) {
                try {
                    this.bitmap = createBitmap(this.relativePathPrefix, this.src);
                } catch (IOException unused) {
                    this.bitmapInvalid = true;
                }
            }
            Float f = this.dyScaled.get(Byte.valueOf(renderContext.rendererJob.tile.zoomLevel));
            if (f == null) {
                f = Float.valueOf(this.f391dy);
            }
            Bitmap bitmap2 = this.bitmap;
            if (bitmap2 != null) {
                renderCallback.renderWaySymbol(renderContext, this.display, this.priority, bitmap2, f.floatValue(), this.alignCenter, this.repeat, this.repeatGap, this.repeatStart, this.rotate, polylineContainer);
            }
        }
    }

    public void scaleStrokeWidth(float f, byte b) {
        if (this.scale == RenderInstruction.Scale.NONE) {
            f = 1.0f;
        }
        this.dyScaled.put(Byte.valueOf(b), Float.valueOf(this.f391dy * f));
    }
}
