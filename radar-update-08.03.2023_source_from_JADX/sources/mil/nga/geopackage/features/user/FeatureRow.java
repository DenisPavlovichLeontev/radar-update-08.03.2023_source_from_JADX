package mil.nga.geopackage.features.user;

import android.content.ContentValues;
import java.io.IOException;
import java.util.Arrays;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.geom.GeoPackageGeometryData;
import mil.nga.geopackage.user.UserRow;

public class FeatureRow extends UserRow<FeatureColumn, FeatureTable> {
    FeatureRow(FeatureTable featureTable, int[] iArr, Object[] objArr) {
        super(featureTable, iArr, objArr);
    }

    FeatureRow(FeatureTable featureTable) {
        super(featureTable);
    }

    public FeatureRow(FeatureRow featureRow) {
        super(featureRow);
    }

    public int getGeometryColumnIndex() {
        return ((FeatureTable) getTable()).getGeometryColumnIndex();
    }

    public FeatureColumn getGeometryColumn() {
        return ((FeatureTable) getTable()).getGeometryColumn();
    }

    public void setValue(int i, Object obj) {
        if (i == getGeometryColumnIndex() && (obj instanceof byte[])) {
            obj = new GeoPackageGeometryData((byte[]) obj);
        }
        super.setValue(i, obj);
    }

    public GeoPackageGeometryData getGeometry() {
        Object value = getValue(getGeometryColumnIndex());
        if (value != null) {
            return (GeoPackageGeometryData) value;
        }
        return null;
    }

    public void setGeometry(GeoPackageGeometryData geoPackageGeometryData) {
        setValue(getGeometryColumnIndex(), geoPackageGeometryData);
    }

    /* access modifiers changed from: protected */
    public Object copyValue(FeatureColumn featureColumn, Object obj) {
        if (!featureColumn.isGeometry() || !(obj instanceof GeoPackageGeometryData)) {
            return super.copyValue(featureColumn, obj);
        }
        try {
            byte[] bytes = ((GeoPackageGeometryData) obj).toBytes();
            return new GeoPackageGeometryData(Arrays.copyOf(bytes, bytes.length));
        } catch (IOException e) {
            throw new GeoPackageException("Failed to copy Geometry Data bytes. column: " + featureColumn.getName(), e);
        }
    }

    /* access modifiers changed from: protected */
    public void columnToContentValue(ContentValues contentValues, FeatureColumn featureColumn, Object obj) {
        if (featureColumn.isGeometry()) {
            String name = featureColumn.getName();
            if (obj instanceof GeoPackageGeometryData) {
                try {
                    contentValues.put(name, ((GeoPackageGeometryData) obj).toBytes());
                } catch (IOException e) {
                    throw new GeoPackageException("Failed to write Geometry Data bytes. column: " + name, e);
                }
            } else if (obj instanceof byte[]) {
                contentValues.put(name, (byte[]) obj);
            } else {
                throw new GeoPackageException("Unsupported update geometry column value type. column: " + name + ", value type: " + obj.getClass().getName());
            }
        } else {
            super.columnToContentValue(contentValues, featureColumn, obj);
        }
    }
}
