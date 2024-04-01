package com.example.gps_tracker.constants;

import android.content.Context;

import com.example.gps_tracker.dataclasses.ServerAction;
import com.example.gps_tracker.managers.ActivityCrudManager;
import com.example.gps_tracker.managers.CustomCommandManager;
import com.example.gps_tracker.managers.FlashlightManager;

public class ServerActions {

    public static final String FLASHLIGHT_ACTION = "flashlight";
    public static final String CRUD_ACTIVITY_ACTIONS = "crudActivity";

    public static void manageAction(CustomCommandManager manager, ServerAction serverAction){
        manager.toggle(serverAction.getAction());
    }
}
