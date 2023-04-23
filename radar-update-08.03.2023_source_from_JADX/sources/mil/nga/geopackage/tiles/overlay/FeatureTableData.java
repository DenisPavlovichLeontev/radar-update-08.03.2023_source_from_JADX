package mil.nga.geopackage.tiles.overlay;

import java.util.ArrayList;
import java.util.List;

public class FeatureTableData {
    private long count;
    private String name;
    private List<FeatureRowData> rows;

    public FeatureTableData(String str, long j) {
        this(str, j, (List<FeatureRowData>) null);
    }

    public FeatureTableData(String str, long j, List<FeatureRowData> list) {
        this.name = str;
        this.count = j;
        this.rows = list;
    }

    public String getName() {
        return this.name;
    }

    public long getCount() {
        return this.count;
    }

    public List<FeatureRowData> getRows() {
        return this.rows;
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
        List<FeatureRowData> list = this.rows;
        if (list == null || list.isEmpty()) {
            return Long.valueOf(this.count);
        }
        ArrayList arrayList = new ArrayList();
        for (FeatureRowData jsonCompatible : this.rows) {
            arrayList.add(jsonCompatible.jsonCompatible(z, z2));
        }
        return arrayList;
    }
}
