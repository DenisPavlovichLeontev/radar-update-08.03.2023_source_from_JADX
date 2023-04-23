package org.mapsforge.map.reader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import kotlin.jvm.internal.ByteCompanionObject;
import okio.Utf8;
import org.mapsforge.core.model.Tag;
import org.mapsforge.core.util.Parameters;

public class ReadBuffer {
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final Logger LOGGER = Logger.getLogger(ReadBuffer.class.getName());
    private byte[] bufferData;
    private int bufferPosition;
    private ByteBuffer bufferWrapper;
    private final FileChannel inputChannel;
    private final List<Integer> tagIds = new ArrayList();

    ReadBuffer(FileChannel fileChannel) {
        this.inputChannel = fileChannel;
    }

    public byte readByte() {
        byte[] bArr = this.bufferData;
        int i = this.bufferPosition;
        this.bufferPosition = i + 1;
        return bArr[i];
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public boolean readFromFile(int i) throws IOException {
        byte[] bArr = this.bufferData;
        if (bArr == null || bArr.length < i) {
            if (i > Parameters.MAXIMUM_BUFFER_SIZE) {
                Logger logger = LOGGER;
                logger.warning("invalid read length: " + i);
                return false;
            }
            byte[] bArr2 = new byte[i];
            this.bufferData = bArr2;
            this.bufferWrapper = ByteBuffer.wrap(bArr2, 0, i);
        }
        this.bufferPosition = 0;
        this.bufferWrapper.clear();
        if (this.inputChannel.read(this.bufferWrapper) == i) {
            return true;
        }
        return false;
    }

    public boolean readFromFile(long j, int i) throws IOException {
        byte[] bArr = this.bufferData;
        boolean z = false;
        if (bArr == null || bArr.length < i) {
            if (i > Parameters.MAXIMUM_BUFFER_SIZE) {
                Logger logger = LOGGER;
                logger.warning("invalid read length: " + i);
                return false;
            }
            byte[] bArr2 = new byte[i];
            this.bufferData = bArr2;
            this.bufferWrapper = ByteBuffer.wrap(bArr2, 0, i);
        }
        this.bufferPosition = 0;
        this.bufferWrapper.clear();
        synchronized (this.inputChannel) {
            this.inputChannel.position(j);
            if (this.inputChannel.read(this.bufferWrapper) == i) {
                z = true;
            }
        }
        return z;
    }

    public int readInt() {
        int i = this.bufferPosition + 4;
        this.bufferPosition = i;
        return Deserializer.getInt(this.bufferData, i - 4);
    }

    public long readLong() {
        int i = this.bufferPosition + 8;
        this.bufferPosition = i;
        return Deserializer.getLong(this.bufferData, i - 8);
    }

    public int readShort() {
        int i = this.bufferPosition + 2;
        this.bufferPosition = i;
        return Deserializer.getShort(this.bufferData, i - 2);
    }

    public int readSignedInt() {
        int i;
        byte b;
        int i2 = 0;
        byte b2 = 0;
        while (true) {
            byte[] bArr = this.bufferData;
            i = this.bufferPosition;
            b = bArr[i];
            if ((b & ByteCompanionObject.MIN_VALUE) == 0) {
                break;
            }
            this.bufferPosition = i + 1;
            i2 |= (b & ByteCompanionObject.MAX_VALUE) << b2;
            b2 = (byte) (b2 + 7);
        }
        if ((b & 64) != 0) {
            this.bufferPosition = i + 1;
            return -(i2 | ((b & Utf8.REPLACEMENT_BYTE) << b2));
        }
        this.bufferPosition = i + 1;
        return i2 | ((b & Utf8.REPLACEMENT_BYTE) << b2);
    }

    /* access modifiers changed from: package-private */
    public List<Tag> readTags(Tag[] tagArr, byte b) {
        ArrayList arrayList = new ArrayList();
        this.tagIds.clear();
        int length = tagArr.length;
        while (b != 0) {
            int readUnsignedInt = readUnsignedInt();
            if (readUnsignedInt < 0 || readUnsignedInt >= length) {
                LOGGER.warning("invalid tag ID: " + readUnsignedInt);
                return null;
            }
            this.tagIds.add(Integer.valueOf(readUnsignedInt));
            b = (byte) (b - 1);
        }
        for (Integer intValue : this.tagIds) {
            Tag tag = tagArr[intValue.intValue()];
            if (tag.value.length() == 2 && tag.value.charAt(0) == '%') {
                String str = tag.value;
                if (str.charAt(1) == 'b') {
                    str = String.valueOf(readByte());
                } else if (str.charAt(1) == 'i') {
                    if (tag.key.contains(":colour")) {
                        str = "#" + Integer.toHexString(readInt());
                    } else {
                        str = String.valueOf(readInt());
                    }
                } else if (str.charAt(1) == 'f') {
                    str = String.valueOf(readFloat());
                } else if (str.charAt(1) == 'h') {
                    str = String.valueOf(readShort());
                } else if (str.charAt(1) == 's') {
                    str = readUTF8EncodedString();
                }
                tag = new Tag(tag.key, str);
            }
            arrayList.add(tag);
        }
        return arrayList;
    }

    public int readUnsignedInt() {
        int i = 0;
        byte b = 0;
        while (true) {
            byte[] bArr = this.bufferData;
            int i2 = this.bufferPosition;
            byte b2 = bArr[i2];
            if ((b2 & ByteCompanionObject.MIN_VALUE) != 0) {
                this.bufferPosition = i2 + 1;
                i |= (b2 & ByteCompanionObject.MAX_VALUE) << b;
                b = (byte) (b + 7);
            } else {
                this.bufferPosition = i2 + 1;
                return i | (b2 << b);
            }
        }
    }

    public String readUTF8EncodedString() {
        return readUTF8EncodedString(readUnsignedInt());
    }

    public String readUTF8EncodedString(int i) {
        if (i > 0) {
            int i2 = this.bufferPosition;
            int i3 = i2 + i;
            byte[] bArr = this.bufferData;
            if (i3 <= bArr.length) {
                int i4 = i2 + i;
                this.bufferPosition = i4;
                try {
                    return new String(bArr, i4 - i, i, CHARSET_UTF8);
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
        Logger logger = LOGGER;
        logger.warning("invalid string length: " + i);
        return null;
    }

    /* access modifiers changed from: package-private */
    public int getBufferPosition() {
        return this.bufferPosition;
    }

    /* access modifiers changed from: package-private */
    public int getBufferSize() {
        return this.bufferData.length;
    }

    /* access modifiers changed from: package-private */
    public void setBufferPosition(int i) {
        this.bufferPosition = i;
    }

    /* access modifiers changed from: package-private */
    public void skipBytes(int i) {
        this.bufferPosition += i;
    }
}
