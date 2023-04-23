package org.osmdroid.tileprovider.modules;

public class CantContinueException extends Exception {
    private static final long serialVersionUID = 146526524087765133L;

    public CantContinueException(String str) {
        super(str);
    }

    public CantContinueException(Throwable th) {
        super(th);
    }
}
