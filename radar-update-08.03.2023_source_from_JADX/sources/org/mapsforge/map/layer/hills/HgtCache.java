package org.mapsforge.map.layer.hills;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.HillshadingBitmap;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.map.layer.hills.ShadingAlgorithm;

class HgtCache {
    final ShadingAlgorithm algorithm;
    final File demFolder;
    /* access modifiers changed from: private */
    public final GraphicFactory graphicsFactory;
    /* access modifiers changed from: private */
    public LazyFuture<Map<TileKey, HgtFileInfo>> hgtFiles;
    final boolean interpolatorOverlap;
    final int mainCacheSize;
    /* access modifiers changed from: private */
    public final Lru mainLru;
    final int neighborCacheSize;
    /* access modifiers changed from: private */
    public List<String> problems = new ArrayList();
    /* access modifiers changed from: private */
    public final Lru secondaryLru;

    protected static final class TileKey {
        final int east;
        final int north;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            TileKey tileKey = (TileKey) obj;
            if (this.north == tileKey.north && this.east == tileKey.east) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.north * 31) + this.east;
        }

        TileKey(int i, int i2) {
            this.east = i2;
            this.north = i;
        }
    }

    class Lru {
        private final LinkedHashSet<Future<HillshadingBitmap>> lru;
        private int size;

        public int getSize() {
            return this.size;
        }

        public void setSize(int i) {
            this.size = Math.max(0, i);
            if (i < this.lru.size()) {
                synchronized (this.lru) {
                    Iterator it = this.lru.iterator();
                    while (this.lru.size() > i) {
                        Future future = (Future) it.next();
                        it.remove();
                    }
                }
            }
        }

        Lru(int i) {
            this.size = i;
            this.lru = i > 0 ? new LinkedHashSet<>() : null;
        }

        /* access modifiers changed from: package-private */
        public Future<HillshadingBitmap> markUsed(Future<HillshadingBitmap> future) {
            if (this.size <= 0 || future == null) {
                return future;
            }
            synchronized (this.lru) {
                this.lru.remove(future);
                this.lru.add(future);
                if (this.lru.size() <= this.size) {
                    return null;
                }
                Iterator it = this.lru.iterator();
                Future<HillshadingBitmap> future2 = (Future) it.next();
                it.remove();
                return future2;
            }
        }

        /* access modifiers changed from: package-private */
        public void evict(Future<HillshadingBitmap> future) {
            if (this.size > 0) {
                synchronized (this.lru) {
                    this.lru.add(future);
                }
            }
        }
    }

    HgtCache(File file, boolean z, GraphicFactory graphicFactory, ShadingAlgorithm shadingAlgorithm, int i, int i2) {
        this.demFolder = file;
        this.interpolatorOverlap = z;
        this.graphicsFactory = graphicFactory;
        this.algorithm = shadingAlgorithm;
        this.mainCacheSize = i;
        this.neighborCacheSize = i2;
        this.mainLru = new Lru(i);
        this.secondaryLru = z ? new Lru(i2) : null;
        this.hgtFiles = new LazyFuture<Map<TileKey, HgtFileInfo>>() {
            /* access modifiers changed from: protected */
            public Map<TileKey, HgtFileInfo> calculate() throws ExecutionException, InterruptedException {
                HashMap hashMap = new HashMap();
                crawl(HgtCache.this.demFolder, Pattern.compile("([ns])(\\d{1,2})([ew])(\\d{1,3})\\.hgt", 2).matcher(""), hashMap, HgtCache.this.problems);
                return hashMap;
            }

            /* access modifiers changed from: package-private */
            public void crawl(File file, Matcher matcher, Map<TileKey, HgtFileInfo> map, List<String> list) {
                File[] listFiles;
                Matcher matcher2 = matcher;
                Map<TileKey, HgtFileInfo> map2 = map;
                List<String> list2 = list;
                if (!file.exists()) {
                    return;
                }
                if (!file.isFile()) {
                    File file2 = file;
                    if (file.isDirectory() && (listFiles = file.listFiles()) != null) {
                        for (File crawl : listFiles) {
                            crawl(crawl, matcher2, map2, list2);
                        }
                    }
                } else if (matcher2.reset(file.getName()).matches()) {
                    int parseInt = Integer.parseInt(matcher2.group(2));
                    int parseInt2 = Integer.parseInt(matcher2.group(4));
                    if (!"n".equals(matcher2.group(1).toLowerCase())) {
                        parseInt = -parseInt;
                    }
                    if (!"e".equals(matcher2.group(3).toLowerCase())) {
                        parseInt2 = -parseInt2;
                    }
                    long length = file.length();
                    long j = length / 2;
                    long sqrt = (long) Math.sqrt((double) j);
                    if (sqrt * sqrt == j) {
                        File file3 = file;
                        TileKey tileKey = new TileKey(parseInt, parseInt2);
                        HgtFileInfo hgtFileInfo = map2.get(tileKey);
                        if (hgtFileInfo == null || hgtFileInfo.size < length) {
                            map2.put(tileKey, new HgtFileInfo(HgtCache.this, file, (double) (parseInt - 1), (double) parseInt2, (double) parseInt, (double) (parseInt2 + 1)));
                        }
                    } else if (list2 != null) {
                        list2.add(file + " length in shorts (" + j + ") is not a square number");
                    }
                }
            }
        };
    }

    /* access modifiers changed from: package-private */
    public void indexOnThread() {
        this.hgtFiles.withRunningThread();
    }

    class LoadUnmergedFuture extends LazyFuture<HillshadingBitmap> {
        private final HgtFileInfo hgtFileInfo;

        LoadUnmergedFuture(HgtFileInfo hgtFileInfo2) {
            this.hgtFileInfo = hgtFileInfo2;
        }

        public HillshadingBitmap calculate() {
            ShadingAlgorithm.RawShadingResult transformToByteBuffer = HgtCache.this.algorithm.transformToByteBuffer(this.hgtFileInfo, HgtCache.this.interpolatorOverlap ? 1 : 0);
            transformToByteBuffer.fillPadding();
            return HgtCache.this.graphicsFactory.createMonoBitmap(transformToByteBuffer.width, transformToByteBuffer.height, transformToByteBuffer.bytes, transformToByteBuffer.padding, this.hgtFileInfo);
        }
    }

    class MergeOverlapFuture extends LazyFuture<HillshadingBitmap> {
        private HgtFileInfo hgtFileInfo;
        final LoadUnmergedFuture loadFuture;

        MergeOverlapFuture(HgtFileInfo hgtFileInfo2, LoadUnmergedFuture loadUnmergedFuture) {
            this.hgtFileInfo = hgtFileInfo2;
            this.loadFuture = loadUnmergedFuture;
        }

        MergeOverlapFuture(HgtCache hgtCache, HgtFileInfo hgtFileInfo2) {
            this(hgtFileInfo2, new LoadUnmergedFuture(hgtFileInfo2));
        }

        public HillshadingBitmap calculate() throws ExecutionException, InterruptedException {
            HillshadingBitmap hillshadingBitmap = (HillshadingBitmap) this.loadFuture.get();
            for (HillshadingBitmap.Border border : HillshadingBitmap.Border.values()) {
                mergePaddingOnBitmap(hillshadingBitmap, this.hgtFileInfo.getNeighbor(border), border);
            }
            return hillshadingBitmap;
        }

        private void mergePaddingOnBitmap(HillshadingBitmap hillshadingBitmap, HgtFileInfo hgtFileInfo2, HillshadingBitmap.Border border) {
            LoadUnmergedFuture access$300;
            int padding = hillshadingBitmap.getPadding();
            if (padding >= 1 && hgtFileInfo2 != null && (access$300 = hgtFileInfo2.getUnmergedAsMergePartner()) != null) {
                try {
                    HgtCache.mergeSameSized(hillshadingBitmap, (HillshadingBitmap) access$300.get(), border, padding, HgtCache.this.graphicsFactory.createCanvas());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class HgtFileInfo extends BoundingBox implements ShadingAlgorithm.RawHillTileSource {
        final File file;
        final long size;
        final /* synthetic */ HgtCache this$0;
        WeakReference<Future<HillshadingBitmap>> weakRef = null;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        HgtFileInfo(HgtCache hgtCache, File file2, double d, double d2, double d3, double d4) {
            super(d, d2, d3, d4);
            this.this$0 = hgtCache;
            this.file = file2;
            this.size = file2.length();
        }

        /* access modifiers changed from: package-private */
        public Future<HillshadingBitmap> getBitmapFuture(double d, double d2) {
            if (!this.this$0.interpolatorOverlap) {
                return getForLores();
            }
            double axisLenght = (double) this.this$0.algorithm.getAxisLenght(this);
            if (d > axisLenght || d2 > axisLenght) {
                return getForHires();
            }
            return getForLores();
        }

        private MergeOverlapFuture getForHires() {
            Future future;
            MergeOverlapFuture mergeOverlapFuture;
            WeakReference<Future<HillshadingBitmap>> weakReference = this.weakRef;
            if (weakReference == null) {
                future = null;
            } else {
                future = (Future) weakReference.get();
            }
            if (future instanceof MergeOverlapFuture) {
                mergeOverlapFuture = (MergeOverlapFuture) future;
            } else if (future instanceof LoadUnmergedFuture) {
                LoadUnmergedFuture loadUnmergedFuture = (LoadUnmergedFuture) future;
                MergeOverlapFuture mergeOverlapFuture2 = new MergeOverlapFuture(this, loadUnmergedFuture);
                this.weakRef = new WeakReference<>(mergeOverlapFuture2);
                this.this$0.secondaryLru.evict(loadUnmergedFuture);
                mergeOverlapFuture = mergeOverlapFuture2;
            } else {
                mergeOverlapFuture = new MergeOverlapFuture(this.this$0, this);
                this.weakRef = new WeakReference<>(mergeOverlapFuture);
            }
            this.this$0.mainLru.markUsed(mergeOverlapFuture);
            return mergeOverlapFuture;
        }

        /* access modifiers changed from: private */
        public LoadUnmergedFuture getUnmergedAsMergePartner() {
            Future future;
            WeakReference<Future<HillshadingBitmap>> weakReference = this.weakRef;
            if (weakReference == null) {
                future = null;
            } else {
                future = (Future) weakReference.get();
            }
            if (future instanceof LoadUnmergedFuture) {
                this.this$0.secondaryLru.markUsed(future);
                return (LoadUnmergedFuture) future;
            } else if (future instanceof MergeOverlapFuture) {
                this.this$0.mainLru.markUsed(future);
                return ((MergeOverlapFuture) future).loadFuture;
            } else {
                LoadUnmergedFuture loadUnmergedFuture = new LoadUnmergedFuture(this);
                this.weakRef = new WeakReference<>(loadUnmergedFuture);
                this.this$0.secondaryLru.markUsed(loadUnmergedFuture);
                return loadUnmergedFuture;
            }
        }

        private Future<HillshadingBitmap> getForLores() {
            Future<HillshadingBitmap> future;
            WeakReference<Future<HillshadingBitmap>> weakReference = this.weakRef;
            if (weakReference == null) {
                future = null;
            } else {
                future = (Future) weakReference.get();
            }
            if (future == null) {
                future = new LoadUnmergedFuture(this);
                this.weakRef = new WeakReference<>(future);
            }
            Future<HillshadingBitmap> markUsed = this.this$0.mainLru.markUsed(future);
            if (this.this$0.secondaryLru != null) {
                this.this$0.secondaryLru.markUsed(markUsed);
            }
            return future;
        }

        public HillshadingBitmap getFinishedConverted() {
            Future future;
            WeakReference<Future<HillshadingBitmap>> weakReference = this.weakRef;
            if (weakReference == null || (future = (Future) weakReference.get()) == null || !future.isDone()) {
                return null;
            }
            try {
                return (HillshadingBitmap) future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        }

        public long getSize() {
            return this.size;
        }

        public File getFile() {
            return this.file;
        }

        public double northLat() {
            return this.maxLatitude;
        }

        public double southLat() {
            return this.minLatitude;
        }

        public double westLng() {
            return this.minLongitude;
        }

        public double eastLng() {
            return this.maxLongitude;
        }

        /* access modifiers changed from: private */
        public HgtFileInfo getNeighbor(HillshadingBitmap.Border border) throws ExecutionException, InterruptedException {
            Map map = (Map) this.this$0.hgtFiles.get();
            int i = C13232.$SwitchMap$org$mapsforge$core$graphics$HillshadingBitmap$Border[border.ordinal()];
            if (i == 1) {
                return (HgtFileInfo) map.get(new TileKey(((int) this.maxLatitude) + 1, (int) this.minLongitude));
            }
            if (i == 2) {
                return (HgtFileInfo) map.get(new TileKey(((int) this.maxLatitude) - 1, (int) this.minLongitude));
            }
            if (i == 3) {
                return (HgtFileInfo) map.get(new TileKey((int) this.maxLatitude, ((int) this.minLongitude) + 1));
            }
            if (i != 4) {
                return null;
            }
            return (HgtFileInfo) map.get(new TileKey((int) this.maxLatitude, ((int) this.minLongitude) - 1));
        }

        public String toString() {
            WeakReference<Future<HillshadingBitmap>> weakReference = this.weakRef;
            Future future = weakReference == null ? null : (Future) weakReference.get();
            StringBuilder sb = new StringBuilder();
            sb.append("[lt:");
            sb.append(this.minLatitude);
            sb.append("-");
            sb.append(this.maxLatitude);
            sb.append(" ln:");
            sb.append(this.minLongitude);
            sb.append("-");
            sb.append(this.maxLongitude);
            sb.append(future == null ? "" : future.isDone() ? "done" : "wip");
            sb.append("]");
            return sb.toString();
        }
    }

    /* renamed from: org.mapsforge.map.layer.hills.HgtCache$2 */
    static /* synthetic */ class C13232 {
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$core$graphics$HillshadingBitmap$Border;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                org.mapsforge.core.graphics.HillshadingBitmap$Border[] r0 = org.mapsforge.core.graphics.HillshadingBitmap.Border.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$mapsforge$core$graphics$HillshadingBitmap$Border = r0
                org.mapsforge.core.graphics.HillshadingBitmap$Border r1 = org.mapsforge.core.graphics.HillshadingBitmap.Border.NORTH     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$HillshadingBitmap$Border     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.core.graphics.HillshadingBitmap$Border r1 = org.mapsforge.core.graphics.HillshadingBitmap.Border.SOUTH     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$HillshadingBitmap$Border     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.mapsforge.core.graphics.HillshadingBitmap$Border r1 = org.mapsforge.core.graphics.HillshadingBitmap.Border.EAST     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$HillshadingBitmap$Border     // Catch:{ NoSuchFieldError -> 0x0033 }
                org.mapsforge.core.graphics.HillshadingBitmap$Border r1 = org.mapsforge.core.graphics.HillshadingBitmap.Border.WEST     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.hills.HgtCache.C13232.<clinit>():void");
        }
    }

    public HillshadingBitmap getHillshadingBitmap(int i, int i2, double d, double d2) throws InterruptedException, ExecutionException {
        HgtFileInfo hgtFileInfo = (HgtFileInfo) this.hgtFiles.get().get(new TileKey(i, i2));
        if (hgtFileInfo == null) {
            return null;
        }
        return hgtFileInfo.getBitmapFuture(d, d2).get();
    }

    static void mergeSameSized(HillshadingBitmap hillshadingBitmap, HillshadingBitmap hillshadingBitmap2, HillshadingBitmap.Border border, int i, Canvas canvas) {
        if (border == HillshadingBitmap.Border.EAST) {
            canvas.setBitmap(hillshadingBitmap);
            int i2 = i * 2;
            canvas.setClip(hillshadingBitmap.getWidth() - i, i, i, hillshadingBitmap.getHeight() - i2);
            canvas.drawBitmap((Bitmap) hillshadingBitmap2, hillshadingBitmap2.getWidth() - i2, 0);
        } else if (border == HillshadingBitmap.Border.WEST) {
            canvas.setBitmap(hillshadingBitmap);
            int i3 = i * 2;
            canvas.setClip(0, i, i, hillshadingBitmap.getHeight() - i3);
            canvas.drawBitmap((Bitmap) hillshadingBitmap2, i3 - hillshadingBitmap2.getWidth(), 0);
        } else if (border == HillshadingBitmap.Border.NORTH) {
            canvas.setBitmap(hillshadingBitmap);
            int i4 = i * 2;
            canvas.setClip(i, 0, hillshadingBitmap.getWidth() - i4, i);
            canvas.drawBitmap((Bitmap) hillshadingBitmap2, 0, i4 - hillshadingBitmap2.getHeight());
        } else if (border == HillshadingBitmap.Border.SOUTH) {
            canvas.setBitmap(hillshadingBitmap);
            int i5 = i * 2;
            canvas.setClip(i, hillshadingBitmap.getHeight() - i, hillshadingBitmap.getWidth() - i5, i);
            canvas.drawBitmap((Bitmap) hillshadingBitmap2, 0, hillshadingBitmap2.getHeight() - i5);
        }
    }
}
