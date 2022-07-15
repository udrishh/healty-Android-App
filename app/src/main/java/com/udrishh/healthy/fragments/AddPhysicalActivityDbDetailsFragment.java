package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class AddPhysicalActivityDbDetailsFragment extends Fragment {
    private View view;
    private PhysicalActivity selectedActivity;
    private MaterialButton addBtn;
    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;
    private TextView burnedCaloriesViewer;
    private User user;

    public AddPhysicalActivityDbDetailsFragment() {
    }

    public AddPhysicalActivityDbDetailsFragment(PhysicalActivity physicalActivity) {
        selectedActivity = physicalActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_physical_activity_db_details, container, false);
        importObjects();
        initialiseComponents();
        setTextChangedListener();
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
                                Integer.parseInt(quantityInput.getText().toString().trim()) >= 200 ) {
                    quantityInput.setError(getString(R.string.invalid_quantity_text));
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

    private void setTextChangedListener() {
        quantityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Objects.requireNonNull(quantityInput.getText()).toString().trim().length() > 0 &&
                        Objects.requireNonNull(quantityInput.getText()).toString().trim().length() < 4 ) {
                    burnedCaloriesViewer.setText(getString(R.string.add_physical_activity_total_view,
                            Math.round(Float.parseFloat(quantityInput.getText().toString().trim()) / 60
                                    * selectedActivity.getCalories() * user.getWeight())));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initialiseComponents() {
        addBtn = view.findViewById(R.id.add_activity_finish);
        nameInput = view.findViewById(R.id.activity_name_input);
        quantityInput = view.findViewById(R.id.activity_quantity_input);
        burnedCaloriesViewer = view.findViewById(R.id.add_physical_activity_total_view);
        burnedCaloriesViewer.setText(getString(R.string.add_physical_activity_total_view,Math.round(selectedActivity.getCalories())));
        burnedCaloriesViewer = view.findViewById(R.id.add_physical_activity_total_view);
        nameInput.setText(selectedActivity.getName());
        quantityInput.setText("1");
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
        physicalActivityRecord.setItemId(selectedActivity.getPhysicalActivityId());
        physicalActivityRecord.setName(Objects.requireNonNull(nameInput.getText()).toString().trim());
        physicalActivityRecord.setQuantity(Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()));

        ((MainActivity) requireActivity()).addPhysicalActivityRecord(physicalActivityRecord);
    }
}