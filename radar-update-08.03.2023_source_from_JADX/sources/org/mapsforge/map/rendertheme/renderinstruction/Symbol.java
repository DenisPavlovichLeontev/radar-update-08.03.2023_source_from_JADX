package org.mapsforge.map.rendertheme.renderinstruction;

import java.io.IOException;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.map.datastore.PointOfInterest;
import org.mapsforge.map.layer.renderer.PolylineContainer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.RenderContext;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xmlpull.p018v1.XmlPullParser;
import org.xmlpull.p018v1.XmlPullParserException;

public class Symbol extends RenderInstruction {
    private Bitmap bitmap;
    private boolean bitmapInvalid;
    private Display display = Display.IFSPACE;

    /* renamed from: id */
    private String f397id;
    private int priority;
    private final String relativePathPrefix;
    private String src;

    public void scaleStrokeWidth(float f, byte b) {
    }

    public void scaleTextSize(float f, byte b) {
    }

    public Symbol(GraphicFactory graphicFactory, DisplayModel displayModel, String str, XmlPullParser xmlPullParser, String str2) throws IOException, XmlPullParserException {
        super(graphicFactory, displayModel);
        this.relativePathPrefix = str2;
        extractValues(str, xmlPullParser);
    }

    public void destroy() {
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null) {
            bitmap2.decrementRefCount();
        }
    }

    private void extractValues(String str, XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            String attributeValue = xmlPullParser.getAttributeValue(i);
            if ("src".equals(attributeName)) {
                this.src = attributeValue;
            } else if ("cat".equals(attributeName)) {
                this.category = attributeValue;
            } else if ("display".equals(attributeName)) {
                this.display = Display.fromString(attributeValue);
            } else if ("id".equals(attributeName)) {
                this.f397id = attributeValue;
            } else if ("priority".equals(attributeName)) {
                this.priority = Integer.parseInt(attributeValue);
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

    public Bitmap getBitmap() {
        if (this.bitmap == null && !this.bitmapInvalid) {
            try {
                this.bitmap = createBitmap(this.relativePathPrefix, this.src);
            } catch (IOException unused) {
                this.bitmapInvalid = true;
            }
        }
        return this.bitmap;
    }

    public String getId() {
        return this.f397id;
    }

    public void renderNode(RenderCallback renderCallback, RenderContext renderContext, PointOfInterest pointOfInterest) {
        if (Display.NEVER != this.display && getBitmap() != null) {
            renderCallback.renderPointOfInterestSymbol(renderContext, this.display, this.priority, this.bitmap, pointOfInterest);
        }
    }

    public void renderWay(RenderCallback renderCallback, RenderContext renderContext, PolylineContainer polylineContainer) {
        if (Display.NEVER != this.display && getBitmap() != null) {
            renderCallback.renderAreaSymbol(renderContext, this.display, this.priority, this.bitmap, polylineContainer);
        }
    }
}
