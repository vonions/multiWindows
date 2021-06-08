package com.qingguo.browserwindows;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class WebViewPageNormalTransformer implements ViewPager2.PageTransformer {

    public WebViewPageNormalTransformer() {
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setScaleX(1);
        page.setScaleY(1);
        page.setTranslationX(0);
    }
}
