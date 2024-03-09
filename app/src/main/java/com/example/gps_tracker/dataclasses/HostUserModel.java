package com.example.gps_tracker.dataclasses;

import java.util.HashMap;

public class HostUserModel {
    private HashMap<String,String> connected;
    private String devicename;
    private String username;

    public void pushDevice(String device){
     if (this.connected != null && this.connected.size() > 0) {
      int ind = Integer.parseInt(connected.keySet().toArray(new String[0])[connected.keySet().size() - 1]);
      this.connected.put(String.valueOf(ind + 1), device);
     }
    else{
        this.connected = new HashMap<String,String>();
        this.connected.put("0",device);
     }
    }

    public HashMap<String, String> getConnected() {
        return connected;
    }

    public void setConnected(HashMap<String, String> connected) {
        this.connected = connected;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
