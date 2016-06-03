package com.tongji.android.recorder_app.Model;

import java.util.Date;

/**
 * Created by 重书 on 2016/6/3.
 */
public class Habit {
    public  String id;
    public  String habitName;
    public Date date;

    public Habit(String id, String habitName,Date date) {
        this.id = id;
        this.habitName = habitName;
        this.date = date;
    }

    @Override
    public String toString() {
        return habitName;
    }
}
