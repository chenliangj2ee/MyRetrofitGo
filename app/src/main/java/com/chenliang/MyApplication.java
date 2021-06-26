package com.chenliang;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    public static Context con;
    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.con=getApplicationContext();
    }
}
