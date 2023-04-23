package mil.nga.geopackage.features.index;

import com.j256.ormlite.dao.CloseableIterator;
import java.util.Iterator;
import mil.nga.geopackage.extension.index.FeatureTableIndex;
import mil.nga.geopackage.extension.index.GeometryIndex;
import mil.nga.geopackage.features.user.FeatureRow;

class FeatureIndexGeoPackageResults implements FeatureIndexResults {
    private final long count;
    /* access modifiers changed from: private */
    public final FeatureTableIndex featureTableIndex;
    /* access modifiers changed from: private */
    public final CloseableIterator<GeometryIndex> geometryIndices;

    public FeatureIndexGeoPackageResults(FeatureTableIndex featureTableIndex2, long j, CloseableIterator<GeometryIndex> closeableIterator) {
        this.featureTableIndex = featureTableIndex2;
        this.count = j;
        this.geometryIndices = closeableIterator;
    }

    public Iterator<FeatureRow> iterator() {
        return new Iterator<FeatureRow>() {
            public boolean hasNext() {
                return FeatureIndexGeoPackageResults.this.geometryIndices.hasNext();
            }

            public FeatureRow next() {
                return FeatureIndexGeoPackageResults.this.featureTableIndex.getFeatureRow((GeometryIndex) FeatureIndexGeoPackageResults.this.geometryIndices.next());
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public long count() {
        return this.count;
    }

    public void close() {
        this.geometryIndices.closeQuietly();
    }
}
