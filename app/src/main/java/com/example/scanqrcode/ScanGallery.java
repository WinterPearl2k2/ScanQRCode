package com.example.scanqrcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ScanGallery extends AppCompatActivity {
    Context mcontext;
    Intent mIntent;
    private static final int request = 101;

    public ScanGallery(Context context, Intent mIntent) {
        this.mcontext = context;
        this.mIntent = mIntent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == request) {

            try {
                /*Sau khi hoàn hành xong 1 activity thì nó sẽ trả về trong biến data
                    dựa trên action code là requestCode , và xác định bằng resultCode
                  kết quả trả về sẽ là 1 Uri , từ Uri đó mình sẽ bỏ vào 1 InputStream để convert nó về Bitmap để set
                * */
                //Lấy dữ liệu từ data
                final Uri imageUri = data.getData();

                if(imageUri != null) {
                    //Lấy trình giải quyết nội dung và mở trình đầu vào
                    final InputStream imageStream = mcontext.getContentResolver().openInputStream(imageUri);
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

                        Intent intent = new Intent(mcontext, EAN.class);
                        intent.putExtra("linksp", result.getText());
                        intent.putExtra("title", result.getBarcodeFormat().toString());
                        mcontext.startActivity(intent);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(mcontext, "Something went wrong", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }else {
            Toast.makeText(mcontext, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
