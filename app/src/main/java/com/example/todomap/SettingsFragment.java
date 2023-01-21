package com.example.todomap;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsFragment extends Fragment {
    View view;
    private Switch switchMode, switchBaseMap;
    String theme = "light", basemap = "normal";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Intent switchOptions =new Intent(getActivity(), MapFragment.class);
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        switchMode = (Switch) view.findViewById(R.id.switchMode);
        switchBaseMap = (Switch) view.findViewById(R.id.switchBaseMap);
//        Intent switchOptions = new Intent(getActivity(), MapFragment.class);
//        switchOptions.putExtra("modeSetting", theme);
//        switchOptions.putExtra("basemapSetting", basemap);
//        startActivity(switchOptions);
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchMode.isChecked()){
                    theme = "dark";
                }else{
                    theme = "light";
                }

//                switchOptions.putExtra("themeExtra", theme);
//                switchOptions.putExtra("basemapExtra", basemap);
//                startActivity(switchOptions);
            }
        });
        switchBaseMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchMode.isChecked()){
                    basemap = "satellite";
                }else{
                    basemap = "normal";
                }
//                Intent switchOptions =new Intent(getActivity(), MapFragment.class);
//                switchOptions.putExtra("themeExtra", theme);
//                switchOptions.putExtra("basemapExtra", basemap);
//                startActivity(switchOptions);
            }
        });
//        startActivity(switchOptions);

        return view;
    }
}