package mil.nga.geopackage.tiles.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import mil.nga.geopackage.p010io.BitmapConverter;
import mil.nga.geopackage.user.UserRow;

public class TileRow extends UserRow<TileColumn, TileTable> {
    TileRow(TileTable tileTable, int[] iArr, Object[] objArr) {
        super(tileTable, iArr, objArr);
    }

    TileRow(TileTable tileTable) {
        super(tileTable);
    }

    public TileRow(TileRow tileRow) {
        super(tileRow);
    }

    public int getZoomLevelColumnIndex() {
        return ((TileTable) getTable()).getZoomLevelColumnIndex();
    }

    public TileColumn getZoomLevelColumn() {
        return ((TileTable) getTable()).getZoomLevelColumn();
    }

    public long getZoomLevel() {
        return ((Number) getValue(getZoomLevelColumnIndex())).longValue();
    }

    public void setZoomLevel(long j) {
        setValue(getZoomLevelColumnIndex(), (Object) Long.valueOf(j));
    }

    public int getTileColumnColumnIndex() {
        return ((TileTable) getTable()).getTileColumnColumnIndex();
    }

    public TileColumn getTileColumnColumn() {
        return ((TileTable) getTable()).getTileColumnColumn();
    }

    public long getTileColumn() {
        return ((Number) getValue(getTileColumnColumnIndex())).longValue();
    }

    public void setTileColumn(long j) {
        setValue(getTileColumnColumnIndex(), (Object) Long.valueOf(j));
    }

    public int getTileRowColumnIndex() {
        return ((TileTable) getTable()).getTileRowColumnIndex();
    }

    public TileColumn getTileRowColumn() {
        return ((TileTable) getTable()).getTileRowColumn();
    }

    public long getTileRow() {
        return ((Number) getValue(getTileRowColumnIndex())).longValue();
    }

    public void setTileRow(long j) {
        setValue(getTileRowColumnIndex(), (Object) Long.valueOf(j));
    }

    public int getTileDataColumnIndex() {
        return ((TileTable) getTable()).getTileDataColumnIndex();
    }

    public TileColumn getTileDataColumn() {
        return ((TileTable) getTable()).getTileDataColumn();
    }

    public byte[] getTileData() {
        return (byte[]) getValue(getTileDataColumnIndex());
    }

    public void setTileData(byte[] bArr) {
        setValue(getTileDataColumnIndex(), (Object) bArr);
    }

    public Bitmap getTileDataBitmap() {
        return getTileDataBitmap((BitmapFactory.Options) null);
    }

    public Bitmap getTileDataBitmap(BitmapFactory.Options options) {
        return BitmapConverter.toBitmap(getTileData(), options);
    }

    public void setTileData(Bitmap bitmap, Bitmap.CompressFormat compressFormat) throws IOException {
        setTileData(bitmap, compressFormat, 100);
    }

    public void setTileData(Bitmap bitmap, Bitmap.CompressFormat compressFormat, int i) throws IOException {
        setTileData(BitmapConverter.toBytes(bitmap, compressFormat, i));
    }
}
