package com.rentpath.motif.sample;

import android.app.Application;
import androidx.appcompat.widget.Toolbar;

import com.rentpath.motif.MotifConfig;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MotifConfig.initDefault(new MotifConfig.Builder()
                .registerViewFactoryForClass(Toolbar.class, new ToolbarViewFactory())
                .registerViewFactoryForIds(new ApplyButtonViewFactory(), R.id.apply)
                .registerStatusBarViewFactory(new SampleStatusBarViewFactory())
                .build());
    }
}
