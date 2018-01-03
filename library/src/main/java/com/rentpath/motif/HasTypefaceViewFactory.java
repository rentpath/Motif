package com.rentpath.motif;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

class HasTypefaceViewFactory extends ViewFactory<HasTypeface> {

    @Override
    void onViewCreated(MotifFactory motifFactory, Context context, HasTypeface view, AttributeSet attrs, int[] attributeId) {
        Typeface typeface = getDefaultTypeface(context, resolveFontPath(context, attrs, attributeId));
        if (typeface != null) {
            view.setTypeface(typeface);
        }
    }
}
