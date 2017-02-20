package com.osh.hackathonbrowser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.osh.hackathonbrowser.api.response.HackathonResponse;
import com.osh.hackathonbrowser.cache.HackCache;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An Activity for browsing the overall details of a {@linkplain HackathonResponse}.
 */
public class HackathonActivity extends AppCompatActivity {
    public static final String TAG = "HackathonActivity";

    public static final String EXTRA_HACKATHON_RESPONSE_STR = "hackathon_response";

    //TODO: When the API begins to return more data, re-integrate the adapters previously built below.
//
//    public class CardViewHolder extends RecyclerView.ViewHolder {
//        public TextView title;
//        public ImageView cardBack;
//
//        public CardViewHolder(View itemView) {
//            super(itemView);
//            this.title = findById(itemView, R.id.card_title);
//            this.cardBack = findById(itemView, R.id.card_image);
//        }
//    }
//
//    /**
//     * An {@linkplain RecyclerView} adapter for displaying {@linkplain FakeSponsor}s.
//     */
//    public class SponsorAdapter extends RecyclerView.Adapter<CardViewHolder> {
//        private List<FakeSponsor> sponsors;
//
//        public SponsorAdapter(List<FakeSponsor> sponsors){
//            this.sponsors = sponsors;
//        }
//
//        @Override
//        public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_card_view, parent, false);
//            return new CardViewHolder(v);
//        }
//
//        @Override
//        public void onBindViewHolder(final CardViewHolder holder, int position) {
//            final FakeSponsor sponsor = sponsors.get(position);
//
//            holder.cardBack.setImageDrawable(null); //Clear of old images; will take time to load so old ones might be visible
//            Utilities.loadUrlIntoImageView(HackathonActivity.this, holder.cardBack, sponsor.getLogoUrl());
//
//            holder.title.setText(sponsor.getName());
//        }
//
//        @Override
//        public int getItemCount() {
//            return sponsors.size();
//        }
//    }
//
//    /**
//     * An {@linkplain RecyclerView} adapter for displaying {@linkplain FakeGuestSpeaker}s.
//     */
//    public class SpeakerAdapter extends RecyclerView.Adapter<CardViewHolder> {
//        private List<FakeGuestSpeaker> speakers;
//
//        public SpeakerAdapter(List<FakeGuestSpeaker> speakers){
//            this.speakers = speakers;
//        }
//
//        @Override
//        public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_card_view, parent, false);
//            return new CardViewHolder(v);
//        }
//
//        @Override
//        public void onBindViewHolder(final CardViewHolder holder, int position) {
//            final FakeGuestSpeaker speaker = speakers.get(position);
//
//            holder.cardBack.setImageDrawable(null); //Clear of old images; will take time to load so old ones might be visible
//            Utilities.loadUrlIntoImageView(HackathonActivity.this, holder.cardBack, speaker.getImageUrl());
//
//            holder.title.setText(speaker.getName());
//        }
//
//        @Override
//        public int getItemCount() {
//            return speakers.size();
//        }
//    }
//
//    /**
//     * An {@linkplain RecyclerView} adapter for displaying {@linkplain FakePrize}s.
//     */
//    public class PrizesAdapter extends RecyclerView.Adapter<CardViewHolder> {
//        private List<FakePrize> prizes;
//
//        public PrizesAdapter(List<FakePrize> prizes){
//            this.prizes = prizes;
//        }
//
//        @Override
//        public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_card_view, parent, false);
//            return new CardViewHolder(v);
//        }
//
//        @Override
//        public void onBindViewHolder(final CardViewHolder holder, int position) {
//            final FakePrize prize = prizes.get(position);
//
//            holder.cardBack.setImageDrawable(null); //Clear of old images; will take time to load so old ones might be visible
//            Utilities.loadUrlIntoImageView(HackathonActivity.this, holder.cardBack, prize.getImageUrl());
//
//            holder.title.setText(prize.getTitle());
//        }
//
//        @Override
//        public int getItemCount() {
//            return prizes.size();
//        }
//    }
//
//    /**
//     * An {@linkplain RecyclerView} adapter for displaying {@linkplain FakeMap}s.
//     */
//    public class MapsAdapter extends PagerAdapter {
//        private List<FakeMap> maps;
//
//        public MapsAdapter(List<FakeMap> maps){
//            this.maps = maps;
//        }
//
//        @Override
//        public Object instantiateItem(final ViewGroup container, final int position) {
//            View layout = LayoutInflater.from(container.getContext()).inflate(R.layout.showcase_view, container, false);
//
//            final ImageView showcaseImage = findById(layout, R.id.showcase_image);
//            TextView showcaseTitle = findById(layout, R.id.showcase_title);
//            TextView showcaseSubText = findById(layout, R.id.showcase_subtext);
//            ((ViewPager) container).addView(layout);
//
//            showcaseTitle.setText(maps.get(position).getTitle());
//            showcaseSubText.setText(maps.get(position).getDescription());
//            layout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//                @Override
//                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                    Picasso.with(container.getContext()).load(maps.get(position).getMapImage())
//                            .resize(container.getWidth(), container.getHeight()).centerCrop()
//                            .transform(new ColorFilterTransformation(0x40000000)).into(showcaseImage);
//                }
//            });
//            return layout;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            //Remove the item from the View
//            ((ViewPager) container).removeView(((View) object));
//        }
//
//        @Override
//        public int getCount() {
//            return maps.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    @BindView(R.id.starHackathonFab)
    FloatingActionButton starHackathonFab;

    @BindView(R.id.apply_button)
    Button applyButton;

    @BindView(R.id.hackathon_icon)
    ImageView hackathonIcon;

    @BindView(R.id.hackathon_background)
    ImageView hackathonBackground;

    @BindView(R.id.short_name)
    TextView shortNameTextView;

    @BindView(R.id.campus)
    TextView campusTextView;

    @BindView(R.id.time)
    TextView timeTextView;

    @BindView(R.id.description)
    TextView descriptionTextView;

    @BindView(R.id.travel_reimbursement_box)
    CheckBox travelReimbursementBox;

    @BindView(R.id.prizes_box)
    CheckBox prizesBox;

//    @BindView(R.id.runtime_elements)
//    LinearLayout runtimeLayout;
//
//    @BindView(R.id.maps_viewpager)
//    ViewPager mapsViewpager;
//
//    @BindView(R.id.maps_viewpager_indicator)
//    DotsPagerIndicator mapsIndicator;

//    @BindView(R.id.webiste_link)
//    TextView websiteLink;

    @BindView(R.id.facebook_link)
    TextView facebookLink;

    @BindView(R.id.google_link)
    TextView googleLink;

    @BindView(R.id.twitter_link)
    TextView twitterLink;

//    @BindView(R.id.linkedin_link)
//    TextView linkedInLink;

    final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("h:mm aa", Locale.US);
    HackathonResponse mHackathon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hackathon);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra(EXTRA_HACKATHON_RESPONSE_STR)){
            mHackathon = new Gson().fromJson(getIntent().getStringExtra(EXTRA_HACKATHON_RESPONSE_STR), HackathonResponse.class);
            populateData();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Populate with data from fake hackathon
        //We inject most of the views on-the-fly by inflating layouts because we don't always know what fields will be present
        populateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hackathon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_schedule:
                startActivity(new Intent(this, ScheduleActivity.class));
            default:
                return false;
        }
    }

    private void populateData() {
        Log.d(TAG, "Populating data...");

        getSupportActionBar().setTitle(mHackathon.getTitle());

        //Header
        Utilities.loadUrlIntoImageViewWithBg(this, hackathonIcon, hackathonBackground, mHackathon.getIconUrl(), new Utilities.PaletteListener() {
            @Override
            public void onColorFound(int color) {
                if(Utilities.isLollipopOrNewer()) getWindow().setStatusBarColor(Utilities.createDarkColorVariant(color));
                toolbar.setBackgroundColor(color);
                toolbarLayout.setContentScrimColor(color);
            }
        });
        shortNameTextView.setText(mHackathon.getTitle());
        campusTextView.setText(mHackathon.getHost());
        /*
        timeTextView.setText(
                getString(
                    R.string.start_end_time,
                    TIME_FORMAT.format(new Date(mHackathon.getStartDate())),
                    TIME_FORMAT.format(new Date(mHackathon.getEndDate()))));
                    */
        descriptionTextView.setText(mHackathon.getNotes());

        travelReimbursementBox.setChecked(mHackathon.isTravelProvided());
        prizesBox.setChecked(mHackathon.arePrizesProvided());

        //Miscellaneous (Facebook, LinkedIn, Twitter, website)
        attachLink(facebookLink, mHackathon.getFacebookLink());
        attachLink(twitterLink, mHackathon.getTwitterLink());
        attachLink(googleLink, mHackathon.getGooglePlusLink());

        if(HackCache.getInstance().isHackathonFavorited(mHackathon.getId())){
            starHackathonFab.setImageResource(R.drawable.ic_star_border_white_24dp);
        } else {
            starHackathonFab.setImageResource(R.drawable.ic_star_white_36dp);
        }
    }

    private void attachLink(TextView v, final String link){
        if(link != null) {
            v.setText(link);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewLink = new Intent(Intent.ACTION_VIEW);
                    viewLink.setData(Uri.parse(link));
                    startActivity(viewLink);
                }
            });
        } else {
            v.setVisibility(View.GONE);
        }
    }

//    private RecyclerView createBigCardWrapper(ViewGroup containerHolder) {
//        View cardWrapper = LayoutInflater.from(containerHolder.getContext()).inflate(R.layout.collapsible_card_container_view, containerHolder, false);
//        containerHolder.addView(cardWrapper);
//        return (RecyclerView) cardWrapper.findViewById(R.id.card_container);
//    }
//
//    private RecyclerView createCardWrapper(String title, ViewGroup parent){
//        View cardWrapper = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_container_view, parent, false);
//        ((TextView) cardWrapper.findViewById(R.id.card_container_title)).setText(title);
//        parent.addView(cardWrapper);
//        return (RecyclerView) cardWrapper.findViewById(R.id.card_container);
//    }
//
//    private ViewGroup addCollapsibleLayout(String title, ViewGroup parent){
//        final View collapseLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.collapsible_view, parent, false);
//        collapseLayout.setTag(true); //tag = visibility
//
//        ((TextView) collapseLayout.findViewById(R.id.collapse_title)).setText(title);
//
//        final ImageView collapseIndicator = (ImageView) collapseLayout.findViewById(R.id.collapse_indicator);
//        collapseIndicator.setImageResource(R.drawable.ic_expand_less_black_36dp);
//
//        final FrameLayout container = (FrameLayout) collapseLayout.findViewById(R.id.collapse_content);
//        collapseLayout.findViewById(R.id.collapse_bar).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Boolean visibility = (Boolean) collapseLayout.getTag();
//                visibility = !visibility;
//                collapseLayout.setTag(visibility);
//                collapseIndicator.setImageResource(visibility ? R.drawable.ic_expand_less_black_36dp : R.drawable.ic_expand_more_black_36dp);
//                container.setVisibility(visibility ? View.VISIBLE : View.GONE);
//            }
//        });
//
//        parent.addView(collapseLayout);
//        return container;
//    }

    @OnClick(R.id.starHackathonFab)
    public void onStarHackathonClick(View view){
        if(HackCache.getInstance().isHackathonFavorited(mHackathon.getId())){
            //Unstar.
            HackCache.getInstance().unpinHackathon(mHackathon.getId(), false, true);
            Snackbar.make(view, R.string.hackathon_unfavorited, Snackbar.LENGTH_LONG).show();
            starHackathonFab.setImageResource(R.drawable.ic_star_white_36dp);
        } else {
            HackCache.getInstance().pinHackathon(mHackathon, false, true);
            Snackbar.make(view, R.string.hackathon_favorited, Snackbar.LENGTH_LONG).show();
            starHackathonFab.setImageResource(R.drawable.ic_star_border_white_24dp);

        }
    }
}