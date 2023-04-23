package mil.nga.geopackage.features.index;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mil.nga.geopackage.features.user.FeatureRow;

public class FeatureIndexListResults implements FeatureIndexResults {
    private final List<FeatureRow> rows = new ArrayList();

    public void close() {
    }

    public FeatureIndexListResults() {
    }

    public FeatureIndexListResults(FeatureRow featureRow) {
        addRow(featureRow);
    }

    public FeatureIndexListResults(List<FeatureRow> list) {
        addRows(list);
    }

    public void addRow(FeatureRow featureRow) {
        this.rows.add(featureRow);
    }

    public void addRows(List<FeatureRow> list) {
        this.rows.addAll(list);
    }

    public long count() {
        return (long) this.rows.size();
    }

    public Iterator<FeatureRow> iterator() {
        return this.rows.iterator();
    }
}
