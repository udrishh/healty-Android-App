package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.MeasurementRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.utilities.DateConverter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class AddMeasurementDetailsFragment extends Fragment {
    private View view;
    private RecordType recordType;
    private TextInputEditText nameInput;
    private TextInputEditText valueInput;
    private User user;

    public AddMeasurementDetailsFragment() {
    }

    public AddMeasurementDetailsFragment(RecordType recordType) {
        this.recordType = recordType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_measurement_detalis, container, false);
        importObjects();
        initialiseComponents();
        return view;
    }

    private void importObjects() {
        user = ((MainActivity) this.requireActivity()).getUserObject();
    }

    private void initialiseComponents() {
        TextView title = view.findViewById(R.id.add_measurement_title);
        nameInput = view.findViewById(R.id.measurement_name_input);
        valueInput = view.findViewById(R.id.measurement_value_input);
        TextInputLayout valueInputLayout = view.findViewById(R.id.measurement_value_input_layout);
        MaterialButton addBtn = view.findViewById(R.id.add_measurement_finish);
        if (recordType == RecordType.HEIGHT) {
            title.setText(getString(R.string.add_height_title));
            valueInputLayout.setHint(getString(R.string.add_height_value_hint));
        } else {
            title.setText(getString(R.string.add_weight_title));
            valueInputLayout.setHint(getString(R.string.add_weight_value_hint));
        }
        addBtn.setOnClickListener(v -> {
            if (inputsValid()) {
                createAndAddRecord();
                moveToProfileFragment();
            }
        });
    }

    private boolean inputsValid() {
        if (Objects.requireNonNull(valueInput.getText()).toString().trim().length() <= 0) {
            valueInput.setError(getString(R.string.invalid_value_text));
            return false;
        }
        if (valueInput.getText().toString().trim().length() > 0) {
            if (recordType == RecordType.HEIGHT) {
                if (Integer.parseInt(valueInput.getText().toString().trim()) < user.getHeight() - 10
                        || Integer.parseInt(valueInput.getText().toString().trim()) > user.getHeight() + 10) {
                    valueInput.setError(getString(R.string.invalid_value_text));
                    return false;
                }
            } else {
                if (Integer.parseInt(valueInput.getText().toString().trim()) < user.getWeight() - 10
                        || Integer.parseInt(valueInput.getText().toString().trim()) > user.getWeight() + 10) {
                    valueInput.setError(getString(R.string.invalid_value_text));
                    return false;
                }
            }
        }
        if (Objects.requireNonNull(nameInput.getText()).toString().trim().length() <= 1) {
            nameInput.setError(getString(R.string.invalid_name_text));
            return false;
        }
        valueInput.setError(null);
        nameInput.setError(null);
        return true;
    }

    private void moveToProfileFragment() {
        BottomNavigationView bottomNavigationView =
                ((MainActivity) requireActivity()).getBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
    }

    private void createAndAddRecord() {
        MeasurementRecord measurementRecord = new MeasurementRecord();
        measurementRecord.setInitial(false);
        measurementRecord.setUserId(user.getUserId());
        measurementRecord.setRecordId(UUID.randomUUID().toString());
        measurementRecord.setDate(DateConverter.fromLongDate(new Date()));
        measurementRecord.setName(Objects.requireNonNull(nameInput.getText()).toString().trim());
        measurementRecord.setValue(Integer.parseInt(Objects.requireNonNull(valueInput.getText()).toString().trim()));
        measurementRecord.setMeasurementCategory(recordType);
        ((MainActivity) requireActivity()).addMeasurementRecord(measurementRecord);
    }
}