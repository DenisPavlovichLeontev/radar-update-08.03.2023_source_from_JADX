package org.osmdroid.gpkg.overlay;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.projection.ProjectionTransform;
import mil.nga.wkb.geom.CircularString;
import mil.nga.wkb.geom.CompoundCurve;
import mil.nga.wkb.geom.Curve;
import mil.nga.wkb.geom.CurvePolygon;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryType;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.MultiLineString;
import mil.nga.wkb.geom.MultiPoint;
import mil.nga.wkb.geom.MultiPolygon;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.PolyhedralSurface;
import mil.nga.wkb.geom.TIN;
import mil.nga.wkb.geom.Triangle;
import org.osmdroid.api.IMapView;
import org.osmdroid.gpkg.C1334R;
import org.osmdroid.gpkg.overlay.features.MarkerOptions;
import org.osmdroid.gpkg.overlay.features.MultiLatLng;
import org.osmdroid.gpkg.overlay.features.MultiMarker;
import org.osmdroid.gpkg.overlay.features.MultiPolyline;
import org.osmdroid.gpkg.overlay.features.MultiPolylineOptions;
import org.osmdroid.gpkg.overlay.features.OsmDroidMapShape;
import org.osmdroid.gpkg.overlay.features.OsmMapShapeType;
import org.osmdroid.gpkg.overlay.features.PolygonOptions;
import org.osmdroid.gpkg.overlay.features.PolygonOrientation;
import org.osmdroid.gpkg.overlay.features.PolylineOptions;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

public class OsmMapShapeConverter {
    private PolygonOrientation exteriorOrientation;
    private final ProjectionTransform fromWgs84;
    private PolygonOrientation holeOrientation;
    private MarkerOptions makerOptions;
    private PolygonOptions polygonOptions;
    private PolylineOptions polylineOptions;
    private final Projection projection;
    private final ProjectionTransform toWgs84;

    public OsmMapShapeConverter() {
        this((Projection) null, (MarkerOptions) null, (PolylineOptions) null, (PolygonOptions) null);
    }

    public OsmMapShapeConverter(Projection projection2, MarkerOptions markerOptions, PolylineOptions polylineOptions2, PolygonOptions polygonOptions2) {
        this.exteriorOrientation = PolygonOrientation.COUNTERCLOCKWISE;
        this.holeOrientation = PolygonOrientation.CLOCKWISE;
        Log.i(IMapView.LOGTAG, "Geopackage support is BETA. Please report any issues");
        this.projection = projection2;
        this.polylineOptions = polylineOptions2;
        this.polygonOptions = polygonOptions2;
        this.makerOptions = markerOptions;
        if (projection2 != null) {
            ProjectionTransform transformation = projection2.getTransformation(4326);
            this.toWgs84 = transformation;
            this.fromWgs84 = transformation.getToProjection().getTransformation(projection2);
            return;
        }
        this.toWgs84 = null;
        this.fromWgs84 = null;
    }

    public Projection getProjection() {
        return this.projection;
    }

    public Point toWgs84(Point point) {
        return this.projection != null ? this.toWgs84.transform(point) : point;
    }

    public Point toProjection(Point point) {
        return this.projection != null ? this.fromWgs84.transform(point) : point;
    }

    public GeoPoint toLatLng2(Point point) {
        Point wgs84 = toWgs84(point);
        return new GeoPoint(wgs84.getY(), wgs84.getX());
    }

    public GeoPoint toLatLng(Point point) {
        Point wgs84 = toWgs84(point);
        return new GeoPoint(wgs84.getY(), wgs84.getX());
    }

    public Polyline toPolyline(LineString lineString) {
        Polyline polyline = new Polyline();
        PolylineOptions polylineOptions2 = this.polylineOptions;
        if (polylineOptions2 != null) {
            polyline.setTitle(polylineOptions2.getTitle());
            polyline.getOutlinePaint().setColor(this.polylineOptions.getColor());
            polyline.setGeodesic(this.polylineOptions.isGeodesic());
            polyline.getOutlinePaint().setStrokeWidth(this.polylineOptions.getWidth());
            polyline.setSubDescription(this.polylineOptions.getSubtitle());
        }
        ArrayList arrayList = new ArrayList();
        for (Point latLng : lineString.getPoints()) {
            arrayList.add(toLatLng(latLng));
        }
        polyline.setPoints(arrayList);
        return polyline;
    }

    public Polygon toPolygon(mil.nga.wkb.geom.Polygon polygon) {
        Polygon polygon2 = new Polygon();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        List rings = polygon.getRings();
        if (!rings.isEmpty()) {
            Double d = null;
            for (Point latLng : ((LineString) rings.get(0)).getPoints()) {
                arrayList.add(toLatLng(latLng));
            }
            for (int i = 1; i < rings.size(); i++) {
                ArrayList arrayList3 = new ArrayList();
                for (Point next : ((LineString) rings.get(i)).getPoints()) {
                    arrayList3.add(toLatLng(next));
                    if (next.hasZ()) {
                        d = Double.valueOf(d == null ? next.getZ().doubleValue() : Math.max(d.doubleValue(), next.getZ().doubleValue()));
                    }
                }
                arrayList2.add(arrayList3);
            }
        }
        polygon2.setPoints(arrayList);
        polygon2.setHoles(arrayList2);
        if (this.polygonOptions != null) {
            polygon2.getFillPaint().setColor(this.polygonOptions.getFillColor());
            polygon2.getOutlinePaint().setColor(this.polygonOptions.getStrokeColor());
            polygon2.getOutlinePaint().setStrokeWidth(this.polygonOptions.getStrokeWidth());
            polygon2.setTitle(this.polygonOptions.getTitle());
        }
        return polygon2;
    }

    public Polygon toCurvePolygon(CurvePolygon curvePolygon) {
        Polygon polygon = new Polygon();
        ArrayList arrayList = new ArrayList();
        List rings = curvePolygon.getRings();
        ArrayList arrayList2 = new ArrayList();
        if (!rings.isEmpty()) {
            Double d = null;
            Curve curve = (Curve) rings.get(0);
            if (curve instanceof CompoundCurve) {
                for (LineString points : ((CompoundCurve) curve).getLineStrings()) {
                    for (Point latLng : points.getPoints()) {
                        arrayList.add(toLatLng(latLng));
                    }
                }
            } else if (curve instanceof LineString) {
                for (Point latLng2 : ((LineString) curve).getPoints()) {
                    arrayList.add(toLatLng(latLng2));
                }
            } else {
                throw new GeoPackageException("Unsupported Curve Type: " + curve.getClass().getSimpleName());
            }
            for (int i = 1; i < rings.size(); i++) {
                Curve curve2 = (Curve) rings.get(i);
                ArrayList arrayList3 = new ArrayList();
                if (curve2 instanceof CompoundCurve) {
                    for (LineString points2 : ((CompoundCurve) curve2).getLineStrings()) {
                        for (Point latLng3 : points2.getPoints()) {
                            arrayList3.add(toLatLng(latLng3));
                        }
                    }
                } else if (curve2 instanceof LineString) {
                    for (Point next : ((LineString) curve2).getPoints()) {
                        arrayList3.add(toLatLng(next));
                        if (next.hasZ()) {
                            d = Double.valueOf(d == null ? next.getZ().doubleValue() : Math.max(d.doubleValue(), next.getZ().doubleValue()));
                        }
                    }
                } else {
                    throw new GeoPackageException("Unsupported Curve Hole Type: " + curve2.getClass().getSimpleName());
                }
                arrayList2.add(arrayList3);
            }
        }
        polygon.setHoles(arrayList2);
        polygon.setPoints(arrayList);
        return polygon;
    }

    public MultiLatLng toLatLngs(MultiPoint multiPoint) {
        MultiLatLng multiLatLng = new MultiLatLng();
        for (Point latLng2 : multiPoint.getPoints()) {
            multiLatLng.add(toLatLng2(latLng2));
        }
        return multiLatLng;
    }

    public List<Polyline> toPolylines(MultiLineString multiLineString) {
        ArrayList arrayList = new ArrayList();
        for (LineString polyline : multiLineString.getLineStrings()) {
            arrayList.add(toPolyline(polyline));
        }
        return arrayList;
    }

    public List<Polygon> toPolygons(MultiPolygon multiPolygon) {
        ArrayList arrayList = new ArrayList();
        for (mil.nga.wkb.geom.Polygon polygon : multiPolygon.getPolygons()) {
            arrayList.add(toPolygon(polygon));
        }
        return arrayList;
    }

    public List<Polyline> toPolylines(CompoundCurve compoundCurve) {
        ArrayList arrayList = new ArrayList();
        new MultiPolylineOptions();
        for (LineString polyline : compoundCurve.getLineStrings()) {
            arrayList.add(toPolyline(polyline));
        }
        return arrayList;
    }

    public List<Polygon> toPolygons(PolyhedralSurface polyhedralSurface) {
        ArrayList arrayList = new ArrayList();
        for (mil.nga.wkb.geom.Polygon polygon : polyhedralSurface.getPolygons()) {
            arrayList.add(toPolygon(polygon));
        }
        return arrayList;
    }

    /* renamed from: org.osmdroid.gpkg.overlay.OsmMapShapeConverter$1 */
    static /* synthetic */ class C13381 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$wkb$geom$GeometryType;

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|(3:25|26|28)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0084 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0090 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                mil.nga.wkb.geom.GeometryType[] r0 = mil.nga.wkb.geom.GeometryType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$mil$nga$wkb$geom$GeometryType = r0
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POINT     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.LINESTRING     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POLYGON     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0033 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTIPOINT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x003e }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTILINESTRING     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0049 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTIPOLYGON     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0054 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.CIRCULARSTRING     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0060 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.COMPOUNDCURVE     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x006c }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.CURVEPOLYGON     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0078 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POLYHEDRALSURFACE     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0084 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.TIN     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0090 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.TRIANGLE     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x009c }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.GEOMETRYCOLLECTION     // Catch:{ NoSuchFieldError -> 0x009c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009c }
                r2 = 13
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009c }
            L_0x009c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.gpkg.overlay.OsmMapShapeConverter.C13381.<clinit>():void");
        }
    }

    public OsmDroidMapShape toShape(Geometry geometry) {
        GeometryType geometryType = geometry.getGeometryType();
        switch (C13381.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometryType.ordinal()]) {
            case 1:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.LAT_LNG, toLatLng((Point) geometry));
            case 2:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.POLYLINE_OPTIONS, toPolyline((LineString) geometry));
            case 3:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.POLYGON_OPTIONS, toPolygon((mil.nga.wkb.geom.Polygon) geometry));
            case 4:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MULTI_LAT_LNG, toLatLngs((MultiPoint) geometry));
            case 5:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MULTI_POLYLINE_OPTIONS, toPolylines((MultiLineString) geometry));
            case 6:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MULTI_POLYGON_OPTIONS, toPolygons((MultiPolygon) geometry));
            case 7:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.POLYLINE_OPTIONS, toPolyline((CircularString) geometry));
            case 8:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MULTI_POLYLINE_OPTIONS, toPolylines((CompoundCurve) geometry));
            case 9:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.POLYGON_OPTIONS, toCurvePolygon((CurvePolygon) geometry));
            case 10:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MULTI_POLYGON_OPTIONS, toPolygons((PolyhedralSurface) geometry));
            case 11:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MULTI_POLYGON_OPTIONS, toPolygons((PolyhedralSurface) (TIN) geometry));
            case 12:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.POLYGON_OPTIONS, toPolygon((Triangle) geometry));
            case 13:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.COLLECTION, toShapes((GeometryCollection) geometry));
            default:
                throw new GeoPackageException("Unsupported Geometry Type: " + geometryType.getName());
        }
    }

    public List<OsmDroidMapShape> toShapes(GeometryCollection<Geometry> geometryCollection) {
        ArrayList arrayList = new ArrayList();
        for (Geometry shape : geometryCollection.getGeometries()) {
            arrayList.add(toShape(shape));
        }
        return arrayList;
    }

    public OsmDroidMapShape addToMap(MapView mapView, Geometry geometry) {
        GeometryType geometryType = geometry.getGeometryType();
        switch (C13381.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometryType.ordinal()]) {
            case 1:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MARKER, addLatLngToMap(mapView, toLatLng2((Point) geometry)));
            case 2:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.POLYLINE, addPolylineToMap(mapView, toPolyline((LineString) geometry)));
            case 3:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.POLYGON, addPolygonToMap(mapView, toPolygon((mil.nga.wkb.geom.Polygon) geometry), this.polygonOptions));
            case 4:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MULTI_MARKER, addLatLngsToMap(mapView, toLatLngs((MultiPoint) geometry)));
            case 5:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MULTI_POLYLINE, addPolylinesToMap(mapView, toPolylines((MultiLineString) geometry)));
            case 6:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MULTI_POLYGON, addPolygonsToMap(mapView, toPolygons((MultiPolygon) geometry), this.polygonOptions));
            case 7:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.POLYLINE, addPolylineToMap(mapView, toPolyline((CircularString) geometry)));
            case 8:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MULTI_POLYLINE, addPolylinesToMap(mapView, toPolylines((CompoundCurve) geometry)));
            case 9:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.POLYGON, addPolygonToMap(mapView, toCurvePolygon((CurvePolygon) geometry), this.polygonOptions));
            case 10:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MULTI_POLYGON, addPolygonsToMap(mapView, toPolygons((PolyhedralSurface) geometry), this.polygonOptions));
            case 11:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.MULTI_POLYGON, addPolygonsToMap(mapView, toPolygons((PolyhedralSurface) (TIN) geometry), this.polygonOptions));
            case 12:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.POLYGON, addPolygonToMap(mapView, toPolygon((Triangle) geometry), this.polygonOptions));
            case 13:
                return new OsmDroidMapShape(geometryType, OsmMapShapeType.COLLECTION, addToMap(mapView, (GeometryCollection<Geometry>) (GeometryCollection) geometry));
            default:
                throw new GeoPackageException("Unsupported Geometry Type: " + geometryType.getName());
        }
    }

    public static Marker addLatLngToMap(MapView mapView, GeoPoint geoPoint) {
        return addLatLngToMap(mapView, geoPoint, new MarkerOptions());
    }

    public static Marker addLatLngToMap(MapView mapView, GeoPoint geoPoint, MarkerOptions markerOptions) {
        Marker marker = new Marker(mapView);
        marker.setPosition(geoPoint);
        if (markerOptions != null) {
            if (markerOptions.getIcon() != null) {
                marker.setIcon(markerOptions.getIcon());
            }
            marker.setAlpha(markerOptions.getAlpha());
            marker.setTitle(markerOptions.getTitle());
            marker.setSubDescription(markerOptions.getSubdescription());
            marker.setInfoWindow(new BasicInfoWindow(C1334R.layout.bonuspack_bubble, mapView));
        }
        mapView.getOverlayManager().add(marker);
        return marker;
    }

    public static Polyline addPolylineToMap(MapView mapView, Polyline polyline) {
        if (polyline.getInfoWindow() == null) {
            polyline.setInfoWindow(new BasicInfoWindow(C1334R.layout.bonuspack_bubble, mapView));
        }
        mapView.getOverlayManager().add(polyline);
        return polyline;
    }

    public static Polygon addPolygonToMap(MapView mapView, List<GeoPoint> list, List<List<GeoPoint>> list2, PolygonOptions polygonOptions2) {
        Polygon polygon = new Polygon(mapView);
        polygon.setPoints(list);
        polygon.getHoles().addAll(list2);
        if (polygonOptions2 != null) {
            polygon.getFillPaint().setColor(polygonOptions2.getFillColor());
            polygon.setTitle(polygonOptions2.getTitle());
            polygon.getOutlinePaint().setColor(polygonOptions2.getStrokeColor());
            polygon.getOutlinePaint().setStrokeWidth(polygonOptions2.getStrokeWidth());
            polygon.setSubDescription(polygonOptions2.getSubtitle());
            polygon.setInfoWindow(new BasicInfoWindow(C1334R.layout.bonuspack_bubble, mapView));
        }
        mapView.getOverlayManager().add(polygon);
        return polygon;
    }

    public static Polygon addPolygonToMap(MapView mapView, Polygon polygon, PolygonOptions polygonOptions2) {
        if (polygonOptions2 != null) {
            polygon.getFillPaint().setColor(polygonOptions2.getFillColor());
            polygon.setTitle(polygonOptions2.getTitle());
            polygon.getOutlinePaint().setColor(polygonOptions2.getStrokeColor());
            polygon.getOutlinePaint().setStrokeWidth(polygonOptions2.getStrokeWidth());
            polygon.setSubDescription(polygonOptions2.getSubtitle());
            polygon.setInfoWindow(new BasicInfoWindow(C1334R.layout.bonuspack_bubble, mapView));
        }
        mapView.getOverlayManager().add(polygon);
        return polygon;
    }

    public static MultiMarker addLatLngsToMap(MapView mapView, MultiLatLng multiLatLng) {
        MultiMarker multiMarker = new MultiMarker();
        for (GeoPoint addLatLngToMap : multiLatLng.getLatLngs()) {
            multiMarker.add(addLatLngToMap(mapView, addLatLngToMap, multiLatLng.getMarkerOptions()));
        }
        return multiMarker;
    }

    public static MultiPolyline addPolylinesToMap(MapView mapView, List<Polyline> list) {
        MultiPolyline multiPolyline = new MultiPolyline();
        for (Polyline next : list) {
            if (next.getInfoWindow() == null) {
                next.setInfoWindow(new BasicInfoWindow(C1334R.layout.bonuspack_bubble, mapView));
            }
            mapView.getOverlayManager().add(next);
            multiPolyline.add(next);
        }
        return multiPolyline;
    }

    public static org.osmdroid.gpkg.overlay.features.MultiPolygon addPolygonsToMap(MapView mapView, List<Polygon> list, PolygonOptions polygonOptions2) {
        org.osmdroid.gpkg.overlay.features.MultiPolygon multiPolygon = new org.osmdroid.gpkg.overlay.features.MultiPolygon();
        for (Polygon next : list) {
            Polygon addPolygonToMap = addPolygonToMap(mapView, next.getActualPoints(), next.getHoles(), polygonOptions2);
            if (addPolygonToMap.getInfoWindow() == null) {
                addPolygonToMap.setInfoWindow(new BasicInfoWindow(C1334R.layout.bonuspack_bubble, mapView));
            }
            multiPolygon.add(addPolygonToMap);
        }
        return multiPolygon;
    }

    public List<OsmDroidMapShape> addToMap(MapView mapView, GeometryCollection<Geometry> geometryCollection) {
        ArrayList arrayList = new ArrayList();
        for (Geometry addToMap : geometryCollection.getGeometries()) {
            arrayList.add(addToMap(mapView, addToMap));
        }
        return arrayList;
    }
}
