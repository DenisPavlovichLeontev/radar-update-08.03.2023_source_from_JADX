package p005ar.com.hjg.pngj;

/* renamed from: ar.com.hjg.pngj.PngjException */
public class PngjException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public PngjException(String str, Throwable th) {
        super(str, th);
    }

    public PngjException(String str) {
        super(str);
    }

    public PngjException(Throwable th) {
        super(th);
    }
}
