package com.tongji.android.recorder_app.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by 重书 on 2016/6/3.
 */
public class Habit implements Serializable {
    public  String id;
    public  String habitName;
    public int score;
    public int type;

    public Habit(String id, String habitName,int score,int type) {
        this.id = id;
        this.habitName = habitName;
        this.score = score;
        this.type=type;
    }

    @Override
    public String toString() {
        return habitName;
    }
}
