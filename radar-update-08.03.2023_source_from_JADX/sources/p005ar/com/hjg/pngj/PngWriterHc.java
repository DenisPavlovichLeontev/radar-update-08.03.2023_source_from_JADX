package p005ar.com.hjg.pngj;

import java.io.File;
import java.io.OutputStream;
import p005ar.com.hjg.pngj.pixels.PixelsWriter;
import p005ar.com.hjg.pngj.pixels.PixelsWriterMultiple;

/* renamed from: ar.com.hjg.pngj.PngWriterHc */
public class PngWriterHc extends PngWriter {
    public PngWriterHc(File file, ImageInfo imageInfo, boolean z) {
        super(file, imageInfo, z);
        setFilterType(FilterType.FILTER_SUPER_ADAPTIVE);
    }

    public PngWriterHc(File file, ImageInfo imageInfo) {
        super(file, imageInfo);
    }

    public PngWriterHc(OutputStream outputStream, ImageInfo imageInfo) {
        super(outputStream, imageInfo);
    }

    /* access modifiers changed from: protected */
    public PixelsWriter createPixelsWriter(ImageInfo imageInfo) {
        return new PixelsWriterMultiple(imageInfo);
    }

    public PixelsWriterMultiple getPixelWriterMultiple() {
        return (PixelsWriterMultiple) this.pixelsWriter;
    }
}
