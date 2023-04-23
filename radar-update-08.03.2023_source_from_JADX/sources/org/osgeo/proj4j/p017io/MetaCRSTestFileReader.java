package org.osgeo.proj4j.p017io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/* renamed from: org.osgeo.proj4j.io.MetaCRSTestFileReader */
public class MetaCRSTestFileReader {
    public static final int COL_COUNT = 19;
    private File file;
    private CSVRecordParser lineParser = new CSVRecordParser();

    public MetaCRSTestFileReader(File file2) {
        this.file = file2;
    }

    public List<MetaCRSTestCase> readTests() throws IOException {
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(this.file));
        try {
            return parseFile(lineNumberReader);
        } finally {
            lineNumberReader.close();
        }
    }

    private List<MetaCRSTestCase> parseFile(LineNumberReader lineNumberReader) throws IOException {
        ArrayList arrayList = new ArrayList();
        boolean z = false;
        while (true) {
            String readLine = lineNumberReader.readLine();
            if (readLine == null) {
                return arrayList;
            }
            if (!readLine.startsWith("#")) {
                if (!z) {
                    z = true;
                } else {
                    arrayList.add(parseTest(readLine));
                }
            }
        }
    }

    private MetaCRSTestCase parseTest(String str) {
        String[] parse = this.lineParser.parse(str);
        if (parse.length == 19) {
            return new MetaCRSTestCase(parse[0], parse[1], parse[2], parse[3], parse[4], parse[5], parseNumber(parse[6]), parseNumber(parse[7]), parseNumber(parse[8]), parseNumber(parse[9]), parseNumber(parse[10]), parseNumber(parse[11]), parseNumber(parse[12]), parseNumber(parse[13]), parseNumber(parse[14]), parse[15], parse[16], parse[17], parse[18]);
        }
        throw new IllegalStateException("Expected 19 columns in file, but found " + parse.length);
    }

    private static double parseNumber(String str) {
        if (str == null || str.length() == 0) {
            return Double.NaN;
        }
        return Double.parseDouble(str);
    }
}
