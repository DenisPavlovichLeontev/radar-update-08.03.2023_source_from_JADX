package org.osgeo.proj4j.proj;

import java.text.FieldPosition;
import org.osgeo.proj4j.InvalidValueException;
import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.datum.Ellipsoid;
import org.osgeo.proj4j.units.AngleFormat;
import org.osgeo.proj4j.units.Unit;
import org.osgeo.proj4j.units.Units;
import org.osgeo.proj4j.util.ProjectionMath;

public abstract class Projection implements Cloneable {
    protected static final double DTR = 0.017453292519943295d;
    protected static final double EPS10 = 1.0E-10d;
    protected static final double RTD = 57.29577951308232d;

    /* renamed from: a */
    protected double f537a = 0.0d;
    protected double alpha = Double.NaN;

    /* renamed from: e */
    protected double f538e = 0.0d;
    protected Ellipsoid ellipsoid;

    /* renamed from: es */
    protected double f539es = 0.0d;
    protected double falseEasting = 0.0d;
    protected double falseNorthing = 0.0d;
    protected double fromMetres = 1.0d;
    protected boolean geocentric;
    protected boolean isSouth = false;
    protected double lonc = Double.NaN;
    protected double maxLatitude = 1.5707963267948966d;
    protected double maxLongitude = 3.141592653589793d;
    protected double minLatitude = -1.5707963267948966d;
    protected double minLongitude = -3.141592653589793d;
    protected String name = null;
    protected double one_es = 0.0d;
    protected double projectionLatitude = 0.0d;
    protected double projectionLatitude1 = 0.0d;
    protected double projectionLatitude2 = 0.0d;
    protected double projectionLongitude = 0.0d;
    protected double rone_es = 0.0d;
    protected double scaleFactor = 1.0d;
    protected boolean spherical;
    private double totalFalseEasting = 0.0d;
    private double totalFalseNorthing = 0.0d;
    protected double totalScale = 0.0d;
    protected double trueScaleLatitude = 0.0d;
    protected Unit unit = null;

    public int getEPSGCode() {
        return 0;
    }

    public boolean hasInverse() {
        return false;
    }

    public boolean isConformal() {
        return false;
    }

    public boolean isEqualArea() {
        return false;
    }

    public boolean isRectilinear() {
        return false;
    }

    public String toString() {
        return "None";
    }

    protected Projection() {
        setEllipsoid(Ellipsoid.SPHERE);
    }

    public Object clone() {
        try {
            return (Projection) super.clone();
        } catch (CloneNotSupportedException unused) {
            throw new InternalError();
        }
    }

    public ProjCoordinate project(ProjCoordinate projCoordinate, ProjCoordinate projCoordinate2) {
        double d = projCoordinate.f409x * 0.017453292519943295d;
        double d2 = this.projectionLongitude;
        if (d2 != 0.0d) {
            d = ProjectionMath.normalizeLongitude(d - d2);
        }
        return projectRadians(d, projCoordinate.f410y * 0.017453292519943295d, projCoordinate2);
    }

    public ProjCoordinate projectRadians(ProjCoordinate projCoordinate, ProjCoordinate projCoordinate2) {
        double d = projCoordinate.f409x;
        double d2 = this.projectionLongitude;
        if (d2 != 0.0d) {
            d = ProjectionMath.normalizeLongitude(d - d2);
        }
        return projectRadians(d, projCoordinate.f410y, projCoordinate2);
    }

    private ProjCoordinate projectRadians(double d, double d2, ProjCoordinate projCoordinate) {
        project(d, d2, projCoordinate);
        if (this.unit == Units.DEGREES) {
            projCoordinate.f409x *= 57.29577951308232d;
            projCoordinate.f410y *= 57.29577951308232d;
        } else {
            projCoordinate.f409x = (this.totalScale * projCoordinate.f409x) + this.totalFalseEasting;
            projCoordinate.f410y = (this.totalScale * projCoordinate.f410y) + this.totalFalseNorthing;
        }
        return projCoordinate;
    }

    /* access modifiers changed from: protected */
    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = d;
        projCoordinate.f410y = d2;
        return projCoordinate;
    }

    public ProjCoordinate inverseProject(ProjCoordinate projCoordinate, ProjCoordinate projCoordinate2) {
        inverseProjectRadians(projCoordinate, projCoordinate2);
        projCoordinate2.f409x *= 57.29577951308232d;
        projCoordinate2.f410y *= 57.29577951308232d;
        return projCoordinate2;
    }

    public ProjCoordinate inverseProjectRadians(ProjCoordinate projCoordinate, ProjCoordinate projCoordinate2) {
        double d;
        double d2;
        if (this.unit == Units.DEGREES) {
            d2 = projCoordinate.f409x * 0.017453292519943295d;
            d = projCoordinate.f410y * 0.017453292519943295d;
        } else {
            d2 = (projCoordinate.f409x - this.totalFalseEasting) / this.totalScale;
            d = (projCoordinate.f410y - this.totalFalseNorthing) / this.totalScale;
        }
        projectInverse(d2, d, projCoordinate2);
        if (projCoordinate2.f409x < -3.141592653589793d) {
            projCoordinate2.f409x = -3.141592653589793d;
        } else if (projCoordinate2.f409x > 3.141592653589793d) {
            projCoordinate2.f409x = 3.141592653589793d;
        }
        if (this.projectionLongitude != 0.0d) {
            projCoordinate2.f409x = ProjectionMath.normalizeLongitude(projCoordinate2.f409x + this.projectionLongitude);
        }
        return projCoordinate2;
    }

    /* access modifiers changed from: protected */
    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = d;
        projCoordinate.f410y = d2;
        return projCoordinate;
    }

    public boolean parallelsAreParallel() {
        return isRectilinear();
    }

    public boolean inside(double d, double d2) {
        double normalizeLongitude = (double) normalizeLongitude((float) ((d * 0.017453292519943295d) - this.projectionLongitude));
        return this.minLongitude <= normalizeLongitude && normalizeLongitude <= this.maxLongitude && this.minLatitude <= d2 && d2 <= this.maxLatitude;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getName() {
        String str = this.name;
        if (str != null) {
            return str;
        }
        return toString();
    }

    public String getPROJ4Description() {
        AngleFormat angleFormat = new AngleFormat(AngleFormat.ddmmssPattern, false);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("+proj=" + getName() + " +a=" + this.f537a);
        if (this.f539es != 0.0d) {
            stringBuffer.append(" +es=" + this.f539es);
        }
        stringBuffer.append(" +lon_0=");
        angleFormat.format(this.projectionLongitude, stringBuffer, (FieldPosition) null);
        stringBuffer.append(" +lat_0=");
        angleFormat.format(this.projectionLatitude, stringBuffer, (FieldPosition) null);
        if (this.falseEasting != 1.0d) {
            stringBuffer.append(" +x_0=" + this.falseEasting);
        }
        if (this.falseNorthing != 1.0d) {
            stringBuffer.append(" +y_0=" + this.falseNorthing);
        }
        if (this.scaleFactor != 1.0d) {
            stringBuffer.append(" +k=" + this.scaleFactor);
        }
        if (this.fromMetres != 1.0d) {
            stringBuffer.append(" +fr_meters=" + this.fromMetres);
        }
        return stringBuffer.toString();
    }

    public void setMinLatitude(double d) {
        this.minLatitude = d;
    }

    public double getMinLatitude() {
        return this.minLatitude;
    }

    public void setMaxLatitude(double d) {
        this.maxLatitude = d;
    }

    public double getMaxLatitude() {
        return this.maxLatitude;
    }

    public double getMaxLatitudeDegrees() {
        return this.maxLatitude * 57.29577951308232d;
    }

    public double getMinLatitudeDegrees() {
        return this.minLatitude * 57.29577951308232d;
    }

    public void setMinLongitude(double d) {
        this.minLongitude = d;
    }

    public double getMinLongitude() {
        return this.minLongitude;
    }

    public void setMinLongitudeDegrees(double d) {
        this.minLongitude = d * 0.017453292519943295d;
    }

    public double getMinLongitudeDegrees() {
        return this.minLongitude * 57.29577951308232d;
    }

    public void setMaxLongitude(double d) {
        this.maxLongitude = d;
    }

    public double getMaxLongitude() {
        return this.maxLongitude;
    }

    public void setMaxLongitudeDegrees(double d) {
        this.maxLongitude = d * 0.017453292519943295d;
    }

    public double getMaxLongitudeDegrees() {
        return this.maxLongitude * 57.29577951308232d;
    }

    public void setProjectionLatitude(double d) {
        this.projectionLatitude = d;
    }

    public double getProjectionLatitude() {
        return this.projectionLatitude;
    }

    public void setProjectionLatitudeDegrees(double d) {
        this.projectionLatitude = d * 0.017453292519943295d;
    }

    public double getProjectionLatitudeDegrees() {
        return this.projectionLatitude * 57.29577951308232d;
    }

    public void setProjectionLongitude(double d) {
        this.projectionLongitude = normalizeLongitudeRadians(d);
    }

    public double getProjectionLongitude() {
        return this.projectionLongitude;
    }

    public void setProjectionLongitudeDegrees(double d) {
        this.projectionLongitude = d * 0.017453292519943295d;
    }

    public double getProjectionLongitudeDegrees() {
        return this.projectionLongitude * 57.29577951308232d;
    }

    public void setTrueScaleLatitude(double d) {
        this.trueScaleLatitude = d;
    }

    public double getTrueScaleLatitude() {
        return this.trueScaleLatitude;
    }

    public void setTrueScaleLatitudeDegrees(double d) {
        this.trueScaleLatitude = d * 0.017453292519943295d;
    }

    public double getTrueScaleLatitudeDegrees() {
        return this.trueScaleLatitude * 57.29577951308232d;
    }

    public void setProjectionLatitude1(double d) {
        this.projectionLatitude1 = d;
    }

    public double getProjectionLatitude1() {
        return this.projectionLatitude1;
    }

    public void setProjectionLatitude1Degrees(double d) {
        this.projectionLatitude1 = d * 0.017453292519943295d;
    }

    public double getProjectionLatitude1Degrees() {
        return this.projectionLatitude1 * 57.29577951308232d;
    }

    public void setProjectionLatitude2(double d) {
        this.projectionLatitude2 = d;
    }

    public double getProjectionLatitude2() {
        return this.projectionLatitude2;
    }

    public void setProjectionLatitude2Degrees(double d) {
        this.projectionLatitude2 = d * 0.017453292519943295d;
    }

    public double getProjectionLatitude2Degrees() {
        return this.projectionLatitude2 * 57.29577951308232d;
    }

    public void setAlphaDegrees(double d) {
        this.alpha = d * 0.017453292519943295d;
    }

    public double getAlpha() {
        return this.alpha;
    }

    public void setLonCDegrees(double d) {
        this.lonc = d * 0.017453292519943295d;
    }

    public double getLonC() {
        return this.lonc;
    }

    public void setFalseNorthing(double d) {
        this.falseNorthing = d;
    }

    public double getFalseNorthing() {
        return this.falseNorthing;
    }

    public void setFalseEasting(double d) {
        this.falseEasting = d;
    }

    public double getFalseEasting() {
        return this.falseEasting;
    }

    public void setSouthernHemisphere(boolean z) {
        this.isSouth = z;
    }

    public boolean getSouthernHemisphere() {
        return this.isSouth;
    }

    public void setScaleFactor(double d) {
        this.scaleFactor = d;
    }

    public double getScaleFactor() {
        return this.scaleFactor;
    }

    public double getEquatorRadius() {
        return this.f537a;
    }

    public void setFromMetres(double d) {
        this.fromMetres = d;
    }

    public double getFromMetres() {
        return this.fromMetres;
    }

    public void setEllipsoid(Ellipsoid ellipsoid2) {
        this.ellipsoid = ellipsoid2;
        this.f537a = ellipsoid2.equatorRadius;
        this.f538e = ellipsoid2.eccentricity;
        this.f539es = ellipsoid2.eccentricity2;
    }

    public Ellipsoid getEllipsoid() {
        return this.ellipsoid;
    }

    public void setUnits(Unit unit2) {
        this.unit = unit2;
    }

    public void initialize() {
        this.spherical = this.f538e == 0.0d;
        double d = 1.0d - this.f539es;
        this.one_es = d;
        this.rone_es = 1.0d / d;
        double d2 = this.f537a;
        double d3 = this.fromMetres;
        this.totalScale = d2 * d3;
        this.totalFalseEasting = this.falseEasting * d3;
        this.totalFalseNorthing = this.falseNorthing * d3;
    }

    public static float normalizeLongitude(float f) {
        double d = (double) f;
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new InvalidValueException("Infinite or NaN longitude");
        }
        while (f > 180.0f) {
            f -= 360.0f;
        }
        while (f < -180.0f) {
            f += 360.0f;
        }
        return f;
    }

    public static double normalizeLongitudeRadians(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new InvalidValueException("Infinite or NaN longitude");
        }
        while (d > 3.141592653589793d) {
            d -= 6.283185307179586d;
        }
        while (d < -3.141592653589793d) {
            d += 6.283185307179586d;
        }
        return d;
    }
}
