package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

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
    private PhysicalActivity selectedPhysicalActivity = new PhysicalActivity();

    public AddPhysicalActivitySearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_physical_activity_search, container, false);
        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        addManuallyBtn = view.findViewById(R.id.add_activity_manual);
        continueBtn = view.findViewById(R.id.add_activity_continue);
        searchTv = view.findViewById(R.id.add_activity_search);
        physicalActivities = ((MainActivity) this.requireActivity()).getPhysicalActivities();

        searchTv.setAdapter(new PhysicalActivityAdapter(requireContext(), R.layout.physical_activity_item,
                (List<PhysicalActivity>) physicalActivities.clone()));
        searchTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                continueBtn.setVisibility(View.VISIBLE);
                for(PhysicalActivity physicalActivity : physicalActivities){
                    if(physicalActivity.getName().equals(searchTv.getText().toString())){
                        selectedPhysicalActivity = physicalActivity;
                    }
                }
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, new com.udrishh.healthy.fragments.AddPhysicalActivityDbDetailsFragment(selectedPhysicalActivity))
                        .commit();
            }
        });

        addManuallyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, new AddPhysicalActivityManuallyDetailsFragment())
                        .commit();
            }
        });
    }
}