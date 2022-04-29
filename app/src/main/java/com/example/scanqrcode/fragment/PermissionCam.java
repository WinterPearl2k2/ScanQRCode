package com.example.scanqrcode.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.scanqrcode.PremissionCamera.DialogUtil;
import com.example.scanqrcode.PremissionCamera.PermissionHelper;
import com.example.scanqrcode.PremissionCamera.PermissionInterface;
import com.example.scanqrcode.R;


public class PermissionCam extends Fragment  implements View.OnClickListener, PermissionInterface {
    int requestPer = 10000;
    Button btn_Permission_camera;
    Button btn_Permission_phone;
    PermissionHelper mPermissionHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Activity activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_permission_cam, container, false);
        btn_Permission_camera = view.findViewById(R.id.hihi1);
        btn_Permission_camera.setOnClickListener(this);
        mPermissionHelper = new PermissionHelper(activity, this);
        mPermissionHelper.requestPermissions(new String[] {Manifest.permission.CAMERA});
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hihi1:
                requestPer = 0;
                mPermissionHelper.requestPermissions(new String[] {Manifest.permission.CAMERA});
                break;
        }
    }

    @Override
    public int getPermissionsRequestCode() {
        return requestPer;
    }

    @Override
    public void requestPermissionsSuccess() {
        if(requestPer == 10000) {

        }else if (requestPer == 0) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void requestPermissionFail() {
        if(requestPer == 10000) {

        } else if(requestPer == 0) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                DialogUtil.showSelectDiaLog(getActivity(), "Quyền máy ảnh không khả dụng", "Cần có quyền đối với máy ảnh để kiểm tra máy ảnh", "Huỷ", "Đồng ý", new DialogUtil.DialogClickListener() {
                    @Override
                    public void confirm() {
                        DialogUtil.showSelectDiaLog(getActivity(), "Quyền đối với máy ảnh không khả dụng", "ui lòng cho phép APP sử dụng quyền đối với máy ảnh để kiểm tra", "Huỷ", "Đồng ý", new DialogUtil.DialogClickListener() {
                            @Override
                            public void confirm() {
                                goToAppSetting();
                            }

                            @Override
                            public void cancel() {

                            }
                        }).show();
                    }

                    @Override
                    public void cancel() {

                    }
                }).show();
            } else {
                DialogUtil.showSelectDiaLog(getActivity(), "Quyền đối với máy ảnh không khả dụng", "ui lòng cho phép APP sử dụng quyền đối với máy ảnh để kiểm tra", "Huỷ", "Đồng ý", new DialogUtil.DialogClickListener() {
                    @Override
                    public void confirm() {
                        goToAppSetting();
                    }

                    @Override
                    public void cancel() {

                    }
                }).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPermissionHelper.requestPermissionsResult(requestCode, permissions, grantResults))
            return;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("pakage", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 123);
    }
}