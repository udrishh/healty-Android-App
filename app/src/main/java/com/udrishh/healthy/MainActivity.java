package com.udrishh.healthy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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