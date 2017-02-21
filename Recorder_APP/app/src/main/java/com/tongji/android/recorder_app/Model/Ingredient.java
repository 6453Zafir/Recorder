package com.tongji.android.recorder_app.Model;

public class Ingredient {

    private String mName;
    private String phone;

    public Ingredient(String name,String phone) {
        mName = name;
        this.phone = phone;
    }

    public String getName() {
        return mName;
    }

    public String getPhone() {
        return phone;
    }
}
