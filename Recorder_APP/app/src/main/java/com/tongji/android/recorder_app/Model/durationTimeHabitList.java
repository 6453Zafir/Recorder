package com.tongji.android.recorder_app.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 重书 on 2016/6/5.
 */
public class durationTimeHabitList {
    public static List<durationTimeHabit> ITEMS = new ArrayList<durationTimeHabit>();

    public static Map<String, durationTimeHabit> ITEM_MAP = new HashMap<String, durationTimeHabit>();

    public static void addItem(durationTimeHabit item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class durationTimeHabit  {
        public  String id;
        public  String habitName;
        public int score;
        public int durationHours;
        public int durantionMinutes;
        public int type;

        public durationTimeHabit(String id, String habitName,int score,int durationHours,int durantionMinutes,int type) {
            this.id = id;
            this.habitName = habitName;
            this.score = score;
            this.durantionMinutes= durantionMinutes;
            this.durationHours = durationHours;
            this.type=type;
        }

        @Override
        public String toString() {
            return habitName;
        }
    }
}
