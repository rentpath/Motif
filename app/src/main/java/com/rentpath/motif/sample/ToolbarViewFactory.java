package com.rentpath.motif.sample;

import android.content.Context;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.rentpath.motif.factory.MotifFactory;
import com.rentpath.motif.factory.ViewFactory;

public class ToolbarViewFactory extends ViewFactory {

    private int mColor = -1;

    @Override
    public void onViewCreated(MotifFactory motifFactory, Context context, View view, AttributeSet attrs) {
        if (!(view instanceof Toolbar)) {
            return;
        }

        if (mColor != -1) {
            view.setBackgroundColor(mColor);
        }
    }

    public void setColor(int color) {
        mColor = color;
    }
}
