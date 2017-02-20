package com.osh.hackathonbrowser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.Delegation;
import com.auth0.android.result.UserProfile;
import com.google.gson.Gson;
import com.osh.hackathonbrowser.api.ApiFactory;
import com.squareup.okhttp.internal.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utilities {
    public static final SimpleDateFormat API_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static boolean isLollipopOrNewer(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void loadUrlIntoImageView(final Context context, final ImageView view, final String url){
        if(view.getWidth() > 0 && view.getHeight() > 0) //Has been laid out?
            Picasso.with(context).load(url).resize(view.getWidth(), view.getHeight()).centerInside().into(view);
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Picasso.with(context).load(url).resize(view.getWidth(), view.getHeight()).centerInside().into(view);
                view.removeOnLayoutChangeListener(this);
            }
        });
    }

    public interface PaletteListener {
        void onColorFound(int color);
    }

    public static void loadUrlIntoImageViewWithBg(final Context context, final ImageView view, final View bg, final String url, final PaletteListener listener){
        if(view.getWidth() > 0 && view.getHeight() > 0) //Has been laid out?
            Picasso.with(context).load(url).resize(view.getWidth(), view.getHeight()).centerInside().into(view);
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Picasso.with(context).load(url).resize(view.getWidth(), view.getHeight()).centerInside().into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(context).load(url).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                //The Bitmap was likely cached so this is cheap
                                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        int color = Color.BLACK;
                                        if(palette.getDarkVibrantSwatch() != null)
                                            color = palette.getDarkVibrantSwatch().getRgb();
                                        else if(palette.getLightVibrantSwatch() != null)
                                            color = palette.getLightVibrantSwatch().getRgb();
                                        else if(palette.getDarkMutedSwatch() != null)
                                            color = palette.getDarkMutedSwatch().getRgb();
                                        else if(palette.getLightMutedSwatch() != null)
                                            color = palette.getLightMutedSwatch().getRgb();

                                        bg.setBackgroundColor(color);
                                        listener.onColorFound(color);
                                    }
                                });
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                });
                view.removeOnLayoutChangeListener(this);
            }
        });
    }

    public static int createDarkColorVariant(int color){
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        r = (int) Math.max(r * 0.8, 0);
        g = (int) Math.max(g * 0.8, 0);
        b = (int) Math.max(b * 0.8, 0);

        return Color.rgb(r, g, b);
    }

    public static Auth0 getAuthZero(Context context){
        return new Auth0(context.getString(R.string.auth0_client_id), context.getString(R.string.auth0_domain));
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

    public static void clearCredentials(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(Constants.Prefs.CREDS_STRING)
                .commit();
    }

    public interface OnCredentialValidateListener {
        void onSuccess(String name, String email, String profileImage);
        void onFailure();
    }

    public static void validateCredentials(final Context ctx, final OnCredentialValidateListener listener){
        final AuthenticationAPIClient aac = new AuthenticationAPIClient(Utilities.getAuthZero(ctx));
        final Credentials creds = Utilities.getCredentials(ApplicationClass.getAppContext());

        aac.tokenInfo(creds.getIdToken()).start(new BaseCallback<UserProfile, AuthenticationException>() {
            @Override
            public void onSuccess(UserProfile payload) {
                String name = payload.getName();
                String email = payload.getEmail();
                String imageUrl = payload.getPictureURL();

                listener.onSuccess(name, email, imageUrl);
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

                        Utilities.storeCredentials(ApplicationClass.getAppContext(), new Credentials(token,
                                creds.getAccessToken(),
                                type,
                                creds.getRefreshToken()));
                        ApiFactory.checkForToken();

                        validateCredentials(ctx, listener);
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        Utilities.clearCredentials(ApplicationClass.getAppContext());
                        ApiFactory.checkForToken();
                        listener.onFailure();
                    }
                });
            }
        });
    }
}