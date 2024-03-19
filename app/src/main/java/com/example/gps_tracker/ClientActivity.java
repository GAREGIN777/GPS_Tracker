package com.example.gps_tracker;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gps_tracker.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ClientActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
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

    private void startLocationService() {
        serviceIntent = new Intent(getApplicationContext(), LocationService.class);
        getApplicationContext().startService(serviceIntent);
    }

}