package com.osh.hackathonbrowser;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    boolean credsValidated = false;
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
        } else {
            //Validate credentials before performing API call to fetch hackathons
            validateCredentials();
        }
    }

    private void validateCredentials() {
        final AuthenticationAPIClient aac = new AuthenticationAPIClient(Utilities.getAuthZero());
        aac.tokenInfo(creds.getIdToken()).start(new BaseCallback<UserProfile, AuthenticationException>() {
            @Override
            public void onSuccess(UserProfile payload) {
                String name = payload.getName();
                String email = payload.getEmail();
                String imageUrl = payload.getPictureURL();

                onAuthenticated(name, email, imageUrl);
            }

            @Override
            public void onFailure(AuthenticationException error) {
                //ID invalid *or* network is down; recheck ID
                aac.delegationWithRefreshToken(creds.getRefreshToken()).start(new BaseCallback<Delegation, AuthenticationException>() {
                    @Override
                    public void onSuccess(Delegation payload) {
                        String token = payload.getIdToken();
                        long expiryDate = payload.getExpiresIn();
                        String type = payload.getType();

                        Utilities.storeCredentials(MainActivity.this, new Credentials(token,
                                creds.getAccessToken(),
                                type,
                                creds.getRefreshToken()));
                        validateCredentials();
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, R.string.sign_in_error, Toast.LENGTH_SHORT).show();
                            }
                        });

                        //ID + refresh token are dead; server revocation likely
                        //Clear creds + open lock
                        Utilities.clearCredentials(MainActivity.this);
                        startActivity(lock.newIntent(MainActivity.this));
                    }
                });
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

    @Override
    public Credentials getCredentials() {
        return credsValidated ? creds : null;
    }
}