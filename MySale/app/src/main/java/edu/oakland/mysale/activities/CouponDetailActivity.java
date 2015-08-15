package edu.oakland.mysale.activities;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import edu.oakland.mysale.R;

@EActivity(R.layout.activity_coupon_detail)
public class CouponDetailActivity extends ActionBarActivity {
    @Extra
    String imageUrl;
    @Extra
    String companyName;
}
