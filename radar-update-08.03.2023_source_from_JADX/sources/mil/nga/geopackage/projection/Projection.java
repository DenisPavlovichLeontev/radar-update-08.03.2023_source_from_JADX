package mil.nga.geopackage.projection;

import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.proj.LongLatProjection;
import org.osgeo.proj4j.units.Unit;
import org.osgeo.proj4j.units.Units;

public class Projection {
    private final String authority;
    private final String code;
    private final CoordinateReferenceSystem crs;
    private Unit unit;

    public Projection(String str, long j, CoordinateReferenceSystem coordinateReferenceSystem) {
        this(str, String.valueOf(j), coordinateReferenceSystem);
    }

    public Projection(String str, String str2, CoordinateReferenceSystem coordinateReferenceSystem) {
        if (str == null || str2 == null || coordinateReferenceSystem == null) {
            throw new IllegalArgumentException("All projection arguments are required. authority: " + str + ", code: " + str2 + ", crs: " + coordinateReferenceSystem);
        }
        this.authority = str;
        this.code = str2;
        this.crs = coordinateReferenceSystem;
    }

    public String getAuthority() {
        return this.authority;
    }

    public String getCode() {
        return this.code;
    }

    public CoordinateReferenceSystem getCrs() {
        return this.crs;
    }

    public ProjectionTransform getTransformation(long j) {
        return getTransformation(ProjectionConstants.AUTHORITY_EPSG, j);
    }

    public ProjectionTransform getTransformation(String str, long j) {
        return getTransformation(ProjectionFactory.getProjection(str, j));
    }

    public ProjectionTransform getTransformation(SpatialReferenceSystem spatialReferenceSystem) {
        return getTransformation(ProjectionFactory.getProjection(spatialReferenceSystem));
    }

    public ProjectionTransform getTransformation(Projection projection) {
        return new ProjectionTransform(this, projection);
    }

    public double toMeters(double d) {
        return d / this.crs.getProjection().getFromMetres();
    }

    public Unit getUnit() {
        int indexOf;
        if (this.unit == null) {
            if (this.crs.getProjection() instanceof LongLatProjection) {
                this.unit = Units.DEGREES;
            } else {
                String str = null;
                String[] parameters = this.crs.getParameters();
                int length = parameters.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    String str2 = parameters[i];
                    if (str2.startsWith("+units") && (indexOf = str2.indexOf(61)) != -1) {
                        str = str2.substring(indexOf + 1);
                        break;
                    }
                    i++;
                }
                if (str != null) {
                    this.unit = Units.findUnits(str);
                } else {
                    this.unit = Units.METRES;
                }
            }
        }
        return this.unit;
    }

    public boolean equals(String str, long j) {
        return equals(str, String.valueOf(j));
    }

    public boolean equals(String str, String str2) {
        return this.authority.equals(str) && this.code.equals(str2);
    }

    public int hashCode() {
        return ((this.authority.hashCode() + 31) * 31) + this.code.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Projection projection = (Projection) obj;
        return equals(projection.authority, projection.code);
    }
}
