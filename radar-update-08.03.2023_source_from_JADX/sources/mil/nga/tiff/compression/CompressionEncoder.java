package mil.nga.tiff.compression;

import java.nio.ByteOrder;

public interface CompressionEncoder {
    byte[] encode(byte[] bArr, ByteOrder byteOrder);

    boolean rowEncoding();
}
