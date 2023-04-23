package org.osmdroid.gpkg.overlay.features;

import java.util.List;
import mil.nga.wkb.geom.GeometryType;

public class OsmDroidMapShape {
    private GeometryType geometryType;
    private Object shape;
    private OsmMapShapeType shapeType;

    public void setVisible(boolean z) {
    }

    public OsmDroidMapShape(GeometryType geometryType2, OsmMapShapeType osmMapShapeType, Object obj) {
        this.geometryType = geometryType2;
        this.shapeType = osmMapShapeType;
        this.shape = obj;
    }

    public GeometryType getGeometryType() {
        return this.geometryType;
    }

    public void setGeometryType(GeometryType geometryType2) {
        this.geometryType = geometryType2;
    }

    public OsmMapShapeType getShapeType() {
        return this.shapeType;
    }

    public void setShapeType(OsmMapShapeType osmMapShapeType) {
        this.shapeType = osmMapShapeType;
    }

    public Object getShape() {
        return this.shape;
    }

    public void setShape(Object obj) {
        this.shape = obj;
    }

    /* renamed from: org.osmdroid.gpkg.overlay.features.OsmDroidMapShape$1 */
    static /* synthetic */ class C13391 {
        static final /* synthetic */ int[] $SwitchMap$org$osmdroid$gpkg$overlay$features$OsmMapShapeType;

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                org.osmdroid.gpkg.overlay.features.OsmMapShapeType[] r0 = org.osmdroid.gpkg.overlay.features.OsmMapShapeType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$osmdroid$gpkg$overlay$features$OsmMapShapeType = r0
                org.osmdroid.gpkg.overlay.features.OsmMapShapeType r1 = org.osmdroid.gpkg.overlay.features.OsmMapShapeType.POLYLINE_MARKERS     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$org$osmdroid$gpkg$overlay$features$OsmMapShapeType     // Catch:{ NoSuchFieldError -> 0x001d }
                org.osmdroid.gpkg.overlay.features.OsmMapShapeType r1 = org.osmdroid.gpkg.overlay.features.OsmMapShapeType.POLYGON_MARKERS     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$org$osmdroid$gpkg$overlay$features$OsmMapShapeType     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.osmdroid.gpkg.overlay.features.OsmMapShapeType r1 = org.osmdroid.gpkg.overlay.features.OsmMapShapeType.MULTI_POLYLINE_MARKERS     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$org$osmdroid$gpkg$overlay$features$OsmMapShapeType     // Catch:{ NoSuchFieldError -> 0x0033 }
                org.osmdroid.gpkg.overlay.features.OsmMapShapeType r1 = org.osmdroid.gpkg.overlay.features.OsmMapShapeType.MULTI_POLYGON_MARKERS     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$org$osmdroid$gpkg$overlay$features$OsmMapShapeType     // Catch:{ NoSuchFieldError -> 0x003e }
                org.osmdroid.gpkg.overlay.features.OsmMapShapeType r1 = org.osmdroid.gpkg.overlay.features.OsmMapShapeType.COLLECTION     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.gpkg.overlay.features.OsmDroidMapShape.C13391.<clinit>():void");
        }
    }

    public boolean isValid() {
        int i = C13391.$SwitchMap$org$osmdroid$gpkg$overlay$features$OsmMapShapeType[this.shapeType.ordinal()];
        boolean z = true;
        if (i == 1) {
            return ((PolylineMarkers) this.shape).isValid();
        }
        if (i == 2) {
            return ((PolygonMarkers) this.shape).isValid();
        }
        if (i == 3) {
            return ((MultiPolylineMarkers) this.shape).isValid();
        }
        if (i == 4) {
            return ((MultiPolygonMarkers) this.shape).isValid();
        }
        if (i != 5) {
            return true;
        }
        for (OsmDroidMapShape isValid : (List) this.shape) {
            z = isValid.isValid();
            if (!z) {
                return z;
            }
        }
        return z;
    }
}
