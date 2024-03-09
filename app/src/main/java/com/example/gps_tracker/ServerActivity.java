package com.example.gps_tracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gps_tracker.adapters.TrackCardAdapter;
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

        //fragmentManager.replaceFragment();


        binding = ActivityServerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        DocumentReference ref = firestoreDb.collection("users").document(Hashes.getHash(getApplicationContext()));

       ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()){
                HostUserModel model = task.getResult().toObject(HostUserModel.class);
                if(model != null) {
                   // model.pushDevice("New device" + Math.random());
                    //String username = task.getResult().getString("username");
                   // task.getResult().getReference().update("connected",model.getConnected());
                    //HashMap<Integer,String> map = task.getResult().get("connected");

                    //ref.update("connected","New device");
                    binding.username.setText(model.getUsername());

                }
            }
        });

        //passedData.putString("document",Hashes.getHash(getApplicationContext()));
        //fragment.setArguments(passedData);



        //binding.fragmentContainerView.
        //data


    }
}