package org.mapsforge.map.reader.header;

import java.io.IOException;
import org.mapsforge.map.reader.ReadBuffer;

public class MapFileHeader {
    private static final int BASE_ZOOM_LEVEL_MAX = 20;
    private static final int HEADER_SIZE_MIN = 70;
    private static final byte SIGNATURE_LENGTH_INDEX = 16;
    private static final char SPACE = ' ';
    private MapFileInfo mapFileInfo;
    private SubFileParameter[] subFileParameters;
    private byte zoomLevelMaximum;
    private byte zoomLevelMinimum;

    public MapFileInfo getMapFileInfo() {
        return this.mapFileInfo;
    }

    public byte getQueryZoomLevel(byte b) {
        byte b2 = this.zoomLevelMaximum;
        if (b > b2) {
            return b2;
        }
        byte b3 = this.zoomLevelMinimum;
        return b < b3 ? b3 : b;
    }

    public SubFileParameter getSubFileParameter(int i) {
        return this.subFileParameters[i];
    }

    public void readHeader(ReadBuffer readBuffer, long j) throws IOException {
        RequiredFields.readMagicByte(readBuffer);
        RequiredFields.readRemainingHeader(readBuffer);
        MapFileInfoBuilder mapFileInfoBuilder = new MapFileInfoBuilder();
        RequiredFields.readFileVersion(readBuffer, mapFileInfoBuilder);
        RequiredFields.readFileSize(readBuffer, j, mapFileInfoBuilder);
        RequiredFields.readMapDate(readBuffer, mapFileInfoBuilder);
        RequiredFields.readBoundingBox(readBuffer, mapFileInfoBuilder);
        RequiredFields.readTilePixelSize(readBuffer, mapFileInfoBuilder);
        RequiredFields.readProjectionName(readBuffer, mapFileInfoBuilder);
        OptionalFields.readOptionalFields(readBuffer, mapFileInfoBuilder);
        RequiredFields.readPoiTags(readBuffer, mapFileInfoBuilder);
        RequiredFields.readWayTags(readBuffer, mapFileInfoBuilder);
        readSubFileParameters(readBuffer, j, mapFileInfoBuilder);
        this.mapFileInfo = mapFileInfoBuilder.build();
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [int, byte] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readSubFileParameters(org.mapsforge.map.reader.ReadBuffer r11, long r12, org.mapsforge.map.reader.header.MapFileInfoBuilder r14) {
        /*
            r10 = this;
            byte r0 = r11.readByte()
            r1 = 1
            if (r0 < r1) goto L_0x0142
            r14.numberOfSubFiles = r0
            org.mapsforge.map.reader.header.SubFileParameter[] r2 = new org.mapsforge.map.reader.header.SubFileParameter[r0]
            r3 = 127(0x7f, float:1.78E-43)
            r10.zoomLevelMinimum = r3
            r3 = -128(0xffffffffffffff80, float:NaN)
            r10.zoomLevelMaximum = r3
            r3 = 0
            r4 = r3
        L_0x0015:
            if (r4 >= r0) goto L_0x0125
            org.mapsforge.map.reader.header.SubFileParameterBuilder r5 = new org.mapsforge.map.reader.header.SubFileParameterBuilder
            r5.<init>()
            byte r6 = r11.readByte()
            if (r6 < 0) goto L_0x010e
            r7 = 20
            if (r6 > r7) goto L_0x010e
            r5.baseZoomLevel = r6
            byte r6 = r11.readByte()
            if (r6 < 0) goto L_0x00f7
            r7 = 22
            if (r6 > r7) goto L_0x00f7
            r5.zoomLevelMin = r6
            byte r8 = r11.readByte()
            if (r8 < 0) goto L_0x00e0
            if (r8 > r7) goto L_0x00e0
            r5.zoomLevelMax = r8
            if (r6 > r8) goto L_0x00c1
            long r6 = r11.readLong()
            r8 = 70
            int r8 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r8 < 0) goto L_0x00aa
            int r8 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
            if (r8 >= 0) goto L_0x00aa
            r5.startAddress = r6
            org.mapsforge.map.reader.header.OptionalFields r8 = r14.optionalFields
            boolean r8 = r8.isDebugFile
            if (r8 == 0) goto L_0x0059
            r8 = 16
            long r6 = r6 + r8
        L_0x0059:
            r5.indexStartAddress = r6
            long r6 = r11.readLong()
            r8 = 1
            int r8 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r8 < 0) goto L_0x0093
            r5.subFileSize = r6
            org.mapsforge.core.model.BoundingBox r6 = r14.boundingBox
            r5.boundingBox = r6
            org.mapsforge.map.reader.header.SubFileParameter r5 = r5.build()
            r2[r4] = r5
            byte r6 = r10.zoomLevelMinimum
            byte r5 = r5.zoomLevelMin
            if (r6 <= r5) goto L_0x007f
            r5 = r2[r4]
            byte r5 = r5.zoomLevelMin
            r10.zoomLevelMinimum = r5
            r14.zoomLevelMin = r5
        L_0x007f:
            byte r5 = r10.zoomLevelMaximum
            r6 = r2[r4]
            byte r6 = r6.zoomLevelMax
            if (r5 >= r6) goto L_0x008f
            r5 = r2[r4]
            byte r5 = r5.zoomLevelMax
            r10.zoomLevelMaximum = r5
            r14.zoomLevelMax = r5
        L_0x008f:
            int r4 = r4 + 1
            byte r4 = (byte) r4
            goto L_0x0015
        L_0x0093:
            org.mapsforge.map.reader.header.MapFileException r11 = new org.mapsforge.map.reader.header.MapFileException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "invalid sub-file size: "
            r12.append(r13)
            r12.append(r6)
            java.lang.String r12 = r12.toString()
            r11.<init>(r12)
            throw r11
        L_0x00aa:
            org.mapsforge.map.reader.header.MapFileException r11 = new org.mapsforge.map.reader.header.MapFileException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "invalid start address: "
            r12.append(r13)
            r12.append(r6)
            java.lang.String r12 = r12.toString()
            r11.<init>(r12)
            throw r11
        L_0x00c1:
            org.mapsforge.map.reader.header.MapFileException r11 = new org.mapsforge.map.reader.header.MapFileException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "invalid zoom level range: "
            r12.append(r13)
            r12.append(r6)
            r13 = 32
            r12.append(r13)
            r12.append(r8)
            java.lang.String r12 = r12.toString()
            r11.<init>(r12)
            throw r11
        L_0x00e0:
            org.mapsforge.map.reader.header.MapFileException r11 = new org.mapsforge.map.reader.header.MapFileException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "invalid maximum zoom level: "
            r12.append(r13)
            r12.append(r8)
            java.lang.String r12 = r12.toString()
            r11.<init>(r12)
            throw r11
        L_0x00f7:
            org.mapsforge.map.reader.header.MapFileException r11 = new org.mapsforge.map.reader.header.MapFileException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "invalid minimum zoom level: "
            r12.append(r13)
            r12.append(r6)
            java.lang.String r12 = r12.toString()
            r11.<init>(r12)
            throw r11
        L_0x010e:
            org.mapsforge.map.reader.header.MapFileException r11 = new org.mapsforge.map.reader.header.MapFileException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "invalid base zoom level: "
            r12.append(r13)
            r12.append(r6)
            java.lang.String r12 = r12.toString()
            r11.<init>(r12)
            throw r11
        L_0x0125:
            byte r11 = r10.zoomLevelMaximum
            int r11 = r11 + r1
            org.mapsforge.map.reader.header.SubFileParameter[] r11 = new org.mapsforge.map.reader.header.SubFileParameter[r11]
            r10.subFileParameters = r11
        L_0x012c:
            if (r3 >= r0) goto L_0x0141
            r11 = r2[r3]
            byte r12 = r11.zoomLevelMin
        L_0x0132:
            byte r13 = r11.zoomLevelMax
            if (r12 > r13) goto L_0x013e
            org.mapsforge.map.reader.header.SubFileParameter[] r13 = r10.subFileParameters
            r13[r12] = r11
            int r12 = r12 + 1
            byte r12 = (byte) r12
            goto L_0x0132
        L_0x013e:
            int r3 = r3 + 1
            goto L_0x012c
        L_0x0141:
            return
        L_0x0142:
            org.mapsforge.map.reader.header.MapFileException r11 = new org.mapsforge.map.reader.header.MapFileException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "invalid number of sub-files: "
            r12.append(r13)
            r12.append(r0)
            java.lang.String r12 = r12.toString()
            r11.<init>(r12)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.reader.header.MapFileHeader.readSubFileParameters(org.mapsforge.map.reader.ReadBuffer, long, org.mapsforge.map.reader.header.MapFileInfoBuilder):void");
    }
}
