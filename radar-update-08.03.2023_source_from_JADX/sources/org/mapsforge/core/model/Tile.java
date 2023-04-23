package org.mapsforge.core.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.mapsforge.core.util.MercatorProjection;

public class Tile implements Serializable {
    private static final long serialVersionUID = 1;
    private BoundingBox boundingBox;
    public final long mapSize;
    private Point origin;
    public final int tileSize;
    public final int tileX;
    public final int tileY;
    public final byte zoomLevel;

    public static BoundingBox getBoundingBox(Tile tile, Tile tile2) {
        return tile.getBoundingBox().extendBoundingBox(tile2.getBoundingBox());
    }

    public static Rectangle getBoundaryAbsolute(Tile tile, Tile tile2) {
        return new Rectangle(tile.getOrigin().f381x, tile.getOrigin().f382y, tile2.getOrigin().f381x + ((double) tile.tileSize), tile2.getOrigin().f382y + ((double) tile.tileSize));
    }

    public static boolean tileAreasOverlap(Tile tile, Tile tile2, Tile tile3, Tile tile4) {
        if (tile.zoomLevel != tile3.zoomLevel) {
            return false;
        }
        if (!tile.equals(tile3) || !tile2.equals(tile4)) {
            return getBoundaryAbsolute(tile, tile2).intersects(getBoundaryAbsolute(tile3, tile4));
        }
        return true;
    }

    public static int getMaxTileNumber(byte b) {
        if (b < 0) {
            throw new IllegalArgumentException("zoomLevel must not be negative: " + b);
        } else if (b == 0) {
            return 0;
        } else {
            return (2 << (b - 1)) - 1;
        }
    }

    public Tile(int i, int i2, byte b, int i3) {
        if (i < 0) {
            throw new IllegalArgumentException("tileX must not be negative: " + i);
        } else if (i2 < 0) {
            throw new IllegalArgumentException("tileY must not be negative: " + i2);
        } else if (b >= 0) {
            long maxTileNumber = (long) getMaxTileNumber(b);
            if (((long) i) > maxTileNumber) {
                throw new IllegalArgumentException("invalid tileX number on zoom level " + b + ": " + i);
            } else if (((long) i2) <= maxTileNumber) {
                this.tileSize = i3;
                this.tileX = i;
                this.tileY = i2;
                this.zoomLevel = b;
                this.mapSize = MercatorProjection.getMapSize(b, i3);
            } else {
                throw new IllegalArgumentException("invalid tileY number on zoom level " + b + ": " + i2);
            }
        } else {
            throw new IllegalArgumentException("zoomLevel must not be negative: " + b);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tile)) {
            return false;
        }
        Tile tile = (Tile) obj;
        return this.tileX == tile.tileX && this.tileY == tile.tileY && this.zoomLevel == tile.zoomLevel && this.tileSize == tile.tileSize;
    }

    public BoundingBox getBoundingBox() {
        if (this.boundingBox == null) {
            double max = Math.max(-85.05112877980659d, MercatorProjection.tileYToLatitude((long) (this.tileY + 1), this.zoomLevel));
            double max2 = Math.max(-180.0d, MercatorProjection.tileXToLongitude((long) this.tileX, this.zoomLevel));
            double min = Math.min(85.05112877980659d, MercatorProjection.tileYToLatitude((long) this.tileY, this.zoomLevel));
            double d = 180.0d;
            double min2 = Math.min(180.0d, MercatorProjection.tileXToLongitude((long) (this.tileX + 1), this.zoomLevel));
            if (min2 != -180.0d) {
                d = min2;
            }
            this.boundingBox = new BoundingBox(max, max2, min, d);
        }
        return this.boundingBox;
    }

    public Set<Tile> getNeighbours() {
        HashSet hashSet = new HashSet(8);
        hashSet.add(getLeft());
        hashSet.add(getAboveLeft());
        hashSet.add(getAbove());
        hashSet.add(getAboveRight());
        hashSet.add(getRight());
        hashSet.add(getBelowRight());
        hashSet.add(getBelow());
        hashSet.add(getBelowLeft());
        return hashSet;
    }

    public Rectangle getBoundaryAbsolute() {
        return new Rectangle(getOrigin().f381x, getOrigin().f382y, getOrigin().f381x + ((double) this.tileSize), getOrigin().f382y + ((double) this.tileSize));
    }

    public Rectangle getBoundaryRelative() {
        int i = this.tileSize;
        return new Rectangle(0.0d, 0.0d, (double) i, (double) i);
    }

    public Point getOrigin() {
        if (this.origin == null) {
            this.origin = new Point((double) MercatorProjection.tileToPixel((long) this.tileX, this.tileSize), (double) MercatorProjection.tileToPixel((long) this.tileY, this.tileSize));
        }
        return this.origin;
    }

    public Tile getLeft() {
        int i = this.tileX - 1;
        if (i < 0) {
            i = getMaxTileNumber(this.zoomLevel);
        }
        return new Tile(i, this.tileY, this.zoomLevel, this.tileSize);
    }

    public Tile getRight() {
        int i = this.tileX + 1;
        if (i > getMaxTileNumber(this.zoomLevel)) {
            i = 0;
        }
        return new Tile(i, this.tileY, this.zoomLevel, this.tileSize);
    }

    public Tile getAbove() {
        int i = this.tileY - 1;
        if (i < 0) {
            i = getMaxTileNumber(this.zoomLevel);
        }
        return new Tile(this.tileX, i, this.zoomLevel, this.tileSize);
    }

    public Tile getBelow() {
        int i = this.tileY + 1;
        if (i > getMaxTileNumber(this.zoomLevel)) {
            i = 0;
        }
        return new Tile(this.tileX, i, this.zoomLevel, this.tileSize);
    }

    public Tile getAboveLeft() {
        int i = this.tileY - 1;
        int i2 = this.tileX - 1;
        if (i < 0) {
            i = getMaxTileNumber(this.zoomLevel);
        }
        if (i2 < 0) {
            i2 = getMaxTileNumber(this.zoomLevel);
        }
        return new Tile(i2, i, this.zoomLevel, this.tileSize);
    }

    public Tile getAboveRight() {
        int i = this.tileY - 1;
        int i2 = this.tileX + 1;
        if (i < 0) {
            i = getMaxTileNumber(this.zoomLevel);
        }
        if (i2 > getMaxTileNumber(this.zoomLevel)) {
            i2 = 0;
        }
        return new Tile(i2, i, this.zoomLevel, this.tileSize);
    }

    public Tile getBelowLeft() {
        int i = this.tileY + 1;
        int i2 = this.tileX - 1;
        if (i > getMaxTileNumber(this.zoomLevel)) {
            i = 0;
        }
        if (i2 < 0) {
            i2 = getMaxTileNumber(this.zoomLevel);
        }
        return new Tile(i2, i, this.zoomLevel, this.tileSize);
    }

    public Tile getBelowRight() {
        int i = this.tileY + 1;
        int i2 = this.tileX + 1;
        if (i > getMaxTileNumber(this.zoomLevel)) {
            i = 0;
        }
        if (i2 > getMaxTileNumber(this.zoomLevel)) {
            i2 = 0;
        }
        return new Tile(i2, i, this.zoomLevel, this.tileSize);
    }

    public Tile getParent() {
        byte b = this.zoomLevel;
        if (b == 0) {
            return null;
        }
        return new Tile(this.tileX / 2, this.tileY / 2, (byte) (b - 1), this.tileSize);
    }

    public int getShiftX(Tile tile) {
        if (equals(tile)) {
            return 0;
        }
        return (this.tileX % 2) + (getParent().getShiftX(tile) * 2);
    }

    public int getShiftY(Tile tile) {
        if (equals(tile)) {
            return 0;
        }
        return (this.tileY % 2) + (getParent().getShiftY(tile) * 2);
    }

    public int hashCode() {
        int i = this.tileX;
        int i2 = this.tileY;
        return ((((((217 + (i ^ (i >>> 16))) * 31) + (i2 ^ (i2 >>> 16))) * 31) + this.zoomLevel) * 31) + this.tileSize;
    }

    public String toString() {
        return "x=" + this.tileX + ", y=" + this.tileY + ", z=" + this.zoomLevel;
    }
}
