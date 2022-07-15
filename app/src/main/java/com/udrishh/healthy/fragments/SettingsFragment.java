package com.udrishh.healthy.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.LoginActivity;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.User;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    private View view;
    private FirebaseAuth firebaseAuth;
    private MaterialButton logoutBtn;
    private MaterialButton editUserDataBtn;
    private MaterialButton editCaloriesPlanBtn;
    private User user;
    private FragmentManager fragmentManager;

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        importObjects();
        initialiseComponents();
        setClickListeners();
        return view;
    }

    private void setClickListeners() {
        logoutBtn.setOnClickListener(v -> {
            if (user != null && firebaseAuth != null) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                requireActivity().finish();
            }
        });
        editUserDataBtn.setOnClickListener(v -> moveToEditUserDataFragment());
        editCaloriesPlanBtn.setOnClickListener(v -> moveToEditCaloriesPlanFragment());
    }

    private void moveToEditCaloriesPlanFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new EditCaloriesPlanFragment())
                .addToBackStack(null)
                .commit();
    }

    private void moveToEditUserDataFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new EditUserDataFragment())
                .addToBackStack(null)
                .commit();
    }

    private void importObjects() {
        user = ((MainActivity) this.requireActivity()).getUserObject();
        firebaseAuth = ((MainActivity) this.requireActivity()).getFirebaseAuth();
    }

    private void initialiseComponents() {
        logoutBtn = view.findViewById(R.id.logout_btn);
        editUserDataBtn = view.findViewById(R.id.edit_user_data_btn);
        editCaloriesPlanBtn = view.findViewById(R.id.edit_calories_plan_btn);
    }
}