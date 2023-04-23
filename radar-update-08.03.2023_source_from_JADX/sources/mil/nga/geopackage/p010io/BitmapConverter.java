package mil.nga.geopackage.p010io;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* renamed from: mil.nga.geopackage.io.BitmapConverter */
public class BitmapConverter {
    public static Bitmap toBitmap(byte[] bArr) {
        return toBitmap(bArr, (BitmapFactory.Options) null);
    }

    public static Bitmap toBitmap(byte[] bArr, BitmapFactory.Options options) {
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
    }

    public static byte[] toBytes(Bitmap bitmap, Bitmap.CompressFormat compressFormat) throws IOException {
        return toBytes(bitmap, compressFormat, 100);
    }

    public static byte[] toBytes(Bitmap bitmap, Bitmap.CompressFormat compressFormat, int i) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            bitmap.compress(compressFormat, i, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } finally {
            byteArrayOutputStream.close();
        }
    }
}
