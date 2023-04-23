package p005ar.com.hjg.pngj.pixels;

import java.awt.image.BufferedImage;
import p005ar.com.hjg.pngj.IImageLineSet;
import p005ar.com.hjg.pngj.ImageInfo;

/* renamed from: ar.com.hjg.pngj.pixels.ImageLineSetARGBbi */
public class ImageLineSetARGBbi implements IImageLineSet<ImageLineARGBbi> {
    BufferedImage image;
    private ImageInfo imginfo;
    private ImageLineARGBbi line;

    public int size() {
        return 1;
    }

    public ImageLineSetARGBbi(BufferedImage bufferedImage, ImageInfo imageInfo) {
        this.image = bufferedImage;
        this.imginfo = imageInfo;
        this.line = new ImageLineARGBbi(imageInfo, bufferedImage, bufferedImage.getRaster().getDataBuffer().getData());
    }

    public ImageLineARGBbi getImageLine(int i) {
        this.line.setRowNumber(i);
        return this.line;
    }

    public boolean hasImageLine(int i) {
        return i >= 0 && i < this.imginfo.rows;
    }
}
