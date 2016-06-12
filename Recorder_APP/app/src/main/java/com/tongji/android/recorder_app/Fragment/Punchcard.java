package com.tongji.android.recorder_app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
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


import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.IndeterminateProgressButton;
import com.dd.morphingbutton.impl.LinearProgressButton;
import com.tongji.android.recorder_app.Activity.MainActivity;
import com.tongji.android.recorder_app.Model.DateItem;
import com.tongji.android.recorder_app.Model.DateList;
import com.tongji.android.recorder_app.Model.Habit;
import com.tongji.android.recorder_app.Model.HabitList;
import com.tongji.android.recorder_app.Model.SystemDefaultHabit;
import com.tongji.android.recorder_app.Model.SystemHabitList;
import com.tongji.android.recorder_app.R;

import java.util.Calendar;
import java.util.Locale;

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
                        Intent intent = new Intent(MainActivity.RELOAD_DATA_FRAGMENT);

                        getActivity().sendBroadcast(intent);
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
        private int mMorphCounter1[] =new int[HabitList.ITEMS.size()];
        private LayoutInflater inflater;
        private Context context;

        private int type;
        private int id;
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
             mMorphCounter1[position] = 1;
            convertView = inflater.inflate(R.layout.punch_card_main_habit_view,null);
            Habit currentHabit = HabitList.ITEMS.get(position);
            type=currentHabit.type;
            id = currentHabit.id;
            TextView textView = (TextView) convertView.findViewById(R.id.punch_card_main_habit_textView);
            textView.setText(currentHabit.habitName);
//            final CircularProgressButton circularButton1 = (CircularProgressButton) convertView.findViewById(R.id.punch_card_main_habit_button_punch);
//            circularButton1.setIdleText(getResources().getString(R.string.check));
////            circularButton1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//            circularButton1.setCompleteText("checked");
//            circularButton1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (circularButton1.getProgress() == 0) {
//                        circularButton1.setIndeterminateProgressMode(true);
//                        circularButton1.setProgress(50);
//                    }
//                }
//            });
            final IndeterminateProgressButton btnMorph1 = (IndeterminateProgressButton) convertView.findViewById(R.id.btnMorph1);
            morphToSquare(btnMorph1, 0);
            btnMorph1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMorphButton1Clicked(btnMorph1,position);
                }
            });
            return convertView;
        }
        private void onMorphButton1Clicked(final IndeterminateProgressButton btnMorph, int position) {
            if (mMorphCounter1[position] == 0) {
                //mMorphCounter1++;
                //morphToSquare(btnMorph, integer(R.integer.mb_animation));
            } else if (mMorphCounter1[position] == 1) {
                mMorphCounter1[position] = 0;
                simulateProgress1(btnMorph);
            }
        }
        private void morphToSquare(final IndeterminateProgressButton btnMorph, int duration) {
            MorphingButton.Params square = MorphingButton.Params.create()
                    .duration(duration)
                    .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                    .width(dimen(R.dimen.mb_width_100))
                    .height(dimen(R.dimen.mb_height_56))
                    .color(color(R.color.mb_blue))
                    .colorPressed(color(R.color.mb_blue_dark))
                    .text(getString(R.string.check));
            btnMorph.morph(square);
        }
        private void morphToSuccess(final IndeterminateProgressButton btnMorph) {
            MorphingButton.Params circle = MorphingButton.Params.create()
                    .duration(integer(R.integer.mb_animation))
                    .cornerRadius(dimen(R.dimen.mb_height_56))
                    .width(dimen(R.dimen.mb_height_56))
                    .height(dimen(R.dimen.mb_height_56))
                    .color(color(R.color.mb_green))
                    .colorPressed(color(R.color.mb_green_dark))
                    .icon(R.drawable.ic_check_white_24dp);
            btnMorph.morph(circle);
            Calendar c = Calendar.getInstance(Locale.ENGLISH);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            Toast.makeText(getActivity(),year+" "+month+" "+day,Toast.LENGTH_SHORT).show();
            c.set(year,month,day);
            DateItem d = new DateItem(type,id);
            d.addDate(c.getTime());
            DateList.addItem(type,d);
        }
        private void simulateProgress2(@NonNull final IndeterminateProgressButton button) {
            int progressColor = color(R.color.mb_blue);
            int color = color(R.color.mb_gray);
            int progressCornerRadius = dimen(R.dimen.mb_corner_radius_4);
            int width = dimen(R.dimen.mb_width_200);
            int height = dimen(R.dimen.mb_height_8);
            int duration = integer(R.integer.mb_animation);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    morphToSquare(button, integer(R.integer.mb_animation));
                    button.unblockTouch();
                }
            }, 4000);

            button.blockTouch(); // prevent user from clicking while button is in progress
            button.morphToProgress(color, progressCornerRadius, width, height, duration, progressColor);
        }

        private void simulateProgress1(@NonNull final IndeterminateProgressButton button) {
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
                    morphToSuccess(button);
                    button.unblockTouch();
                }
            }, 4000);

            button.blockTouch(); // prevent user from clicking while button is in progress
            button.morphToProgress(color, progressCornerRadius, width, height, duration, progressColor1, progressColor2,
                    progressColor3, progressColor4);
        }
    }


}
