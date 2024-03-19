package com.example.gps_tracker;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.gps_tracker.dataclasses.DeviceInfo;
import com.example.gps_tracker.dataclasses.GeoPointWithSpeed;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;


public class LocationService extends Service {



    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private Context appContext;
    private String userId;




    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        userId = Hashes.getHash(getApplicationContext());
        // Initialize Firebase Firestore
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        updateOptions();
        createLocationCallback();
        requestLocationUpdates();
        //startForeground(NOTIFICATION_ID, buildNotification());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Service")
                .setContentText("Location service is running.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
    }*/

    public void updateOptions(){
        DeviceInfo deviceInfo = new DeviceInfo(getApplicationContext());
        deviceInfo.startWatching();
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                //Long timestamp = (Long) new Date().getTime();
                //DeviceInfo deviceInfo = new DeviceInfo(Build.BRAND + " " + Build.MODEL,87);
                //myRef.child("battery").setValue(String.valueOf(87));
                //myRef.child("aboutDevice").setValue(Build.BRAND + " " + Build.MODEL);
                for (Location location : locationResult.getLocations()) {
                    if (location.hasSpeed()) {
                        GeoPointWithSpeed geoPointWithSpeed = new GeoPointWithSpeed(Math.round(location.getSpeed() * 100.0) / 100.0f,new GeoPoint(location.getLatitude(),location.getLongitude()));
                        database.collection("users").document(userId).collection("track").add(geoPointWithSpeed.getMap());
                        //myRef.child("location").child(String.valueOf(timestamp)).setValue(location.getLatitude() + "," + location.getLongitude() + "," + location.getSpeed());
                    }
                    // showToast("New Location: " + location.getLatitude() + ", " + location.getLongitude());
                    // Handle the new location here
                }
            }
        };
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000); // Update interval in milliseconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions  more details.
            ActivityCompat.requestPermissions((Activity) appContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    /*private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}