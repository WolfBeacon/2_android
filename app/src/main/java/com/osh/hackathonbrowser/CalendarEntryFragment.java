package com.osh.hackathonbrowser;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.osh.hackathonbrowser.model.CalendarEntry;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarEntryFragment extends Fragment {
    SparseArray<List<CalendarEntry>> dayOfHackathonToEvents = new SparseArray<>();

    RecyclerView recyclerView;
    LinearLayout dayViewContainer;

    public CalendarEntryFragment() {
    }

    @SuppressWarnings("unused")
    public static CalendarEntryFragment newInstance() {
        CalendarEntryFragment fragment = new CalendarEntryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) { //Set arguments from bundlle
        }

        List<CalendarEntry> events = CalendarEntry.getDummyData();
        Calendar firstEntry = null;
        for(CalendarEntry entry : events){
            if(firstEntry == null || entry.getStartTime().before(firstEntry)){
                firstEntry = entry.getStartTime();
            }
        }

        //First day is the "firstEntry"
        Calendar temp = (Calendar) firstEntry.clone();
        for(CalendarEntry entry : events){
            Calendar time  = entry.getStartTime();
            int rollAmount = 0;
            while(time.get(Calendar.DAY_OF_MONTH) != temp.get(Calendar.DAY_OF_MONTH) ||
                    time.get(Calendar.MONTH) != temp.get(Calendar.MONTH) ||
                    time.get(Calendar.YEAR) != temp.get(Calendar.YEAR)){
                temp.roll(Calendar.DAY_OF_MONTH, 1);
                rollAmount++;
            }

            if(dayOfHackathonToEvents.get(rollAmount) != null){
                dayOfHackathonToEvents.get(rollAmount).add(entry);
            } else {
                List<CalendarEntry> dayList = new ArrayList<>();
                dayList.add(entry);
                dayOfHackathonToEvents.put(rollAmount, dayList);
            }

            temp.roll(Calendar.DAY_OF_MONTH, -rollAmount);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendary_entry_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        dayViewContainer = (LinearLayout) view.findViewById(R.id.day_selector_container);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).color(Color.parseColor("#404040")).build());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        LayoutInflater inflater = LayoutInflater.from(getContext());

        dayViewContainer.removeAllViews();
        for(int i = 0; i < dayOfHackathonToEvents.size(); i++){
            final int key = dayOfHackathonToEvents.keyAt(i);

            View dayView = inflater.inflate(R.layout.day_selector_layout, dayViewContainer, false);
            ((TextView) dayView).setText(getString(R.string.day_string, key + 1));
            dayView.setBackgroundResource(i == 0 ? R.drawable.day_rounded_rectangle : 0);
            dayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i = 0; i < dayViewContainer.getChildCount(); i++){
                        dayViewContainer.getChildAt(i).setBackgroundResource(0);
                    }
                    v.setBackgroundResource(R.drawable.day_rounded_rectangle);
                    updateFilledList(key);
                }
            });

            dayViewContainer.addView(dayView);
        }

        updateFilledList(0);
    }

    private void updateFilledList(int index){
        recyclerView.setAdapter(new CalendarRecyclerViewAdapter(dayOfHackathonToEvents.get(index)));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
