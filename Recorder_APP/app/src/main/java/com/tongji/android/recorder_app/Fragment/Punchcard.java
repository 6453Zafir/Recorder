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
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tongji.android.recorder_app.Model.Habit;
import com.tongji.android.recorder_app.Model.HabitList;
import com.tongji.android.recorder_app.Model.SystemDefaultHabit;
import com.tongji.android.recorder_app.Model.SystemHabitList;
import com.tongji.android.recorder_app.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    private String spinnerItemString;
    private List<Habit> temp;



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
        temp = new ArrayList<Habit>();
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

                final TextView textView = (TextView) dialogView.findViewById(R.id.add_custom_dialog_textView);

                if (SystemHabitList.ITEMS.size() == 0) {
                    textView.setBackgroundColor(Color.parseColor("#ee0000"));
                }

                final Button button = (Button) dialogView.findViewById(R.id.add_custom_dialog_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder buttonBuilder = new AlertDialog.Builder(getActivity());

                        View addOwnHabitView = inflater.inflate(R.layout.add_user_own_habit,null);


                        final EditText editText = (EditText) addOwnHabitView.findViewById(R.id.add_user_own_habit_EditText);
                        final Spinner spinner = (Spinner)
                                addOwnHabitView.findViewById(R.id.add_user_own_habit_spinner);

                        ArrayAdapter<CharSequence> arrayAdapter =
                                ArrayAdapter.createFromResource(
                                        getActivity(),R.array.select_user_habit_type,android.R.layout.simple_spinner_item
                                );

                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(arrayAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                spinnerItemString = (String) parent.getItemAtPosition(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        buttonBuilder.setView(addOwnHabitView).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                CharSequence charSequence = editText.getText();

                                if (charSequence.toString().trim().equals("")) {
                                    AlertDialog.Builder ifNullBuilder = new AlertDialog.Builder(getActivity());
                                    ifNullBuilder.setTitle("You must enter habit name")
                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();

                                } else {
                                    Habit currentHabit = null;
                                    switch (spinnerItemString) {
                                        case "Date" :
                                            currentHabit = new Habit("0", charSequence.toString(), 0, 0);      //记得修改ID
                                            break;
                                        case "Degree" :
                                            currentHabit = new Habit("0", charSequence.toString(), 0, 1);      //记得修改ID
                                            break;
                                        case "Do or Not" :
                                            currentHabit = new Habit("0", charSequence.toString(), 0, 2);      //记得修改ID
                                            break;
                                        case "Duration" :
                                            currentHabit = new Habit("0", charSequence.toString(), 0, 3);      //记得修改ID
                                            break;
                                        default :
                                            break;
                                    }

                                    boolean currentFlag = false;
                                    for (int i = 0; i < HabitList.ITEMS.size(); i++) {

                                        if (HabitList.ITEMS.get(i).habitName.equals(charSequence.toString().trim())) {
                                            currentFlag = true;
                                            break;
                                        }
                                    }


                                    if (currentFlag) {
                                        AlertDialog.Builder habitBuilder = new AlertDialog.Builder(getActivity());
                                        habitBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).setTitle("Habit already exists!").create().show();

                                    } else {
                                        HabitList.addItem(currentHabit);
                                        //通知服务器并且加入到本地数据库里面，往下写
                                    }

                                    dialog.dismiss();
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editText.setText("");
                            }
                        });

                        buttonBuilder.create().show();
                    }
                });

                builder.setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(temp!=null){
                            for(int i=0;i<temp.size();i++){
                                HabitList.addItem(temp.get(i));
                            }
                        }
                        temp.clear();

                        dialogGridViewAdapter.notifyDataSetChanged();
                        puncuGridViewAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"Check OK",Toast.LENGTH_SHORT).show();
                        //把系统默认的习惯加入到HabitList里面，并且更新服务器数据和本地数据库，往下写


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(temp!=null){
                            for(int i=0;i<temp.size();i++){
                                SystemDefaultHabit currentHabit = new
                                        SystemDefaultHabit(temp.get(i).id,temp.get(i).habitName,temp.get(i).score,temp.get(i).type);
                                SystemHabitList.addItem(currentHabit);
                            }
                        }
                        temp.clear();

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
                        //HabitList.addItem(habit);
                        temp.add(habit);
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

            return convertView;
        }
    }


}
