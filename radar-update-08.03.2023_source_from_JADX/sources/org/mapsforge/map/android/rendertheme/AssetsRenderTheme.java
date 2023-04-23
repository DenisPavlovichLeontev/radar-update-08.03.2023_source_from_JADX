package org.mapsforge.map.android.rendertheme;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStream;
import org.mapsforge.core.util.Utils;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderThemeMenuCallback;

public class AssetsRenderTheme implements XmlRenderTheme {
    private final String assetName;
    private final InputStream inputStream;
    private XmlRenderThemeMenuCallback menuCallback;
    private final String relativePathPrefix;

    public AssetsRenderTheme(Context context, String str, String str2) throws IOException {
        this(context, str, str2, (XmlRenderThemeMenuCallback) null);
    }

    public AssetsRenderTheme(Context context, String str, String str2, XmlRenderThemeMenuCallback xmlRenderThemeMenuCallback) throws IOException {
        this.assetName = str2;
        this.relativePathPrefix = str;
        AssetManager assets = context.getAssets();
        StringBuilder sb = new StringBuilder();
        sb.append(TextUtils.isEmpty(str) ? "" : str);
        sb.append(str2);
        this.inputStream = assets.open(sb.toString());
        this.menuCallback = xmlRenderThemeMenuCallback;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AssetsRenderTheme)) {
            return false;
        }
        AssetsRenderTheme assetsRenderTheme = (AssetsRenderTheme) obj;
        return Utils.equals(this.assetName, assetsRenderTheme.assetName) && Utils.equals(this.relativePathPrefix, assetsRenderTheme.relativePathPrefix);
    }

    public XmlRenderThemeMenuCallback getMenuCallback() {
        return this.menuCallback;
    }

    public String getRelativePathPrefix() {
        return this.relativePathPrefix;
    }

    public InputStream getRenderThemeAsStream() {
        return this.inputStream;
    }

    public int hashCode() {
        String str = this.assetName;
        int i = 0;
        int hashCode = ((str == null ? 0 : str.hashCode()) + 31) * 31;
        String str2 = this.relativePathPrefix;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode + i;
    }

    public void setMenuCallback(XmlRenderThemeMenuCallback xmlRenderThemeMenuCallback) {
        this.menuCallback = xmlRenderThemeMenuCallback;
    }
}
