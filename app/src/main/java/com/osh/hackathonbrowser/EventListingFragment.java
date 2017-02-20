package com.osh.hackathonbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.osh.hackathonbrowser.api.ApiFactory;
import com.osh.hackathonbrowser.api.response.HackathonResponse;
import com.osh.hackathonbrowser.cache.HackCache;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static butterknife.ButterKnife.findById;

public class EventListingFragment extends BaseFragment {
    public static final String TAG = "EventListingFragment";

    public static final String TITLE_STRING_ARGUMENT = "title";
    public static final String LISTING_MODE_ARGUMENT = "listing_mode";

    public static final int LISTING_MODE_NEARBY_HACKS = 1;
    public static final int LISTING_MODE_FAVORITE_HACKS = 2;

    private String title = "(not set)";
    private int mode = LISTING_MODE_FAVORITE_HACKS;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    FragmentHostInterface host;
    List<HackathonResponse> events = new ArrayList<>();
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
        private List<HackathonResponse> events;
        private int endOfResultsStringRes = R.string.end_of_results;

        public EventAdapter(List<HackathonResponse> events){
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
            holder.noItems.setText(endOfResultsStringRes);
            holder.mainContainer.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
            if(position == getItemCount() - 1) return;

            final HackathonResponse event = events.get(position);
            holder.title.setText(event.getTitle());
            holder.subtitle.setText(event.getHost());
            holder.description.setText(event.getLocation());
            holder.icon.setImageBitmap(null);
            holder.mainContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if(event.getIconUrl() != null) {
                        Picasso.with(v.getContext()).load(event.getIconUrl())
                                .resize(holder.icon.getWidth(), holder.icon.getHeight()).centerInside()
                                .into(holder.icon);
                        holder.mainContainer.removeOnLayoutChangeListener(this);
                    }
                }
            });
            //RecyclerViews don't use onItemClickListeners(...); set on the largest view instead
            holder.mainContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent hackathonActivity = new Intent(v.getContext(), HackathonActivity.class);
                    hackathonActivity.putExtra(HackathonActivity.EXTRA_HACKATHON_RESPONSE_STR, new Gson().toJson(event));
                    startActivity(hackathonActivity);
                }
            });
        }

        @Override
        public int getItemCount() {
            return events.size() + 1; //For end of list at end
        }

        public void setEndOfResultsStringResource(int resource){
            endOfResultsStringRes = resource;
        }
    }

    public EventListingFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static EventListingFragment newInstance(String title, int mode){
        EventListingFragment fragment = new EventListingFragment();

        Bundle args = new Bundle();
        args.putString(TITLE_STRING_ARGUMENT, title);
        args.putInt(LISTING_MODE_ARGUMENT, mode);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.title = args.getString(TITLE_STRING_ARGUMENT);
        this.mode = args.getInt(LISTING_MODE_ARGUMENT);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        host = (FragmentHostInterface) activity;
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
        adapter.setEndOfResultsStringResource(R.string.loading_hackathons);
        adapter.notifyDataSetChanged();

        if(mode == LISTING_MODE_NEARBY_HACKS){
            GregorianCalendar aWeekAgo = new GregorianCalendar();
            aWeekAgo.roll(Calendar.WEEK_OF_YEAR, -1);
            GregorianCalendar aYearForward = new GregorianCalendar();
            aYearForward.roll(Calendar.YEAR, 1);
            Pair<Double, Double> location = host.getLocation();
            if(location == null) location = new Pair<>(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE);

            Log.d(TAG, "Type: " + Utilities.getCredentials(getContext()).getType());
            Log.d(TAG, "ID token: " + Utilities.getCredentials(getContext()).getIdToken());

            ApiFactory.getInstance().listHackathons(
                    Utilities.API_DATE_FORMAT.format(aWeekAgo.getTime()),
                    Utilities.API_DATE_FORMAT.format(aYearForward.getTime()),
                    String.valueOf(location.first),
                    String.valueOf(location.second),
                    "distance",
                    new Callback<HackathonResponse[]>() {
                        @Override
                        public void success(HackathonResponse[] hackathonResponses, Response response) {
                            Log.d(TAG, "Success!");
                            events.clear();
                            for(HackathonResponse r : hackathonResponses){
                                events.add(r);
                            }
                            adapter.setEndOfResultsStringResource(R.string.end_of_results);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(TAG, "Failed to load!", error);
                            adapter.setEndOfResultsStringResource(R.string.end_of_results);
                            adapter.notifyDataSetChanged();
                        }
                    });
        } else if (mode == LISTING_MODE_FAVORITE_HACKS){
            adapter.setEndOfResultsStringResource(R.string.end_of_results);
            List<HackathonResponse> responses = HackCache.getInstance().getFavoriteHackathons();
            events.addAll(responses);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getNameResource(Context context) {
        return title;
    }

    @Override
    public boolean onToolbarItemSelected(int itemId) {
        return false;
    }
}