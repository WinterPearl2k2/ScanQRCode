package com.example.scanqrcode.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.scanqrcode.R;

public class Fragment_Create extends Fragment {
    FrameLayout hoho;
    ImageView imageView;
    private float x1, x2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        hoho = view.findViewById(R.id.hoho);
        imageView = view.findViewById(R.id.hjhj);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getX();
                        float deltaX = x2 - x1;
                        if (deltaX < 0) {
                            Toast.makeText(activity,
                                    "Right to Left swipe",
                                    Toast.LENGTH_SHORT).show();
                        }else if(deltaX >0){
                            Toast.makeText(activity,
                                    "Left to Right swipe",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

                return false;
            }
        });
        return view;
    }
}