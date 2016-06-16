package com.tongji.android.recorder_app.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 重书 on 2016/6/6.
 */
public class DateItem  {
    public int type;
    public String id;
    private List<Date> date;



    public DateItem(int type,String id) {
        this.id = id;

        this.type=type;
        date = new ArrayList<Date>();
       // addDate(new Date());
    }

    public void addDate(Date date){
        this.date.add(date);
    }
    public int getDateSize(){
        return date.size();
    }
    public Date getDate(int i){
        return date.get(i);
    }


}
