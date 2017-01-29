package com.osh.hackathonbrowser;

import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import com.auth0.android.Auth0;
import com.auth0.android.result.Credentials;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class Utilities {
    public static boolean isLollipopOrNewer(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void loadUrlIntoImageView(final Context context, final ImageView view, final String url){
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Picasso.with(context).load(url).resize(view.getWidth(), view.getHeight()).centerInside().into(view);
                view.removeOnLayoutChangeListener(this);
            }
        });
    }

    public static Auth0 getAuthZero(){
        return new Auth0("vIZ8UrlA4vByjltC1mmeTTjGCJzOokEL", "wolf-beacon.auth0.com");
    }

    public static Credentials getCredentials(Context context){
        String jsonifiedCreds = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.Prefs.CREDS_STRING, null);
        return jsonifiedCreds == null ? null : new Gson().fromJson(jsonifiedCreds, Credentials.class);
    }

    public static void storeCredentials(Context context, Credentials creds){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(Constants.Prefs.CREDS_STRING, new Gson().toJson(creds))
                .commit();
    }
}
