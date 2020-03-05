package com.xiandao.android.view;

import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * Created by zengx on 2019/5/15.
 * Describe:
 */
public class NoUnderlineSpan extends UnderlineSpan {
    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
