package edu.oakland.mysale.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import edu.oakland.mysale.R;
import edu.oakland.mysale.adapters.CouponListAdapter;
import edu.oakland.mysale.utils.GpsLocationHandler;
import go.gosale.Gosale;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @ViewById(R.id.list)
    ListView list;

    private GpsLocationHandler locationHandler;
    static List<List<Gosale.CouponsByLocation>> coupons;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        locationHandler = new GpsLocationHandler(this);
    }

    public void getStuff() {
        List<List<Gosale.CouponsByLocation>> coupons = GpsLocationHandler.getCoupons();

        CouponListAdapter adapter = new CouponListAdapter(
                MainActivity.this,
                list,
                R.layout.header_card,
                R.layout.info_card,
                coupons
        );

        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
