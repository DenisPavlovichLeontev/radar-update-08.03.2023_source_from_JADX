package mil.nga.geopackage.tiles.features;

import android.graphics.Bitmap;
import mil.nga.geopackage.features.index.FeatureIndexResults;
import mil.nga.geopackage.features.user.FeatureCursor;

public interface CustomFeaturesTile {
    Bitmap drawTile(int i, int i2, long j, FeatureIndexResults featureIndexResults);

    Bitmap drawUnindexedTile(int i, int i2, long j, FeatureCursor featureCursor);
}
