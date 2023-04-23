package mil.nga.geopackage.extension.coverage;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;

public class CoverageDataRequest {
    private BoundingBox boundingBox;
    private boolean point;
    private BoundingBox projectedBoundingBox;

    public CoverageDataRequest(BoundingBox boundingBox2) {
        this.boundingBox = boundingBox2;
    }

    public CoverageDataRequest(double d, double d2) {
        this(new BoundingBox(d2, d, d2, d));
        this.point = true;
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public boolean isPoint() {
        return this.point;
    }

    public BoundingBox getProjectedBoundingBox() {
        return this.projectedBoundingBox;
    }

    public void setProjectedBoundingBox(BoundingBox boundingBox2) {
        this.projectedBoundingBox = boundingBox2;
    }

    public BoundingBox overlap(BoundingBox boundingBox2) {
        if (this.point) {
            return this.projectedBoundingBox;
        }
        return TileBoundingBoxUtils.overlap(this.projectedBoundingBox, boundingBox2);
    }
}
