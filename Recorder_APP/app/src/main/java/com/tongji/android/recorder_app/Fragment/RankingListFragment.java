package com.tongji.android.recorder_app.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tongji.android.recorder_app.Activity.MainActivity;
import com.tongji.android.recorder_app.Activity.dummy.FriendList;
import com.tongji.android.recorder_app.Model.Friend;
import com.tongji.android.recorder_app.Model.FriendSort;
import com.tongji.android.recorder_app.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String RELOAD_RANKING = "reload_ranking_friendlist";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean mTwoPane;
    private SimpleItemRecyclerViewAdapter adapter;
    private TextView firstName;
    private TextView firstScore;
    private TextView secondName;
    private TextView secondScore;
    private TextView thirdName;
    private TextView thirdScore;


    public RankingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankingListFragment newInstance(String param1, String param2) {
        RankingListFragment fragment = new RankingListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.ranking_list_fragment, container, false);
        View recyclerView = v.findViewById(R.id.ranking_list);


        firstName = (TextView) v.findViewById(R.id.FirstName);
        firstScore = (TextView) v.findViewById(R.id.firstScore);
        secondName = (TextView) v.findViewById(R.id.secondName);
        secondScore = (TextView) v.findViewById(R.id.secondScore);
        thirdName = (TextView) v.findViewById(R.id.thirdName);
        thirdScore = (TextView) v.findViewById(R.id.thirdScore);
        List<Friend> friends = new ArrayList<Friend>();

        BuildUpRanking();

        assert recyclerView != null;
        adapter = new SimpleItemRecyclerViewAdapter(FriendList.ITEMS);
        setupRecyclerView((RecyclerView) recyclerView);
        if (v.findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        IntentFilter filter = new IntentFilter(RELOAD_RANKING);
        getActivity().registerReceiver(broadcastReceiver, filter);
        IntentFilter filter1 = new IntentFilter(MainActivity.RELOAD_DATA_FRAGMENT);
        getActivity().registerReceiver(broadcastReceiver1, filter1);
        return v;
    }

    private void BuildUpRanking() {
        Collections.sort(com.tongji.android.recorder_app.Model.FriendList.ITEMS, new FriendSort());
        FriendList.ITEMS.clear();
        for (int i = 0; i < com.tongji.android.recorder_app.Model.FriendList.ITEMS.size(); i++) {
            if (i == 0) {
                firstName.setText(com.tongji.android.recorder_app.Model.FriendList.ITEMS.get(0).username);
                firstScore.setText(com.tongji.android.recorder_app.Model.FriendList.ITEMS.get(0).score + "");
            } else if (i == 1) {
                secondName.setText(com.tongji.android.recorder_app.Model.FriendList.ITEMS.get(1).username);
                secondScore.setText(com.tongji.android.recorder_app.Model.FriendList.ITEMS.get(1).score + "");
            } else if (i == 2) {
                thirdName.setText(com.tongji.android.recorder_app.Model.FriendList.ITEMS.get(2).username);
                thirdScore.setText(com.tongji.android.recorder_app.Model.FriendList.ITEMS.get(2).score + "");
            } else {

                FriendList.addItem(com.tongji.android.recorder_app.Model.FriendList.ITEMS.get(i));
            }
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            adapter.notifyDataSetChanged();
            BuildUpRanking();
        }
    };
    BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            adapter.notifyDataSetChanged();
            //BuildUpRanking();
        }
    };

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(adapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Friend> mValues;

        public SimpleItemRecyclerViewAdapter(List<Friend> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ranking_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).phoneNumber);
            holder.mContentView.setText(mValues.get(position).username);
            holder.mScore.setText(mValues.get(position).score+"");

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public final TextView mScore;
            public Friend mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
                mScore = (TextView) view.findViewById(R.id.score);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        getActivity().unregisterReceiver(broadcastReceiver);
    }

}