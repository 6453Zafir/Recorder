package com.tongji.android.recorder_app.Model;

import com.tongji.android.recorder_app.Activity.dummy.Friend;

import java.util.Comparator;

/**
 * Created by e on 16/6/7.
 */
public class FriendSort implements Comparator<Friend>{
    @Override
    public int compare(Friend lhs, Friend rhs) {
        if(lhs.score>=rhs.score) return -1;
        else if(lhs.score<rhs.score) return 1;
        else return 0;
    }
}
