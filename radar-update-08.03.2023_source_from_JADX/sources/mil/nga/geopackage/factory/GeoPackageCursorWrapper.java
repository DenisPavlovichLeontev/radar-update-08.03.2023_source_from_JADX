package mil.nga.geopackage.factory;

import android.database.Cursor;

interface GeoPackageCursorWrapper {
    Cursor wrapCursor(Cursor cursor);
}
