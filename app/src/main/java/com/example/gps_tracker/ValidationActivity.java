package com.example.gps_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.gps_tracker.databinding.ActivityValidationBinding;
import com.example.gps_tracker.dataclasses.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicInteger;

public class ValidationActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    ActivityValidationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityValidationBinding.inflate(getLayoutInflater());
        UserData user = new UserData();
        //db.collection("users").document().
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        // получаем объект RadioGroup
        // обработка переключения состояния переключателя
        binding.radioGroup.setOnCheckedChangeListener((radiogroup, id)-> {

            // получае выбранную кнопку
            RadioButton radio = findViewById(id);
            switch (radio.getText().toString()) {
                case "Parent":
                     user.setUserType(0);
                    break;
                case "Child":
                    user.setUserType(1);
                    break;
                default:
                    break;
            }
        });

        binding.submit.setOnClickListener(v -> {

            user.setUserName(binding.username.getText().toString());
            if(user.isValidated()){
                db.collection("users").document(Hashes.getHash(getApplicationContext())).set(user.getMap()).addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}