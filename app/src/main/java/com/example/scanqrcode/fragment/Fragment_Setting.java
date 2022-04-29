package com.example.scanqrcode.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanqrcode.EAN;
import com.example.scanqrcode.R;
import com.example.scanqrcode.Test1;
import com.example.scanqrcode.adapter;

import java.util.ArrayList;

public class Fragment_Setting extends Fragment {

    private RecyclerView rvItems;
    private adapter adapter;
    private ArrayList<Test1> arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        rvItems = view.findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        arrayList.add(new Test1("Long", 12));
        arrayList.add(new Test1("My", 13));
        arrayList.add(new Test1("Duong", 14));
        arrayList.add(new Test1("Duyen", 15));

        adapter = new adapter(getActivity(), arrayList);
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
}
