package com.example.gps_tracker;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gps_tracker.databinding.ActivityValidationBinding;
import com.example.gps_tracker.dataclasses.DeviceInfo;
import com.example.gps_tracker.dataclasses.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicInteger;

public class ValidationActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    ActivityValidationBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityValidationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        db.collection("users").document(Hashes.getHash(getApplicationContext())).get().addOnSuccessListener(documentSnapshot -> {
            UserData user = new UserData(getApplicationContext());
            if(documentSnapshot.exists()){
                startActivity(user.redirectIntent(documentSnapshot.getString("role")));
            }
            else {
                binding.pending.setVisibility(View.GONE);
                binding.main.setVisibility(View.VISIBLE);
                //db.collection("users").document().

                // получаем объект RadioGroup
                // обработка переключения состояния переключателя
                binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int radioId = group.getCheckedRadioButtonId();
                        int guest = R.id.radio_guest;
                        int host = R.id.radio_host;
                        if(radioId == guest){
                            user.setUserType(1);
                        }
                        else if(radioId == host){
                            user.setUserType(0);
                        }
                    }
                });

                binding.submit.setOnClickListener(v -> {

                    user.setUserName(binding.username.getText().toString());
                    if(user.isValidated()){
                        db.collection("users").document(Hashes.getHash(getApplicationContext())).set(user.getMap()).addOnSuccessListener(unused -> {
                            binding.submit.setEnabled(false);
                            startActivity(user.redirectIntent(user.getUserType()));
                            Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_LONG).show();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        });
                    }
                    else{
                       for(int iErr : user.showErrors().values()){
                          Toast.makeText(getApplicationContext(),getString(iErr),Toast.LENGTH_LONG).show();
                       }
                    }
                });
            }
        });


    }
}