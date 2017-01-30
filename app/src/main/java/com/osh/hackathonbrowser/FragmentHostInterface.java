package com.osh.hackathonbrowser;

import com.auth0.android.result.Credentials;

public interface FragmentHostInterface {
    void closeDrawer();
    void openDrawer();
    Credentials getCredentials();
}
