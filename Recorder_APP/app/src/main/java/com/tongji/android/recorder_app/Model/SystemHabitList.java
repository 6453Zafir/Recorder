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
        addItem(new SystemDefaultHabit(0+"","Early Bed",0,3,"feature"));
        addItem(new SystemDefaultHabit(0+"","Early Up",0,3,"feature"));
        addItem(new SystemDefaultHabit(0+"","Build Up Body",0,4,"feature"));
        addItem(new SystemDefaultHabit(0+"","Study",0,4,"feature"));
        addItem(new SystemDefaultHabit(0+"","Smoke",0,3,"feature"));
        addItem(new SystemDefaultHabit(0+"","Drunk",0,3,"feature"));
        addItem(new SystemDefaultHabit(0+"","Play Game",0,3,"feature"));
        addItem(new SystemDefaultHabit(0+"","Breakfast",0,1,"feature"));
        addItem(new SystemDefaultHabit(0+"","Drink",0,2,"feature"));
        addItem(new SystemDefaultHabit(0+"","Eat Word",0,2,"feature"));
        flag = true;
    }
}
