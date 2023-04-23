package mil.nga.geopackage.features.user;

import java.util.List;
import mil.nga.geopackage.user.UserTable;
import mil.nga.wkb.geom.GeometryType;

public class FeatureTable extends UserTable<FeatureColumn> {
    private final int geometryIndex;

    public FeatureTable(String str, List<FeatureColumn> list) {
        super(str, list);
        Integer num = null;
        for (FeatureColumn next : list) {
            if (next.isGeometry()) {
                duplicateCheck(next.getIndex(), num, GeometryType.GEOMETRY.name());
                num = Integer.valueOf(next.getIndex());
            }
        }
        missingCheck(num, GeometryType.GEOMETRY.name());
        this.geometryIndex = num.intValue();
    }

    public int getGeometryColumnIndex() {
        return this.geometryIndex;
    }

    public FeatureColumn getGeometryColumn() {
        return (FeatureColumn) getColumn(this.geometryIndex);
    }
}
