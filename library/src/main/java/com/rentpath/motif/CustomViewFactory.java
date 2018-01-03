package com.rentpath.motif;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Method;

class CustomViewFactory extends ViewFactory<View> {

    @Override
    void onViewCreated(MotifFactory motifFactory, Context context, View view, AttributeSet attrs, int[] attributeId) {
        final Method setTypeface = ReflectionUtils.getMethod(view.getClass(), "setTypeface");
        String fontPath = resolveFontPath(context, attrs, attributeId);
        Typeface typeface = getDefaultTypeface(context, fontPath);
        if (setTypeface != null && typeface != null) {
            ReflectionUtils.invokeMethod(view, setTypeface, typeface);
        }
    }
}
