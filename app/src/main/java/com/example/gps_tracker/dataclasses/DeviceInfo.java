package com.example.gps_tracker.dataclasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

import com.example.gps_tracker.Hashes;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DeviceInfo {
    private int battery;
    private final Context ctx;
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
private final String userId;
    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }


    public void watchBatteryService(){
        if(ctx != null && userId != null) {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            DeviceInfo that = this;//simple context pointer

            Intent batteryStatus = ctx.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    that.setBattery(level * 100 / scale);
                    database.collection("users").document(userId).update("deviceInfo",that.getBaseInfo());

                }
            }, ifilter);
        }
    }



    public void startWatching(){
        this.watchBatteryService();
    }

    public DeviceInfo(){
        this.userId = null;
        this.ctx = null;
    }
    public DeviceInfo(Context ctx){
        userId  = Hashes.getHash(ctx);
        this.ctx = ctx;
    }


    public Map<String,Object> getBaseInfo(){
        Map<String,Object> baseInfoMap = new HashMap<>();
        baseInfoMap.put("battery",this.battery);
        return baseInfoMap;
    }
}
