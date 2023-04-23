package org.mapsforge.map.reader.header;

import kotlin.jvm.internal.ByteCompanionObject;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.reader.ReadBuffer;

final class OptionalFields {
    private static final int HEADER_BITMASK_COMMENT = 8;
    private static final int HEADER_BITMASK_CREATED_BY = 4;
    private static final int HEADER_BITMASK_DEBUG = 128;
    private static final int HEADER_BITMASK_LANGUAGES_PREFERENCE = 16;
    private static final int HEADER_BITMASK_START_POSITION = 64;
    private static final int HEADER_BITMASK_START_ZOOM_LEVEL = 32;
    private static final int START_ZOOM_LEVEL_MAX = 22;
    String comment;
    String createdBy;
    final boolean hasComment;
    final boolean hasCreatedBy;
    final boolean hasLanguagesPreference;
    final boolean hasStartPosition;
    final boolean hasStartZoomLevel;
    final boolean isDebugFile;
    String languagesPreference;
    LatLong startPosition;
    Byte startZoomLevel;

    static void readOptionalFields(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        OptionalFields optionalFields = new OptionalFields(readBuffer.readByte());
        mapFileInfoBuilder.optionalFields = optionalFields;
        optionalFields.readOptionalFields(readBuffer);
    }

    private OptionalFields(byte b) {
        boolean z = true;
        this.isDebugFile = (b & ByteCompanionObject.MIN_VALUE) != 0;
        this.hasStartPosition = (b & 64) != 0;
        this.hasStartZoomLevel = (b & 32) != 0;
        this.hasLanguagesPreference = (b & 16) != 0;
        this.hasComment = (b & 8) != 0;
        this.hasCreatedBy = (b & 4) == 0 ? false : z;
    }

    private void readLanguagesPreference(ReadBuffer readBuffer) {
        if (this.hasLanguagesPreference) {
            this.languagesPreference = readBuffer.readUTF8EncodedString();
        }
    }

    private void readMapStartPosition(ReadBuffer readBuffer) {
        if (this.hasStartPosition) {
            try {
                this.startPosition = new LatLong(LatLongUtils.microdegreesToDegrees(readBuffer.readInt()), LatLongUtils.microdegreesToDegrees(readBuffer.readInt()));
            } catch (IllegalArgumentException e) {
                throw new MapFileException(e.getMessage());
            }
        }
    }

    private void readMapStartZoomLevel(ReadBuffer readBuffer) {
        if (this.hasStartZoomLevel) {
            byte readByte = readBuffer.readByte();
            if (readByte < 0 || readByte > 22) {
                throw new MapFileException("invalid map start zoom level: " + readByte);
            }
            this.startZoomLevel = Byte.valueOf(readByte);
        }
    }

    private void readOptionalFields(ReadBuffer readBuffer) {
        readMapStartPosition(readBuffer);
        readMapStartZoomLevel(readBuffer);
        readLanguagesPreference(readBuffer);
        if (this.hasComment) {
            this.comment = readBuffer.readUTF8EncodedString();
        }
        if (this.hasCreatedBy) {
            this.createdBy = readBuffer.readUTF8EncodedString();
        }
    }
}
