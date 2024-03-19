package com.example.gps_tracker;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gps_tracker.constants.UI;
import com.example.gps_tracker.databinding.FragmentTrackBinding;
import com.example.gps_tracker.dataclasses.ChildParentPath;
import com.example.gps_tracker.dataclasses.UserData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private String myPath;

    FragmentTrackBinding binding;
    public TrackFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment TrackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrackFragment newInstance() {
        return new TrackFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myPath = Hashes.getHash(requireContext());


        binding = FragmentTrackBinding.inflate(inflater, container, false);
        binding.yourTrack.setText(getString(R.string.track_you, myPath));

        DocumentReference myRef = database.collection("users").document(myPath);

        binding.addDeviceBtn.setOnClickListener(v -> {


                    String parentPath = binding.deviceInput.getText().toString();
                    if (parentPath.trim().length() == 16) {
                        DocumentReference parentRef = database.collection("users").document(parentPath);
                        binding.addDeviceBtn.setEnabled(false);
                        parentRef.get().addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(requireContext(), getString(R.string.not_found_error), Toast.LENGTH_SHORT).show();
                            } else if (!task.getResult().exists()) {
                                Toast.makeText(requireContext(), getString(R.string.not_found_error), Toast.LENGTH_SHORT).show();
                            } else if (Objects.equals(task.getResult().get("role"), UserData.HOST)) {
                                parentRef.collection("children").document(myPath).get().addOnCompleteListener(add_task -> {
                                    DocumentSnapshot doc = add_task.getResult();
                                    if(doc != null) {
                                        if (doc.exists()) {
                                            Toast.makeText(requireContext(), getString(R.string.parent_already_connected), Toast.LENGTH_SHORT).show();
                                        } else {
                                            myRef.collection("parents").document(parentPath).set(new ChildParentPath().getMap());
                                            parentRef.collection("children").document(myPath).set(new ChildParentPath().getMap());
                                            Toast.makeText(requireContext(), getString(R.string.track_found_success), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        Toast.makeText(requireContext(), getString(R.string.track_smth_wrong), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {
                                Toast.makeText(requireContext(), getString(R.string.track_role_error), Toast.LENGTH_SHORT).show();
                            }
                            new Handler().postDelayed(() -> binding.addDeviceBtn.setEnabled(true), UI.BTN_MID_DELAY);
                        });
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.input_wrong), Toast.LENGTH_SHORT).show();
                        binding.addDeviceBtn.setEnabled(true);
                    }

            });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}