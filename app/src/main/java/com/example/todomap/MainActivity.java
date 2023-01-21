package com.example.todomap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.todomap.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static String myTheme = "light";
    public static String basemap = "normal";
    public static String navigation = "foot-walking";

    public void setConfig(String theme, String basemap, String navigation){
        this.myTheme = theme;
        this.basemap = basemap;
        this.navigation = navigation;
    }

    public String getNavigation() {
        return navigation;
    }

    public String getBasemap() {
        return basemap;
    }

    public String getMyTheme() {
        return myTheme;
    }

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // start tutorial activity at the first time
        firstTime();

        // set style
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }


        // initialize
        replaceFragment(new TaskFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.task:
                    replaceFragment(new TaskFragment());
                    break;
                case R.id.map:
                    replaceFragment(new MapFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    break;

            }

            return true;
        });
    }

    private void firstTime() {
        SharedPreferences sharedTime = getSharedPreferences("tutorial",0);
        if (sharedTime.getBoolean("firstTime",true))
        {

            // first time tutorial
            Intent tutorialIntent = new Intent(this,TutorialActivity.class);
            startActivity(tutorialIntent);

            sharedTime.edit().putBoolean("firstTime",false).apply();
        }
        else
        {
            // when not using tutorial
            replaceFragment(new TaskFragment());

        }
    }

    // change view
    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

}