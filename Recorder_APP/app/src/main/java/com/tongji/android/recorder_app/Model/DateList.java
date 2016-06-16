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

    public static Map<String, DateItem> ITEM_MAP = new HashMap<String, DateItem>();

    public static void addItem(int type,DateItem item) {
//        if(type == 0){

            ITEMS.add(item);
            ITEM_MAP.put(item.type+"+"+item.id, item);
//        }else if(type==1){
//            ITEMS.add(item);
//            ITEM_MAP.put(item.type, item);
//        }
//        else if(type==2){
//            ITEMS.add(item);
//            ITEM_MAP.put(item.type, item);
//        }
//        else if(type==3){
//            ITEMS.add(item);
//            ITEM_MAP.put(item.type, item);
//        }else {
//
//        }

    }


}
