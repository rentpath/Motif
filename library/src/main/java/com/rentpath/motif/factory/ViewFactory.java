package com.rentpath.motif.factory;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.rentpath.motif.MotifConfig;
import com.rentpath.motif.utils.MotifUtils;
import com.rentpath.motif.utils.TypefaceUtils;

public abstract class ViewFactory<T> {

    protected static final String ACTION_BAR_TITLE = "action_bar_title";
    protected static final String ACTION_BAR_SUBTITLE = "action_bar_subtitle";

    abstract void onViewCreated(MotifFactory motifFactory, Context context, T view, AttributeSet attrs, int[] attributeId);

    /**
     * Some styles are in sub styles, such as actionBarTextStyle etc..
     *
     * @param view view to check.
     * @return 2 element array, default to -1 unless a style has been found.
     */
    protected static int[] getStyleForTextView(TextView view) {
        final int[] styleIds = new int[]{-1, -1};
        // Try to find the specific actionbar styles
        if (isActionBarTitle(view)) {
            styleIds[0] = android.R.attr.actionBarStyle;
            styleIds[1] = android.R.attr.titleTextStyle;
        } else if (isActionBarSubTitle(view)) {
            styleIds[0] = android.R.attr.actionBarStyle;
            styleIds[1] = android.R.attr.subtitleTextStyle;
        }
        if (styleIds[0] == -1) {
            // Use TextAppearance as default style
            styleIds[0] = getConfig().getClassStyles().containsKey(view.getClass())
                    ? getConfig().getClassStyles().get(view.getClass())
                    : android.R.attr.textAppearance;
        }
        return styleIds;
    }

    /**
     * An even dirtier way to see if the TextView is part of the ActionBar
     *
     * @param view TextView to check is Title
     * @return true if it is.
     */
    protected static boolean isActionBarTitle(TextView view) {
        if (matchesResourceIdName(view, ACTION_BAR_TITLE)) return true;
        if (parentIsToolbarV7(view)) {
            final android.support.v7.widget.Toolbar parent = (android.support.v7.widget.Toolbar) view.getParent();
            return TextUtils.equals(parent.getTitle(), view.getText());
        }
        return false;
    }

    /**
     * An even dirtier way to see if the TextView is part of the ActionBar
     *
     * @param view TextView to check is Title
     * @return true if it is.
     */
    protected static boolean isActionBarSubTitle(TextView view) {
        if (matchesResourceIdName(view, ACTION_BAR_SUBTITLE)) return true;
        if (parentIsToolbarV7(view)) {
            final android.support.v7.widget.Toolbar parent = (android.support.v7.widget.Toolbar) view.getParent();
            return TextUtils.equals(parent.getSubtitle(), view.getText());
        }
        return false;
    }

    protected static boolean parentIsToolbarV7(View view) {
        return MotifUtils.canCheckForV7Toolbar() && view.getParent() != null && (view.getParent() instanceof android.support.v7.widget.Toolbar);
    }

    /**
     * Use to match a view against a potential view id. Such as ActionBar title etc.
     *
     * @param view    not null view you want to see has resource matching name.
     * @param matches not null resource name to match against. Its not case sensitive.
     * @return true if matches false otherwise.
     */
    protected static boolean matchesResourceIdName(View view, String matches) {
        if (view.getId() == View.NO_ID) return false;
        final String resourceEntryName = view.getResources().getResourceEntryName(view.getId());
        return resourceEntryName.equalsIgnoreCase(matches);
    }

    /**
     * Resolving font path from xml attrs, style attrs or text appearance
     */
    protected String resolveFontPath(Context context, AttributeSet attrs, int[] attributeId) {
        // Try view xml attributes
        String textViewFont = MotifUtils.pullFontPathFromView(context, attrs, attributeId);

        // Try view style attributes
        if (TextUtils.isEmpty(textViewFont)) {
            textViewFont = MotifUtils.pullFontPathFromStyle(context, attrs, attributeId);
        }

        // Try View TextAppearance
        if (TextUtils.isEmpty(textViewFont)) {
            textViewFont = MotifUtils.pullFontPathFromTextAppearance(context, attrs, attributeId);
        }

        return textViewFont;
    }

    protected Typeface getDefaultTypeface(Context context, String fontPath) {
        if (TextUtils.isEmpty(fontPath)) {
            fontPath = getConfig().getFontPath();
        }
        if (!TextUtils.isEmpty(fontPath)) {
            return TypefaceUtils.load(context.getAssets(), fontPath);
        }
        return null;
    }

    protected static MotifConfig getConfig() {
        return MotifConfig.get();
    }
}
