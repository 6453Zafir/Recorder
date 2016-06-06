package com.tongji.android.recorder_app.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 重书 on 2016/6/6.
 */
public class DateList {
    public static List<DateItem> ITEMS = new ArrayList<DateItem>();

    public static Map<Integer, DateItem> ITEM_MAP = new HashMap<Integer, DateItem>();

    public static void addItem(int type,DateItem item) {
        if(type == 0){

            ITEMS.add(item);
            ITEM_MAP.put(item.type, item);
        }

    }

    public static class DateItem  {
        public int type;
        public int id;
        public Date date;



        public DateItem(int type,int id, Date date) {
            this.id = id;

            this.type=type;
            this.date = date;
        }


    }
}
