package com.myfirst.storagedemo;

import android.app.Application;

import com.myfirst.storagedemo.managers.DatabaseManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseManager.init(this);
    }
}
