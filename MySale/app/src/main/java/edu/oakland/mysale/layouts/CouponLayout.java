package edu.oakland.mysale.layouts;

import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Erik on 8/14/15.
 */
public class CouponLayout {
    public android.support.v7.widget.CardView headerCard;
    public RelativeLayout headerCardRelativeLayout;
    public LinearLayout cardHeader;
    public ImageView cardImage;
    public TextView cardTitle;

    public List<CardView> locationCoupons;
    public LinearLayout couponInfoLayout;
    public TextView couponDescription;
    public TextView couponDetail;
}
