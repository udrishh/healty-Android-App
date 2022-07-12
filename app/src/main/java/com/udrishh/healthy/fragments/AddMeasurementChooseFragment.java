package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.udrishh.healthy.R;
import com.udrishh.healthy.enums.RecordType;

public class AddMeasurementChooseFragment extends Fragment {
    private View view;
    private MaterialButton weightBtn;
    private MaterialButton heightBtn;

    public AddMeasurementChooseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_measurement_choose, container, false);
        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
       weightBtn = view.findViewById(R.id.add_weight_btn);
       heightBtn = view.findViewById(R.id.add_height_btn);

       weightBtn.setOnClickListener(view -> {
           FragmentManager fragmentManager = getParentFragmentManager();
           fragmentManager.beginTransaction()
                   .replace(R.id.main_frame_layout, new AddMeasurementDetailsFragment(RecordType.WEIGHT))
                   .addToBackStack(null)
                   .commit();
       });

        heightBtn.setOnClickListener(view -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, new AddMeasurementDetailsFragment(RecordType.HEIGHT))
                    .addToBackStack(null)
                    .commit();
        });
    }
}