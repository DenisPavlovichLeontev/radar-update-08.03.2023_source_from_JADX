package org.jsoup.helper;

import javax.annotation.Nullable;

public final class Validate {
    private Validate() {
    }

    public static void notNull(@Nullable Object obj) {
        if (obj == null) {
            throw new ValidationException("Object must not be null");
        }
    }

    public static void notNullParam(@Nullable Object obj, String str) {
        if (obj == null) {
            throw new ValidationException(String.format("The parameter '%s' must not be null.", new Object[]{str}));
        }
    }

    public static void notNull(@Nullable Object obj, String str) {
        if (obj == null) {
            throw new ValidationException(str);
        }
    }

    public static Object ensureNotNull(@Nullable Object obj) {
        if (obj != null) {
            return obj;
        }
        throw new ValidationException("Object must not be null");
    }

    public static Object ensureNotNull(@Nullable Object obj, String str, Object... objArr) {
        if (obj != null) {
            return obj;
        }
        throw new ValidationException(String.format(str, objArr));
    }

    public static void isTrue(boolean z) {
        if (!z) {
            throw new ValidationException("Must be true");
        }
    }

    public static void isTrue(boolean z, String str) {
        if (!z) {
            throw new ValidationException(str);
        }
    }

    public static void isFalse(boolean z) {
        if (z) {
            throw new ValidationException("Must be false");
        }
    }

    public static void isFalse(boolean z, String str) {
        if (z) {
            throw new ValidationException(str);
        }
    }

    public static void noNullElements(Object[] objArr) {
        noNullElements(objArr, "Array must not contain any null objects");
    }

    public static void noNullElements(Object[] objArr, String str) {
        int length = objArr.length;
        int i = 0;
        while (i < length) {
            if (objArr[i] != null) {
                i++;
            } else {
                throw new ValidationException(str);
            }
        }
    }

    public static void notEmpty(@Nullable String str) {
        if (str == null || str.length() == 0) {
            throw new ValidationException("String must not be empty");
        }
    }

    public static void notEmptyParam(@Nullable String str, String str2) {
        if (str == null || str.length() == 0) {
            throw new ValidationException(String.format("The '%s' parameter must not be empty.", new Object[]{str2}));
        }
    }

    public static void notEmpty(@Nullable String str, String str2) {
        if (str == null || str.length() == 0) {
            throw new ValidationException(str2);
        }
    }

    public static void wtf(String str) {
        throw new IllegalStateException(str);
    }

    public static void fail(String str) {
        throw new ValidationException(str);
    }
}
