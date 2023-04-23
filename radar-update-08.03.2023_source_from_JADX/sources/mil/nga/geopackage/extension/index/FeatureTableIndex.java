package mil.nga.geopackage.extension.index;

import android.util.Log;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.misc.TransactionManager;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.features.user.FeatureCursor;
import mil.nga.geopackage.features.user.FeatureDao;
import mil.nga.geopackage.features.user.FeatureRow;
import mil.nga.geopackage.features.user.FeatureRowSync;
import mil.nga.geopackage.projection.Projection;

public class FeatureTableIndex extends FeatureTableCoreIndex {
    /* access modifiers changed from: private */
    public final FeatureDao featureDao;
    private final FeatureRowSync featureRowSync = new FeatureRowSync();

    public void close() {
    }

    public FeatureTableIndex(GeoPackage geoPackage, FeatureDao featureDao2) {
        super(geoPackage, featureDao2.getTableName(), featureDao2.getGeometryColumnName());
        this.featureDao = featureDao2;
    }

    public boolean index(FeatureRow featureRow) {
        TableIndex tableIndex = getTableIndex();
        if (tableIndex != null) {
            boolean index = index(tableIndex, featureRow.getId(), featureRow.getGeometry());
            updateLastIndexed();
            return index;
        }
        throw new GeoPackageException("GeoPackage table is not indexed. GeoPackage: " + getGeoPackage().getName() + ", Table: " + getTableName());
    }

    /* access modifiers changed from: protected */
    public int indexTable(final TableIndex tableIndex) {
        try {
            return ((Integer) TransactionManager.callInTransaction(getGeoPackage().getDatabase().getConnectionSource(), new Callable<Integer>() {
                public Integer call() throws Exception {
                    int access$100 = FeatureTableIndex.this.indexRows(tableIndex, (FeatureCursor) FeatureTableIndex.this.featureDao.queryForAll());
                    if (FeatureTableIndex.this.progress == null || FeatureTableIndex.this.progress.isActive()) {
                        FeatureTableIndex.this.updateLastIndexed();
                    }
                    return Integer.valueOf(access$100);
                }
            })).intValue();
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to Index Table. GeoPackage: " + getGeoPackage().getName() + ", Table: " + getTableName(), e);
        }
    }

    /* access modifiers changed from: private */
    public int indexRows(TableIndex tableIndex, FeatureCursor featureCursor) {
        int i = 0;
        while (true) {
            try {
                if ((this.progress == null || this.progress.isActive()) && featureCursor.moveToNext()) {
                    FeatureRow featureRow = (FeatureRow) featureCursor.getRow();
                    if (featureRow.isValid()) {
                        if (index(tableIndex, featureRow.getId(), featureRow.getGeometry())) {
                            i++;
                        }
                        if (this.progress != null) {
                            this.progress.addProgress(1);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("FeatureTableIndex", "Failed to index feature. Table: " + tableIndex.getTableName() + ", Position: " + featureCursor.getPosition(), e);
            } catch (Throwable th) {
                featureCursor.close();
                throw th;
            }
        }
        featureCursor.close();
        return i;
    }

    public int deleteIndex(FeatureRow featureRow) {
        return deleteIndex(featureRow.getId());
    }

    public CloseableIterator<GeometryIndex> query(BoundingBox boundingBox, Projection projection) {
        return query(getFeatureBoundingBox(boundingBox, projection));
    }

    public long count(BoundingBox boundingBox, Projection projection) {
        return count(getFeatureBoundingBox(boundingBox, projection));
    }

    private BoundingBox getFeatureBoundingBox(BoundingBox boundingBox, Projection projection) {
        return projection.getTransformation(this.featureDao.getProjection()).transform(boundingBox);
    }

    /* JADX INFO: finally extract failed */
    public FeatureRow getFeatureRow(GeometryIndex geometryIndex) {
        long geomId = geometryIndex.getGeomId();
        FeatureRow featureRow = (FeatureRow) this.featureRowSync.getRowOrLock(geomId);
        if (featureRow != null) {
            return featureRow;
        }
        try {
            FeatureRow featureRow2 = (FeatureRow) this.featureDao.queryForIdRow(geomId);
            this.featureRowSync.setRow(geomId, featureRow2);
            return featureRow2;
        } catch (Throwable th) {
            this.featureRowSync.setRow(geomId, featureRow);
            throw th;
        }
    }
}
