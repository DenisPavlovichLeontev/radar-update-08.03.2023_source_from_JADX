package p005ar.com.hjg.pngj;

/* renamed from: ar.com.hjg.pngj.PngjBadCrcException */
public class PngjBadCrcException extends PngjInputException {
    private static final long serialVersionUID = 1;

    public PngjBadCrcException(String str, Throwable th) {
        super(str, th);
    }

    public PngjBadCrcException(String str) {
        super(str);
    }

    public PngjBadCrcException(Throwable th) {
        super(th);
    }
}
