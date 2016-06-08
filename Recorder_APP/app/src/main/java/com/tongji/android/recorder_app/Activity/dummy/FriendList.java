package com.tongji.android.recorder_app.Activity.dummy;

import com.tongji.android.recorder_app.Model.Friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 重书 on 2016/6/5.
 */
public class FriendList {
    public static List<Friend> ITEMS = new ArrayList<Friend>();

    public static Map<String, Friend> ITEM_MAP = new HashMap<String, Friend>();

    public static void addItem(Friend item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.phoneNumber, item);
    }
}
