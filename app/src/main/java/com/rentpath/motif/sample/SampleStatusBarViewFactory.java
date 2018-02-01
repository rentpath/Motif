package com.rentpath.motif.sample;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;

import com.rentpath.motif.factory.StatusBarViewFactory;

public class SampleStatusBarViewFactory extends StatusBarViewFactory {

    private int mColor = -1;

    @Override
    public int getStatusBarColor(Context context) {
        if (mColor != -1) {
            return mColor;
        }

        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimaryDark });
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public void setColor(int color) {
        mColor = color;
    }
}
