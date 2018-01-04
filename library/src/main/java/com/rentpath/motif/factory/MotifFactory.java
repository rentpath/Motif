package com.rentpath.motif.factory;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import com.rentpath.motif.MotifConfig;
import com.rentpath.motif.R;
import com.rentpath.motif.utils.HasTypeface;
import com.rentpath.motif.utils.MotifUtils;

import java.util.HashMap;
import java.util.Map;

public class MotifFactory {

    private static final String TAG = "Motif";
    private static final Map<Class, ViewFactory> FACTORIES = new HashMap<>();

    static {
        FACTORIES.put(TextView.class, new TextViewFactory());
        FACTORIES.put(Toolbar.class, new ToolbarViewFactory());
        FACTORIES.put(ViewParent.class, new ViewParentFactory());
        FACTORIES.put(HasTypeface.class, new HasTypefaceViewFactory());
    }

    private final int[] mAttributeId;
    private CustomViewFactory mCustomViewFactory = new CustomViewFactory();

    public MotifFactory(int attributeId) {
        this.mAttributeId = new int[]{attributeId};
    }

    /**
     * Handle the created view
     *
     * @param view    nullable.
     * @param context shouldn't be null.
     * @param attrs   shouldn't be null.
     * @return null if null is passed in.
     */

    public View onViewCreated(View view, Context context, AttributeSet attrs) {
        if (view != null && view.getTag(R.id.motif_tag_id) != Boolean.TRUE) {
            onViewCreatedInternal(view, context, attrs);
            view.setTag(R.id.motif_tag_id, Boolean.TRUE);
        }
        return view;
    }

    private void onViewCreatedInternal(View view, final Context context, AttributeSet attrs) {
        ViewFactory viewFactory;

        try {
            viewFactory = FACTORIES.get(view.getClass());
        } catch (Throwable t) {
            if (MotifConfig.get().isCustomViewTypefaceSupport() && MotifConfig.get().isCustomViewHasTypeface(view)) {
                mCustomViewFactory.onViewCreated(this, context, view, attrs, mAttributeId);
            } else {
                logViewClassError(view.getClass());
            }
            return;
        }

        if (view instanceof TextView) {
            ((TextViewFactory) viewFactory).onViewCreated(this, context, (TextView) view, attrs, mAttributeId);
        }

        if (view instanceof ViewParent && viewFactory instanceof ViewParentFactory) {
            ((ViewParentFactory) viewFactory).onViewCreated(this, context, (ViewParent) view, attrs, mAttributeId);
        }

        // AppCompat API21+ The ActionBar doesn't inflate default Title/SubTitle, we need to scan the
        // Toolbar(Which underlies the ActionBar) for its children.
        if (MotifUtils.canCheckForV7Toolbar() && view instanceof android.support.v7.widget.Toolbar && viewFactory instanceof ToolbarViewFactory) {
            ((ToolbarViewFactory) viewFactory).onViewCreated(this, context, (Toolbar) view, attrs, mAttributeId);
        }

        // Try to set typeface for custom views using interface method or via reflection if available
        if (view instanceof HasTypeface && viewFactory instanceof HasTypefaceViewFactory) {
            ((HasTypefaceViewFactory) viewFactory).onViewCreated(this, context, (HasTypeface) view, attrs, mAttributeId);
        }
    }

    private void logViewClassError(Class clazz) {
        Log.e(TAG, "Unable to apply theme to view of type " + clazz);
    }
}
