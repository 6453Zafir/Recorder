package com.tongji.android.recorder_app.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

/**
 * Created by lishigang on 16/6/7.
 */
public class SystemHabitList {
    public static boolean flag = false;

    public static List<SystemDefaultHabit> ITEMS = new ArrayList<SystemDefaultHabit>();

    public static Map<String, SystemDefaultHabit> ITEM_MAP = new HashMap<String, SystemDefaultHabit>();

    public static void addItem(SystemDefaultHabit item) {
        if (ITEMS.contains(item) || HabitList.ITEMS.contains(new Habit(item.id,item.habitName,item.score,item.type,item.feature))) {
            ;
        }
        else {
            ITEMS.add(item);
            ITEM_MAP.put(item.habitName, item);
        }

    }

    public static void removeItem(SystemDefaultHabit item) {
        ITEMS.remove(item);
        ITEM_MAP.remove(item.habitName);
    }

    public static void initList() {
        addItem(new SystemDefaultHabit(0+"","Early Sleep",0,0,"22:00"));
        addItem(new SystemDefaultHabit(0+"","Early Get Up",0,0,"7:00"));
        addItem(new SystemDefaultHabit(0+"","Build Up Body",0,2,""));
        addItem(new SystemDefaultHabit(0+"","Study",0,3,"180 minutes"));
        addItem(new SystemDefaultHabit(0+"","Fitting",0,3,"120 minutes"));
        addItem(new SystemDefaultHabit(0+"","No Drunk",0,2,""));
        addItem(new SystemDefaultHabit(0+"","No Game",0,2,""));
        addItem(new SystemDefaultHabit(0+"","Breakfast",0,0,"8:00"));
        addItem(new SystemDefaultHabit(0+"","Drink",0,1,"8 times"));
        addItem(new SystemDefaultHabit(0+"","Recite Word",0,2,""));
        flag = true;
    }
}
