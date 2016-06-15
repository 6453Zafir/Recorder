package com.tongji.android.recorder_app.Model;

import java.io.Serializable;

/**
 * Created by lishigang on 16/6/7.
 */
public class SystemDefaultHabit implements Serializable {
    public int id;
    public String habitName;
    public int score;
    public int type;

    public SystemDefaultHabit(int id, String habitName,int score,int type) {
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
