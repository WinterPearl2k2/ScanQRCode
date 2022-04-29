package com.example.scanqrcode.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ScanViewAdapter extends FragmentStateAdapter {


    public ScanViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Fragment_Scan_Home();

            case 1:
                return new Fragment_Create();

            case 2:
                return new Fragment_History();

            case 3:
                return new Fragment_Setting();
            default:
                return new Fragment_Scan_Home();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
