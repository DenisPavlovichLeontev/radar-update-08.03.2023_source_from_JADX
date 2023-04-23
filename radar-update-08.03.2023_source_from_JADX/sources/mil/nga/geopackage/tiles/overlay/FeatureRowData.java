package mil.nga.geopackage.tiles.overlay;

import java.util.HashMap;
import java.util.Map;
import mil.nga.geopackage.geom.GeoPackageGeometryData;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryType;
import mil.nga.wkb.util.GeometryJSONCompatible;

public class FeatureRowData {
    private String geometryColumn;
    private Map<String, Object> values;

    public FeatureRowData(Map<String, Object> map, String str) {
        this.values = map;
        this.geometryColumn = str;
    }

    public Map<String, Object> getValues() {
        return this.values;
    }

    public String getGeometryColumn() {
        return this.geometryColumn;
    }

    public GeoPackageGeometryData getGeometryData() {
        return (GeoPackageGeometryData) this.values.get(this.geometryColumn);
    }

    public Geometry getGeometry() {
        return getGeometryData().getGeometry();
    }

    public Object jsonCompatible() {
        return jsonCompatible(true, true);
    }

    public Object jsonCompatibleWithPoints(boolean z) {
        return jsonCompatible(z, false);
    }

    public Object jsonCompatibleWithGeometries(boolean z) {
        return jsonCompatible(z, z);
    }

    public Object jsonCompatible(boolean z, boolean z2) {
        HashMap hashMap = new HashMap();
        for (String next : this.values.keySet()) {
            Object obj = null;
            Object obj2 = this.values.get(next);
            if (next.equals(this.geometryColumn)) {
                GeoPackageGeometryData geoPackageGeometryData = (GeoPackageGeometryData) obj2;
                if (geoPackageGeometryData.getGeometry() == null) {
                    obj = obj2;
                } else if (z2 || (z && geoPackageGeometryData.getGeometry().getGeometryType() == GeometryType.POINT)) {
                    obj = GeometryJSONCompatible.getJSONCompatibleGeometry(geoPackageGeometryData.getGeometry());
                }
                if (obj != null) {
                    hashMap.put(next, obj);
                }
            }
        }
        return hashMap;
    }
}
