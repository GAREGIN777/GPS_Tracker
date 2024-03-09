package com.example.gps_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gps_tracker.adapters.TrackCardAdapter;
import com.example.gps_tracker.constants.UI;
import com.example.gps_tracker.databinding.FragmentDevicesBinding;
import com.example.gps_tracker.databinding.FragmentHomeBinding;
import com.example.gps_tracker.dataclasses.HostUserModel;
import com.example.gps_tracker.dataclasses.TrackCard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DevicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DevicesFragment extends Fragment {
    FragmentDevicesBinding binding;
    CustomManager fragmentManager;
    FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
     int containerId;

    // TODO: Rename and change types of parameters





    public DevicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment DevicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DevicesFragment newInstance(int containerId) {
        DevicesFragment fragment = new DevicesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, containerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDevicesBinding.inflate(inflater, container, false);

        DocumentReference ref = firestoreDb.collection("users").document(Hashes.getHash(requireContext()));


       /* binding.addDeviceBtn.setOnClickListener(v -> {
            binding.addDeviceBtn.setEnabled(false);
            ref.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()){
                    HostUserModel model = task.getResult().toObject(HostUserModel.class);
                    if(model != null) {
                        String input = String.valueOf(binding.deviceInput.getText());
                        if(input.trim().length() > 0) {
                            firestoreDb.collection("users").document(input).get().addOnCompleteListener(taskGotDevice -> {
                                if(taskGotDevice.getResult().exists()) {
                                    Toast.makeText(requireContext(),getString(R.string.track_found_success),Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(requireContext(),getString(R.string.not_found_error),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        binding.addDeviceBtn.setEnabled(true);

                        // model.pushDevice("New device" + Math.random());
                        //String username = task.getResult().getString("username");
                        // task.getResult().getReference().update("connected",model.getConnected());
                        //HashMap<Integer,String> map = task.getResult().get("connected");

                        //ref.update("connected","New device");
                        //binding.username.setText(model.getUsername());

                    }
                }
            });
        });*/


        List<TrackCard> cards = new ArrayList<TrackCard>();
        for(int i = 0;i < 100;i++) {
            cards.add(new TrackCard("Phone"+i, "Phone"+i, "Nexus 6p"));
        }

        binding.devicesRecycle.setAdapter(new TrackCardAdapter(cards, new CustomManager(getParentFragmentManager(),UI.serverFragmentCont)));

        return binding.getRoot();

    }
}