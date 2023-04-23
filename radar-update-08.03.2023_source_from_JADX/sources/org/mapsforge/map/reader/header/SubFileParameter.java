package org.mapsforge.map.reader.header;

import org.mapsforge.core.util.MercatorProjection;

public class SubFileParameter {
    public static final byte BYTES_PER_INDEX_ENTRY = 5;
    public final byte baseZoomLevel;
    public final long blocksHeight;
    public final long blocksWidth;
    public final long boundaryTileBottom;
    public final long boundaryTileLeft;
    public final long boundaryTileRight;
    public final long boundaryTileTop;
    private final int hashCodeValue = calculateHashCode();
    public final long indexEndAddress;
    public final long indexStartAddress;
    public final long numberOfBlocks;
    public final long startAddress;
    public final long subFileSize;
    public final byte zoomLevelMax;
    public final byte zoomLevelMin;

    SubFileParameter(SubFileParameterBuilder subFileParameterBuilder) {
        this.startAddress = subFileParameterBuilder.startAddress;
        long j = subFileParameterBuilder.indexStartAddress;
        this.indexStartAddress = j;
        this.subFileSize = subFileParameterBuilder.subFileSize;
        byte b = subFileParameterBuilder.baseZoomLevel;
        this.baseZoomLevel = b;
        this.zoomLevelMin = subFileParameterBuilder.zoomLevelMin;
        this.zoomLevelMax = subFileParameterBuilder.zoomLevelMax;
        long latitudeToTileY = (long) MercatorProjection.latitudeToTileY(subFileParameterBuilder.boundingBox.minLatitude, b);
        this.boundaryTileBottom = latitudeToTileY;
        long longitudeToTileX = (long) MercatorProjection.longitudeToTileX(subFileParameterBuilder.boundingBox.minLongitude, b);
        this.boundaryTileLeft = longitudeToTileX;
        long latitudeToTileY2 = (long) MercatorProjection.latitudeToTileY(subFileParameterBuilder.boundingBox.maxLatitude, b);
        this.boundaryTileTop = latitudeToTileY2;
        long longitudeToTileX2 = (long) MercatorProjection.longitudeToTileX(subFileParameterBuilder.boundingBox.maxLongitude, b);
        this.boundaryTileRight = longitudeToTileX2;
        long j2 = (longitudeToTileX2 - longitudeToTileX) + 1;
        this.blocksWidth = j2;
        long j3 = (latitudeToTileY - latitudeToTileY2) + 1;
        this.blocksHeight = j3;
        long j4 = j2 * j3;
        this.numberOfBlocks = j4;
        this.indexEndAddress = j + (j4 * 5);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SubFileParameter)) {
            return false;
        }
        SubFileParameter subFileParameter = (SubFileParameter) obj;
        return this.startAddress == subFileParameter.startAddress && this.subFileSize == subFileParameter.subFileSize && this.baseZoomLevel == subFileParameter.baseZoomLevel;
    }

    public int hashCode() {
        return this.hashCodeValue;
    }

    private int calculateHashCode() {
        long j = this.startAddress;
        long j2 = this.subFileSize;
        return ((((217 + ((int) (j ^ (j >>> 32)))) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 31) + this.baseZoomLevel;
    }
}
