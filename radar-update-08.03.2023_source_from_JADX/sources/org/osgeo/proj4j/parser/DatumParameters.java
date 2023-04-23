package org.osgeo.proj4j.parser;

import org.osgeo.proj4j.datum.Datum;
import org.osgeo.proj4j.datum.Ellipsoid;

public class DatumParameters {
    private static final double RA4 = 0.04722222222222222d;
    private static final double RA6 = 0.022156084656084655d;
    private static final double RV4 = 0.06944444444444445d;
    private static final double RV6 = 0.04243827160493827d;
    private static final double SIXTH = 0.16666666666666666d;

    /* renamed from: a */
    private double f417a = Double.NaN;
    private Datum datum = null;
    private double[] datumTransform = null;
    private Ellipsoid ellipsoid;

    /* renamed from: es */
    private double f418es = Double.NaN;

    public Datum getDatum() {
        Datum datum2 = this.datum;
        if (datum2 != null) {
            return datum2;
        }
        if (this.ellipsoid == null && !isDefinedExplicitly()) {
            return Datum.WGS84;
        }
        if (this.ellipsoid == Ellipsoid.WGS84) {
            return Datum.WGS84;
        }
        return new Datum("User", this.datumTransform, getEllipsoid(), "User-defined");
    }

    private boolean isDefinedExplicitly() {
        return !Double.isNaN(this.f417a) && !Double.isNaN(this.f418es);
    }

    public Ellipsoid getEllipsoid() {
        Ellipsoid ellipsoid2 = this.ellipsoid;
        if (ellipsoid2 != null) {
            return ellipsoid2;
        }
        return new Ellipsoid("user", this.f417a, this.f418es, "User-defined");
    }

    public void setDatumTransform(double[] dArr) {
        this.datumTransform = dArr;
        this.datum = null;
    }

    public void setDatum(Datum datum2) {
        this.datum = datum2;
    }

    public void setEllipsoid(Ellipsoid ellipsoid2) {
        this.ellipsoid = ellipsoid2;
        this.f418es = ellipsoid2.eccentricity2;
        this.f417a = ellipsoid2.equatorRadius;
    }

    public void setA(double d) {
        this.ellipsoid = null;
        this.f417a = d;
    }

    public void setB(double d) {
        this.ellipsoid = null;
        double d2 = this.f417a;
        this.f418es = 1.0d - ((d * d) / (d2 * d2));
    }

    public void setES(double d) {
        this.ellipsoid = null;
        this.f418es = d;
    }

    public void setRF(double d) {
        this.ellipsoid = null;
        this.f418es = d * (2.0d - d);
    }

    public void setR_A() {
        this.ellipsoid = null;
        double d = this.f417a;
        double d2 = this.f418es;
        this.f417a = d * (1.0d - (d2 * ((((RA6 * d2) + RA4) * d2) + SIXTH)));
    }

    public void setF(double d) {
        this.ellipsoid = null;
        double d2 = 1.0d / d;
        this.f418es = d2 * (2.0d - d2);
    }

    public double getA() {
        return this.f417a;
    }

    public double getES() {
        return this.f418es;
    }
}
