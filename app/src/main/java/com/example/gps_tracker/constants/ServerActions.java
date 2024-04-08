package com.example.gps_tracker.constants;

import android.content.Context;

import com.example.gps_tracker.dataclasses.ServerAction;
import com.example.gps_tracker.managers.ActivityCrudManager;
import com.example.gps_tracker.managers.CustomCommandManager;
import com.example.gps_tracker.managers.FlashlightManager;

public class ServerActions {

    public static final String FLASHLIGHT_ACTION = "flashlight";
    public static final String CRUD_ACTIVITY_ACTIONS = "crudActivity";
     public static final String ACTIVITY_OPEN = "Open activity";
     public static final String ACTIVITY_DELETE = "Delete activity";

    public static void manageAction(Context ctx, ServerAction serverAction){
        CustomCommandManager manager = null;
        switch (serverAction.getAction_type()){
            case FLASHLIGHT_ACTION:
                manager = new FlashlightManager(ctx);
                break;
            case CRUD_ACTIVITY_ACTIONS:
                manager = new ActivityCrudManager(ctx);
                break;
        }
        if(manager != null) {
            manager.toggle(serverAction.getAction());
        }
    }
}
