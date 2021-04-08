package com.appteam.myapplication.base;

import android.app.Application;

import com.appteam.myapplication.BuildConfig;

import timber.log.Timber;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }
}
