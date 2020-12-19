package com.example.spontan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MyLocationListener implements LocationListener {

    private Location location;
    private boolean canGetLocation = false;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 200;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    LocationManager locationManager;
    Context mContext;
    Activity mActivity;

    public MyLocationListener(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        location = new Location("initial");
        location.setLatitude(0);
        location.setLongitude(0);
        getLoc();
    }

    public void getLoc() {
        try{
            // get GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.v("isGPSEnabled", "=" + isGPSEnabled);
            // get network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

            if (isGPSEnabled == false && isNetworkEnabled == false) {

            } else {
                // use network location provider or gps based on battery level & connectivity
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    getLocFromNetwork();
                }
                if (isGPSEnabled) {
                    getLocFromGPS();
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @SuppressLint("MissingPermission")
    public void getLocFromGPS() {
        checkPermissions();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Log.d("GPS Enabled", "GPS Enabled");
        if(locationManager!=null){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null){
                this.location = location;
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocFromNetwork() {
        checkPermissions();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Log.d("Network", "Network");
        if(locationManager!=null){
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null){
                this.location = location;
            }
        }
    }

    public void checkPermissions() {
        //Checks if permission is NOT granted...
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //If not, request location permission...
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    public double getLatitude() {
        return this.location.getLatitude();
    }

    public double getLongitude() {
        return this.location.getLongitude();
    }

    public boolean isCanGetLocation() {
        return canGetLocation;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }


}
