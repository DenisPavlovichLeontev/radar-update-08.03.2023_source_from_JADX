package org.osmdroid.views.overlay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import org.osmdroid.library.C1340R;

public class ItemizedOverlayControlView extends LinearLayout {
    protected ImageButton mCenterToButton;
    protected ItemizedOverlayControlViewListener mLis;
    protected ImageButton mNavToButton;
    protected ImageButton mNextButton;
    protected ImageButton mPreviousButton;

    public interface ItemizedOverlayControlViewListener {
        void onCenter();

        void onNavTo();

        void onNext();

        void onPrevious();
    }

    public ItemizedOverlayControlView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ImageButton imageButton = new ImageButton(context);
        this.mPreviousButton = imageButton;
        imageButton.setImageDrawable(context.getResources().getDrawable(C1340R.C1341drawable.previous));
        ImageButton imageButton2 = new ImageButton(context);
        this.mNextButton = imageButton2;
        imageButton2.setImageDrawable(context.getResources().getDrawable(C1340R.C1341drawable.next));
        ImageButton imageButton3 = new ImageButton(context);
        this.mCenterToButton = imageButton3;
        imageButton3.setImageDrawable(context.getResources().getDrawable(C1340R.C1341drawable.center));
        ImageButton imageButton4 = new ImageButton(context);
        this.mNavToButton = imageButton4;
        imageButton4.setImageDrawable(context.getResources().getDrawable(C1340R.C1341drawable.navto_small));
        addView(this.mPreviousButton, new LinearLayout.LayoutParams(-2, -2));
        addView(this.mCenterToButton, new LinearLayout.LayoutParams(-2, -2));
        addView(this.mNavToButton, new LinearLayout.LayoutParams(-2, -2));
        addView(this.mNextButton, new LinearLayout.LayoutParams(-2, -2));
        initViewListeners();
    }

    public void setItemizedOverlayControlViewListener(ItemizedOverlayControlViewListener itemizedOverlayControlViewListener) {
        this.mLis = itemizedOverlayControlViewListener;
    }

    public void setNextEnabled(boolean z) {
        this.mNextButton.setEnabled(z);
    }

    public void setPreviousEnabled(boolean z) {
        this.mPreviousButton.setEnabled(z);
    }

    public void setNavToVisible(int i) {
        this.mNavToButton.setVisibility(i);
    }

    private void initViewListeners() {
        this.mNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ItemizedOverlayControlView.this.mLis != null) {
                    ItemizedOverlayControlView.this.mLis.onNext();
                }
            }
        });
        this.mPreviousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ItemizedOverlayControlView.this.mLis != null) {
                    ItemizedOverlayControlView.this.mLis.onPrevious();
                }
            }
        });
        this.mCenterToButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ItemizedOverlayControlView.this.mLis != null) {
                    ItemizedOverlayControlView.this.mLis.onCenter();
                }
            }
        });
        this.mNavToButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ItemizedOverlayControlView.this.mLis != null) {
                    ItemizedOverlayControlView.this.mLis.onNavTo();
                }
            }
        });
    }
}
