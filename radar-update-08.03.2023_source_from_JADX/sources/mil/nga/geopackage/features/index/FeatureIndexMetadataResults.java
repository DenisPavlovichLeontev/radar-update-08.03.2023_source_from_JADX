package mil.nga.geopackage.features.index;

import android.database.Cursor;
import java.util.Iterator;
import mil.nga.geopackage.features.user.FeatureRow;
import mil.nga.geopackage.p009db.FeatureIndexer;

public class FeatureIndexMetadataResults implements FeatureIndexResults {
    /* access modifiers changed from: private */
    public final FeatureIndexer featureIndexer;
    /* access modifiers changed from: private */
    public final Cursor geometryMetadata;

    public FeatureIndexMetadataResults(FeatureIndexer featureIndexer2, Cursor cursor) {
        this.featureIndexer = featureIndexer2;
        this.geometryMetadata = cursor;
    }

    public Iterator<FeatureRow> iterator() {
        return new Iterator<FeatureRow>() {
            public boolean hasNext() {
                return !FeatureIndexMetadataResults.this.geometryMetadata.isLast();
            }

            public FeatureRow next() {
                FeatureIndexMetadataResults.this.geometryMetadata.moveToNext();
                return FeatureIndexMetadataResults.this.featureIndexer.getFeatureRow(FeatureIndexMetadataResults.this.geometryMetadata);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public long count() {
        return (long) this.geometryMetadata.getCount();
    }

    public void close() {
        this.geometryMetadata.close();
    }
}
