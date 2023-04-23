package org.mapsforge.core.graphics;

public class CorruptedInputStreamException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public CorruptedInputStreamException(String str, Throwable th) {
        super(str, th);
    }
}
