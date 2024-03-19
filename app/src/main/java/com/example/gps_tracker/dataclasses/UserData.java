package com.example.gps_tracker.dataclasses;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gps_tracker.ClientActivity;
import com.example.gps_tracker.R;
import com.example.gps_tracker.ServerActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserData {


    public static final String GUEST = "Guest";
    public static final String HOST = "Host";
    private final String[] userTypes = {HOST,GUEST};
   private final Class<? extends AppCompatActivity>[] activityClasses = new Class[]{
            ServerActivity.class,
            ClientActivity.class,
    };

   private Context ctx;
    private String userType;
    private String userName;



    public UserData(Context ctx){
        this.ctx = ctx;
    }
    public String getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        if(userType >= 0 && userType < userTypes.length){
            this.userType = userTypes[userType];
            return;
        }

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if(userName.trim().length() >= 3 && userName.trim().length() <= 20) {
            this.userName = userName;
            return;
        }
    }

    public Boolean isValidated(){
        return this.userName != null && this.userType != null;
    }

    public int[] showErrors(){
      int[] errors = new int[userTypes.length];
        if(this.userName == null) {
           errors[0] = R.string.user_name_warn;
        }
        else if(this.userType == null){
            errors[1] = R.string.user_role_warn;
        }
        return errors;
    }

    public Map<String, Object> getMap(){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username",this.userName);
        userMap.put("role",this.userType);
        userMap.put("devicename",Build.BRAND + " " + Build.MODEL);
        if(Objects.equals(this.userType, HOST)){
            userMap.put("connected", new HashMap<>());
        }
        else{
            userMap.put("parent",null);
            userMap.put("deviceInfo",null);
        }
        return userMap;
    }

    public Intent redirectIntent(String role){
        int index = Arrays.asList(userTypes).indexOf(role);
        Class<? extends AppCompatActivity> intentClass = activityClasses[index];
        return new Intent(ctx,intentClass);
    }
}
