package mil.nga.tiff.compression;

import java.nio.ByteOrder;

public interface CompressionDecoder {
    byte[] decode(byte[] bArr, ByteOrder byteOrder);
}
