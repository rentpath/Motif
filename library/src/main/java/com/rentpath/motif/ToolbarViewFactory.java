package com.rentpath.motif;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

class ToolbarViewFactory extends ViewFactory<Toolbar> {

    @Override
    void onViewCreated(MotifFactory motifFactory, Context context, Toolbar view, AttributeSet attrs, int[] attributeId) {
        applyFontToToolbar(motifFactory, view);
    }

    /**
     * Will forceably set text on the views then remove ones that didn't have copy.
     *
     * @param view toolbar view.
     */
    private void applyFontToToolbar(MotifFactory motifFactory, final Toolbar view) {
        final CharSequence previousTitle = view.getTitle();
        final CharSequence previousSubtitle = view.getSubtitle();
        // The toolbar inflates both the title and the subtitle views lazily but luckily they do it
        // synchronously when you set a title and a subtitle programmatically.
        // So we set a title and a subtitle to something, then get the views, then revert.
        view.setTitle(" ");
        view.setSubtitle(" ");

        // Iterate through the children to run post inflation on them
        final int childCount = view.getChildCount();
        for (int i = 0; i < childCount; i++) {
            motifFactory.onViewCreated(view.getChildAt(i), view.getContext(), null);
        }
        // Remove views from view if they didn't have copy set.
        view.setTitle(previousTitle);
        view.setSubtitle(previousSubtitle);
    }
}
