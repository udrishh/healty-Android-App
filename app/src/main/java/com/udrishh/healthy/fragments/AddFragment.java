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

public class AddFragment extends Fragment {
    private View view;

    private MaterialButton addFoodBtn;
    private MaterialButton addDrinkBtn;
    private MaterialButton addWaterBtn;
    private MaterialButton addPhysicalActivityBtn;
    private MaterialButton addMeasurementBtn;
    private FragmentManager fragmentManager;

    public AddFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add, container, false);
        initialiseComponents();
        setClickListeners();
        return view;
    }

    private void setClickListeners() {
        addFoodBtn.setOnClickListener(view -> moveToAddFoodFragment());
        addDrinkBtn.setOnClickListener(view -> moveToAddDrinkFragment());
        addWaterBtn.setOnClickListener(view -> moveToAddWaterFragment());
        addPhysicalActivityBtn.setOnClickListener(view -> moveToAddPhysicalActivityFragment());
        addMeasurementBtn.setOnClickListener(view -> moveToAddMeasurementFragment());
    }

    private void moveToAddMeasurementFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new AddMeasurementChooseFragment())
                .addToBackStack(null)
                .commit();
    }

    private void moveToAddPhysicalActivityFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new AddPhysicalActivitySearchFragment())
                .addToBackStack(null)
                .commit();
    }

    private void moveToAddWaterFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new AddWaterFragment())
                .addToBackStack(null)
                .commit();
    }

    private void moveToAddDrinkFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new AddDrinksSearchFragment())
                .addToBackStack(null)
                .commit();
    }

    private void moveToAddFoodFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new AddFoodsSearchFragment())
                .addToBackStack(null)
                .commit();
    }

    private void initialiseComponents() {
        addFoodBtn = view.findViewById(R.id.foods_category_btn);
        addDrinkBtn = view.findViewById(R.id.drinks_category_btn);
        addWaterBtn = view.findViewById(R.id.water_category_btn);
        addPhysicalActivityBtn = view.findViewById(R.id.activities_category_btn);
        addMeasurementBtn = view.findViewById(R.id.measurements_category_btn);
    }
}