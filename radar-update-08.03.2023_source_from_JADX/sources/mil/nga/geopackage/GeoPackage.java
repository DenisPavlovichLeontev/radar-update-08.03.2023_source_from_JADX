package mil.nga.geopackage;

import android.database.Cursor;
import mil.nga.geopackage.attributes.AttributesDao;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.user.FeatureDao;
import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.user.TileDao;

public interface GeoPackage extends GeoPackageCore {
    Cursor foreignKeyCheck();

    AttributesDao getAttributesDao(String str);

    AttributesDao getAttributesDao(Contents contents);

    GeoPackageConnection getConnection();

    FeatureDao getFeatureDao(String str);

    FeatureDao getFeatureDao(Contents contents);

    FeatureDao getFeatureDao(GeometryColumns geometryColumns);

    TileDao getTileDao(String str);

    TileDao getTileDao(Contents contents);

    TileDao getTileDao(TileMatrixSet tileMatrixSet);

    Cursor integrityCheck();

    Cursor quickCheck();

    Cursor rawQuery(String str, String[] strArr);
}
