package com.tongji.android.recorder_app;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
    TabHost tabhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabhost = getTabHost();

        TabHost.TabSpec ts1 =  tabhost.newTabSpec("PunchPage");
        ts1.setIndicator("Punch");
        ts1.setContent(new Intent(this,Punchcard.class));
        tabhost.addTab(ts1);
        tabhost.setCurrentTab(1);

        TabHost.TabSpec ts2 =  tabhost.newTabSpec("FriendPage");
        ts2.setIndicator("Friend");
        ts2.setContent(new Intent(this,Friendlist.class));
        tabhost.addTab(ts2);

        TabHost.TabSpec ts3 =  tabhost.newTabSpec("RankPage");
        ts3.setIndicator("Rank");
        ts3.setContent(new Intent(this,Rankinglist.class));
        tabhost.addTab(ts3);

        TabHost.TabSpec ts4 =  tabhost.newTabSpec("RecordPage");
        ts4.setIndicator("Record");
        ts4.setContent(new Intent(this,Record.class));
        tabhost.addTab(ts4);
    }
}
