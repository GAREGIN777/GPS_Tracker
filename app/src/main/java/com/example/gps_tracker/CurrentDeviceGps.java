package com.example.gps_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gps_tracker.constants.UI;
import com.example.gps_tracker.databinding.FragmentCurrentDeviceGpsBinding;
import com.example.gps_tracker.databinding.FragmentCurrentDeviceItemBinding;
import com.example.gps_tracker.databinding.FragmentDevicesBinding;
import com.example.gps_tracker.dataclasses.GuestUserModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yandex.mapkit.MapKitFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentDeviceItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentDeviceGps extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private static final String ARG_PARAM1 = "deviceId";

    // TODO: Rename and change types of parameters
    private String paramDeviceId;
    FragmentCurrentDeviceGpsBinding binding;

    public CurrentDeviceGps() {
        // Required empty public constructor
    }



    public static CurrentDeviceGps newInstance(String paramDeviceId) {
        CurrentDeviceGps fragment = new CurrentDeviceGps();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, paramDeviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("00d69039-167f-47ca-85e4-7573dcfdcdf1");
        MapKitFactory.initialize(requireActivity().getApplicationContext());

        if (getArguments() != null) {
            paramDeviceId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override public void onStart() {
        super.onStart();
        binding.trackMap.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override public void onStop() {
        super.onStop();
        binding.trackMap.onStop();
        MapKitFactory.getInstance().onStop();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCurrentDeviceGpsBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}