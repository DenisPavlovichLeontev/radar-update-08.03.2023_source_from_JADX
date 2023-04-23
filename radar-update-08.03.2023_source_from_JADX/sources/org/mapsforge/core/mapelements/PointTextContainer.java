package org.mapsforge.core.mapelements;

import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.core.model.Point;

public abstract class PointTextContainer extends MapElementContainer {
    public final boolean isVisible;
    public final int maxTextWidth;
    public final Paint paintBack;
    public final Paint paintFront;
    public final Position position;
    public final SymbolContainer symbolContainer;
    public final String text;
    public final int textHeight;
    public final int textWidth;

    protected PointTextContainer(Point point, Display display, int i, String str, Paint paint, Paint paint2, SymbolContainer symbolContainer2, Position position2, int i2) {
        super(point, display, i);
        this.maxTextWidth = i2;
        this.text = str;
        this.symbolContainer = symbolContainer2;
        this.paintFront = paint;
        this.paintBack = paint2;
        this.position = position2;
        if (paint2 != null) {
            this.textWidth = paint2.getTextWidth(str);
            this.textHeight = paint2.getTextHeight(str);
        } else {
            this.textWidth = paint.getTextWidth(str);
            this.textHeight = paint.getTextHeight(str);
        }
        this.isVisible = !paint.isTransparent() || (paint2 != null && !paint2.isTransparent());
    }

    public boolean clashesWith(MapElementContainer mapElementContainer) {
        if (super.clashesWith(mapElementContainer)) {
            return true;
        }
        if (!(mapElementContainer instanceof PointTextContainer)) {
            return false;
        }
        PointTextContainer pointTextContainer = (PointTextContainer) mapElementContainer;
        if (!this.text.equals(pointTextContainer.text) || this.f380xy.distance(pointTextContainer.f380xy) >= 200.0d) {
            return false;
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (super.equals(obj) && (obj instanceof PointTextContainer) && this.text.equals(((PointTextContainer) obj).text)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + this.text.hashCode();
    }

    public String toString() {
        return super.toString() + ", text=" + this.text;
    }
}
