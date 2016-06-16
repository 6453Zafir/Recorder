package com.tongji.android.recorder_app.Fragment;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.tongji.android.recorder_app.Activity.ItemDetailActivity;
import com.tongji.android.recorder_app.Activity.ItemListActivity;
import com.tongji.android.recorder_app.Application.MyApplication;
import com.tongji.android.recorder_app.Model.DateItem;
import com.tongji.android.recorder_app.Model.DateList;
import com.tongji.android.recorder_app.Model.Habit;
import com.tongji.android.recorder_app.Model.HabitList;
import com.tongji.android.recorder_app.R;
import com.tongji.android.recorder_app.Activity.dummy.DummyContent;
import com.tongji.android.recorder_app.View.RingView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_TYPE = "item_type";

    /**
     * The dummy content this fragment is presenting.
     */
    private Habit mItem;
    private DateItem dateItem;
    private float intensity_per;
    public static float INTENSITY;
    public static final String HABIT_NAME = "";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    private Button show;
    private RingView ringView;
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    public ItemDetailFragment() {
    }

    public float getIntensity(){
        return intensity_per;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            int type = getArguments().getInt(ARG_ITEM_TYPE);
            String id = getArguments().getString(ARG_ITEM_ID);

            for(int i =0;i< DateList.ITEMS.size();i++){
                if(id.equals(DateList.ITEMS.get(i).id) && type == DateList.ITEMS.get(i).type && DateList.ITEMS!=null){
                    dateItem = DateList.ITEMS.get(i);
                    break;
                }else{
                    //dateItem=
                }
            }


            for(int i =0;i< HabitList.ITEMS.size();i++){
                if(id.equals(HabitList.ITEMS.get(i).id) && type == HabitList.ITEMS.get(i).type){
                    mItem = HabitList.ITEMS.get(i);
                    break;
                }
            }


            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.habitName);
              //  HABIT_NAME = mItem.habitName;

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        ringView= (RingView) rootView.findViewById(R.id.tensity);
        float intensity = 0;
        Calendar c =  Calendar.getInstance(Locale.getDefault());
        Date d = new Date();
        c.set(2016,6,d.getDate());
        if(dateItem !=  null){
            for(int i = dateItem.getDateSize()-1;i>=0;i--) {

                Date storedDateFromDB = dateItem.getDate(i);

                long mili = c.getTime().getTime()-storedDateFromDB.getTime();
                long offset = TimeUnit.MILLISECONDS.toDays(mili);
                if(offset>=0 && offset<=7){
                    intensity++;
                }else {
                    break;
                }
            }
        }

        intensity_per = intensity/7;
        INTENSITY = intensity_per;

        MyApplication myapp = (MyApplication)getActivity().getApplication();

        myapp.setTempHabitName(mItem.habitName);

        ringView.setPercentage(intensity_per);
        ringView.setColor(R.color.colorPrimaryDark);

        final CompactCalendarView compactCalendarView = (CompactCalendarView) rootView.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setLocale(Locale.ENGLISH);
        compactCalendarView.setUseThreeLetterAbbreviation(true);


        addEvents(compactCalendarView, dateItem);

        compactCalendarView.invalidate();


        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                //Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });

        compactCalendarView.showCalendarWithAnimation();

        return rootView;
    }
    private void addEvents(CompactCalendarView compactCalendarView, DateItem dateItem) {
//        currentCalender.setTime(new Date());
//        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        if(dateItem!=null){
            for(int i = 0;i<dateItem.getDateSize();i++){
                Date storedDateFromDB = dateItem.getDate(i);

                int day = storedDateFromDB.getDate();
                int mo = storedDateFromDB.getMonth()-1;
                int year = storedDateFromDB.getYear()+1900;

                currentCalender.set(year,mo,day);
                //currentCalender.setTime(storedDateFromDB);

                setToMidnight(currentCalender);
                long timeInMillis = currentCalender.getTimeInMillis();

                List<Event> events = getEvents(timeInMillis, 1);

                compactCalendarView.addEvents(events);
            }
        }



    }
    private List<Event> getEvents(long timeInMillis, int day) {
        if (day < 2) {
            return Arrays.asList(new Event(getResources().getColor(R.color.dotColor), timeInMillis, "Event at " + new Date(timeInMillis)));
        } else if ( day > 2 && day <= 4) {
            return Arrays.asList(
                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)));
        } else {
            return Arrays.asList(
                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis) ),
                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 70, 68, 65), timeInMillis, "Event 3 at " + new Date(timeInMillis)));
        }
    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

}
