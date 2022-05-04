package com.example.scanqrcode;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import java.util.Calendar;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class EAN extends AppCompatActivity {
    private ImageView qrCodeIV;
    private TextView txtQrInfor, txtQrTitle;
    private Button generateQrBtn;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    private String TO = "", SUB = "", BODY = "", T = "", S = "", P = "", H = "", TEL = "", SMS = "";
    String Json = "";
    String link = "";
    String LATITUDE = "", LONGITUDE = "";
    String FN = "", ORG = "", TITLE = "", ADR = "", WORK = "", CELL = "", FAX = "", EMAIL1 = "", EMAIL2 = "", URL = "";
    String SUMMARY = "", DTSTART = "", DTEND = "", LOCATION = "", DESCRIPTION = "";
    TextView view, txtTitleResult, txtSearch, txtAddContact;
    ImageView imgSearch,imgAddContact;
    LinearLayout button, lnShare, see_Code;
    TextClock txtClock;
    Intent intent;
    LinearLayout llAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primay_icon)));
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_ean_scan);
        txtSearch = findViewById(R.id.txt_search);
        imgSearch = findViewById(R.id.img_search);
        txtAddContact = findViewById(R.id.txt_add_contact);
        imgAddContact = findViewById(R.id.img_add_contact);
        llAddContact = findViewById(R.id.add_contact);
        view = findViewById(R.id.txtResult);
        txtTitleResult = findViewById(R.id.txtTitleResult);
        button = findViewById(R.id.button);
        txtClock = findViewById(R.id.txtClock);
        lnShare = findViewById(R.id.share);
        qrCodeIV = findViewById(R.id.imageQR);
//        see_Code = findViewById(R.id.see_code);
        String formatdate = "dd/MM/yyyy HH:mm";
        txtClock.setFormat24Hour(formatdate);
        intent = getIntent();
        XuLi();
        actionBar.setTitle(intent.getStringExtra("title"));
        view.setMovementMethod(new ScrollingMovementMethod());
    }

    private void shareOtherApp (String result) {
        lnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("text/plain");
//                share.putExtra(Intent.EXTRA_TEXT, result);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image Description", null);
                Uri screen = Uri.parse(path);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, screen);
                startActivity(Intent.createChooser(share, "Share image"));
            }
        });
    }

    private void seeCode(String result) {
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

        //setting this dimensions inside our qr code encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(result, null, QRGContents.Type.TEXT, dimen);
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

    private void ClickLink(String s) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(s));
                startActivity(intent);
            }
        });
    }

    public void XuLi() {
        String s = intent.getStringExtra("title");
        String result = intent.getStringExtra("linksp");
        String title = "";
        String ss = "";
        if( s.equals("QR_CODE")) {
            int d = 0;
            for(int i = 0; i < result.length() ; i++) {
                if(result.charAt(i) == ':') {
                    d = i;
                    break;
                }
                ss += result.charAt(i);
            }
            int flag = 0;
            switch (ss.toUpperCase()) {
                case "HTTPS": title = "URL";
                    txtTitleResult.setText(title);
                    for(int i = d ; i < result.length() ; i++) {
                        link += result.charAt(i);
                    }
                    txtSearch.setText("Truy cập liên kết");
                    imgSearch.setImageResource(R.drawable.ic_baseline_insert_link_36);
                    view.setText(result);
                    ClickLink(link.length() > 0 ? ("https:" + link) : "");
                    break;
                case "HTTP": title = "URL";
                    txtTitleResult.setText(title);
                    for(int i = d ; i < result.length() ; i++) {
                        link += result.charAt(i);
                    }
                    txtSearch.setText("Truy cập liên kết");
                    imgSearch.setImageResource(R.drawable.ic_baseline_insert_link_36);
                    view.setText(result);
                    ClickLink(link.length() > 0 ? ("http:" + link) : "");
                    break;
                case "MATMSG": title = "Email";
                    txtTitleResult.setText(title);
//                    String TO = "", SUB = "", BODY = "";
                    for(int i = d + 1 ; i < result.length() ; i++) {
                        if(result.charAt(i) == ':') {
                            for(int j = i + 1 ; j < result.length() ; j++) {
                                if(result.charAt(j) == ';') {
                                    d = j;
                                    flag++;
                                    break;
                                }
                                if(flag == 0)
                                    TO += result.charAt(j);
                                else if (flag == 1)
                                    SUB += result.charAt(j);
                                else if (flag == 2)
                                    BODY += result.charAt(j);
                            }
                        }else d = i;
                    }
                    txtSearch.setText("Gửi email từ " + TO);
                    imgSearch.setImageResource(R.drawable.ic_baseline_email_36);
                    view.setText((TO.length() > 0 ? ("To: " + TO): "")
                            + (SUB.length() > 0 ? ("\nSubject: " + SUB): "")
                            + (BODY.length() > 0 ? ("\nMessage: " + BODY): ""));

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent send = new Intent(Intent.ACTION_SENDTO);
                            send.setData(Uri.parse("mailto:" + TO + "?subject=" + SUB + "&body=" + BODY));
                            send.putExtra(Intent.EXTRA_SUBJECT,"Email subject" + SUB);
                            send.putExtra(Intent.EXTRA_TEXT, "Email body" + BODY);
                            startActivity(send);
                        }
                    });
                    break;
                case "WIFI": title = "WIFI";
                    txtTitleResult.setText(title);
                    for(int i = d + 1 ; i < result.length() ; i++) {
                        if(result.charAt(i) == ':') {
                            for(int j = i + 1 ; j < result.length() ; j++) {
                                if(result.charAt(j) == ';') {
                                    d = j;
                                    flag++;
                                    break;
                                }
                                if(flag == 0)
                                    T += result.charAt(j);
                                else if (flag == 1)
                                    S += result.charAt(j);
                                else if (flag == 2)
                                    P += result.charAt(j);
                                else if(flag == 3)
                                    H += result.charAt(j);
                            }
                        }else d = i;
                    }
                    txtSearch.setText("Chuyển hướng tới mạng Wifi");
                    imgSearch.setImageResource(R.drawable.ic_baseline_wifi_36);
                    view.setText((S.length() > 0 ? ("Network Name: " + S): "")
                            + (P.length() > 0 ? ("\nPassword: " + P): "")
                            + (T.length() > 0 ? ("\nEncrytion: " + T): "")
                            + "\nHidden: " + (H.equals("true")? H.toUpperCase() : "NO"));

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(EAN.this);
                            if(T.equals("WEP")) {
                                builder.setMessage("Rất tiếc, phương pháp mã hoá WEP không còn được Google hỗ trợ kể từ phiên bản Android 10 :(")
                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        }).show();
                            } else {
                                builder.setMessage("Chuyển hướng đến mạng Wifi: " + S
                                                    + ". \nMật khẩu của bạn đã được sao chép, ấn OK để chuyển hướng đến cài đặt.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent wifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                                startActivity(wifi);
                                                copyTextToClipboard(P);
                                                WifiConfiguration conf = new WifiConfiguration();
                                                conf.SSID = "\"" + S + "\"";
                                                conf.wepKeys[0] = "\"" + P + "\"";

                                                WifiManager manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                                                int netWorkID = manager.addNetwork(conf);
                                                if(netWorkID == -1) {
                                                    conf.wepKeys[0] = P;
                                                    netWorkID = manager.addNetwork(conf);
                                                }
                                                manager.disconnect();
                                                manager.enableNetwork(netWorkID, true);
                                                manager.reconnect();
                                            }
                                        })
                                        .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                }).show();
                            }
                        }
                    });
                    break;
                case "TEL": title = "TELEPHONE";
                    txtTitleResult.setText(title);
                    for(int i = d + 1 ; i < result.length() ; i++) {
                        TEL += result.charAt(i);
                    }
                    txtSearch.setText("Quay số");
                    imgSearch.setImageResource(R.drawable.ic_baseline_local_phone_36);
                    txtAddContact.setText("Thêm liên hệ");
                    imgAddContact.setImageResource(R.drawable.ic_baseline_add_ic_call_36);
                    llAddContact.setVisibility(View.VISIBLE);
                    llAddContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addContact(TEL);
                        }
                    });
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent call = new Intent(Intent.ACTION_CALL);
                            call.setData(Uri.parse("tel:" + TEL));
                            startActivity(call);
                        }
                    });
                    view.setText((TEL.length() > 0 ? ("Phone: " + TEL): ""));
                    break;
                case "SMSTO": title = "SMS";
                    txtTitleResult.setText(title);
                    for(int i = d + 1 ; i < result.length() ; i++) {
                        if(result.charAt(i) == ':') {
                            i++;
                            flag++;
                        }
                        if(flag == 0)
                            TEL += result.charAt(i);
                        else if (flag == 1)
                            SMS += result.charAt(i);
                    }
                    txtSearch.setText("Gửi tin nhắn");
                    imgSearch.setImageResource(R.drawable.ic_baseline_sms_36);
                    txtAddContact.setText("Thêm liên hệ");
                    imgAddContact.setImageResource(R.drawable.ic_baseline_add_ic_call_36);
                    llAddContact.setVisibility(View.VISIBLE);
                    llAddContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addContact(TEL);
                        }
                    });
                    view.setText((TEL.length() > 0 ? ("Phone: " + TEL): "")
                            + (SMS.length() > 0 ? ("\nMessage: " + SMS): ""));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent sms = new Intent(Intent.ACTION_SENDTO);
                            sms.putExtra("sms_body", SMS);
                            sms.setData(Uri.parse("sms:" + TEL));
                            startActivity(sms);
                        }
                    });
                    break;
                case "GEO" : title = "GEO";
                    txtTitleResult.setText(title);
                    for(int i = d + 1 ; i < result.length() ; i++) {
                        if(result.charAt(i) == ',') {
                            i++;
                            flag++;
                        }
                        if(flag == 0)
                            LATITUDE += result.charAt(i);
                        else if (flag == 1)
                            LONGITUDE += result.charAt(i);
                    }
                    txtSearch.setText("Truy cập vị trí");
                    imgSearch.setImageResource(R.drawable.ic_baseline_map_36);
                    view.setText((LATITUDE.length() > 0 ? ("LATITUDE: " + LATITUDE): "")
                            + (LONGITUDE.length() > 0 ? ("\nLONGITUDE: " + LONGITUDE): ""));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent map = new Intent(Intent.ACTION_VIEW);
                            map.setData(Uri.parse("geo:" + LATITUDE + "," + LONGITUDE + "?q=" + LATITUDE + "+" + LONGITUDE));
                            startActivity(map);
                        }
                    });
                    break;
                case "BEGIN":
                    for(int i = d + 1; i < result.length(); i++) {
                        if(result.charAt(i) == '\n') {
                            d = i+1;
                            break;
                        }
                        Json += result.charAt(i);
                    }
                    if(Json.equals("VCARD")) {
                        Json = "";
                        title = "VCARD";
                        txtTitleResult.setText(title);
                        for(int i = 0; i < result.length(); i++) {
                            for(int j = i; j < result.length(); j++) {
                                if(result.charAt(j) == ':' || result.charAt(j) == '\n') {
                                    i = j;
                                    break;
                                }
                                Json += result.charAt(j);
                            }
                            if(Json.equals("FN")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    FN += result.charAt(j);
                                }
                            } else if(Json.equals("ORG")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    ORG += result.charAt(j);
                                }
                            } else if(Json.equals("TITLE")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    TITLE += result.charAt(j);
                                }
                            } else if(Json.equals("ADR") || Json.equals("ADR;TYPE=HOME")) {
                                String string = "";
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j-1) == '\n') {
                                        i = j;
                                        flag++;
                                        break;
                                    }
                                    if(result.charAt(j) == ';' || result.charAt(j) == '\n') {
                                        if(string.length() > 0)
                                            if(result.charAt(j) == '\n')
                                                ADR += string;
                                            else
                                                ADR += string + ", ";
                                        string = "";
                                        continue;
                                    }
                                    string += result.charAt(j);
                                }
                            } else if(Json.equals("TEL;WORK;VOICE") || Json.equals("TEL;TYPE=WORK,VOICE")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    WORK += result.charAt(j);
                                }
                            } else if(Json.equals("TEL;CELL") || Json.equals("TEL;TYPE=HOME,VOICE") || Json.equals("TEL")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    CELL += result.charAt(j);
                                }
                            } else if(Json.equals("TEL;FAX") || Json.equals("TEL;TYPE=FAX,WORK,VOICE") || Json.equals("FAX")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    FAX += result.charAt(j);
                                }
                            } else if(Json.equals("EMAIL;TYPE=PREF,INTERNET") || Json.equals("EMAIL")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    EMAIL1 += result.charAt(j);
                                }
                            } else if(Json.equals("EMAIL;WORK;INTERNET") || Json.equals("EMAIL;TYPE=WORK,INTERNET")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    EMAIL2 += result.charAt(j);
                                }
                            } else if(Json.equals("URL")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    URL += result.charAt(j);
                                }
                            } else {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                }
                            }
                            Json = "";
                        }
//                        (EMAIL1.length() > 0 ? ("\nEmail: " + EMAIL1): ""
                        view.setText((FN.length() > 0 ? ("Full name: " + FN): "")
                                + (ORG.length() > 0 ? ("\nCompany: " + ORG): "")
                                + (TITLE.length() > 0 ? ("\nJob: " + TITLE): "")
                                + (ADR.length() > 0 ? ("\nAddress: " + ADR): "")
                                + (WORK.length() > 0 ? ("\nTelephone: " + WORK): "")
                                + (CELL.length() > 0 ? ("\nMobile number: " + CELL): "")
                                + (FAX.length() > 0 ? ("\nFax: " + FAX): "")
                                + (EMAIL1.length() > 0 ? ("\nEmail: " + EMAIL1): "")
                                + (EMAIL2.length() > 0 ? ("\nEmail: " + EMAIL2): "")
                                + (URL.length() > 0 ? ("\nLink: " + URL): "")) ;
                        txtAddContact.setText("Thêm liên hệ");
                        imgAddContact.setImageResource(R.drawable.ic_baseline_add_ic_call_36);
                        llAddContact.setVisibility(View.VISIBLE);
                        llAddContact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addContactVcard(FN, ORG, TITLE, ADR, WORK, CELL, FAX, EMAIL1, EMAIL2, URL);
                            }
                        });
                    } else if(Json.equals("VEVENT")) {
                        title = "EVENT";
                        txtTitleResult.setText(title);
                        Json = "";
                        for(int i = 0; i < result.length(); i++) {
                            for(int j = i; j < result.length(); j++) {
                                if(result.charAt(j) == ':') {
                                    i = j;
                                    break;
                                }
                                Json += result.charAt(j);
                            }
                            if (Json.equals("SUMMARY")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    SUMMARY += result.charAt(j);
                                }
                            } else if (Json.equals("DTSTART")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    DTSTART += result.charAt(j);
                                }
                            } else if (Json.equals("DTEND")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    DTEND += result.charAt(j);
                                }
                            } else if (Json.equals("LOCATION")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    LOCATION += result.charAt(j);
                                }
                            } else if (Json.equals("DESCRIPTION")) {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                    DESCRIPTION += result.charAt(j);
                                }
                            } else {
                                for(int j = i + 1; j < result.length(); j++) {
                                    if(result.charAt(j) == '\n') {
                                        i = j;
                                        break;
                                    }
                                }
                            }
                            Json = "";
                        }

                        view.setText((SUMMARY.length() > 0 ? ("Tiêu đề: " + SUMMARY): "")
                                    + (DTSTART.length() > 0 ? ("\nNgày bắt đầu: " + DTSTART): "")
                                    + (DTEND.length() > 0 ? ("\nNgày kết thúc: " + DTEND): "")
                                    + (LOCATION.length() > 0 ? ("\nĐịa điểm: " + LOCATION): "")
                                    + (DESCRIPTION.length() > 0 ? ("\nMô tả: " + DESCRIPTION): ""));
                        txtAddContact.setText("Thêm lịch trình");
                        imgAddContact.setImageResource(R.drawable.ic_baseline_edit_calendar_36);
                        llAddContact.setVisibility(View.VISIBLE);
                        llAddContact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Calendar calender = Calendar.getInstance();
                                Intent cal = new Intent(Intent.ACTION_EDIT);
                                cal.setType("vnd.android.cursor.item/event");
                                cal.putExtra("dtstart", DTSTART);
                                cal.putExtra("dtend", DTEND);
                                cal.putExtra("rrule", "FREQ=YEARLY");
                                cal.putExtra("allDay", 1);
                                cal.putExtra("title", SUMMARY);
                                cal.putExtra("description", DESCRIPTION);
                                cal.putExtra("eventLocation", LOCATION);
                                startActivity(cal);
                            }
                        });
                    }
                    button.setVisibility(View.GONE);
                    break;
                default: txtTitleResult.setText("Text");
                    view.setText(result);
                    button.setVisibility(View.GONE);
                    break;
            }
        } else {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '_')
                    break;
                ss += s.charAt(i);
            }
            if (ss.equals("EAN") || ss.equals("UPC")) {
                txtTitleResult.setText(R.string.EAN_title);
                ClickLink("https://www.google.com/search?q=" + result);
            } else {
                txtTitleResult.setText("Text");
                button.setVisibility(View.GONE);
            }
            view.setText(result);
        }

        shareOtherApp(result);
        seeCode(result);
    }

    private void copyTextToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", text);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(EAN.this, "Copy success!!!", Toast.LENGTH_SHORT).show();
    }

    private void addContact(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber);
        startActivity(intent);
    }

    private void addContactVcard(String name, String company, String job,
                                 String adr, String Telephone, String mobileNumber,
                                 String fax, String mail1, String mail2, String url) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, job);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, mobileNumber);
        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE, "Nhà riêng");
        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL, mail1);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, mail2);
        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE , "Cơ quan");
        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE , Telephone);
        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, adr);
        intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE, "Số fax cơ quan");
        intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, fax);
        intent.putExtra("website", url);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
    }
}