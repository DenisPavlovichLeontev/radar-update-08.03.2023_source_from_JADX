package mil.nga.geopackage.p009db.metadata;

import android.content.ContentValues;
import android.database.Cursor;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.GeoPackageDatabase;
import mil.nga.wkb.geom.GeometryEnvelope;

/* renamed from: mil.nga.geopackage.db.metadata.GeometryMetadataDataSource */
public class GeometryMetadataDataSource {

    /* renamed from: db */
    private GeoPackageDatabase f343db;

    public GeometryMetadataDataSource(GeoPackageMetadataDb geoPackageMetadataDb) {
        this.f343db = geoPackageMetadataDb.getDb();
    }

    GeometryMetadataDataSource(GeoPackageDatabase geoPackageDatabase) {
        this.f343db = geoPackageDatabase;
    }

    public long create(GeometryMetadata geometryMetadata) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("geopackage_id", Long.valueOf(geometryMetadata.getGeoPackageId()));
        contentValues.put("table_name", geometryMetadata.getTableName());
        contentValues.put("geom_id", Long.valueOf(geometryMetadata.getId()));
        contentValues.put("min_x", Double.valueOf(geometryMetadata.getMinX()));
        contentValues.put("max_x", Double.valueOf(geometryMetadata.getMaxX()));
        contentValues.put("min_y", Double.valueOf(geometryMetadata.getMinY()));
        contentValues.put("max_y", Double.valueOf(geometryMetadata.getMaxY()));
        contentValues.put("min_z", geometryMetadata.getMinZ());
        contentValues.put("max_z", geometryMetadata.getMaxZ());
        contentValues.put("min_m", geometryMetadata.getMinM());
        contentValues.put("max_m", geometryMetadata.getMaxM());
        long insert = this.f343db.insert(GeometryMetadata.TABLE_NAME, (String) null, contentValues);
        if (insert != -1) {
            geometryMetadata.setId(insert);
            return insert;
        }
        throw new GeoPackageException("Failed to insert geometry metadata. GeoPackage Id: " + geometryMetadata.getGeoPackageId() + ", Table Name: " + geometryMetadata.getTableName() + ", Geometry Id: " + geometryMetadata.getId());
    }

    public GeometryMetadata create(String str, String str2, long j, GeometryEnvelope geometryEnvelope) {
        return create(getGeoPackageId(str), str2, j, geometryEnvelope);
    }

    public GeometryMetadata create(long j, String str, long j2, GeometryEnvelope geometryEnvelope) {
        GeometryMetadata populate = populate(j, str, j2, geometryEnvelope);
        create(populate);
        return populate;
    }

    public GeometryMetadata populate(long j, String str, long j2, GeometryEnvelope geometryEnvelope) {
        GeometryMetadata geometryMetadata = new GeometryMetadata();
        geometryMetadata.setGeoPackageId(j);
        geometryMetadata.setTableName(str);
        geometryMetadata.setId(j2);
        geometryMetadata.setMinX(geometryEnvelope.getMinX());
        geometryMetadata.setMaxX(geometryEnvelope.getMaxX());
        geometryMetadata.setMinY(geometryEnvelope.getMinY());
        geometryMetadata.setMaxY(geometryEnvelope.getMaxY());
        if (geometryEnvelope.hasZ()) {
            geometryMetadata.setMinZ(geometryEnvelope.getMinZ());
            geometryMetadata.setMaxZ(geometryEnvelope.getMaxZ());
        }
        if (geometryEnvelope.hasM()) {
            geometryMetadata.setMinM(geometryEnvelope.getMinM());
            geometryMetadata.setMaxM(geometryEnvelope.getMaxM());
        }
        return geometryMetadata;
    }

    public boolean delete(GeometryMetadata geometryMetadata) {
        return delete(geometryMetadata.getGeoPackageId(), geometryMetadata.getTableName(), geometryMetadata.getId());
    }

    public int delete(String str) {
        return delete(getGeoPackageId(str));
    }

    public int delete(long j) {
        return this.f343db.delete(GeometryMetadata.TABLE_NAME, "geopackage_id = ?", new String[]{String.valueOf(j)});
    }

    public int delete(String str, String str2) {
        return delete(getGeoPackageId(str), str2);
    }

    public int delete(long j, String str) {
        return this.f343db.delete(GeometryMetadata.TABLE_NAME, "geopackage_id = ? AND table_name = ?", new String[]{String.valueOf(j), str});
    }

    public boolean delete(String str, String str2, long j) {
        return delete(getGeoPackageId(str), str2, j);
    }

    public boolean delete(long j, String str, long j2) {
        if (this.f343db.delete(GeometryMetadata.TABLE_NAME, "geopackage_id = ? AND table_name = ? AND geom_id = ?", new String[]{String.valueOf(j), str, String.valueOf(j2)}) > 0) {
            return true;
        }
        return false;
    }

    public boolean createOrUpdate(GeometryMetadata geometryMetadata) {
        if (exists(geometryMetadata)) {
            return update(geometryMetadata);
        }
        create(geometryMetadata);
        return true;
    }

    public boolean update(GeometryMetadata geometryMetadata) {
        String[] strArr = {String.valueOf(geometryMetadata.getGeoPackageId()), geometryMetadata.getTableName(), String.valueOf(geometryMetadata.getId())};
        ContentValues contentValues = new ContentValues();
        contentValues.put("min_x", Double.valueOf(geometryMetadata.getMinX()));
        contentValues.put("max_x", Double.valueOf(geometryMetadata.getMaxX()));
        contentValues.put("min_y", Double.valueOf(geometryMetadata.getMinY()));
        contentValues.put("max_y", Double.valueOf(geometryMetadata.getMaxY()));
        contentValues.put("min_z", geometryMetadata.getMinZ());
        contentValues.put("max_z", geometryMetadata.getMaxZ());
        contentValues.put("min_m", geometryMetadata.getMinM());
        contentValues.put("max_m", geometryMetadata.getMaxM());
        if (this.f343db.update(GeometryMetadata.TABLE_NAME, contentValues, "geopackage_id = ? AND table_name = ? AND geom_id = ?", strArr) > 0) {
            return true;
        }
        return false;
    }

    public boolean exists(GeometryMetadata geometryMetadata) {
        return get(geometryMetadata) != null;
    }

    public GeometryMetadata get(GeometryMetadata geometryMetadata) {
        return get(geometryMetadata.getGeoPackageId(), geometryMetadata.getTableName(), geometryMetadata.getId());
    }

    public GeometryMetadata get(String str, String str2, long j) {
        return get(getGeoPackageId(str), str2, j);
    }

    public GeometryMetadata get(long j, String str, long j2) {
        Cursor query = this.f343db.query(GeometryMetadata.TABLE_NAME, GeometryMetadata.COLUMNS, "geopackage_id = ? AND table_name = ? AND geom_id = ?", new String[]{String.valueOf(j), str, String.valueOf(j2)}, (String) null, (String) null, (String) null);
        try {
            return query.moveToNext() ? createGeometryMetadata(query) : null;
        } finally {
            query.close();
        }
    }

    public Cursor query(String str, String str2) {
        return query(getGeoPackageId(str), str2);
    }

    public int count(String str, String str2) {
        return count(getGeoPackageId(str), str2);
    }

    public Cursor query(long j, String str) {
        return this.f343db.query(GeometryMetadata.TABLE_NAME, GeometryMetadata.COLUMNS, "geopackage_id = ? AND table_name = ?", new String[]{String.valueOf(j), str}, (String) null, (String) null, (String) null);
    }

    public int count(long j, String str) {
        Cursor query = query(j, str);
        int count = query.getCount();
        query.close();
        return count;
    }

    public Cursor query(String str, String str2, BoundingBox boundingBox) {
        return query(getGeoPackageId(str), str2, boundingBox);
    }

    public int count(String str, String str2, BoundingBox boundingBox) {
        return count(getGeoPackageId(str), str2, boundingBox);
    }

    public Cursor query(long j, String str, BoundingBox boundingBox) {
        GeometryEnvelope geometryEnvelope = new GeometryEnvelope();
        geometryEnvelope.setMinX(boundingBox.getMinLongitude());
        geometryEnvelope.setMaxX(boundingBox.getMaxLongitude());
        geometryEnvelope.setMinY(boundingBox.getMinLatitude());
        geometryEnvelope.setMaxY(boundingBox.getMaxLatitude());
        return query(j, str, geometryEnvelope);
    }

    public int count(long j, String str, BoundingBox boundingBox) {
        Cursor query = query(j, str, boundingBox);
        int count = query.getCount();
        query.close();
        return count;
    }

    public Cursor query(String str, String str2, GeometryEnvelope geometryEnvelope) {
        return query(getGeoPackageId(str), str2, geometryEnvelope);
    }

    public int count(String str, String str2, GeometryEnvelope geometryEnvelope) {
        return count(getGeoPackageId(str), str2, geometryEnvelope);
    }

    public Cursor query(long j, String str, GeometryEnvelope geometryEnvelope) {
        int i;
        StringBuilder sb = new StringBuilder();
        sb.append("geopackage_id");
        sb.append(" = ? AND ");
        sb.append("table_name");
        sb.append(" = ?");
        sb.append(" AND ");
        sb.append("min_x");
        sb.append(" <= ?");
        sb.append(" AND ");
        sb.append("max_x");
        sb.append(" >= ?");
        sb.append(" AND ");
        sb.append("min_y");
        sb.append(" <= ?");
        sb.append(" AND ");
        sb.append("max_y");
        sb.append(" >= ?");
        int i2 = 8;
        if (geometryEnvelope.hasZ()) {
            sb.append(" AND ");
            sb.append("min_z");
            sb.append(" <= ?");
            sb.append(" AND ");
            sb.append("max_z");
            sb.append(" >= ?");
            i = 8;
        } else {
            i = 6;
        }
        if (geometryEnvelope.hasM()) {
            i += 2;
            sb.append(" AND ");
            sb.append("min_m");
            sb.append(" <= ?");
            sb.append(" AND ");
            sb.append("max_m");
            sb.append(" >= ?");
        }
        String[] strArr = new String[i];
        strArr[0] = String.valueOf(j);
        strArr[1] = str;
        strArr[2] = String.valueOf(geometryEnvelope.getMaxX());
        strArr[3] = String.valueOf(geometryEnvelope.getMinX());
        strArr[4] = String.valueOf(geometryEnvelope.getMaxY());
        strArr[5] = String.valueOf(geometryEnvelope.getMinY());
        if (geometryEnvelope.hasZ()) {
            strArr[6] = String.valueOf(geometryEnvelope.getMaxZ());
            strArr[7] = String.valueOf(geometryEnvelope.getMinZ());
        } else {
            i2 = 6;
        }
        if (geometryEnvelope.hasM()) {
            strArr[i2] = String.valueOf(geometryEnvelope.getMaxM());
            strArr[i2 + 1] = String.valueOf(geometryEnvelope.getMinM());
        }
        return this.f343db.query(GeometryMetadata.TABLE_NAME, GeometryMetadata.COLUMNS, sb.toString(), strArr, (String) null, (String) null, (String) null);
    }

    public int count(long j, String str, GeometryEnvelope geometryEnvelope) {
        Cursor query = query(j, str, geometryEnvelope);
        int count = query.getCount();
        query.close();
        return count;
    }

    public long getGeoPackageId(String str) {
        GeoPackageMetadata geoPackageMetadata = new GeoPackageMetadataDataSource(this.f343db).get(str);
        if (geoPackageMetadata != null) {
            return geoPackageMetadata.getId();
        }
        return -1;
    }

    public static GeometryMetadata createGeometryMetadata(Cursor cursor) {
        GeometryMetadata geometryMetadata = new GeometryMetadata();
        geometryMetadata.setGeoPackageId(cursor.getLong(0));
        geometryMetadata.setTableName(cursor.getString(1));
        geometryMetadata.setId(cursor.getLong(2));
        geometryMetadata.setMinX(cursor.getDouble(3));
        geometryMetadata.setMaxX(cursor.getDouble(4));
        geometryMetadata.setMinY(cursor.getDouble(5));
        geometryMetadata.setMaxY(cursor.getDouble(6));
        if (!cursor.isNull(7)) {
            geometryMetadata.setMinZ(Double.valueOf(cursor.getDouble(7)));
        }
        if (!cursor.isNull(8)) {
            geometryMetadata.setMaxZ(Double.valueOf(cursor.getDouble(8)));
        }
        if (!cursor.isNull(9)) {
            geometryMetadata.setMinM(Double.valueOf(cursor.getDouble(9)));
        }
        if (!cursor.isNull(10)) {
            geometryMetadata.setMaxM(Double.valueOf(cursor.getDouble(10)));
        }
        return geometryMetadata;
    }
}
