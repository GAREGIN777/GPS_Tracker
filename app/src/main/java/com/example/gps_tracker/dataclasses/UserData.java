package com.example.gps_tracker.dataclasses;

import java.util.HashMap;
import java.util.Map;

public class UserData {
    private static final String[] userTypes = {"Host","Guest"};
    private String userType;
    private String userName;



    public String getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        if(userType > 0 && userType < userTypes.length){
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

    public Map<String, String> getMap(){
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name",this.userName);
        userMap.put("role",this.userType);
        return userMap;
    }
}
