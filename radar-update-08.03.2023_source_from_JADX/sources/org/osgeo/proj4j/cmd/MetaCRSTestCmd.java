package org.osgeo.proj4j.cmd;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import mil.nga.geopackage.property.PropertyConstants;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.Proj4jException;
import org.osgeo.proj4j.p017io.MetaCRSTestCase;
import org.osgeo.proj4j.p017io.MetaCRSTestFileReader;
import org.osgeo.proj4j.util.CRSCache;

public class MetaCRSTestCmd {
    private static final int TESTS_PER_LINE = 50;
    private static CRSFactory csFactory = new CRSFactory();
    int count = 0;
    private CRSCache crsCache = new CRSCache();
    int errCount = 0;
    int failCount = 0;
    private List<String> filenames = new ArrayList();
    private boolean verbose = false;

    private static String usage() {
        return "Usage: MetaCRSTestCmd [-verbose] { <test-file-name> }";
    }

    public static void main(String[] strArr) {
        MetaCRSTestCmd metaCRSTestCmd = new MetaCRSTestCmd();
        metaCRSTestCmd.parseArgs(strArr);
        try {
            metaCRSTestCmd.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseArgs(String[] strArr) {
        if (strArr.length <= 0) {
            System.err.println(usage());
            System.exit(1);
        }
        parseFlags(strArr);
        parseFiles(strArr);
    }

    private void parseFlags(String[] strArr) {
        for (String str : strArr) {
            if (str.startsWith("-") && str.equalsIgnoreCase("-verbose")) {
                this.verbose = true;
            }
        }
    }

    private void parseFiles(String[] strArr) {
        for (String str : strArr) {
            if (!str.startsWith("-")) {
                this.filenames.add(str);
            }
        }
    }

    private void execute() throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        for (String execute : this.filenames) {
            execute(execute);
        }
        System.out.println();
        PrintStream printStream = System.out;
        printStream.println("Tests run: " + this.count + ",  Failures: " + this.failCount + ",  Errors: " + this.errCount);
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        PrintStream printStream2 = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("Time: ");
        sb.append(((double) currentTimeMillis2) / 1000.0d);
        sb.append(" s");
        printStream2.println(sb.toString());
    }

    private void execute(String str) throws IOException {
        System.out.println("File: " + str);
        for (MetaCRSTestCase next : new MetaCRSTestFileReader(new File(str)).readTests()) {
            next.setCache(this.crsCache);
            this.count++;
            System.out.print(PropertyConstants.PROPERTY_DIVIDER);
            try {
                boolean execute = next.execute(csFactory);
                if (!execute) {
                    this.failCount++;
                    System.out.print("F");
                }
                if (this.verbose || !execute) {
                    System.out.println();
                    next.print(System.out);
                }
                if (this.count % 50 == 0) {
                    System.out.println();
                }
            } catch (Proj4jException e) {
                System.out.println(e);
                this.errCount++;
            }
        }
        System.out.println();
    }
}
