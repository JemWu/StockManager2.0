package com.app.jem.stockmanager;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by jem on 2017/4/26.
 */

public class leakcanary extends Application {
    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
