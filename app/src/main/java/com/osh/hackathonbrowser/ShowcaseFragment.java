package com.osh.hackathonbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.osh.hackathonbrowser.model.PresentationSlide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;

import static butterknife.ButterKnife.findById;

public class ShowcaseFragment extends BaseFragment {
    public static final String TAG = "ShowcaseFragment";

    @BindView(R.id.events_viewpager)
    ViewPager eventsViewpager;

    @BindView(R.id.browse_events_tabs)
    TabLayout tabs;

    @BindView(R.id.browse_events_viewpager)
    ViewPager browseEventsViewpager;

    @BindView(R.id.show_menu)
    ImageView showMenuButton;

    @BindView(R.id.events_viewpager_indicator)
    DotsPagerIndicator eventsIndicator;

    FragmentHostInterface fhi;

    public class HackathonShowcaseAdapter extends PagerAdapter {
        private List<PresentationSlide> slides;

        public HackathonShowcaseAdapter(List<PresentationSlide> slides){
            this.slides = slides;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            View layout = LayoutInflater.from(container.getContext()).inflate(R.layout.showcase_view, container, false);

            final ImageView showcaseImage = findById(layout, R.id.showcase_image);
            TextView showcaseTitle = findById(layout, R.id.showcase_title);
            TextView showcaseSubText = findById(layout, R.id.showcase_subtext);
            ((ViewPager) container).addView(layout);

            showcaseTitle.setText(slides.get(position).getTitle());
            showcaseSubText.setText(slides.get(position).getSubtitle());
            layout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    Picasso.with(container.getContext()).load(slides.get(position).getImageUrl())
                            .resize(container.getWidth(), container.getHeight()).centerCrop()
                            .transform(new ColorFilterTransformation(0x80000000)).into(showcaseImage);
                }
            });
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(v.getContext(), HackathonActivity.class));
                }
            });

            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //Remove the item from the View
            ((ViewPager) container).removeView(((View) object));
        }

        @Override
        public int getCount() {
            return slides.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public class EventListingsAdapter extends FragmentStatePagerAdapter {
        BaseFragment allEvents;
        BaseFragment favorites;

        public EventListingsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment toReturn;
            switch (position){
                case 0:
                    toReturn = allEvents == null ? allEvents = EventListingFragment.newInstance(getString(R.string.all_events)) : allEvents;
                    break;
                case 1:
                    toReturn = favorites == null ? favorites = EventListingFragment.newInstance(getString(R.string.favorites)) : favorites;
                    break;
                default:
                    toReturn = EventListingFragment.newInstance("Default");
            }
            return toReturn;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ((BaseFragment) getItem(position)).getNameResource(getContext());
        }
    }

    public static ShowcaseFragment newInstance(){
        ShowcaseFragment sf = new ShowcaseFragment();
        return sf;
    }

    public ShowcaseFragment(){
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fhi = (FragmentHostInterface) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_showcase, container, false);
        ButterKnife.bind(this, v);

        List<PresentationSlide> slides = new ArrayList<>();
        slides.add(new PresentationSlide("Sample Hackathon Name", "Location of it", "https://d24wuq6o951i2g.cloudfront.net/img/events/id/197/1979732/assets/5f6.hackathon.jpg"));
        slides.add(new PresentationSlide("Sample Hackathon #2", "Location of this one", "http://rack.2.mshcdn.com/media/ZgkyMDEzLzExLzAxL2NhL2xhdW5jaDIuMDNiZTcucG5nCnAJdGh1bWIJMTIwMHg2MjcjCmUJanBn/ff51ee1e/244/launch2.jpg"));
        eventsViewpager.setAdapter(new HackathonShowcaseAdapter(slides));
        eventsIndicator.attachViewPager(eventsViewpager);

        browseEventsViewpager.setAdapter(new EventListingsAdapter(getChildFragmentManager()));
        tabs.setupWithViewPager(browseEventsViewpager);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick(R.id.show_drawer)
    public void clickShowDrawer(){
        fhi.openDrawer();
    }

    @OnClick(R.id.show_menu)
    public void clickShowMenu(){
        PopupMenu pm = new PopupMenu(getActivity(), showMenuButton, Gravity.BOTTOM);
        pm.inflate(R.menu.fragment_showcase);
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        pm.show();
    }

    @Override
    public String getNameResource(Context context) {
        return getString(R.string.showcase_fragment);
    }
}
