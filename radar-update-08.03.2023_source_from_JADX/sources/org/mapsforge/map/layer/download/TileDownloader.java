package org.mapsforge.map.layer.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import org.mapsforge.core.graphics.CorruptedInputStreamException;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.util.IOUtils;

class TileDownloader {
    private final DownloadJob downloadJob;
    private final GraphicFactory graphicFactory;

    private static InputStream getInputStream(URLConnection uRLConnection) throws IOException {
        if ("gzip".equals(uRLConnection.getContentEncoding())) {
            return new GZIPInputStream(uRLConnection.getInputStream());
        }
        return uRLConnection.getInputStream();
    }

    TileDownloader(DownloadJob downloadJob2, GraphicFactory graphicFactory2) {
        if (downloadJob2 == null) {
            throw new IllegalArgumentException("downloadJob must not be null");
        } else if (graphicFactory2 != null) {
            this.downloadJob = downloadJob2;
            this.graphicFactory = graphicFactory2;
        } else {
            throw new IllegalArgumentException("graphicFactory must not be null");
        }
    }

    /* access modifiers changed from: package-private */
    public TileBitmap downloadImage() throws IOException {
        URLConnection openConnection = this.downloadJob.tileSource.getTileUrl(this.downloadJob.tile).openConnection();
        openConnection.setConnectTimeout(this.downloadJob.tileSource.getTimeoutConnect());
        openConnection.setReadTimeout(this.downloadJob.tileSource.getTimeoutRead());
        if (this.downloadJob.tileSource.getUserAgent() != null) {
            openConnection.setRequestProperty("User-Agent", this.downloadJob.tileSource.getUserAgent());
        }
        if (this.downloadJob.tileSource.getReferer() != null) {
            openConnection.setRequestProperty("Referer", this.downloadJob.tileSource.getReferer());
        }
        if (this.downloadJob.tileSource.getAuthorization() != null) {
            openConnection.setRequestProperty("Authorization", this.downloadJob.tileSource.getAuthorization());
        }
        if (openConnection instanceof HttpURLConnection) {
            ((HttpURLConnection) openConnection).setInstanceFollowRedirects(this.downloadJob.tileSource.isFollowRedirects());
        }
        InputStream inputStream = getInputStream(openConnection);
        try {
            TileBitmap createTileBitmap = this.graphicFactory.createTileBitmap(inputStream, this.downloadJob.tile.tileSize, this.downloadJob.hasAlpha);
            createTileBitmap.setExpiration(openConnection.getExpiration());
            return createTileBitmap;
        } catch (CorruptedInputStreamException unused) {
            return null;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
