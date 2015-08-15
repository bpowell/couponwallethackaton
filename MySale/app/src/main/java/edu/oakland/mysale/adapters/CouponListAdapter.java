package edu.oakland.mysale.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import edu.oakland.mysale.R;
import edu.oakland.mysale.layouts.CouponHeaderLayout;
import go.gosale.Gosale;

public class CouponListAdapter extends ArrayAdapter<List<Gosale.CouponsByLocation>> {

    private final Context context;
    private final ListView list;
    private final List<List<Gosale.CouponsByLocation>> locations;
    private final LayoutInflater inflater;
    private final int resourceId;

    public CouponListAdapter(Context context, ListView list, int resourceId, List<List<Gosale.CouponsByLocation>> locations) {
        super(context, resourceId, locations);
        this.context = context;
        this.list = list;
        this.resourceId = resourceId;
        this.locations = locations;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell = convertView;
        CouponHeaderLayout couponLayout = null;
        CouponDetailsListAdapter detailsListAdapter = null;

        if (cell == null) {
            couponLayout = new CouponHeaderLayout();

            cell = inflater.inflate(resourceId, parent, false);

            couponLayout.headerCard = (CardView) cell.findViewById(R.id.header_card);
            couponLayout.cardImage = (ImageView) cell.findViewById(R.id.card_image);
            couponLayout.cardTitle = (TextView) cell.findViewById(R.id.card_title);
            couponLayout.locationCoupons = (ListView) cell.findViewById(R.id.coupon_list);

            detailsListAdapter = new CouponDetailsListAdapter(context, couponLayout.locationCoupons, R.layout.info_card, couponLayout, locations);
            couponLayout.locationCoupons.setAdapter(detailsListAdapter);

            cell.setTag(couponLayout);

        } else {
            couponLayout = (CouponHeaderLayout) cell.getTag();
        }

        return cell;
    }

}
