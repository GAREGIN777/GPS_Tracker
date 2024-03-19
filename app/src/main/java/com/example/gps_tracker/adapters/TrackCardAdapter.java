package com.example.gps_tracker.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gps_tracker.CurrentDeviceItem;
import com.example.gps_tracker.CustomManager;
import com.example.gps_tracker.DevicesFragment;
import com.example.gps_tracker.HomeFragment;
import com.example.gps_tracker.TrackFragment;
import com.example.gps_tracker.databinding.CustomDeviceItemBinding;
import com.example.gps_tracker.dataclasses.TrackCard;

import java.util.ArrayList;
import java.util.List;

public class TrackCardAdapter extends RecyclerView.Adapter<TrackCardAdapter.MyViewHolder> {

    private final List<TrackCard> cards;
    private final CustomManager fragmentManager;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CustomDeviceItemBinding binding;
        public MyViewHolder(CustomDeviceItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public TrackCardAdapter(List<TrackCard> cards,CustomManager fragmentManager){
      this.cards = cards;
      this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public TrackCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CustomDeviceItemBinding itemBinding = CustomDeviceItemBinding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackCardAdapter.MyViewHolder holder, int position) {
        TrackCard trackCard = cards.get(position);
        holder.binding.getRoot().setOnClickListener(v -> {
            fragmentManager.replaceFragment(CurrentDeviceItem.newInstance(trackCard.getId()));
            //Toast.makeText(v.getContext(), trackCard.getDeviceName(),Toast.LENGTH_SHORT).show();
        });
        holder.binding.username.setText(trackCard.getUsername());
        holder.binding.devicename.setText(trackCard.getDeviceName());
    }


    @Override
    public int getItemCount() {
        return cards.size();
    }
}
