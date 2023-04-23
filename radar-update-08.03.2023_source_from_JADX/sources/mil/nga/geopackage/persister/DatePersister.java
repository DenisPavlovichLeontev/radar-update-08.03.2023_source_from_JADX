package mil.nga.geopackage.persister;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import java.sql.SQLException;
import java.util.Date;
import mil.nga.geopackage.p009db.DateConverter;

public class DatePersister extends StringType {
    private static final DateConverter dateConverter = DateConverter.dateTimeConverter();
    private static final DatePersister singleTon = new DatePersister();

    private DatePersister() {
        super(SqlType.STRING, new Class[]{Date.class});
    }

    public static DatePersister getSingleton() {
        return singleTon;
    }

    public Object parseDefaultString(FieldType fieldType, String str) {
        if (DateConverter.DATETIME_FORMAT.equals(str)) {
            return javaToSqlArg((FieldType) null, new Date());
        }
        return null;
    }

    public Object javaToSqlArg(FieldType fieldType, Object obj) {
        if (obj == null || !(obj instanceof Date)) {
            return null;
        }
        return dateConverter.stringValue((Date) obj);
    }

    public Object sqlArgToJava(FieldType fieldType, Object obj, int i) throws SQLException {
        if (obj == null || !(obj instanceof String)) {
            return null;
        }
        try {
            return dateConverter.dateValue((String) obj);
        } catch (Exception e) {
            throw new SQLException("Failed to parse date string: " + obj, e);
        }
    }
}
