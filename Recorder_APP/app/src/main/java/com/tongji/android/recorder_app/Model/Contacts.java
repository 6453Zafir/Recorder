package com.tongji.android.recorder_app.Model;

/**
 * Created by admin on 2016/6/13.
 */
public class Contacts {
    public  String phoneNumber;
    public  String username;
    public String id;
    public boolean ifadded;

    public Contacts(String id,String phoneNumber, String username,boolean ifadded) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.ifadded = ifadded;
    }

    @Override
    public String toString() {
        return username;
    }
}
