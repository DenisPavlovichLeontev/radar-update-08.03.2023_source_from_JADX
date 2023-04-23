package org.osmdroid.views.overlay.gridlines;

import android.content.Context;
import androidx.core.view.ViewCompat;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

@Deprecated
public class LatLonGridlineOverlay {
    public static boolean DEBUG = false;
    public static boolean DEBUG2 = false;
    public static int backgroundColor = ViewCompat.MEASURED_STATE_MASK;

    /* renamed from: df */
    static final DecimalFormat f566df = new DecimalFormat("#.#####");
    public static int fontColor = -1;
    public static short fontSizeDp = 24;
    public static int lineColor = ViewCompat.MEASURED_STATE_MASK;
    public static float lineWidth = 1.0f;
    private static float multiplier = 1.0f;

    private static void applyMarkerAttributes(Marker marker) {
        marker.setTextLabelBackgroundColor(backgroundColor);
        marker.setTextLabelFontSize(fontSizeDp);
        marker.setTextLabelForegroundColor(fontColor);
    }

    public static FolderOverlay getLatLonGrid(Context context, MapView mapView) {
        boolean z;
        double d;
        double d2;
        double d3;
        double d4;
        String str;
        MapView mapView2 = mapView;
        BoundingBox boundingBox = mapView.getBoundingBox();
        int zoomLevel = mapView.getZoomLevel();
        if (DEBUG) {
            System.out.println("######### getLatLonGrid ");
        }
        FolderOverlay folderOverlay = new FolderOverlay();
        if (zoomLevel >= 2) {
            double latNorth = boundingBox.getLatNorth();
            double latSouth = boundingBox.getLatSouth();
            double lonEast = boundingBox.getLonEast();
            double lonWest = boundingBox.getLonWest();
            if (latNorth < latSouth) {
                return folderOverlay;
            }
            if (DEBUG) {
                PrintStream printStream = System.out;
                printStream.println("N " + latNorth + " S " + latSouth + ", " + 0.0d);
            }
            boolean z2 = lonEast < 0.0d && lonWest > 0.0d;
            if (DEBUG) {
                PrintStream printStream2 = System.out;
                StringBuilder sb = new StringBuilder();
                z = z2;
                sb.append("delta ");
                sb.append(0.0d);
                printStream2.println(sb.toString());
            } else {
                z = z2;
            }
            double incrementor = getIncrementor(zoomLevel);
            double[] startEndPointsNS = getStartEndPointsNS(latNorth, latSouth, zoomLevel);
            double d5 = startEndPointsNS[0];
            double d6 = startEndPointsNS[1];
            double d7 = d5;
            while (true) {
                d = latSouth;
                d2 = latNorth;
                if (d7 > d6) {
                    break;
                }
                Polyline polyline = new Polyline();
                double d8 = incrementor;
                polyline.getOutlinePaint().setStrokeWidth(lineWidth);
                polyline.getOutlinePaint().setColor(lineColor);
                ArrayList arrayList = new ArrayList();
                arrayList.add(new GeoPoint(d7, lonEast));
                arrayList.add(new GeoPoint(d7, lonWest));
                if (DEBUG) {
                    PrintStream printStream3 = System.out;
                    printStream3.println("drawing NS " + d7 + "," + lonEast + " to " + d7 + "," + lonWest + ", zoom " + zoomLevel);
                }
                polyline.setPoints(arrayList);
                folderOverlay.add(polyline);
                MapView mapView3 = mapView;
                Marker marker = new Marker(mapView3);
                applyMarkerAttributes(marker);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(f566df.format(d7));
                sb2.append(d7 > 0.0d ? "N" : "S");
                String sb3 = sb2.toString();
                marker.setTitle(sb3);
                marker.setTextIcon(sb3);
                marker.setPosition(new GeoPoint(d7, lonWest + d8));
                folderOverlay.add(marker);
                d7 += d8;
                mapView2 = mapView3;
                latSouth = d;
                latNorth = d2;
                incrementor = d8;
            }
            MapView mapView4 = mapView2;
            double d9 = incrementor;
            double[] startEndPointsWE = getStartEndPointsWE(lonWest, lonEast, zoomLevel);
            double d10 = startEndPointsWE[1];
            double d11 = startEndPointsWE[0];
            double d12 = d10;
            while (d12 <= d11) {
                Polyline polyline2 = new Polyline();
                String str2 = "E";
                polyline2.getOutlinePaint().setStrokeWidth(lineWidth);
                polyline2.getOutlinePaint().setColor(lineColor);
                ArrayList arrayList2 = new ArrayList();
                double d13 = d11;
                double d14 = d2;
                arrayList2.add(new GeoPoint(d14, d12));
                double d15 = d10;
                double d16 = d;
                arrayList2.add(new GeoPoint(d16, d12));
                polyline2.setPoints(arrayList2);
                if (DEBUG) {
                    PrintStream printStream4 = System.err;
                    StringBuilder sb4 = new StringBuilder();
                    str = "W";
                    sb4.append("drawing EW ");
                    sb4.append(d16);
                    sb4.append(",");
                    sb4.append(d12);
                    sb4.append(" to ");
                    sb4.append(d14);
                    sb4.append(",");
                    sb4.append(d12);
                    sb4.append(", zoom ");
                    sb4.append(zoomLevel);
                    printStream4.println(sb4.toString());
                } else {
                    str = "W";
                }
                folderOverlay.add(polyline2);
                Marker marker2 = new Marker(mapView4);
                applyMarkerAttributes(marker2);
                marker2.setRotation(-90.0f);
                StringBuilder sb5 = new StringBuilder();
                sb5.append(f566df.format(d12));
                sb5.append(d12 > 0.0d ? str2 : str);
                String sb6 = sb5.toString();
                marker2.setTitle(sb6);
                marker2.setTextIcon(sb6);
                marker2.setPosition(new GeoPoint(d16 + d9, d12));
                folderOverlay.add(marker2);
                d12 += d9;
                d = d16;
                d10 = d15;
                d2 = d14;
                d11 = d13;
            }
            String str3 = "E";
            double d17 = d11;
            double d18 = d2;
            double d19 = d10;
            double d20 = d;
            String str4 = "W";
            if (z) {
                if (DEBUG) {
                    PrintStream printStream5 = System.out;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("DATELINE zoom ");
                    sb7.append(zoomLevel);
                    sb7.append(" ");
                    sb7.append(d19);
                    sb7.append(" ");
                    d3 = d17;
                    sb7.append(d3);
                    printStream5.println(sb7.toString());
                } else {
                    d3 = d17;
                }
                double d21 = d19;
                while (d21 <= 180.0d) {
                    Polyline polyline3 = new Polyline();
                    polyline3.getOutlinePaint().setStrokeWidth(lineWidth);
                    polyline3.getOutlinePaint().setColor(lineColor);
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.add(new GeoPoint(d18, d21));
                    arrayList3.add(new GeoPoint(d20, d21));
                    polyline3.setPoints(arrayList3);
                    if (DEBUG2) {
                        PrintStream printStream6 = System.out;
                        StringBuilder sb8 = new StringBuilder();
                        d4 = d3;
                        sb8.append("DATELINE drawing NS");
                        sb8.append(d20);
                        sb8.append(",");
                        sb8.append(d21);
                        sb8.append(" to ");
                        sb8.append(d18);
                        sb8.append(",");
                        sb8.append(d21);
                        sb8.append(", zoom ");
                        sb8.append(zoomLevel);
                        printStream6.println(sb8.toString());
                    } else {
                        d4 = d3;
                    }
                    folderOverlay.add(polyline3);
                    d21 += d9;
                    MapView mapView5 = mapView;
                    d3 = d4;
                }
                double d22 = d3;
                double d23 = -180.0d;
                while (d23 <= d22) {
                    Polyline polyline4 = new Polyline();
                    polyline4.getOutlinePaint().setStrokeWidth(lineWidth);
                    polyline4.getOutlinePaint().setColor(lineColor);
                    ArrayList arrayList4 = new ArrayList();
                    arrayList4.add(new GeoPoint(d18, d23));
                    arrayList4.add(new GeoPoint(d20, d23));
                    polyline4.setPoints(arrayList4);
                    if (DEBUG2) {
                        PrintStream printStream7 = System.out;
                        printStream7.println("DATELINE drawing EW" + d20 + "," + d23 + " to " + d18 + "," + d23 + ", zoom " + zoomLevel);
                    }
                    folderOverlay.add(polyline4);
                    Marker marker3 = new Marker(mapView);
                    applyMarkerAttributes(marker3);
                    marker3.setRotation(-90.0f);
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(f566df.format(d23));
                    sb9.append(d23 > 0.0d ? str3 : str4);
                    String sb10 = sb9.toString();
                    marker3.setTitle(sb10);
                    marker3.setTextIcon(sb10);
                    marker3.setPosition(new GeoPoint(d20 + d9, d23));
                    folderOverlay.add(marker3);
                    d23 += d9;
                }
                MapView mapView6 = mapView;
                double d24 = d19;
                while (d24 < 180.0d) {
                    Marker marker4 = new Marker(mapView6);
                    applyMarkerAttributes(marker4);
                    marker4.setRotation(-90.0f);
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append(f566df.format(d24));
                    sb11.append(d24 > 0.0d ? str3 : str4);
                    String sb12 = sb11.toString();
                    marker4.setTitle(sb12);
                    marker4.setTextIcon(sb12);
                    marker4.setPosition(new GeoPoint(d20 + d9, d24));
                    folderOverlay.add(marker4);
                    d24 += d9;
                }
            }
        }
        return folderOverlay;
    }

    private static double[] getStartEndPointsNS(double d, double d2, int i) {
        int i2 = i;
        double d3 = 90.0d;
        double d4 = -90.0d;
        if (i2 < 10) {
            double floor = Math.floor(d2);
            double incrementor = getIncrementor(i);
            double d5 = -90.0d;
            while (d5 < floor) {
                d5 += incrementor;
            }
            double d6 = 90.0d;
            while (d6 > Math.ceil(d)) {
                d6 -= incrementor;
            }
            if (d6 <= 90.0d) {
                d3 = d6;
            }
            if (d5 >= -90.0d) {
                d4 = d5;
            }
            return new double[]{d4, d3};
        }
        if (d2 > 0.0d) {
            d4 = 0.0d;
        }
        if (d < 0.0d) {
            d3 = 0.0d;
        }
        for (int i3 = 2; i3 <= i2; i3++) {
            double incrementor2 = getIncrementor(i3);
            while (d4 < d2 - incrementor2) {
                d4 += incrementor2;
                if (DEBUG) {
                    System.out.println("south " + d4);
                }
            }
            while (d3 > d + incrementor2) {
                d3 -= incrementor2;
                if (DEBUG) {
                    System.out.println("north " + d3);
                }
            }
        }
        return new double[]{d4, d3};
    }

    private static double[] getStartEndPointsWE(double d, double d2, int i) {
        int i2 = i;
        double incrementor = getIncrementor(i);
        double d3 = 180.0d;
        double d4 = -180.0d;
        if (i2 < 10) {
            double d5 = 180.0d;
            while (d5 > Math.floor(d)) {
                d5 -= incrementor;
            }
            double ceil = Math.ceil(d2);
            for (double d6 = -180.0d; d6 < ceil; d6 += incrementor) {
            }
            if (d5 >= -180.0d) {
                d4 = d5;
            }
            if (ceil <= 180.0d) {
                d3 = ceil;
            }
            return new double[]{d3, d4};
        }
        if (d > 0.0d) {
            d4 = 0.0d;
        }
        if (d2 < 0.0d) {
            d3 = 0.0d;
        }
        for (int i3 = 2; i3 <= i2; i3++) {
            double incrementor2 = getIncrementor(i3);
            while (d3 > d2 + incrementor2) {
                d3 -= incrementor2;
            }
            while (d4 < d - incrementor2) {
                d4 += incrementor2;
                if (DEBUG) {
                    System.out.println("west " + d4);
                }
            }
        }
        if (DEBUG) {
            System.out.println("return EW set as " + d4 + " " + d3);
        }
        return new double[]{d3, d4};
    }

    private static double getIncrementor(int i) {
        double d;
        float f;
        switch (i) {
            case 0:
            case 1:
                d = 30.0d;
                f = multiplier;
                break;
            case 2:
                d = 15.0d;
                f = multiplier;
                break;
            case 3:
                d = 9.0d;
                f = multiplier;
                break;
            case 4:
                d = 6.0d;
                f = multiplier;
                break;
            case 5:
                d = 3.0d;
                f = multiplier;
                break;
            case 6:
                d = 2.0d;
                f = multiplier;
                break;
            case 7:
                d = 1.0d;
                f = multiplier;
                break;
            case 8:
                d = 0.5d;
                f = multiplier;
                break;
            case 9:
                d = 0.25d;
                f = multiplier;
                break;
            case 10:
                d = 0.1d;
                f = multiplier;
                break;
            case 11:
                d = 0.05d;
                f = multiplier;
                break;
            case 12:
                d = 0.025d;
                f = multiplier;
                break;
            case 13:
                d = 0.0125d;
                f = multiplier;
                break;
            case 14:
                d = 0.00625d;
                f = multiplier;
                break;
            case 15:
                d = 0.003125d;
                f = multiplier;
                break;
            case 16:
                d = 0.0015625d;
                f = multiplier;
                break;
            case 17:
                d = 7.8125E-4d;
                f = multiplier;
                break;
            case 18:
                d = 3.90625E-4d;
                f = multiplier;
                break;
            case 19:
                d = 1.953125E-4d;
                f = multiplier;
                break;
            case 20:
                d = 9.765625E-5d;
                f = multiplier;
                break;
            case 21:
                d = 4.8828125E-5d;
                f = multiplier;
                break;
            default:
                d = 2.44140625E-5d;
                f = multiplier;
                break;
        }
        return ((double) f) * d;
    }

    public static void setDefaults() {
        lineColor = ViewCompat.MEASURED_STATE_MASK;
        fontColor = -1;
        backgroundColor = ViewCompat.MEASURED_STATE_MASK;
        lineWidth = 1.0f;
        fontSizeDp = 32;
        DEBUG = false;
        DEBUG2 = false;
    }
}
