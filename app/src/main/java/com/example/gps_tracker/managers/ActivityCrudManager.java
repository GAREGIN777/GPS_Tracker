package com.example.gps_tracker.managers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

public class ActivityCrudManager extends CustomCommandManager{
        private final Context context;
        private final PackageManager packageManager;

        private final String OPEN = "Open activity";
        private final String DELETE = "Delete activity";

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
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void delete(String packageName){
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(android.net.Uri.parse("package:" + packageName));
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        context.startActivity(intent);

    }

    @Override
    public void toggle(Object v) {
        String action = (String) v;
        if(action.split(" ").length == 2) {
            String packageName = action.split(" ")[0];
            String action_type = action.split(" ")[1];
            switch (action_type){
                case OPEN:
                    open(packageName);
                case DELETE:
                    delete(packageName);
            }
        }
    }



}
