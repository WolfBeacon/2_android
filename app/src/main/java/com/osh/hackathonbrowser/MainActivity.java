package com.osh.hackathonbrowser;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.PermissionChecker;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.PasswordlessLock;
import com.auth0.android.lock.internal.configuration.Theme;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.Delegation;
import com.auth0.android.result.UserProfile;
import com.squareup.okhttp.internal.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.id.toggle;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentHostInterface {
    private static final String TAG = "MainActivity";

    private static final int LOCATION_REQUEST_CODE = 501;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    ActionBarDrawerToggle toggle;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    Lock lock;
    LockCallback lockCallback;

    BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup main UI
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigationView.setNavigationItemSelectedListener(this);
        //Replace with first fragment
        replaceFragment(R.id.explore_tab);

        Auth0 auth0 = Utilities.getAuthZero(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //Handles dispatch to fragments, if ever needed

        switch (requestCode){
            case LOCATION_REQUEST_CODE: //Nothing to actually do here.
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Credentials creds = Utilities.getCredentials(this);
        if(creds == null){
            startActivity(lock.newIntent(this));
        } else {
            validateCredentials();
        }
    }

    private void validateCredentials() {
        Utilities.validateCredentials(this, new Utilities.OnCredentialValidateListener() {
            @Override
            public void onSuccess(String name, String email, String profileImage) {
                onAuthenticated(name, email, profileImage);
            }

            @Override
            public void onFailure() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, R.string.sign_in_error, Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(lock.newIntent(MainActivity.this));
            }
        });
    }

    private void onAuthenticated(final String name, final String email, final String imageUrl) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(imageUrl != null && !imageUrl.isEmpty())
                    Utilities.loadUrlIntoImageView(MainActivity.this, (ImageView) navigationView.findViewById(R.id.profile_image), imageUrl);
                if(email != null && !email.isEmpty())
                    ((TextView) navigationView.findViewById(R.id.profile_email)).setText(email);
                else
                    navigationView.findViewById(R.id.profile_email).setVisibility(View.GONE);
                if(name != null && !name.isEmpty())
                    ((TextView) navigationView.findViewById(R.id.profile_name)).setText(name);
                else
                    navigationView.findViewById(R.id.profile_name ).setVisibility(View.GONE);
                navigationView.findViewById(R.id.nav_header).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.logout)
                                .setMessage(R.string.would_you_like_sign_out)
                                .setNegativeButton(R.string.cancel, null)
                                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Utilities.clearCredentials(MainActivity.this);
                                        startActivity(lock.newIntent(MainActivity.this));
                                    }
                                })
                                .show();
                    }
                });
            }
        });
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
            case R.id.today_tab:
            case R.id.explore_tab:
                if(!item.isChecked()) replaceFragment(item.getItemId());
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        closeDrawer();
        return true;
    }

    public void replaceFragment(int itemId){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch(itemId) {
            case R.id.explore_tab:
                navigationView.getMenu().findItem(R.id.explore_tab).setChecked(true);
                currentFragment = ShowcaseFragment.newInstance();
                ft.replace(R.id.fragment_container, currentFragment);
                break;
            case R.id.today_tab:
                navigationView.getMenu().findItem(R.id.today_tab).setChecked(true);
                currentFragment = DummyFragment.newInstance();
                ft.replace(R.id.fragment_container, currentFragment);
                break;
        }
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

    @Override
    public DrawerLayout getDrawerLayout() {
        return drawer;
    }

    @Override
    @TargetApi(23)
    public Pair<Double, Double> getLocation() {
        if(PermissionChecker.checkCallingOrSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PermissionChecker.PERMISSION_GRANTED){
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria c = new Criteria();
            c.setAccuracy(Criteria.ACCURACY_COARSE);
            String locationProvider = lm.getBestProvider(c, true);

            Location l = lm.getLastKnownLocation(locationProvider);
            if(l != null) return new Pair<>(l.getLatitude(), l.getLongitude());
        } else { //Request location permission
            Toast.makeText(this, R.string.location_permission_expl, Toast.LENGTH_SHORT).show();
            requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, LOCATION_REQUEST_CODE);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(menuResourceId != -1) getMenuInflater().inflate(menuResourceId, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item) ||
                (currentFragment != null && currentFragment.onToolbarItemSelected(item.getItemId()));
    }

    int menuResourceId = -1;

    @Override
    public void attachNewToolbar(Toolbar toolbar, int menuResId) {
        //Ugh.
        if(toggle != null){
            drawer.removeDrawerListener(toggle);
        }

        menuResourceId = menuResId;
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();
        toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    }
}