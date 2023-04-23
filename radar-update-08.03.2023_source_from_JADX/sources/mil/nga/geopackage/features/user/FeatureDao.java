package mil.nga.geopackage.features.user;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.projection.ProjectionFactory;
import mil.nga.geopackage.user.UserDao;
import mil.nga.wkb.geom.GeometryType;

public class FeatureDao extends UserDao<FeatureColumn, FeatureTable, FeatureRow, FeatureCursor> {
    private final FeatureConnection featureDb;
    private final GeometryColumns geometryColumns;

    public FeatureDao(String str, GeoPackageConnection geoPackageConnection, FeatureConnection featureConnection, GeometryColumns geometryColumns2, FeatureTable featureTable) {
        super(str, geoPackageConnection, featureConnection, featureTable);
        this.featureDb = featureConnection;
        this.geometryColumns = geometryColumns2;
        if (geometryColumns2.getContents() == null) {
            StringBuilder sb = new StringBuilder();
            Class<GeometryColumns> cls = GeometryColumns.class;
            sb.append("GeometryColumns");
            sb.append(" ");
            sb.append(geometryColumns2.getId());
            sb.append(" has null ");
            Class<Contents> cls2 = Contents.class;
            sb.append("Contents");
            throw new GeoPackageException(sb.toString());
        } else if (geometryColumns2.getSrs() != null) {
            this.projection = ProjectionFactory.getProjection(geometryColumns2.getSrs());
        } else {
            StringBuilder sb2 = new StringBuilder();
            Class<GeometryColumns> cls3 = GeometryColumns.class;
            sb2.append("GeometryColumns");
            sb2.append(" ");
            sb2.append(geometryColumns2.getId());
            sb2.append(" has null ");
            Class<SpatialReferenceSystem> cls4 = SpatialReferenceSystem.class;
            sb2.append("SpatialReferenceSystem");
            throw new GeoPackageException(sb2.toString());
        }
    }

    public BoundingBox getBoundingBox() {
        Contents contents = this.geometryColumns.getContents();
        BoundingBox boundingBox = contents.getBoundingBox();
        if (boundingBox == null) {
            return boundingBox;
        }
        Projection projection = ProjectionFactory.getProjection(contents.getSrs());
        return !this.projection.equals(projection) ? projection.getTransformation(this.projection).transform(boundingBox) : boundingBox;
    }

    public FeatureRow newRow() {
        return new FeatureRow((FeatureTable) getTable());
    }

    public FeatureConnection getFeatureDb() {
        return this.featureDb;
    }

    public GeometryColumns getGeometryColumns() {
        return this.geometryColumns;
    }

    public String getGeometryColumnName() {
        return this.geometryColumns.getColumnName();
    }

    public GeometryType getGeometryType() {
        return this.geometryColumns.getGeometryType();
    }
}
