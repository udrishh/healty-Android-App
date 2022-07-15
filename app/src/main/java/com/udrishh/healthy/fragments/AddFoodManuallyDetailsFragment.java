package com.udrishh.healthy.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.Food;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.utilities.DateConverter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class AddFoodManuallyDetailsFragment extends Fragment {
    private View view;
    private MaterialButton addBtn;
    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;
    private TextInputLayout quantityInputLabel;
    private RadioGroup radioGroup;
    private TextInputEditText calories100Input;
    private TextInputEditText proteins100Input;
    private TextInputEditText lipids100Input;
    private TextInputEditText fibers100Input;
    private TextInputEditText carbs100Input;
    private TextInputEditText caloriesTotalInput;
    private TextInputEditText proteinsTotalInput;
    private TextInputEditText lipidsTotalInput;
    private TextInputEditText fibersTotalInput;
    private TextInputEditText carbsTotalInput;
    private TextView caloriesTotalView;
    private TextView proteinsTotalView;
    private TextView lipidsTotalView;
    private TextView fibersTotalView;
    private TextView carbsTotalView;

    private LinearLayout per100InputLayout;
    private LinearLayout totalInputLayout;
    private LinearLayout totalViewLayout;

    private User user;

    public AddFoodManuallyDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food_manually_details, container, false);
        importObjects();
        initialiseComponents();
        setCheckChangedListener();
        return view;
    }

    private void importObjects() {
        user = ((MainActivity) this.requireActivity()).getUserObject();
    }

    @SuppressLint("NonConstantResourceId")
    private void setCheckChangedListener() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.add_known100_radio:
                    known();
                    break;
                case R.id.add_unknown100_radio:
                    unknown();
                    break;
            }
        });
    }

    private void initialiseComponents() {
        addBtn = view.findViewById(R.id.add_food_m_finish);
        nameInput = view.findViewById(R.id.food_m_name_input);
        quantityInput = view.findViewById(R.id.food_m_quantity_input);
        quantityInputLabel = view.findViewById(R.id.food_m_quantity_label);
        radioGroup = view.findViewById(R.id.add_food_m_radio_group);
        calories100Input = view.findViewById(R.id.food_m_per100_calories_input);
        proteins100Input = view.findViewById(R.id.food_m_per100_proteins_input);
        carbs100Input = view.findViewById(R.id.food_m_per100_carbs_input);
        lipids100Input = view.findViewById(R.id.food_m_per100_lipids_input);
        fibers100Input = view.findViewById(R.id.food_m_per100_fibers_input);
        caloriesTotalInput = view.findViewById(R.id.food_m_total_calories_input);
        proteinsTotalInput = view.findViewById(R.id.food_m_total_proteins_input);
        carbsTotalInput = view.findViewById(R.id.food_m_total_carbs_input);
        lipidsTotalInput = view.findViewById(R.id.food_m_total_lipids_input);
        fibersTotalInput = view.findViewById(R.id.food_m_total_fibers_input);
        proteinsTotalView = view.findViewById(R.id.add_food_m_total_proteins_view);
        caloriesTotalView = view.findViewById(R.id.add_food_m_total_calories_view);
        carbsTotalView = view.findViewById(R.id.add_food_m_total_carbs_view);
        lipidsTotalView = view.findViewById(R.id.add_food_m_total_lipids_view);
        fibersTotalView = view.findViewById(R.id.add_food_m_total_fibers_view);
        per100InputLayout = view.findViewById(R.id.add_food_m_per100_layout);
        totalInputLayout = view.findViewById(R.id.add_food_m_total_input_layout);
        totalViewLayout = view.findViewById(R.id.add_food_m_total_view_layout);
        known();
    }

    private void known() {
        quantityInput.setVisibility(View.VISIBLE);
        quantityInputLabel.setVisibility(View.VISIBLE);
        per100InputLayout.setVisibility(View.VISIBLE);
        totalInputLayout.setVisibility(View.GONE);
        totalViewLayout.setVisibility(View.VISIBLE);

        setInputsZero();
        per100InputTextChangedHandler();
        setViewersZero();
        calculateTotal();

        addBtn.setOnClickListener(v -> {
            if (per100InputsValid()) {
                createAndAddRecordKnown();
                moveToProfileFragment();
            }
        });
    }

    private void setViewersZero() {
        caloriesTotalView.setText(getString(R.string.add_food_db_per100_calories, 0));
        proteinsTotalView.setText(getString(R.string.add_food_db_per100_proteins, 0));
        lipidsTotalView.setText(getString(R.string.add_food_db_per100_lipids, 0));
        carbsTotalView.setText(getString(R.string.add_food_db_per100_carbs, 0));
        fibersTotalView.setText(getString(R.string.add_food_db_per100_fibers, 0));
    }

    private void createAndAddRecordKnown() {
        Food food = new Food();
        food.setUserId(user.getUserId());
        food.setFoodId(UUID.randomUUID().toString());
        food.setName(Objects.requireNonNull(nameInput.getText()).toString().trim());
        food.setCalories(Integer.parseInt(Objects.requireNonNull(calories100Input.getText()).toString().trim()));
        food.setProteins(Integer.parseInt(Objects.requireNonNull(proteins100Input.getText()).toString().trim()));
        food.setLipids(Integer.parseInt(Objects.requireNonNull(lipids100Input.getText()).toString().trim()));
        food.setCarbs(Integer.parseInt(Objects.requireNonNull(carbs100Input.getText()).toString().trim()));
        food.setFibers(Integer.parseInt(Objects.requireNonNull(fibers100Input.getText()).toString().trim()));

        FoodDrinkRecord foodDrinkRecord = new FoodDrinkRecord();
        foodDrinkRecord.setName(nameInput.getText().toString().trim());
        foodDrinkRecord.setRecordId(UUID.randomUUID().toString());
        foodDrinkRecord.setUserId(user.getUserId());
        foodDrinkRecord.setItemId(food.getFoodId());
        foodDrinkRecord.setDate(DateConverter.fromLongDate(new Date()));
        foodDrinkRecord.setRecordType(RecordType.FOOD);
        foodDrinkRecord.setQuantity(Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()));

        ((MainActivity) requireActivity()).addFoodDrinkRecord(foodDrinkRecord);
        ((MainActivity) requireActivity()).addFood(food);
    }

    private void per100InputTextChangedHandler() {
        quantityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotal();
            }
        });
        calories100Input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotal();
            }
        });
        proteins100Input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotal();
            }
        });
        fibers100Input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotal();

            }
        });
        lipids100Input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotal();
            }
        });
        carbs100Input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotal();
            }
        });
    }

    private void calculateTotal() {
        if (per100InputsValid()) {
            caloriesTotalView.setText(getString(R.string.add_food_db_total_calories,
                    Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) *
                            Integer.parseInt(Objects.requireNonNull(calories100Input.getText()).toString().trim()) / 100));
            proteinsTotalView.setText(getString(R.string.add_food_db_total_proteins,
                    Integer.parseInt(quantityInput.getText().toString().trim()) *
                            Integer.parseInt(Objects.requireNonNull(proteins100Input.getText()).toString().trim()) / 100));
            lipidsTotalView.setText(getString(R.string.add_food_db_total_lipids,
                    Integer.parseInt(quantityInput.getText().toString().trim()) *
                            Integer.parseInt(Objects.requireNonNull(lipids100Input.getText()).toString().trim()) / 100));
            carbsTotalView.setText(getString(R.string.add_food_db_total_carbs,
                    Integer.parseInt(quantityInput.getText().toString().trim()) *
                            Integer.parseInt(Objects.requireNonNull(carbs100Input.getText()).toString().trim()) / 100));
            fibersTotalView.setText(getString(R.string.add_food_db_total_fibers,
                    Integer.parseInt(quantityInput.getText().toString().trim()) *
                            Integer.parseInt(Objects.requireNonNull(fibers100Input.getText()).toString().trim()) / 100));
        }
    }

    private boolean per100InputsValid() {
        boolean valid = true;
        if (Objects.requireNonNull(nameInput.getText()).toString().trim().length() <= 1) {
            nameInput.setError(getString(R.string.invalid_name_text));
            valid = false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(quantityInput.getText()).toString().trim())) {
            quantityInput.setError(getString(R.string.invalid_quantity_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(quantityInput.getText().toString().trim())
                && (Integer.parseInt(quantityInput.getText().toString().trim()) < 10
                || Integer.parseInt(quantityInput.getText().toString().trim()) > 2000)) {
            quantityInput.setError(getString(R.string.invalid_quantity_text));
            valid = false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(proteins100Input.getText()).toString().trim())) {
            proteins100Input.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(proteins100Input.getText().toString().trim())
                && Integer.parseInt(proteins100Input.getText().toString().trim()) > 200) {
            proteins100Input.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(calories100Input.getText()).toString().trim())) {
            calories100Input.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(calories100Input.getText().toString().trim())
                && (Integer.parseInt(calories100Input.getText().toString().trim()) < 1
                || Integer.parseInt(calories100Input.getText().toString().trim()) > 2000)) {
            calories100Input.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(carbs100Input.getText()).toString().trim())) {
            carbs100Input.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(carbs100Input.getText().toString().trim())
                && Integer.parseInt(carbs100Input.getText().toString().trim()) > 200) {
            carbs100Input.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(fibers100Input.getText()).toString().trim())) {
            fibers100Input.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(fibers100Input.getText().toString().trim())
                && Integer.parseInt(fibers100Input.getText().toString().trim()) > 200) {
            fibers100Input.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(lipids100Input.getText()).toString().trim())) {
            lipids100Input.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(lipids100Input.getText().toString().trim())
                && Integer.parseInt(lipids100Input.getText().toString().trim()) > 200) {
            lipids100Input.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!valid) {
            return false;
        }
        quantityInput.setError(null);
        calories100Input.setError(null);
        proteins100Input.setError(null);
        carbs100Input.setError(null);
        lipids100Input.setError(null);
        fibers100Input.setError(null);
        return true;
    }

    private boolean totalInputsValid() {
        boolean valid = true;
        if (Objects.requireNonNull(nameInput.getText()).toString().trim().length() <= 1) {
            nameInput.setError(getString(R.string.invalid_name_text));
            valid = false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(quantityInput.getText()).toString().trim())) {
            quantityInput.setError(getString(R.string.invalid_quantity_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(quantityInput.getText().toString().trim())
                && (Integer.parseInt(quantityInput.getText().toString().trim()) < 10
                || Integer.parseInt(quantityInput.getText().toString().trim()) > 2000)) {
            quantityInput.setError(getString(R.string.invalid_quantity_text));
            valid = false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(proteinsTotalInput.getText()).toString().trim())) {
            proteinsTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(proteinsTotalInput.getText().toString().trim())
                && Integer.parseInt(proteinsTotalInput.getText().toString().trim()) > 200) {
            proteinsTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(caloriesTotalInput.getText()).toString().trim())) {
            caloriesTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(caloriesTotalInput.getText().toString().trim())
                && (Integer.parseInt(caloriesTotalInput.getText().toString().trim()) < 1
                || Integer.parseInt(caloriesTotalInput.getText().toString().trim()) > 2000)) {
            caloriesTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(carbsTotalInput.getText()).toString().trim())) {
            carbsTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(carbsTotalInput.getText().toString().trim())
                && Integer.parseInt(carbsTotalInput.getText().toString().trim()) > 200) {
            carbsTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(fibersTotalInput.getText()).toString().trim())) {
            fibersTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(fibersTotalInput.getText().toString().trim())
                && Integer.parseInt(fibersTotalInput.getText().toString().trim()) > 200) {
            fibersTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(lipidsTotalInput.getText()).toString().trim())) {
            lipidsTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(lipidsTotalInput.getText().toString().trim())
                && Integer.parseInt(lipidsTotalInput.getText().toString().trim()) > 200) {
            lipidsTotalView.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!valid) {
            return false;
        }
        quantityInput.setError(null);
        calories100Input.setError(null);
        proteins100Input.setError(null);
        carbs100Input.setError(null);
        lipids100Input.setError(null);
        fibers100Input.setError(null);
        return true;
    }

    private void unknown() {
        per100InputLayout.setVisibility(View.GONE);
        totalInputLayout.setVisibility(View.VISIBLE);
        totalViewLayout.setVisibility(View.GONE);
        addBtn.setOnClickListener(v -> {
            if (totalInputsValid()) {
                createAndAddRecordUnknown();
                moveToProfileFragment();
            }
        });
        setTotalInputsZero();
        totalInputsValid();
        totalInputTextChangedHandler();
    }

    private void moveToProfileFragment() {
        BottomNavigationView bottomNavigationView =
                ((MainActivity) requireActivity()).getBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
    }

    private void createAndAddRecordUnknown() {
        Food food = new Food();
        food.setUserId(user.getUserId());
        food.setFoodId("x" + UUID.randomUUID().toString());
        food.setName(Objects.requireNonNull(nameInput.getText()).toString().trim());
        food.setCalories(Integer.parseInt(Objects.requireNonNull(caloriesTotalInput.getText()).toString().trim()));
        food.setProteins(Integer.parseInt(Objects.requireNonNull(proteinsTotalInput.getText()).toString().trim()));
        food.setLipids(Integer.parseInt(Objects.requireNonNull(lipidsTotalInput.getText()).toString().trim()));
        food.setCarbs(Integer.parseInt(Objects.requireNonNull(carbsTotalInput.getText()).toString().trim()));
        food.setFibers(Integer.parseInt(Objects.requireNonNull(fibersTotalInput.getText()).toString().trim()));

        FoodDrinkRecord foodDrinkRecord = new FoodDrinkRecord();
        foodDrinkRecord.setName(nameInput.getText().toString().trim());
        foodDrinkRecord.setRecordId(UUID.randomUUID().toString());
        foodDrinkRecord.setUserId(user.getUserId());
        foodDrinkRecord.setItemId(food.getFoodId());
        foodDrinkRecord.setDate(DateConverter.fromLongDate(new Date()));
        foodDrinkRecord.setRecordType(RecordType.FOOD);
        foodDrinkRecord.setQuantity(Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()));

        ((MainActivity) requireActivity()).addFoodDrinkRecord(foodDrinkRecord);
        ((MainActivity) requireActivity()).addFood(food);
    }

    private void setTotalInputsZero() {
        quantityInput.setText("0");
        caloriesTotalInput.setText("0");
        proteinsTotalInput.setText("0");
        lipidsTotalInput.setText("0");
        carbsTotalInput.setText("0");
        fibersTotalInput.setText("0");
    }

    private void setInputsZero() {
        quantityInput.setText("0");
        calories100Input.setText("0");
        proteins100Input.setText("0");
        lipids100Input.setText("0");
        carbs100Input.setText("0");
        fibers100Input.setText("0");
    }

    private void totalInputTextChangedHandler() {
        quantityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotal();
            }
        });
        caloriesTotalInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                totalInputsValid();

            }
        });
        proteinsTotalInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                totalInputsValid();

            }
        });
        fibersTotalInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                totalInputsValid();

            }
        });
        lipidsTotalInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                totalInputsValid();

            }
        });
        carbsTotalInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                totalInputsValid();

            }
        });
    }
}