package com.tongji.android.recorder_app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.tongji.android.recorder_app.Model.Habit;
import com.tongji.android.recorder_app.Model.HabitList;
import com.tongji.android.recorder_app.Model.SystemDefaultHabit;
import com.tongji.android.recorder_app.Model.SystemHabitList;
import com.tongji.android.recorder_app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Punchcard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Punchcard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Punchcard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Punchcard.
     */
    // TODO: Rename and change types and number of parameters
    public static Punchcard newInstance(String param1, String param2) {
        Punchcard fragment = new Punchcard();
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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        if (!SystemHabitList.flag) {
            SystemHabitList.initList();
        }



        View view = inflater.inflate(R.layout.punch_card_fragment, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.punch_card_main_habit_gridView);
        final PuncuGridViewAdapter puncuGridViewAdapter = new PuncuGridViewAdapter(getActivity());

        gridView.setAdapter(puncuGridViewAdapter);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                View dialogView = inflater.inflate(R.layout.punch_card_add_custom_dialog,null);
                GridView gridView = (GridView) dialogView.findViewById(R.id.add_custom_dialog_gridView);
                final DialogGridViewAdapter dialogGridViewAdapter = new DialogGridViewAdapter(getActivity());
                gridView.setAdapter(dialogGridViewAdapter);

                TextView textView = (TextView) dialogView.findViewById(R.id.add_custom_dialog_textView);

                if (SystemHabitList.ITEMS.size() == 0) {
                    textView.setBackgroundColor(Color.parseColor("#ee0000"));
                }

                final Button button = (Button) dialogView.findViewById(R.id.add_custom_dialog_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        button.setTextColor(Color.parseColor("#ffffff"));
                        AlertDialog.Builder buttonBuilder = new AlertDialog.Builder(getActivity());

                    }
                });

                builder.setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialogGridViewAdapter.notifyDataSetChanged();
                        puncuGridViewAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"Check OK",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"Check Cancel",Toast.LENGTH_SHORT).show();
                    }
                }).create().show();

            }
        });

        return view;
    }

    //弹出的对话框的gridview适配器
    public class DialogGridViewAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Context context;

        public DialogGridViewAdapter (Context context) {
            this.inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return SystemHabitList.ITEMS.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = inflater.inflate(R.layout.punch_card_add_custom_dialog_view,null);
            final SystemDefaultHabit currentHabit = SystemHabitList.ITEMS.get(position);
            final Habit habit = new Habit(currentHabit.id,currentHabit.habitName,currentHabit.score,currentHabit.type);
            TextView textView = (TextView) convertView.findViewById(R.id.dialog_view_TextView);
            textView.setText(currentHabit.habitName);

            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.dialog_view_checkBox);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        HabitList.addItem(habit);
                        SystemHabitList.removeItem(currentHabit);
                        System.out.println("Habit : " + HabitList.ITEMS.size() + ", System: " + SystemHabitList.ITEMS.size());
                    } else {


                        if (HabitList.ITEMS.remove(habit)) {
                            System.out.println("Successfully");
                        }

                        if (SystemHabitList.ITEMS.contains(currentHabit)) {
                            ;
                        }
                        else {
                            SystemHabitList.addItem(currentHabit);
                        }
                    }
                }
            });

            return convertView;
        }
    }



    //打卡界面的gridview的适配器
    public class PuncuGridViewAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Context context;

        public PuncuGridViewAdapter (Context context) {
            this.inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return HabitList.ITEMS.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = inflater.inflate(R.layout.punch_card_main_habit_view,null);
            Habit currentHabit = HabitList.ITEMS.get(position);
            TextView textView = (TextView) convertView.findViewById(R.id.punch_card_main_habit_textView);
            textView.setText(currentHabit.habitName);
            final CircularProgressButton circularButton1 = (CircularProgressButton) convertView.findViewById(R.id.punch_card_main_habit_button_punch);
            circularButton1.setIdleText(getResources().getString(R.string.check));
//            circularButton1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            circularButton1.setCompleteText("checked");
            circularButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (circularButton1.getProgress() == 0) {
                        circularButton1.setIndeterminateProgressMode(true);
                        circularButton1.setProgress(50);
                    }
                }
            });
            return convertView;
        }
    }

}
