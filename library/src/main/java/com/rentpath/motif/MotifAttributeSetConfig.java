package com.rentpath.motif;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rentpath.motif.utils.MotifAttributeSetUtil;

public interface MotifAttributeSetConfig {

    /**
     * Interface to allow implementor to modify the AttributeSet by using the {@link MotifAttributeSetUtil}
     */
    AttributeSet getConfiguredAttributeSet(@Nullable View view, String name, Context context, AttributeSet attrs);
}
