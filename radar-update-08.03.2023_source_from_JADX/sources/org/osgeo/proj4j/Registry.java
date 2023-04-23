package org.osgeo.proj4j;

import java.util.HashMap;
import java.util.Map;
import org.osgeo.proj4j.datum.Datum;
import org.osgeo.proj4j.datum.Ellipsoid;
import org.osgeo.proj4j.proj.AiryProjection;
import org.osgeo.proj4j.proj.AitoffProjection;
import org.osgeo.proj4j.proj.AlbersProjection;
import org.osgeo.proj4j.proj.AugustProjection;
import org.osgeo.proj4j.proj.BipolarProjection;
import org.osgeo.proj4j.proj.BoggsProjection;
import org.osgeo.proj4j.proj.BonneProjection;
import org.osgeo.proj4j.proj.CassiniProjection;
import org.osgeo.proj4j.proj.CentralCylindricalProjection;
import org.osgeo.proj4j.proj.CollignonProjection;
import org.osgeo.proj4j.proj.CrasterProjection;
import org.osgeo.proj4j.proj.DenoyerProjection;
import org.osgeo.proj4j.proj.Eckert1Projection;
import org.osgeo.proj4j.proj.Eckert2Projection;
import org.osgeo.proj4j.proj.Eckert4Projection;
import org.osgeo.proj4j.proj.Eckert5Projection;
import org.osgeo.proj4j.proj.Eckert6Projection;
import org.osgeo.proj4j.proj.EquidistantAzimuthalProjection;
import org.osgeo.proj4j.proj.EquidistantConicProjection;
import org.osgeo.proj4j.proj.EulerProjection;
import org.osgeo.proj4j.proj.FaheyProjection;
import org.osgeo.proj4j.proj.FoucautProjection;
import org.osgeo.proj4j.proj.FoucautSinusoidalProjection;
import org.osgeo.proj4j.proj.GallProjection;
import org.osgeo.proj4j.proj.GnomonicAzimuthalProjection;
import org.osgeo.proj4j.proj.GoodeProjection;
import org.osgeo.proj4j.proj.HammerProjection;
import org.osgeo.proj4j.proj.HatanoProjection;
import org.osgeo.proj4j.proj.KavraiskyVProjection;
import org.osgeo.proj4j.proj.LagrangeProjection;
import org.osgeo.proj4j.proj.LambertAzimuthalEqualAreaProjection;
import org.osgeo.proj4j.proj.LambertConformalConicProjection;
import org.osgeo.proj4j.proj.LambertEqualAreaConicProjection;
import org.osgeo.proj4j.proj.LandsatProjection;
import org.osgeo.proj4j.proj.LarriveeProjection;
import org.osgeo.proj4j.proj.LaskowskiProjection;
import org.osgeo.proj4j.proj.LongLatProjection;
import org.osgeo.proj4j.proj.LoximuthalProjection;
import org.osgeo.proj4j.proj.McBrydeThomasFlatPolarParabolicProjection;
import org.osgeo.proj4j.proj.McBrydeThomasFlatPolarQuarticProjection;
import org.osgeo.proj4j.proj.McBrydeThomasFlatPolarSine2Projection;
import org.osgeo.proj4j.proj.MercatorProjection;
import org.osgeo.proj4j.proj.MillerProjection;
import org.osgeo.proj4j.proj.MolleweideProjection;
import org.osgeo.proj4j.proj.Murdoch1Projection;
import org.osgeo.proj4j.proj.Murdoch2Projection;
import org.osgeo.proj4j.proj.Murdoch3Projection;
import org.osgeo.proj4j.proj.NellProjection;
import org.osgeo.proj4j.proj.NicolosiProjection;
import org.osgeo.proj4j.proj.ObliqueMercatorProjection;
import org.osgeo.proj4j.proj.ObliqueStereographicAlternativeProjection;
import org.osgeo.proj4j.proj.OrthographicAzimuthalProjection;
import org.osgeo.proj4j.proj.PerspectiveConicProjection;
import org.osgeo.proj4j.proj.PerspectiveProjection;
import org.osgeo.proj4j.proj.PlateCarreeProjection;
import org.osgeo.proj4j.proj.PolyconicProjection;
import org.osgeo.proj4j.proj.Projection;
import org.osgeo.proj4j.proj.PutninsP2Projection;
import org.osgeo.proj4j.proj.PutninsP4Projection;
import org.osgeo.proj4j.proj.PutninsP5PProjection;
import org.osgeo.proj4j.proj.PutninsP5Projection;
import org.osgeo.proj4j.proj.QuarticAuthalicProjection;
import org.osgeo.proj4j.proj.RectangularPolyconicProjection;
import org.osgeo.proj4j.proj.RobinsonProjection;
import org.osgeo.proj4j.proj.SinusoidalProjection;
import org.osgeo.proj4j.proj.StereographicAzimuthalProjection;
import org.osgeo.proj4j.proj.SwissObliqueMercatorProjection;
import org.osgeo.proj4j.proj.TransverseCylindricalEqualArea;
import org.osgeo.proj4j.proj.TransverseMercatorProjection;
import org.osgeo.proj4j.proj.TranverseCentralCylindricalProjection;
import org.osgeo.proj4j.proj.UrmaevFlatPolarSinusoidalProjection;
import org.osgeo.proj4j.proj.VanDerGrintenProjection;
import org.osgeo.proj4j.proj.VitkovskyProjection;
import org.osgeo.proj4j.proj.Wagner1Projection;
import org.osgeo.proj4j.proj.Wagner2Projection;
import org.osgeo.proj4j.proj.Wagner3Projection;
import org.osgeo.proj4j.proj.Wagner4Projection;
import org.osgeo.proj4j.proj.Wagner5Projection;
import org.osgeo.proj4j.proj.Wagner7Projection;
import org.osgeo.proj4j.proj.WerenskioldProjection;
import org.osgeo.proj4j.proj.WinkelTripelProjection;

public class Registry {
    public static final Datum[] datums = {Datum.WGS84, Datum.GGRS87, Datum.NAD27, Datum.NAD83, Datum.POTSDAM, Datum.CARTHAGE, Datum.HERMANNSKOGEL, Datum.IRE65, Datum.NZGD49, Datum.OSEB36};
    public static final Ellipsoid[] ellipsoids = {Ellipsoid.SPHERE, new Ellipsoid("MERIT", 6378137.0d, 0.0d, 298.257d, "MERIT 1983"), new Ellipsoid("SGS85", 6378136.0d, 0.0d, 298.257d, "Soviet Geodetic System 85"), Ellipsoid.GRS80, new Ellipsoid("IAU76", 6378140.0d, 0.0d, 298.257d, "IAU 1976"), Ellipsoid.AIRY, Ellipsoid.MOD_AIRY, new Ellipsoid("APL4.9", 6378137.0d, 0.0d, 298.25d, "Appl. Physics. 1965"), new Ellipsoid("NWL9D", 6378145.0d, 298.25d, 0.0d, "Naval Weapons Lab., 1965"), new Ellipsoid("andrae", 6377104.43d, 300.0d, 0.0d, "Andrae 1876 (Den., Iclnd.)"), new Ellipsoid("aust_SA", 6378160.0d, 0.0d, 298.25d, "Australian Natl & S. Amer. 1969"), new Ellipsoid("GRS67", 6378160.0d, 0.0d, 298.247167427d, "GRS 67 (IUGG 1967)"), Ellipsoid.BESSEL, new Ellipsoid("bess_nam", 6377483.865d, 0.0d, 299.1528128d, "Bessel 1841 (Namibia)"), Ellipsoid.CLARKE_1866, Ellipsoid.CLARKE_1880, new Ellipsoid("CPM", 6375738.7d, 0.0d, 334.29d, "Comm. des Poids et Mesures 1799"), new Ellipsoid("delmbr", 6376428.0d, 0.0d, 311.5d, "Delambre 1810 (Belgium)"), new Ellipsoid("engelis", 6378136.05d, 0.0d, 298.2566d, "Engelis 1985"), Ellipsoid.EVEREST, new Ellipsoid("evrst48", 6377304.063d, 0.0d, 300.8017d, "Everest 1948"), new Ellipsoid("evrst56", 6377301.243d, 0.0d, 300.8017d, "Everest 1956"), new Ellipsoid("evrst69", 6377295.664d, 0.0d, 300.8017d, "Everest 1969"), new Ellipsoid("evrstSS", 6377298.556d, 0.0d, 300.8017d, "Everest (Sabah & Sarawak)"), new Ellipsoid("fschr60", 6378166.0d, 0.0d, 298.3d, "Fischer (Mercury Datum) 1960"), new Ellipsoid("fschr60m", 6378155.0d, 0.0d, 298.3d, "Modified Fischer 1960"), new Ellipsoid("fschr68", 6378150.0d, 0.0d, 298.3d, "Fischer 1968"), new Ellipsoid("helmert", 6378200.0d, 0.0d, 298.3d, "Helmert 1906"), new Ellipsoid("hough", 6378270.0d, 0.0d, 297.0d, "Hough"), Ellipsoid.INTERNATIONAL, Ellipsoid.INTERNATIONAL_1967, Ellipsoid.KRASSOVSKY, new Ellipsoid("kaula", 6378163.0d, 0.0d, 298.24d, "Kaula 1961"), new Ellipsoid("lerch", 6378139.0d, 0.0d, 298.257d, "Lerch 1979"), new Ellipsoid("mprts", 6397300.0d, 0.0d, 191.0d, "Maupertius 1738"), new Ellipsoid("plessis", 6376523.0d, 6355863.0d, 0.0d, "Plessis 1817 France)"), new Ellipsoid("SEasia", 6378155.0d, 6356773.3205d, 0.0d, "Southeast Asia"), new Ellipsoid("walbeck", 6376896.0d, 6355834.8467d, 0.0d, "Walbeck"), Ellipsoid.WGS60, Ellipsoid.WGS66, Ellipsoid.WGS72, Ellipsoid.WGS84, new Ellipsoid("NAD27", 6378249.145d, 0.0d, 293.4663d, "NAD27: Clarke 1880 mod."), new Ellipsoid("NAD83", 6378137.0d, 0.0d, 298.257222101d, "NAD83: GRS 1980 (IUGG, 1980)")};
    private Map<String, Class> projRegistry;

    public Registry() {
        initialize();
    }

    public Datum getDatum(String str) {
        int i = 0;
        while (true) {
            Datum[] datumArr = datums;
            if (i >= datumArr.length) {
                return null;
            }
            if (datumArr[i].getCode().equals(str)) {
                return datumArr[i];
            }
            i++;
        }
    }

    public Ellipsoid getEllipsoid(String str) {
        int i = 0;
        while (true) {
            Ellipsoid[] ellipsoidArr = ellipsoids;
            if (i >= ellipsoidArr.length) {
                return null;
            }
            if (ellipsoidArr[i].shortName.equals(str)) {
                return ellipsoidArr[i];
            }
            i++;
        }
    }

    private void register(String str, Class cls, String str2) {
        this.projRegistry.put(str, cls);
    }

    public Projection getProjection(String str) {
        Class cls = this.projRegistry.get(str);
        if (cls == null) {
            return null;
        }
        try {
            Projection projection = (Projection) cls.newInstance();
            if (projection != null) {
                projection.setName(str);
            }
            return projection;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private synchronized void initialize() {
        if (this.projRegistry == null) {
            this.projRegistry = new HashMap();
            register("aea", AlbersProjection.class, "Albers Equal Area");
            register("aeqd", EquidistantAzimuthalProjection.class, "Azimuthal Equidistant");
            register("airy", AiryProjection.class, "Airy");
            register("aitoff", AitoffProjection.class, "Aitoff");
            register("alsk", Projection.class, "Mod. Stereographics of Alaska");
            register("apian", Projection.class, "Apian Globular I");
            register("august", AugustProjection.class, "August Epicycloidal");
            register("bacon", Projection.class, "Bacon Globular");
            register("bipc", BipolarProjection.class, "Bipolar conic of western hemisphere");
            register("boggs", BoggsProjection.class, "Boggs Eumorphic");
            register("bonne", BonneProjection.class, "Bonne (Werner lat_1=90)");
            register("cass", CassiniProjection.class, "Cassini");
            register("cc", CentralCylindricalProjection.class, "Central Cylindrical");
            register("cea", Projection.class, "Equal Area Cylindrical");
            register("collg", CollignonProjection.class, "Collignon");
            register("crast", CrasterProjection.class, "Craster Parabolic (Putnins P4)");
            register("denoy", DenoyerProjection.class, "Denoyer Semi-Elliptical");
            register("eck1", Eckert1Projection.class, "Eckert I");
            register("eck2", Eckert2Projection.class, "Eckert II");
            register("eck4", Eckert4Projection.class, "Eckert IV");
            register("eck5", Eckert5Projection.class, "Eckert V");
            register("eck6", Eckert6Projection.class, "Eckert VI");
            register("eqc", PlateCarreeProjection.class, "Equidistant Cylindrical (Plate Caree)");
            register("eqdc", EquidistantConicProjection.class, "Equidistant Conic");
            register("euler", EulerProjection.class, "Euler");
            register("fahey", FaheyProjection.class, "Fahey");
            register("fouc", FoucautProjection.class, "Foucaut");
            register("fouc_s", FoucautSinusoidalProjection.class, "Foucaut Sinusoidal");
            register("gall", GallProjection.class, "Gall (Gall Stereographic)");
            register("gnom", GnomonicAzimuthalProjection.class, "Gnomonic");
            register("goode", GoodeProjection.class, "Goode Homolosine");
            register("hammer", HammerProjection.class, "Hammer & Eckert-Greifendorff");
            register("hatano", HatanoProjection.class, "Hatano Asymmetrical Equal Area");
            register("kav5", KavraiskyVProjection.class, "Kavraisky V");
            register("laea", LambertAzimuthalEqualAreaProjection.class, "Lambert Azimuthal Equal Area");
            register("lagrng", LagrangeProjection.class, "Lagrange");
            register("larr", LarriveeProjection.class, "Larrivee");
            register("lask", LaskowskiProjection.class, "Laskowski");
            register("latlong", LongLatProjection.class, "Lat/Long");
            register("longlat", LongLatProjection.class, "Lat/Long");
            register("lcc", LambertConformalConicProjection.class, "Lambert Conformal Conic");
            register("leac", LambertEqualAreaConicProjection.class, "Lambert Equal Area Conic");
            register("loxim", LoximuthalProjection.class, "Loximuthal");
            register("lsat", LandsatProjection.class, "Space oblique for LANDSAT");
            register("mbt_fps", McBrydeThomasFlatPolarSine2Projection.class, "McBryde-Thomas Flat-Pole Sine (No. 2)");
            register("mbtfpp", McBrydeThomasFlatPolarParabolicProjection.class, "McBride-Thomas Flat-Polar Parabolic");
            register("mbtfpq", McBrydeThomasFlatPolarQuarticProjection.class, "McBryde-Thomas Flat-Polar Quartic");
            register("merc", MercatorProjection.class, "Mercator");
            register("mill", MillerProjection.class, "Miller Cylindrical");
            register("moll", MolleweideProjection.class, "Mollweide");
            register("murd1", Murdoch1Projection.class, "Murdoch I");
            register("murd2", Murdoch2Projection.class, "Murdoch II");
            register("murd3", Murdoch3Projection.class, "Murdoch III");
            register("nell", NellProjection.class, "Nell");
            register("nicol", NicolosiProjection.class, "Nicolosi Globular");
            register("nsper", PerspectiveProjection.class, "Near-sided perspective");
            register("omerc", ObliqueMercatorProjection.class, "Oblique Mercator");
            register("ortho", OrthographicAzimuthalProjection.class, "Orthographic");
            register("pconic", PerspectiveConicProjection.class, "Perspective Conic");
            register("poly", PolyconicProjection.class, "Polyconic (American)");
            register("putp2", PutninsP2Projection.class, "Putnins P2");
            register("putp4p", PutninsP4Projection.class, "Putnins P4'");
            register("putp5", PutninsP5Projection.class, "Putnins P5");
            register("putp5p", PutninsP5PProjection.class, "Putnins P5'");
            register("qua_aut", QuarticAuthalicProjection.class, "Quartic Authalic");
            register("robin", RobinsonProjection.class, "Robinson");
            register("rpoly", RectangularPolyconicProjection.class, "Rectangular Polyconic");
            register("sinu", SinusoidalProjection.class, "Sinusoidal (Sanson-Flamsteed)");
            register("somerc", SwissObliqueMercatorProjection.class, "Swiss Oblique Mercator");
            register("stere", StereographicAzimuthalProjection.class, "Stereographic");
            register("sterea", ObliqueStereographicAlternativeProjection.class, "Oblique Stereographic Alternative");
            register("tcc", TranverseCentralCylindricalProjection.class, "Transverse Central Cylindrical");
            register("tcea", TransverseCylindricalEqualArea.class, "Transverse Cylindrical Equal Area");
            register("tmerc", TransverseMercatorProjection.class, "Transverse Mercator");
            register("urmfps", UrmaevFlatPolarSinusoidalProjection.class, "Urmaev Flat-Polar Sinusoidal");
            register("utm", TransverseMercatorProjection.class, "Universal Transverse Mercator (UTM)");
            register("vandg", VanDerGrintenProjection.class, "van der Grinten (I)");
            register("vitk1", VitkovskyProjection.class, "Vitkovsky I");
            register("wag1", Wagner1Projection.class, "Wagner I (Kavraisky VI)");
            register("wag2", Wagner2Projection.class, "Wagner II");
            register("wag3", Wagner3Projection.class, "Wagner III");
            register("wag4", Wagner4Projection.class, "Wagner IV");
            register("wag5", Wagner5Projection.class, "Wagner V");
            register("wag7", Wagner7Projection.class, "Wagner VII");
            register("weren", WerenskioldProjection.class, "Werenskiold I");
            register("wintri", WinkelTripelProjection.class, "Winkel Tripel");
        }
    }
}
