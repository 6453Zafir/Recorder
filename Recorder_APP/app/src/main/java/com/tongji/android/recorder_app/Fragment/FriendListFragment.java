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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tongji.android.recorder_app.Activity.ItemDetailActivity;
import com.tongji.android.recorder_app.Activity.ItemListActivity;

import com.tongji.android.recorder_app.Activity.MainActivity;
import com.tongji.android.recorder_app.Application.MyApplication;
import com.tongji.android.recorder_app.Model.Friend;
import com.tongji.android.recorder_app.Model.FriendList;
import com.tongji.android.recorder_app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

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
    private String friendphoneNum;
    private SimpleItemRecyclerViewAdapter adapter;

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
        if(friendphoneNum!=null){
            // call the ifFriendExist function
            if(true){

            }
        }
        SerachFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendphoneNum = FriendPhoneNum.getText().toString();
                if(!friendphoneNum.isEmpty()){
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.add("phoneNumber",friendphoneNum);
                    client.post("http://lshunran.com:3000/recorder/search", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String re = new String(responseBody);
                            try{
                                JSONObject object = new JSONObject(re);
                                int errCode =object.getInt("errCode");
                                int isExist = object.getInt("isExist");
                                if(errCode == 0 && isExist == 1 ){
                                    builder = new AlertDialog.Builder(getActivity());
                                    alert = builder.setIcon(R.drawable.success)
                                            .setTitle("Adding Friends")
                                            .setMessage("Are you sure you want to add "+friendphoneNum)
                                            .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    AsyncHttpClient client = new AsyncHttpClient();
                                                    RequestParams params = new RequestParams();
                                                    MyApplication myapp = (MyApplication)getActivity().getApplication();
                                                    params.add("phoneNumber",myapp.getPhoneNumber());
                                                    params.add("addPhoneNumber",friendphoneNum);
                                                    client.post("http://lshunran.com:3000/recorder/add", params, new AsyncHttpResponseHandler() {
                                                        @Override
                                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                            String re = new String(responseBody);
                                                            try{
                                                                JSONObject object = new JSONObject(re);
                                                                int errCode =object.getInt("errCode");
                                                                if(errCode == 0 ){
                                                                    int score = object.getInt("score");
                                                                    String username = object.getString("username");
                                                                    Friend mynewfriend = new Friend(friendphoneNum,username,score);
                                                                    FriendList.addItem(mynewfriend);
                                                                    NoSQLEntity<Friend>entity=new NoSQLEntity<Friend>("friend",friendphoneNum+"");
                                                                    entity.setData(mynewfriend);
                                                                    NoSQL.with(getActivity()).using(Friend.class).save(entity);
//                                                                    NoSQL.with(getActivity()).using(Friend.class)
//                                                                            .bucketId("friend")
//                                                                            .retrieve(new RetrievalCallback<Friend>() {
//                                                                                @Override
//                                                                                public void retrievedResults(List<NoSQLEntity<Friend>> noSQLEntities) {
//                                                                                    for(int i=0;i<noSQLEntities.size();i++){
//                                                                                        Friend addfriend = noSQLEntities.get(i).getData();
//                                                                                        FriendList.addItem(addfriend);
//                                                                                    }
//                                                                                }
//                                                                            });
                                                                    builder = new AlertDialog.Builder(getActivity());
                                                                    alert = builder.setIcon(R.drawable.success)
                                                                            .setTitle("Congratulations：")
                                                                            .setMessage("You have added the "+friendphoneNum+" as your friend!Now you can check his/her habit list")
                                                                            .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                }
                                                                            })
                                                                            .setPositiveButton("Check Now", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                        adapter.notifyDataSetChanged();
                                                                                    Intent intent = new Intent(RankingListFragment.RELOAD_RANKING);

                                                                                    getActivity().sendBroadcast(intent);

                                                                                }
                                                                            }).create();
                                                                    alert.show();
                                                                }else if(errCode== 1){
                                                                    builder = new AlertDialog.Builder(getActivity());
                                                                    alert = builder.setIcon(R.drawable.error)
                                                                            .setTitle("Sorry：")
                                                                            .setMessage("You have added the "+friendphoneNum+" as friend")

                                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                }
                                                                            }).create();
                                                                    alert.show();
                                                                }
                                                            }catch (JSONException e){
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                                        }
                                                    });
                                                }
                                            }).create();
                                    alert.show();
                                }else if(errCode==0 && isExist == 0){
                                    builder = new AlertDialog.Builder(getActivity());
                                    alert = builder.setIcon(R.drawable.error)
                                            .setTitle("Sorry：")
                                            .setMessage("The phone number "+friendphoneNum+" haven't register")

                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).create();
                                    alert.show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });

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

        View recyclerView = v.findViewById(R.id.item_friend_list);
        assert recyclerView != null;
        adapter=new SimpleItemRecyclerViewAdapter(FriendList.ITEMS);
        setupRecyclerView((RecyclerView) recyclerView);
        return v;
    }
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
                    .inflate(R.layout.item_friend_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).phoneNumber);
            holder.mContentView.setText(mValues.get(position).username);
            holder.mScoreView.setText(mValues.get(position).score+"");
//


        }

        @Override
        public int getItemCount() {
            return FriendList.ITEMS.size();
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
