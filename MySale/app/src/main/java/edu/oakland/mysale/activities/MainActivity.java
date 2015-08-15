package edu.oakland.mysale.activities;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.EActivity;

import edu.oakland.mysale.R;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            go.couponsbylocation.Couponsbylocation.This c = go.couponsbylocation.Couponsbylocation.Init(42.62, -83.02, 1.5);
            Log.d("TESTING", String.valueOf(c.Size()));
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
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
