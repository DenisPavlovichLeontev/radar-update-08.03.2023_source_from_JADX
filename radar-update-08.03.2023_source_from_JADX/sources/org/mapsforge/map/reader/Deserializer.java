package org.mapsforge.map.reader;

import kotlin.UByte;

final class Deserializer {
    static long getFiveBytesLong(byte[] bArr, int i) {
        return (((long) bArr[i + 4]) & 255) | ((((long) bArr[i]) & 255) << 32) | ((((long) bArr[i + 1]) & 255) << 24) | ((((long) bArr[i + 2]) & 255) << 16) | ((((long) bArr[i + 3]) & 255) << 8);
    }

    static int getInt(byte[] bArr, int i) {
        return (bArr[i + 3] & UByte.MAX_VALUE) | (bArr[i] << 24) | ((bArr[i + 1] & UByte.MAX_VALUE) << 16) | ((bArr[i + 2] & UByte.MAX_VALUE) << 8);
    }

    static long getLong(byte[] bArr, int i) {
        return (((long) bArr[i + 7]) & 255) | ((((long) bArr[i]) & 255) << 56) | ((((long) bArr[i + 1]) & 255) << 48) | ((((long) bArr[i + 2]) & 255) << 40) | ((((long) bArr[i + 3]) & 255) << 32) | ((((long) bArr[i + 4]) & 255) << 24) | ((((long) bArr[i + 5]) & 255) << 16) | ((((long) bArr[i + 6]) & 255) << 8);
    }

    static int getShort(byte[] bArr, int i) {
        return (bArr[i + 1] & UByte.MAX_VALUE) | (bArr[i] << 8);
    }

    private Deserializer() {
        throw new IllegalStateException();
    }
}
