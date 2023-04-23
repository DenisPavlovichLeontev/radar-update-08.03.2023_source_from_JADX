package mil.nga.geopackage.features.user;

import android.database.Cursor;
import java.util.List;
import mil.nga.geopackage.geom.GeoPackageGeometryData;
import mil.nga.geopackage.user.UserCursor;
import mil.nga.geopackage.user.UserDao;
import mil.nga.geopackage.user.UserInvalidCursor;

public class FeatureCursor extends UserCursor<FeatureColumn, FeatureTable, FeatureRow> {
    public FeatureCursor(FeatureTable featureTable, Cursor cursor) {
        super(featureTable, cursor);
    }

    public FeatureRow getRow(int[] iArr, Object[] objArr) {
        return new FeatureRow((FeatureTable) getTable(), iArr, objArr);
    }

    public Object getValue(FeatureColumn featureColumn) {
        if (featureColumn.isGeometry()) {
            return getGeometry();
        }
        return super.getValue(featureColumn);
    }

    public GeoPackageGeometryData getGeometry() {
        byte[] blob;
        int geometryColumnIndex = ((FeatureTable) getTable()).getGeometryColumnIndex();
        if (getType(geometryColumnIndex) == 0 || (blob = getBlob(geometryColumnIndex)) == null) {
            return null;
        }
        return new GeoPackageGeometryData(blob);
    }

    public void enableInvalidRequery(FeatureDao featureDao) {
        super.enableInvalidRequery(featureDao);
    }

    /* access modifiers changed from: protected */
    public UserInvalidCursor<FeatureColumn, FeatureTable, FeatureRow, ? extends UserCursor<FeatureColumn, FeatureTable, FeatureRow>, ? extends UserDao<FeatureColumn, FeatureTable, FeatureRow, ? extends UserCursor<FeatureColumn, FeatureTable, FeatureRow>>> createInvalidCursor(UserDao userDao, UserCursor userCursor, List<Integer> list, List<FeatureColumn> list2) {
        return new FeatureInvalidCursor((FeatureDao) userDao, (FeatureCursor) userCursor, list, list2);
    }
}
