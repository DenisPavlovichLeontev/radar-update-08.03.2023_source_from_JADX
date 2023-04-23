package mil.nga.geopackage.p009db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import java.util.Date;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.features.user.FeatureCursor;
import mil.nga.geopackage.features.user.FeatureDao;
import mil.nga.geopackage.features.user.FeatureRow;
import mil.nga.geopackage.features.user.FeatureRowSync;
import mil.nga.geopackage.geom.GeoPackageGeometryData;
import mil.nga.geopackage.p009db.metadata.GeoPackageMetadataDb;
import mil.nga.geopackage.p009db.metadata.GeometryMetadata;
import mil.nga.geopackage.p009db.metadata.GeometryMetadataDataSource;
import mil.nga.geopackage.p009db.metadata.TableMetadata;
import mil.nga.geopackage.p009db.metadata.TableMetadataDataSource;
import mil.nga.geopackage.p010io.GeoPackageProgress;
import mil.nga.geopackage.projection.Projection;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryEnvelope;
import mil.nga.wkb.util.GeometryEnvelopeBuilder;

/* renamed from: mil.nga.geopackage.db.FeatureIndexer */
public class FeatureIndexer {
    private final Context context;

    /* renamed from: db */
    private final GeoPackageMetadataDb f335db;
    private final FeatureDao featureDao;
    private final FeatureRowSync featureRowSync = new FeatureRowSync();
    private final GeometryMetadataDataSource geometryMetadataDataSource;
    private GeoPackageProgress progress;

    public FeatureIndexer(Context context2, FeatureDao featureDao2) {
        this.context = context2;
        this.featureDao = featureDao2;
        GeoPackageMetadataDb geoPackageMetadataDb = new GeoPackageMetadataDb(context2);
        this.f335db = geoPackageMetadataDb;
        geoPackageMetadataDb.open();
        this.geometryMetadataDataSource = new GeometryMetadataDataSource(geoPackageMetadataDb);
    }

    public void close() {
        this.f335db.close();
    }

    public void setProgress(GeoPackageProgress geoPackageProgress) {
        this.progress = geoPackageProgress;
    }

    public int index() {
        return index(false);
    }

    public int index(boolean z) {
        if (z || !isIndexed()) {
            return indexTable();
        }
        return 0;
    }

    public boolean index(FeatureRow featureRow) {
        long geoPackageId = this.geometryMetadataDataSource.getGeoPackageId(this.featureDao.getDatabase());
        boolean index = index(geoPackageId, featureRow, true);
        updateLastIndexed(this.f335db, geoPackageId);
        return index;
    }

    private int indexTable() {
        TableMetadata orCreate = new TableMetadataDataSource(this.f335db).getOrCreate(this.featureDao.getDatabase(), this.featureDao.getTableName());
        this.geometryMetadataDataSource.delete(this.featureDao.getDatabase(), this.featureDao.getTableName());
        FeatureCursor featureCursor = (FeatureCursor) this.featureDao.queryForAll();
        int i = 0;
        while (true) {
            try {
                GeoPackageProgress geoPackageProgress = this.progress;
                if ((geoPackageProgress == null || geoPackageProgress.isActive()) && featureCursor.moveToNext()) {
                    if (index(orCreate.getGeoPackageId(), (FeatureRow) featureCursor.getRow(), false)) {
                        i++;
                    }
                    GeoPackageProgress geoPackageProgress2 = this.progress;
                    if (geoPackageProgress2 != null) {
                        geoPackageProgress2.addProgress(1);
                    }
                }
            } catch (Exception e) {
                Log.e("FeatureIndexer", "Failed to index feature. Table: " + this.featureDao.getTableName() + ", Position: " + featureCursor.getPosition(), e);
            } catch (Throwable th) {
                featureCursor.close();
                throw th;
            }
        }
        featureCursor.close();
        GeoPackageProgress geoPackageProgress3 = this.progress;
        if (geoPackageProgress3 == null || geoPackageProgress3.isActive()) {
            updateLastIndexed(this.f335db, orCreate.getGeoPackageId());
        }
        return i;
    }

    private boolean index(long j, FeatureRow featureRow, boolean z) {
        Geometry geometry;
        GeoPackageGeometryData geometry2 = featureRow.getGeometry();
        if (geometry2 != null) {
            GeometryEnvelope envelope = geometry2.getEnvelope();
            if (envelope == null && (geometry = geometry2.getGeometry()) != null) {
                envelope = GeometryEnvelopeBuilder.buildEnvelope(geometry);
            }
            GeometryEnvelope geometryEnvelope = envelope;
            if (geometryEnvelope != null) {
                GeometryMetadata populate = this.geometryMetadataDataSource.populate(j, this.featureDao.getTableName(), featureRow.getId(), geometryEnvelope);
                if (z) {
                    this.geometryMetadataDataSource.createOrUpdate(populate);
                } else {
                    this.geometryMetadataDataSource.create(populate);
                }
                return true;
            }
        }
        return false;
    }

    private void updateLastIndexed(GeoPackageMetadataDb geoPackageMetadataDb, long j) {
        long time = new Date().getTime();
        if (!new TableMetadataDataSource(geoPackageMetadataDb).updateLastIndexed(j, this.featureDao.getTableName(), time)) {
            throw new GeoPackageException("Failed to update last indexed time. Table: GeoPackage Id: " + j + ", Table: " + this.featureDao.getTableName() + ", Last Indexed: " + time);
        }
    }

    public boolean deleteIndex() {
        return new TableMetadataDataSource(this.f335db).delete(this.featureDao.getDatabase(), this.featureDao.getTableName());
    }

    public boolean deleteIndex(FeatureRow featureRow) {
        return deleteIndex(featureRow.getId());
    }

    public boolean deleteIndex(long j) {
        return this.geometryMetadataDataSource.delete(this.featureDao.getDatabase(), this.featureDao.getTableName(), j);
    }

    public boolean isIndexed() {
        Date lastIndexed = getLastIndexed();
        if (lastIndexed == null) {
            return false;
        }
        Date lastChange = this.featureDao.getGeometryColumns().getContents().getLastChange();
        if (lastIndexed.equals(lastChange) || lastIndexed.after(lastChange)) {
            return true;
        }
        return false;
    }

    public Date getLastIndexed() {
        Long lastIndexed;
        TableMetadata tableMetadata = new TableMetadataDataSource(this.f335db).get(this.featureDao.getDatabase(), this.featureDao.getTableName());
        if (tableMetadata == null || (lastIndexed = tableMetadata.getLastIndexed()) == null) {
            return null;
        }
        return new Date(lastIndexed.longValue());
    }

    public Cursor query() {
        return this.geometryMetadataDataSource.query(this.featureDao.getDatabase(), this.featureDao.getTableName());
    }

    public int count() {
        return this.geometryMetadataDataSource.count(this.featureDao.getDatabase(), this.featureDao.getTableName());
    }

    public Cursor query(BoundingBox boundingBox) {
        return this.geometryMetadataDataSource.query(this.featureDao.getDatabase(), this.featureDao.getTableName(), boundingBox);
    }

    public int count(BoundingBox boundingBox) {
        return this.geometryMetadataDataSource.count(this.featureDao.getDatabase(), this.featureDao.getTableName(), boundingBox);
    }

    public Cursor query(GeometryEnvelope geometryEnvelope) {
        return this.geometryMetadataDataSource.query(this.featureDao.getDatabase(), this.featureDao.getTableName(), geometryEnvelope);
    }

    public int count(GeometryEnvelope geometryEnvelope) {
        return this.geometryMetadataDataSource.count(this.featureDao.getDatabase(), this.featureDao.getTableName(), geometryEnvelope);
    }

    public Cursor query(BoundingBox boundingBox, Projection projection) {
        return query(getFeatureBoundingBox(boundingBox, projection));
    }

    public long count(BoundingBox boundingBox, Projection projection) {
        return (long) count(getFeatureBoundingBox(boundingBox, projection));
    }

    private BoundingBox getFeatureBoundingBox(BoundingBox boundingBox, Projection projection) {
        return projection.getTransformation(this.featureDao.getProjection()).transform(boundingBox);
    }

    public GeometryMetadata getGeometryMetadata(Cursor cursor) {
        return GeometryMetadataDataSource.createGeometryMetadata(cursor);
    }

    public FeatureRow getFeatureRow(Cursor cursor) {
        return getFeatureRow(getGeometryMetadata(cursor));
    }

    /* JADX INFO: finally extract failed */
    public FeatureRow getFeatureRow(GeometryMetadata geometryMetadata) {
        long id = geometryMetadata.getId();
        FeatureRow featureRow = (FeatureRow) this.featureRowSync.getRowOrLock(id);
        if (featureRow != null) {
            return featureRow;
        }
        try {
            FeatureRow featureRow2 = (FeatureRow) this.featureDao.queryForIdRow(id);
            this.featureRowSync.setRow(id, featureRow2);
            return featureRow2;
        } catch (Throwable th) {
            this.featureRowSync.setRow(id, featureRow);
            throw th;
        }
    }
}
