package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.core.util.Pair;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.google.android.material.internal.ViewUtils;
import java.util.Collection;

public interface DateSelector<S> extends Parcelable {
    int getDefaultThemeResId(Context context);

    int getDefaultTitleResId();

    Collection<Long> getSelectedDays();

    Collection<Pair<Long, Long>> getSelectedRanges();

    S getSelection();

    String getSelectionDisplayString(Context context);

    boolean isSelectionComplete();

    View onCreateTextInputView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle, CalendarConstraints calendarConstraints, OnSelectionChangedListener<S> onSelectionChangedListener);

    void select(long j);

    void setSelection(S s);

    @SynthesizedClassV2(kind = 7, versionHash = "5e5398f0546d1d7afd62641edb14d82894f11ddc41bce363a0c8d0dac82c9c5a")
    /* renamed from: com.google.android.material.datepicker.DateSelector$-CC  reason: invalid class name */
    public final /* synthetic */ class CC<S> {
        public static void showKeyboardWithAutoHideBehavior(EditText... editTextArr) {
            if (editTextArr.length != 0) {
                DateSelector$$ExternalSyntheticLambda0 dateSelector$$ExternalSyntheticLambda0 = new DateSelector$$ExternalSyntheticLambda0(editTextArr);
                for (EditText onFocusChangeListener : editTextArr) {
                    onFocusChangeListener.setOnFocusChangeListener(dateSelector$$ExternalSyntheticLambda0);
                }
                ViewUtils.requestFocusAndShowKeyboard(editTextArr[0]);
            }
        }

        public static /* synthetic */ void lambda$showKeyboardWithAutoHideBehavior$0(EditText[] editTextArr, View view, boolean z) {
            int length = editTextArr.length;
            int i = 0;
            while (i < length) {
                if (!editTextArr[i].hasFocus()) {
                    i++;
                } else {
                    return;
                }
            }
            ViewUtils.hideKeyboard(view);
        }
    }
}
