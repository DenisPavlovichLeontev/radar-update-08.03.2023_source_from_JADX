package org.mapsforge.map.datastore;

import java.util.Arrays;
import java.util.List;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Tag;

public class Way {
    public final LatLong labelPosition;
    public final LatLong[][] latLongs;
    public final byte layer;
    public final List<Tag> tags;

    public Way(byte b, List<Tag> list, LatLong[][] latLongArr, LatLong latLong) {
        this.layer = b;
        this.tags = list;
        this.latLongs = latLongArr;
        this.labelPosition = latLong;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Way)) {
            return false;
        }
        Way way = (Way) obj;
        if (this.layer != way.layer || !this.tags.equals(way.tags)) {
            return false;
        }
        LatLong latLong = this.labelPosition;
        if (latLong == null && way.labelPosition != null) {
            return false;
        }
        if ((latLong != null && !latLong.equals(way.labelPosition)) || this.latLongs.length != way.latLongs.length) {
            return false;
        }
        int i = 0;
        while (true) {
            LatLong[][] latLongArr = this.latLongs;
            if (i < latLongArr.length) {
                if (latLongArr[i].length == way.latLongs[i].length) {
                    int i2 = 0;
                    while (true) {
                        LatLong[] latLongArr2 = this.latLongs[i];
                        if (i2 >= latLongArr2.length) {
                            break;
                        } else if (!latLongArr2[i2].equals(way.latLongs[i][i2])) {
                            return false;
                        } else {
                            i2++;
                        }
                    }
                } else {
                    return false;
                }
            } else {
                return true;
            }
            i++;
        }
    }

    public int hashCode() {
        int hashCode = ((((this.layer + 31) * 31) + this.tags.hashCode()) * 31) + Arrays.deepHashCode(this.latLongs);
        LatLong latLong = this.labelPosition;
        return latLong != null ? (hashCode * 31) + latLong.hashCode() : hashCode;
    }
}
