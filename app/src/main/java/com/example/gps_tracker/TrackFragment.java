package com.example.gps_tracker;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.gps_tracker.adapters.TrackAdapter;
import com.example.gps_tracker.constants.UI;
import com.example.gps_tracker.databinding.FragmentHomeBinding;
import com.example.gps_tracker.databinding.FragmentTrackBinding;
import com.example.gps_tracker.dataclasses.Track;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
private String myRef;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final ArrayList<String> pointsList = new ArrayList<String>();
    FragmentTrackBinding binding;
    public TrackFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrackFragment newInstance(String param1, String param2) {
        TrackFragment fragment = new TrackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        myRef = Hashes.getHash(requireContext());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTrackBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(),R.layout.custom_track_item);

        binding.yourTrack.setText(getString(R.string.track_you, myRef));
        binding.submitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setClickable(false);
                v.setAlpha(0.5f);
                String refName = String.valueOf(binding.trackInput.getText()).trim();
                if (refName.length() == 0) {
                    Toast.makeText(getContext(), getString(R.string.track_input_empty), Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference ref = database.getReference(refName);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if (snapshot.getValue() != null) {
                                //Toast.makeText(getContext(), getString(R.string.track_found_success,refName), Toast.LENGTH_SHORT).show();
                               /* String point = String.valueOf(snapshot.getValue());
                                pointsList.add("Hello");
                                adapter.notifyDataSetChanged();*/
                            } else {
                                Toast.makeText(getContext(), getString(R.string.not_found_error), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    binding.trackInput.setText("");
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(UI.BTN_MID_DELAY);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        v.setClickable(true);
                        v.setAlpha(1);
                    }
                }).start();


            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
    }
}