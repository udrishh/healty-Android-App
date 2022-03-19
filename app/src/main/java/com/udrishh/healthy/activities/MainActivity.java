package com.udrishh.healthy.activities;

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
import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.fragments.SettingsFragment;
import com.udrishh.healthy.fragments.AddFragment;
import com.udrishh.healthy.fragments.ProfileFragment;
import com.udrishh.healthy.fragments.RecipesFragment;
import com.udrishh.healthy.fragments.StatisticsFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    private User user;

    public User getUserObject(){
        return user;
    }

    private void setProfileFragment(){
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_profile);
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame_layout, new ProfileFragment())
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("userObject");

        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.main_frame_layout);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_profile);
        fragment = new ProfileFragment();
        fragmentManager.beginTransaction()
                .add(R.id.main_frame_layout, fragment)
                .commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_profile:
                        setProfileFragment();
                        break;
                    case R.id.menu_item_recipes:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_recipes);
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_frame_layout, new RecipesFragment())
                                .commit();
                        break;
                    case R.id.menu_item_add:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_add);
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_frame_layout, new AddFragment())
                                .commit();
                        break;
                    case R.id.menu_item_statistics:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_statistics);
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_frame_layout, new StatisticsFragment())
                                .commit();
                        break;
                    case R.id.menu_item_settings:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_settings);
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_frame_layout, new SettingsFragment())
                                .commit();
                        break;
                }
                return true;
            }
        });

    }
}