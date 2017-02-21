package com.tongji.android.recorder_app.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;

import com.tongji.android.recorder_app.Application.MyApplication;
import com.tongji.android.recorder_app.Application.SharedPreferrenceHelper;
import com.tongji.android.recorder_app.Fragment.FriendListFragment;
import com.tongji.android.recorder_app.Fragment.Punchcard;
import com.tongji.android.recorder_app.Fragment.RankingListFragment;
import com.tongji.android.recorder_app.Fragment.Record;
import com.tongji.android.recorder_app.Model.DateItem;
import com.tongji.android.recorder_app.Model.DateList;
import com.tongji.android.recorder_app.Model.Friend;
import com.tongji.android.recorder_app.Model.FriendList;
import com.tongji.android.recorder_app.Model.Habit;
import com.tongji.android.recorder_app.Model.HabitList;
import com.tongji.android.recorder_app.Model.MessageEvent;
import com.tongji.android.recorder_app.Model.SystemHabitList;
import com.tongji.android.recorder_app.R;
import com.tongji.android.recorder_app.tabs.SlidingTabLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.Objects;
//import com.tongji.android.recorder_app.tabs.SlidingTabLayout;

public class MainActivity extends ActionBarActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String RELOAD_DATA_FRAGMENT = "jason.broadcast.action";
    public static final String ALERT = "alert";
    private boolean alert_exit= true;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private Button btn;
    private NavigationView navigationView;
    private SharedPreferrenceHelper sh;
    private TextView nickname;
    private MyApplication myApp;
    private Habit firstBean;
    private Calendar c;

    public static final int PREPARE_DATE_AFTER_LOGIN = 1;

    public void setOffline(){
        myApp.setStatus(MyApplication.OFFLINE);
        //sh.remove();
        nickname.setText("请登录");
        btn.setText("Log in");
    }
    @Override
    protected void onStart() {
        super.onStart();

        Map<String,String> data = sh.read();
        if(Objects.equals(data.get("token"),"1")){
            Log.i("myLog",data.get("nickname"));
            myApp.setStatus(MyApplication.ONLINE);
            myApp.setPhoneNumber(data.get("username"));
            nickname.setText(data.get("nickname"));
            btn.setText("Log out");
        }else {
            setOffline();
        }


    }




    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c =  Calendar.getInstance(Locale.getDefault());
        loadRanking();
        myApp = (MyApplication) getApplication();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sh = new SharedPreferrenceHelper(this);
        Map<String,String> data = sh.read();
        if(Objects.equals(data.get("token"),"1")){
            loadFromDb();
        }

        nickname = (TextView)v.findViewById(R.id.textView);
        btn = (Button) v.findViewById(R.id.LogInOutBtn);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initTabs();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myApp.getStatus() == MyApplication.OFFLINE){
                    Intent it = new Intent(MainActivity.this,LoginActivity.class);
                    startActivityForResult(it,PREPARE_DATE_AFTER_LOGIN);
                }else if(myApp.getStatus() == MyApplication.ONLINE){
                    sh.remove();
                    NoSQL.with(MainActivity.this).using(Habit.class)
                            .bucketId("habit")
                            .delete();
                    NoSQL.with(MainActivity.this).using(DateItem.class)
                            .bucketId("date")
                            .delete();
                    NoSQL.with(MainActivity.this).using(Friend.class)
                            .bucketId("friend")
                            .delete();
                    setOffline();
                    resetAllCache();
                }

            }
        });
        
       // initDB();

    }

    private void resetAllCache(){
        HabitList.ITEMS.clear();
        HabitList.ITEM_MAP.clear();
        DateList.ITEMS.clear();
        DateList.ITEM_MAP.clear();
        FriendList.ITEMS.clear();

        FriendList.ITEM_MAP.clear();

        Intent intent = new Intent(MainActivity.RELOAD_DATA_FRAGMENT);

        sendBroadcast(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == PREPARE_DATE_AFTER_LOGIN || requestCode==PREPARE_DATE_AFTER_LOGIN){
            Map<String,String> d = sh.read();
            if(Objects.equals(d.get("token"),"1")) {
                //Toast.makeText(this,"正在更新习惯",Toast.LENGTH_SHORT).show();
                getDataFromServer();
                initDB(0);


            }

        }

    }

    private void getDataFromServer() {
    }

    //模拟习惯数据
    private void initDB(int status) {
//
//        Calendar c =  Calendar.getInstance(Locale.getDefault());
//        Habit h1= new Habit(1+"","Reading",0,Habit.TYPE_DURATION,"180 minutes");
//        DateItem dateitem = new DateItem(h1.type,h1.id);
//        c.set(2016,6,4);
//        dateitem.addDate(c.getTime());
//        c.set(2016,6,3);
//        dateitem.addDate(c.getTime());
//
//        DateList.addItem(1, dateitem);
//        Habit h2= new Habit(2+"","lunch",0,Habit.TYPE_DOORNOT,"");
//        DateItem dateitem2 = new DateItem(h2.type,h2.id);
//        c.set(2016,6,2);
//        dateitem2.addDate(c.getTime());
//
//        c.set(2016,6,1);
//        dateitem2.addDate(c.getTime());
//        DateList.addItem(2, dateitem2);
//        Habit h3= new Habit(3+"","Drink",0,3,"8 times");
//        DateItem dateitem3 = new DateItem(h3.type,h3.id);
//        c.set(2016,6,6);
//        dateitem3.addDate(c.getTime());
//
//        c.set(2016,6,7);
//        dateitem3.addDate(c.getTime());
//        DateList.addItem(3, dateitem3);
//        Habit[] h = new Habit[]{h1,h2,h3};
//        DateItem[] d =new DateItem[]{dateitem,dateitem2,dateitem3};
//        //Toast.makeText(this, DateList.ITEMS.get(0).getDate(0).getYear()+"",Toast.LENGTH_SHORT).show();
//        Friend f = new Friend("13333333","zhangjunyi",82);
//
//        HabitList.addItem(h1);
//        HabitList.addItem(h2);
//        HabitList.addItem(h3);
//        for(int i =0;i<h.length;i++){
//            NoSQLEntity<Habit> entity = new NoSQLEntity<Habit>("habit",h[i].id+"");
//            entity.setData(h[i]);
//            NoSQL.with(this).using(Habit.class).save(entity);
//        }
//
//        for(int i =0;i<d.length;i++){
//            NoSQLEntity<DateItem> entity = new NoSQLEntity<DateItem>("date",d[i].type+"+"+d[i].id);
//            entity.setData(d[i]);
//            NoSQL.with(this).using(DateItem.class).save(entity);
//        }
        Intent intent = new Intent(RELOAD_DATA_FRAGMENT);

        sendBroadcast(intent);



        //Toast.makeText(this,firstBean.habitName,Toast.LENGTH_SHORT).show();

    }
    private void loadFromDb(){
        HabitList.ITEMS.clear();
        HabitList.ITEM_MAP.clear();
        DateList.ITEMS.clear();
        DateList.ITEM_MAP.clear();
        FriendList.ITEMS.clear();

        FriendList.ITEM_MAP.clear();
//        for(int i=0;i<10;i++){
//            Friend f = new Friend("15316373836"+i,"张君义"+i,(int)(Math.random()*100)+1);
//            FriendList.addItem(f);
//        }
        NoSQL.with(this).using(DateItem.class)
                .bucketId("date")
                .retrieve(new RetrievalCallback<DateItem>() {
                    @Override
                    public void retrievedResults(List<NoSQLEntity<DateItem>> noSQLEntities) {
                        for(int i = 0;i<noSQLEntities.size();i++){
                            DateItem currentBean = noSQLEntities.get(i).getData(); // always check length of a list first...
//                                for(int j=0;j<HabitList.ITEMS.size();j++){
//                                    if(HabitList.ITEMS.get(j).id.equals(currentBean.id)){

                            DateList.addItem(currentBean.type,currentBean);
//                                    }
//                                }
                        }

                    }


                });

        NoSQL.with(this).using(Habit.class)
                .bucketId("habit")
                .retrieve(new RetrievalCallback<Habit>() {
                    @Override
                    public void retrievedResults(List<NoSQLEntity<Habit>> noSQLEntities) {
                        for(int i = 0;i<noSQLEntities.size();i++){
                            Habit currentBean = noSQLEntities.get(i).getData(); // always check length of a list first...
//                                for(int j=0;j<HabitList.ITEMS.size();j++){
//                                    if(HabitList.ITEMS.get(j).id.equals(currentBean.id)){

                            DateItem d =DateList.ITEM_MAP.get(currentBean.type+"+"+currentBean.id);

                            int now = c.getTime().getDate();
                   //         Toast.makeText(MainActivity.this,now+"",Toast.LENGTH_SHORT).show();
                            if(d!=null){

                                for(int j=0;j<d.getDateSize();j++){

                           //         Toast.makeText(MainActivity.this,now+" "+d.getDate(j).getDate(),Toast.LENGTH_SHORT).show();
                                    if(now == d.getDate(j).getDate()){    //待完善
                                        currentBean.isChecked=true;
                                        break;
                                    }
                                    else if(j == d.getDateSize()-1){
                                        currentBean.isChecked=false;
                                    }
                                }
                            }else {
                          //      Toast.makeText(MainActivity.this,"load error",Toast.LENGTH_SHORT).show();
                            }

                            HabitList.addItem(currentBean);
//                                    }
//                                }
                        }
                        //adapter.notifyDataSetChanged();
                    }


                });
        NoSQL.with(this).using(Friend.class)
                .bucketId("friend")
                .retrieve(new RetrievalCallback<Friend>() {
                    @Override
                    public void retrievedResults(List<NoSQLEntity<Friend>> noSQLEntities) {
                        for(int i = 0;i<noSQLEntities.size();i++){
                            Friend currentBean = noSQLEntities.get(i).getData(); // always check length of a list first...
//                                for(int j=0;j<HabitList.ITEMS.size();j++){
//                                    if(HabitList.ITEMS.get(j).id.equals(currentBean.id)){
                            FriendList.addItem(currentBean);
//                                    }
//                                }
                        }
                        //adapter.notifyDataSetChanged();
                    }


                });
        Intent intent = new Intent(RELOAD_DATA_FRAGMENT);

        sendBroadcast(intent);
    }

    private void loadRanking() {



    }

    public void initTabs(){
        mPager= (ViewPager) findViewById(R.id.pager);
        mTabs= (SlidingTabLayout) findViewById(R.id.tabs);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs.setDistributeEvenly(true);

        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);

        mTabs.setViewPager(mPager);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(alert_exit == true) {
            Toast.makeText(this,"再按一次退出应用",Toast.LENGTH_SHORT).show();
            alert_exit = false;
        }else {
            super.onBackPressed();
        }
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        int icons[]={R.drawable.ic_check_circle_white_24dp,R.drawable.record,R.drawable.rank,R.drawable.ic_people_white_24dp};
        String[] tabs;
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs=getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment myFragment=null;
            switch (position){
                case 0:
                    myFragment= Punchcard.newInstance("", "");
                    break;
                case 1:
                    myFragment= Record.newInstance("", "");
                    break;
                case 2:
                    myFragment= RankingListFragment.newInstance("", "");
                    break;
                case 3:
                    myFragment= FriendListFragment.newInstance("", "");
                    break;
            }
            return myFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {      //set image and text of the tag
            Drawable drawable=getResources().getDrawable(icons[position]);
            drawable.setBounds(0,0,50,50);
            ImageSpan imageSpan=new ImageSpan(drawable);
            SpannableString spannableString=new SpannableString("fdsfsd");
            spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
            //return getResources().getStringArray(R.array.tabs)[position];

        }

        public Drawable getIcons(int position) {
            return getResources().getDrawable(icons[position]);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent it = new Intent(this,SettingsActivity.class);
            startActivity(it);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_punch) {
            mPager.setCurrentItem(0);
            // Handle the camera action
        } else if (id == R.id.nav_list) {
            mPager.setCurrentItem(1);
        } else if (id == R.id.nav_rank) {
            mPager.setCurrentItem(2);
        } else if (id == R.id.nav_friend) {
            mPager.setCurrentItem(3);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_set) {

        }
//        else if (id == R.id.Login) {
//
//            Intent it = new Intent(MainActivity.this,LoginActivity.class);
//            startActivity(it);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FriendList.ITEMS.clear();
    }

}
