package mil.nga.tiff.compression;

import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;
import mil.nga.tiff.p011io.ByteReader;
import mil.nga.tiff.util.TiffException;

public class PackbitsCompression implements CompressionDecoder, CompressionEncoder {
    public boolean rowEncoding() {
        return true;
    }

    public byte[] decode(byte[] bArr, ByteOrder byteOrder) {
        ByteReader byteReader = new ByteReader(bArr, byteOrder);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (byteReader.hasByte()) {
            byte readByte = byteReader.readByte();
            if (readByte != Byte.MIN_VALUE) {
                int i = 0;
                if (readByte < 0) {
                    short readUnsignedByte = byteReader.readUnsignedByte();
                    int i2 = -readByte;
                    while (i <= i2) {
                        byteArrayOutputStream.write(readUnsignedByte);
                        i++;
                    }
                } else {
                    while (i <= readByte) {
                        byteArrayOutputStream.write(byteReader.readUnsignedByte());
                        i++;
                    }
                }
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] encode(byte[] bArr, ByteOrder byteOrder) {
        throw new TiffException("Packbits encoder is not yet implemented");
    }
}
