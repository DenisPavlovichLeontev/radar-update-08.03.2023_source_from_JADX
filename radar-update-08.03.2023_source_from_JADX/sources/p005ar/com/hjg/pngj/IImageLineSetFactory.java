package p005ar.com.hjg.pngj;

import p005ar.com.hjg.pngj.IImageLine;

/* renamed from: ar.com.hjg.pngj.IImageLineSetFactory */
public interface IImageLineSetFactory<T extends IImageLine> {
    IImageLineSet<T> create(ImageInfo imageInfo, boolean z, int i, int i2, int i3);
}
