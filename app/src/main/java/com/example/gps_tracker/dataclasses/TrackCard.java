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
        this.username = username.length() > 10 ? username.substring(0,7)+"..." : username;

    }

    public String getDeviceName() {

        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName.length() > 10 ? deviceName.substring(0,10)+"..." : deviceName;
    }

    public TrackCard(String id, String username, String deviceName) {
        this.id = id;
        this.setUsername(username);
        this.setDeviceName(deviceName);
    }
}
