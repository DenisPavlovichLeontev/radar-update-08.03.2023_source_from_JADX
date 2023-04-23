package p005ar.com.hjg.pngj;

import p005ar.com.hjg.pngj.IImageLine;

/* renamed from: ar.com.hjg.pngj.IImageLineFactory */
public interface IImageLineFactory<T extends IImageLine> {
    T createImageLine(ImageInfo imageInfo);
}
