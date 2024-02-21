package com.example.gps_tracker;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentContainerView;

import com.example.gps_tracker.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final String DEFAULT_NAME = "Anonymous".toUpperCase();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private ActivityMainBinding binding;
    private Intent serviceIntent;
    private final boolean serviceRunning = false;
    private final int[] drawables = new int[]{R.drawable.baseline_double_arrow_24,R.drawable.twotone_pause_24};
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    CustomManager fragmentManager;
    MenuInflater menuInflater;
    //Menu menu;
    // do something when the button is clicked
    // Yes we will handle click here but which button clicked??? We don't know


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_menu, menu);
        //menuHome = menu.findItem(R.id.menu_home);
        //menuAbout = menu.findItem(R.id.menu_about);
        this.menu = menu;
        return true;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //binding.yourAppId.setText(String.format(getString(R.string.app_your_id),Hashes.getHash(getApplicationContext())));

        fragmentManager = new CustomManager(getSupportFragmentManager());
        //btnAbout.setOnClickListener(fragmentManager.listenFragmentBtn(AboutFragment.class,"replace"));
        //btnHome.setOnClickListener(fragmentManager.listenFragmentBtn(HomeFragment.class,"replace"));

        //if(menu != null) {
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });


        /*myRef = database.getReference(Hashes.getHash(getApplicationContext()));
        serviceRunning = ServiceUtils.isServiceRunning(getApplicationContext(), LocationService.class);
        serviceIntent = serviceRunning ? new Intent(this, LocationService.class) : null;
        int currentimg = serviceRunning ? drawables[1] : drawables[0];
        binding.stopStartServiceBtn.setImageResource(currentimg);

        binding.stopStartServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serviceRunning) {
                    stopLocationService();
                }
                else{
                    if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                            android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        startLocationService();
                    } else {
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_REQUEST_CODE);
                    }
                }
                serviceRunning = !serviceRunning;
                int currentimg = serviceRunning ? drawables[1] : drawables[0];
                binding.stopStartServiceBtn.setImageResource(currentimg);

            }
        });*/


        // Check and request location permission

    }





}