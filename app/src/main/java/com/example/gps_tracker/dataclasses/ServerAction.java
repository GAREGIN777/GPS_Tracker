package com.example.gps_tracker.dataclasses;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ServerAction {
    private String action_type;
    private Object action;


    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public Object getAction() {
        return action;
    }

    public void setAction(Object action) {
        this.action = action;
    }

    public ServerAction(String action_type, Object action) {
        this.action_type = action_type;
        this.action = action;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getAction_type();
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("action_type",this.action_type);
        map.put("action",this.action);
        return map;
    }

    public ServerAction(){}
}
