package mil.nga.geopackage.features.index;

import android.content.Context;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.index.FeatureTableIndex;
import mil.nga.geopackage.features.user.FeatureDao;
import mil.nga.geopackage.features.user.FeatureRow;
import mil.nga.geopackage.p009db.FeatureIndexer;
import mil.nga.geopackage.p010io.GeoPackageProgress;
import mil.nga.geopackage.projection.Projection;
import mil.nga.wkb.geom.GeometryEnvelope;

public class FeatureIndexManager {
    private final FeatureDao featureDao;
    private final FeatureIndexer featureIndexer;
    private final FeatureTableIndex featureTableIndex;
    private FeatureIndexType indexLocation;
    private Set<FeatureIndexType> indexLocationQueryOrder = new LinkedHashSet();

    public FeatureIndexManager(Context context, GeoPackage geoPackage, FeatureDao featureDao2) {
        this.featureDao = featureDao2;
        this.featureTableIndex = new FeatureTableIndex(geoPackage, featureDao2);
        this.featureIndexer = new FeatureIndexer(context, featureDao2);
        this.indexLocationQueryOrder.add(FeatureIndexType.GEOPACKAGE);
        this.indexLocationQueryOrder.add(FeatureIndexType.METADATA);
    }

    public void close() {
        this.featureTableIndex.close();
        this.featureIndexer.close();
    }

    public FeatureDao getFeatureDao() {
        return this.featureDao;
    }

    public FeatureTableIndex getFeatureTableIndex() {
        return this.featureTableIndex;
    }

    public FeatureIndexer getFeatureIndexer() {
        return this.featureIndexer;
    }

    public FeatureIndexType getIndexLocation() {
        return this.indexLocation;
    }

    public void prioritizeQueryLocation(FeatureIndexType... featureIndexTypeArr) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (FeatureIndexType add : featureIndexTypeArr) {
            linkedHashSet.add(add);
        }
        linkedHashSet.addAll(this.indexLocationQueryOrder);
        this.indexLocationQueryOrder = linkedHashSet;
    }

    public void setIndexLocation(FeatureIndexType featureIndexType) {
        this.indexLocation = featureIndexType;
    }

    public void setProgress(GeoPackageProgress geoPackageProgress) {
        this.featureTableIndex.setProgress(geoPackageProgress);
        this.featureIndexer.setProgress(geoPackageProgress);
    }

    public int index() {
        return index(verifyIndexLocation(), false);
    }

    public int index(List<FeatureIndexType> list) {
        int i = 0;
        for (FeatureIndexType index : list) {
            i = Math.max(i, index(index));
        }
        return i;
    }

    public int index(FeatureIndexType featureIndexType) {
        return index(featureIndexType, false);
    }

    public int index(boolean z) {
        return index(verifyIndexLocation(), z);
    }

    public int index(boolean z, List<FeatureIndexType> list) {
        int i = 0;
        for (FeatureIndexType index : list) {
            i = Math.max(i, index(index, z));
        }
        return i;
    }

    /* renamed from: mil.nga.geopackage.features.index.FeatureIndexManager$1 */
    static /* synthetic */ class C11711 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                mil.nga.geopackage.features.index.FeatureIndexType[] r0 = mil.nga.geopackage.features.index.FeatureIndexType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType = r0
                mil.nga.geopackage.features.index.FeatureIndexType r1 = mil.nga.geopackage.features.index.FeatureIndexType.GEOPACKAGE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.geopackage.features.index.FeatureIndexType r1 = mil.nga.geopackage.features.index.FeatureIndexType.METADATA     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.features.index.FeatureIndexManager.C11711.<clinit>():void");
        }
    }

    public int index(FeatureIndexType featureIndexType, boolean z) {
        if (featureIndexType != null) {
            int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[featureIndexType.ordinal()];
            if (i == 1) {
                return this.featureTableIndex.index(z);
            }
            if (i == 2) {
                return this.featureIndexer.index(z);
            }
            throw new GeoPackageException("Unsupported FeatureIndexType: " + featureIndexType);
        }
        throw new GeoPackageException("FeatureIndexType is required to index");
    }

    public boolean index(FeatureRow featureRow) {
        return index(verifyIndexLocation(), featureRow);
    }

    public boolean index(FeatureRow featureRow, List<FeatureIndexType> list) {
        boolean z = false;
        for (FeatureIndexType index : list) {
            if (index(index, featureRow)) {
                z = true;
            }
        }
        return z;
    }

    public boolean index(FeatureIndexType featureIndexType, FeatureRow featureRow) {
        if (featureIndexType != null) {
            int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[featureIndexType.ordinal()];
            if (i == 1) {
                return this.featureTableIndex.index(featureRow);
            }
            if (i == 2) {
                return this.featureIndexer.index(featureRow);
            }
            throw new GeoPackageException("Unsupported FeatureIndexType: " + featureIndexType);
        }
        throw new GeoPackageException("FeatureIndexType is required to index");
    }

    public boolean deleteIndex() {
        return deleteIndex(verifyIndexLocation());
    }

    public boolean deleteIndex(List<FeatureIndexType> list) {
        boolean z = false;
        for (FeatureIndexType deleteIndex : list) {
            if (deleteIndex(deleteIndex)) {
                z = true;
            }
        }
        return z;
    }

    public boolean deleteIndex(FeatureIndexType featureIndexType) {
        if (featureIndexType != null) {
            int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[featureIndexType.ordinal()];
            if (i == 1) {
                return this.featureTableIndex.deleteIndex();
            }
            if (i == 2) {
                return this.featureIndexer.deleteIndex();
            }
            throw new GeoPackageException("Unsupported FeatureIndexType: " + featureIndexType);
        }
        throw new GeoPackageException("FeatureIndexType is required to delete index");
    }

    public boolean deleteIndex(FeatureRow featureRow) {
        return deleteIndex(verifyIndexLocation(), featureRow);
    }

    public boolean deleteIndex(FeatureRow featureRow, List<FeatureIndexType> list) {
        boolean z = false;
        for (FeatureIndexType deleteIndex : list) {
            if (deleteIndex(deleteIndex, featureRow)) {
                z = true;
            }
        }
        return z;
    }

    public boolean deleteIndex(FeatureIndexType featureIndexType, FeatureRow featureRow) {
        return deleteIndex(featureIndexType, featureRow.getId());
    }

    public boolean deleteIndex(long j) {
        return deleteIndex(verifyIndexLocation(), j);
    }

    public boolean deleteIndex(long j, List<FeatureIndexType> list) {
        boolean z = false;
        for (FeatureIndexType deleteIndex : list) {
            if (deleteIndex(deleteIndex, j)) {
                z = true;
            }
        }
        return z;
    }

    public boolean deleteIndex(FeatureIndexType featureIndexType, long j) {
        if (featureIndexType != null) {
            int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[featureIndexType.ordinal()];
            boolean z = true;
            if (i == 1) {
                if (this.featureTableIndex.deleteIndex(j) <= 0) {
                    z = false;
                }
                return z;
            } else if (i == 2) {
                return this.featureIndexer.deleteIndex(j);
            } else {
                throw new GeoPackageException("Unsupported FeatureIndexType: " + featureIndexType);
            }
        } else {
            throw new GeoPackageException("FeatureIndexType is required to delete index");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x0007 A[LOOP:0: B:1:0x0007->B:4:0x0017, LOOP_START, PHI: r1 
      PHI: (r1v1 boolean) = (r1v0 boolean), (r1v5 boolean) binds: [B:0:0x0000, B:4:0x0017] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isIndexed() {
        /*
            r3 = this;
            java.util.Set<mil.nga.geopackage.features.index.FeatureIndexType> r0 = r3.indexLocationQueryOrder
            java.util.Iterator r0 = r0.iterator()
            r1 = 0
        L_0x0007:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0019
            java.lang.Object r1 = r0.next()
            mil.nga.geopackage.features.index.FeatureIndexType r1 = (mil.nga.geopackage.features.index.FeatureIndexType) r1
            boolean r1 = r3.isIndexed(r1)
            if (r1 == 0) goto L_0x0007
        L_0x0019:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.features.index.FeatureIndexManager.isIndexed():boolean");
    }

    public boolean isIndexed(FeatureIndexType featureIndexType) {
        if (featureIndexType == null) {
            return isIndexed();
        }
        int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[featureIndexType.ordinal()];
        if (i == 1) {
            return this.featureTableIndex.isIndexed();
        }
        if (i == 2) {
            return this.featureIndexer.isIndexed();
        }
        throw new GeoPackageException("Unsupported FeatureIndexType: " + featureIndexType);
    }

    public List<FeatureIndexType> getIndexedTypes() {
        ArrayList arrayList = new ArrayList();
        for (FeatureIndexType next : this.indexLocationQueryOrder) {
            if (isIndexed(next)) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x0007 A[LOOP:0: B:1:0x0007->B:4:0x0017, LOOP_START, PHI: r1 
      PHI: (r1v1 java.util.Date) = (r1v0 java.util.Date), (r1v5 java.util.Date) binds: [B:0:0x0000, B:4:0x0017] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Date getLastIndexed() {
        /*
            r3 = this;
            java.util.Set<mil.nga.geopackage.features.index.FeatureIndexType> r0 = r3.indexLocationQueryOrder
            java.util.Iterator r0 = r0.iterator()
            r1 = 0
        L_0x0007:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0019
            java.lang.Object r1 = r0.next()
            mil.nga.geopackage.features.index.FeatureIndexType r1 = (mil.nga.geopackage.features.index.FeatureIndexType) r1
            java.util.Date r1 = r3.getLastIndexed(r1)
            if (r1 == 0) goto L_0x0007
        L_0x0019:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.features.index.FeatureIndexManager.getLastIndexed():java.util.Date");
    }

    public Date getLastIndexed(FeatureIndexType featureIndexType) {
        if (featureIndexType == null) {
            return getLastIndexed();
        }
        int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[featureIndexType.ordinal()];
        if (i == 1) {
            return this.featureTableIndex.getLastIndexed();
        }
        if (i == 2) {
            return this.featureIndexer.getLastIndexed();
        }
        throw new GeoPackageException("Unsupported FeatureIndexType: " + featureIndexType);
    }

    public FeatureIndexResults query() {
        int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[getIndexedType().ordinal()];
        if (i == 1) {
            return new FeatureIndexGeoPackageResults(this.featureTableIndex, this.featureTableIndex.count(), this.featureTableIndex.query());
        } else if (i != 2) {
            return null;
        } else {
            return new FeatureIndexMetadataResults(this.featureIndexer, this.featureIndexer.query());
        }
    }

    public long count() {
        int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[getIndexedType().ordinal()];
        if (i == 1) {
            return this.featureTableIndex.count();
        }
        if (i != 2) {
            return 0;
        }
        return (long) this.featureIndexer.count();
    }

    public FeatureIndexResults query(BoundingBox boundingBox) {
        int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[getIndexedType().ordinal()];
        if (i == 1) {
            return new FeatureIndexGeoPackageResults(this.featureTableIndex, this.featureTableIndex.count(boundingBox), this.featureTableIndex.query(boundingBox));
        } else if (i != 2) {
            return null;
        } else {
            return new FeatureIndexMetadataResults(this.featureIndexer, this.featureIndexer.query(boundingBox));
        }
    }

    public long count(BoundingBox boundingBox) {
        int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[getIndexedType().ordinal()];
        if (i == 1) {
            return this.featureTableIndex.count(boundingBox);
        }
        if (i != 2) {
            return 0;
        }
        return (long) this.featureIndexer.count(boundingBox);
    }

    public FeatureIndexResults query(GeometryEnvelope geometryEnvelope) {
        int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[getIndexedType().ordinal()];
        if (i == 1) {
            return new FeatureIndexGeoPackageResults(this.featureTableIndex, this.featureTableIndex.count(geometryEnvelope), this.featureTableIndex.query(geometryEnvelope));
        } else if (i != 2) {
            return null;
        } else {
            return new FeatureIndexMetadataResults(this.featureIndexer, this.featureIndexer.query(geometryEnvelope));
        }
    }

    public long count(GeometryEnvelope geometryEnvelope) {
        int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[getIndexedType().ordinal()];
        if (i == 1) {
            return this.featureTableIndex.count(geometryEnvelope);
        }
        if (i != 2) {
            return 0;
        }
        return (long) this.featureIndexer.count(geometryEnvelope);
    }

    public FeatureIndexResults query(BoundingBox boundingBox, Projection projection) {
        FeatureIndexResults featureIndexResults;
        int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[getIndexedType().ordinal()];
        if (i == 1) {
            featureIndexResults = new FeatureIndexGeoPackageResults(this.featureTableIndex, this.featureTableIndex.count(boundingBox, projection), this.featureTableIndex.query(boundingBox, projection));
        } else if (i != 2) {
            return null;
        } else {
            featureIndexResults = new FeatureIndexMetadataResults(this.featureIndexer, this.featureIndexer.query(boundingBox, projection));
        }
        return featureIndexResults;
    }

    public long count(BoundingBox boundingBox, Projection projection) {
        int i = C11711.$SwitchMap$mil$nga$geopackage$features$index$FeatureIndexType[getIndexedType().ordinal()];
        if (i == 1) {
            return this.featureTableIndex.count(boundingBox, projection);
        }
        if (i != 2) {
            return 0;
        }
        return this.featureIndexer.count(boundingBox, projection);
    }

    private FeatureIndexType verifyIndexLocation() {
        FeatureIndexType featureIndexType = this.indexLocation;
        if (featureIndexType != null) {
            return featureIndexType;
        }
        throw new GeoPackageException("Index Location is not set, set the location or call an index method specifying the location");
    }

    private FeatureIndexType getIndexedType() {
        FeatureIndexType featureIndexType;
        Iterator<FeatureIndexType> it = this.indexLocationQueryOrder.iterator();
        while (true) {
            if (!it.hasNext()) {
                featureIndexType = null;
                break;
            }
            featureIndexType = it.next();
            if (isIndexed(featureIndexType)) {
                break;
            }
        }
        if (featureIndexType != null) {
            return featureIndexType;
        }
        throw new GeoPackageException("Features are not indexed. GeoPackage: " + this.featureTableIndex.getGeoPackage().getName() + ", Table: " + this.featureTableIndex.getTableName());
    }
}
