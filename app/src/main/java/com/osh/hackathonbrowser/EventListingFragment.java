package com.osh.hackathonbrowser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.osh.hackathonbrowser.model.HackathonEvent;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;

import static butterknife.ButterKnife.findById;

public class EventListingFragment extends BaseFragment {
    public static final String TAG = "EventListingFragment";

    public static final String TITLE_STRING_ARGUMENT = "title";

    private String title = "(not set)";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    List<HackathonEvent> events = new ArrayList<>();
    EventAdapter adapter;

    public class EventAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView noItems;
        public View mainContainer;
        public TextView title;
        public TextView subtitle;
        public TextView description;
        public ImageView icon;

        public EventAdapterViewHolder(View itemView) {
            super(itemView);
            this.mainContainer = findById(itemView, R.id.event_listing_container);
            this.noItems = findById(itemView, R.id.event_listing_end_of_results);
            this.title = findById(itemView, R.id.event_listing_title);
            this.subtitle = findById(itemView, R.id.event_listing_subtitle);
            this.description = findById(itemView, R.id.event_listing_description);
            this.icon = findById(itemView, R.id.event_listing_icon);
        }
    }

    public class EventAdapter extends RecyclerView.Adapter<EventAdapterViewHolder> {
        private List<HackathonEvent> events;

        public EventAdapter(List<HackathonEvent> events){
            this.events = events;
        }

        @Override
        public EventAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_listing_view, parent, false);
            return new EventAdapterViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final EventAdapterViewHolder holder, int position) {
            holder.noItems.setVisibility(position == getItemCount() - 1 ? View.VISIBLE : View.GONE);
            holder.mainContainer.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
            if(position == getItemCount() - 1) return;

            final HackathonEvent event = events.get(position);
            holder.title.setText(event.getTitle());
            holder.subtitle.setText(event.getSubtitle());
            holder.description.setText(event.getDescription());
            holder.mainContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    Picasso.with(v.getContext()).load(event.getIconUrl())
                            .resize(holder.icon.getWidth(), holder.icon.getHeight()).centerInside()
                            .into(holder.icon);
                    holder.mainContainer.removeOnLayoutChangeListener(this);
                }
            });
        }

        @Override
        public int getItemCount() {
            return events.size() + 1; //For end of list at end
        }
    }

    public EventListingFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static EventListingFragment newInstance(String title){
        EventListingFragment fragment = new EventListingFragment();

        Bundle args = new Bundle();
        args.putString(TITLE_STRING_ARGUMENT, title);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.title = args.getString(TITLE_STRING_ARGUMENT);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_listing, container, false);

        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Setup RecyclerView
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        adapter = new EventAdapter(events);
        recyclerView.setAdapter(adapter);

        events.clear();
        events.add(new HackathonEvent("HackNY", "Columbia University", "The spring version of HackNY, this hackathon encourages lipsum etc.", "https://s.graphiq.com/sites/default/files/728/media/images/t2/Columbia_University_College_of_Physicians_and_Surgeons_2_403319.jpg"));
        events.add(new HackathonEvent("U of T Hacks", "University of Toronto", "A major hackathon held each year, this hackathon encourages lipsum etc.", "http://cupanion.com/wordpress/wp-content/uploads/2015/09/University-of-Toronto-logo_0-1.jpg"));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getNameResource(Context context) {
        return title;
    }
}
