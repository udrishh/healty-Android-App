package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udrishh.healthy.R;

public class StatisticsFragment extends Fragment {
    private View view;
    
    public StatisticsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
    }
}