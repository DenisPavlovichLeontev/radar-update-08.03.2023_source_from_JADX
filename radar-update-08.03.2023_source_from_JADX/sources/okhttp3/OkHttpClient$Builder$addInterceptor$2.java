package okhttp3;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Interceptor;

@Metadata(mo19893d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo19894d2 = {"<anonymous>", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "intercept"}, mo19895k = 3, mo19896mv = {1, 6, 0}, mo19898xi = 176)
/* compiled from: OkHttpClient.kt */
public final class OkHttpClient$Builder$addInterceptor$2 implements Interceptor {
    final /* synthetic */ Function1<Interceptor.Chain, Response> $block;

    public OkHttpClient$Builder$addInterceptor$2(Function1<? super Interceptor.Chain, Response> function1) {
        this.$block = function1;
    }

    public final Response intercept(Interceptor.Chain chain) {
        Intrinsics.checkNotNullParameter(chain, "chain");
        return this.$block.invoke(chain);
    }
}