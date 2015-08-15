package edu.oakland.mysale.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.oakland.mysale.activities.MainActivity;
import edu.oakland.mysale.activities.MainActivity_;
import go.gosale.Gosale;

/**
 * Created by Erik on 8/14/15.
 */
public class GpsLocationHandler implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Activity referenceActivity;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private static List<List<Gosale.CouponsByLocation>> branbarn = null;

    public GpsLocationHandler(Activity referenceActivity) {
        this.referenceActivity = referenceActivity;
        Log.i("Location(Erik)", "GPS Location created");
        buildGoogleApiClient(referenceActivity.getApplicationContext());
        createLocationRequest();
        googleApiClient.connect();
        this.referenceActivity = referenceActivity;
    }

    protected synchronized void buildGoogleApiClient(Context context) {
        Log.i("Location(Erik)", "Google Api Client created");
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        Log.i("Location(Erik)", "Location Request created");
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setMaxWaitTime(1000);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("Location(Erik)", "GPS Connected");
        if (locationRequest != null) {
            getLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Location(Erik)", "Connection Suspended");
        stopLocationUpdates();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("Location(Erik)", "Connection Failed");
        stopLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("Location(Erik)", location.getLatitude() + "," + location.getLongitude());
        try {
            HashMap<String, Integer> alreadyThere = new HashMap<>();
            go.gosale.Gosale.ThisCouponsByLocation c = go.gosale.Gosale.CouponsByLocationInit(location.getLatitude(), location.getLongitude(), 0.02);
            Log.d("TESTING", String.valueOf(c.Size()));
            branbarn = new ArrayList<>();
            for(int i=0; i<c.Size(); i++) {
                go.gosale.Gosale.CouponsByLocation a = c.Get(i);
                if(alreadyThere.containsKey(a.getId_business_id())) {
                    int position = alreadyThere.get(a.getId_business_id());
                    branbarn.get(position).add(a);
                } else {
                    int size = branbarn.size() + 1;
                    alreadyThere.put(a.getId_business_id(), size);
                    List<go.gosale.Gosale.CouponsByLocation> newlist = new ArrayList<>();
                    newlist.add(a);
                    branbarn.add(newlist);
                }
            }
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
        stopLocationUpdates();

        ((MainActivity) referenceActivity).getStuff();
    }

    private void getLocation() {
        Log.i("Location(Erik)", "Get Location");
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, this);
    }

    public void stopLocationUpdates() {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
    }

    public static List<List<Gosale.CouponsByLocation>> getCoupons() {
        return branbarn;
    }
}
