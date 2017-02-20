package com.osh.hackathonbrowser;

import android.app.Application;
import android.content.Context;

import com.osh.hackathonbrowser.cache.HackCache;

public class ApplicationClass extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        HackCache.getInstance().warm();
    }

    public static Context getAppContext(){
        return appContext;
    }
}
