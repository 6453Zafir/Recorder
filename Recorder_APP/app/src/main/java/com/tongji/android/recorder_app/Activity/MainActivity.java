package com.tongji.android.recorder_app.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
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


import com.tongji.android.recorder_app.Fragment.FriendListFragment;
import com.tongji.android.recorder_app.Fragment.Punchcard;
import com.tongji.android.recorder_app.Fragment.RankingListFragment;
import com.tongji.android.recorder_app.Fragment.Record;
import com.tongji.android.recorder_app.R;
import com.tongji.android.recorder_app.tabs.SlidingTabLayout;
import com.umeng.analytics.MobclickAgent;
//import com.tongji.android.recorder_app.tabs.SlidingTabLayout;

public class MainActivity extends ActionBarActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private ImageView imageView;
    private LinearLayout linearLayout;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById(R.id.avatar);
        linearLayout = (LinearLayout) findViewById(R.id.header);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initTabs();

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
        } else {
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
        else if (id == R.id.Login) {
            Intent it = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
