package com.example.scanqrcode.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private final static int requestPer = 100, requestPer2 = 200, requestPer3 = 300;
    public final static String ALLOW_KEY = "ALLOWED";
    public final static String CAMERA_PREF = "camera_pref";
    boolean check = false;
    ScanGallery scanGallery;
    View view;
    int d = 0;

    Button taolao, gallery;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[] {Manifest.permission.CAMERA},
//                        requestPer);
//            } else {
//                openScan();
//            }
//        } else openScan();

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
                startActivityForResult(photoItent, 101);
            }
        });

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openScan();
        }
        return view;
    }

    public static void saveToPreferences(Context context, String key, Boolean allowed) {
        SharedPreferences mPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefersEditor = mPrefs.edit();
        prefersEditor.putBoolean(key, allowed);
        prefersEditor.commit();
    }

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences mPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE);
        return (mPrefs.getBoolean(key, false));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openScan();
        }else if(requestCode == 101) {
            scanGallery.onActivityResult(requestCode, resultCode, data);
        } else if(requestCode == 100 && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openScan();
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if(getFromPref(getActivity(), ALLOW_KEY)) {
                    showSetting();
                } else if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CAMERA)) {
                        showPer();
                    } else {
                        requestPermissions(new String[] {Manifest.permission.CAMERA}, requestPer);
//                        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, requestPer);
                    }
                }
            } else {
                openScan();
            }
        } else openScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case requestPer: {

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openScan();
                    break;
                }

                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale =
                                ActivityCompat.shouldShowRequestPermissionRationale
                                        (getActivity(), permission);
                        if (showRationale) {
                            showPer();
//                            saveToPreferences(getActivity(), ALLOW_KEY, true);
                        } else if (!showRationale) {
                            // user denied flagging NEVER ASK AGAIN
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting
                            saveToPreferences(getActivity(), ALLOW_KEY, true);
                        }
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void showPer() {
        new AlertDialog.Builder(getActivity()).setCancelable(false).setTitle("Chưa cấp quyền cam")
                .setMessage("Vui lòng cấp quyền cho camera để sử dụng tín năng quét")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, requestPer);
                        requestPermissions(new String[] {Manifest.permission.CAMERA}, requestPer);

                    }
                }).show();
    }

    private void showSetting() {
        new AlertDialog.Builder(getActivity()).setCancelable(false).setTitle("Chưa cấp quyền cam")
                .setMessage("Vui lòng cấp quyền cho camera để sử dụng tín năng quét QR")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 200);
                    }
                }).show();
    }

    private void openScan() {
        Fragment_Scan fragment2 = new Fragment_Scan();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment1, fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
//        FragmentManager.BackStackEntry first = getActivity().getSupportFragmentManager().getBackStackEntryAt(0);
//        getActivity().getSupportFragmentManager().popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }
}