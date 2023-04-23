package mil.nga.geopackage.features.user;

import java.util.List;
import mil.nga.geopackage.user.UserInvalidCursor;

public class FeatureInvalidCursor extends UserInvalidCursor<FeatureColumn, FeatureTable, FeatureRow, FeatureCursor, FeatureDao> {
    public FeatureInvalidCursor(FeatureDao featureDao, FeatureCursor featureCursor, List<Integer> list, List<FeatureColumn> list2) {
        super(featureDao, featureCursor, list, list2);
    }
}
