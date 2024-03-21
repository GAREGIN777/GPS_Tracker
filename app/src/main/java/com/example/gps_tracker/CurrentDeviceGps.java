package com.example.gps_tracker;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gps_tracker.constants.UI;
import com.example.gps_tracker.databinding.FragmentCurrentDeviceGpsBinding;
import com.example.gps_tracker.databinding.FragmentCurrentDeviceItemBinding;
import com.example.gps_tracker.databinding.FragmentDevicesBinding;
import com.example.gps_tracker.dataclasses.GeoPointWithSpeed;
import com.example.gps_tracker.dataclasses.GuestUserModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.geometry.PolylineBuilder;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.PlacemarkCreatedCallback;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.List;

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
        if(paramDeviceId != null){
            binding.trackMap.getMapWindow().getMap().getMapObjects().clear();
            PolylineBuilder route = new PolylineBuilder();

            database.collection("users").document(paramDeviceId).collection("track").get().addOnCompleteListener(task -> {
                if (task.getResult() != null){
                   for(DocumentSnapshot snapshot : task.getResult().getDocuments()){
                       GeoPointWithSpeed point = snapshot.toObject(GeoPointWithSpeed.class);
                       if(point != null) {
                           route.append(new Point(point.getGeoPoint().getLatitude(), point.getGeoPoint().getLongitude()));
                       }

                       database.collection("users").document(paramDeviceId).collection("track").addSnapshotListener((querySnapshot, error) -> {
                           if (querySnapshot != null && !querySnapshot.isEmpty()) {
                               for (DocumentChange document : querySnapshot.getDocumentChanges()) {
                                   GeoPointWithSpeed realPoint = document.getDocument().toObject(GeoPointWithSpeed.class);
                                   route.append(new Point(realPoint.getGeoPoint().getLatitude(),realPoint.getGeoPoint().getLongitude()));
                               }

                               if (route.build() != null && route.build().getPoints().size() > 1) {
                                   Point coordPointEnd = route.build().getPoints().get(0);
                                   binding.trackMap.getMapWindow().getMap().getMapObjects().addPlacemark(new PlacemarkCreatedCallback() {
                                       @Override
                                       public void onPlacemarkCreated(@NonNull PlacemarkMapObject placemarkMapObject) {
                                           placemarkMapObject.setGeometry(coordPointEnd);
                                           placemarkMapObject.setIcon(ImageProvider.fromResource(requireContext(), R.drawable.custom_placemark));
                                       }
                                   });

                                   //adding route


                                   binding.trackMap.getMapWindow().getMap().getMapObjects().addPolyline(route.build());

                                   //moving camera
                                   binding.trackMap.getMapWindow().getMap().move(new CameraPosition(coordPointEnd, 21, 90, 30.0f));
                               }

                           }
                       });

                   }
                }
            });

        }
        return binding.getRoot();
    }
}