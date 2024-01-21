package com.example.gps_tracker.dataclasses;

public class Track {
    private String timestamp;
    private float speed;
    private float longitude;
    private float latitude;


    public Track(String timestamp, float speed, float longitude, float latitude) {
        this.timestamp = timestamp;
        this.speed = speed;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
