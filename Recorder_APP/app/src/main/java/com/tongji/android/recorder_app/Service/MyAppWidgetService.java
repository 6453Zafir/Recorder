package com.tongji.android.recorder_app.Service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.tongji.android.recorder_app.Activity.MainActivity;
import com.tongji.android.recorder_app.Model.HabitList;
import com.tongji.android.recorder_app.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAppWidgetService  extends RemoteViewsService {

    private static final String TAG = "SKYWANG";
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new GridRemoteViewsFactory(this, intent);
    }

    private class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private int mAppWidgetId;

        private String IMAGE_ITEM = "imgage_item";
        private String TEXT_ITEM = "text_item";
        private ArrayList<HashMap<String, Object>> data ;

        private String[] arrText = new String[]{
                "Picture 1", "Picture 2", "Picture 3",
                "Picture 4", "Picture 5", "Picture 6",
                "Picture 7", "Picture 8", "Picture 9"
        };


        /**
         * 构造GridRemoteViewsFactory
         * @author skywang
         */
        public GridRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

        }

        @Override
        public RemoteViews getViewAt(int position) {
            HashMap<String, Object> map;

            // 获取 grid_view_item.xml 对应的RemoteViews
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_layout);
            Intent intent = new Intent(mContext,MainActivity.class);

            //rv.setInt(R.id.itemLayout, "setBackgroundResource", R.color.transparent);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
            rv.setOnClickPendingIntent(R.id.itemLayout,pendingIntent);

            rv.setTextViewText(R.id.punch_card_main_habit_textView, HabitList.ITEMS.get(position).habitName);

            // 设置 第position位的“视图”对应的响应事件
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(WidgetProvider.COLLECTION_VIEW_EXTRA, position);
            rv.setOnClickFillInIntent(R.id.itemLayout, fillInIntent);

            return rv;
        }

        /**
         * 初始化GridView的数据
         * @author skywang
         */
        private void initGridViewData() {
            data = new ArrayList<HashMap<String, Object>>();

            for (int i=0; i<9; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
             //   map.put(IMAGE_ITEM, arrImages[i]);
                map.put(TEXT_ITEM, arrText[i]);
                data.add(map);
            }
        }

        @Override
        public void onCreate() {
         //   Log.d(TAG, "onCreate");
            // 初始化“集合视图”中的数据
            initGridViewData();
        }

        @Override
        public int getCount() {
            // 返回“集合视图”中的数据的总数
            return HabitList.ITEMS.size();
        }

        @Override
        public long getItemId(int position) {
            // 返回当前项在“集合视图”中的位置
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            // 只有一类 GridView
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
            data.clear();
        }
    }
}