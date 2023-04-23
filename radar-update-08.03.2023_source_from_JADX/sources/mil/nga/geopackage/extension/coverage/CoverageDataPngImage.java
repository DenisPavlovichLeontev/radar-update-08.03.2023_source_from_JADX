package mil.nga.geopackage.extension.coverage;

import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.tiles.user.TileRow;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.ImageLineInt;
import p005ar.com.hjg.pngj.PngReaderInt;
import p005ar.com.hjg.pngj.PngWriter;

public class CoverageDataPngImage implements CoverageDataImage {
    private final int height;
    private byte[] imageBytes;
    private ByteArrayOutputStream outputStream;
    private int[][] pixels;
    private PngReaderInt reader;
    private final int width;
    private PngWriter writer;

    public CoverageDataPngImage(TileRow tileRow) {
        this.imageBytes = tileRow.getTileData();
        PngReaderInt pngReaderInt = new PngReaderInt((InputStream) new ByteArrayInputStream(this.imageBytes));
        this.reader = pngReaderInt;
        CoverageDataPng.validateImageType(pngReaderInt);
        this.width = this.reader.imgInfo.cols;
        this.height = this.reader.imgInfo.rows;
    }

    public CoverageDataPngImage(ImageInfo imageInfo) {
        this.outputStream = new ByteArrayOutputStream();
        this.writer = new PngWriter((OutputStream) this.outputStream, imageInfo);
        this.width = imageInfo.cols;
        this.height = imageInfo.rows;
    }

    public byte[] getImageBytes() {
        byte[] bArr = this.imageBytes;
        if (bArr != null) {
            return bArr;
        }
        ByteArrayOutputStream byteArrayOutputStream = this.outputStream;
        if (byteArrayOutputStream != null) {
            return byteArrayOutputStream.toByteArray();
        }
        return null;
    }

    public PngReaderInt getReader() {
        return this.reader;
    }

    public PngWriter getWriter() {
        return this.writer;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void flushStream() {
        ByteArrayOutputStream byteArrayOutputStream = this.outputStream;
        if (byteArrayOutputStream != null) {
            if (this.imageBytes == null) {
                this.imageBytes = byteArrayOutputStream.toByteArray();
            }
            try {
                this.outputStream.close();
            } catch (IOException e) {
                Log.w("CoverageDataPngImage", "Failed to close output stream", e);
            }
        }
    }

    public int getPixel(int i, int i2) {
        if (this.pixels == null) {
            readPixels();
        }
        int[][] iArr = this.pixels;
        if (iArr != null) {
            return iArr[i2][i];
        }
        throw new GeoPackageException("Could not retrieve pixel value");
    }

    private void readPixels() {
        PngReaderInt pngReaderInt = this.reader;
        if (pngReaderInt != null) {
            int i = pngReaderInt.imgInfo.rows;
            int[] iArr = new int[2];
            iArr[1] = this.reader.imgInfo.cols;
            iArr[0] = i;
            this.pixels = (int[][]) Array.newInstance(Integer.TYPE, iArr);
            int i2 = 0;
            while (this.reader.hasMoreRows()) {
                ImageLineInt readRowInt = this.reader.readRowInt();
                int i3 = this.reader.imgInfo.cols;
                int[] iArr2 = new int[i3];
                System.arraycopy(readRowInt.getScanline(), 0, iArr2, 0, i3);
                this.pixels[i2] = iArr2;
                i2++;
            }
            this.reader.close();
        }
    }
}
