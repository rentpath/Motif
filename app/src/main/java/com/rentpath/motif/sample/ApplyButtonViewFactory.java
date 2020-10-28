package com.rentpath.motif.sample;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.rentpath.motif.factory.MotifFactory;
import com.rentpath.motif.factory.ViewFactory;

public class ApplyButtonViewFactory extends ViewFactory {

    private int mColor = -1;

    @Override
    public void onViewCreated(MotifFactory motifFactory, Context context, View view, AttributeSet attrs) {
        if (!(view instanceof Button)) {
            return;
        }

        if (mColor != -1) {
            int darkerColor = darker(mColor, 0.9f);
            Drawable drawable = view.getBackground();
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]
                            {
                                    new int[]{android.R.attr.state_pressed},
                                    new int[]{android.R.attr.state_focused},
                                    new int[]{android.R.attr.state_activated},
                                    new int[]{}
                            },
                    new int[]
                            {
                                    darkerColor,
                                    darkerColor,
                                    darkerColor,
                                    mColor
                            }
            );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable instanceof RippleDrawable) {
                RippleDrawable background = (RippleDrawable) drawable;
                background.getCurrent().setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
                background.setColor(colorStateList);
            } else {
                ViewCompat.setBackgroundTintMode(view, PorterDuff.Mode.SRC_ATOP);
                ViewCompat.setBackgroundTintList(view, colorStateList);
            }
        }
    }

    public void setColor(int color) {
        mColor = color;
    }

    private int darker(int color, float factor) {
        int a = Color.alpha( color );
        int r = Color.red( color );
        int g = Color.green( color );
        int b = Color.blue( color );

        return Color.argb( a,
                Math.max( (int)(r * factor), 0 ),
                Math.max( (int)(g * factor), 0 ),
                Math.max( (int)(b * factor), 0 ) );
    }
}
