package org.mapsforge.core.mapelements;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Rectangle;

public class SymbolContainer extends MapElementContainer {
    final boolean alignCenter;
    public Bitmap symbol;
    public final float theta;

    public SymbolContainer(Point point, Display display, int i, Bitmap bitmap) {
        this(point, display, i, bitmap, 0.0f, true);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SymbolContainer(Point point, Display display, int i, Bitmap bitmap, float f, boolean z) {
        super(point, display, i);
        boolean z2 = z;
        this.symbol = bitmap;
        this.theta = f;
        this.alignCenter = z2;
        if (z2) {
            double width = ((double) bitmap.getWidth()) / 2.0d;
            double height = ((double) this.symbol.getHeight()) / 2.0d;
            this.boundary = new Rectangle(-width, -height, width, height);
        } else {
            this.boundary = new Rectangle(0.0d, 0.0d, (double) bitmap.getWidth(), (double) this.symbol.getHeight());
        }
        this.symbol.incrementRefCount();
    }

    public boolean equals(Object obj) {
        if (super.equals(obj) && (obj instanceof SymbolContainer) && this.symbol == ((SymbolContainer) obj).symbol) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + this.symbol.hashCode();
    }

    public void draw(Canvas canvas, Point point, Matrix matrix, Filter filter) {
        matrix.reset();
        matrix.translate((float) ((int) ((this.f380xy.f381x - point.f381x) + this.boundary.left)), (float) ((int) ((this.f380xy.f382y - point.f382y) + this.boundary.top)));
        float f = this.theta;
        if (f == 0.0f || !this.alignCenter) {
            matrix.rotate(f);
        } else {
            matrix.rotate(f, (float) (-this.boundary.left), (float) (-this.boundary.top));
        }
        canvas.drawBitmap(this.symbol, matrix, filter);
    }
}
