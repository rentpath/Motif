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

import java.util.HashMap;
import java.util.Map;

public class MotifFactory {

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
        } else {
            logViewClassError(view.getClass());
        }
    }

    private void logViewClassError(Class clazz) {
        Log.e(TAG, "Unable to apply theme to view of type " + clazz);
    }

    private MotifConfig getConfig() {
        return MotifConfig.get();
    }
}
