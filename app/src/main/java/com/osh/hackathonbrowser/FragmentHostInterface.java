package com.osh.hackathonbrowser;

import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import com.auth0.android.result.Credentials;

public interface FragmentHostInterface {
    void closeDrawer();
    void openDrawer();

    DrawerLayout getDrawerLayout();

    Pair<Double, Double> getLocation();

    void attachNewToolbar(Toolbar toolbar, int menuResId);
}
