package com.example.scanqrcode;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRImageActivity extends AppCompatActivity {
    private ImageView qrCodeIV;
    private TextView txtQrInfor, txtQrTitle;
    private Button generateQrBtn;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrimage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("QR Code");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primay_icon)));
        actionBar.setDisplayHomeAsUpEnabled(true);

        qrCodeIV = findViewById(R.id.imageQR);
        txtQrTitle = findViewById(R.id.QRTitle);
        txtQrInfor = findViewById(R.id.QRInf);
        txtQrInfor.setMovementMethod(new ScrollingMovementMethod());
//
        QRGenerator();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.to_right_1, R.anim.to_right_2);
    }
    private void QRGenerator(){


        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //initializing a variable for default display.
        Display display = manager.getDefaultDisplay();
        //creating a variable for point which is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);
        //getting width and height of a point
        int width = point.x;
        int height = point.y;
        //generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;
        Bundle extras = this.getIntent().getExtras();
        txtQrTitle.setText(extras.get("QRtitle").toString());
        txtQrInfor.setText(extras.get("QRinfor").toString());

        //setting this dimensions inside our qr code encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(txtQrInfor.getText().toString(), null, QRGContents.Type.TEXT, dimen);
        try {
            //getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image view using .setimagebitmap method.
            qrCodeIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            //this method is called for exception handling.
            Log.e("Tag", e.toString());
        }
    }
}