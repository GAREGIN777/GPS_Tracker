package com.example.gps_tracker.managers;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.media.AudioManager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

public class FlashlightManager {
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

    public void turnOnFlashlight() {
        if (isFlashlightAvailable()) {
            try {
                cameraManager.setTorchMode(cameraId, true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void turnOffFlashlight() {
        if (isFlashlightAvailable()) {
            try {
                cameraManager.setTorchMode(cameraId, false);
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

    public void toggle(Boolean value){
        if(value){
            turnOnFlashlight();
        }
        else{
            turnOffFlashlight();
        }
    }

    private boolean isFlashlightAvailable() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}
