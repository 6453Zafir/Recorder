package com.tongji.android.recorder_app.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by 重书 on 2016/6/3.
 */
public class Habit implements Serializable {

    public static int TYPE_DATE = 0;
    public static int TYPE_DURATION = 3;
    public static int TYPE_DOORNOT = 2;
    public static int TYPE_DEGREE = 1;
    public  String id;
    public  String habitName;

    public int score;
    public int type;
    public String feature;
    public boolean isChecked;

    public Habit(String id, String habitName,int score,int type, String feature) {
        this.id = id;
        this.habitName = habitName;
        this.score = score;
        this.type=type;
        this.isChecked= false;
        this.feature = feature;
    }

    @Override
    public String toString() {
        return habitName;
    }
}
