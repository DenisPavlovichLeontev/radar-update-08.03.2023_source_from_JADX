package org.mapsforge.map.layer;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.model.DisplayModel;

public abstract class Layer {
    private Redrawer assignedRedrawer;
    protected DisplayModel displayModel;
    private boolean visible = true;

    public abstract void draw(BoundingBox boundingBox, byte b, Canvas canvas, Point point);

    public LatLong getPosition() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onAdd() {
    }

    public void onDestroy() {
    }

    public boolean onLongPress(LatLong latLong, Point point, Point point2) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onRemove() {
    }

    public boolean onTap(LatLong latLong, Point point, Point point2) {
        return false;
    }

    public final boolean isVisible() {
        return this.visible;
    }

    public final synchronized void requestRedraw() {
        Redrawer redrawer = this.assignedRedrawer;
        if (redrawer != null) {
            redrawer.redrawLayers();
        }
    }

    public synchronized DisplayModel getDisplayModel() {
        return this.displayModel;
    }

    public synchronized void setDisplayModel(DisplayModel displayModel2) {
        this.displayModel = displayModel2;
    }

    public final void setVisible(boolean z) {
        setVisible(z, true);
    }

    public void setVisible(boolean z, boolean z2) {
        this.visible = z;
        if (z2) {
            requestRedraw();
        }
    }

    /* access modifiers changed from: package-private */
    public final synchronized void assign(Redrawer redrawer) {
        if (this.assignedRedrawer == null) {
            this.assignedRedrawer = redrawer;
            onAdd();
        } else {
            throw new IllegalStateException("layer already assigned");
        }
    }

    /* access modifiers changed from: package-private */
    public final synchronized void unassign() {
        if (this.assignedRedrawer != null) {
            this.assignedRedrawer = null;
            onRemove();
        } else {
            throw new IllegalStateException("layer is not assigned");
        }
    }
}
