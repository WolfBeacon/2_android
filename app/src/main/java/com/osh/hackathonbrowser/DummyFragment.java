package com.osh.hackathonbrowser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public class DummyFragment extends BaseFragment {
    public static final String TAG = "DummyFragment";

    FragmentHostInterface fhi;

    public static DummyFragment newInstance(){
        DummyFragment df = new DummyFragment();
        return df;
    }

    public DummyFragment(){
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
        View v = inflater.inflate(R.layout.fragment_dummy, container, false);
        ButterKnife.bind(this, v);

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

    @Override
    public String getNameResource(Context context) {
        return getString(R.string.dummy_fragment);
    }

    @Override
    public boolean onToolbarItemSelected(int itemId) {
        return false;
    }
}
