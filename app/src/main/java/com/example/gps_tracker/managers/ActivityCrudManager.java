package com.example.gps_tracker.managers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

import com.example.gps_tracker.constants.ServerActions;

public class ActivityCrudManager extends CustomCommandManager{
        private final Context context;
        private final PackageManager packageManager;



    //private String cameraId;


    //crud operation



        public ActivityCrudManager(Context context) {
            this.context = context;
            packageManager = context.getPackageManager();

            if (packageManager != null) {

            }
        }


    /*public boolean isFlashlightOn() {
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            return Boolean.TRUE.equals(cameraManager.getCameraCharacteristics(cameraId)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE));
        } catch (CameraAccessException e) {
            e.printStackTrace();
            return false;
        }
    }*/



    public void open(String packageName){
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    public void delete(String packageName){
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(android.net.Uri.parse("package:" + packageName));
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        context.startActivity(intent);

    }

    @Override
    public void toggle(Object v) {
        String action = (String) v;
        if(action.split("&").length == 2) {
            String packageName = action.split("&")[0];
            String action_type = action.split("&")[1];
            switch (action_type){
                case ServerActions.ACTIVITY_OPEN:
                    open(packageName);
                case ServerActions.ACTIVITY_DELETE:
                    delete(packageName);
            }
        }
    }



}
