package mil.nga.tiff.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import mil.nga.tiff.util.TiffException;

public class DeflateCompression implements CompressionDecoder, CompressionEncoder {
    public boolean rowEncoding() {
        return false;
    }

    public byte[] decode(byte[] bArr, ByteOrder byteOrder) {
        try {
            Inflater inflater = new Inflater();
            inflater.setInput(bArr);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
            byte[] bArr2 = new byte[1024];
            while (!inflater.finished()) {
                byteArrayOutputStream.write(bArr2, 0, inflater.inflate(bArr2));
            }
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new TiffException("Failed close decoded byte stream", e);
        } catch (DataFormatException e2) {
            throw new TiffException("Data format error while decoding stream", e2);
        }
    }

    public byte[] encode(byte[] bArr, ByteOrder byteOrder) {
        try {
            Deflater deflater = new Deflater();
            deflater.setInput(bArr);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
            deflater.finish();
            byte[] bArr2 = new byte[1024];
            while (!deflater.finished()) {
                byteArrayOutputStream.write(bArr2, 0, deflater.deflate(bArr2));
            }
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new TiffException("Failed close encoded stream", e);
        }
    }
}
