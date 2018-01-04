package com.rentpath.motif.sample;

import android.app.Application;

import com.rentpath.motif.MotifConfig;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MotifConfig.initDefault(new MotifConfig.Builder()
                .build());
    }
}
