package mil.nga.geopackage.user;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.DateConverter;
import mil.nga.geopackage.p009db.GeoPackageDataType;

public class UserCoreResultUtils {
    public static final int FIELD_TYPE_BLOB = 4;
    public static final int FIELD_TYPE_FLOAT = 2;
    public static final int FIELD_TYPE_INTEGER = 1;
    public static final int FIELD_TYPE_NULL = 0;
    public static final int FIELD_TYPE_STRING = 3;

    public static Object getValue(UserCoreResult<?, ?, ?> userCoreResult, int i, GeoPackageDataType geoPackageDataType) {
        int type = userCoreResult.getType(i);
        if (type == 1) {
            return getIntegerValue(userCoreResult, i, geoPackageDataType);
        }
        if (type == 2) {
            return getFloatValue(userCoreResult, i, geoPackageDataType);
        }
        if (type == 3) {
            String string = userCoreResult.getString(i);
            if (geoPackageDataType == GeoPackageDataType.DATE || geoPackageDataType == GeoPackageDataType.DATETIME) {
                return DateConverter.converter(geoPackageDataType).dateValue(string);
            }
            return string;
        } else if (type != 4) {
            return null;
        } else {
            return userCoreResult.getBlob(i);
        }
    }

    public static Object getIntegerValue(UserCoreResult<?, ?, ?> userCoreResult, int i, GeoPackageDataType geoPackageDataType) {
        Object obj;
        switch (C11781.$SwitchMap$mil$nga$geopackage$db$GeoPackageDataType[geoPackageDataType.ordinal()]) {
            case 1:
                if (userCoreResult.getShort(i) != 0) {
                    obj = Boolean.TRUE;
                    break;
                } else {
                    obj = Boolean.FALSE;
                    break;
                }
            case 2:
                obj = Byte.valueOf((byte) userCoreResult.getShort(i));
                break;
            case 3:
                obj = Short.valueOf(userCoreResult.getShort(i));
                break;
            case 4:
                obj = Integer.valueOf(userCoreResult.getInt(i));
                break;
            case 5:
            case 6:
                obj = Long.valueOf(userCoreResult.getLong(i));
                break;
            default:
                throw new GeoPackageException("Data Type " + geoPackageDataType + " is not an integer type");
        }
        if (userCoreResult.wasNull()) {
            return null;
        }
        return obj;
    }

    public static Object getFloatValue(UserCoreResult<?, ?, ?> userCoreResult, int i, GeoPackageDataType geoPackageDataType) {
        Object obj;
        switch (geoPackageDataType) {
            case INT:
            case INTEGER:
            case DOUBLE:
            case REAL:
                obj = Double.valueOf(userCoreResult.getDouble(i));
                break;
            case FLOAT:
                obj = Float.valueOf(userCoreResult.getFloat(i));
                break;
            default:
                throw new GeoPackageException("Data Type " + geoPackageDataType + " is not a float type");
        }
        if (userCoreResult.wasNull()) {
            return null;
        }
        return obj;
    }
}
