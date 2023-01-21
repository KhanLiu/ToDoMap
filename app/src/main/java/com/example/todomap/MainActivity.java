package com.example.todomap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.todomap.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static String myTheme;
    public static String basemap;
    public static String navigation;

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

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

}