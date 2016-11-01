package com.osh.hackathonbrowser;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

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
}
