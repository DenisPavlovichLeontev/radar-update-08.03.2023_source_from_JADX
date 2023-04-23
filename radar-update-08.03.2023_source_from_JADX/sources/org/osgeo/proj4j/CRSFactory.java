package org.osgeo.proj4j;

import org.osgeo.proj4j.p017io.Proj4FileReader;
import org.osgeo.proj4j.parser.Proj4Parser;

public class CRSFactory {
    private static Proj4FileReader csReader = new Proj4FileReader();
    private static Registry registry = new Registry();

    public Registry getRegistry() {
        return registry;
    }

    public CoordinateReferenceSystem createFromName(String str) throws UnsupportedParameterException, InvalidValueException, UnknownAuthorityCodeException {
        String[] parameters = csReader.getParameters(str);
        if (parameters != null) {
            return createFromParameters(str, parameters);
        }
        throw new UnknownAuthorityCodeException(str);
    }

    public CoordinateReferenceSystem createFromParameters(String str, String str2) throws UnsupportedParameterException, InvalidValueException {
        return createFromParameters(str, splitParameters(str2));
    }

    public CoordinateReferenceSystem createFromParameters(String str, String[] strArr) throws UnsupportedParameterException, InvalidValueException {
        if (strArr == null) {
            return null;
        }
        return new Proj4Parser(registry).parse(str, strArr);
    }

    private static String[] splitParameters(String str) {
        return str.split("\\s+");
    }
}
