package com.example.gps_tracker;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.gps_tracker.constants.ServerActions;
import com.example.gps_tracker.dataclasses.DefaultAppInfo;
import com.example.gps_tracker.dataclasses.DeviceInfo;
import com.example.gps_tracker.dataclasses.GeoPointWithSpeed;
import com.example.gps_tracker.dataclasses.ServerAction;
import com.example.gps_tracker.helpers.ImageUtils;
import com.example.gps_tracker.managers.FlashlightManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LocationService extends Service {


    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private Context appContext;
    private String userId;
    private static final String CHANNEL_ID_TRACKER = "tracker_channel";
    private static final int NOTIFICATION_ID_TRACKER = 1001;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        userId = Hashes.getHash(getApplicationContext());
        // Initialize Firebase Firestore

        //startForeground(NOTIFICATION_ID, buildNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
// Create the notification channel (for Android Oreo and higher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Tracker Channel";
            String description = "Notifications for tracker service";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_TRACKER, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        updateOptions();
        listenToServerEvents();
        createLocationCallback();
        requestLocationUpdates();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID_TRACKER, buildNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
        }
        else{
            startForeground(NOTIFICATION_ID_TRACKER, buildNotification());
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID_TRACKER)
                .setContentTitle("Location Service")
                .setContentText("Location service is running...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
    }

    public void listenToServerEvents(){
        database.collection("users").document(userId).collection("server").addSnapshotListener((queryDocumentSnapshots, error) -> {
            if(error != null){
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                for (DocumentChange document : queryDocumentSnapshots.getDocumentChanges()) {
                    ServerAction serverAction = document.getDocument().toObject(ServerAction.class);
                    Toast.makeText(appContext,serverAction.toString(),Toast.LENGTH_SHORT).show();
                    ServerActions.manageAction(new FlashlightManager(appContext),serverAction);
                   //serverAction.getAction_type()

                    //Toast.makeText(appContext,document.getDocument().toString(),Toast.LENGTH_SHORT).show();
                     //document.getDocument().toObject();
                }
            }
        });
    }


    private List<DefaultAppInfo> retrieveDefaultApps(String action) {
        List<DefaultAppInfo> defaultAppsList = new ArrayList<>();
        List<android.content.pm.PackageInfo> resolveInfoList = getPackageManager().getInstalledPackages(0);
        for (android.content.pm.PackageInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.applicationInfo.packageName;
            String appName = resolveInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            Bitmap  appIcon = ImageUtils.drawableToBitmap(resolveInfo.applicationInfo.loadIcon(getPackageManager()));//.getBitmap();
            DefaultAppInfo appInfo = new DefaultAppInfo(packageName, appName, appIcon);
            defaultAppsList.add(appInfo);
        }
        return defaultAppsList;
    }

    private void storeDefaultAppsInFirebase(List<DefaultAppInfo> defaultAppsList) {
        for (DefaultAppInfo appInfo : defaultAppsList) {
            // Convert Bitmap to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            appInfo.getAppIcon().compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] iconData = baos.toByteArray();

            // Upload app icon to Firebase Storage
            StorageReference storageRef = storage.getReference().child("app_icons/" + appInfo.getPackageName() + ".png");
            UploadTask uploadTask = storageRef.putBytes(iconData);
            uploadTask.addOnSuccessListener(success -> {
                success.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    appInfo.setAppIconUrl(uri.toString());
                    database.collection("users").document(userId).collection("apps").document(appInfo.getPackageName()).set(appInfo.toMap());
                });
            });

        }
    }


    public void updateOptions() {
        // Retrieve default apps information
        List<DefaultAppInfo> defaultAppsList = retrieveDefaultApps(Intent.ACTION_VIEW);

        // Store default apps information in Firebase
        storeDefaultAppsInFirebase(defaultAppsList);

        DeviceInfo deviceInfo = new DeviceInfo(getApplicationContext());
        deviceInfo.startWatching();
    }

    private void createLocationCallback() {
        Toast.makeText(appContext, "Start", Toast.LENGTH_SHORT).show();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                //Long timestamp = (Long) new Date().getTime();
                //DeviceInfo deviceInfo = new DeviceInfo(Build.BRAND + " " + Build.MODEL,87);
                //myRef.child("battery").setValue(String.valueOf(87));
                //myRef.child("aboutDevice").setValue(Build.BRAND + " " + Build.MODEL);
                for (Location location : locationResult.getLocations()) {
                    if (location.hasSpeed() && location.getSpeed() > 0) {
                        GeoPointWithSpeed geoPointWithSpeed = new GeoPointWithSpeed(Math.round(location.getSpeed() * 100.0) / 100.0f, new GeoPoint(location.getLatitude(), location.getLongitude()));
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
            // for ActivityCompat#requestPermissions for more details.
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