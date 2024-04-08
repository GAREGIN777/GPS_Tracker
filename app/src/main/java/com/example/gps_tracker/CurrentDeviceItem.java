package com.example.gps_tracker;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gps_tracker.adapters.DefaultAppAdapter;
import com.example.gps_tracker.constants.ServerActions;
import com.example.gps_tracker.constants.UI;
import com.example.gps_tracker.databinding.FragmentCurrentDeviceItemBinding;
import com.example.gps_tracker.databinding.FragmentDevicesBinding;
import com.example.gps_tracker.dataclasses.DefaultAppInfo;
import com.example.gps_tracker.dataclasses.GuestUserModel;
import com.example.gps_tracker.dataclasses.ServerAction;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentDeviceItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentDeviceItem extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private static final String ARG_PARAM1 = "deviceId";

    // TODO: Rename and change types of parameters
    private String paramDeviceId;

    private boolean turn = true;

    private RecyclerView appsRecycle;
    private List<DefaultAppInfo> appDocuments;

    private DefaultAppAdapter appsAdapter;
    private final int PER_PAGE = 16;


    public CurrentDeviceItem() {
        // Required empty public constructor
    }



    public static CurrentDeviceItem newInstance(String paramDeviceId) {
        CurrentDeviceItem fragment = new CurrentDeviceItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, paramDeviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paramDeviceId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCurrentDeviceItemBinding binding = FragmentCurrentDeviceItemBinding.inflate(inflater, container, false);

        appsRecycle = binding.appsRecycle;
        binding.mainContainer.setVisibility(View.GONE);
        binding.loader.setVisibility(View.VISIBLE);

        if (paramDeviceId != null){
            database.collection("users").document(paramDeviceId).addSnapshotListener((documentSnapshot, error) -> {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    GuestUserModel currentChildModel = documentSnapshot.toObject(GuestUserModel.class);
                    if (currentChildModel != null && currentChildModel.getDeviceInfo() != null) {
                        binding.deviceName.setText(getString(R.string.device_name, currentChildModel.getDevicename()));
                        binding.userName.setText(currentChildModel.getUsername());

                        int battery = currentChildModel.getDeviceInfo().getBattery();
                        binding.batteryImage.setImageResource(R.drawable.battery_ui);
                        binding.battery.setText(getString(R.string.track_battery_charge, battery));

                        //AtomicBoolean turn = new AtomicBoolean(true);



                        binding.flashlight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ServerAction serverAction = new ServerAction(ServerActions.FLASHLIGHT_ACTION, turn);
                                database.collection("users").document(paramDeviceId).collection("server").document(ServerActions.FLASHLIGHT_ACTION).set(serverAction.toMap());
                                turn = !turn;
                            }
                        });
//apps
                        getAppsDefault();


                        binding.searchChildApp.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                searchByAppName(s.toString());
                            }

                            @Override public void afterTextChanged(Editable s) {}
                        });


                        binding.loader.setVisibility(View.GONE);
                        binding.mainContainer.setVisibility(View.VISIBLE);
                        }


                    //CustomManager manager = new CustomManager(getParentFragmentManager(),UI.serverFragmentCont);
                    binding.gpsOpen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //manager.replaceFragment(CurrentDeviceGps.newInstance(paramDeviceId), CustomManager.REPLACE_ACTION);
                            ((ServerActivity) requireActivity()).replace(CurrentDeviceGps.newInstance(paramDeviceId));
                            //manager.listenFragmentBtn(CurrentDeviceGps.newInstance(paramDeviceId), CustomManager.REPLACE_ACTION);
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), getString(R.string.not_found_error), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void searchByAppName(String appName) {
        Toast.makeText(getContext(),appName,Toast.LENGTH_SHORT).show();

        //appName = appName.toLowerCase(Locale.getDefault());
        database.collection("users").document(paramDeviceId).collection("apps").whereGreaterThanOrEqualTo("appName", appName)
                .whereLessThanOrEqualTo("appName", appName + "\uf8ff").limit(PER_PAGE).orderBy("appName").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DefaultAppInfo> searchDocuments = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                // Extract document ID or other data
                DefaultAppInfo appId = document.toObject(DefaultAppInfo.class);
                searchDocuments.add(appId);
            }


            appDocuments.clear();
            appDocuments.addAll(searchDocuments);
//appDocuments = searchDocuments;
            appsAdapter.notifyDataSetChanged();
            //appsRecycle.setAdapter(new DefaultAppAdapter(appDocuments, requireContext()));

        });
    }


    public  void getAppsDefault(){
        database.collection("users").document(paramDeviceId).collection("apps").limit(PER_PAGE).orderBy("appName").get().addOnSuccessListener(queryDocumentSnapshots -> {
           appDocuments = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                // Extract document ID or other data
                DefaultAppInfo appId = document.toObject(DefaultAppInfo.class);
                appDocuments.add(appId);
            }




            appsAdapter = new DefaultAppAdapter(appDocuments, requireContext(),paramDeviceId);
            appsRecycle.setAdapter(appsAdapter);

        });
    }

}