package com.tongji.android.recorder_app.Service;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.IndeterminateProgressButton;
import com.tongji.android.recorder_app.Activity.MainActivity;
import com.tongji.android.recorder_app.Application.MyApplication;
import com.tongji.android.recorder_app.Model.DateItem;
import com.tongji.android.recorder_app.Model.DateList;
import com.tongji.android.recorder_app.Model.Habit;
import com.tongji.android.recorder_app.Model.HabitList;
import com.tongji.android.recorder_app.R;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;

/**
 * Created by 重书 on 2016/6/14.
 */
public class WidgetProvider extends AppWidgetProvider {
    public static final String BT_REFRESH_ACTION = "recorder.BT_REFRESH_ACTION";
    public static final String COLLECTION_VIEW_ACTION = "recorder.COLLECTION_VIEW_ACTION";
    public static final String COLLECTION_VIEW_EXTRA = "recorder.COLLECTION_VIEW_EXTRA";
    public static final String ACTION_UPDATE_ALL="com.feature.test.update_all";

    private static final Intent AppWidget_Service=new Intent("recorder.MyAppWidgetService");
    public static final String INTEGER_DATA="integer_data";
    @Override
    public void onUpdate(Context context,AppWidgetManager appWidgetProvider,int[] appWidgetIds)
    {

        Intent intent = new Intent(context,MainActivity.class);
       // intent.setAction(broadCastString);
        //设置pendingIntent的作用
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //绑定事件

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        Intent serviceIntent = new Intent(context, MyAppWidgetService.class);
        remoteViews.setRemoteAdapter(R.id.punch_card_main_habit_gridView, serviceIntent);
        remoteViews.setOnClickPendingIntent(R.id.btn,pendingIntent);
        // 每次 widget 被创建时，对应的将widget的id添加到set中
        appWidgetProvider.updateAppWidget(appWidgetIds, remoteViews);

    }
    public class PuncuGridViewAdapter extends BaseAdapter {
        private int mMorphCounter1[] =new int[HabitList.ITEMS.size()];
        private LayoutInflater inflater;
        private Context context;

        private int type;
        private String id;
        public PuncuGridViewAdapter (Context context) {
            this.inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return HabitList.ITEMS.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//             mMorphCounter1[position] = 1;
            convertView = inflater.inflate(R.layout.item_widget_layout,null);
            Habit currentHabit = HabitList.ITEMS.get(position);
            type=currentHabit.type;
            id = currentHabit.id;
            TextView textView = (TextView) convertView.findViewById(R.id.punch_card_main_habit_textView);

            textView.setText(currentHabit.habitName);


            return convertView;
        }




    }

    //当桌面组件删除时调用
    @Override
    public void onDeleted(Context context,int[] appWidgetIds)
    {


        super.onDeleted(context, appWidgetIds);
    }

    //当桌面提供的第一个组件创建时调用
    @Override
    public void onEnabled(Context context)
    {
        //启动服务
//        context.startService(AppWidget_Service);

        super.onEnabled(context);
    }

    //当桌面提供的最后一个组件删除时调用
    @Override
    public void onDisabled(Context context)
    {
        //停止服务
        context.stopService(AppWidget_Service);

        super.onDisabled(context);
    }

    /*
     * 重写广播接收方法
     * 用于接收除系统默认的更新广播外的  自定义广播（本程序由服务发送过来的，一个是更新UI，一个是按钮事件消息）
     */
    @Override
    public void onReceive(Context context,Intent intent)
    {

        super.onReceive(context, intent);
    }

    //更新UI
    public void updateAllWidget(Context context, AppWidgetManager manager, HashSet<Integer> set)
    {

    }

    //设置按钮事件处理
    private PendingIntent getPendingIntent(Context context, int buttonid)
    {
        Intent intent=new Intent("test.test");
        intent.setClass(context, WidgetProvider.class);
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        intent.setData(Uri.parse("custom:"+buttonid));
        //进行广播
        PendingIntent pi=PendingIntent.getBroadcast(context, 0, intent, 0);
        return pi;
    }
}
