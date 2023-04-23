package okio.internal;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo19893d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n"}, mo19894d2 = {"<anonymous>", "", "it", "Lokio/internal/ZipEntry;"}, mo19895k = 3, mo19896mv = {1, 5, 1}, mo19898xi = 48)
/* compiled from: zip.kt */
final class ZipKt$openZip$1 extends Lambda implements Function1<ZipEntry, Boolean> {
    public static final ZipKt$openZip$1 INSTANCE = new ZipKt$openZip$1();

    ZipKt$openZip$1() {
        super(1);
    }

    public final Boolean invoke(ZipEntry zipEntry) {
        Intrinsics.checkNotNullParameter(zipEntry, "it");
        return true;
    }
}
