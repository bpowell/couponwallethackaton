package edu.oakland.mysale.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import edu.oakland.mysale.R;
import edu.oakland.mysale.utils.ImageProcessing;

@EActivity(R.layout.activity_coupon_detail)
public class CouponDetailActivity extends ActionBarActivity {
    @Extra
    String imageUrl;
    @Extra
    String companyName;
    @Extra
    String couponURL;

    @AfterViews
    public void init() {
        final ImageView thumb1View = (ImageView) findViewById(R.id.large_image);
        String url = couponURL.replace(" ", "%20");
                if(url.matches("^(http://)(?=.*http://).+$")) {
                    String[] split = url.split("http://");
                    url = "http://" + split[2];
                }
        ImageProcessing.setImageFromUrl(thumb1View, url);
        // Retrieve and cache the system's default "short" animation time
    }
}
