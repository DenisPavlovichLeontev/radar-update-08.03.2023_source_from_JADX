package org.mapsforge.core.mapelements;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.GraphicUtils;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Rectangle;

public class WayTextContainer extends MapElementContainer {
    private final Point end;
    private final Paint paintBack;
    private final Paint paintFront;
    private final String text;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public WayTextContainer(Point point, Point point2, Display display, int i, String str, Paint paint, Paint paint2, double d) {
        super(point, display, i);
        Point point3 = point;
        Point point4 = point2;
        this.text = str;
        this.paintFront = paint;
        this.paintBack = paint2;
        this.end = point4;
        this.boundary = null;
        double d2 = d / 2.0d;
        this.boundaryAbsolute = new Rectangle(Math.min(point3.f381x, point4.f381x), Math.min(point3.f382y, point4.f382y), Math.max(point3.f381x, point4.f381x), Math.max(point3.f382y, point4.f382y)).enlarge(0.0d, d2, 0.0d, d2);
    }

    public void draw(Canvas canvas, Point point, Matrix matrix, Filter filter) {
        Point offset = this.f380xy.offset(-point.f381x, -point.f382y);
        Point offset2 = this.end.offset(-point.f381x, -point.f382y);
        Paint paint = this.paintBack;
        if (paint != null) {
            int color = paint.getColor();
            if (filter != Filter.NONE) {
                this.paintBack.setColor(GraphicUtils.filterColor(color, filter));
            }
            canvas.drawTextRotated(this.text, (int) offset.f381x, (int) offset.f382y, (int) offset2.f381x, (int) offset2.f382y, this.paintBack);
            if (filter != Filter.NONE) {
                this.paintBack.setColor(color);
            }
        }
        int color2 = this.paintFront.getColor();
        if (filter != Filter.NONE) {
            this.paintFront.setColor(GraphicUtils.filterColor(color2, filter));
        }
        canvas.drawTextRotated(this.text, (int) offset.f381x, (int) offset.f382y, (int) offset2.f381x, (int) offset2.f382y, this.paintFront);
        if (filter != Filter.NONE) {
            this.paintFront.setColor(color2);
        }
    }

    public String toString() {
        return super.toString() + ", text=" + this.text;
    }
}
