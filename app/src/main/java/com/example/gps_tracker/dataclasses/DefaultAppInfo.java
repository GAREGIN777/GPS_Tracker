package com.example.gps_tracker.dataclasses;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

public class DefaultAppInfo {
    private  String packageName;
    private  String appName;
    private  Bitmap appIcon;



    private String appIconUrl;

    //constructor to read
    private DefaultAppInfo(){

    }
    public DefaultAppInfo(String packageName, String appName, Bitmap appIcon) {
        this.packageName = packageName;
        this.appName = appName;
        this.appIcon = appIcon;
    }

    public String getAppIconUrl() {
        return appIconUrl;
    }

    public void setAppIconUrl(String appIconUrl) {
        this.appIconUrl = appIconUrl;
    }

    public Bitmap getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Bitmap appIcon) {
        this.appIcon = appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAppName() {
        return appName;
    }
    public String getShortAppName(){return appName.length() > 20 ? appName.substring(0,17)+"..." : appName;}

    public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<>();
        map.put("packageName",this.getPackageName());
        map.put("appName",this.getAppName());
        map.put("appIconUrl",this.getAppIconUrl());
        return map;
    }


}