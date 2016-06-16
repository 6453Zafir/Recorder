package com.tongji.android.recorder_app.Model;

import java.io.Serializable;

/**
 * Created by lishigang on 16/6/7.
 */
public class SystemDefaultHabit implements Serializable {
    public String id;
    public String habitName;
    public int score;
    public int type;
    public String feature;

    public SystemDefaultHabit(String id, String habitName,int score,int type,String feature) {
        this.id = id;
        this.habitName = habitName;
        this.score = score;
        this.type=type;
        this.feature = feature;
    }

    @Override
    public String toString() {
        return habitName;
    }
}
