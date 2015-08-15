package edu.oakland.mysale.adapters;

import android.content.Context;
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

import java.net.URLEncoder;
import java.util.List;

import edu.oakland.mysale.R;
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
                Log.d("hallelujeahjeu", businessInfo.getLogo());
                ImageProcessing.setImageFromUrl(couponLayout.cardImage, businessInfo.getLogo().replace(" ", "%21"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            couponLayout.cardTitle.setText(coupon.getBusinessName());

            View detailCell = inflater.inflate(R.layout.info_card, parent, false);
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
