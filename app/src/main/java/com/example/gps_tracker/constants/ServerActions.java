package com.example.gps_tracker.constants;

import android.content.Context;

import com.example.gps_tracker.managers.FlashlightManager;

public class ServerActions {

    public static final String FLASHLIGHT_ACTION = "flashlight";

    public static void manageAction(String action_type, Object action, Context ctx){
        switch (action_type) {
            case FLASHLIGHT_ACTION:
                FlashlightManager manager = new FlashlightManager(ctx);
                manager.toggle((Boolean) action);
                break;
        }
    }
}
