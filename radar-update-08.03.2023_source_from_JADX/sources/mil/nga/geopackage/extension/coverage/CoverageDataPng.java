package mil.nga.geopackage.extension.coverage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.tiles.user.TileDao;
import mil.nga.geopackage.tiles.user.TileRow;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.ImageLineInt;
import p005ar.com.hjg.pngj.PngReader;
import p005ar.com.hjg.pngj.PngReaderInt;
import p005ar.com.hjg.pngj.PngWriter;

public class CoverageDataPng extends CoverageData<CoverageDataPngImage> {
    public CoverageDataPng(GeoPackage geoPackage, TileDao tileDao, Integer num, Integer num2, Projection projection) {
        super(geoPackage, tileDao, num, num2, projection);
    }

    public CoverageDataPng(GeoPackage geoPackage, TileDao tileDao) {
        this(geoPackage, tileDao, (Integer) null, (Integer) null, tileDao.getProjection());
    }

    public CoverageDataPng(GeoPackage geoPackage, TileDao tileDao, Projection projection) {
        this(geoPackage, tileDao, (Integer) null, (Integer) null, projection);
    }

    public CoverageDataPngImage createImage(TileRow tileRow) {
        return new CoverageDataPngImage(tileRow);
    }

    public double getValue(GriddedTile griddedTile, TileRow tileRow, int i, int i2) {
        return getValue(griddedTile, tileRow.getTileData(), i, i2).doubleValue();
    }

    public Double getValue(GriddedTile griddedTile, CoverageDataPngImage coverageDataPngImage, int i, int i2) {
        if (coverageDataPngImage.getReader() != null) {
            return getValue(griddedTile, coverageDataPngImage.getPixel(i, i2));
        }
        return getValue(griddedTile, coverageDataPngImage.getImageBytes(), i, i2);
    }

    public int getPixelValue(byte[] bArr, int i, int i2) {
        PngReaderInt pngReaderInt = new PngReaderInt((InputStream) new ByteArrayInputStream(bArr));
        validateImageType(pngReaderInt);
        int i3 = ((ImageLineInt) pngReaderInt.readRow(i2)).getScanline()[i];
        pngReaderInt.close();
        return i3;
    }

    public int[] getPixelValues(byte[] bArr) {
        PngReaderInt pngReaderInt = new PngReaderInt((InputStream) new ByteArrayInputStream(bArr));
        validateImageType(pngReaderInt);
        int[] iArr = new int[(pngReaderInt.imgInfo.cols * pngReaderInt.imgInfo.rows)];
        int i = 0;
        while (pngReaderInt.hasMoreRows()) {
            int[] scanline = pngReaderInt.readRowInt().getScanline();
            System.arraycopy(scanline, 0, iArr, pngReaderInt.imgInfo.cols * i, scanline.length);
            i++;
        }
        pngReaderInt.close();
        return iArr;
    }

    public static void validateImageType(PngReader pngReader) {
        if (pngReader == null) {
            throw new GeoPackageException("The image is null");
        } else if (pngReader.imgInfo.channels != 1 || pngReader.imgInfo.bitDepth != 16) {
            throw new GeoPackageException("The coverage data tile is expected to be a single channel 16 bit unsigned short, channels: " + pngReader.imgInfo.channels + ", bits: " + pngReader.imgInfo.bitDepth);
        }
    }

    public Double getValue(GriddedTile griddedTile, byte[] bArr, int i, int i2) {
        return getValue(griddedTile, getPixelValue(bArr, i, i2));
    }

    public Double[] getValues(GriddedTile griddedTile, byte[] bArr) {
        return getValues(griddedTile, getPixelValues(bArr));
    }

    public CoverageDataPngImage drawTile(short[] sArr, int i, int i2) {
        CoverageDataPngImage createImage = createImage(i, i2);
        PngWriter writer = createImage.getWriter();
        for (int i3 = 0; i3 < i2; i3++) {
            ImageLineInt imageLineInt = new ImageLineInt(writer.imgInfo, new int[i]);
            int[] scanline = imageLineInt.getScanline();
            for (int i4 = 0; i4 < i; i4++) {
                setPixelValue(scanline, i4, sArr[(i3 * i) + i4]);
            }
            writer.writeRow(imageLineInt);
        }
        writer.end();
        createImage.flushStream();
        return createImage;
    }

    public byte[] drawTileData(short[] sArr, int i, int i2) {
        return drawTile(sArr, i, i2).getImageBytes();
    }

    public CoverageDataPngImage drawTile(short[][] sArr) {
        int length = sArr[0].length;
        int length2 = sArr.length;
        CoverageDataPngImage createImage = createImage(length, length2);
        PngWriter writer = createImage.getWriter();
        for (int i = 0; i < length2; i++) {
            ImageLineInt imageLineInt = new ImageLineInt(writer.imgInfo, new int[length]);
            int[] scanline = imageLineInt.getScanline();
            for (int i2 = 0; i2 < length; i2++) {
                setPixelValue(scanline, i2, sArr[i][i2]);
            }
            writer.writeRow(imageLineInt);
        }
        writer.end();
        createImage.flushStream();
        return createImage;
    }

    public byte[] drawTileData(short[][] sArr) {
        return drawTile(sArr).getImageBytes();
    }

    public CoverageDataPngImage drawTile(int[] iArr, int i, int i2) {
        CoverageDataPngImage createImage = createImage(i, i2);
        PngWriter writer = createImage.getWriter();
        for (int i3 = 0; i3 < i2; i3++) {
            ImageLineInt imageLineInt = new ImageLineInt(writer.imgInfo, new int[i]);
            int[] scanline = imageLineInt.getScanline();
            for (int i4 = 0; i4 < i; i4++) {
                setPixelValue(scanline, i4, iArr[(i3 * i) + i4]);
            }
            writer.writeRow(imageLineInt);
        }
        writer.end();
        createImage.flushStream();
        return createImage;
    }

    public byte[] drawTileData(int[] iArr, int i, int i2) {
        return drawTile(iArr, i, i2).getImageBytes();
    }

    public CoverageDataPngImage drawTile(int[][] iArr) {
        int length = iArr[0].length;
        int length2 = iArr.length;
        CoverageDataPngImage createImage = createImage(length, length2);
        PngWriter writer = createImage.getWriter();
        for (int i = 0; i < length2; i++) {
            ImageLineInt imageLineInt = new ImageLineInt(writer.imgInfo, new int[length]);
            int[] scanline = imageLineInt.getScanline();
            for (int i2 = 0; i2 < length; i2++) {
                setPixelValue(scanline, i2, iArr[i][i2]);
            }
            writer.writeRow(imageLineInt);
        }
        writer.end();
        createImage.flushStream();
        return createImage;
    }

    public byte[] drawTileData(int[][] iArr) {
        return drawTile(iArr).getImageBytes();
    }

    public CoverageDataPngImage drawTile(GriddedTile griddedTile, Double[] dArr, int i, int i2) {
        CoverageDataPngImage createImage = createImage(i, i2);
        PngWriter writer = createImage.getWriter();
        for (int i3 = 0; i3 < i2; i3++) {
            ImageLineInt imageLineInt = new ImageLineInt(writer.imgInfo, new int[i]);
            int[] scanline = imageLineInt.getScanline();
            for (int i4 = 0; i4 < i; i4++) {
                setPixelValue(scanline, i4, getPixelValue(griddedTile, dArr[(i3 * i) + i4]));
            }
            writer.writeRow(imageLineInt);
        }
        writer.end();
        createImage.flushStream();
        return createImage;
    }

    public byte[] drawTileData(GriddedTile griddedTile, Double[] dArr, int i, int i2) {
        return drawTile(griddedTile, dArr, i, i2).getImageBytes();
    }

    public CoverageDataPngImage drawTile(GriddedTile griddedTile, Double[][] dArr) {
        int length = dArr[0].length;
        int length2 = dArr.length;
        CoverageDataPngImage createImage = createImage(length, length2);
        PngWriter writer = createImage.getWriter();
        for (int i = 0; i < length2; i++) {
            ImageLineInt imageLineInt = new ImageLineInt(writer.imgInfo, new int[length]);
            int[] scanline = imageLineInt.getScanline();
            for (int i2 = 0; i2 < length; i2++) {
                setPixelValue(scanline, i2, getPixelValue(griddedTile, dArr[i][i2]));
            }
            writer.writeRow(imageLineInt);
        }
        writer.end();
        createImage.flushStream();
        return createImage;
    }

    public byte[] drawTileData(GriddedTile griddedTile, Double[][] dArr) {
        return drawTile(griddedTile, dArr).getImageBytes();
    }

    public CoverageDataPngImage createImage(int i, int i2) {
        return new CoverageDataPngImage(new ImageInfo(i, i2, 16, false, true, false));
    }

    public void setPixelValue(ImageLineInt imageLineInt, int i, short s) {
        setPixelValue(imageLineInt.getScanline(), i, s);
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [int[]] */
    /* JADX WARNING: type inference failed for: r3v0, types: [short] */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setPixelValue(int[] r1, int r2, short r3) {
        /*
            r0 = this;
            r1[r2] = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.extension.coverage.CoverageDataPng.setPixelValue(int[], int, short):void");
    }

    public void setPixelValue(ImageLineInt imageLineInt, int i, int i2) {
        setPixelValue(imageLineInt, i, getPixelValue(i2));
    }

    public void setPixelValue(int[] iArr, int i, int i2) {
        setPixelValue(iArr, i, getPixelValue(i2));
    }

    public static CoverageDataPng createTileTableWithMetadata(GeoPackage geoPackage, String str, BoundingBox boundingBox, long j, BoundingBox boundingBox2, long j2) {
        return (CoverageDataPng) CoverageData.createTileTableWithMetadata(geoPackage, str, boundingBox, j, boundingBox2, j2, GriddedCoverageDataType.INTEGER);
    }
}
