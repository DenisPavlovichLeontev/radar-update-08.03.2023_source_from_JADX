package mil.nga.geopackage.p010io;

import android.content.Context;
import java.io.File;

/* renamed from: mil.nga.geopackage.io.ContextIOUtils */
public class ContextIOUtils {
    public static File getInternalFile(Context context, String str) {
        if (str != null) {
            return new File(context.getFilesDir(), str);
        }
        return context.getFilesDir();
    }

    public static String getInternalFilePath(Context context, String str) {
        return getInternalFile(context, str).getAbsolutePath();
    }
}
