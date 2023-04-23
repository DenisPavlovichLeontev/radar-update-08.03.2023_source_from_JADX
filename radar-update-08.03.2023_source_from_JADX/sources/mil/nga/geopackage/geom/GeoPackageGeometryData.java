package mil.nga.geopackage.geom;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.GeometryExtensions;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryEnvelope;
import mil.nga.wkb.p012io.ByteReader;
import mil.nga.wkb.p012io.ByteWriter;
import mil.nga.wkb.p012io.WkbGeometryReader;
import mil.nga.wkb.p012io.WkbGeometryWriter;

public class GeoPackageGeometryData {
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    private byte[] bytes;
    private boolean empty = true;
    private GeometryEnvelope envelope;
    private boolean extended = false;
    private Geometry geometry;
    private int srsId;
    private int wkbGeometryIndex;

    public GeoPackageGeometryData(long j) {
        this.srsId = (int) j;
    }

    public GeoPackageGeometryData(byte[] bArr) {
        fromBytes(bArr);
    }

    public void fromBytes(byte[] bArr) {
        this.bytes = bArr;
        ByteReader byteReader = new ByteReader(bArr);
        try {
            String readString = byteReader.readString(2);
            if (readString.equals(GeoPackageConstants.GEO_PACKAGE_GEOMETRY_MAGIC_NUMBER)) {
                byte readByte = byteReader.readByte();
                if (readByte == 0) {
                    int readFlags = readFlags(byteReader.readByte());
                    byteReader.setByteOrder(this.byteOrder);
                    this.srsId = byteReader.readInt();
                    this.envelope = readEnvelope(readFlags, byteReader);
                    this.wkbGeometryIndex = byteReader.getNextByte();
                    if (!this.empty) {
                        this.geometry = WkbGeometryReader.readGeometry(byteReader);
                        return;
                    }
                    return;
                }
                throw new GeoPackageException("Unexpected GeoPackage Geometry version: " + readByte + ", Expected: " + 0);
            }
            throw new GeoPackageException("Unexpected GeoPackage Geometry magic number: " + readString + ", Expected: " + GeoPackageConstants.GEO_PACKAGE_GEOMETRY_MAGIC_NUMBER);
        } catch (UnsupportedEncodingException unused) {
            throw new GeoPackageException("Unexpected GeoPackage Geometry magic number character encoding: Expected: GP");
        }
    }

    public byte[] toBytes() throws IOException {
        ByteWriter byteWriter = new ByteWriter();
        byteWriter.writeString(GeoPackageConstants.GEO_PACKAGE_GEOMETRY_MAGIC_NUMBER);
        byteWriter.writeByte((byte) 0);
        byteWriter.writeByte(buildFlagsByte());
        byteWriter.setByteOrder(this.byteOrder);
        byteWriter.writeInt(this.srsId);
        writeEnvelope(byteWriter);
        this.wkbGeometryIndex = byteWriter.size();
        if (!this.empty) {
            WkbGeometryWriter.writeGeometry(byteWriter, this.geometry);
        }
        this.bytes = byteWriter.getBytes();
        byteWriter.close();
        return this.bytes;
    }

    private int readFlags(byte b) {
        ByteOrder byteOrder2;
        int i = (b >> 7) & 1;
        int i2 = (b >> 6) & 1;
        if (i == 0 && i2 == 0) {
            boolean z = false;
            this.extended = ((b >> 5) & 1) == 1;
            if (((b >> 4) & 1) == 1) {
                z = true;
            }
            this.empty = z;
            int i3 = (b >> 1) & 7;
            if (i3 <= 4) {
                if ((b & 1) == 0) {
                    byteOrder2 = ByteOrder.BIG_ENDIAN;
                } else {
                    byteOrder2 = ByteOrder.LITTLE_ENDIAN;
                }
                this.byteOrder = byteOrder2;
                return i3;
            }
            throw new GeoPackageException("Unexpected GeoPackage Geometry flags. Envelope contents indicator must be between 0 and 4. Actual: " + i3);
        }
        throw new GeoPackageException("Unexpected GeoPackage Geometry flags. Flag bit 7 and 6 should both be 0, 7=" + i + ", 6=" + i2);
    }

    private byte buildFlagsByte() {
        byte b = 0;
        byte b2 = (byte) (((byte) (((this.extended ? 1 : 0) << true) + 0)) + ((this.empty ? 1 : 0) << true));
        GeometryEnvelope geometryEnvelope = this.envelope;
        byte indicator = (byte) (b2 + ((geometryEnvelope == null ? 0 : getIndicator(geometryEnvelope)) << 1));
        if (this.byteOrder != ByteOrder.BIG_ENDIAN) {
            b = 1;
        }
        return (byte) (indicator + b);
    }

    private GeometryEnvelope readEnvelope(int i, ByteReader byteReader) {
        boolean z;
        Double d;
        Double d2;
        Double d3;
        Double d4;
        int i2 = i;
        if (i2 <= 0) {
            return null;
        }
        double readDouble = byteReader.readDouble();
        double readDouble2 = byteReader.readDouble();
        double readDouble3 = byteReader.readDouble();
        double readDouble4 = byteReader.readDouble();
        boolean z2 = true;
        if (i2 == 2 || i2 == 4) {
            d2 = Double.valueOf(byteReader.readDouble());
            d = Double.valueOf(byteReader.readDouble());
            z = true;
        } else {
            z = false;
            d2 = null;
            d = null;
        }
        if (i2 == 3 || i2 == 4) {
            d3 = Double.valueOf(byteReader.readDouble());
            d4 = Double.valueOf(byteReader.readDouble());
        } else {
            z2 = false;
            d4 = null;
            d3 = null;
        }
        GeometryEnvelope geometryEnvelope = new GeometryEnvelope(z, z2);
        geometryEnvelope.setMinX(readDouble);
        geometryEnvelope.setMaxX(readDouble2);
        geometryEnvelope.setMinY(readDouble3);
        geometryEnvelope.setMaxY(readDouble4);
        if (z) {
            geometryEnvelope.setMinZ(d2);
            geometryEnvelope.setMaxZ(d);
        }
        if (z2) {
            geometryEnvelope.setMinM(d3);
            geometryEnvelope.setMaxM(d4);
        }
        return geometryEnvelope;
    }

    private void writeEnvelope(ByteWriter byteWriter) throws IOException {
        GeometryEnvelope geometryEnvelope = this.envelope;
        if (geometryEnvelope != null) {
            byteWriter.writeDouble(geometryEnvelope.getMinX());
            byteWriter.writeDouble(this.envelope.getMaxX());
            byteWriter.writeDouble(this.envelope.getMinY());
            byteWriter.writeDouble(this.envelope.getMaxY());
            if (this.envelope.hasZ()) {
                byteWriter.writeDouble(this.envelope.getMinZ().doubleValue());
                byteWriter.writeDouble(this.envelope.getMaxZ().doubleValue());
            }
            if (this.envelope.hasM()) {
                byteWriter.writeDouble(this.envelope.getMinM().doubleValue());
                byteWriter.writeDouble(this.envelope.getMaxM().doubleValue());
            }
        }
    }

    public boolean isExtended() {
        return this.extended;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public int getSrsId() {
        return this.srsId;
    }

    public GeometryEnvelope getEnvelope() {
        return this.envelope;
    }

    public Geometry getGeometry() {
        return this.geometry;
    }

    public void setExtended(boolean z) {
        this.extended = z;
    }

    public void setEmpty(boolean z) {
        this.empty = z;
    }

    public void setByteOrder(ByteOrder byteOrder2) {
        this.byteOrder = byteOrder2;
    }

    public void setSrsId(int i) {
        this.srsId = i;
    }

    public void setEnvelope(GeometryEnvelope geometryEnvelope) {
        this.envelope = geometryEnvelope;
    }

    public void setGeometry(Geometry geometry2) {
        this.geometry = geometry2;
        this.empty = geometry2 == null;
        if (geometry2 != null) {
            this.extended = GeometryExtensions.isNonStandard(geometry2.getGeometryType());
        }
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public byte[] getHeaderBytes() {
        int i = this.wkbGeometryIndex;
        byte[] bArr = new byte[i];
        System.arraycopy(this.bytes, 0, bArr, 0, i);
        return bArr;
    }

    public ByteBuffer getHeaderByteBuffer() {
        return ByteBuffer.wrap(this.bytes, 0, this.wkbGeometryIndex).order(this.byteOrder);
    }

    public byte[] getWkbBytes() {
        byte[] bArr = this.bytes;
        int length = bArr.length;
        int i = this.wkbGeometryIndex;
        int i2 = length - i;
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        return bArr2;
    }

    public ByteBuffer getWkbByteBuffer() {
        byte[] bArr = this.bytes;
        int i = this.wkbGeometryIndex;
        return ByteBuffer.wrap(bArr, i, bArr.length - i).order(this.byteOrder);
    }

    public int getWkbGeometryIndex() {
        return this.wkbGeometryIndex;
    }

    public static int getIndicator(GeometryEnvelope geometryEnvelope) {
        int i = geometryEnvelope.hasZ() ? 2 : 1;
        return geometryEnvelope.hasM() ? i + 2 : i;
    }
}
