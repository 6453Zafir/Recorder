package com.tongji.android.recorder_app.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/6/13.
 */
public class ContactsList {
    public static List<Contacts> ITEMS = new ArrayList<Contacts>();

    public static Map<String, Contacts> ITEM_MAP = new HashMap<String, Contacts>();

    public static void addItem(Contacts item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.phoneNumber, item);
    }
}
