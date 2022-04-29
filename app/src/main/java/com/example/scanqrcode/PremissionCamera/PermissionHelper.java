package com.example.scanqrcode.PremissionCamera;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

public class PermissionHelper {
    private Activity mActivity;
    private PermissionInterface mPermissionInterface;

    public PermissionHelper(@NonNull Activity activity, @NonNull PermissionInterface permissionInterface) {
        mActivity = activity;
        mPermissionInterface = permissionInterface;
    }

    /**
     * Bắt đầu yêu cầu quyền
     * Kiểm tra Android M trở lên
     * Nêu không phải android M trở lên, nó cũng sẽ gọi lại phương thức requestPermissionsSuccess
     */
    public void requestPermissions(String[] permissions) {
        String[] deniedPermissions = PermissionUtil.getDeniePermissions(mActivity, permissions);
        if(deniedPermissions != null && deniedPermissions.length > 0) {
            PermissionUtil.requestPermissions(mActivity, deniedPermissions, mPermissionInterface.getPermissionsRequestCode());
        } else {
            mPermissionInterface.requestPermissionsSuccess();
        }
    }

    /**
     * Gọi onRequestPermissionsResult trong Activity
     * @param  requestCode
     * @param permissons
     * @param grantResults
     * @return true: Mã yêu cầu được quan tâm và loại bỏ
     *          false: Không quan tâm và không xử lý nó
     */
    public boolean requestPermissionsResult(int requestCode, @NonNull String[] permissons, @NonNull int[] grantResults) {
        if(requestCode == mPermissionInterface.getPermissionsRequestCode()) {
            boolean isAllGranted = true; //Tất cả quyền được cấp phép
            for (int result: grantResults) {
                if(result == PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            //Được cấp quyền đầy đủ
            if(isAllGranted)
                mPermissionInterface.requestPermissionsSuccess();
            //Cấp quyền không đầy đủ
            else
                mPermissionInterface.requestPermissionFail();
        }
        return false;
    }
}
