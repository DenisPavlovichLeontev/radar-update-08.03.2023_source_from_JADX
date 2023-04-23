package mil.nga.tiff.compression;

import java.nio.ByteOrder;

public class RawCompression implements CompressionDecoder, CompressionEncoder {
    public byte[] decode(byte[] bArr, ByteOrder byteOrder) {
        return bArr;
    }

    public byte[] encode(byte[] bArr, ByteOrder byteOrder) {
        return bArr;
    }

    public boolean rowEncoding() {
        return false;
    }
}
