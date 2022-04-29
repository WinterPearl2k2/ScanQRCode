package com.example.scanqrcode.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.scanqrcode.R;
import com.example.scanqrcode.ScanGallery;

public class Fragment_Scan_Home extends Fragment{
    int requestPer = 10000;
    boolean check = false;
    ScanGallery scanGallery;
    View view;

    Button taolao, gallery;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        taolao = view.findViewById(R.id.bb);
        taolao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        gallery = view.findViewById(R.id.bb2);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoItent = new Intent(Intent.ACTION_PICK);
                scanGallery = new ScanGallery(getActivity(), photoItent);
                photoItent.setType("image/*");
                startActivityForResult(photoItent, 10000);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        scanGallery.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    new AlertDialog.Builder(getActivity()).setCancelable(false).setTitle("Chưa cấp quyền cam")
                            .setMessage("Vui lòng cấp quyền cho camera để sử dụng tín năng quét")
                            .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, requestPer);
                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(getActivity()).setCancelable(false).setTitle("Chưa cấp quyền cam")
                            .setMessage("Vui lòng cấp quyền cho camera để sử dụng tín năng quét QR")
                            .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            }).show();
                }
            } else {
//                requestPer = 10000;
                        Fragment_Scan fragment2 = new Fragment_Scan();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment1, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
            }
        }
    }
}