package com.example.scanqrcode.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.scanqrcode.EAN;
import com.example.scanqrcode.R;
import com.example.scanqrcode.ScanGallery;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Fragment_Scan_Home extends Fragment{
    private final static int requestPer = 100;
    public final static String ALLOW_KEY = "ALLOWED";
    public final static String CAMERA_PREF = "camera_pref";
    ConstraintLayout noScan;
    FrameLayout Scan;
    boolean check = false;
    View view;
    CodeScanner mCodeScanner;
    ScanGallery scanGallery;
    LinearLayout btn_Cam, btn_Gallery;
    SeekBar zoom_Frame, zoom_Camera;
    ImageButton btn_QRCode, btn_BarCode, btn_increase, btn_decrease,
            btn_plus_frame, btn_minus_frame, btn_flash,
            btn_rotate_cam, btn_scan_gallery;
    CodeScannerView scannerView;
    boolean Flash = true, Switch = true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan_home, container, false);
        Scan = view.findViewById(R.id.scan);
        noScan = view.findViewById(R.id.noScan);
        btn_Cam = view.findViewById(R.id.btn_cam);
        btn_Cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        btn_Gallery = view.findViewById(R.id.btn_gallery);
        btn_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanByGallery();
            }
        });

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            check = true;
            openScan();
        }
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
        btn_scan_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanByGallery();
            }
        });
        return view;
    }

    private void ScanByGallery() {
                Intent photoItent = new Intent(Intent.ACTION_PICK);
                scanGallery = new ScanGallery(getActivity(), photoItent);
                photoItent.setType("image/*");
                startActivityForResult(photoItent, 101);

    }

    private void Scan() {
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
//            if(check == true)
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
        mCodeScanner.setAutoFocusEnabled(true);
        if (check == true)
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        if(check == true)
        mCodeScanner.releaseResources();
        super.onPause();
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
            check = true;
            openScan();
        }else if(requestCode == 101) {
            try {
                /*Sau khi hoàn hành xong 1 activity thì nó sẽ trả về trong biến data
                    dựa trên action code là requestCode , và xác định bằng resultCode
                  kết quả trả về sẽ là 1 Uri , từ Uri đó mình sẽ bỏ vào 1 InputStream để convert nó về Bitmap để set
                * */
                //Lấy dữ liệu từ data
                final Uri imageUri = data.getData();

                if(imageUri != null) {
                    //Lấy trình giải quyết nội dung và mở trình đầu vào
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    //Chuyển dữ liệu thành bitmap
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    Bitmap bMap = selectedImage;

                    //Lấy diện tích của hình ảnh
                    int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
                    //Đọc toàn bộ điểm ảnh vào 1 số nguyên
                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

                    //Chuyển các điểm ảnh thành chuỗi nhị phân
                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    Reader reader = new MultiFormatReader();
                    Result result = reader.decode(bitmap);

                    Intent intent = new Intent(getActivity(), EAN.class);
                    intent.putExtra("linksp", result.getText());
                    intent.putExtra("title", result.getBarcodeFormat().toString());
                    getActivity().startActivity(intent);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(requestCode == 100 && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            check = true;
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
                check = true;
                openScan();
            }
        } else {
            check = true;
            openScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case requestPer: {

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    check = true;
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
        new AlertDialog.Builder(getActivity()).setCancelable(false).setTitle(R.string.showper_title)//hello
                .setMessage(R.string.showper_message)
                .setNegativeButton(R.string.showper_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton(R.string.showper_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, requestPer);
                        requestPermissions(new String[] {Manifest.permission.CAMERA}, requestPer);

                    }
                }).show();
    }

    private void showSetting() {
        new AlertDialog.Builder(getActivity()).setCancelable(false).setTitle(R.string.showsetting_title)
                .setMessage(R.string.showsetting_message)
                .setNegativeButton(R.string.showper_cancel, new DialogInterface.OnClickListener() {
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
//        Fragment_Scan fragment2 = new Fragment_Scan();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment1, fragment2);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
        Scan.setVisibility(View.VISIBLE);
        noScan.setVisibility(View.GONE);

    }
}