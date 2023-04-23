package org.mapsforge.map.reader.header;

import org.mapsforge.core.model.BoundingBox;

class SubFileParameterBuilder {
    byte baseZoomLevel;
    BoundingBox boundingBox;
    long indexStartAddress;
    long startAddress;
    long subFileSize;
    byte zoomLevelMax;
    byte zoomLevelMin;

    SubFileParameterBuilder() {
    }

    /* access modifiers changed from: package-private */
    public SubFileParameter build() {
        return new SubFileParameter(this);
    }
}
