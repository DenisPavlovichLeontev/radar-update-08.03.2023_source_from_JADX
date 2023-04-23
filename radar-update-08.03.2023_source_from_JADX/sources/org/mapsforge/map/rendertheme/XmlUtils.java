package org.mapsforge.map.rendertheme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.Logger;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.ResourceBitmap;
import org.mapsforge.core.model.Tag;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;
import org.xmlpull.p018v1.XmlPullParserException;

public final class XmlUtils {
    private static final Logger LOGGER = Logger.getLogger(XmlUtils.class.getName());
    private static final String PREFIX_ASSETS = "assets:";
    private static final String PREFIX_FILE = "file:";
    private static final String PREFIX_JAR = "jar:";
    private static final String PREFIX_JAR_V1 = "jar:/org/mapsforge/android/maps/rendertheme";
    private static final String UNSUPPORTED_COLOR_FORMAT = "unsupported color format: ";

    public static void checkMandatoryAttribute(String str, String str2, Object obj) throws XmlPullParserException {
        if (obj == null) {
            throw new XmlPullParserException("missing attribute '" + str2 + "' for element: " + str);
        }
    }

    public static ResourceBitmap createBitmap(GraphicFactory graphicFactory, DisplayModel displayModel, String str, String str2, int i, int i2, int i3) throws IOException {
        if (str2 == null || str2.length() == 0) {
            return null;
        }
        InputStream createInputStream = createInputStream(graphicFactory, str, str2);
        try {
            String absoluteName = getAbsoluteName(str, str2);
            int hashCode = (absoluteName + i + i2 + i3).hashCode();
            if (str2.toLowerCase(Locale.ENGLISH).endsWith(".svg")) {
                ResourceBitmap renderSvg = graphicFactory.renderSvg(createInputStream, displayModel.getScaleFactor(), i, i2, i3, hashCode);
                createInputStream.close();
                return renderSvg;
            }
            ResourceBitmap createResourceBitmap = graphicFactory.createResourceBitmap(createInputStream, displayModel.getScaleFactor(), i, i2, i3, hashCode);
            createInputStream.close();
            return createResourceBitmap;
        } catch (IOException e) {
            throw new IOException("Reading bitmap file failed " + str2, e);
        } catch (IOException e2) {
            throw new IOException("SVG render failed " + str2, e2);
        } catch (Throwable th) {
            createInputStream.close();
            throw th;
        }
    }

    public static XmlPullParserException createXmlPullParserException(String str, String str2, String str3, int i) {
        return new XmlPullParserException("unknown attribute (" + i + ") in element '" + str + "': " + str2 + Tag.KEY_VALUE_SEPARATOR + str3);
    }

    public static int getColor(GraphicFactory graphicFactory, String str) {
        return getColor(graphicFactory, str, (ThemeCallback) null, (RenderInstruction) null);
    }

    public static int getColor(GraphicFactory graphicFactory, String str, ThemeCallback themeCallback, RenderInstruction renderInstruction) {
        if (str.isEmpty() || str.charAt(0) != '#') {
            throw new IllegalArgumentException(UNSUPPORTED_COLOR_FORMAT + str);
        } else if (str.length() == 7) {
            return getColor(graphicFactory, str, 255, 1, themeCallback, renderInstruction);
        } else {
            if (str.length() == 9) {
                return getColor(graphicFactory, str, Integer.parseInt(str.substring(1, 3), 16), 3, themeCallback, renderInstruction);
            }
            throw new IllegalArgumentException(UNSUPPORTED_COLOR_FORMAT + str);
        }
    }

    public static byte parseNonNegativeByte(String str, String str2) throws XmlPullParserException {
        byte parseByte = Byte.parseByte(str2);
        checkForNegativeValue(str, (float) parseByte);
        return parseByte;
    }

    public static float parseNonNegativeFloat(String str, String str2) throws XmlPullParserException {
        float parseFloat = Float.parseFloat(str2);
        checkForNegativeValue(str, parseFloat);
        return parseFloat;
    }

    public static int parseNonNegativeInteger(String str, String str2) throws XmlPullParserException {
        int parseInt = Integer.parseInt(str2);
        checkForNegativeValue(str, (float) parseInt);
        return parseInt;
    }

    private static void checkForNegativeValue(String str, float f) throws XmlPullParserException {
        if (f < 0.0f) {
            throw new XmlPullParserException("Attribute '" + str + "' must not be negative: " + f);
        }
    }

    private static InputStream createInputStream(GraphicFactory graphicFactory, String str, String str2) throws IOException {
        InputStream inputStream;
        if (str2.startsWith(PREFIX_ASSETS)) {
            str2 = str2.substring(7);
            inputStream = inputStreamFromAssets(graphicFactory, str, str2);
        } else if (str2.startsWith(PREFIX_FILE)) {
            str2 = str2.substring(5);
            inputStream = inputStreamFromFile(str, str2);
        } else if (str2.startsWith(PREFIX_JAR) || str2.startsWith(PREFIX_JAR_V1)) {
            if (str2.startsWith(PREFIX_JAR)) {
                str2 = str2.substring(4);
            } else if (str2.startsWith(PREFIX_JAR_V1)) {
                str2 = str2.substring(43);
            }
            inputStream = inputStreamFromJar(str, str2);
        } else {
            InputStream inputStreamFromFile = inputStreamFromFile(str, str2);
            inputStream = inputStreamFromFile == null ? inputStreamFromAssets(graphicFactory, str, str2) : inputStreamFromFile;
            if (inputStream == null) {
                inputStream = inputStreamFromJar(str, str2);
            }
        }
        if (inputStream == null && (inputStream = inputStreamFromJar("/assets/", str2)) != null) {
            Logger logger = LOGGER;
            logger.info("internal resource: " + str2);
        }
        if (inputStream != null) {
            return inputStream;
        }
        Logger logger2 = LOGGER;
        logger2.severe("invalid resource: " + str2);
        throw new FileNotFoundException("invalid resource: " + str2);
    }

    private static InputStream inputStreamFromAssets(GraphicFactory graphicFactory, String str, String str2) throws IOException {
        InputStream inputStream;
        try {
            inputStream = graphicFactory.platformSpecificSources(str, str2);
        } catch (IOException unused) {
            inputStream = null;
        }
        if (inputStream != null) {
            return inputStream;
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0035, code lost:
        if (r0.canRead() != false) goto L_0x0038;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0028, code lost:
        if (r0.exists() == false) goto L_0x0037;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.io.InputStream inputStreamFromFile(java.lang.String r4, java.lang.String r5) throws java.io.IOException {
        /*
            java.io.File r0 = getFile(r4, r5)
            boolean r1 = r0.exists()
            r2 = 0
            if (r1 != 0) goto L_0x002b
            int r1 = r5.length()
            if (r1 <= 0) goto L_0x0024
            r1 = 0
            char r1 = r5.charAt(r1)
            char r3 = java.io.File.separatorChar
            if (r1 != r3) goto L_0x0024
            r0 = 1
            java.lang.String r5 = r5.substring(r0)
            java.io.File r4 = getFile(r4, r5)
            r0 = r4
        L_0x0024:
            boolean r4 = r0.exists()
            if (r4 != 0) goto L_0x0038
            goto L_0x0037
        L_0x002b:
            boolean r4 = r0.isFile()
            if (r4 == 0) goto L_0x0037
            boolean r4 = r0.canRead()
            if (r4 != 0) goto L_0x0038
        L_0x0037:
            r0 = r2
        L_0x0038:
            if (r0 == 0) goto L_0x0040
            java.io.FileInputStream r4 = new java.io.FileInputStream
            r4.<init>(r0)
            return r4
        L_0x0040:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.rendertheme.XmlUtils.inputStreamFromFile(java.lang.String, java.lang.String):java.io.InputStream");
    }

    private static InputStream inputStreamFromJar(String str, String str2) throws IOException {
        return XmlUtils.class.getResourceAsStream(getAbsoluteName(str, str2));
    }

    private static String getAbsoluteName(String str, String str2) {
        if (str2.charAt(0) == File.separatorChar) {
            return str2;
        }
        return str + str2;
    }

    private static int getColor(GraphicFactory graphicFactory, String str, int i, int i2, ThemeCallback themeCallback, RenderInstruction renderInstruction) {
        int i3 = i2 + 2;
        int i4 = i2 + 4;
        int createColor = graphicFactory.createColor(i, Integer.parseInt(str.substring(i2, i3), 16), Integer.parseInt(str.substring(i3, i4), 16), Integer.parseInt(str.substring(i4, i2 + 6), 16));
        return themeCallback != null ? themeCallback.getColor(renderInstruction, createColor) : createColor;
    }

    private static File getFile(String str, String str2) {
        if (str2.charAt(0) == File.separatorChar) {
            return new File(str2);
        }
        return new File(str, str2);
    }

    private XmlUtils() {
        throw new IllegalStateException();
    }
}
