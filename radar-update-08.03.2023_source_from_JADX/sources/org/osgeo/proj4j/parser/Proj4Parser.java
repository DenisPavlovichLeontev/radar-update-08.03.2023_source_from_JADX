package org.osgeo.proj4j.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.InvalidValueException;
import org.osgeo.proj4j.Registry;
import org.osgeo.proj4j.datum.Datum;
import org.osgeo.proj4j.datum.Ellipsoid;
import org.osgeo.proj4j.proj.Projection;
import org.osgeo.proj4j.proj.TransverseMercatorProjection;
import org.osgeo.proj4j.units.Angle;
import org.osgeo.proj4j.units.Unit;
import org.osgeo.proj4j.units.Units;

public class Proj4Parser {
    private Registry registry;

    public Proj4Parser(Registry registry2) {
        this.registry = registry2;
    }

    public CoordinateReferenceSystem parse(String str, String[] strArr) {
        if (strArr == null) {
            return null;
        }
        Map createParameterMap = createParameterMap(strArr);
        Proj4Keyword.checkUnsupported((Collection) createParameterMap.keySet());
        DatumParameters datumParameters = new DatumParameters();
        parseDatum(createParameterMap, datumParameters);
        parseEllipsoid(createParameterMap, datumParameters);
        Datum datum = datumParameters.getDatum();
        return new CoordinateReferenceSystem(str, strArr, datum, parseProjection(createParameterMap, datum.getEllipsoid()));
    }

    private Projection parseProjection(Map map, Ellipsoid ellipsoid) {
        Projection projection;
        String str;
        Unit findUnits;
        String str2 = (String) map.get(Proj4Keyword.proj);
        if (str2 != null) {
            projection = this.registry.getProjection(str2);
            if (projection == null) {
                throw new InvalidValueException("Unknown projection: " + str2);
            }
        } else {
            projection = null;
        }
        projection.setEllipsoid(ellipsoid);
        String str3 = (String) map.get("alpha");
        if (str3 != null) {
            projection.setAlphaDegrees(Double.parseDouble(str3));
        }
        String str4 = (String) map.get(Proj4Keyword.lonc);
        if (str4 != null) {
            projection.setLonCDegrees(Double.parseDouble(str4));
        }
        String str5 = (String) map.get(Proj4Keyword.lat_0);
        if (str5 != null) {
            projection.setProjectionLatitudeDegrees(parseAngle(str5));
        }
        String str6 = (String) map.get(Proj4Keyword.lon_0);
        if (str6 != null) {
            projection.setProjectionLongitudeDegrees(parseAngle(str6));
        }
        String str7 = (String) map.get(Proj4Keyword.lat_1);
        if (str7 != null) {
            projection.setProjectionLatitude1Degrees(parseAngle(str7));
        }
        String str8 = (String) map.get(Proj4Keyword.lat_2);
        if (str8 != null) {
            projection.setProjectionLatitude2Degrees(parseAngle(str8));
        }
        String str9 = (String) map.get(Proj4Keyword.lat_ts);
        if (str9 != null) {
            projection.setTrueScaleLatitudeDegrees(parseAngle(str9));
        }
        String str10 = (String) map.get(Proj4Keyword.x_0);
        if (str10 != null) {
            projection.setFalseEasting(Double.parseDouble(str10));
        }
        String str11 = (String) map.get(Proj4Keyword.y_0);
        if (str11 != null) {
            projection.setFalseNorthing(Double.parseDouble(str11));
        }
        String str12 = (String) map.get(Proj4Keyword.k_0);
        if (str12 == null) {
            str12 = (String) map.get(Proj4Keyword.f424k);
        }
        if (str12 != null) {
            projection.setScaleFactor(Double.parseDouble(str12));
        }
        String str13 = (String) map.get(Proj4Keyword.units);
        if (!(str13 == null || (findUnits = Units.findUnits(str13)) == null)) {
            projection.setFromMetres(1.0d / findUnits.value);
            projection.setUnits(findUnits);
        }
        String str14 = (String) map.get(Proj4Keyword.to_meter);
        if (str14 != null) {
            projection.setFromMetres(1.0d / Double.parseDouble(str14));
        }
        if (map.containsKey(Proj4Keyword.south)) {
            projection.setSouthernHemisphere(true);
        }
        if ((projection instanceof TransverseMercatorProjection) && (str = (String) map.get(Proj4Keyword.zone)) != null) {
            ((TransverseMercatorProjection) projection).setUTMZone(Integer.parseInt(str));
        }
        projection.initialize();
        return projection;
    }

    private void parseDatum(Map map, DatumParameters datumParameters) {
        String str = (String) map.get(Proj4Keyword.towgs84);
        if (str != null) {
            datumParameters.setDatumTransform(parseToWGS84(str));
        }
        String str2 = (String) map.get(Proj4Keyword.datum);
        if (str2 != null) {
            Datum datum = this.registry.getDatum(str2);
            if (datum != null) {
                datumParameters.setDatum(datum);
                return;
            }
            throw new InvalidValueException("Unknown datum: " + str2);
        }
    }

    private double[] parseToWGS84(String str) {
        String[] split = str.split(",");
        if (split.length == 3 || split.length == 7) {
            int length = split.length;
            double[] dArr = new double[length];
            for (int i = 0; i < split.length; i++) {
                dArr[i] = Double.parseDouble(split[i]);
            }
            if (length > 3 && dArr[3] == 0.0d && dArr[4] == 0.0d && dArr[5] == 0.0d && dArr[6] == 0.0d) {
                dArr = new double[]{dArr[0], dArr[1], dArr[2]};
            }
            if (dArr.length > 3) {
                dArr[3] = dArr[3] * 4.84813681109536E-6d;
                dArr[4] = dArr[4] * 4.84813681109536E-6d;
                dArr[5] = dArr[5] * 4.84813681109536E-6d;
                dArr[6] = (dArr[6] / 1000000.0d) + 1.0d;
            }
            return dArr;
        }
        throw new InvalidValueException("Invalid number of values (must be 3 or 7) in +towgs84: " + str);
    }

    private void parseEllipsoid(Map map, DatumParameters datumParameters) {
        double d;
        String str = (String) map.get(Proj4Keyword.ellps);
        if (str != null) {
            Ellipsoid ellipsoid = this.registry.getEllipsoid(str);
            if (ellipsoid != null) {
                datumParameters.setEllipsoid(ellipsoid);
            } else {
                throw new InvalidValueException("Unknown ellipsoid: " + str);
            }
        }
        String str2 = (String) map.get(Proj4Keyword.f420a);
        if (str2 != null) {
            datumParameters.setA(Double.parseDouble(str2));
        }
        String str3 = (String) map.get(Proj4Keyword.f422es);
        if (str3 != null) {
            datumParameters.setES(Double.parseDouble(str3));
        }
        String str4 = (String) map.get(Proj4Keyword.f426rf);
        if (str4 != null) {
            datumParameters.setRF(Double.parseDouble(str4));
        }
        String str5 = (String) map.get(Proj4Keyword.f423f);
        if (str5 != null) {
            datumParameters.setF(Double.parseDouble(str5));
        }
        String str6 = (String) map.get(Proj4Keyword.f421b);
        if (str6 != null) {
            d = Double.parseDouble(str6);
            datumParameters.setB(d);
        } else {
            d = 0.0d;
        }
        if (d == 0.0d) {
            datumParameters.getA();
            Math.sqrt(1.0d - datumParameters.getES());
        }
        parseEllipsoidModifiers(map, datumParameters);
    }

    private void parseEllipsoidModifiers(Map map, DatumParameters datumParameters) {
        if (map.containsKey(Proj4Keyword.R_A)) {
            datumParameters.setR_A();
        }
    }

    private Map createParameterMap(String[] strArr) {
        HashMap hashMap = new HashMap();
        for (String str : strArr) {
            if (str.startsWith("+")) {
                str = str.substring(1);
            }
            int indexOf = str.indexOf(61);
            if (indexOf != -1) {
                hashMap.put(str.substring(0, indexOf), str.substring(indexOf + 1));
            } else {
                hashMap.put(str, (Object) null);
            }
        }
        return hashMap;
    }

    private static double parseAngle(String str) {
        return Angle.parse(str);
    }
}
