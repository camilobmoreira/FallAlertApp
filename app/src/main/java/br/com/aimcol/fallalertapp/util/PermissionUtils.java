package br.com.aimcol.fallalertapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

import br.com.aimcol.fallalertapp.activity.MainActivity;

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

    /**
     * @param context {@link android.content.ContextWrapper#getApplicationContext()}
     * @param permission {@link Manifest.permission}
     * @param requestCode
     */
    public static void requestPermission(Context context, String permission, int requestCode) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasPermission = context.checkSelfPermission(permission);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                ((Activity) context).requestPermissions(new String[] {permission}, requestCode);
            }
        }
    }

    /**
     *
     * @param context
     * @param action {@link Settings* }
     */
    public static void requestPermissionOnSettings(Context context, String action) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Intent myIntent = new Intent(action);
            context.startActivity(myIntent);
        }
    }
}
