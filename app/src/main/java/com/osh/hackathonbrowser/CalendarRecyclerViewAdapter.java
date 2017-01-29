package com.osh.hackathonbrowser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.osh.hackathonbrowser.model.CalendarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class CalendarRecyclerViewAdapter extends RecyclerView.Adapter<CalendarRecyclerViewAdapter.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_TIME_HEADER = 1;
    private static final int ITEM_VIEW_TYPE_EVENT = 2;

    private final List<WrappedCalendarEntry> mValues;

    /** A psuedo-union. **/
    private class WrappedCalendarEntry {
        CalendarEntry calendarData;
        Calendar time;
        int itemType;

        public WrappedCalendarEntry(CalendarEntry entry) {
            this.calendarData = entry;
            this.time = null;
            this.itemType = ITEM_VIEW_TYPE_EVENT;
        }

        public WrappedCalendarEntry(Calendar time){
            this.calendarData = null;
            this.time = time;
            this.itemType = ITEM_VIEW_TYPE_TIME_HEADER;
        }

        public int getItemType(){
            return itemType;
        }

        public CalendarEntry getCalendarData() {
            return calendarData;
        }

        public Calendar getTime() {
            return time;
        }
    }

    /**
     * Create an adapter. This constructor takes care of sorting the data and generating headers.
     * @param items The raw entries to be in the list.
     */
    public CalendarRecyclerViewAdapter(List<CalendarEntry> items) {
        Collections.sort(items, new Comparator<CalendarEntry>() {
            @Override
            public int compare(CalendarEntry o1, CalendarEntry o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });

        List<WrappedCalendarEntry> result = new ArrayList<>(items.size() * 2);
        long lastTimeEncountered = -1;
        for(CalendarEntry item : items){
            if(lastTimeEncountered != item.getStartTime().getTimeInMillis()){
                result.add(new WrappedCalendarEntry(item.getStartTime()));
                lastTimeEncountered = item.getStartTime().getTimeInMillis();
            }
            result.add(new WrappedCalendarEntry(item));
        }

        mValues = result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_calendar_entry, parent, false);
        return new ViewHolder(view);
    }

    public final SimpleDateFormat HEADER_FORMAT = new SimpleDateFormat("h:mm aa", Locale.getDefault());

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(getItemViewType(position) == ITEM_VIEW_TYPE_TIME_HEADER){
            Calendar startTime = mValues.get(position).getTime();
            holder.mHeaderView.setVisibility(View.VISIBLE);
            holder.mContentView.setVisibility(View.GONE);
            holder.mHeaderText.setText(HEADER_FORMAT.format(startTime.getTime()));
        } else {
            final CalendarEntry entry = mValues.get(position).getCalendarData();

            holder.mHeaderView.setVisibility(View.GONE);
            holder.mContentView.setVisibility(View.VISIBLE);
            holder.mContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    entry.setIsGoingTo(!entry.isGoingTo());
                }
            });
            holder.mContentTitle.setText(entry.getEventName());

            StringBuilder subtitleStr = new StringBuilder();
            subtitleStr.append(entry.getEventLocation());
            subtitleStr.append(" - ");

            int hours = (int) Math.floor(entry.getEventDuration() / 1000 / 60 / 60);
            int minutes = (int) (Math.floor(entry.getEventDuration() / 1000 / 60) - (hours * 60));

            if(hours > 0 && minutes != 0){
                subtitleStr.append(hours);
                subtitleStr.append(".");

                subtitleStr.append(String.format(Locale.getDefault(), "%1.0f", (minutes / 60f) * 10));
                subtitleStr.append(" hours");
            } else if (hours > 0){
                subtitleStr.append(hours);
                if(hours > 1){
                    subtitleStr.append(" hours");
                } else {
                    subtitleStr.append(" hour");
                }
            } else {
                subtitleStr.append(minutes);
                subtitleStr.append(" mins");
            }

            holder.mContentSubtitle.setText(subtitleStr.toString());
            holder.mContentRequired.setVisibility(entry.isGoingTo() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mHeaderView;
        public final TextView mHeaderText;

        public final View mContentView;
        public final TextView mContentTitle;
        public final TextView mContentSubtitle;
        public final View mContentRequired;

        public ViewHolder(View view) {
            super(view);

            mHeaderView = view.findViewById(R.id.list_item_cal_header_content);
            mHeaderText = (TextView) view.findViewById(R.id.schedule_header_text);

            mContentView = view.findViewById(R.id.list_item_cal_entry_content);
            mContentTitle = (TextView) view.findViewById(R.id.cal_entry_title);
            mContentSubtitle = (TextView) view.findViewById(R.id.cal_entry_subtitle);
            mContentRequired = view.findViewById(R.id.cal_entry_required);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mValues.get(position).getItemType();
    }
}
