package org.mapsforge.map.android.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Logger;
import org.mapsforge.core.util.IOUtils;

public class AndroidSvgBitmapStore {
    /* access modifiers changed from: private */
    public static final Logger LOGGER = Logger.getLogger(AndroidSvgBitmapStore.class.getName());
    private static final String SVG_PREFIX = "svg-";
    private static final String SVG_SUFFIX = ".png";

    private static class SvgStorer implements Runnable {
        private Bitmap bitmap;
        private int hash;

        public SvgStorer(int i, Bitmap bitmap2) {
            this.hash = i;
            this.bitmap = bitmap2;
        }

        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0036 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r5 = this;
                int r0 = r5.hash
                java.lang.String r0 = org.mapsforge.map.android.graphics.AndroidSvgBitmapStore.createFileName(r0)
                r1 = 0
                org.mapsforge.map.android.graphics.AndroidGraphicFactory r2 = org.mapsforge.map.android.graphics.AndroidGraphicFactory.INSTANCE     // Catch:{ IllegalStateException -> 0x004f, FileNotFoundException -> 0x0036 }
                r3 = 0
                java.io.FileOutputStream r1 = r2.openFileOutput(r0, r3)     // Catch:{ IllegalStateException -> 0x004f, FileNotFoundException -> 0x0036 }
                android.graphics.Bitmap r2 = r5.bitmap     // Catch:{ IllegalStateException -> 0x004f, FileNotFoundException -> 0x0036 }
                android.graphics.Bitmap$CompressFormat r4 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ IllegalStateException -> 0x004f, FileNotFoundException -> 0x0036 }
                boolean r2 = r2.compress(r4, r3, r1)     // Catch:{ IllegalStateException -> 0x004f, FileNotFoundException -> 0x0036 }
                if (r2 != 0) goto L_0x0030
                java.util.logging.Logger r2 = org.mapsforge.map.android.graphics.AndroidSvgBitmapStore.LOGGER     // Catch:{ IllegalStateException -> 0x004f, FileNotFoundException -> 0x0036 }
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IllegalStateException -> 0x004f, FileNotFoundException -> 0x0036 }
                r3.<init>()     // Catch:{ IllegalStateException -> 0x004f, FileNotFoundException -> 0x0036 }
                java.lang.String r4 = "SVG Failed to write svg bitmap "
                r3.append(r4)     // Catch:{ IllegalStateException -> 0x004f, FileNotFoundException -> 0x0036 }
                r3.append(r0)     // Catch:{ IllegalStateException -> 0x004f, FileNotFoundException -> 0x0036 }
                java.lang.String r3 = r3.toString()     // Catch:{ IllegalStateException -> 0x004f, FileNotFoundException -> 0x0036 }
                r2.warning(r3)     // Catch:{ IllegalStateException -> 0x004f, FileNotFoundException -> 0x0036 }
            L_0x0030:
                org.mapsforge.core.util.IOUtils.closeQuietly(r1)
                goto L_0x0068
            L_0x0034:
                r0 = move-exception
                goto L_0x0069
            L_0x0036:
                java.util.logging.Logger r2 = org.mapsforge.map.android.graphics.AndroidSvgBitmapStore.LOGGER     // Catch:{ all -> 0x0034 }
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0034 }
                r3.<init>()     // Catch:{ all -> 0x0034 }
                java.lang.String r4 = "SVG Failed to create file for svg bitmap "
                r3.append(r4)     // Catch:{ all -> 0x0034 }
                r3.append(r0)     // Catch:{ all -> 0x0034 }
                java.lang.String r0 = r3.toString()     // Catch:{ all -> 0x0034 }
                r2.warning(r0)     // Catch:{ all -> 0x0034 }
                goto L_0x0030
            L_0x004f:
                java.util.logging.Logger r2 = org.mapsforge.map.android.graphics.AndroidSvgBitmapStore.LOGGER     // Catch:{ all -> 0x0034 }
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0034 }
                r3.<init>()     // Catch:{ all -> 0x0034 }
                java.lang.String r4 = "SVG Failed to stream bitmap to file "
                r3.append(r4)     // Catch:{ all -> 0x0034 }
                r3.append(r0)     // Catch:{ all -> 0x0034 }
                java.lang.String r0 = r3.toString()     // Catch:{ all -> 0x0034 }
                r2.warning(r0)     // Catch:{ all -> 0x0034 }
                goto L_0x0030
            L_0x0068:
                return
            L_0x0069:
                org.mapsforge.core.util.IOUtils.closeQuietly(r1)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.android.graphics.AndroidSvgBitmapStore.SvgStorer.run():void");
        }
    }

    public static void clear() {
        for (String str : AndroidGraphicFactory.INSTANCE.fileList()) {
            if (str.startsWith(SVG_PREFIX) && str.endsWith(SVG_SUFFIX)) {
                AndroidGraphicFactory.INSTANCE.deleteFile(str);
            }
        }
    }

    /* access modifiers changed from: private */
    public static String createFileName(int i) {
        return SVG_PREFIX + i + SVG_SUFFIX;
    }

    public static Bitmap get(int i) {
        FileInputStream fileInputStream;
        Throwable th;
        try {
            fileInputStream = AndroidGraphicFactory.INSTANCE.openFileInput(createFileName(i));
            try {
                Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream);
                IOUtils.closeQuietly(fileInputStream);
                return decodeStream;
            } catch (FileNotFoundException unused) {
                IOUtils.closeQuietly(fileInputStream);
                return null;
            } catch (Throwable th2) {
                th = th2;
                IOUtils.closeQuietly(fileInputStream);
                throw th;
            }
        } catch (FileNotFoundException unused2) {
            fileInputStream = null;
            IOUtils.closeQuietly(fileInputStream);
            return null;
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            IOUtils.closeQuietly(fileInputStream);
            throw th;
        }
    }

    public static void put(int i, Bitmap bitmap) {
        new Thread(new SvgStorer(i, bitmap)).start();
    }

    private AndroidSvgBitmapStore() {
    }
}
