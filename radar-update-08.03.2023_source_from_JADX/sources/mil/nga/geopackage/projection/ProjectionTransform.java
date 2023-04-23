package mil.nga.geopackage.projection;

import java.util.ArrayList;
import java.util.List;
import mil.nga.geopackage.BoundingBox;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.Point;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;

public class ProjectionTransform {
    private static CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
    private final Projection fromProjection;
    private final Projection toProjection;
    private final CoordinateTransform transform;

    ProjectionTransform(Projection projection, Projection projection2) {
        this.fromProjection = projection;
        this.toProjection = projection2;
        this.transform = ctFactory.createTransform(projection.getCrs(), projection2.getCrs());
    }

    public ProjCoordinate transform(ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = new ProjCoordinate();
        this.transform.transform(projCoordinate, projCoordinate2);
        return projCoordinate2;
    }

    public Point transform(Point point) {
        return new GeometryProjectionTransform(this).transform(point);
    }

    public List<Point> transform(List<Point> list) {
        ArrayList arrayList = new ArrayList();
        GeometryProjectionTransform geometryProjectionTransform = new GeometryProjectionTransform(this);
        for (Point transform2 : list) {
            arrayList.add(geometryProjectionTransform.transform(transform2));
        }
        return arrayList;
    }

    public Geometry transform(Geometry geometry) {
        return new GeometryProjectionTransform(this).transform(geometry);
    }

    public BoundingBox transform(BoundingBox boundingBox) {
        ProjCoordinate projCoordinate = new ProjCoordinate(boundingBox.getMinLongitude(), boundingBox.getMinLatitude());
        ProjCoordinate projCoordinate2 = new ProjCoordinate(boundingBox.getMaxLongitude(), boundingBox.getMinLatitude());
        ProjCoordinate projCoordinate3 = new ProjCoordinate(boundingBox.getMaxLongitude(), boundingBox.getMaxLatitude());
        ProjCoordinate projCoordinate4 = new ProjCoordinate(boundingBox.getMinLongitude(), boundingBox.getMaxLatitude());
        ProjCoordinate transform2 = transform(projCoordinate);
        ProjCoordinate transform3 = transform(projCoordinate2);
        ProjCoordinate transform4 = transform(projCoordinate3);
        ProjCoordinate transform5 = transform(projCoordinate4);
        return new BoundingBox(Math.min(transform2.f409x, transform5.f409x), Math.min(transform2.f410y, transform3.f410y), Math.max(transform3.f409x, transform4.f409x), Math.max(transform5.f410y, transform4.f410y));
    }

    public double[] transform(double d, double d2) {
        ProjCoordinate transform2 = transform(new ProjCoordinate(d, d2));
        return new double[]{transform2.f409x, transform2.f410y};
    }

    public Projection getFromProjection() {
        return this.fromProjection;
    }

    public Projection getToProjection() {
        return this.toProjection;
    }

    public CoordinateTransform getTransform() {
        return this.transform;
    }
}
