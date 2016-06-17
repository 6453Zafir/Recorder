package com.tongji.android.recorder_app.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/6/17.
 */
public class FriendHabitList {
    public static List<FriendHabit> ITEMS = new ArrayList<FriendHabit>();


    public static void addItem(FriendHabit item) {
        ITEMS.add(item);
        //ITEM_MAP.put(item.id, item);
    }
}
