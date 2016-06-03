package com.tongji.android.recorder_app.Application;

import android.app.Application;

/**
 * Created by 重书 on 2016/5/31.
 */
public class MyApplication extends Application {
    public static int ONLINE = 1;
    public static int OFFLINE = 0;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

    @Override
    public void onCreate() {
        super.onCreate();
        status = OFFLINE;
    }
}
