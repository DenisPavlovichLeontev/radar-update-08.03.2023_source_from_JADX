package org.osgeo.proj4j.p017io;

import java.io.PrintStream;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.util.CRSCache;
import org.osgeo.proj4j.util.ProjectionUtil;

/* renamed from: org.osgeo.proj4j.io.MetaCRSTestCase */
public class MetaCRSTestCase {
    private static final CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
    private CRSCache crsCache = null;
    String dataCmnts;
    String dataSource;
    private boolean isInTol;
    String maintenanceCmnts;
    ProjCoordinate resultPt = new ProjCoordinate();
    CoordinateReferenceSystem srcCS;
    String srcCrs;
    String srcCrsAuth;
    double srcOrd1;
    double srcOrd2;
    double srcOrd3;
    ProjCoordinate srcPt = new ProjCoordinate();
    String testMethod;
    String testName;
    CoordinateReferenceSystem tgtCS;
    String tgtCrs;
    String tgtCrsAuth;
    double tgtOrd1;
    double tgtOrd2;
    double tgtOrd3;
    double tolOrd1;
    double tolOrd2;
    double tolOrd3;
    String using;
    private boolean verbose = true;

    public MetaCRSTestCase(String str, String str2, String str3, String str4, String str5, String str6, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, String str7, String str8, String str9, String str10) {
        this.testName = str;
        this.testMethod = str2;
        this.srcCrsAuth = str3;
        this.srcCrs = str4;
        this.tgtCrsAuth = str5;
        this.tgtCrs = str6;
        this.srcOrd1 = d;
        this.srcOrd2 = d2;
        this.srcOrd3 = d3;
        this.tgtOrd1 = d4;
        this.tgtOrd2 = d5;
        this.tgtOrd3 = d6;
        this.tolOrd1 = d7;
        this.tolOrd2 = d8;
        this.tolOrd3 = d9;
        this.using = str7;
        this.dataSource = str8;
        this.dataCmnts = str9;
        this.maintenanceCmnts = str10;
    }

    public String getName() {
        return this.testName;
    }

    public String getSourceCrsName() {
        return csName(this.srcCrsAuth, this.srcCrs);
    }

    public String getTargetCrsName() {
        return csName(this.tgtCrsAuth, this.tgtCrs);
    }

    public CoordinateReferenceSystem getSourceCS() {
        return this.srcCS;
    }

    public CoordinateReferenceSystem getTargetCS() {
        return this.tgtCS;
    }

    public ProjCoordinate getSourceCoordinate() {
        return new ProjCoordinate(this.srcOrd1, this.srcOrd2, this.srcOrd3);
    }

    public ProjCoordinate getTargetCoordinate() {
        return new ProjCoordinate(this.tgtOrd1, this.tgtOrd2, this.tgtOrd3);
    }

    public ProjCoordinate getResultCoordinate() {
        return new ProjCoordinate(this.resultPt.f409x, this.resultPt.f410y);
    }

    public void setCache(CRSCache cRSCache) {
        this.crsCache = cRSCache;
    }

    public boolean execute(CRSFactory cRSFactory) {
        this.srcCS = createCS(cRSFactory, this.srcCrsAuth, this.srcCrs);
        CoordinateReferenceSystem createCS = createCS(cRSFactory, this.tgtCrsAuth, this.tgtCrs);
        this.tgtCS = createCS;
        return executeTransform(this.srcCS, createCS);
    }

    public static String csName(String str, String str2) {
        return str + ":" + str2;
    }

    public CoordinateReferenceSystem createCS(CRSFactory cRSFactory, String str, String str2) {
        String csName = csName(str, str2);
        CRSCache cRSCache = this.crsCache;
        if (cRSCache != null) {
            return cRSCache.createFromName(csName);
        }
        return cRSFactory.createFromName(csName);
    }

    private boolean executeTransform(CoordinateReferenceSystem coordinateReferenceSystem, CoordinateReferenceSystem coordinateReferenceSystem2) {
        this.srcPt.f409x = this.srcOrd1;
        this.srcPt.f410y = this.srcOrd2;
        ctFactory.createTransform(coordinateReferenceSystem, coordinateReferenceSystem2).transform(this.srcPt, this.resultPt);
        boolean z = Math.abs(this.resultPt.f409x - this.tgtOrd1) <= this.tolOrd1 && Math.abs(this.resultPt.f410y - this.tgtOrd2) <= this.tolOrd2;
        this.isInTol = z;
        return z;
    }

    public void print(PrintStream printStream) {
        System.out.println(this.testName);
        PrintStream printStream2 = System.out;
        printStream2.println(ProjectionUtil.toString(this.srcPt) + " -> " + ProjectionUtil.toString(this.resultPt) + " ( expected: " + this.tgtOrd1 + ", " + this.tgtOrd2 + " )");
        if (!this.isInTol) {
            System.out.println("FAIL");
            PrintStream printStream3 = System.out;
            printStream3.println("Src CRS: (" + this.srcCrsAuth + ":" + this.srcCrs + ") " + this.srcCS.getParameterString());
            PrintStream printStream4 = System.out;
            printStream4.println("Tgt CRS: (" + this.tgtCrsAuth + ":" + this.tgtCrs + ") " + this.tgtCS.getParameterString());
        }
    }
}
