package com.tongji.android.recorder_app.Fragment;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;

import android.widget.Spinner;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.IndeterminateProgressButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tongji.android.recorder_app.Activity.LoginActivity;
import com.tongji.android.recorder_app.Activity.MainActivity;
import com.tongji.android.recorder_app.Application.MyApplication;
import com.tongji.android.recorder_app.Model.DateItem;
import com.tongji.android.recorder_app.Model.DateList;
import com.tongji.android.recorder_app.Model.Habit;
import com.tongji.android.recorder_app.Model.HabitList;
import com.tongji.android.recorder_app.Model.SystemDefaultHabit;
import com.tongji.android.recorder_app.Model.SystemHabitList;
import com.tongji.android.recorder_app.R;
import com.tongji.android.recorder_app.Service.AlarmReceiver;


import android.app.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

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
    private String daytime ;
    private String duration ;
    private String degree ;
    private int temphourOfDay;
    private int tempminute;
    private  MyApplication myapp;
    PuncuGridViewAdapter puncuGridViewAdapter;



    public Punchcard() {
        // Required empty public constructor
    }
    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
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
        myapp = (MyApplication)getActivity().getApplication();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        if (!SystemHabitList.flag) {
            SystemHabitList.initList();
        }

        View view = inflater.inflate(R.layout.punch_card_fragment, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.punch_card_main_habit_gridView);
        puncuGridViewAdapter = new PuncuGridViewAdapter(getActivity());

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

//                final TextView textView = (TextView) dialogView.findViewById(R.id.add_custom_dialog_textView);
//
//                if (SystemHabitList.ITEMS.size() == 0) {
//                    textView.setBackgroundColor(Color.parseColor("#ee0000"));
//                }


                final Button button = (Button) dialogView.findViewById(R.id.add_custom_dialog_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder buttonBuilder = new AlertDialog.Builder(getActivity());

                        final View addOwnHabitView = inflater.inflate(R.layout.add_user_own_habit,null);
                        final TextView hintText = (TextView) addOwnHabitView.findViewById(R.id.hint_text);
                        final EditText durationInput = (EditText) addOwnHabitView.findViewById(R.id.duration_input);
                        final EditText degreeInput = (EditText) addOwnHabitView.findViewById(R.id.degree_input);
                        final Button timePicker = (Button) addOwnHabitView.findViewById(R.id.time_picker);
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
                                switch (spinnerItemString) {
                                    case "Date":
                                        hintText.setText("Please choose time");
                                        timePicker.setVisibility(View.VISIBLE);
                                        timePicker.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final Calendar c = Calendar.getInstance();
                                                int mHour = c.get(Calendar.HOUR_OF_DAY);
                                                int mMinute = c.get(Calendar.MINUTE);

                                                // Launch Time Picker Dialog
                                                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                                        new TimePickerDialog.OnTimeSetListener() {

                                                            @Override
                                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                                  int minute) {
                                                                temphourOfDay=hourOfDay;
                                                                tempminute=minute;
                                                                daytime = hourOfDay + ":" + minute;
                                                                //txtTime.setText(hourOfDay + ":" + minute);
                                                            }
                                                        }, mHour, mMinute, false);
                                                timePickerDialog.show();

                                            }
                                        });
                                        durationInput.setVisibility(View.GONE);
                                        degreeInput.setVisibility(View.GONE);
                                        break;
                                    case "Degree":
                                        hintText.setText("Please input times");
                                        timePicker.setVisibility(View.GONE);
                                        durationInput.setVisibility(View.GONE);
                                        degreeInput.setVisibility(View.VISIBLE);
                                        break;
                                    case "Do or Not":
                                        hintText.setText("It'a common habit");
                                        timePicker.setVisibility(View.GONE);
                                        durationInput.setVisibility(View.GONE);
                                        degreeInput.setVisibility(View.GONE);
                                        break;
                                    case "Duration":
                                        hintText.setText("Please input duration time");
                                        timePicker.setVisibility(View.GONE);
                                        durationInput.setVisibility(View.VISIBLE);
                                        degreeInput.setVisibility(View.GONE);
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        buttonBuilder.setView(addOwnHabitView).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                CharSequence charSequence = editText.getText();

                                if (charSequence.toString().trim().equals("") ) {
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
                                            currentHabit = new Habit("d1", charSequence.toString(), 0, Habit.TYPE_DATE,daytime);      //记得修改ID
                                            break;
                                        case "Degree" :
                                            currentHabit = new Habit("d2", charSequence.toString(), 0, Habit.TYPE_DEGREE,degreeInput.getText().toString()+"times");      //记得修改ID
                                            break;
                                        case "Do or Not" :
                                            currentHabit = new Habit("d3", charSequence.toString(), 0, Habit.TYPE_DOORNOT,"");      //记得修改ID
                                            break;
                                        case "Duration" :
                                            currentHabit = new Habit("d4", charSequence.toString(), 0, Habit.TYPE_DURATION,durationInput.getText().toString()+"minutes");      //记得修改ID
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
                                        AsyncHttpClient client = new AsyncHttpClient();
                                        RequestParams params = new RequestParams();

                                        params.add("phoneNumber",myapp.getPhoneNumber());
                                        params.add("catalog",currentHabit.type+"");
                                        params.add("feature",currentHabit.feature);
                                        params.add("name",currentHabit.habitName);

                                        final Habit finalCurrentHabit = currentHabit;
                                        client.post("http://lshunran.com:3000/recorder/addhabit", params, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                String re = new String(responseBody);
                                                try {
                                                    JSONObject object = new JSONObject(re);
                                                    int errCode = object.getInt("errCode");
                                                    if (errCode == 0) {

                                                        Toast.makeText(getActivity(),temphourOfDay+" "+tempminute,Toast.LENGTH_SHORT).show();
                                                        if(finalCurrentHabit.type == finalCurrentHabit.TYPE_DATE){
                                                            setAlarm();
                                                        }
                                                        //通知服务器并且加入到本地数据库里面，往下写
                                                        finalCurrentHabit.id = object.getString("id");
                                                        HabitList.addItem(finalCurrentHabit);
                                                        DateItem dateItem = new DateItem(finalCurrentHabit.type, finalCurrentHabit.id);
                                                        DateList.addItem(dateItem.type,dateItem);
                                                        NoSQLEntity<Habit> entity = new NoSQLEntity<Habit>("habit",finalCurrentHabit.id);
                                                        entity.setData(finalCurrentHabit);
                                                        NoSQL.with(getActivity()).using(Habit.class).save(entity);
                                                        NoSQLEntity<DateItem> entity2 = new NoSQLEntity<DateItem>("date",dateItem.type+"+"+dateItem.id);
                                                        entity2.setData(dateItem);
                                                        NoSQL.with(getActivity()).using(DateItem.class).save(entity2);

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                            }
                                        });

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
                                AsyncHttpClient client = new AsyncHttpClient();
                                RequestParams params = new RequestParams();

                                params.add("phoneNumber",myapp.getPhoneNumber());
                                params.add("catalog",temp.get(i).type+"");
                                params.add("feature",temp.get(i).feature);
                                params.add("name",temp.get(i).habitName);
                                final Habit temphabit =temp.get(i);
                                final int finalI = i;
                                client.post("http://lshunran.com:3000/recorder/addhabit", params, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                String re = new String(responseBody);
                                                try {
                                                    JSONObject object = new JSONObject(re);
                                                    int errCode = object.getInt("errCode");
                                                    if (errCode == 0) {
                                                        temphabit.id = object.getString("id");
                                                        HabitList.addItem(temphabit);
                                                        DateItem dateItem = new DateItem(temphabit.type,temphabit.id);
                                                        DateList.addItem(dateItem.type,dateItem);
                                                        NoSQLEntity<Habit> entity = new NoSQLEntity<Habit>("habit",temphabit.id);
                                                        entity.setData(temphabit);
                                                        NoSQL.with(getActivity()).using(Habit.class).save(entity);
                                                        NoSQLEntity<DateItem> entity2 = new NoSQLEntity<DateItem>("date",dateItem.type+"+"+dateItem.id);
                                                        entity2.setData(dateItem);
                                                        NoSQL.with(getActivity()).using(DateItem.class).save(entity2);
                                                        dialogGridViewAdapter.notifyDataSetChanged();
                                                        puncuGridViewAdapter.notifyDataSetChanged();
                                                        Intent intent = new Intent(MainActivity.RELOAD_DATA_FRAGMENT);

                                                        getActivity().sendBroadcast(intent);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                            }
                                        });



                            }
                        }
                        temp.clear();

                        dialogGridViewAdapter.notifyDataSetChanged();
                        puncuGridViewAdapter.notifyDataSetChanged();
                        dialog.dismiss();





            // Toast.makeText(getActivity(),"Check OK",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.RELOAD_DATA_FRAGMENT);

                        getActivity().sendBroadcast(intent);
                        //把系统默认的习惯加入到HabitList里面，并且更新服务器数据和本地数据库，往下写


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(temp!=null){
                            for(int i=0;i<temp.size();i++){
                                SystemDefaultHabit currentHabit = new
                                        SystemDefaultHabit(temp.get(i).id,temp.get(i).habitName,temp.get(i).score,temp.get(i).type,temp.get(i).feature);
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
        IntentFilter filter = new IntentFilter(MainActivity.RELOAD_DATA_FRAGMENT);
        getActivity().registerReceiver(broadcastReceiver, filter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getActivity().unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            puncuGridViewAdapter.notifyDataSetChanged();
        }
    };
    private void setAlarm(){

        Calendar calNow = Calendar.getInstance(Locale.getDefault());
        Calendar calSet = (Calendar) calNow.clone();
        calSet.set(Calendar.HOUR_OF_DAY, temphourOfDay);
        calSet.set(Calendar.MINUTE, tempminute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        if(calSet.compareTo(calNow) <= 0){
            calSet.add(Calendar.DATE, 1);
        }
        Toast.makeText(getActivity(),temphourOfDay+" "+tempminute,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
        setNotification(calSet.getTimeInMillis());
    }

    void setNotification(long time){

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
            final Habit habit = new Habit(currentHabit.id,currentHabit.habitName,currentHabit.score,currentHabit.type,currentHabit.feature);
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

    public void modifyHabit(){

    }

    //打卡界面的gridview的适配器
    public class PuncuGridViewAdapter extends BaseAdapter {
        private int mMorphCounter1[] =new int[HabitList.ITEMS.size()];
        private LayoutInflater inflater;
        private Context context;

        private int type;
        private String id;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
//             mMorphCounter1[position] = 1;
            convertView = inflater.inflate(R.layout.punch_card_main_habit_view,null);
            convertView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {

                                                   final AlertDialog.Builder buttonBuilder = new AlertDialog.Builder(getActivity());

                                                   final View addOwnHabitView = inflater.inflate(R.layout.add_user_own_habit, null);
                                                   final TextView hintText = (TextView) addOwnHabitView.findViewById(R.id.hint_text);
                                                   final EditText durationInput = (EditText) addOwnHabitView.findViewById(R.id.duration_input);
                                                   final EditText degreeInput = (EditText) addOwnHabitView.findViewById(R.id.degree_input);
                                                   final Button timePicker = (Button) addOwnHabitView.findViewById(R.id.time_picker);
                                                   final EditText editText = (EditText) addOwnHabitView.findViewById(R.id.add_user_own_habit_EditText);
                                                   final Spinner spinner = (Spinner)
                                                           addOwnHabitView.findViewById(R.id.add_user_own_habit_spinner);

                                                   ArrayAdapter<CharSequence> arrayAdapter =
                                                           ArrayAdapter.createFromResource(
                                                                   getActivity(), R.array.select_user_habit_type, android.R.layout.simple_spinner_item
                                                           );
                                                    editText.setText(HabitList.ITEMS.get(position).habitName);
                                                   editText.setFocusable(false);
                                                   arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                   Habit h = HabitList.ITEMS.get(position);

                                                   spinner.setAdapter(arrayAdapter);


                                                   switch (h.type) {
                                                       case 0:
                                                           spinner.setSelection(0,true);
                                                           spinner.setClickable(false);
                                                           hintText.setText("Please choose time");
                                                           timePicker.setVisibility(View.VISIBLE);
                                                           timePicker.setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   final Calendar c = Calendar.getInstance();
                                                                   int mHour = c.get(Calendar.HOUR_OF_DAY);
                                                                   int mMinute = c.get(Calendar.MINUTE);

                                                                   // Launch Time Picker Dialog
                                                                   TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                                                           new TimePickerDialog.OnTimeSetListener() {

                                                                               @Override
                                                                               public void onTimeSet(TimePicker view, int hourOfDay,
                                                                                                     int minute) {
                                                                                   temphourOfDay = hourOfDay;
                                                                                   tempminute = minute;
                                                                                   daytime = hourOfDay + ":" + minute;
                                                                                   //txtTime.setText(hourOfDay + ":" + minute);
                                                                               }
                                                                           }, mHour, mMinute, false);
                                                                   timePickerDialog.show();

                                                               }
                                                           });
                                                           durationInput.setVisibility(View.GONE);
                                                           degreeInput.setVisibility(View.GONE);
                                                           break;
                                                       case 1:
                                                           spinner.setSelection(1,true);
                                                           spinner.setClickable(false);
                                                           hintText.setText("Please input times");
                                                           timePicker.setVisibility(View.GONE);
                                                           durationInput.setVisibility(View.GONE);
                                                           degreeInput.setVisibility(View.VISIBLE);
                                                           degreeInput.setText(h.feature);
                                                           break;
                                                       case 2:
                                                           spinner.setSelection(2,true);
                                                           spinner.setClickable(false);
                                                           hintText.setText("It'a common habit");
                                                           timePicker.setVisibility(View.GONE);
                                                           durationInput.setVisibility(View.GONE);
                                                           degreeInput.setVisibility(View.GONE);
                                                           break;
                                                       case 3:
                                                           spinner.setSelection(3,true);
                                                           spinner.setClickable(false);
                                                           hintText.setText("Please input duration time");
                                                           timePicker.setVisibility(View.GONE);
                                                           durationInput.setVisibility(View.VISIBLE);
                                                           degreeInput.setVisibility(View.GONE);
                                                           durationInput.setText(h.feature);
                                                           break;
                                                   }
                                                   ;


                                                   buttonBuilder.setView(addOwnHabitView).setPositiveButton("Modify", new DialogInterface.OnClickListener() {
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

                                                               switch (HabitList.ITEMS.get(position).type) {
                                                                   case 0:
                                                                       HabitList.ITEMS.get(position).feature = daytime;
                                                                       setAlarm();
                                                                       break;
                                                                   case 1:
                                                                       HabitList.ITEMS.get(position).feature = degreeInput.getText().toString();
                                                                       break;
                                                                   case 2:
                                                                       HabitList.ITEMS.get(position).feature =  "";
                                                                       break;
                                                                   case 3:
                                                                       HabitList.ITEMS.get(position).feature =  durationInput.getText().toString();
                                                                       break;
                                                                   default:
                                                                       break;
                                                               }


                                                                notifyDataSetChanged();

                                                               dialog.dismiss();
                                                           }
                                                       }
                                                   }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                       @Override
                                                       public void onClick(DialogInterface dialog, int which) {
                                                           dialog.dismiss();
                                                       }
                                                   });


                                                   buttonBuilder.create().show();
                                               }
                                           });




            Habit currentHabit = HabitList.ITEMS.get(position);
            type=currentHabit.type;
            id = currentHabit.id;
            TextView textView = (TextView) convertView.findViewById(R.id.punch_card_main_habit_textView);
            TextView detail = (TextView) convertView.findViewById(R.id.punch_card_main_habit_button_setting);
            textView.setText(currentHabit.habitName);
            detail.setText(currentHabit.feature);

            final IndeterminateProgressButton btnMorph1 = (IndeterminateProgressButton) convertView.findViewById(R.id.btnMorph1);
            if (HabitList.ITEMS.get(position).isChecked) {
                morphAlreadySuccess(btnMorph1);
            }else {
                morphToSquare(btnMorph1, 0);
            }

            btnMorph1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMorphButton1Clicked(btnMorph1,position);
                }
            });
            return convertView;
        }
        private void onMorphButton1Clicked(final IndeterminateProgressButton btnMorph, int position) {
            if (HabitList.ITEMS.get(position).isChecked) {
                morphAlreadySuccess(btnMorph);

                //mMorphCounter1++;
                //morphToSquare(btnMorph, integer(R.integer.mb_animation));
            } else if (!HabitList.ITEMS.get(position).isChecked) {
                //HabitList.ITEMS.get(position).isChecked = true;
                simulateProgress1(btnMorph,position);
            }
        }
        private void morphToSquare(final IndeterminateProgressButton btnMorph, int duration) {
            MorphingButton.Params square = MorphingButton.Params.create()
                    .duration(duration)
                    .cornerRadius(dimen(R.dimen.mb_corner_radius_1))
                    .width(dimen(R.dimen.mb_width_100))
                    .height(dimen(R.dimen.mb_height_56))
                    .color(color(R.color.DarkGreenButton))
                    .colorPressed(color(R.color.DarkGreenButtonPressed))
                    .text(getString(R.string.check));
            btnMorph.morph(square);
        }
        private void morphAlreadySuccess(final IndeterminateProgressButton btnMorph) {
            MorphingButton.Params circle = MorphingButton.Params.create()
                    .duration(integer(R.integer.mb_animation))
                    .cornerRadius(dimen(R.dimen.mb_height_56))
                    .width(dimen(R.dimen.mb_height_56))
                    .height(dimen(R.dimen.mb_height_56))
                    .color(color(R.color.colorAccent))
                    .colorPressed(color(R.color.mb_green_dark))
                    .icon(R.drawable.ic_check_white_24dp);
            btnMorph.morph(circle);

        }
        private void morphToSuccess(final IndeterminateProgressButton btnMorph , final int position) {
            MorphingButton.Params circle = MorphingButton.Params.create()
                    .duration(integer(R.integer.mb_animation))
                    .cornerRadius(dimen(R.dimen.mb_height_56))
                    .width(dimen(R.dimen.mb_height_56))
                    .height(dimen(R.dimen.mb_height_56))
                    .color(color(R.color.colorAccent))
                    .colorPressed(color(R.color.mb_green_dark))
                    .icon(R.drawable.ic_check_white_24dp);
            btnMorph.morph(circle);
            final Calendar c = Calendar.getInstance(Locale.getDefault());
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            //Toast.makeText(getActivity(),year+" "+month+" "+day+":"+type+" "+id,Toast.LENGTH_SHORT).show();
            c.set(year,month,day);
            int sco = HabitList.ITEMS.get(position).score+1;
            String dateStr = year+"/"+month+"/"+day;
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            params.add("phoneNumber",myapp.getPhoneNumber());
            params.add("id", HabitList.ITEMS.get(position).id);
            params.add("score",sco+"");
            params.add("date",dateStr);

            client.post("http://lshunran.com:3000/recorder/updatehabit", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String re = new String(responseBody);
                    try {
                        JSONObject object = new JSONObject(re);
                        int errCode = object.getInt("errCode");
                        if (errCode == 0) {
                            DateList.ITEMS.get(position).addDate(c.getTime());

                            HabitList.ITEMS.get(position).isChecked = true;
                            HabitList.ITEMS.get(position).score++;
                            Intent intent = new Intent(MainActivity.RELOAD_DATA_FRAGMENT);

                            getActivity().sendBroadcast(intent);

                            NoSQL.with(getActivity()).using(Habit.class)
                                    .bucketId("habit")
                                    .entityId(HabitList.ITEMS.get(position).id)
                                    .delete();
                            NoSQL.with(getActivity()).using(DateItem.class)
                                    .bucketId("date")
                                    .entityId(type+"+"+id)
                                    .delete();

                            NoSQLEntity<Habit> entity = new NoSQLEntity<Habit>("habit",HabitList.ITEMS.get(position).id);
                            entity.setData(HabitList.ITEMS.get(position));
                            NoSQL.with(getActivity()).using(Habit.class).save(entity);
                            NoSQLEntity<DateItem> entity2 = new NoSQLEntity<DateItem>("date",type+"+"+id);
                            entity2.setData(DateList.ITEMS.get(position));
                            NoSQL.with(getActivity()).using(DateItem.class).save(entity2);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });

        }


        private void simulateProgress1(@NonNull final IndeterminateProgressButton button , final int position) {
            int progressColor1 = color(R.color.holo_blue_bright);
            int progressColor2 = color(R.color.holo_green_light);
            int progressColor3 = color(R.color.holo_orange_light);
            int progressColor4 = color(R.color.holo_red_light);
            int color = color(R.color.mb_gray);
            int progressCornerRadius = dimen(R.dimen.mb_corner_radius_4);
            int width = dimen(R.dimen.mb_width_200);
            int height = dimen(R.dimen.mb_height_8);
            int duration = integer(R.integer.mb_animation);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    morphToSuccess(button, position);
                    button.unblockTouch();
                }
            }, 2000);

            button.blockTouch(); // prevent user from clicking while button is in progress
            button.morphToProgress(color, progressCornerRadius, width, height, duration, progressColor1, progressColor2,
                    progressColor3, progressColor4);
        }
    }


}
