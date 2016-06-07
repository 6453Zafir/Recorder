package com.tongji.android.recorder_app.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 重书 on 2016/6/5.
 */
public class doItHabitList {
    public static List<doItHabit> ITEMS = new ArrayList<doItHabit>();

    public static Map<String, doItHabit> ITEM_MAP = new HashMap<String, doItHabit>();

    public static void addItem(doItHabit item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class doItHabit  {
        public  String id;
        public  String habitName;
        public int score;

        public int type;

        public doItHabit(String id, String habitName,int score,int type) {
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
}
