package mil.nga.geopackage.extension.coverage;

import java.io.IOException;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.tiles.user.TileRow;
import mil.nga.tiff.FileDirectory;
import mil.nga.tiff.Rasters;
import mil.nga.tiff.TIFFImage;
import mil.nga.tiff.TiffReader;
import mil.nga.tiff.TiffWriter;

public class CoverageDataTiffImage implements CoverageDataImage {
    private FileDirectory directory;
    private final int height;
    private byte[] imageBytes;
    private Rasters rasters;
    private final int width;

    public CoverageDataTiffImage(TileRow tileRow) {
        byte[] tileData = tileRow.getTileData();
        this.imageBytes = tileData;
        FileDirectory fileDirectory = TiffReader.readTiff(tileData).getFileDirectory();
        this.directory = fileDirectory;
        CoverageDataTiff.validateImageType(fileDirectory);
        this.width = this.directory.getImageWidth().intValue();
        this.height = this.directory.getImageHeight().intValue();
    }

    public CoverageDataTiffImage(FileDirectory fileDirectory) {
        this.directory = fileDirectory;
        this.rasters = fileDirectory.getWriteRasters();
        this.width = fileDirectory.getImageWidth().intValue();
        this.height = fileDirectory.getImageHeight().intValue();
    }

    public byte[] getImageBytes() {
        if (this.imageBytes == null) {
            writeTiff();
        }
        return this.imageBytes;
    }

    public FileDirectory getDirectory() {
        return this.directory;
    }

    public Rasters getRasters() {
        if (this.rasters == null) {
            readPixels();
        }
        return this.rasters;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void writeTiff() {
        if (this.directory.getWriteRasters() != null) {
            TIFFImage tIFFImage = new TIFFImage();
            tIFFImage.add(this.directory);
            try {
                this.imageBytes = TiffWriter.writeTiffToBytes(tIFFImage);
            } catch (IOException e) {
                throw new GeoPackageException("Failed to write TIFF image", e);
            }
        }
    }

    public float getPixel(int i, int i2) {
        if (this.rasters == null) {
            readPixels();
        }
        Rasters rasters2 = this.rasters;
        if (rasters2 != null) {
            return rasters2.getFirstPixelSample(i, i2).floatValue();
        }
        throw new GeoPackageException("Could not retrieve pixel value");
    }

    private void readPixels() {
        FileDirectory fileDirectory = this.directory;
        if (fileDirectory != null) {
            this.rasters = fileDirectory.readRasters();
        }
    }
}
