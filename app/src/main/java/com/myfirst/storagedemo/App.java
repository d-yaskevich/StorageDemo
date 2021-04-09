package com.myfirst.storagedemo;

import android.app.Application;

import com.myfirst.storagedemo.managers.ContentManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ContentManager.init(this);
    }
}
