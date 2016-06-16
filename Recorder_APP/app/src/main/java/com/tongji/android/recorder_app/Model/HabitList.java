package com.tongji.android.recorder_app.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 重书 on 2016/6/3.
 */
public class HabitList {
    public static  List<Habit> ITEMS = new ArrayList<Habit>();

    public static  Map<Integer, Habit> ITEM_MAP = new HashMap<Integer, Habit>();

    public static void addItem(Habit item) {
        ITEMS.add(item);
        //ITEM_MAP.put(item.id, item);
    }

}
