package com.udrishh.healthy.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private User user;

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        user = ((MainActivity) this.requireActivity()).getUserObject();
        firebaseAuth = ((MainActivity) this.requireActivity()).getFirebaseAuth();

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        if (user != null) {
            initialiseComponents();
        }

        return view;
    }

    private void initialiseComponents() {
        logoutBtn = view.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null && firebaseAuth != null) {
                    firebaseAuth.signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    requireActivity().finish();
                }
            }
        });
    }
}