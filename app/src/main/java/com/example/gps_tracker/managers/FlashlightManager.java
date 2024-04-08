package com.example.gps_tracker.managers;


import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.media.AudioManager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

public class FlashlightManager extends CustomCommandManager{
    private final Context context;
    private final CameraManager cameraManager;
    private String cameraId;

    public FlashlightManager(Context context) {
        this.context = context;
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        if (cameraManager != null) {
            try {
                cameraId = cameraManager.getCameraIdList()[0]; // Use the first camera
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
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

    public void toggle(Object value){
        boolean mode = (Boolean) value;
        if (isAvailable()) {
            try {
                cameraManager.setTorchMode(cameraId, mode);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isAvailable() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}
