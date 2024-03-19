package com.example.gps_tracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.example.gps_tracker.adapters.TrackCardAdapter;
import com.example.gps_tracker.constants.UI;
import com.example.gps_tracker.databinding.ActivityServerBinding;
import com.example.gps_tracker.dataclasses.HostUserModel;
import com.example.gps_tracker.dataclasses.TrackCard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ServerActivity extends AppCompatActivity {
    CustomManager fragmentManager;
    ActivityServerBinding binding;
    FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityServerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }






    public void replace(Fragment fragment){
        CustomManager manager = new CustomManager(getSupportFragmentManager(),binding.serverFragmentContainer.getId());
        manager.replaceFragment(fragment);
    }
}