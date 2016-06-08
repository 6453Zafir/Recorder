package com.tongji.android.recorder_app.Model;

/**
 * Created by 重书 on 2016/6/5.
 */
public class Friend {
    public  String phoneNumber;
    public  String username;
    public int score;

    public Friend(String phoneNumber, String username,int score) {
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.score = score;
    }

    @Override
    public String toString() {
        return username;
    }
}
