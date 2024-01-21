package com.example.gps_tracker.adapters;



import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.gps_tracker.R;
import com.example.gps_tracker.databinding.CustomTrackItemBinding;
import com.example.gps_tracker.dataclasses.Track;

import java.util.ArrayList;

public class TrackAdapter extends ArrayAdapter<Track> {
    private ArrayList<Track> dataSet;
    Context mContext;
    private static class ViewHolder {
        TextView trackTimestamp;
        TextView trackSpeed;
    }

    public TrackAdapter(ArrayList<Track> data, Context context) {
        super(context, R.layout.custom_track_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //Track dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
       //CustomTrackItemBinding binding; // view lookup cache stored in tag

       // binding = CustomTrackItemBinding.bind(convertView);

        /*if(dataModel != null) {
            binding.trackSpeed.setText(String.valueOf(dataModel.getSpeed()));
        }*/

        return convertView;
    }
}
