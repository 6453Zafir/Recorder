package com.tongji.android.recorder_app.Fragment;



import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;

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
import com.tongji.android.recorder_app.Model.Contacts;
import com.tongji.android.recorder_app.Model.ContactsList;
import com.tongji.android.recorder_app.Model.Friend;
import com.tongji.android.recorder_app.Model.FriendHabit;
import com.tongji.android.recorder_app.Model.FriendHabitList;
import com.tongji.android.recorder_app.Model.FriendList;
import com.tongji.android.recorder_app.Model.Habit;
import com.tongji.android.recorder_app.Model.Ingredient;
import com.tongji.android.recorder_app.Model.Recipe;
import com.tongji.android.recorder_app.R;
import com.tongji.android.recorder_app.View.RecipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
    private FriendListAdapter friendListAdapter;
    private View friend_habitlist_view;
    private RecipeAdapter mAdapter;
    private Recipe contactsList;
    private RecyclerView contactsView;
    private List<Recipe> recipes;

    private MyApplication myapp;


    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

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
        myapp = (MyApplication)getActivity().getApplication();
        FriendPhoneNum=(EditText)v.findViewById(R.id.friend_number);
        SerachFriendBtn = (Button)v.findViewById(R.id.friend_search);
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
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    AsyncHttpClient client = new AsyncHttpClient();
                                                    RequestParams params = new RequestParams();
                                                    params.add("phoneNumber",myapp.getPhoneNumber());
                                                    params.add("addPhoneNumber",friendphoneNum);
                                                    client.post("http://lshunran.com:3000/recorder/add", params, new AsyncHttpResponseHandler() {
                                                        @Override
                                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                            String re = new String(responseBody);
                                                            try{
                                                                final JSONObject object = new JSONObject(re);
                                                                int errCode = object.getInt("errCode");
                                                                if(errCode == 0 ){
                                                                    String addedusername = object.getString("username");
                                                                    String addedphoneNum = object.getString("phoneNumber");
                                                                    int addedscore = object.getInt("score");
                                                                    final Friend mynewfriend = new Friend(addedphoneNum,addedusername,addedscore);
                                                                    //新建要添加好友的对象



                                                                    builder = new AlertDialog.Builder(getActivity());
                                                                    alert = builder.setIcon(R.drawable.success)
                                                                            .setTitle("Congratulations：")
                                                                            .setMessage("You have added the "+friendphoneNum+" as your friend! Now you can check his/her habit list")
                                                                            .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                }
                                                                            })
                                                                            .setPositiveButton("Check Now", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                    FriendList.addItem(mynewfriend);
                                                                                    NoSQLEntity<Friend>entity=new NoSQLEntity<Friend>("friend",friendphoneNum+"");
                                                                                    entity.setData(mynewfriend);
                                                                                    NoSQL.with(getActivity()).using(Friend.class).save(entity);
                                                                                    Intent intent = new Intent(RankingListFragment.RELOAD_RANKING);
                                                                                    getActivity().sendBroadcast(intent);
                                                                                    adapter.notifyDataSetChanged();
                                                                                }
                                                                            }).create();
                                                                    alert.show();
                                                                }else if(errCode== 1){
                                                                    builder = new AlertDialog.Builder(getActivity());
                                                                    alert = builder.setIcon(R.drawable.error)
                                                                            .setTitle("Sorry：")
                                                                            .setMessage("You have already added "+friendphoneNum)

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


        contactsView = (RecyclerView) v.findViewById(R.id.contactsView);
        asynLoadContacts asynLoad = new asynLoadContacts();
        asynLoad.execute();
//        IntentFilter filter = new IntentFilter(RankingListFragment.RELOAD_RANKING);
//        getActivity().registerReceiver(broadcastReceiver, filter);
        IntentFilter filter1 = new IntentFilter(MainActivity.RELOAD_DATA_FRAGMENT);
        getActivity().registerReceiver(broadcastReceiver1,filter1);

        return v;
    }

    //--------------------------------------------------------------------------------------------------------------------


    public void getPermissionToReadUserContacts() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
       // adapter = new SimpleItemRecyclerViewAdapter(FriendList.ITEMS);
        recyclerView.setAdapter(adapter);
    }
    private void setupFriendRecyclerView(@NonNull RecyclerView recyclerView) {
        // adapter = new SimpleItemRecyclerViewAdapter(FriendList.ITEMS);
        recyclerView.setAdapter(friendListAdapter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            adapter.notifyDataSetChanged();
            //·BuildUpRanking();
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

    private class asynLoadContacts extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            recipes = ReadAllContacts();


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mAdapter = new RecipeAdapter(getActivity(), recipes);
            mAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
                @Override
                public void onListItemExpanded(int position) {
                    Recipe expandedRecipe = recipes.get(position);

                }

                @Override
                public void onListItemCollapsed(int position) {
                    Recipe collapsedRecipe = recipes.get(position);

                }
            });

            contactsView.setAdapter(mAdapter);
            contactsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Friend> mValues;
        private LayoutInflater inflater;

        public SimpleItemRecyclerViewAdapter(List<Friend> items) {
           // this.inflater = LayoutInflater.from()
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_friend_list_content, parent, false);
            return new ViewHolder(view);
        }





        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).username);
            holder.mContentView.setText(mValues.get(position).phoneNumber);
            holder.mScoreView.setText(mValues.get(position).score+"");
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    friend_habitlist_view=inflater.inflate(R.layout.friend_habitlist,null,false);

                    View friendRecycler = friend_habitlist_view.findViewById(R.id.friend_habit);
                    assert friendRecycler != null;
                    friendListAdapter=new FriendListAdapter(FriendHabitList.ITEMS,getActivity());
                    setupFriendRecyclerView((RecyclerView) friendRecycler);
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.add("phoneNumber",mValues.get(position).phoneNumber);
                    client.post("http://lshunran.com:3000/recorder/gethabit", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String re = new String(responseBody);
                            try {
                                FriendHabitList.ITEMS.clear();
                                String username=null;
                                int score = 0;
                                JSONObject object = new JSONObject(re);

                                JSONArray habitjson = object.getJSONArray("habitList");
                                for(int i=0;i<habitjson.length();i++){
                                    JSONObject friend  = habitjson.getJSONObject(i);
                                    username = friend.getString("name");
                                    score = friend.getInt("score");
                                    //Toast.makeText(getActivity(),username+" "+ score,Toast.LENGTH_SHORT).show();
                                    FriendHabit ffff = new FriendHabit(username,score);
                                    FriendHabitList.addItem(ffff);

                                }
                                friendListAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                    //弹出好友习惯列表及相应得分

                    builder = new AlertDialog.Builder(getActivity());
                    alert = builder.setIcon(R.drawable.list)
                            .setTitle(holder.mItem.username)
                            .setMessage("habitlist")
                            .setView(friend_habitlist_view)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create();
                    alert.show();
                    }

            });


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


    public List<Recipe> ReadAllContacts() {
        Cursor cursor = this.getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;
        List<Ingredient> list = new ArrayList<Ingredient>();

        if(cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        while(cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);


            /*
             * 查找该联系人的phone信息
             */
            Cursor phones = this.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex);
                Ingredient singleFriend = new Ingredient(name, phoneNumber);
                list.add(singleFriend);
            }



            contactsList = new Recipe("Your Contacts", list);
            // mAdapter.notifyDataSetChanged();



        }

        return Arrays.asList(contactsList);
    }

    public class FriendListAdapter
            extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

        private final List<FriendHabit> mValues;
        private Context context;

        public FriendListAdapter(List<FriendHabit> items,Context context) {
            mValues = items;
            this.context=context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).score+"");
            holder.mContentView.setText(mValues.get(position).habitName);


        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public FriendHabit mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }


}
