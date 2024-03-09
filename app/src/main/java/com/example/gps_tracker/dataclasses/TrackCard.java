package com.example.gps_tracker.dataclasses;

public class TrackCard {
    private String id;
    private String username;
    private String deviceName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public TrackCard(String id, String username, String deviceName) {
        this.id = id;
        this.username = username;
        this.deviceName = deviceName;
    }
}
