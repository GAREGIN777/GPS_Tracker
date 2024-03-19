
package com.example.gps_tracker.dataclasses;

import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import com.google.firebase.firestore.PropertyName;


//? exists
public class GuestUserModel{
    private DeviceInfo deviceInfo;
    private String devicename;
    private String username;
    private String role;
    private HashMap<String,String> parent;


    public HashMap<String, String> getParent() {
        return this.parent;
    }


    public void setParent(HashMap<String, String> parent) {
        this.parent = parent;
    }



    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }


    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }




}