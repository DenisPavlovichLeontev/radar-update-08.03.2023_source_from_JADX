package mil.nga.geopackage.p010io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import mil.nga.geopackage.property.PropertyConstants;
import okhttp3.internal.p014ws.RealWebSocket;

/* renamed from: mil.nga.geopackage.io.GeoPackageIOUtils */
public class GeoPackageIOUtils {
    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(PropertyConstants.PROPERTY_DIVIDER);
        if (lastIndexOf > -1) {
            return name.substring(lastIndexOf + 1);
        }
        return null;
    }

    public static String getFileNameWithoutExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(PropertyConstants.PROPERTY_DIVIDER);
        return lastIndexOf > -1 ? name.substring(0, lastIndexOf) : name;
    }

    public static void copyFile(File file, File file2) throws IOException {
        copyStream((InputStream) new FileInputStream(file), (OutputStream) new FileOutputStream(file2));
    }

    public static void copyStream(InputStream inputStream, File file) throws IOException {
        copyStream(inputStream, file, (GeoPackageProgress) null);
    }

    public static void copyStream(InputStream inputStream, File file, GeoPackageProgress geoPackageProgress) throws IOException {
        copyStream(inputStream, (OutputStream) new FileOutputStream(file), geoPackageProgress);
        if (geoPackageProgress != null && !geoPackageProgress.isActive() && geoPackageProgress.cleanupOnCancel()) {
            file.delete();
        }
    }

    public static byte[] fileBytes(File file) throws IOException {
        return streamBytes(new FileInputStream(file));
    }

    public static byte[] streamBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyStream(inputStream, (OutputStream) byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        copyStream(inputStream, outputStream, (GeoPackageProgress) null);
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream, GeoPackageProgress geoPackageProgress) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            if (geoPackageProgress != null && !geoPackageProgress.isActive()) {
                break;
            }
            int read = inputStream.read(bArr);
            if (read <= 0) {
                break;
            }
            outputStream.write(bArr, 0, read);
            if (geoPackageProgress != null) {
                geoPackageProgress.addProgress(read);
            }
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public static String formatBytes(long j) {
        double d = (double) j;
        String str = "B";
        if (j >= RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE) {
            int min = Math.min((int) (Math.log(d) / Math.log(1024.0d)), 4);
            if (min == 1) {
                str = "KB";
            } else if (min == 2) {
                str = "MB";
            } else if (min == 3) {
                str = "GB";
            } else if (min == 4) {
                str = "TB";
            }
            d /= Math.pow(1024.0d, (double) min);
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(d) + " " + str;
    }
}
