package com.rentpath.motif.factory;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.rentpath.motif.utils.MotifUtils;
import com.rentpath.motif.utils.TypefaceUtils;

class TextViewFactory extends ViewFactory<TextView> {

    @Override
    public void onViewCreated(MotifFactory motifFactory, Context context, TextView view, AttributeSet attrs, int[] attributeId) {
        // Fast path the setting of TextView's font, means if we do some delayed setting of font,
        // which has already been set by use we skip this TextView (mainly for inflating custom,
        // TextView's inside the Toolbar/ActionBar).
        if (TypefaceUtils.isLoaded(view.getTypeface())) {
            return;
        }
        // Try to get typeface attribute value
        // Since we're not using namespace it's a little bit tricky

        // Check xml attrs, style attrs and text appearance for font path
        String textViewFont = resolveFontPath(context, attrs, attributeId);

        // Try theme attributes
        if (TextUtils.isEmpty(textViewFont)) {
            final int[] styleForTextView = getStyleForTextView(view);
            if (styleForTextView[1] != -1)
                textViewFont = MotifUtils.pullFontPathFromTheme(context, styleForTextView[0], styleForTextView[1], attributeId);
            else
                textViewFont = MotifUtils.pullFontPathFromTheme(context, styleForTextView[0], attributeId);
        }

        // Still need to defer the Native action bar, appcompat-v7:21+ uses the Toolbar underneath. But won't match these anyway.
        final boolean deferred = matchesResourceIdName(view, ACTION_BAR_TITLE) || matchesResourceIdName(view, ACTION_BAR_SUBTITLE);

        MotifUtils.applyFontToTextView(context, view, getConfig(), textViewFont, deferred);
    }

}
