package androidx.activity;

import android.os.Build;
import android.view.View;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;

@Metadata(mo19893d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u001a\u0013\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u0002H\u0007¢\u0006\u0002\b\u0003\u001a\u0019\u0010\u0004\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u0001H\u0007¢\u0006\u0002\b\u0007¨\u0006\b"}, mo19894d2 = {"findViewTreeOnBackPressedDispatcherOwner", "Landroidx/activity/OnBackPressedDispatcherOwner;", "Landroid/view/View;", "get", "setViewTreeOnBackPressedDispatcherOwner", "", "onBackPressedDispatcherOwner", "set", "activity_release"}, mo19895k = 2, mo19896mv = {1, 6, 0}, mo19898xi = 48)
/* compiled from: ViewTreeOnBackPressedDispatcherOwner.kt */
public final class ViewTreeOnBackPressedDispatcherOwner {
    public static final void set(View view, OnBackPressedDispatcherOwner onBackPressedDispatcherOwner) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Intrinsics.checkNotNullParameter(onBackPressedDispatcherOwner, "onBackPressedDispatcherOwner");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            view.setTag(C0008R.C0009id.view_tree_on_back_pressed_dispatcher_owner, onBackPressedDispatcherOwner);
        }
    }

    public static final OnBackPressedDispatcherOwner get(View view) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        return (OnBackPressedDispatcherOwner) SequencesKt.firstOrNull(SequencesKt.mapNotNull(SequencesKt.generateSequence(view, C0010x8c7c000a.INSTANCE), C0011x8c7c000b.INSTANCE));
    }
}
