package org.mapsforge.map.reader.header;

import java.io.IOException;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Tag;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.reader.ReadBuffer;

final class RequiredFields {
    private static final String BINARY_OSM_MAGIC_BYTE = "mapsforge binary OSM";
    private static final int HEADER_SIZE_MAX = 1000000;
    private static final int HEADER_SIZE_MIN = 70;
    private static final String MERCATOR = "Mercator";
    private static final int SUPPORTED_FILE_VERSION_MAX = 5;
    private static final int SUPPORTED_FILE_VERSION_MIN = 3;

    static void readBoundingBox(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        try {
            mapFileInfoBuilder.boundingBox = new BoundingBox(LatLongUtils.microdegreesToDegrees(readBuffer.readInt()), LatLongUtils.microdegreesToDegrees(readBuffer.readInt()), LatLongUtils.microdegreesToDegrees(readBuffer.readInt()), LatLongUtils.microdegreesToDegrees(readBuffer.readInt()));
        } catch (IllegalArgumentException e) {
            throw new MapFileException(e.getMessage());
        }
    }

    static void readFileSize(ReadBuffer readBuffer, long j, MapFileInfoBuilder mapFileInfoBuilder) {
        long readLong = readBuffer.readLong();
        if (readLong == j) {
            mapFileInfoBuilder.fileSize = j;
            return;
        }
        throw new MapFileException("invalid file size: " + readLong);
    }

    static void readFileVersion(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        int readInt = readBuffer.readInt();
        if (readInt < 3 || readInt > 5) {
            throw new MapFileException("unsupported file version: " + readInt);
        }
        mapFileInfoBuilder.fileVersion = readInt;
    }

    static void readMagicByte(ReadBuffer readBuffer) throws IOException {
        if (readBuffer.readFromFile(24)) {
            String readUTF8EncodedString = readBuffer.readUTF8EncodedString(20);
            if (!BINARY_OSM_MAGIC_BYTE.equals(readUTF8EncodedString)) {
                throw new MapFileException("invalid magic byte: " + readUTF8EncodedString);
            }
            return;
        }
        throw new MapFileException("reading magic byte has failed");
    }

    static void readMapDate(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        long readLong = readBuffer.readLong();
        if (readLong >= 1200000000000L) {
            mapFileInfoBuilder.mapDate = readLong;
            return;
        }
        throw new MapFileException("invalid map date: " + readLong);
    }

    static void readPoiTags(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        int readShort = readBuffer.readShort();
        if (readShort >= 0) {
            Tag[] tagArr = new Tag[readShort];
            int i = 0;
            while (i < readShort) {
                String readUTF8EncodedString = readBuffer.readUTF8EncodedString();
                if (readUTF8EncodedString != null) {
                    tagArr[i] = new Tag(readUTF8EncodedString);
                    i++;
                } else {
                    throw new MapFileException("POI tag must not be null: " + i);
                }
            }
            mapFileInfoBuilder.poiTags = tagArr;
            return;
        }
        throw new MapFileException("invalid number of POI tags: " + readShort);
    }

    static void readProjectionName(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        String readUTF8EncodedString = readBuffer.readUTF8EncodedString();
        if (MERCATOR.equals(readUTF8EncodedString)) {
            mapFileInfoBuilder.projectionName = readUTF8EncodedString;
            return;
        }
        throw new MapFileException("unsupported projection: " + readUTF8EncodedString);
    }

    static void readRemainingHeader(ReadBuffer readBuffer) throws IOException {
        int readInt = readBuffer.readInt();
        if (readInt < 70 || readInt > 1000000) {
            throw new MapFileException("invalid remaining header size: " + readInt);
        } else if (!readBuffer.readFromFile(readInt)) {
            throw new MapFileException("reading header data has failed: " + readInt);
        }
    }

    static void readTilePixelSize(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        mapFileInfoBuilder.tilePixelSize = readBuffer.readShort();
    }

    static void readWayTags(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        int readShort = readBuffer.readShort();
        if (readShort >= 0) {
            Tag[] tagArr = new Tag[readShort];
            int i = 0;
            while (i < readShort) {
                String readUTF8EncodedString = readBuffer.readUTF8EncodedString();
                if (readUTF8EncodedString != null) {
                    tagArr[i] = new Tag(readUTF8EncodedString);
                    i++;
                } else {
                    throw new MapFileException("way tag must not be null: " + i);
                }
            }
            mapFileInfoBuilder.wayTags = tagArr;
            return;
        }
        throw new MapFileException("invalid number of way tags: " + readShort);
    }

    private RequiredFields() {
        throw new IllegalStateException();
    }
}
