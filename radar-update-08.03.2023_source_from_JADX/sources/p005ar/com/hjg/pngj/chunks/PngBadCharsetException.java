package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.PngjException;

/* renamed from: ar.com.hjg.pngj.chunks.PngBadCharsetException */
public class PngBadCharsetException extends PngjException {
    private static final long serialVersionUID = 1;

    public PngBadCharsetException(String str, Throwable th) {
        super(str, th);
    }

    public PngBadCharsetException(String str) {
        super(str);
    }

    public PngBadCharsetException(Throwable th) {
        super(th);
    }
}
