package com.tongji.android.recorder_app.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 重书 on 2016/6/5.
 */
public class degreeHabitList {

    public static List<degreeHabit> ITEMS = new ArrayList<degreeHabit>();

    public static Map<String, degreeHabit> ITEM_MAP = new HashMap<String, degreeHabit>();

    public static void addItem(degreeHabit item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class degreeHabit  {
        public  String id;
        public  String habitName;
        public int score;
        public int degree;
        public int type;

        public degreeHabit(String id, String habitName,int score,int degree,int type) {
            this.id = id;
            this.habitName = habitName;
            this.score = score;
            this.degree=degree;
            this.type=type;
        }

        @Override
        public String toString() {
            return habitName;
        }
    }

}
