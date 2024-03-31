package com.example.gps_tracker;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuInflater;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.gps_tracker.databinding.ActivityMainBinding;
import com.example.gps_tracker.dataclasses.DefaultAppInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ClientActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DatabaseReference myRef;
    CustomManager fragmentManager;
    MenuInflater menuInflater;

    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());


        boolean serviceRunning = ServiceUtils.isServiceRunning(getApplicationContext(), LocationService.class);
        serviceIntent = serviceRunning ? new Intent(getApplicationContext(), LocationService.class) : null;
        /*int currentImg = serviceRunning ? drawables[1] : drawables[0];
        binding.stopStartServiceBtn.setImageResource(currentImg);*/

        if(!serviceRunning){
            startLocationService();
        }

        setContentView(binding.getRoot());

        fragmentManager = new CustomManager(getSupportFragmentManager(),binding.clientFragmentContainer.getId());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_about) {
                fragmentManager.replaceFragment(AboutFragment.class);
            } else if (id == R.id.menu_home) {
                fragmentManager.replaceFragment(HomeFragment.class);
            }
         else if (id == R.id.menu_track) {
            fragmentManager.replaceFragment(TrackFragment.class);
        }
            return true;
        });

    }




    protected void onDestroy() {
        super.onDestroy();
    }

    private void startLocationService() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions  more details.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
       LocationUtils.openLocationSettings(getApplicationContext());

        serviceIntent = new Intent(getApplicationContext(), LocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(serviceIntent);
        }
        else{
            getApplicationContext().startService(serviceIntent);
        }
    }

}