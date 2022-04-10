package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.Food;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.PhysicalActivity;
import com.udrishh.healthy.classes.PhysicalActivityRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.utilities.DateConverter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class AddPhysicalActivityManuallyDetailsFragment extends Fragment {
    private View view;
    private MaterialButton addBtn;
    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;
    private TextInputEditText caloriesInput;

    private User user;

    public AddPhysicalActivityManuallyDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_physical_activity_manually_details, container, false);
        user = ((MainActivity) this.requireActivity()).getUserObject();

        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        addBtn = view.findViewById(R.id.add_activity_finish);
        nameInput = view.findViewById(R.id.activity_name_input);
        quantityInput = view.findViewById(R.id.activity_quantity_input);
        caloriesInput = view.findViewById(R.id.activity_burned_calories_input);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                if (quantityInput.getText().toString().trim().length() <= 0) {
                    quantityInput.setError(getString(R.string.invalid_quantity_text));
                    isValid = false;
                }
                if (quantityInput.getText().toString().trim().length() > 0) {
                    if (Integer.parseInt(quantityInput.getText().toString().trim()) <= 0) {
                        quantityInput.setError(getString(R.string.invalid_quantity_text));
                        isValid = false;
                    }
                }
                if (caloriesInput.getText().toString().trim().length() <= 0) {
                    caloriesInput.setError(getString(R.string.invalid_value_text));
                    isValid = false;
                }
                if (caloriesInput.getText().toString().trim().length() > 0) {
                    if (Integer.parseInt(caloriesInput.getText().toString().trim()) <= 0) {
                        caloriesInput.setError(getString(R.string.invalid_value_text));
                        isValid = false;
                    }
                }
                if (nameInput.getText().toString().trim().length() <= 1) {
                    nameInput.setError(getString(R.string.invalid_name_text));
                }
                if (!isValid) {
                    return;
                }

                quantityInput.setError(null);
                nameInput.setError(null);

                PhysicalActivityRecord physicalActivityRecord = new PhysicalActivityRecord();
                physicalActivityRecord.setRecordId(UUID.randomUUID().toString());
                physicalActivityRecord.setDate(DateConverter.fromLongDate(new Date()));
                physicalActivityRecord.setUserId(user.getUserId());
                physicalActivityRecord.setItemId(UUID.randomUUID().toString());
                physicalActivityRecord.setName(nameInput.getText().toString().trim());
                physicalActivityRecord.setCalories(-1);
                physicalActivityRecord.setDuration(Integer.parseInt(quantityInput.getText().toString().trim()));
                physicalActivityRecord.setTotalCalories(Integer.parseInt(caloriesInput.getText().toString().trim()));

                ((MainActivity) requireActivity()).addPhysicalActivityRecord(physicalActivityRecord);

                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, new ProfileFragment())
                        .commit();

                Toast.makeText(getContext(), R.string.record_added_text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}