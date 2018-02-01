package com.rentpath.motif.factory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.rentpath.motif.MotifConfig;
import com.rentpath.motif.R;

import java.util.ArrayList;
import java.util.List;

public class MotifFactory implements ViewGroup.OnHierarchyChangeListener {

    private static final String TAG = "Motif";

    public MotifFactory() {
    }

    /**
     * Handle the created view. If the config has an overriding view factory, or has a custom view factory registered for a
     * specific view type, we will use this to handle view creation. Our internal code will not be executed if this view
     * factory exists. If you have disabled internal theming, internal view factories will NOT be called.
     *
     * @param view    nullable.
     * @param context shouldn't be null.
     * @param attrs   shouldn't be null.
     * @return null if null is passed in.
     */
    public View onViewCreated(View view, Context context, AttributeSet attrs) {
        if (view != null && view.getTag(R.id.motif_tag_id) != Boolean.TRUE) {
            if (getConfig().getOverridingViewFactory() != null) {
                getConfig().getOverridingViewFactory().onViewCreated(this, context, view, attrs);
            } else {
                if (getConfig().hasRegisteredViewFactoryRegisteredForId(view.getId())) {
                    getConfig().getRegisteredViewFactoryForId(view.getId()).onViewCreated(this, context, view, attrs);
                } else if (getConfig().hasRegisteredViewFactoryRegisteredForClass(view.getClass())) {
                    getConfig().getRegisteredViewFactoryForClass(view.getClass()).onViewCreated(this, context, view, attrs);
                }
            }
            view.setTag(R.id.motif_tag_id, Boolean.TRUE);
        }
        return view;
    }

    /**
     * Called when a view is added to a view group. This checks the child type and if a view group is added, we ensure we
     * catch all children of that view group so proper theming can be applied
     *
     * @param parent Parent view the child is added to
     * @param child Child view being added
     */
    @Override
    public void onChildViewAdded(View parent, View child) {
        if (child instanceof ViewGroup) {
            List<View> nestedChildren = getChildrenFromViewGroup((ViewGroup) child);
            for (View nestedChild : nestedChildren) {
                onChildViewAdded(child, nestedChild);
            }
        } else {
            onViewCreated(child, parent.getContext(), null);
        }
    }

    /**
     * Ignored
     *
     * @param parent Parent view the child is removed from
     * @param child Child view being removed
     */
    @Override
    public void onChildViewRemoved(View parent, View child) {
    }

    private List<View> getChildrenFromViewGroup(ViewGroup viewGroup) {
        List<View> children = new ArrayList<>();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            children.add(viewGroup.getChildAt(i));
        }
        return children;
    }

    private MotifConfig getConfig() {
        return MotifConfig.get();
    }
}
