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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.adapters.PhysicalActivityAdapter;
import com.udrishh.healthy.classes.PhysicalActivity;

import java.util.ArrayList;
import java.util.List;

public class AddPhysicalActivitySearchFragment extends Fragment {
    private View view;
    private MaterialButton addManuallyBtn;
    private MaterialButton continueBtn;
    private MaterialAutoCompleteTextView searchTv;
    private ArrayList<PhysicalActivity> physicalActivities = new ArrayList<>();
    private PhysicalActivity selectedPhysicalActivity = null;
    private FragmentManager fragmentManager;

    public AddPhysicalActivitySearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_physical_activity_search, container, false);
        initialiseComponents();
        setClickListeners();
        return view;
    }

    private void setClickListeners() {
        searchTv.setOnItemClickListener((parent, view, position, id) -> {
            continueBtn.setVisibility(View.VISIBLE);
            for (PhysicalActivity physicalActivity : physicalActivities) {
                if (physicalActivity.getName().equals(searchTv.getText().toString())) {
                    selectedPhysicalActivity = physicalActivity;
                }
            }
        });
        continueBtn.setOnClickListener(v -> moveToAddPhysicalActivityRecordDbFragment());
        addManuallyBtn.setOnClickListener(v -> moveToAddPhysicalActivityRecordManuallyFragment());
    }

    private void moveToAddPhysicalActivityRecordManuallyFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new AddPhysicalActivityManuallyDetailsFragment())
                .addToBackStack(null)
                .commit();
    }

    private void moveToAddPhysicalActivityRecordDbFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new AddPhysicalActivityDbDetailsFragment(selectedPhysicalActivity))
                .addToBackStack(null)
                .commit();
    }

    private void initialiseComponents() {
        addManuallyBtn = view.findViewById(R.id.add_activity_manual);
        continueBtn = view.findViewById(R.id.add_activity_continue);
        searchTv = view.findViewById(R.id.add_activity_search);
        physicalActivities = ((MainActivity) this.requireActivity()).getPhysicalActivities();
        if (selectedPhysicalActivity == null) {
            continueBtn.setVisibility(View.INVISIBLE);
        }
        searchTv.setAdapter(new PhysicalActivityAdapter(requireContext(), R.layout.physical_activity_item,
                (List<PhysicalActivity>) physicalActivities.clone()));
    }
}