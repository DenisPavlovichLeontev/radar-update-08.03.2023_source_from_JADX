package org.osgeo.proj4j.util;

import java.util.HashMap;
import java.util.Map;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.InvalidValueException;
import org.osgeo.proj4j.UnknownAuthorityCodeException;
import org.osgeo.proj4j.UnsupportedParameterException;

public class CRSCache {
    private static CRSFactory crsFactory = new CRSFactory();
    private static Map<String, CoordinateReferenceSystem> projCache = new HashMap();

    public CoordinateReferenceSystem createFromName(String str) throws UnsupportedParameterException, InvalidValueException, UnknownAuthorityCodeException {
        CoordinateReferenceSystem coordinateReferenceSystem = projCache.get(str);
        if (coordinateReferenceSystem != null) {
            return coordinateReferenceSystem;
        }
        CoordinateReferenceSystem createFromName = crsFactory.createFromName(str);
        projCache.put(str, createFromName);
        return createFromName;
    }
}
