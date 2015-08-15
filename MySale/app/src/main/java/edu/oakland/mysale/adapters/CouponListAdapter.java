package edu.oakland.mysale.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.oakland.mysale.R;
import edu.oakland.mysale.layouts.CouponLayout;
import go.gosale.Gosale;

public class CouponListAdapter extends ArrayAdapter<List<Gosale.CouponsByLocation>> {

    private final Context context;
    private final ListView list;
    private final List<List<Gosale.CouponsByLocation>> locations;
    private final LayoutInflater inflater;
    private final int headerResourceId;
    private final int infoResourceId;

    public CouponListAdapter(Context context, ListView list, int headerResourceId, int infoResourceId, List<List<Gosale.CouponsByLocation>> locations) {
        super(context, headerResourceId, infoResourceId, locations);
        this.context = context;
        this.list = list;
        this.headerResourceId = headerResourceId;
        this.infoResourceId = infoResourceId;
        this.locations = locations;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell = convertView;
        CouponLayout couponLayout = null;

        if (cell == null) {
            cell = inflater.inflate(headerResourceId, parent, false);
            couponLayout = new CouponLayout();

            couponLayout.headerCard = (CardView) cell.findViewById(R.id.header_card);
            couponLayout.headerCardRelativeLayout = (RelativeLayout) cell.findViewById(R.id.header_layout);
            couponLayout.cardHeader = (LinearLayout) cell.findViewById(R.id.card_header);
            couponLayout.cardImage = (ImageView) cell.findViewById(R.id.card_image);
            couponLayout.cardTitle = (TextView) cell.findViewById(R.id.card_title);

            couponLayout.locationCoupons = new ArrayList<>();
            cell = inflater.inflate(infoResourceId, parent, false);
            for (List<Gosale.CouponsByLocation> location : locations) {
                for (Gosale.CouponsByLocation coupon : location) {

                    couponLayout.couponInfoLayout = (LinearLayout) cell.findViewById(R.id.card_info);
                    couponLayout.couponDescription = (TextView) cell.findViewById(R.id.card_description);
                    couponLayout.couponDetail = (TextView) cell.findViewById(R.id.card_detail);

                    couponLayout.couponDescription.setText(coupon.getDescription());
                    couponLayout.couponDetail.setText(coupon.getDetails());

                }
            }

        } else {
            couponLayout = (CouponLayout) cell.getTag();
        }

        return cell;
    }

}
