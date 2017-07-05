package com.example.deviceinformation;

import android.app.Application;
import android.content.Context;

/**
 * Created by kannas on 6/24/2017.
 */

public  class testproject extends Application {
    public static testproject instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
    public static testproject getInstance() {
        return instance;
    }
}