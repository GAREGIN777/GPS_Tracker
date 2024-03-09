package com.example.gps_tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gps_tracker.databinding.FragmentCurrentDeviceItemBinding;
import com.example.gps_tracker.databinding.FragmentDevicesBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentDeviceItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentDeviceItem extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

     FragmentCurrentDeviceItemBinding binding;
    private static final String ARG_PARAM1 = "deviceId";

    // TODO: Rename and change types of parameters
    private String mParam1;


    public CurrentDeviceItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentDeviceItem.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentDeviceItem newInstance(String param1, String param2) {
        CurrentDeviceItem fragment = new CurrentDeviceItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCurrentDeviceItemBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}