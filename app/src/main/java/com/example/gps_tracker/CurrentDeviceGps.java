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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.geometry.PolylineBuilder;
import com.yandex.mapkit.map.BaseMapObjectCollection;
import com.yandex.mapkit.map.Callback;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollectionListener;
import com.yandex.mapkit.map.MapObjectDragListener;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.MapObjectVisitor;
import com.yandex.mapkit.map.PlacemarkCreatedCallback;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.map.internal.ClusterizedPlacemarkCollectionBinding;
import com.yandex.runtime.image.AnimatedImageProvider;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.ui_view.ViewProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
           List<Point> route = new ArrayList<>();

            database.collection("users").document(paramDeviceId).collection("track").orderBy("timestamp").get().addOnCompleteListener(task -> {
                if (task.getResult() != null){
                   for(QueryDocumentSnapshot snapshot : task.getResult()) {
                       GeoPointWithSpeed point = snapshot.toObject(GeoPointWithSpeed.class);
                       Point geom = new Point(point.getGeoPoint().getLatitude(), point.getGeoPoint().getLongitude());
                       route.add(geom);
                   }

                    Polyline startPolyline = new Polyline(route);
                    List<Point> startPoints = startPolyline.getPoints();

                    PlacemarkMapObject placemark =  binding.trackMap.getMapWindow().getMap().getMapObjects().addPlacemark(placemarkMapObject -> {
                        placemarkMapObject.setGeometry(startPoints.get(startPoints.size()-1));
                        placemarkMapObject.setIcon(ImageProvider.fromResource(requireContext(), R.drawable.custom_placemark));
                    });

                    PolylineMapObject startPolylineObject = binding.trackMap.getMapWindow().getMap().getMapObjects().addPolyline(startPolyline);
                    binding.trackMap.getMapWindow().getMap().move(new CameraPosition(startPoints.get(startPoints.size()-1), 19, 90, 30.0f));

                       database.collection("users").document(paramDeviceId).collection("track").whereGreaterThan("timestamp",new Timestamp(new Date())).addSnapshotListener((queryDocumentSnapshots, error) -> {
                           if(error != null){
                               return;
                           }

                           if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                               for (DocumentChange document : queryDocumentSnapshots.getDocumentChanges()) {
                                   GeoPointWithSpeed realPoint = document.getDocument().toObject(GeoPointWithSpeed.class);
                                   //Toast.makeText(requireContext(),realPoint.getMap().toString(),Toast.LENGTH_SHORT).show();
                                   Point snapshotPoint = new Point(realPoint.getGeoPoint().getLatitude(),realPoint.getGeoPoint().getLongitude());
                                   route.add(snapshotPoint);
                               }
                               Polyline currentPolyline = new Polyline(route);
                               List<Point> currentRoute = currentPolyline.getPoints();

                              if (currentRoute.size() > 1) {
                                   Point coordPointEnd = currentRoute.get(currentRoute.size()-1);
                                   placemark.setGeometry(coordPointEnd);
                                   startPolylineObject.setGeometry(currentPolyline);
                                   binding.trackMap.getMapWindow().getMap().move(new CameraPosition(coordPointEnd, 19, 90, 30.0f));
                               }

                           }
                       });

                }
            });

        }
        return binding.getRoot();
    }
}