package org.osmdroid.gpkg.overlay.features;

public interface ShapeWithChildrenMarkers extends ShapeMarkers {
    ShapeMarkers createChild();
}
