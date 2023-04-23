package org.osgeo.proj4j.p017io;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

/* renamed from: org.osgeo.proj4j.io.Proj4FileReader */
public class Proj4FileReader {
    public String[] readParametersFromFile(String str, String str2) throws IOException {
        String str3 = "/nad/" + str.toLowerCase();
        InputStream resourceAsStream = Proj4FileReader.class.getResourceAsStream(str3);
        if (resourceAsStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
            try {
                return readFile(bufferedReader, str2);
            } finally {
                bufferedReader.close();
            }
        } else {
            throw new IllegalStateException("Unable to access CRS file: " + str3);
        }
    }

    private StreamTokenizer createTokenizer(BufferedReader bufferedReader) {
        StreamTokenizer streamTokenizer = new StreamTokenizer(bufferedReader);
        streamTokenizer.commentChar(35);
        streamTokenizer.ordinaryChars(48, 57);
        streamTokenizer.ordinaryChars(46, 46);
        streamTokenizer.ordinaryChars(45, 45);
        streamTokenizer.ordinaryChars(43, 43);
        streamTokenizer.wordChars(48, 57);
        streamTokenizer.wordChars(39, 39);
        streamTokenizer.wordChars(34, 34);
        streamTokenizer.wordChars(95, 95);
        streamTokenizer.wordChars(46, 46);
        streamTokenizer.wordChars(45, 45);
        streamTokenizer.wordChars(43, 43);
        streamTokenizer.wordChars(44, 44);
        streamTokenizer.wordChars(64, 64);
        return streamTokenizer;
    }

    private String[] readFile(BufferedReader bufferedReader, String str) throws IOException {
        StreamTokenizer createTokenizer = createTokenizer(bufferedReader);
        createTokenizer.nextToken();
        while (createTokenizer.ttype == 60) {
            createTokenizer.nextToken();
            if (createTokenizer.ttype == -3) {
                String str2 = createTokenizer.sval;
                createTokenizer.nextToken();
                if (createTokenizer.ttype == 62) {
                    createTokenizer.nextToken();
                    ArrayList arrayList = new ArrayList();
                    while (createTokenizer.ttype != 60) {
                        if (createTokenizer.ttype == 43) {
                            createTokenizer.nextToken();
                        }
                        if (createTokenizer.ttype == -3) {
                            String str3 = createTokenizer.sval;
                            createTokenizer.nextToken();
                            if (createTokenizer.ttype == 61) {
                                createTokenizer.nextToken();
                                String str4 = createTokenizer.sval;
                                createTokenizer.nextToken();
                                addParam(arrayList, str3, str4);
                            } else {
                                addParam(arrayList, str3, (String) null);
                            }
                        } else {
                            throw new IOException(createTokenizer.lineno() + ": Word expected after '+'");
                        }
                    }
                    createTokenizer.nextToken();
                    if (createTokenizer.ttype == 62) {
                        createTokenizer.nextToken();
                        if (str2.equals(str)) {
                            return (String[]) arrayList.toArray(new String[0]);
                        }
                    } else {
                        throw new IOException(createTokenizer.lineno() + ": '<>' expected");
                    }
                } else {
                    throw new IOException(createTokenizer.lineno() + ": '>' expected");
                }
            } else {
                throw new IOException(createTokenizer.lineno() + ": Word expected after '<'");
            }
        }
        return null;
    }

    private static void addParam(List list, String str, String str2) {
        if (!str.startsWith("+")) {
            str = "+" + str;
        }
        if (str2 != null) {
            list.add(str + SimpleComparison.EQUAL_TO_OPERATION + str2);
            return;
        }
        list.add(str);
    }

    public String[] getParameters(String str) {
        try {
            int indexOf = str.indexOf(58);
            if (indexOf >= 0) {
                return readParametersFromFile(str.substring(0, indexOf), str.substring(indexOf + 1));
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
