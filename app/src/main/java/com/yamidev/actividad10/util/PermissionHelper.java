package com.yamidev.actividad10.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHelper {

    public static final int REQ_READ = 101;

    public static boolean hasReadImages(Activity a) {
        if (Build.VERSION.SDK_INT >= 33) {
            return ContextCompat.checkSelfPermission(a, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(a, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    public static void requestReadImages(Activity a) {
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(a,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQ_READ);
        } else {
            ActivityCompat.requestPermissions(a,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_READ);
        }
    }
}