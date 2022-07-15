package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.PhysicalActivity;
import com.udrishh.healthy.classes.PhysicalActivityRecord;
import com.udrishh.healthy.classes.User;
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
        importObjects();
        initialiseComponents();
        setClickListener();
        return view;
    }

    private void importObjects() {
        user = ((MainActivity) this.requireActivity()).getUserObject();
    }

    private void setClickListener() {
        addBtn.setOnClickListener(v -> {
            boolean isValid = true;
            if (Objects.requireNonNull(quantityInput.getText()).toString().trim().length() <= 0) {
                quantityInput.setError(getString(R.string.invalid_quantity_text));
                isValid = false;
            }
            if (quantityInput.getText().toString().trim().length() > 0) {
                if (Integer.parseInt(quantityInput.getText().toString().trim()) <= 0 ||
                        Integer.parseInt(quantityInput.getText().toString().trim()) > 200) {
                    quantityInput.setError(getString(R.string.invalid_quantity_text));
                    isValid = false;
                }
            }
            if (Objects.requireNonNull(caloriesInput.getText()).toString().trim().length() <= 0) {
                caloriesInput.setError(getString(R.string.invalid_value_text));
                isValid = false;
            }
            if (caloriesInput.getText().toString().trim().length() > 0) {
                if (Integer.parseInt(caloriesInput.getText().toString().trim()) <= 0 ||
                        Integer.parseInt(caloriesInput.getText().toString().trim()) > 3000) {
                    caloriesInput.setError(getString(R.string.invalid_value_text));
                    isValid = false;
                }
            }
            if (Objects.requireNonNull(nameInput.getText()).toString().trim().length() <= 1) {
                nameInput.setError(getString(R.string.invalid_name_text));
            }
            if (!isValid) {
                return;
            }

            quantityInput.setError(null);
            nameInput.setError(null);

            createAndAddRecord();
            moveToProfileFragment();
        });
    }

    private void initialiseComponents() {
        addBtn = view.findViewById(R.id.add_activity_finish);
        nameInput = view.findViewById(R.id.activity_name_input);
        quantityInput = view.findViewById(R.id.activity_quantity_input);
        caloriesInput = view.findViewById(R.id.activity_burned_calories_input);
    }

    private void moveToProfileFragment() {
        BottomNavigationView bottomNavigationView =
                ((MainActivity) requireActivity()).getBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
    }

    private void createAndAddRecord() {
        PhysicalActivityRecord physicalActivityRecord = new PhysicalActivityRecord();
        physicalActivityRecord.setRecordId(UUID.randomUUID().toString());
        physicalActivityRecord.setDate(DateConverter.fromLongDate(new Date()));
        physicalActivityRecord.setUserId(user.getUserId());
        physicalActivityRecord.setItemId(UUID.randomUUID().toString());
        physicalActivityRecord.setName(Objects.requireNonNull(nameInput.getText()).toString().trim());
        physicalActivityRecord.setQuantity(Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()));

        PhysicalActivity physicalActivity = new PhysicalActivity();
        physicalActivity.setPhysicalActivityId(physicalActivityRecord.getItemId());
        physicalActivity.setUserId(user.getUserId());
        physicalActivity.setName(nameInput.getText().toString().trim());
        physicalActivity.setCalories((float) Integer.parseInt(Objects.requireNonNull(caloriesInput.getText()).toString()) /
                ((float) Integer.parseInt(quantityInput.getText().toString()) / 60 * user.getWeight()));

        ((MainActivity) requireActivity()).addPhysicalActivity(physicalActivity);
        ((MainActivity) requireActivity()).addPhysicalActivityRecord(physicalActivityRecord);
    }
}