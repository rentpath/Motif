package com.rentpath.motif.factory;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.rentpath.motif.MotifConfig;
import com.rentpath.motif.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MotifFactory implements ViewGroup.OnHierarchyChangeListener {

    private static final String TAG = "Motif";
    private static final Map<Class, ViewFactory> FACTORIES = new HashMap<>();

    static {
        FACTORIES.put(Button.class, new ButtonFactory());
        FACTORIES.put(CheckBox.class, new CheckBoxFactory());
        FACTORIES.put(ImageButton.class, new ImageButtonFactory());
        FACTORIES.put(ImageView.class, new ImageViewFactory());
        FACTORIES.put(RadioButton.class, new RadioButtonFactory());
        FACTORIES.put(TextView.class, new TextViewFactory());
        FACTORIES.put(Toolbar.class, new ToolbarViewFactory());
        FACTORIES.put(View.class, new StandardViewFactory());
        FACTORIES.put(ViewGroup.class, new ViewGroupFactory());
    }

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
                if (getConfig().hasCustomViewFactoryRegisteredForClass(view.getClass())) {
                    getConfig().getCustomViewFactoryForClass(view.getClass()).onViewCreated(this, context, view, attrs);
                } else if (getConfig().hasCustomViewFactoryRegisteredForId(view.getId())) {
                    getConfig().getCustomViewFactoryForId(view.getId()).onViewCreated(this, context, view, attrs);
                } else if (!getConfig().isDisableInternalViewFactoryTheming()) {
                    onViewCreatedInternal(view, context, attrs);
                }
            }
            view.setTag(R.id.motif_tag_id, Boolean.TRUE);
        }
        return view;
    }

    private void onViewCreatedInternal(View view, final Context context, AttributeSet attrs) {
        if (view instanceof CheckBox) {
            ((CheckBoxFactory) FACTORIES.get(CheckBox.class)).onViewCreated(this, context, (CheckBox) view, attrs);
        } else if (view instanceof RadioButton) {
            ((RadioButtonFactory) FACTORIES.get(RadioButton.class)).onViewCreated(this, context, (RadioButton) view, attrs);
        } else if (view instanceof Button) {
            ((ButtonFactory) FACTORIES.get(Button.class)).onViewCreated(this, context, (Button) view, attrs);
        } else if (view instanceof ImageButton) {
            ((ImageButtonFactory) FACTORIES.get(ImageButton.class)).onViewCreated(this, context, (ImageButton) view, attrs);
        } else if (view instanceof ImageView) {
            ((ImageViewFactory) FACTORIES.get(ImageView.class)).onViewCreated(this, context, (ImageView) view, attrs);
        } else if (view instanceof TextView) {
            ((TextViewFactory) FACTORIES.get(TextView.class)).onViewCreated(this, context, (TextView) view, attrs);
        } else if (view instanceof ViewGroup) {
            ((ViewGroupFactory) FACTORIES.get(ViewGroup.class)).onViewCreated(this, context, (ViewGroup) view, attrs);

            // add our hierarchy listener to detect view additions and apply themes
            ((ViewGroup) view).setOnHierarchyChangeListener(this);
        } else {
            Log.e(TAG, "Unable to apply theme to view of type " + view.getClass());
        }
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
