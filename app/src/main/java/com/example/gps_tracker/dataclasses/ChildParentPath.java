package com.example.gps_tracker.dataclasses;


import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class ChildParentPath {
    private final Timestamp connectedAt;

    public ChildParentPath() {
        this.connectedAt = Timestamp.now();
    }

    public Map<String,Object> getMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("connectedAt",this.connectedAt);
        return map;
    }
}
