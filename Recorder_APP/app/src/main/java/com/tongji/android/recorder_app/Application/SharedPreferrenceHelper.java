package com.tongji.android.recorder_app.Application;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 重书 on 2016/5/31.
 */
public class SharedPreferrenceHelper {
    private Context mContext;

    public SharedPreferrenceHelper() {
    }

    public SharedPreferrenceHelper(Context mContext) {
        this.mContext = mContext;
    }


    //定义一个保存数据的方法
    public void save(String username, String password , String nickname) {
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("nickname", nickname);
        editor.putInt("token",1);
        //editor.clear();
        editor.commit();

    }

    //定义一个读取SP文件的方法
    public Map<String, String> read() {
        int x = 0;
        Map<String, String> data = new HashMap<String, String>();
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        data.put("username", sp.getString("username", ""));
        data.put("password", sp.getString("password", ""));
        data.put("nickname", sp.getString("nickname", ""));
        data.put("token", String.valueOf(sp.getInt("token",x)));
        return data;
    }

    public void remove(){
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //editor.remove("username");
        editor.remove("password");
        editor.remove("nickname");
        editor.remove("token");
        //editor.clear();
        editor.commit();
    }
}
