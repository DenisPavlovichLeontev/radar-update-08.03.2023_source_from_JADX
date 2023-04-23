package mil.nga.geopackage.features.index;

import mil.nga.geopackage.features.user.FeatureRow;

public interface FeatureIndexResults extends Iterable<FeatureRow> {
    void close();

    long count();
}
