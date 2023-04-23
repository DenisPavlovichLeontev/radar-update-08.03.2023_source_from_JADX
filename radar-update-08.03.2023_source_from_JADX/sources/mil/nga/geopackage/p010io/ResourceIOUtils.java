package mil.nga.geopackage.p010io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import mil.nga.geopackage.GeoPackageException;

/* renamed from: mil.nga.geopackage.io.ResourceIOUtils */
public class ResourceIOUtils {
    public static List<String> parseSQLStatements(String str, String str2) {
        return parseSQLStatements("/" + str + "/" + str2);
    }

    public static List<String> parseSQLStatements(String str) {
        InputStream resourceAsStream = ResourceIOUtils.class.getResourceAsStream(str);
        if (resourceAsStream != null) {
            return parseSQLStatements(resourceAsStream);
        }
        throw new GeoPackageException("Failed to find resource: " + str);
    }

    public static List<String> parseSQLStatements(InputStream inputStream) {
        ArrayList arrayList = new ArrayList();
        Scanner scanner = new Scanner(inputStream);
        try {
            scanner.useDelimiter(Pattern.compile("\\n\\s*\\n"));
            while (scanner.hasNext()) {
                arrayList.add(scanner.next().trim());
            }
            return arrayList;
        } finally {
            scanner.close();
        }
    }
}
