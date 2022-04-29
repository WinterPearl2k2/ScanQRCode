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
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.scanqrcode.EAN;
import com.example.scanqrcode.R;
import com.example.scanqrcode.ScanGallery;
import com.google.zxing.Result;

public class Fragment_Scan_Home extends Fragment{
//    View view;
//    boolean cameraPer = false;
//
    int requestPer = 10000;
    boolean check = false;
//    ScanGallery scanGallery;

//    private int requestCode = 10000;
CodeScanner mCodeScanner;
    ScanGallery scanGallery;
    SeekBar zoom_Frame, zoom_Camera;
    ImageButton btn_QRCode, btn_BarCode, btn_increase, btn_decrease,
            btn_plus_frame, btn_minus_frame, btn_flash,
            btn_rotate_cam, btn_scan_gallery;
    CodeScannerView scannerView;
    boolean Flash = true, Switch = true;
    View view;
    boolean cameraPer = false;

    Button taolao, gallery;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                check = true;
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
            } else if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED) {
                check = false;
                view = inflater.inflate(R.layout.fragment_scan_home, container, false);
                btn_QRCode = view.findViewById(R.id.btn_QRCODE);
                btn_BarCode = view.findViewById(R.id.btn_BarCode);
                btn_increase = view.findViewById(R.id.increase);
                btn_decrease = view.findViewById(R.id.decrease);
                zoom_Frame = view.findViewById(R.id.zoom_frame);
                zoom_Camera = view.findViewById(R.id.zoom_camera);
                btn_minus_frame = view.findViewById(R.id.minus_frame);
                btn_plus_frame = view.findViewById(R.id.plus_frame);
                scannerView = view.findViewById(R.id.scanner_view);
                btn_flash = view.findViewById(R.id.btn_flash);
                btn_rotate_cam = view.findViewById(R.id.rotate_cam);
                btn_scan_gallery = view.findViewById(R.id.scan_gallery);
                mCodeScanner = new CodeScanner(getActivity(), scannerView);
                Scan();
                ZoomFrame();
                ZoomCamera();
                FlashSwitch();
                RotateCamera();
                ScanByGallery();
            }
        }

//        view = inflater.inflate(R.layout.fragment_setting, container, false);
//        taolao = view.findViewById(R.id.bb);
//        taolao.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkPermission();
//            }
//        });
//        gallery = view.findViewById(R.id.bb2);
//        gallery.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent photoItent = new Intent(Intent.ACTION_PICK);
//                scanGallery = new ScanGallery(getActivity(), photoItent);
//                photoItent.setType("image/*");
//                startActivityForResult(photoItent, 10000);
//            }
//        });
        return view;
    }
    private void ScanByGallery() {
        btn_scan_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoItent = new Intent(Intent.ACTION_PICK);
                scanGallery = new ScanGallery(getActivity(), photoItent);
                photoItent.setType("image/*");
                startActivityForResult(photoItent, 10000);
            }
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        scanGallery.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private void Scan() {
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), EAN.class);
                        intent.putExtra("linksp", result.getText());
                        intent.putExtra("title", result.getBarcodeFormat().toString());
                        startActivity(intent);
//                        result.getBarcodeFormat().toString()
//                        Toast.makeText(getActivity(), result.getText()+"", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        scannerView.setOnClickListener(view -> {
            mCodeScanner.startPreview();
            mCodeScanner.setAutoFocusEnabled(true);
        });
    }

    private void RotateCamera() {
        btn_rotate_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Switch == true) {
                    mCodeScanner.setCamera(1);
                    mCodeScanner.setAutoFocusEnabled(true);
                    Switch = false;
                }else {
                    mCodeScanner.setCamera(0);
                    mCodeScanner.setAutoFocusEnabled(true);
                    Switch = true;
                }
            }
        });
    }

    private void FlashSwitch() {
        btn_flash.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(Flash == true) {
                    mCodeScanner.setFlashEnabled(true);
                    Flash = false;
                    btn_flash.setImageResource(R.drawable.ic_flash_off);
                }else {
                    mCodeScanner.setFlashEnabled(false);
                    Flash = true;
                    btn_flash.setImageResource(R.drawable.ic_flash_on);
                }
            }
        });
    }

    private void ZoomCamera() {
        btn_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zoom_Camera.getProgress() >= zoom_Camera.getMax()) {
                    mCodeScanner.setZoom(zoom_Camera.getProgress());
                }else {
                    zoom_Camera.setProgress(zoom_Camera.getProgress() + 5);
                    mCodeScanner.setZoom(zoom_Camera.getProgress());
                }
            }
        });
        btn_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zoom_Camera.getProgress() <= 0) {
                    mCodeScanner.setZoom(zoom_Camera.getProgress());
                }else {
                    zoom_Camera.setProgress(zoom_Camera.getProgress() - 5);
                    mCodeScanner.setZoom(zoom_Camera.getProgress());
                }
            }
        });

        zoom_Camera.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mCodeScanner.setZoom(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void ZoomFrame() {
        zoom_Frame.setMax(80);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            zoom_Frame.setMin(20);
        }
        btn_QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    zoom_Frame.setMin(20);
                }
                scannerView.setFrameAspectRatioWidth(1);
                btn_BarCode.setBackgroundResource(R.drawable.cus_btn_scanqr);
                btn_QRCode.setBackgroundResource(R.drawable.cus_btn_scanqr2);
            }
        });

        btn_BarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    zoom_Frame.setMin(40);
                }

                scannerView.setFrameAspectRatioWidth(3);
                btn_BarCode.setBackgroundResource(R.drawable.cus_btn_scanqr2);
                btn_QRCode.setBackgroundResource(R.drawable.cus_btn_scanqr);
            }
        });

        btn_plus_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (zoom_Frame.getProgress() >= 80) {
                    scannerView.setFrameSize(zoom_Frame.getProgress()/100F);
                }else {
                    zoom_Frame.setProgress(zoom_Frame.getProgress() + 6);
                    scannerView.setFrameSize(zoom_Frame.getProgress() / 100F);
                }
            }
        });

        btn_minus_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (zoom_Frame.getProgress() <= 20) {
                    scannerView.setFrameSize(zoom_Frame.getProgress()/100F);
                }else {
                    zoom_Frame.setProgress(zoom_Frame.getProgress() - 6);
                    scannerView.setFrameSize(zoom_Frame.getProgress() / 100F);
                }
            }
        });

        zoom_Frame.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                scannerView.setFrameSize(i/100F);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                scannerView.setFrameSize(zoom_Frame.getProgress()/100F);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(check == false) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    public void onPause() {
        if(check == false)
            mCodeScanner.releaseResources();
        super.onPause();
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
//                        Fragment_Scan fragment2 = new Fragment_Scan();
//                        FragmentManager fragmentManager = getFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.fragment1, fragment2);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();

            }
        }
    }
}