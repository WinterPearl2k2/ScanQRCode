package com.example.scanqrcode;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.scanqrcode.fragment.ScanViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;
    ViewPager2 viewPager;
    ScanViewAdapter scanViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        AnhXa();
        SetupViewPager2();
        EventButtonNavigation();
    }

    private void SetupViewPager2() {
        scanViewAdapter = new ScanViewAdapter(this);
        viewPager.setAdapter(scanViewAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        navigation.getMenu().findItem(R.id.navigation_scan).setChecked(true);
                        break;
                    case 1:
                        navigation.getMenu().findItem(R.id.navigation_create).setChecked(true);
                        break;
                    case 2:
                        navigation.getMenu().findItem(R.id.navigation_memory).setChecked(true);
                        break;
                    case 3:
                        navigation.getMenu().findItem(R.id.navigation_setting).setChecked(true);
                        break;
                    default:
                        navigation.getMenu().findItem(R.id.navigation_scan).setChecked(true);
                        break;
                }
            }
        });
    }

    private void EventButtonNavigation() {
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_create:
                    viewPager.setCurrentItem(1);
                    break;

                case R.id.navigation_scan:
                    viewPager.setCurrentItem(0);
                    break;

                case R.id.navigation_memory:
                    viewPager.setCurrentItem(2);
                    break;

                case R.id.navigation_setting:
                    viewPager.setCurrentItem(3);
                    break;
            }
            return true;
        });
    }

    boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                    return false;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
        return true;
    }

    private void AnhXa() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        viewPager = findViewById(R.id.viewPager);
    }



}