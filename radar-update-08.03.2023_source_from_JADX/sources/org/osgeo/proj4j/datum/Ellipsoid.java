package org.osgeo.proj4j.datum;

public class Ellipsoid implements Cloneable {
    public static final Ellipsoid AIRY;
    public static final Ellipsoid ANDRAE;
    public static final Ellipsoid APL4_9;
    public static final Ellipsoid AUSTRALIAN;
    public static final Ellipsoid AUST_SA;
    public static final Ellipsoid BESSEL;
    public static final Ellipsoid BESS_NAM;
    public static final Ellipsoid CLARKE_1866;
    public static final Ellipsoid CLARKE_1880;
    public static final Ellipsoid CPM;
    public static final Ellipsoid DELMBR;
    public static final Ellipsoid ENGELIS;
    public static final Ellipsoid EVEREST;
    public static final Ellipsoid EVRST48;
    public static final Ellipsoid EVRST56;
    public static final Ellipsoid EVRTS69;
    public static final Ellipsoid EVRTSTSS;
    public static final Ellipsoid FRSCH60;
    public static final Ellipsoid FSCHR68;
    public static final Ellipsoid FSRCH60M;
    public static final Ellipsoid GRS67;
    public static final Ellipsoid GRS80;
    public static final Ellipsoid HELMERT;
    public static final Ellipsoid HOUGH;
    public static final Ellipsoid IAU76;
    public static final Ellipsoid INTERNATIONAL = new Ellipsoid("intl", 6378388.0d, 0.0d, 297.0d, "International 1909 (Hayford)");
    public static final Ellipsoid INTERNATIONAL_1967;
    public static final Ellipsoid INTL;
    public static final Ellipsoid KAULA;
    public static final Ellipsoid KRASSOVSKY;
    public static final Ellipsoid LERCH;
    public static final Ellipsoid MERIT;
    public static final Ellipsoid MOD_AIRY;
    public static final Ellipsoid MPRTS;
    public static final Ellipsoid NAD27;
    public static final Ellipsoid NAD83;
    public static final Ellipsoid NWL9D;
    public static final Ellipsoid PLESSIS;
    public static final Ellipsoid SEASIA;
    public static final Ellipsoid SGS85;
    public static final Ellipsoid SPHERE;
    public static final Ellipsoid WALBECK;
    public static final Ellipsoid WGS60;
    public static final Ellipsoid WGS66;
    public static final Ellipsoid WGS72;
    public static final Ellipsoid WGS84;
    public static final Ellipsoid[] ellipsoids;
    public double eccentricity;
    public double eccentricity2;
    public double equatorRadius;
    public String name;
    public double poleRadius;
    public String shortName;

    static {
        Ellipsoid ellipsoid = new Ellipsoid("bessel", 6377397.155d, 0.0d, 299.1528128d, "Bessel 1841");
        BESSEL = ellipsoid;
        Ellipsoid ellipsoid2 = new Ellipsoid("clrk66", 6378206.4d, 6356583.8d, 0.0d, "Clarke 1866");
        CLARKE_1866 = ellipsoid2;
        Ellipsoid ellipsoid3 = new Ellipsoid("clrk80", 6378249.145d, 0.0d, 293.4663d, "Clarke 1880 mod.");
        CLARKE_1880 = ellipsoid3;
        Ellipsoid ellipsoid4 = new Ellipsoid("airy", 6377563.396d, 6356256.91d, 0.0d, "Airy 1830");
        AIRY = ellipsoid4;
        Ellipsoid ellipsoid5 = new Ellipsoid("WGS60", 6378165.0d, 0.0d, 298.3d, "WGS 60");
        WGS60 = ellipsoid5;
        Ellipsoid ellipsoid6 = new Ellipsoid("WGS66", 6378145.0d, 0.0d, 298.25d, "WGS 66");
        WGS66 = ellipsoid6;
        Ellipsoid ellipsoid7 = new Ellipsoid("WGS72", 6378135.0d, 0.0d, 298.26d, "WGS 72");
        WGS72 = ellipsoid7;
        Ellipsoid ellipsoid8 = new Ellipsoid("WGS84", 6378137.0d, 0.0d, 298.257223563d, "WGS 84");
        WGS84 = ellipsoid8;
        Ellipsoid ellipsoid9 = new Ellipsoid("krass", 6378245.0d, 0.0d, 298.3d, "Krassovsky, 1942");
        KRASSOVSKY = ellipsoid9;
        Ellipsoid ellipsoid10 = new Ellipsoid("evrst30", 6377276.345d, 0.0d, 300.8017d, "Everest 1830");
        EVEREST = ellipsoid10;
        Ellipsoid ellipsoid11 = new Ellipsoid("new_intl", 6378157.5d, 6356772.2d, 0.0d, "New International 1967");
        INTERNATIONAL_1967 = ellipsoid11;
        Ellipsoid ellipsoid12 = new Ellipsoid("GRS80", 6378137.0d, 0.0d, 298.257222101d, "GRS 1980 (IUGG, 1980)");
        GRS80 = ellipsoid12;
        Ellipsoid ellipsoid13 = new Ellipsoid("australian", 6378160.0d, 6356774.7d, 298.25d, "Australian");
        AUSTRALIAN = ellipsoid13;
        Ellipsoid ellipsoid14 = new Ellipsoid("MERIT", 6378137.0d, 0.0d, 298.257d, "MERIT 1983");
        MERIT = ellipsoid14;
        Ellipsoid ellipsoid15 = new Ellipsoid("SGS85", 6378136.0d, 0.0d, 298.257d, "Soviet Geodetic System 85");
        SGS85 = ellipsoid15;
        Ellipsoid ellipsoid16 = new Ellipsoid("IAU76", 6378140.0d, 0.0d, 298.257d, "IAU 1976");
        IAU76 = ellipsoid16;
        Ellipsoid ellipsoid17 = new Ellipsoid("APL4.9", 6378137.0d, 0.0d, 298.25d, "Appl. Physics. 1965");
        APL4_9 = ellipsoid17;
        Ellipsoid ellipsoid18 = new Ellipsoid("NWL9D", 6378145.0d, 0.0d, 298.25d, "Naval Weapons Lab., 1965");
        NWL9D = ellipsoid18;
        Ellipsoid ellipsoid19 = new Ellipsoid("mod_airy", 6377340.189d, 6356034.446d, 0.0d, "Modified Airy");
        MOD_AIRY = ellipsoid19;
        Ellipsoid ellipsoid20 = new Ellipsoid("andrae", 6377104.43d, 0.0d, 300.0d, "Andrae 1876 (Den., Iclnd.)");
        ANDRAE = ellipsoid20;
        Ellipsoid ellipsoid21 = new Ellipsoid("aust_SA", 6378160.0d, 0.0d, 298.25d, "Australian Natl & S. Amer. 1969");
        AUST_SA = ellipsoid21;
        Ellipsoid ellipsoid22 = new Ellipsoid("GRS67", 6378160.0d, 0.0d, 298.247167427d, "GRS 67 (IUGG 1967)");
        GRS67 = ellipsoid22;
        Ellipsoid ellipsoid23 = new Ellipsoid("bess_nam", 6377483.865d, 0.0d, 299.1528128d, "Bessel 1841 (Namibia)");
        BESS_NAM = ellipsoid23;
        Ellipsoid ellipsoid24 = new Ellipsoid("CPM", 6375738.7d, 0.0d, 334.29d, "Comm. des Poids et Mesures 1799");
        CPM = ellipsoid24;
        Ellipsoid ellipsoid25 = new Ellipsoid("delmbr", 6376428.0d, 0.0d, 311.5d, "Delambre 1810 (Belgium)");
        DELMBR = ellipsoid25;
        Ellipsoid ellipsoid26 = new Ellipsoid("engelis", 6378136.05d, 0.0d, 298.2566d, "Engelis 1985");
        ENGELIS = ellipsoid26;
        Ellipsoid ellipsoid27 = new Ellipsoid("evrst48", 6377304.063d, 0.0d, 300.8017d, "Everest 1948");
        EVRST48 = ellipsoid27;
        Ellipsoid ellipsoid28 = new Ellipsoid("evrst56", 6377301.243d, 0.0d, 300.8017d, "Everest 1956");
        EVRST56 = ellipsoid28;
        Ellipsoid ellipsoid29 = new Ellipsoid("evrst69", 6377295.664d, 0.0d, 300.8017d, "Everest 1969");
        EVRTS69 = ellipsoid29;
        Ellipsoid ellipsoid30 = new Ellipsoid("evrstSS", 6377298.556d, 0.0d, 300.8017d, "Everest (Sabah & Sarawak)");
        EVRTSTSS = ellipsoid30;
        Ellipsoid ellipsoid31 = new Ellipsoid("fschr60", 6378166.0d, 0.0d, 298.3d, "Fischer (Mercury Datum) 1960");
        FRSCH60 = ellipsoid31;
        Ellipsoid ellipsoid32 = new Ellipsoid("fschr60m", 6378155.0d, 0.0d, 298.3d, "Modified Fischer 1960");
        FSRCH60M = ellipsoid32;
        Ellipsoid ellipsoid33 = new Ellipsoid("fschr68", 6378150.0d, 0.0d, 298.3d, "Fischer 1968");
        FSCHR68 = ellipsoid33;
        Ellipsoid ellipsoid34 = new Ellipsoid("helmert", 6378200.0d, 0.0d, 298.3d, "Helmert 1906");
        HELMERT = ellipsoid34;
        Ellipsoid ellipsoid35 = new Ellipsoid("hough", 6378270.0d, 0.0d, 297.0d, "Hough");
        HOUGH = ellipsoid35;
        Ellipsoid ellipsoid36 = new Ellipsoid("intl", 6378388.0d, 0.0d, 297.0d, "International 1909 (Hayford)");
        INTL = ellipsoid36;
        Ellipsoid ellipsoid37 = new Ellipsoid("kaula", 6378163.0d, 0.0d, 298.24d, "Kaula 1961");
        KAULA = ellipsoid37;
        Ellipsoid ellipsoid38 = new Ellipsoid("lerch", 6378139.0d, 0.0d, 298.257d, "Lerch 1979");
        LERCH = ellipsoid38;
        Ellipsoid ellipsoid39 = new Ellipsoid("mprts", 6397300.0d, 0.0d, 191.0d, "Maupertius 1738");
        MPRTS = ellipsoid39;
        Ellipsoid ellipsoid40 = new Ellipsoid("plessis", 6376523.0d, 6355863.0d, 0.0d, "Plessis 1817 France)");
        PLESSIS = ellipsoid40;
        Ellipsoid ellipsoid41 = new Ellipsoid("SEasia", 6378155.0d, 6356773.3205d, 0.0d, "Southeast Asia");
        SEASIA = ellipsoid41;
        Ellipsoid ellipsoid42 = new Ellipsoid("walbeck", 6376896.0d, 6355834.8467d, 0.0d, "Walbeck");
        WALBECK = ellipsoid42;
        Ellipsoid ellipsoid43 = new Ellipsoid("NAD27", 6378249.145d, 0.0d, 293.4663d, "NAD27: Clarke 1880 mod.");
        NAD27 = ellipsoid43;
        Ellipsoid ellipsoid44 = new Ellipsoid("NAD83", 6378137.0d, 0.0d, 298.257222101d, "NAD83: GRS 1980 (IUGG, 1980)");
        NAD83 = ellipsoid44;
        Ellipsoid ellipsoid45 = new Ellipsoid("sphere", 6371008.7714d, 6371008.7714d, 0.0d, "Sphere");
        SPHERE = ellipsoid45;
        ellipsoids = new Ellipsoid[]{ellipsoid, ellipsoid2, ellipsoid3, ellipsoid4, ellipsoid5, ellipsoid6, ellipsoid7, ellipsoid8, ellipsoid9, ellipsoid10, ellipsoid11, ellipsoid12, ellipsoid13, ellipsoid14, ellipsoid15, ellipsoid16, ellipsoid17, ellipsoid18, ellipsoid19, ellipsoid20, ellipsoid21, ellipsoid22, ellipsoid23, ellipsoid24, ellipsoid25, ellipsoid26, ellipsoid27, ellipsoid28, ellipsoid29, ellipsoid30, ellipsoid31, ellipsoid32, ellipsoid33, ellipsoid34, ellipsoid35, ellipsoid36, ellipsoid37, ellipsoid38, ellipsoid39, ellipsoid40, ellipsoid41, ellipsoid42, ellipsoid43, ellipsoid44, ellipsoid45};
    }

    public Ellipsoid() {
        this.equatorRadius = 1.0d;
        this.poleRadius = 1.0d;
        this.eccentricity = 1.0d;
        this.eccentricity2 = 1.0d;
    }

    public Ellipsoid(String str, double d, double d2, double d3, String str2) {
        this.eccentricity = 1.0d;
        this.eccentricity2 = 1.0d;
        this.shortName = str;
        this.name = str2;
        this.equatorRadius = d;
        this.poleRadius = d2;
        if (d2 == 0.0d && d3 == 0.0d) {
            throw new IllegalArgumentException("One of poleRadius or reciprocalFlattening must be specified");
        }
        if (d3 != 0.0d) {
            double d4 = 1.0d / d3;
            double d5 = (2.0d * d4) - (d4 * d4);
            this.eccentricity2 = d5;
            this.poleRadius = d * Math.sqrt(1.0d - d5);
        } else {
            this.eccentricity2 = 1.0d - ((d2 * d2) / (d * d));
        }
        this.eccentricity = Math.sqrt(this.eccentricity2);
    }

    public Ellipsoid(String str, double d, double d2, String str2) {
        this.poleRadius = 1.0d;
        this.eccentricity = 1.0d;
        this.eccentricity2 = 1.0d;
        this.shortName = str;
        this.name = str2;
        this.equatorRadius = d;
        setEccentricitySquared(d2);
    }

    public Object clone() {
        try {
            return (Ellipsoid) super.clone();
        } catch (CloneNotSupportedException unused) {
            throw new InternalError();
        }
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public void setShortName(String str) {
        this.shortName = str;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setEquatorRadius(double d) {
        this.equatorRadius = d;
    }

    public double getEquatorRadius() {
        return this.equatorRadius;
    }

    public double getA() {
        return this.equatorRadius;
    }

    public double getB() {
        return this.poleRadius;
    }

    public void setEccentricitySquared(double d) {
        this.eccentricity2 = d;
        this.poleRadius = this.equatorRadius * Math.sqrt(1.0d - d);
        this.eccentricity = Math.sqrt(d);
    }

    public double getEccentricitySquared() {
        return this.eccentricity2;
    }

    public boolean isEqual(Ellipsoid ellipsoid) {
        return this.equatorRadius == ellipsoid.equatorRadius && this.eccentricity2 == ellipsoid.eccentricity2;
    }

    public boolean isEqual(Ellipsoid ellipsoid, double d) {
        if (this.equatorRadius == ellipsoid.equatorRadius && Math.abs(this.eccentricity2 - ellipsoid.eccentricity2) <= d) {
            return true;
        }
        return false;
    }

    public String toString() {
        return this.name;
    }
}
