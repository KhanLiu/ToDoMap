package com.example.todomap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsFragment extends Fragment {
    View view;
    private Switch switchMode, switchBaseMap, switchNavigation;
    private String theme = "light";
    private String basemap = "normal";
    private String navigation = "walk";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Intent switchOptions =new Intent(getActivity(), MapFragment.class);
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switchMode = (Switch) view.findViewById(R.id.switchMode);
        switchBaseMap = (Switch) view.findViewById(R.id.switchBaseMap);
        switchNavigation = (Switch) view.findViewById(R.id.switchNavigation);


        String mainTheme = new MainActivity().getMyTheme();
        String mainBasemap = new MainActivity().getBasemap();
        String mainNav = new MainActivity().getNavigation();
        Log.d("SettingFFFFF", "onViewCreated: " + mainTheme + mainBasemap+ mainNav);


        theme = mainTheme;
        basemap = mainBasemap;
        navigation = mainNav;

        if (mainTheme == "dark")
            switchMode.setChecked(true);
        if (mainTheme == "light")
            switchMode.setChecked(false);

        if (mainBasemap == "satellite")
            switchBaseMap.setChecked(true);
        if (mainBasemap == "normal")
            switchBaseMap.setChecked(false);

        if (mainNav == "driving-car")
            switchNavigation.setChecked(true);
        if (mainNav == "foot-walking")
            switchNavigation.setChecked(false);

        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchMode.isChecked()){
                    theme = "dark";
                }else{
                    theme = "light";
                }
                Log.d("sentTheme", "onCheckedChanged: " + theme);
            }
        });

        switchBaseMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchBaseMap.isChecked()){
                    basemap = "satellite";
                }else{
                    basemap = "normal";
                }
                Log.d("sentBasemap", "onCheckedChanged: " + basemap);
            }
        });

        switchNavigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchNavigation.isChecked()){
                    navigation = "driving-car";
                }else {
                    navigation = "foot-walking";
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Bundle result = new Bundle();
        result.putString("theme", theme);
        result.putString("basemap", basemap);
        result.putString("navigation", navigation);
        getParentFragmentManager().setFragmentResult("settings", result);

        new MainActivity().setConfig(theme, basemap, navigation);
    }
}