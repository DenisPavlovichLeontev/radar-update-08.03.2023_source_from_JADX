package mil.nga.geopackage.extension.coverage;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kotlin.UShort;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.coverage.CoverageDataImage;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.projection.ProjectionFactory;
import mil.nga.geopackage.projection.ProjectionTransform;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import org.osgeo.proj4j.ProjCoordinate;

public abstract class CoverageDataCore<TImage extends CoverageDataImage> extends BaseExtension {
    public static final String EXTENSION_AUTHOR = "gpkg";
    public static final String EXTENSION_DEFINITION = GeoPackageProperties.getProperty(PropertyConstants.EXTENSIONS, EXTENSION_NAME_NO_AUTHOR);
    public static final String EXTENSION_NAME = Extensions.buildExtensionName("gpkg", EXTENSION_NAME_NO_AUTHOR);
    public static final String EXTENSION_NAME_NO_AUTHOR = "2d_gridded_coverage";
    protected CoverageDataAlgorithm algorithm;
    protected final BoundingBox coverageBoundingBox;
    protected final Projection coverageProjection;
    protected GriddedCoverageEncodingType encoding;
    private GriddedCoverage griddedCoverage;
    private final GriddedCoverageDao griddedCoverageDao;
    private final GriddedTileDao griddedTileDao;
    protected Integer height;
    protected final Projection requestProjection;
    protected final boolean sameProjection;
    private final TileMatrixSet tileMatrixSet;
    protected Integer width;
    protected boolean zoomIn;
    protected boolean zoomInBeforeOut;
    protected boolean zoomOut;

    private float getSource(float f, float f2, float f3, float f4) {
        return f3 + ((f - f2) * f4);
    }

    public short getPixelValue(int i) {
        return (short) i;
    }

    public int getUnsignedPixelValue(short s) {
        return s & UShort.MAX_VALUE;
    }

    public abstract Double getValue(GriddedTile griddedTile, TImage timage, int i, int i2);

    public abstract CoverageDataResults getValues(CoverageDataRequest coverageDataRequest, Integer num, Integer num2);

    public abstract CoverageDataResults getValuesUnbounded(CoverageDataRequest coverageDataRequest);

    protected CoverageDataCore(GeoPackageCore geoPackageCore, TileMatrixSet tileMatrixSet2, Integer num, Integer num2, Projection projection) {
        super(geoPackageCore);
        this.zoomIn = true;
        this.zoomOut = true;
        this.zoomInBeforeOut = true;
        this.algorithm = CoverageDataAlgorithm.NEAREST_NEIGHBOR;
        this.encoding = GriddedCoverageEncodingType.CENTER;
        this.tileMatrixSet = tileMatrixSet2;
        this.griddedCoverageDao = geoPackageCore.getGriddedCoverageDao();
        this.griddedTileDao = geoPackageCore.getGriddedTileDao();
        queryGriddedCoverage();
        this.width = num;
        this.height = num2;
        this.requestProjection = projection;
        Projection projection2 = ProjectionFactory.getProjection(tileMatrixSet2.getSrs());
        this.coverageProjection = projection2;
        this.coverageBoundingBox = tileMatrixSet2.getBoundingBox();
        if (projection != null) {
            this.sameProjection = projection.getUnit().name.equals(projection2.getUnit().name);
        } else {
            this.sameProjection = true;
        }
    }

    protected CoverageDataCore(GeoPackageCore geoPackageCore, TileMatrixSet tileMatrixSet2) {
        this(geoPackageCore, tileMatrixSet2, (Integer) null, (Integer) null, (Projection) null);
    }

    public TileMatrixSet getTileMatrixSet() {
        return this.tileMatrixSet;
    }

    public GriddedCoverageDao getGriddedCoverageDao() {
        return this.griddedCoverageDao;
    }

    public GriddedTileDao getGriddedTileDao() {
        return this.griddedTileDao;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer num) {
        this.width = num;
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer num) {
        this.height = num;
    }

    public Projection getRequestProjection() {
        return this.requestProjection;
    }

    public Projection getCoverageProjection() {
        return this.coverageProjection;
    }

    public BoundingBox getCoverageBoundingBox() {
        return this.coverageBoundingBox;
    }

    public boolean isSameProjection() {
        return this.sameProjection;
    }

    public boolean isZoomIn() {
        return this.zoomIn;
    }

    public void setZoomIn(boolean z) {
        this.zoomIn = z;
    }

    public boolean isZoomOut() {
        return this.zoomOut;
    }

    public void setZoomOut(boolean z) {
        this.zoomOut = z;
    }

    public boolean isZoomInBeforeOut() {
        return this.zoomInBeforeOut;
    }

    public void setZoomInBeforeOut(boolean z) {
        this.zoomInBeforeOut = z;
    }

    public CoverageDataAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    public void setAlgorithm(CoverageDataAlgorithm coverageDataAlgorithm) {
        if (coverageDataAlgorithm == null) {
            coverageDataAlgorithm = CoverageDataAlgorithm.NEAREST_NEIGHBOR;
        }
        this.algorithm = coverageDataAlgorithm;
    }

    public GriddedCoverageEncodingType getEncoding() {
        return this.encoding;
    }

    public void setEncoding(GriddedCoverageEncodingType griddedCoverageEncodingType) {
        if (griddedCoverageEncodingType == null) {
            griddedCoverageEncodingType = GriddedCoverageEncodingType.CENTER;
        }
        this.encoding = griddedCoverageEncodingType;
    }

    public List<Extensions> getOrCreate() {
        this.geoPackage.createGriddedCoverageTable();
        this.geoPackage.createGriddedTileTable();
        ArrayList arrayList = new ArrayList();
        String str = EXTENSION_NAME;
        String str2 = str;
        String str3 = EXTENSION_DEFINITION;
        Extensions orCreate = getOrCreate(str2, GriddedCoverage.TABLE_NAME, (String) null, str3, ExtensionScopeType.READ_WRITE);
        Extensions orCreate2 = getOrCreate(str2, GriddedTile.TABLE_NAME, (String) null, str3, ExtensionScopeType.READ_WRITE);
        Extensions orCreate3 = getOrCreate(str2, this.tileMatrixSet.getTableName(), "tile_data", str3, ExtensionScopeType.READ_WRITE);
        arrayList.add(orCreate);
        arrayList.add(orCreate2);
        arrayList.add(orCreate3);
        return arrayList;
    }

    public boolean has() {
        return has(EXTENSION_NAME, this.tileMatrixSet.getTableName(), "tile_data");
    }

    public GriddedCoverage getGriddedCoverage() {
        return this.griddedCoverage;
    }

    public GriddedCoverage queryGriddedCoverage() {
        try {
            if (this.griddedCoverageDao.isTableExists()) {
                this.griddedCoverage = this.griddedCoverageDao.query(this.tileMatrixSet);
            }
            return this.griddedCoverage;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to get Gridded Coverage for table name: " + this.tileMatrixSet.getTableName(), e);
        }
    }

    public List<GriddedTile> getGriddedTile() {
        try {
            if (this.griddedTileDao.isTableExists()) {
                return this.griddedTileDao.query(this.tileMatrixSet.getTableName());
            }
            return null;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to get Gridded Tile for table name: " + this.tileMatrixSet.getTableName(), e);
        }
    }

    public GriddedTile getGriddedTile(long j) {
        try {
            if (this.griddedTileDao.isTableExists()) {
                return this.griddedTileDao.query(this.tileMatrixSet.getTableName(), j);
            }
            return null;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to get Gridded Tile for table name: " + this.tileMatrixSet.getTableName() + ", tile id: " + j, e);
        }
    }

    public Double getDataNull() {
        GriddedCoverage griddedCoverage2 = this.griddedCoverage;
        if (griddedCoverage2 != null) {
            return griddedCoverage2.getDataNull();
        }
        return null;
    }

    public boolean isDataNull(double d) {
        Double dataNull = getDataNull();
        return dataNull != null && dataNull.doubleValue() == d;
    }

    public static List<String> getTables(GeoPackageCore geoPackageCore) {
        return geoPackageCore.getTables(ContentsDataType.GRIDDED_COVERAGE);
    }

    /* access modifiers changed from: protected */
    public Double[][] reprojectCoverageData(Double[][] dArr, int i, int i2, BoundingBox boundingBox, ProjectionTransform projectionTransform, BoundingBox boundingBox2) {
        Double[][] dArr2 = dArr;
        int i3 = i;
        int i4 = i2;
        double maxLongitude = (boundingBox.getMaxLongitude() - boundingBox.getMinLongitude()) / ((double) i3);
        double maxLatitude = (boundingBox.getMaxLatitude() - boundingBox.getMinLatitude()) / ((double) i4);
        double maxLongitude2 = boundingBox2.getMaxLongitude() - boundingBox2.getMinLongitude();
        double maxLatitude2 = boundingBox2.getMaxLatitude() - boundingBox2.getMinLatitude();
        int i5 = 0;
        int length = dArr2[0].length;
        int length2 = dArr2.length;
        int[] iArr = new int[2];
        iArr[1] = i3;
        iArr[0] = i4;
        Double[][] dArr3 = (Double[][]) Array.newInstance(Double.class, iArr);
        int i6 = 0;
        while (i6 < i4) {
            while (i5 < i3) {
                double d = maxLongitude;
                double d2 = maxLatitude;
                ProjCoordinate transform = projectionTransform.transform(new ProjCoordinate(boundingBox.getMinLongitude() + (((double) i5) * maxLongitude), boundingBox.getMaxLatitude() - (((double) i6) * maxLatitude)));
                double d3 = transform.f409x;
                dArr3[i6][i5] = dArr2[Math.min(length2 - 1, Math.max(0, (int) Math.round(((boundingBox2.getMaxLatitude() - transform.f410y) / maxLatitude2) * ((double) length2))))][Math.min(length - 1, Math.max(0, (int) Math.round(((d3 - boundingBox2.getMinLongitude()) / maxLongitude2) * ((double) length))))];
                i5++;
                i3 = i;
                int i7 = i2;
                maxLatitude = d2;
                maxLongitude = d;
            }
            double d4 = maxLatitude;
            i6++;
            i3 = i;
            i4 = i2;
            i5 = 0;
            maxLongitude = maxLongitude;
        }
        return dArr3;
    }

    /* access modifiers changed from: protected */
    public Double[][] formatUnboundedResults(TileMatrix tileMatrix, Map<Long, Map<Long, Double[][]>> map, int i, long j, long j2, long j3, long j4) {
        int i2;
        int i3;
        Map<Long, Map<Long, Double[][]>> map2 = map;
        Double[][] dArr = null;
        if (map.isEmpty()) {
            return null;
        }
        if (i == 1) {
            return (Double[][]) map2.get(Long.valueOf(j)).get(Long.valueOf(j3));
        }
        Double[][] dArr2 = (Double[][]) map2.get(Long.valueOf(j)).get(Long.valueOf(j3));
        Double[][] dArr3 = (Double[][]) map2.get(Long.valueOf(j2)).get(Long.valueOf(j4));
        int length = dArr2[0].length;
        int length2 = dArr2.length;
        if (j3 < j4) {
            i2 = dArr3[0].length + length;
            long j5 = (j4 - j3) - 1;
            if (j5 > 0) {
                i2 = (int) (((long) i2) + (j5 * tileMatrix.getTileWidth()));
            }
        } else {
            i2 = length;
        }
        if (j < j2) {
            i3 = dArr3.length + length2;
            long j6 = (j2 - j) - 1;
            if (j6 > 0) {
                i3 = (int) (((long) i3) + (j6 * tileMatrix.getTileHeight()));
            }
        } else {
            i3 = length2;
        }
        int[] iArr = new int[2];
        iArr[1] = i2;
        iArr[0] = i3;
        Double[][] dArr4 = (Double[][]) Array.newInstance(Double.class, iArr);
        for (Map.Entry next : map.entrySet()) {
            long longValue = ((Long) next.getKey()).longValue();
            int tileHeight = j < longValue ? ((int) (((longValue - j) - 1) * tileMatrix.getTileHeight())) + length2 : 0;
            for (Map.Entry entry : ((Map) next.getValue()).entrySet()) {
                long longValue2 = ((Long) entry.getKey()).longValue();
                int tileWidth = j3 < longValue2 ? ((int) (((longValue2 - j3) - 1) * tileMatrix.getTileWidth())) + length : 0;
                Double[][] dArr5 = (Double[][]) entry.getValue();
                for (int i4 = 0; i4 < dArr5.length; i4++) {
                    Double[] dArr6 = dArr5[i4];
                    System.arraycopy(dArr6, 0, dArr4[tileHeight + i4], tileWidth, dArr6.length);
                }
            }
        }
        return dArr4;
    }

    /* access modifiers changed from: protected */
    public float getXSource(int i, float f, float f2, float f3) {
        return getSource(getXEncodedLocation((float) i, this.encoding), f, f2, f3);
    }

    /* access modifiers changed from: protected */
    public float getYSource(int i, float f, float f2, float f3) {
        return getSource(getYEncodedLocation((float) i, this.encoding), f, f2, f3);
    }

    /* renamed from: mil.nga.geopackage.extension.coverage.CoverageDataCore$1 */
    static /* synthetic */ class C11641 {

        /* renamed from: $SwitchMap$mil$nga$geopackage$extension$coverage$GriddedCoverageEncodingType */
        static final /* synthetic */ int[] f347xc4324eda;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                mil.nga.geopackage.extension.coverage.GriddedCoverageEncodingType[] r0 = mil.nga.geopackage.extension.coverage.GriddedCoverageEncodingType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f347xc4324eda = r0
                mil.nga.geopackage.extension.coverage.GriddedCoverageEncodingType r1 = mil.nga.geopackage.extension.coverage.GriddedCoverageEncodingType.CENTER     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f347xc4324eda     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.geopackage.extension.coverage.GriddedCoverageEncodingType r1 = mil.nga.geopackage.extension.coverage.GriddedCoverageEncodingType.AREA     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f347xc4324eda     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.geopackage.extension.coverage.GriddedCoverageEncodingType r1 = mil.nga.geopackage.extension.coverage.GriddedCoverageEncodingType.CORNER     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.extension.coverage.CoverageDataCore.C11641.<clinit>():void");
        }
    }

    private float getXEncodedLocation(float f, GriddedCoverageEncodingType griddedCoverageEncodingType) {
        int i = C11641.f347xc4324eda[griddedCoverageEncodingType.ordinal()];
        if (i == 1 || i == 2) {
            return f + 0.5f;
        }
        if (i == 3) {
            return f;
        }
        throw new GeoPackageException("Unsupported Encoding Type: " + griddedCoverageEncodingType);
    }

    private float getYEncodedLocation(float f, GriddedCoverageEncodingType griddedCoverageEncodingType) {
        float f2;
        int i = C11641.f347xc4324eda[griddedCoverageEncodingType.ordinal()];
        if (i == 1 || i == 2) {
            f2 = 0.5f;
        } else if (i == 3) {
            f2 = 1.0f;
        } else {
            throw new GeoPackageException("Unsupported Encoding Type: " + griddedCoverageEncodingType);
        }
        return f + f2;
    }

    /* access modifiers changed from: protected */
    public List<int[]> getNearestNeighbors(float f, float f2) {
        float f3;
        int i;
        int i2;
        int i3;
        int i4;
        float f4;
        ArrayList arrayList = new ArrayList();
        CoverageDataSourcePixel xSourceMinAndMax = getXSourceMinAndMax(f);
        CoverageDataSourcePixel ySourceMinAndMax = getYSourceMinAndMax(f2);
        if (((double) xSourceMinAndMax.getOffset()) > 0.5d) {
            i2 = xSourceMinAndMax.getMax();
            i = xSourceMinAndMax.getMin();
            f3 = 1.0f - xSourceMinAndMax.getOffset();
        } else {
            i2 = xSourceMinAndMax.getMin();
            i = xSourceMinAndMax.getMax();
            f3 = xSourceMinAndMax.getOffset();
        }
        if (((double) ySourceMinAndMax.getOffset()) > 0.5d) {
            i4 = ySourceMinAndMax.getMax();
            i3 = ySourceMinAndMax.getMin();
            f4 = 1.0f - ySourceMinAndMax.getOffset();
        } else {
            i4 = ySourceMinAndMax.getMin();
            i3 = ySourceMinAndMax.getMax();
            f4 = ySourceMinAndMax.getOffset();
        }
        arrayList.add(new int[]{i2, i4});
        if (f3 <= f4) {
            arrayList.add(new int[]{i, i4});
            arrayList.add(new int[]{i2, i3});
        } else {
            arrayList.add(new int[]{i2, i3});
            arrayList.add(new int[]{i, i4});
        }
        arrayList.add(new int[]{i, i3});
        if (xSourceMinAndMax.getOffset() == 0.0f) {
            arrayList.add(new int[]{xSourceMinAndMax.getMin() - 1, ySourceMinAndMax.getMin()});
            arrayList.add(new int[]{xSourceMinAndMax.getMin() - 1, ySourceMinAndMax.getMax()});
        }
        if (ySourceMinAndMax.getOffset() == 0.0f) {
            arrayList.add(new int[]{xSourceMinAndMax.getMin(), ySourceMinAndMax.getMin() - 1});
            arrayList.add(new int[]{xSourceMinAndMax.getMax(), ySourceMinAndMax.getMin() - 1});
        }
        if (xSourceMinAndMax.getOffset() == 0.0f && ySourceMinAndMax.getOffset() == 0.0f) {
            arrayList.add(new int[]{xSourceMinAndMax.getMin() - 1, ySourceMinAndMax.getMin() - 1});
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public CoverageDataSourcePixel getXSourceMinAndMax(float f) {
        int floor = (int) Math.floor((double) f);
        return getSourceMinAndMax(f, floor, getXEncodedLocation((float) floor, this.griddedCoverage.getGridCellEncodingType()));
    }

    /* access modifiers changed from: protected */
    public CoverageDataSourcePixel getYSourceMinAndMax(float f) {
        int floor = (int) Math.floor((double) f);
        return getSourceMinAndMax(f, floor, getYEncodedLocation((float) floor, this.griddedCoverage.getGridCellEncodingType()));
    }

    private CoverageDataSourcePixel getSourceMinAndMax(float f, int i, float f2) {
        float f3;
        int i2;
        if (f < f2) {
            f3 = 1.0f - (f2 - f);
            i2 = i;
            i--;
        } else {
            i2 = i + 1;
            f3 = f - f2;
        }
        return new CoverageDataSourcePixel(f, i, i2, f3);
    }

    /* access modifiers changed from: protected */
    public Double getBilinearInterpolationValue(CoverageDataSourcePixel coverageDataSourcePixel, CoverageDataSourcePixel coverageDataSourcePixel2, Double[][] dArr) {
        return getBilinearInterpolationValue(coverageDataSourcePixel.getOffset(), coverageDataSourcePixel2.getOffset(), (float) coverageDataSourcePixel.getMin(), (float) coverageDataSourcePixel.getMax(), (float) coverageDataSourcePixel2.getMin(), (float) coverageDataSourcePixel2.getMax(), dArr);
    }

    /* access modifiers changed from: protected */
    public Double getBilinearInterpolationValue(float f, float f2, float f3, float f4, float f5, float f6, Double[][] dArr) {
        if (dArr == null) {
            return null;
        }
        Double[] dArr2 = dArr[0];
        Double d = dArr2[0];
        Double d2 = dArr2[1];
        Double[] dArr3 = dArr[1];
        return getBilinearInterpolationValue(f, f2, f3, f4, f5, f6, d, d2, dArr3[0], dArr3[1]);
    }

    /* access modifiers changed from: protected */
    public Double getBilinearInterpolationValue(float f, float f2, float f3, float f4, float f5, float f6, Double d, Double d2, Double d3, Double d4) {
        double d5;
        Double d6;
        if (d == null || ((d2 == null && f3 != f4) || ((d3 == null && f5 != f6) || (d4 == null && (f3 != f4 || f5 != f6))))) {
            return null;
        }
        float f7 = f4 - f3;
        if (f7 == 0.0f) {
            d5 = d.doubleValue();
            d6 = d3;
        } else {
            double d7 = (double) ((f7 - f) / f7);
            double d8 = (double) (f / f7);
            double doubleValue = (d.doubleValue() * d7) + (d2.doubleValue() * d8);
            d6 = Double.valueOf((d7 * d3.doubleValue()) + (d8 * d4.doubleValue()));
            d5 = doubleValue;
        }
        float f8 = f6 - f5;
        if (f8 != 0.0f) {
            d5 = (((double) ((f8 - f2) / f8)) * d5) + (((double) (f2 / f8)) * d6.doubleValue());
        }
        return Double.valueOf(d5);
    }

    /* access modifiers changed from: protected */
    public Double getBicubicInterpolationValue(Double[][] dArr, CoverageDataSourcePixel coverageDataSourcePixel, CoverageDataSourcePixel coverageDataSourcePixel2) {
        return getBicubicInterpolationValue(dArr, coverageDataSourcePixel.getOffset(), coverageDataSourcePixel2.getOffset());
    }

    /* access modifiers changed from: protected */
    public Double getBicubicInterpolationValue(Double[][] dArr, float f, float f2) {
        Double[] dArr2 = new Double[4];
        int i = 0;
        while (true) {
            if (i >= 4) {
                break;
            }
            Double[] dArr3 = dArr[i];
            Double cubicInterpolationValue = getCubicInterpolationValue(dArr3[0], dArr3[1], dArr3[2], dArr3[3], (double) f);
            if (cubicInterpolationValue == null) {
                dArr2 = null;
                break;
            }
            dArr2[i] = cubicInterpolationValue;
            i++;
        }
        if (dArr2 != null) {
            return getCubicInterpolationValue(dArr2, (double) f2);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Double getCubicInterpolationValue(Double[] dArr, double d) {
        if (dArr == null) {
            return null;
        }
        return getCubicInterpolationValue(dArr[0], dArr[1], dArr[2], dArr[3], d);
    }

    /* access modifiers changed from: protected */
    public Double getCubicInterpolationValue(Double d, Double d2, Double d3, Double d4, double d5) {
        if (d == null || d2 == null || d3 == null || d4 == null) {
            return null;
        }
        double doubleValue = d3.doubleValue() - d.doubleValue();
        return Double.valueOf(((((((((((-d.doubleValue()) + (d2.doubleValue() * 3.0d)) - (d3.doubleValue() * 3.0d)) + d4.doubleValue()) * d5) * d5) * d5) + ((((((d.doubleValue() * 2.0d) - (d2.doubleValue() * 5.0d)) + (d3.doubleValue() * 4.0d)) - d4.doubleValue()) * d5) * d5)) + (doubleValue * d5)) + (d2.doubleValue() * 2.0d)) / 2.0d);
    }

    /* access modifiers changed from: protected */
    public BoundingBox padBoundingBox(TileMatrix tileMatrix, BoundingBox boundingBox, int i) {
        double d = (double) i;
        double pixelXSize = tileMatrix.getPixelXSize() * d;
        double pixelYSize = tileMatrix.getPixelYSize() * d;
        return new BoundingBox(boundingBox.getMinLongitude() - pixelXSize, boundingBox.getMinLatitude() - pixelYSize, boundingBox.getMaxLongitude() + pixelXSize, boundingBox.getMaxLatitude() + pixelYSize);
    }

    public short getPixelValue(short[] sArr, int i, int i2, int i3) {
        return sArr[(i3 * i) + i2];
    }

    public int getUnsignedPixelValue(short[] sArr, int i, int i2, int i3) {
        return getUnsignedPixelValue(getPixelValue(sArr, i, i2, i3));
    }

    public int getUnsignedPixelValue(int[] iArr, int i, int i2, int i3) {
        return iArr[(i3 * i) + i2];
    }

    public int[] getUnsignedPixelValues(short[] sArr) {
        int[] iArr = new int[sArr.length];
        for (int i = 0; i < sArr.length; i++) {
            iArr[i] = getUnsignedPixelValue(sArr[i]);
        }
        return iArr;
    }

    public Double getValue(GriddedTile griddedTile, short s) {
        return getValue(griddedTile, getUnsignedPixelValue(s));
    }

    public Double getValue(GriddedTile griddedTile, int i) {
        double d = (double) i;
        if (!isDataNull(d)) {
            return pixelValueToValue(griddedTile, new Double(d));
        }
        return null;
    }

    private Double pixelValueToValue(GriddedTile griddedTile, Double d) {
        GriddedCoverage griddedCoverage2 = this.griddedCoverage;
        if (griddedCoverage2 == null || griddedCoverage2.getDataType() != GriddedCoverageDataType.INTEGER) {
            return d;
        }
        if (griddedTile != null) {
            d = Double.valueOf(Double.valueOf(d.doubleValue() * griddedTile.getScale()).doubleValue() + griddedTile.getOffset());
        }
        return Double.valueOf(Double.valueOf(d.doubleValue() * this.griddedCoverage.getScale()).doubleValue() + this.griddedCoverage.getOffset());
    }

    public Double[] getValues(GriddedTile griddedTile, short[] sArr) {
        Double[] dArr = new Double[sArr.length];
        for (int i = 0; i < sArr.length; i++) {
            dArr[i] = getValue(griddedTile, sArr[i]);
        }
        return dArr;
    }

    public Double[] getValues(GriddedTile griddedTile, int[] iArr) {
        Double[] dArr = new Double[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            dArr[i] = getValue(griddedTile, iArr[i]);
        }
        return dArr;
    }

    public static TileMatrixSet createTileTableWithMetadata(GeoPackageCore geoPackageCore, String str, BoundingBox boundingBox, long j, BoundingBox boundingBox2, long j2) {
        return geoPackageCore.createTileTableWithMetadata(ContentsDataType.GRIDDED_COVERAGE, str, boundingBox, j, boundingBox2, j2);
    }

    public int getUnsignedPixelValue(GriddedTile griddedTile, Double d) {
        if (d != null) {
            return (int) Math.round(valueToPixelValue(griddedTile, d.doubleValue()));
        }
        GriddedCoverage griddedCoverage2 = this.griddedCoverage;
        if (griddedCoverage2 != null) {
            return griddedCoverage2.getDataNull().intValue();
        }
        return 0;
    }

    private double valueToPixelValue(GriddedTile griddedTile, double d) {
        GriddedCoverage griddedCoverage2 = this.griddedCoverage;
        if (griddedCoverage2 == null || griddedCoverage2.getDataType() != GriddedCoverageDataType.INTEGER) {
            return d;
        }
        double offset = (d - this.griddedCoverage.getOffset()) / this.griddedCoverage.getScale();
        return griddedTile != null ? (offset - griddedTile.getOffset()) / griddedTile.getScale() : offset;
    }

    public short getPixelValue(GriddedTile griddedTile, Double d) {
        return getPixelValue(getUnsignedPixelValue(griddedTile, d));
    }

    public float getPixelValue(float[] fArr, int i, int i2, int i3) {
        return fArr[(i3 * i) + i2];
    }

    public Double getValue(GriddedTile griddedTile, float f) {
        double d = (double) f;
        if (!isDataNull(d)) {
            return pixelValueToValue(griddedTile, new Double(d));
        }
        return null;
    }

    public Double[] getValues(GriddedTile griddedTile, float[] fArr) {
        Double[] dArr = new Double[fArr.length];
        for (int i = 0; i < fArr.length; i++) {
            dArr[i] = getValue(griddedTile, fArr[i]);
        }
        return dArr;
    }

    public float getFloatPixelValue(GriddedTile griddedTile, Double d) {
        double d2;
        if (d == null) {
            GriddedCoverage griddedCoverage2 = this.griddedCoverage;
            d2 = griddedCoverage2 != null ? griddedCoverage2.getDataNull().doubleValue() : 0.0d;
        } else {
            d2 = valueToPixelValue(griddedTile, d.doubleValue());
        }
        return (float) d2;
    }

    public Double getValue(double d, double d2) {
        CoverageDataResults values = getValues(new CoverageDataRequest(d, d2), (Integer) 1, (Integer) 1);
        if (values != null) {
            return values.getValues()[0][0];
        }
        return null;
    }

    public CoverageDataResults getValues(BoundingBox boundingBox) {
        return getValues(new CoverageDataRequest(boundingBox));
    }

    public CoverageDataResults getValues(BoundingBox boundingBox, Integer num, Integer num2) {
        return getValues(new CoverageDataRequest(boundingBox), num, num2);
    }

    public CoverageDataResults getValues(CoverageDataRequest coverageDataRequest) {
        return getValues(coverageDataRequest, this.width, this.height);
    }

    public CoverageDataResults getValuesUnbounded(BoundingBox boundingBox) {
        return getValuesUnbounded(new CoverageDataRequest(boundingBox));
    }

    /* access modifiers changed from: protected */
    public Double getBilinearInterpolationValue(GriddedTile griddedTile, TImage timage, Double[][] dArr, Double[][] dArr2, Double[][] dArr3, int i, int i2, float f, float f2, float f3, float f4, float f5, float f6) {
        float xSource = getXSource(i2, f4, f6, f);
        float ySource = getYSource(i, f3, f5, f2);
        CoverageDataSourcePixel xSourceMinAndMax = getXSourceMinAndMax(xSource);
        CoverageDataSourcePixel ySourceMinAndMax = getYSourceMinAndMax(ySource);
        Double[][] dArr4 = (Double[][]) Array.newInstance(Double.class, new int[]{2, 2});
        populateValues(griddedTile, timage, dArr, dArr2, dArr3, xSourceMinAndMax, ySourceMinAndMax, dArr4);
        if (dArr4 != null) {
            return getBilinearInterpolationValue(xSourceMinAndMax, ySourceMinAndMax, dArr4);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Double getBicubicInterpolationValue(GriddedTile griddedTile, TImage timage, Double[][] dArr, Double[][] dArr2, Double[][] dArr3, int i, int i2, float f, float f2, float f3, float f4, float f5, float f6) {
        float xSource = getXSource(i2, f4, f6, f);
        float ySource = getYSource(i, f3, f5, f2);
        CoverageDataSourcePixel xSourceMinAndMax = getXSourceMinAndMax(xSource);
        xSourceMinAndMax.setMin(xSourceMinAndMax.getMin() - 1);
        xSourceMinAndMax.setMax(xSourceMinAndMax.getMax() + 1);
        CoverageDataSourcePixel ySourceMinAndMax = getYSourceMinAndMax(ySource);
        ySourceMinAndMax.setMin(ySourceMinAndMax.getMin() - 1);
        ySourceMinAndMax.setMax(ySourceMinAndMax.getMax() + 1);
        Double[][] dArr4 = (Double[][]) Array.newInstance(Double.class, new int[]{4, 4});
        populateValues(griddedTile, timage, dArr, dArr2, dArr3, xSourceMinAndMax, ySourceMinAndMax, dArr4);
        if (dArr4 != null) {
            return getBicubicInterpolationValue(dArr4, xSourceMinAndMax, ySourceMinAndMax);
        }
        return null;
    }

    private void populateValues(GriddedTile griddedTile, TImage timage, Double[][] dArr, Double[][] dArr2, Double[][] dArr3, CoverageDataSourcePixel coverageDataSourcePixel, CoverageDataSourcePixel coverageDataSourcePixel2, Double[][] dArr4) {
        populateValues(griddedTile, timage, dArr, dArr2, dArr3, coverageDataSourcePixel.getMin(), coverageDataSourcePixel.getMax(), coverageDataSourcePixel2.getMin(), coverageDataSourcePixel2.getMax(), dArr4);
    }

    private void populateValues(GriddedTile griddedTile, TImage timage, Double[][] dArr, Double[][] dArr2, Double[][] dArr3, int i, int i2, int i3, int i4, Double[][] dArr4) {
        int i5 = i;
        int i6 = i3;
        int i7 = i4;
        Double[][] dArr5 = dArr4;
        while (dArr5 != null && i7 >= i6) {
            int i8 = i2;
            while (true) {
                if (i8 < i5) {
                    break;
                }
                Double valueOverBorders = getValueOverBorders(griddedTile, timage, dArr, dArr2, dArr3, i8, i7);
                if (valueOverBorders == null) {
                    Double[][] dArr6 = null;
                    dArr5 = null;
                    break;
                }
                dArr5[i7 - i6][i8 - i5] = valueOverBorders;
                i8--;
            }
            i7--;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0024, code lost:
        r2 = r1.next();
     */
    /* JADX WARNING: Removed duplicated region for block: B:1:0x001e A[LOOP:0: B:1:0x001e->B:4:0x0040, LOOP_START, PHI: r2 
      PHI: (r2v4 java.lang.Double) = (r2v3 java.lang.Double), (r2v9 java.lang.Double) binds: [B:0:0x0000, B:4:0x0040] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Double getNearestNeighborValue(mil.nga.geopackage.extension.coverage.GriddedTile r7, TImage r8, java.lang.Double[][] r9, java.lang.Double[][] r10, java.lang.Double[][] r11, int r12, int r13, float r14, float r15, float r16, float r17, float r18, float r19) {
        /*
            r6 = this;
            r0 = r6
            r1 = r13
            r2 = r14
            r3 = r17
            r4 = r19
            float r1 = r6.getXSource(r13, r3, r4, r14)
            r2 = r12
            r3 = r15
            r4 = r16
            r5 = r18
            float r2 = r6.getYSource(r12, r4, r5, r15)
            java.util.List r1 = r6.getNearestNeighbors(r1, r2)
            java.util.Iterator r1 = r1.iterator()
            r2 = 0
        L_0x001e:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L_0x0042
            java.lang.Object r2 = r1.next()
            int[] r2 = (int[]) r2
            r3 = 0
            r3 = r2[r3]
            r4 = 1
            r2 = r2[r4]
            r12 = r6
            r13 = r7
            r14 = r8
            r15 = r9
            r16 = r10
            r17 = r11
            r18 = r3
            r19 = r2
            java.lang.Double r2 = r12.getValueOverBorders(r13, r14, r15, r16, r17, r18, r19)
            if (r2 == 0) goto L_0x001e
        L_0x0042:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.extension.coverage.CoverageDataCore.getNearestNeighborValue(mil.nga.geopackage.extension.coverage.GriddedTile, mil.nga.geopackage.extension.coverage.CoverageDataImage, java.lang.Double[][], java.lang.Double[][], java.lang.Double[][], int, int, float, float, float, float, float, float):java.lang.Double");
    }

    private Double getValueOverBorders(GriddedTile griddedTile, TImage timage, Double[][] dArr, Double[][] dArr2, Double[][] dArr3, int i, int i2) {
        int i3;
        int i4;
        Double d;
        int i5;
        if (i >= timage.getWidth() || i2 >= timage.getHeight()) {
            return null;
        }
        if (i >= 0 && i2 >= 0) {
            return getValue(griddedTile, timage, i, i2);
        }
        if (i >= 0 || i2 >= 0) {
            if (i < 0) {
                if (dArr == null || (i * -1) - 1 >= dArr.length) {
                    return null;
                }
                Double[] dArr4 = dArr[i4];
                if (i2 >= dArr4.length) {
                    return null;
                }
                d = dArr4[i2];
            } else if (dArr3 == null || (i2 * -1) - 1 >= dArr3.length) {
                return null;
            } else {
                Double[] dArr5 = dArr3[i3];
                if (i < dArr5.length) {
                    return dArr5[i];
                }
                return null;
            }
        } else if (dArr2 == null || (i2 * -1) - 1 >= dArr2.length) {
            return null;
        } else {
            Double[] dArr6 = dArr2[i5];
            int length = i + dArr6.length;
            if (length < 0) {
                return null;
            }
            d = dArr6[length];
        }
        return d;
    }
}
