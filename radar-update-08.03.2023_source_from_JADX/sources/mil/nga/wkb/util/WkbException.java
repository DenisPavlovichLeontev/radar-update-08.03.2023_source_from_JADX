package mil.nga.wkb.util;

public class WkbException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public WkbException() {
    }

    public WkbException(String str) {
        super(str);
    }

    public WkbException(String str, Throwable th) {
        super(str, th);
    }

    public WkbException(Throwable th) {
        super(th);
    }
}
