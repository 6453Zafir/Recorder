package com.tongji.android.recorder_app.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 重书 on 2016/6/5.
 */
public class timePointHabitList {
    public static List<timePointHabit> ITEMS = new ArrayList<timePointHabit>();

    public static Map<String, timePointHabit> ITEM_MAP = new HashMap<String, timePointHabit>();

    public static void addItem(timePointHabit item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class timePointHabit  {
        public  String id;
        public  String habitName;
        public int score;
        public long timePoint;

        public int type;

        public timePointHabit(String id, String habitName,int score,int timePoint,int type) {
            this.id = id;
            this.habitName = habitName;
            this.score = score;
            this.timePoint=timePoint;
            this.type=type;
        }

        @Override
        public String toString() {
            return habitName;
        }
    }
}
