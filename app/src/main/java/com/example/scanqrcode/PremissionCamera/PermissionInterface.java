package com.example.scanqrcode.PremissionCamera;

public interface PermissionInterface {

    //Lấy mã quyền yêu cầu
    int getPermissionsRequestCode();
    //Yêu cầu quyền thành công
    void requestPermissionsSuccess();
    //Yêu cầu quyền không thành công
    void requestPermissionFail();
}
