package com.example.gps_tracker.dataclasses;

import java.util.HashMap;

public class HostUserModel {
    private HashMap<String,String> connected;
    private String devicename;
    private String username;

    public static void pushDevice(HostUserModel ctx,String deviceId){
     if (ctx.connected != null && ctx.connected.size() > 0) {
      int ind = Integer.parseInt(ctx.connected.keySet().toArray(new String[0])[ctx.connected.keySet().size() - 1]);
      ctx.connected.put(String.valueOf(ind + 1), deviceId);
     }
    else{
        ctx.connected = new HashMap<String,String>();
        ctx.connected.put("0",deviceId);
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
