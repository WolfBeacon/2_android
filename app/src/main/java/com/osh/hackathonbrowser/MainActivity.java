package com.osh.hackathonbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.squareup.okhttp.internal.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentHostInterface {
    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    Credentials creds;
    Lock lock;
    LockCallback lockCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup main UI
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        //Replace with first fragment
        replaceFragment(-1);

        Auth0 auth0 = Utilities.getAuthZero();
        lockCallback = new AuthenticationCallback() {
            @Override
            public void onAuthentication(Credentials credentials) {
                Log.d(TAG, "Lock authenticated!");
                Utilities.storeCredentials(MainActivity.this, credentials);
            }

            @Override
            public void onCanceled() {
                Log.d(TAG, "Lock cancelled!");
                Toast.makeText(MainActivity.this, R.string.you_must_sign_in, Toast.LENGTH_SHORT).show();
                //Fine, onResume(...) will launch again
            }

            @Override
            public void onError(LockException error) {
                Log.e(TAG, "Lock exception!", error);
                finish();
            }
        };
        lock = Lock.newBuilder(auth0, lockCallback)
                .build(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        lock.onDestroy(this);
        lock = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        creds = Utilities.getCredentials(this);
        if(creds == null){
            startActivity(lock.newIntent(this));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        closeDrawer();
        return true;
    }

    public void replaceFragment(int itemId){
        //TODO: Actually implement replacement logic that works for more than just main page
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, ShowcaseFragment.newInstance());
        getSupportActionBar().hide();
        ft.commit();
    }

    @Override
    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }
}