package p005ar.com.hjg.pngj;

import p005ar.com.hjg.pngj.IImageLine;

/* renamed from: ar.com.hjg.pngj.IImageLineSet */
public interface IImageLineSet<T extends IImageLine> {
    T getImageLine(int i);

    boolean hasImageLine(int i);

    int size();
}
