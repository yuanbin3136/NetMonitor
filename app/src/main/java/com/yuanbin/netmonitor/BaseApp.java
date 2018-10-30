package com.yuanbin.netmonitor;

import android.app.Application;
import android.content.Context;

import com.yuanbin.netmonitor.Utils.L;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        L.setContext(this);
    }
    private static Context mContext;
    public static Context getContext(){
        return mContext;
    }
}
