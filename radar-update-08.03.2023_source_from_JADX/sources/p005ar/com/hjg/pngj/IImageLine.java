package p005ar.com.hjg.pngj;

/* renamed from: ar.com.hjg.pngj.IImageLine */
public interface IImageLine {
    void endReadFromPngRaw();

    void readFromPngRaw(byte[] bArr, int i, int i2, int i3);

    void writeToPngRaw(byte[] bArr);
}
