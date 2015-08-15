package edu.oakland.mysale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import edu.oakland.mysale.R;
import edu.oakland.mysale.layouts.CouponDetailLayout;
import edu.oakland.mysale.layouts.CouponHeaderLayout;
import edu.oakland.mysale.utils.ImageProcessing;
import go.gosale.Gosale;

public class CouponDetailsListAdapter extends ArrayAdapter<List<Gosale.CouponsByLocation>> {

    private final Context context;
    private final ListView list;
    private final List<List<Gosale.CouponsByLocation>> locations;
    private final LayoutInflater inflater;
    private final int resourceId;
    private final CouponHeaderLayout couponHeader;

    public CouponDetailsListAdapter(Context context, ListView list, int resourceId, CouponHeaderLayout couponHeader, List<List<Gosale.CouponsByLocation>> locations) {
        super(context, resourceId, locations);
        this.context = context;
        this.list = list;
        this.resourceId = resourceId;
        this.locations = locations;
        this.couponHeader = couponHeader;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell = convertView;
        CouponDetailLayout couponDetailLayout = null;

        if (cell == null) {
            cell = inflater.inflate(resourceId, parent, false);
            couponDetailLayout = new CouponDetailLayout();

            for (List<Gosale.CouponsByLocation> coupons : locations) {
                boolean headerNotSet = true;
                for (Gosale.CouponsByLocation coupon : coupons) {

                    if (headerNotSet) {
                        ImageProcessing.setImageFromUrl(couponHeader.cardImage, coupon.getImage());
                        couponHeader.cardTitle.setText(coupon.getBusinessName());
                        headerNotSet = false;
                    }

                    couponDetailLayout.couponDescription = (TextView) cell.findViewById(R.id.card_description);
                    couponDetailLayout.couponDetails = (TextView) cell.findViewById(R.id.card_detail);

                    couponDetailLayout.couponDescription.setText(coupon.getDescription());
                    couponDetailLayout.couponDetails.setText(coupon.getDetails());

                    cell.setTag(couponDetailLayout);

                }

            }

        } else {
            couponDetailLayout = (CouponDetailLayout) cell.getTag();
        }

        return cell;
    }

}
