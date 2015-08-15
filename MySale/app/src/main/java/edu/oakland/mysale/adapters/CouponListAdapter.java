package edu.oakland.mysale.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import edu.oakland.mysale.R;
import edu.oakland.mysale.activities.CouponDetailActivity_;
import edu.oakland.mysale.activities.MainActivity;
import edu.oakland.mysale.layouts.CouponDetailLayout;
import edu.oakland.mysale.layouts.CouponHeaderLayout;
import edu.oakland.mysale.utils.ImageProcessing;
import go.gosale.Gosale;

public class CouponListAdapter extends ArrayAdapter<Gosale.CouponsByLocation> {

    private final Context context;
    private final ListView list;
    private final List<Gosale.CouponsByLocation> coupons;
    private final LayoutInflater inflater;
    private final int resourceId;

    public CouponListAdapter(Context context, ListView list, int resourceId, List<Gosale.CouponsByLocation> coupons) {
        super(context, resourceId, coupons);
        this.context = context;
        this.list = list;
        this.resourceId = resourceId;
        this.coupons = coupons;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell = convertView;
        CouponHeaderLayout couponLayout;
        String url = "";

        if (cell == null) {
            couponLayout = new CouponHeaderLayout();

            cell = inflater.inflate(resourceId, parent, false);

            couponLayout.headerCard = (CardView) cell.findViewById(R.id.header_card);
            couponLayout.cardImage = (ImageView) cell.findViewById(R.id.card_image);
            couponLayout.cardTitle = (TextView) cell.findViewById(R.id.card_title);
            couponLayout.locationCoupons = (LinearLayout) cell.findViewById(R.id.coupon_list);

            Gosale.CouponsByLocation coupon = coupons.get(position);
            try {
                Gosale.BusinessInfo businessInfo = Gosale.BusinessInfoInit(Integer.parseInt(coupon.getId_business_id()));
                url = businessInfo.getLogo().replace(" ", "%20");
                if(url.matches("^(http://)(?=.*http://).+$")) {
                    String[] split = url.split("http://");
                    url = "http://" + split[2];
                }

                final Bitmap[] b = new Bitmap[1];
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        b[0] = bitmap;
                    }
                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) { }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) { }
                };
                ImageProcessing.setImageFromUrl(couponLayout.cardImage, url, target);
            } catch (Exception e) {
                e.printStackTrace();
            }
            couponLayout.cardTitle.setText(coupon.getBusinessName());

            final View detailCell = inflater.inflate(R.layout.info_card, parent, false);
            final View tranCell = cell;
            final String tempUrl = url;
            final String tempName = coupon.getBusinessName();

            detailCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String imagerUrl = tempUrl;
                    String companyName = tempName;
                    Intent i = CouponDetailActivity_
                            .intent(context)
                            .imageUrl(imagerUrl)
                            .companyName(companyName)
                            .get();

                    View sharedView = tranCell;
                    String transitionName = context.getString(R.string.header_tran);

                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity)context, sharedView, transitionName);
                    context.startActivity(i, transitionActivityOptions.toBundle());
                }
            });

            CouponDetailLayout couponDetailLayout = new CouponDetailLayout();
            couponDetailLayout.couponDescription = (TextView) detailCell.findViewById(R.id.card_description);
            couponDetailLayout.couponDetails = (TextView) detailCell.findViewById(R.id.card_detail);

            couponDetailLayout.couponDescription.setText(coupon.getDescription());
            couponDetailLayout.couponDetails.setText(coupon.getDetails());

            couponLayout.locationCoupons.addView(detailCell);

            cell.setTag(couponLayout);

        } else {
            couponLayout = (CouponHeaderLayout) cell.getTag();
        }

        return cell;
    }

}
