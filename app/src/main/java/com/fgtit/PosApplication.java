package com.fgtit;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;


public class PosApplication extends Application {
    public static PosApplication instances;
    private static Context context;

    public static PosApplication getInstances() {
        return instances;
    }

    public static Context getContext() {
        return instances.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        context = getApplicationContext();
        Stetho.initializeWithDefaults(this);
    }
}
