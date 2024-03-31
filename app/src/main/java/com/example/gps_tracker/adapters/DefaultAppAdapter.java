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

import com.bumptech.glide.Glide;
import com.example.gps_tracker.CurrentDeviceItem;
import com.example.gps_tracker.CustomManager;
import com.example.gps_tracker.DevicesFragment;
import com.example.gps_tracker.HomeFragment;
import com.example.gps_tracker.TrackFragment;
import com.example.gps_tracker.databinding.CustomAppItemBinding;
import com.example.gps_tracker.databinding.CustomDeviceItemBinding;
import com.example.gps_tracker.dataclasses.DefaultAppInfo;
import com.example.gps_tracker.dataclasses.TrackCard;
import com.example.gps_tracker.helpers.AppLauncher;

import java.util.ArrayList;
import java.util.List;

public class DefaultAppAdapter extends RecyclerView.Adapter<DefaultAppAdapter.MyViewHolder> {

    private final List<DefaultAppInfo> apps;
    private final Context appContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CustomAppItemBinding binding;
        public MyViewHolder(CustomAppItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public DefaultAppAdapter(List<DefaultAppInfo> apps,Context appContext){
        this.apps = apps;
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public DefaultAppAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CustomAppItemBinding itemBinding = CustomAppItemBinding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DefaultAppAdapter.MyViewHolder holder, int position) {
        DefaultAppInfo appCard = apps.get(position);
        holder.binding.appName.setText(appCard.getShortAppName());
        holder.binding.getRoot().setOnClickListener(clickTask -> {
            AppLauncher.launchApp(appContext,appCard.getPackageName());
        });
        Glide.with(appContext).load(appCard.getAppIconUrl()).override(96,96).into(holder.binding.appImg);
        //holder.binding.devicename.setText(trackCard.getDeviceName());
    }


    @Override
    public int getItemCount() {
        return apps.size();
    }
}

