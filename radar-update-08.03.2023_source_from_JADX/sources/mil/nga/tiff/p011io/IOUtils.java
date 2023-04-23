package mil.nga.tiff.p011io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* renamed from: mil.nga.tiff.io.IOUtils */
public class IOUtils {
    public static void copyFile(File file, File file2) throws IOException {
        copyStream((InputStream) new FileInputStream(file), (OutputStream) new FileOutputStream(file2));
    }

    public static void copyStream(InputStream inputStream, File file) throws IOException {
        copyStream(inputStream, (OutputStream) new FileOutputStream(file));
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
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read > 0) {
                outputStream.write(bArr, 0, read);
            } else {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                return;
            }
        }
    }
}
