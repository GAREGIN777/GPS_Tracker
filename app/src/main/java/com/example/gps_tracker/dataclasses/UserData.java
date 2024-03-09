package com.example.gps_tracker.dataclasses;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gps_tracker.ClientActivity;
import com.example.gps_tracker.ServerActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserData {
    private final String[] userTypes = {"Host","Guest"};
   private final Class<? extends AppCompatActivity>[] activityClasses = new Class[]{
            ServerActivity.class,
            ClientActivity.class,
    };
    private String userType;
    private String userName;



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

    public Map<String, Object> getMap(){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username",this.userName);
        userMap.put("role",this.userType);
        userMap.put("devicename", Build.MODEL);
        if(Objects.equals(this.userType, userTypes[0])){
            userMap.put("connected", new HashMap<>());
        }
        else{
            userMap.put("parent",null);
        }
        return userMap;
    }

    public Intent redirectIntent(Context ctx,String role){
        int index = Arrays.asList(userTypes).indexOf(role);
        Class<? extends AppCompatActivity> intentClass = activityClasses[index];
        Intent intent = new Intent(ctx,intentClass);
        return intent;
    }
}
