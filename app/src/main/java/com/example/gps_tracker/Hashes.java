package com.example.gps_tracker;

import android.content.Context;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

public class Hashes {
    public static String getHash(Context appContext) {
        String androidId = android.provider.Settings.Secure.getString(appContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        //UUID deviceUuid = new UUID(androidId.hashCode(),30L);
        return androidId;//deviceUuid.toString();
    }


}
