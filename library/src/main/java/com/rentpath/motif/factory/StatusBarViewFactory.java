package com.rentpath.motif.factory;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.WindowManager;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public abstract class StatusBarViewFactory {

    public abstract int getStatusBarColor(Context context);

    public int getWindowFlags() {
        return WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
    }
}
