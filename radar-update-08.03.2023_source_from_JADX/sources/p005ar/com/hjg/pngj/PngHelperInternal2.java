package p005ar.com.hjg.pngj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/* renamed from: ar.com.hjg.pngj.PngHelperInternal2 */
final class PngHelperInternal2 {
    PngHelperInternal2() {
    }

    static OutputStream ostreamFromFile(File file, boolean z) {
        if (!file.exists() || z) {
            try {
                return new FileOutputStream(file);
            } catch (Exception e) {
                throw new PngjInputException("Could not open for write" + file, e);
            }
        } else {
            throw new PngjOutputException("File already exists: " + file);
        }
    }
}
