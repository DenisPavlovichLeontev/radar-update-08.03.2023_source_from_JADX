package mil.nga.geopackage.extension.coverage;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.tiles.user.TileDao;
import mil.nga.geopackage.tiles.user.TileRow;
import mil.nga.tiff.FileDirectory;
import mil.nga.tiff.Rasters;
import mil.nga.tiff.TiffReader;

public class CoverageDataTiff extends CoverageData<CoverageDataTiffImage> {
    public static final int BITS_PER_SAMPLE = 32;
    public static final int SAMPLES_PER_PIXEL = 1;

    public CoverageDataTiff(GeoPackage geoPackage, TileDao tileDao, Integer num, Integer num2, Projection projection) {
        super(geoPackage, tileDao, num, num2, projection);
    }

    public CoverageDataTiff(GeoPackage geoPackage, TileDao tileDao) {
        this(geoPackage, tileDao, (Integer) null, (Integer) null, tileDao.getProjection());
    }

    public CoverageDataTiff(GeoPackage geoPackage, TileDao tileDao, Projection projection) {
        this(geoPackage, tileDao, (Integer) null, (Integer) null, projection);
    }

    public CoverageDataTiffImage createImage(TileRow tileRow) {
        return new CoverageDataTiffImage(tileRow);
    }

    public double getValue(GriddedTile griddedTile, TileRow tileRow, int i, int i2) {
        return getValue(griddedTile, tileRow.getTileData(), i, i2).doubleValue();
    }

    public Double getValue(GriddedTile griddedTile, CoverageDataTiffImage coverageDataTiffImage, int i, int i2) {
        if (coverageDataTiffImage.getDirectory() != null) {
            return getValue(griddedTile, coverageDataTiffImage.getPixel(i, i2));
        }
        return getValue(griddedTile, coverageDataTiffImage.getImageBytes(), i, i2);
    }

    public float getPixelValue(byte[] bArr, int i, int i2) {
        FileDirectory fileDirectory = TiffReader.readTiff(bArr).getFileDirectory();
        validateImageType(fileDirectory);
        return fileDirectory.readRasters().getFirstPixelSample(i, i2).floatValue();
    }

    public float[] getPixelValues(byte[] bArr) {
        FileDirectory fileDirectory = TiffReader.readTiff(bArr).getFileDirectory();
        validateImageType(fileDirectory);
        Rasters readRasters = fileDirectory.readRasters();
        float[] fArr = new float[(readRasters.getWidth() * readRasters.getHeight())];
        for (int i = 0; i < readRasters.getHeight(); i++) {
            for (int i2 = 0; i2 < readRasters.getWidth(); i2++) {
                fArr[readRasters.getSampleIndex(i2, i)] = readRasters.getPixelSample(0, i2, i).floatValue();
            }
        }
        return fArr;
    }

    public static void validateImageType(FileDirectory fileDirectory) {
        if (fileDirectory != null) {
            int samplesPerPixel = fileDirectory.getSamplesPerPixel();
            Integer num = null;
            Integer num2 = (fileDirectory.getBitsPerSample() == null || fileDirectory.getBitsPerSample().isEmpty()) ? null : fileDirectory.getBitsPerSample().get(0);
            if (fileDirectory.getSampleFormat() != null && !fileDirectory.getSampleFormat().isEmpty()) {
                num = fileDirectory.getSampleFormat().get(0);
            }
            if (samplesPerPixel != 1 || num2 == null || num2.intValue() != 32 || num == null || num.intValue() != 3) {
                throw new GeoPackageException("The coverage data tile is expected to be a single sample 32 bit float. Samples Per Pixel: " + samplesPerPixel + ", Bits Per Sample: " + num2 + ", Sample Format: " + num);
            }
            return;
        }
        throw new GeoPackageException("The image is null");
    }

    public Double getValue(GriddedTile griddedTile, byte[] bArr, int i, int i2) {
        return getValue(griddedTile, getPixelValue(bArr, i, i2));
    }

    public Double[] getValues(GriddedTile griddedTile, byte[] bArr) {
        return getValues(griddedTile, getPixelValues(bArr));
    }

    public CoverageDataTiffImage drawTile(float[] fArr, int i, int i2) {
        CoverageDataTiffImage createImage = createImage(i, i2);
        for (int i3 = 0; i3 < i2; i3++) {
            for (int i4 = 0; i4 < i; i4++) {
                setPixelValue(createImage, i4, i3, fArr[(i3 * i) + i4]);
            }
        }
        createImage.writeTiff();
        return createImage;
    }

    public byte[] drawTileData(float[] fArr, int i, int i2) {
        return drawTile(fArr, i, i2).getImageBytes();
    }

    public CoverageDataTiffImage drawTile(float[][] fArr) {
        int length = fArr[0].length;
        int length2 = fArr.length;
        CoverageDataTiffImage createImage = createImage(length, length2);
        for (int i = 0; i < length2; i++) {
            for (int i2 = 0; i2 < length; i2++) {
                setPixelValue(createImage, i2, i, fArr[i][i2]);
            }
        }
        createImage.writeTiff();
        return createImage;
    }

    public byte[] drawTileData(float[][] fArr) {
        return drawTile(fArr).getImageBytes();
    }

    public CoverageDataTiffImage drawTile(GriddedTile griddedTile, Double[] dArr, int i, int i2) {
        CoverageDataTiffImage createImage = createImage(i, i2);
        for (int i3 = 0; i3 < i; i3++) {
            for (int i4 = 0; i4 < i2; i4++) {
                setPixelValue(createImage, i3, i4, (float) getPixelValue(griddedTile, dArr[(i4 * i) + i3]));
            }
        }
        createImage.writeTiff();
        return createImage;
    }

    public byte[] drawTileData(GriddedTile griddedTile, Double[] dArr, int i, int i2) {
        return drawTile(griddedTile, dArr, i, i2).getImageBytes();
    }

    public CoverageDataTiffImage drawTile(GriddedTile griddedTile, Double[][] dArr) {
        int length = dArr[0].length;
        int length2 = dArr.length;
        CoverageDataTiffImage createImage = createImage(length, length2);
        for (int i = 0; i < length; i++) {
            for (int i2 = 0; i2 < length2; i2++) {
                setPixelValue(createImage, i, i2, (float) getPixelValue(griddedTile, dArr[i2][i]));
            }
        }
        createImage.writeTiff();
        return createImage;
    }

    public byte[] drawTileData(GriddedTile griddedTile, Double[][] dArr) {
        return drawTile(griddedTile, dArr).getImageBytes();
    }

    public CoverageDataTiffImage createImage(int i, int i2) {
        Rasters rasters = new Rasters(i, i2, 1, 32, 3);
        int calculateRowsPerStrip = rasters.calculateRowsPerStrip(1);
        FileDirectory fileDirectory = new FileDirectory();
        fileDirectory.setImageWidth(i);
        fileDirectory.setImageHeight(i2);
        fileDirectory.setBitsPerSample(32);
        fileDirectory.setCompression(1);
        fileDirectory.setPhotometricInterpretation(1);
        fileDirectory.setSamplesPerPixel(1);
        fileDirectory.setRowsPerStrip(calculateRowsPerStrip);
        fileDirectory.setPlanarConfiguration(1);
        fileDirectory.setSampleFormat(3);
        fileDirectory.setWriteRasters(rasters);
        return new CoverageDataTiffImage(fileDirectory);
    }

    public void setPixelValue(CoverageDataTiffImage coverageDataTiffImage, int i, int i2, float f) {
        coverageDataTiffImage.getRasters().setFirstPixelSample(i, i2, Float.valueOf(f));
    }

    public static CoverageDataTiff createTileTableWithMetadata(GeoPackage geoPackage, String str, BoundingBox boundingBox, long j, BoundingBox boundingBox2, long j2) {
        return (CoverageDataTiff) CoverageData.createTileTableWithMetadata(geoPackage, str, boundingBox, j, boundingBox2, j2, GriddedCoverageDataType.FLOAT);
    }
}
