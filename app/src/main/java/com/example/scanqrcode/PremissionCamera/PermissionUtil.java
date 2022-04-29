package com.example.scanqrcode.PremissionCamera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;

public class PermissionUtil {
    /**
    * Yêu cầu quyền
    * @param activity
     * @param permissions
     * @param requestCode
    */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(permissions, requestCode);
        }
    }

    /**
    * Trả về các quyền bị thiếu
    * @param context
    * @param permissions
     * @return trả về quyền bị thiếu, null là không còn quyền bị thiếu
    */
    public static String[] getDeniePermissions(Context context, String[] permissions) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> deniedPermissionList = new ArrayList<>();
            for(String permission : permissions) {
                if(context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissionList.add(permission);
                }
            }
            int size = deniedPermissionList.size();
            if(size > 0) {
                return deniedPermissionList.toArray(new String[deniedPermissionList.size()]);
            }
        }
        return null;
    }
}
