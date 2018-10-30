package com.yuanbin.netmonitor.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PermissionUtils {

    /**
     * 检测GPS、位置权限
     */
    public static void getLocatePermission(Activity context, int requestCode) {

        if (Build.VERSION.SDK_INT >= 27) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        requestCode);
            } else {
                if(Build.VERSION.SDK_INT >= 27)// android 8.1
                    openGPS(context,requestCode);
            }
        }
    }

    private static void openGPS(Activity context,int requestCode){
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!ok) {//if no
            Toast.makeText(context, "Plese Open the GPS Service", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivityForResult(intent, requestCode);
        }
    }
}
