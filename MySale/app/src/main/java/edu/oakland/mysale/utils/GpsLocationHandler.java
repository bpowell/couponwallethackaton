package edu.oakland.mysale.utils;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import go.gosale.Gosale;

/**
 * Created by Erik on 8/14/15.
 */
public class GpsLocationHandler implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private List<Gosale.CouponsByLocation> branbarn = null;

    public GpsLocationHandler(Context context) {
        Log.i("Location(Erik)", "GPS Location created");
        buildGoogleApiClient(context);
        createLocationRequest();
        googleApiClient.connect();
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
            go.gosale.Gosale.ThisCouponsByLocation c = go.gosale.Gosale.CouponsByLocationInit(42.63, -80.02, 1.5);
            Log.d("TESTING", String.valueOf(c.Size()));
            branbarn = new ArrayList<>();
            for(int i=0; i<c.Size(); i++) {
                branbarn.add(c.Get(i));
            }
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
        stopLocationUpdates();
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

    public List<Gosale.CouponsByLocation> getCoupons() {
        return branbarn;
    }
}
