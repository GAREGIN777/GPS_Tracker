package com.example.gps_tracker.dataclasses;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class GeoPointWithSpeed {
    private Timestamp timestamp;
   private float speed;
   private GeoPoint geoPoint;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public GeoPointWithSpeed() {

   }
    public GeoPointWithSpeed(float speed,GeoPoint geoPoint){
        this.geoPoint = geoPoint;
        this.timestamp = Timestamp.now();
        this.speed = speed;
    }

    public Map<String,Object> getMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("geoPoint",this.geoPoint);
        map.put("timestamp",this.timestamp);
        map.put("speed",this.speed);
        return map;
    }
}
