package com.udrishh.healthy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.main_frame_layout);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_recipes:
                        getSupportActionBar().setTitle("Recipes");
                        break;
                    case R.id.menu_item_profile:
                        getSupportActionBar().setTitle("Profile");
                        if(fragment == null) {
                            fragment = new ProfileFragment();
                            fragmentManager.beginTransaction()
                                    .add(R.id.main_frame_layout, fragment)
                                    .commit();
                        }
                        break;
                    case R.id.menu_item_add:
                        getSupportActionBar().setTitle("Add");
                        break;
                    case R.id.menu_item_statistics:
                        getSupportActionBar().setTitle("Statistics");
                        break;
                    case R.id.menu_item_settings:
                        getSupportActionBar().setTitle("Settings");
                        break;
                    default:
                        //TODO
                }
                return true;
            }
        });

    }
}