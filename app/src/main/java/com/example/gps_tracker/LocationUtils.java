package com.example.gps_tracker;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

public class LocationUtils {

    public static void openLocationSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag
        context.startActivity(intent);
    }

    /*public static boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }*/

}