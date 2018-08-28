package br.com.aimcol.fallalertapp.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {

    /**
     *
     * @param context {@link android.content.ContextWrapper#getApplicationContext()}
     * @param permission {@link Manifest.permission}
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }
}
