package mil.nga.tiff.util;

public class TiffException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public TiffException() {
    }

    public TiffException(String str) {
        super(str);
    }

    public TiffException(String str, Throwable th) {
        super(str, th);
    }

    public TiffException(Throwable th) {
        super(th);
    }
}
