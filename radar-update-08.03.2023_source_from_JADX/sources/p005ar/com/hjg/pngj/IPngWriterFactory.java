package p005ar.com.hjg.pngj;

import java.io.OutputStream;

/* renamed from: ar.com.hjg.pngj.IPngWriterFactory */
public interface IPngWriterFactory {
    PngWriter createPngWriter(OutputStream outputStream, ImageInfo imageInfo);
}
