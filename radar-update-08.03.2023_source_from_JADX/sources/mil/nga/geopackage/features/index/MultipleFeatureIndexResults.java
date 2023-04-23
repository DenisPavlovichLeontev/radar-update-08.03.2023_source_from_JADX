package mil.nga.geopackage.features.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import mil.nga.geopackage.features.user.FeatureRow;

public class MultipleFeatureIndexResults implements FeatureIndexResults {
    private final int count;
    /* access modifiers changed from: private */
    public final List<FeatureIndexResults> results;

    public MultipleFeatureIndexResults(FeatureIndexResults... featureIndexResultsArr) {
        this((Collection<FeatureIndexResults>) Arrays.asList(featureIndexResultsArr));
    }

    public MultipleFeatureIndexResults(Collection<FeatureIndexResults> collection) {
        ArrayList arrayList = new ArrayList();
        this.results = arrayList;
        arrayList.addAll(collection);
        int i = 0;
        for (FeatureIndexResults count2 : collection) {
            i = (int) (((long) i) + count2.count());
        }
        this.count = i;
    }

    public long count() {
        return (long) this.count;
    }

    public void close() {
        for (FeatureIndexResults close : this.results) {
            close.close();
        }
    }

    public Iterator<FeatureRow> iterator() {
        return new Iterator<FeatureRow>() {
            private Iterator<FeatureRow> currentResults = null;
            int index = -1;

            public boolean hasNext() {
                Iterator<FeatureRow> it = this.currentResults;
                boolean hasNext = it != null ? it.hasNext() : false;
                if (!hasNext) {
                    while (!hasNext) {
                        int i = this.index + 1;
                        this.index = i;
                        if (i >= MultipleFeatureIndexResults.this.results.size()) {
                            break;
                        }
                        Iterator<FeatureRow> it2 = ((FeatureIndexResults) MultipleFeatureIndexResults.this.results.get(this.index)).iterator();
                        this.currentResults = it2;
                        hasNext = it2.hasNext();
                    }
                }
                return hasNext;
            }

            public FeatureRow next() {
                Iterator<FeatureRow> it = this.currentResults;
                if (it != null) {
                    return it.next();
                }
                return null;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
