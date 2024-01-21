package com.example.gps_tracker;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
public class ServiceUtils {




        public static boolean isServiceRunning(Context context, Class<? extends Service> serviceClass) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            if (activityManager != null) {
                for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                    if (serviceClass.getName().equals(service.service.getClassName())) {
                        return true;
                    }
                }
            }

            return false;
        }
}
