package org.jsoup.parser;

import androidx.core.app.FrameMetricsAggregator;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import javax.annotation.Nullable;
import org.jsoup.UncheckedIOException;
import org.jsoup.helper.Validate;

public final class CharacterReader {
    static final char EOF = '￿';
    static final int maxBufferLen = 32768;
    private static final int maxStringCacheLen = 12;
    private static final int minReadAheadLen = 1024;
    static final int readAheadLimit = 24576;
    private static final int stringCacheSize = 512;
    private int bufLength;
    private int bufMark;
    private int bufPos;
    private int bufSplitPoint;
    private char[] charBuf;
    private int lastIcIndex;
    @Nullable
    private String lastIcSeq;
    private int lineNumberOffset;
    @Nullable
    private ArrayList<Integer> newlinePositions;
    private boolean readFully;
    private Reader reader;
    private int readerPos;
    private String[] stringCache;

    public CharacterReader(Reader reader2, int i) {
        this.bufMark = -1;
        this.stringCache = new String[512];
        this.newlinePositions = null;
        this.lineNumberOffset = 1;
        Validate.notNull(reader2);
        Validate.isTrue(reader2.markSupported());
        this.reader = reader2;
        this.charBuf = new char[Math.min(i, 32768)];
        bufferUp();
    }

    public CharacterReader(Reader reader2) {
        this(reader2, 32768);
    }

    public CharacterReader(String str) {
        this(new StringReader(str), str.length());
    }

    public void close() {
        Reader reader2 = this.reader;
        if (reader2 != null) {
            try {
                reader2.close();
            } catch (IOException unused) {
            } catch (Throwable th) {
                this.reader = null;
                this.charBuf = null;
                this.stringCache = null;
                throw th;
            }
            this.reader = null;
            this.charBuf = null;
            this.stringCache = null;
        }
    }

    private void bufferUp() {
        int i;
        int i2;
        boolean z;
        if (!this.readFully && (i = this.bufPos) >= this.bufSplitPoint) {
            int i3 = this.bufMark;
            if (i3 != -1) {
                int i4 = i3;
                i2 = i - i3;
                i = i4;
            } else {
                i2 = 0;
            }
            try {
                long j = (long) i;
                long skip = this.reader.skip(j);
                this.reader.mark(32768);
                int i5 = 0;
                while (true) {
                    z = true;
                    if (i5 > 1024) {
                        break;
                    }
                    Reader reader2 = this.reader;
                    char[] cArr = this.charBuf;
                    int read = reader2.read(cArr, i5, cArr.length - i5);
                    if (read == -1) {
                        this.readFully = true;
                    }
                    if (read <= 0) {
                        break;
                    }
                    i5 += read;
                }
                this.reader.reset();
                if (i5 > 0) {
                    if (skip != j) {
                        z = false;
                    }
                    Validate.isTrue(z);
                    this.bufLength = i5;
                    this.readerPos += i;
                    this.bufPos = i2;
                    if (this.bufMark != -1) {
                        this.bufMark = 0;
                    }
                    this.bufSplitPoint = Math.min(i5, readAheadLimit);
                }
                scanBufferForNewlines();
                this.lastIcSeq = null;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    public int pos() {
        return this.readerPos + this.bufPos;
    }

    public void trackNewlines(boolean z) {
        if (z && this.newlinePositions == null) {
            this.newlinePositions = new ArrayList<>(409);
            scanBufferForNewlines();
        } else if (!z) {
            this.newlinePositions = null;
        }
    }

    public boolean isTrackNewlines() {
        return this.newlinePositions != null;
    }

    public int lineNumber() {
        return lineNumber(pos());
    }

    /* access modifiers changed from: package-private */
    public int lineNumber(int i) {
        if (!isTrackNewlines()) {
            return 1;
        }
        int lineNumIndex = lineNumIndex(i);
        if (lineNumIndex == -1) {
            return this.lineNumberOffset;
        }
        return lineNumIndex + this.lineNumberOffset + 1;
    }

    public int columnNumber() {
        return columnNumber(pos());
    }

    /* access modifiers changed from: package-private */
    public int columnNumber(int i) {
        if (!isTrackNewlines()) {
            return i + 1;
        }
        int lineNumIndex = lineNumIndex(i);
        if (lineNumIndex == -1) {
            return i + 1;
        }
        return (i - this.newlinePositions.get(lineNumIndex).intValue()) + 1;
    }

    /* access modifiers changed from: package-private */
    public String cursorPos() {
        return lineNumber() + ":" + columnNumber();
    }

    private int lineNumIndex(int i) {
        if (!isTrackNewlines()) {
            return 0;
        }
        int binarySearch = Collections.binarySearch(this.newlinePositions, Integer.valueOf(i));
        return binarySearch < -1 ? Math.abs(binarySearch) - 2 : binarySearch;
    }

    private void scanBufferForNewlines() {
        if (isTrackNewlines()) {
            if (this.newlinePositions.size() > 0) {
                int lineNumIndex = lineNumIndex(this.readerPos);
                if (lineNumIndex == -1) {
                    lineNumIndex = 0;
                }
                int intValue = this.newlinePositions.get(lineNumIndex).intValue();
                this.lineNumberOffset += lineNumIndex;
                this.newlinePositions.clear();
                this.newlinePositions.add(Integer.valueOf(intValue));
            }
            for (int i = this.bufPos; i < this.bufLength; i++) {
                if (this.charBuf[i] == 10) {
                    this.newlinePositions.add(Integer.valueOf(this.readerPos + 1 + i));
                }
            }
        }
    }

    public boolean isEmpty() {
        bufferUp();
        return this.bufPos >= this.bufLength;
    }

    private boolean isEmptyNoBufferUp() {
        return this.bufPos >= this.bufLength;
    }

    public char current() {
        bufferUp();
        if (isEmptyNoBufferUp()) {
            return 65535;
        }
        return this.charBuf[this.bufPos];
    }

    /* access modifiers changed from: package-private */
    public char consume() {
        bufferUp();
        char c = isEmptyNoBufferUp() ? 65535 : this.charBuf[this.bufPos];
        this.bufPos++;
        return c;
    }

    /* access modifiers changed from: package-private */
    public void unconsume() {
        int i = this.bufPos;
        if (i >= 1) {
            this.bufPos = i - 1;
            return;
        }
        throw new UncheckedIOException(new IOException("WTF: No buffer left to unconsume."));
    }

    public void advance() {
        this.bufPos++;
    }

    /* access modifiers changed from: package-private */
    public void mark() {
        if (this.bufLength - this.bufPos < 1024) {
            this.bufSplitPoint = 0;
        }
        bufferUp();
        this.bufMark = this.bufPos;
    }

    /* access modifiers changed from: package-private */
    public void unmark() {
        this.bufMark = -1;
    }

    /* access modifiers changed from: package-private */
    public void rewindToMark() {
        int i = this.bufMark;
        if (i != -1) {
            this.bufPos = i;
            unmark();
            return;
        }
        throw new UncheckedIOException(new IOException("Mark invalid"));
    }

    /* access modifiers changed from: package-private */
    public int nextIndexOf(char c) {
        bufferUp();
        for (int i = this.bufPos; i < this.bufLength; i++) {
            if (c == this.charBuf[i]) {
                return i - this.bufPos;
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public int nextIndexOf(CharSequence charSequence) {
        bufferUp();
        char charAt = charSequence.charAt(0);
        int i = this.bufPos;
        while (i < this.bufLength) {
            int i2 = 1;
            if (charAt != this.charBuf[i]) {
                do {
                    i++;
                    if (i >= this.bufLength) {
                        break;
                    }
                } while (charAt == this.charBuf[i]);
            }
            int i3 = i + 1;
            int length = (charSequence.length() + i3) - 1;
            int i4 = this.bufLength;
            if (i < i4 && length <= i4) {
                int i5 = i3;
                while (i5 < length && charSequence.charAt(i2) == this.charBuf[i5]) {
                    i5++;
                    i2++;
                }
                if (i5 == length) {
                    return i - this.bufPos;
                }
            }
            i = i3;
        }
        return -1;
    }

    public String consumeTo(char c) {
        int nextIndexOf = nextIndexOf(c);
        if (nextIndexOf == -1) {
            return consumeToEnd();
        }
        String cacheString = cacheString(this.charBuf, this.stringCache, this.bufPos, nextIndexOf);
        this.bufPos += nextIndexOf;
        return cacheString;
    }

    /* access modifiers changed from: package-private */
    public String consumeTo(String str) {
        int nextIndexOf = nextIndexOf((CharSequence) str);
        if (nextIndexOf != -1) {
            String cacheString = cacheString(this.charBuf, this.stringCache, this.bufPos, nextIndexOf);
            this.bufPos += nextIndexOf;
            return cacheString;
        } else if (this.bufLength - this.bufPos < str.length()) {
            return consumeToEnd();
        } else {
            int length = (this.bufLength - str.length()) + 1;
            char[] cArr = this.charBuf;
            String[] strArr = this.stringCache;
            int i = this.bufPos;
            String cacheString2 = cacheString(cArr, strArr, i, length - i);
            this.bufPos = length;
            return cacheString2;
        }
    }

    public String consumeToAny(char... cArr) {
        bufferUp();
        int i = this.bufPos;
        int i2 = this.bufLength;
        char[] cArr2 = this.charBuf;
        int i3 = i;
        loop0:
        while (i3 < i2) {
            for (char c : cArr) {
                if (cArr2[i3] == c) {
                    break loop0;
                }
            }
            i3++;
        }
        this.bufPos = i3;
        return i3 > i ? cacheString(this.charBuf, this.stringCache, i, i3 - i) : "";
    }

    /* access modifiers changed from: package-private */
    public String consumeToAnySorted(char... cArr) {
        bufferUp();
        int i = this.bufPos;
        int i2 = this.bufLength;
        char[] cArr2 = this.charBuf;
        int i3 = i;
        while (i3 < i2 && Arrays.binarySearch(cArr, cArr2[i3]) < 0) {
            i3++;
        }
        this.bufPos = i3;
        return i3 > i ? cacheString(this.charBuf, this.stringCache, i, i3 - i) : "";
    }

    /* access modifiers changed from: package-private */
    public String consumeData() {
        int i = this.bufPos;
        int i2 = this.bufLength;
        char[] cArr = this.charBuf;
        int i3 = i;
        while (i3 < i2) {
            char c = cArr[i3];
            if (c == 0 || c == '&' || c == '<') {
                break;
            }
            i3++;
        }
        this.bufPos = i3;
        return i3 > i ? cacheString(this.charBuf, this.stringCache, i, i3 - i) : "";
    }

    /* access modifiers changed from: package-private */
    public String consumeAttributeQuoted(boolean z) {
        int i = this.bufPos;
        int i2 = this.bufLength;
        char[] cArr = this.charBuf;
        int i3 = i;
        while (i3 < i2) {
            char c = cArr[i3];
            if (c == 0) {
                break;
            }
            if (c != '\"') {
                if (c == '&') {
                    break;
                } else if (c == '\'') {
                    if (z) {
                        break;
                    }
                } else {
                    continue;
                    i3++;
                }
            }
            if (!z) {
                break;
            }
            i3++;
        }
        this.bufPos = i3;
        return i3 > i ? cacheString(this.charBuf, this.stringCache, i, i3 - i) : "";
    }

    /* access modifiers changed from: package-private */
    public String consumeRawData() {
        int i = this.bufPos;
        int i2 = this.bufLength;
        char[] cArr = this.charBuf;
        int i3 = i;
        while (i3 < i2) {
            char c = cArr[i3];
            if (c == 0 || c == '<') {
                break;
            }
            i3++;
        }
        this.bufPos = i3;
        return i3 > i ? cacheString(this.charBuf, this.stringCache, i, i3 - i) : "";
    }

    /* access modifiers changed from: package-private */
    public String consumeTagName() {
        bufferUp();
        int i = this.bufPos;
        int i2 = this.bufLength;
        char[] cArr = this.charBuf;
        int i3 = i;
        while (i3 < i2) {
            char c = cArr[i3];
            if (c == 9 || c == 10 || c == 12 || c == 13 || c == ' ' || c == '/' || c == '<' || c == '>') {
                break;
            }
            i3++;
        }
        this.bufPos = i3;
        return i3 > i ? cacheString(this.charBuf, this.stringCache, i, i3 - i) : "";
    }

    /* access modifiers changed from: package-private */
    public String consumeToEnd() {
        bufferUp();
        char[] cArr = this.charBuf;
        String[] strArr = this.stringCache;
        int i = this.bufPos;
        String cacheString = cacheString(cArr, strArr, i, this.bufLength - i);
        this.bufPos = this.bufLength;
        return cacheString;
    }

    /* access modifiers changed from: package-private */
    public String consumeLetterSequence() {
        char c;
        bufferUp();
        int i = this.bufPos;
        while (true) {
            int i2 = this.bufPos;
            if (i2 < this.bufLength && (((c = this.charBuf[i2]) >= 'A' && c <= 'Z') || ((c >= 'a' && c <= 'z') || Character.isLetter(c)))) {
                this.bufPos++;
            }
        }
        return cacheString(this.charBuf, this.stringCache, i, this.bufPos - i);
    }

    /* access modifiers changed from: package-private */
    public String consumeLetterThenDigitSequence() {
        char c;
        bufferUp();
        int i = this.bufPos;
        while (true) {
            int i2 = this.bufPos;
            if (i2 < this.bufLength && (((c = this.charBuf[i2]) >= 'A' && c <= 'Z') || ((c >= 'a' && c <= 'z') || Character.isLetter(c)))) {
                this.bufPos++;
            }
        }
        while (!isEmptyNoBufferUp() && (r1 = this.charBuf[r2]) >= '0' && r1 <= '9') {
            this.bufPos = (r2 = this.bufPos) + 1;
        }
        return cacheString(this.charBuf, this.stringCache, i, this.bufPos - i);
    }

    /* access modifiers changed from: package-private */
    public String consumeHexSequence() {
        int i;
        char c;
        bufferUp();
        int i2 = this.bufPos;
        while (true) {
            i = this.bufPos;
            if (i < this.bufLength && (((c = this.charBuf[i]) >= '0' && c <= '9') || ((c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f')))) {
                this.bufPos = i + 1;
            }
        }
        return cacheString(this.charBuf, this.stringCache, i2, i - i2);
    }

    /* access modifiers changed from: package-private */
    public String consumeDigitSequence() {
        int i;
        char c;
        bufferUp();
        int i2 = this.bufPos;
        while (true) {
            i = this.bufPos;
            if (i < this.bufLength && (c = this.charBuf[i]) >= '0' && c <= '9') {
                this.bufPos = i + 1;
            }
        }
        return cacheString(this.charBuf, this.stringCache, i2, i - i2);
    }

    /* access modifiers changed from: package-private */
    public boolean matches(char c) {
        return !isEmpty() && this.charBuf[this.bufPos] == c;
    }

    /* access modifiers changed from: package-private */
    public boolean matches(String str) {
        bufferUp();
        int length = str.length();
        if (length > this.bufLength - this.bufPos) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (str.charAt(i) != this.charBuf[this.bufPos + i]) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesIgnoreCase(String str) {
        bufferUp();
        int length = str.length();
        if (length > this.bufLength - this.bufPos) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (Character.toUpperCase(str.charAt(i)) != Character.toUpperCase(this.charBuf[this.bufPos + i])) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesAny(char... cArr) {
        if (isEmpty()) {
            return false;
        }
        bufferUp();
        char c = this.charBuf[this.bufPos];
        for (char c2 : cArr) {
            if (c2 == c) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesAnySorted(char[] cArr) {
        bufferUp();
        return !isEmpty() && Arrays.binarySearch(cArr, this.charBuf[this.bufPos]) >= 0;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesLetter() {
        if (isEmpty()) {
            return false;
        }
        char c = this.charBuf[this.bufPos];
        if ((c < 'A' || c > 'Z') && ((c < 'a' || c > 'z') && !Character.isLetter(c))) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesAsciiAlpha() {
        if (isEmpty()) {
            return false;
        }
        char c = this.charBuf[this.bufPos];
        if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesDigit() {
        char c;
        if (!isEmpty() && (c = this.charBuf[this.bufPos]) >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean matchConsume(String str) {
        bufferUp();
        if (!matches(str)) {
            return false;
        }
        this.bufPos += str.length();
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean matchConsumeIgnoreCase(String str) {
        if (!matchesIgnoreCase(str)) {
            return false;
        }
        this.bufPos += str.length();
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean containsIgnoreCase(String str) {
        boolean z = false;
        int i = -1;
        if (str.equals(this.lastIcSeq)) {
            int i2 = this.lastIcIndex;
            if (i2 == -1) {
                return false;
            }
            if (i2 >= this.bufPos) {
                return true;
            }
        }
        this.lastIcSeq = str;
        int nextIndexOf = nextIndexOf((CharSequence) str.toLowerCase(Locale.ENGLISH));
        if (nextIndexOf > -1) {
            this.lastIcIndex = this.bufPos + nextIndexOf;
            return true;
        }
        int nextIndexOf2 = nextIndexOf((CharSequence) str.toUpperCase(Locale.ENGLISH));
        if (nextIndexOf2 > -1) {
            z = true;
        }
        if (z) {
            i = this.bufPos + nextIndexOf2;
        }
        this.lastIcIndex = i;
        return z;
    }

    public String toString() {
        int i = this.bufLength;
        int i2 = this.bufPos;
        if (i - i2 < 0) {
            return "";
        }
        return new String(this.charBuf, i2, i - i2);
    }

    private static String cacheString(char[] cArr, String[] strArr, int i, int i2) {
        if (i2 > 12) {
            return new String(cArr, i, i2);
        }
        if (i2 < 1) {
            return "";
        }
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            i3 = (i3 * 31) + cArr[i + i4];
        }
        int i5 = i3 & FrameMetricsAggregator.EVERY_DURATION;
        String str = strArr[i5];
        if (str != null && rangeEquals(cArr, i, i2, str)) {
            return str;
        }
        String str2 = new String(cArr, i, i2);
        strArr[i5] = str2;
        return str2;
    }

    static boolean rangeEquals(char[] cArr, int i, int i2, String str) {
        if (i2 != str.length()) {
            return false;
        }
        int i3 = 0;
        while (true) {
            int i4 = i2 - 1;
            if (i2 == 0) {
                return true;
            }
            int i5 = i + 1;
            int i6 = i3 + 1;
            if (cArr[i] != str.charAt(i3)) {
                return false;
            }
            i = i5;
            i2 = i4;
            i3 = i6;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean rangeEquals(int i, int i2, String str) {
        return rangeEquals(this.charBuf, i, i2, str);
    }
}
