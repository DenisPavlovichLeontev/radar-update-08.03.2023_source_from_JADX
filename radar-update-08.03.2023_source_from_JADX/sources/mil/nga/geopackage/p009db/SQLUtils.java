package mil.nga.geopackage.p009db;

import android.content.ContentValues;
import android.os.Parcel;
import java.util.HashMap;
import java.util.Map;

/* renamed from: mil.nga.geopackage.db.SQLUtils */
public class SQLUtils {
    public static ContentValues quoteWrap(ContentValues contentValues) {
        if (contentValues == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        for (Map.Entry next : contentValues.valueSet()) {
            hashMap.put(CoreSQLUtils.quoteWrap((String) next.getKey()), next.getValue());
        }
        Parcel obtain = Parcel.obtain();
        obtain.writeMap(hashMap);
        obtain.setDataPosition(0);
        ContentValues contentValues2 = (ContentValues) ContentValues.CREATOR.createFromParcel(obtain);
        obtain.recycle();
        return contentValues2;
    }
}
