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
import com.example.gps_tracker.dataclasses.GuestUserModel;
import com.example.gps_tracker.dataclasses.HostUserModel;
import com.example.gps_tracker.dataclasses.TrackCard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
        String yourId = Hashes.getHash(requireContext());

        binding = FragmentDevicesBinding.inflate(inflater, container, false);



        DocumentReference myRef = firestoreDb.collection("users").document(yourId);

        myRef.get().addOnCompleteListener(userTask -> {
            if (userTask.getResult().exists()){
                binding.serverTrackId.setText(getString(R.string.track_you,yourId));
                binding.username.setText(userTask.getResult().getString("username"));
            }
        });

        myRef.collection("children").addSnapshotListener((querySnapshot, error) -> {

            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                List<TrackCard> cards = new ArrayList<>();
                List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

                binding.devicesLoader.setVisibility(View.VISIBLE);
                binding.devicesRecycle.setVisibility(View.GONE);

                for (QueryDocumentSnapshot document : querySnapshot) {
                    String docId = document.getId();
                    Task<DocumentSnapshot> docTask = firestoreDb.collection("users").document(docId).get();
                    tasks.add(docTask);
                    docTask.addOnCompleteListener(doc_task -> {
                        if (doc_task.isSuccessful()) {
                            GuestUserModel childModel = doc_task.getResult().toObject(GuestUserModel.class);
                            if (childModel != null) {
                                cards.add(new TrackCard(docId, childModel.getUsername(), childModel.getDevicename()));
                            }
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.not_found_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Tasks.whenAllComplete(tasks).addOnCompleteListener(taskList -> {
                    binding.devicesLoader.setVisibility(View.GONE);
                    binding.devicesRecycle.setVisibility(View.VISIBLE);
                    binding.devicesRecycle.setAdapter(new TrackCardAdapter(cards, new CustomManager(getParentFragmentManager(), UI.serverFragmentCont)));
                });
            } else  if(querySnapshot == null){
                    Toast.makeText(requireContext(), getString(R.string.track_smth_wrong), Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(requireContext(), getString(R.string.not_found_error), Toast.LENGTH_SHORT).show();
            }
        });


        /*myRef.collection("children").get().addOnCompleteListener(task -> {
            List<TrackCard> cards = new ArrayList<TrackCard>();
            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

            binding.devicesLoader.setVisibility(View.VISIBLE);
            binding.devicesRecycle.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                   String docId = document.getId();
                   Task<DocumentSnapshot> docTask = firestoreDb.collection("users").document(docId).get();
                   tasks.add(docTask);
                    docTask.addOnCompleteListener(doc_task -> {
                        if(doc_task.isSuccessful()){
                            GuestUserModel childModel = doc_task.getResult().toObject(GuestUserModel.class);
                            //Toast.makeText(requireContext(),Toast.LENGTH_SHORT).show();
                            if(childModel != null) {
                                cards.add(new TrackCard(docId, childModel.getUsername(), childModel.getDevicename()));
                            }
                        }
                        else {
                            Toast.makeText(requireContext(), getString(R.string.not_found_error), Toast.LENGTH_SHORT).show();
                        }
                    });

                   //Toast.makeText(requireContext(),childModel.getUsername(),Toast.LENGTH_SHORT).show();
                  // cards.add(new TrackCard(docId,document.get("username").toString(),document.get("devicename").toString()));
                }
                Tasks.whenAllComplete(tasks).addOnCompleteListener(taskList -> {
                    binding.devicesLoader.setVisibility(View.GONE);
                    binding.devicesRecycle.setVisibility(View.VISIBLE);
                    binding.devicesRecycle.setAdapter(new TrackCardAdapter(cards, new CustomManager(getParentFragmentManager(), UI.serverFragmentCont)));
                });
            } else {
                Toast.makeText(requireContext(), getString(R.string.track_smth_wrong), Toast.LENGTH_SHORT).show();
            }
        });*/


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


        /*for(int i = 0;i < 100;i++) {
            cards.add(new TrackCard("Phone"+i, "Phone"+i, "Nexus 6p"));
        }*/


        return binding.getRoot();

    }
}