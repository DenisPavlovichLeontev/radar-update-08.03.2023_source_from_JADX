package p005ar.com.hjg.pngj;

import java.io.File;
import java.io.InputStream;

/* renamed from: ar.com.hjg.pngj.PngReaderInt */
public class PngReaderInt extends PngReader {
    public PngReaderInt(File file) {
        super(file);
    }

    public PngReaderInt(InputStream inputStream) {
        super(inputStream);
    }

    public ImageLineInt readRowInt() {
        IImageLine readRow = readRow();
        if (readRow instanceof ImageLineInt) {
            return (ImageLineInt) readRow;
        }
        throw new PngjException("This is not a ImageLineInt : " + readRow.getClass());
    }
}
