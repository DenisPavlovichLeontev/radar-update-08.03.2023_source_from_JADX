package mil.nga.geopackage.p009db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import mil.nga.geopackage.GeoPackageException;

/* renamed from: mil.nga.geopackage.db.DateConverter */
public class DateConverter {
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATETIME_FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    private final List<SimpleDateFormat> formatters = new ArrayList();

    /* renamed from: mil.nga.geopackage.db.DateConverter$1 */
    static /* synthetic */ class C11621 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$geopackage$db$GeoPackageDataType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                mil.nga.geopackage.db.GeoPackageDataType[] r0 = mil.nga.geopackage.p009db.GeoPackageDataType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$mil$nga$geopackage$db$GeoPackageDataType = r0
                mil.nga.geopackage.db.GeoPackageDataType r1 = mil.nga.geopackage.p009db.GeoPackageDataType.DATE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$mil$nga$geopackage$db$GeoPackageDataType     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.geopackage.db.GeoPackageDataType r1 = mil.nga.geopackage.p009db.GeoPackageDataType.DATETIME     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.p009db.DateConverter.C11621.<clinit>():void");
        }
    }

    public static DateConverter converter(GeoPackageDataType geoPackageDataType) {
        int i = C11621.$SwitchMap$mil$nga$geopackage$db$GeoPackageDataType[geoPackageDataType.ordinal()];
        if (i == 1) {
            return dateConverter();
        }
        if (i == 2) {
            return dateTimeConverter();
        }
        throw new GeoPackageException("Not a date data type: " + geoPackageDataType);
    }

    public static DateConverter dateConverter() {
        return new DateConverter(DATE_FORMAT);
    }

    public static DateConverter dateTimeConverter() {
        return new DateConverter(DATETIME_FORMAT, DATETIME_FORMAT2);
    }

    private DateConverter(String... strArr) {
        for (String simpleDateFormat : strArr) {
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(simpleDateFormat);
            simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.formatters.add(simpleDateFormat2);
        }
    }

    public String stringValue(Date date) {
        String format;
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = this.formatters.get(0);
        synchronized (simpleDateFormat) {
            format = simpleDateFormat.format(date);
        }
        return format;
    }

    public Date dateValue(String str) {
        Date date = null;
        if (str != null) {
            Iterator<SimpleDateFormat> it = this.formatters.iterator();
            ParseException parseException = null;
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SimpleDateFormat next = it.next();
                try {
                    synchronized (next) {
                        date = next.parse(str);
                    }
                    break;
                } catch (ParseException e) {
                    if (parseException == null) {
                        parseException = e;
                    }
                }
            }
            if (date == null) {
                throw new GeoPackageException("Failed to parse date string: " + str, parseException);
            }
        }
        return date;
    }
}
