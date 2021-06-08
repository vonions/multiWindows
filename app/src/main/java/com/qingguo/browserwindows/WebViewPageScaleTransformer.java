package com.qingguo.browserwindows;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;


public class WebViewPageScaleTransformer implements ViewPager2.PageTransformer {

    private final float scale = 0.6f;
    private int transXOffset;
    private int pageMargin;

    private WebViewPageScaleTransformer() {
    }

    public WebViewPageScaleTransformer(int pageWidth, int pageHeight) {
        if (pageHeight == 0) {
            pageWidth = ScreenUtils.getDisplayMetrics().widthPixels;
            pageHeight = ScreenUtils.getDisplayMetrics().heightPixels;
        }
        transXOffset = (int) (pageWidth * (1 - scale));
        pageMargin = ScreenUtils.dpToPx(30);
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setScaleX(scale);
        page.setScaleY(scale);
        page.setTranslationX((-transXOffset + pageMargin) * position);
    }
}
