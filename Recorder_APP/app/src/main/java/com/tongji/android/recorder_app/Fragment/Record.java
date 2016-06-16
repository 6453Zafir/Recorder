package com.tongji.android.recorder_app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.tongji.android.recorder_app.Activity.ItemDetailActivity;
import com.tongji.android.recorder_app.Activity.MainActivity;
import com.tongji.android.recorder_app.Activity.dummy.DummyContent;
import com.tongji.android.recorder_app.Model.Habit;
import com.tongji.android.recorder_app.Model.HabitList;
import com.tongji.android.recorder_app.Model.MessageEvent;
import com.tongji.android.recorder_app.R;


import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Record#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Record extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean mTwoPane;
    private SimpleItemRecyclerViewAdapter adapter;


    public Record() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getActivity().unregisterReceiver(broadcastReceiver);
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
    public static Record newInstance(String param1, String param2) {
        Record fragment = new Record();
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
//        loadDataFromSnappy ld = new loadDataFromSnappy();
//        ld.execute();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.record_fragment, container, false);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        View recyclerView = v.findViewById(R.id.item_list);
        adapter = new SimpleItemRecyclerViewAdapter(HabitList.ITEMS);

        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (v.findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        IntentFilter filter = new IntentFilter(MainActivity.RELOAD_DATA_FRAGMENT);
        getActivity().registerReceiver(broadcastReceiver, filter);
        return v;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            adapter.notifyDataSetChanged();
        }
    };

    private class loadDataFromSnappy extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            NoSQL.with(getActivity()).using(Habit.class)
                    .bucketId("habit")
                    .retrieve(new RetrievalCallback<Habit>() {
                        @Override
                        public void retrievedResults(List<NoSQLEntity<Habit>> noSQLEntities) {
                            for(int i = 0;i<noSQLEntities.size();i++){
                                Habit currentBean = noSQLEntities.get(i).getData(); // always check length of a list first...
//                                for(int j=0;j<HabitList.ITEMS.size();j++){
//                                    if(HabitList.ITEMS.get(j).id.equals(currentBean.id)){
                                        HabitList.addItem(currentBean);
//                                    }
//                                }
                            }
                            adapter.notifyDataSetChanged();
                        }


                    });


            return null;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(adapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Habit> mValues;

        public SimpleItemRecyclerViewAdapter(List<Habit> items) {
            mValues = items;
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

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id+"");
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_TYPE, holder.mItem.type);
                        //Toast.makeText(getActivity(),holder.mItem.id+" "+holder.mItem.type,Toast.LENGTH_SHORT).show();
                        context.startActivity(intent);

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Habit mItem;

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
