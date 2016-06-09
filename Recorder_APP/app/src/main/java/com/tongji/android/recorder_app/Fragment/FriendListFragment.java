package com.tongji.android.recorder_app.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tongji.android.recorder_app.Activity.ItemDetailActivity;
import com.tongji.android.recorder_app.Activity.ItemListActivity;

import com.tongji.android.recorder_app.Model.Friend;
import com.tongji.android.recorder_app.Model.FriendList;
import com.tongji.android.recorder_app.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button SerachFriendBtn;
    private EditText FriendPhoneNum;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
//    private Context mContext;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isFriendExist=false;
    private String phoneNum;

    public FriendListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendListFragment newInstance(String param1, String param2) {
        FriendListFragment fragment = new FriendListFragment();
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
        View v =  inflater.inflate(R.layout.friend_list_fragment, container, false);
        FriendPhoneNum=(EditText)v.findViewById(R.id.friend_number);
        SerachFriendBtn = (Button)v.findViewById(R.id.friend_search);
        if(phoneNum!=null){
            // call the ifFriendExist function
            if(true){

            }
        }
        SerachFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNum = FriendPhoneNum.getText().toString();
                if(!phoneNum.isEmpty()){
                    // call the ifFriendExist function
                    if (isFriendExist){
                        builder = new AlertDialog.Builder(getActivity());
                        alert = builder.setIcon(R.drawable.success)
                                .setTitle("Adding Friends")
                                .setMessage("Are you sure you want to add "+phoneNum)
                                .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create();
                        alert.show();
                    }else {
                        builder = new AlertDialog.Builder(getActivity());
                        alert = builder.setIcon(R.drawable.error)
                                .setTitle("Sorry：")
                                .setMessage("he phone number "+phoneNum+" haven't register")

                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create();
                        alert.show();
                    }
                }else{
                    builder = new AlertDialog.Builder(getActivity());
                    alert = builder.setIcon(R.drawable.error)
                            .setTitle("Sorry：")
                            .setMessage("You must input a phone number to search")

                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create();
                    alert.show();
                }
            }
        });



//        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        View recyclerView = v.findViewById(R.id.item_friend_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        return v;
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(FriendList.ITEMS));
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
                    .inflate(R.layout.item_friend_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).phoneNumber);
            holder.mContentView.setText(mValues.get(position).username);
//


        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public final TextView mScoreView;
            public Friend mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
                mScoreView = (TextView)view.findViewById(R.id.score);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }

    }


}
