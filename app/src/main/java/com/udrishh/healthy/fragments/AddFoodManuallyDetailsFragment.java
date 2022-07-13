package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.Drink;
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
        user = ((MainActivity) this.requireActivity()).getUserObject();

        initialiseComponents();
        return view;
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

        know();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.add_known100_radio:
                        know();
                        break;
                    case R.id.add_unknown100_radio:
                        unknown();
                        break;
                }
            }
        });

//        quantityInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                //UPDATE TOTAL VIEWERS
//                if (quantityInput.getText().toString().trim().length() > 0) {
//                    caloriesTotalViewer.setText(getString(R.string.add_food_db_per100_calories,
//                            selectedFood.getCalories()
//                                    * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
//                    proteinsTotalViewer.setText(getString(R.string.add_food_db_per100_proteins,
//                            selectedFood.getProteins()
//                                    * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
//                    lipidsTotalViewer.setText(getString(R.string.add_food_db_per100_lipids,
//                            selectedFood.getLipids()
//                                    * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
//                    carbsTotalViewer.setText(getString(R.string.add_food_db_per100_carbs,
//                            selectedFood.getCarbs()
//                                    * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
//                    fibersTotalViewer.setText(getString(R.string.add_food_db_per100_fibers,
//                            selectedFood.getFibers()
//                                    * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
//                } else {
//                    caloriesTotalViewer.setText(getString(R.string.add_food_db_per100_calories, 0));
//                    proteinsTotalViewer.setText(getString(R.string.add_food_db_per100_proteins, 0));
//                    lipidsTotalViewer.setText(getString(R.string.add_food_db_per100_lipids, 0));
//                    carbsTotalViewer.setText(getString(R.string.add_food_db_per100_carbs, 0));
//                    fibersTotalViewer.setText(getString(R.string.add_food_db_per100_fibers, 0));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//        caloriesViewer = view.findViewById(R.id.add_food_db_per100_calories);
//        caloriesViewer.setText(getString(R.string.add_food_db_per100_calories, selectedFood.getCalories()));
//        proteinsViewer = view.findViewById(R.id.add_food_db_per100_proteins);
//        proteinsViewer.setText(getString(R.string.add_food_db_per100_proteins, selectedFood.getProteins()));
//        lipidsViewer = view.findViewById(R.id.add_food_db_per100_lipids);
//        lipidsViewer.setText(getString(R.string.add_food_db_per100_lipids, selectedFood.getLipids()));
//        carbsViewer = view.findViewById(R.id.add_food_db_per100_carbs);
//        carbsViewer.setText(getString(R.string.add_food_db_per100_carbs, selectedFood.getCarbs()));
//        fibersViewer = view.findViewById(R.id.add_food_db_per100_fibers);
//        fibersViewer.setText(getString(R.string.add_food_db_per100_fibers, selectedFood.getFibers()));
//
//        caloriesTotalViewer = view.findViewById(R.id.add_food_db_total_calories);
//        proteinsTotalViewer = view.findViewById(R.id.add_food_db_total_proteins);
//        lipidsTotalViewer = view.findViewById(R.id.add_food_db_total_lipids);
//        carbsTotalViewer = view.findViewById(R.id.add_food_db_total_carbs);
//        fibersTotalViewer = view.findViewById(R.id.add_food_db_total_fibers);
//
//        nameInput.setText(selectedFood.getName());
//        quantityInput.setText("100");
//
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean isValid = true;
//                if (quantityInput.getText().toString().trim().length() <= 0 ||
//                        Integer.parseInt(quantityInput.getText().toString().trim()) <= 0) {
//                    quantityInput.setError(getString(R.string.invalid_quantity_text));
//                    isValid = false;
//                }
//                if (nameInput.getText().toString().trim().length() <= 1) {
//                    nameInput.setError(getString(R.string.invalid_name_text));
//                }
//                if (!isValid) {
//                    return;
//                }
//
//                quantityInput.setError(null);
//                nameInput.setError(null);
//                FoodDrinkRecord foodDrinkRecord = new FoodDrinkRecord();
//                foodDrinkRecord.setRecordId(UUID.randomUUID().toString());
//                foodDrinkRecord.setUserId(user.getUserId());
//                foodDrinkRecord.setItemId(selectedFood.getFoodId());
//                foodDrinkRecord.setName(Objects.requireNonNull(nameInput.getText()).toString().trim());
//                foodDrinkRecord.setCalories(selectedFood.getCalories());
//                foodDrinkRecord.setLipids(selectedFood.getLipids());
//                foodDrinkRecord.setCarbs(selectedFood.getCarbs());
//                foodDrinkRecord.setFibers(selectedFood.getFibers());
//                foodDrinkRecord.setProteins(selectedFood.getProteins());
//                foodDrinkRecord.setQuantity(Integer.parseInt(Objects.
//                        requireNonNull(quantityInput.getText()).toString().trim()));
//                foodDrinkRecord.setCategory(RecordType.FOOD);
//                foodDrinkRecord.setDate(DateConverter.fromLongDate(new Date()));
//                foodDrinkRecord.setTotalCalories(foodDrinkRecord.getQuantity() * foodDrinkRecord.getCalories() / 100);
//                foodDrinkRecord.setTotalLipids(foodDrinkRecord.getQuantity() * foodDrinkRecord.getLipids() / 100);
//                foodDrinkRecord.setTotalCarbs(foodDrinkRecord.getQuantity() * foodDrinkRecord.getCarbs() / 100);
//                foodDrinkRecord.setTotalFibers(foodDrinkRecord.getQuantity() * foodDrinkRecord.getFibers() / 100);
//                foodDrinkRecord.setTotalProteins(foodDrinkRecord.getQuantity() * foodDrinkRecord.getProteins() / 100);
//
//                ((MainActivity) requireActivity()).addFoodDrinkRecord(foodDrinkRecord);
//
//                FragmentManager fragmentManager = getParentFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.main_frame_layout, new ProfileFragment())
//                        .commit();
//
//                Toast.makeText(getContext(), R.string.record_added_text, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void know() {
        quantityInput.setVisibility(View.VISIBLE);
        quantityInputLabel.setVisibility(View.VISIBLE);
        per100InputLayout.setVisibility(View.VISIBLE);
        totalInputLayout.setVisibility(View.GONE);
        totalViewLayout.setVisibility(View.VISIBLE);

        //SET INPUTS TO 0
        quantityInput.setText("0");
        calories100Input.setText("0");
        proteins100Input.setText("0");
        lipids100Input.setText("0");
        carbs100Input.setText("0");
        fibers100Input.setText("0");

        //SET VIEWS TO 0


        per100InputTextChangedHandler();
        caloriesTotalView.setText(getString(R.string.add_food_db_per100_calories, 0));
        proteinsTotalView.setText(getString(R.string.add_food_db_per100_proteins, 0));
        lipidsTotalView.setText(getString(R.string.add_food_db_per100_lipids, 0));
        carbsTotalView.setText(getString(R.string.add_food_db_per100_carbs, 0));
        fibersTotalView.setText(getString(R.string.add_food_db_per100_fibers, 0));
        calculateTotal();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (per100InputsValid()) {
                    //ADD
                    Food food = new Food();
                    food.setUserId(user.getUserId());
                    food.setFoodId(UUID.randomUUID().toString());
                    food.setName(nameInput.getText().toString().trim());
                    food.setCalories(Integer.parseInt(calories100Input.getText().toString().trim()));
                    food.setProteins(Integer.parseInt(proteins100Input.getText().toString().trim()));
                    food.setLipids(Integer.parseInt(lipids100Input.getText().toString().trim()));
                    food.setCarbs(Integer.parseInt(carbs100Input.getText().toString().trim()));
                    food.setFibers(Integer.parseInt(fibers100Input.getText().toString().trim()));

                    FoodDrinkRecord foodDrinkRecord = new FoodDrinkRecord();
                    foodDrinkRecord.setName(nameInput.getText().toString().trim());
                    foodDrinkRecord.setRecordId(UUID.randomUUID().toString());
                    foodDrinkRecord.setUserId(user.getUserId());
                    foodDrinkRecord.setItemId(food.getFoodId());
                    foodDrinkRecord.setDate(DateConverter.fromLongDate(new Date()));
                    foodDrinkRecord.setRecordType(RecordType.FOOD);

                    ((MainActivity) requireActivity()).addFoodDrinkRecord(foodDrinkRecord);
                    ((MainActivity) requireActivity()).addFood(food);

                    BottomNavigationView bottomNavigationView =
                            ((MainActivity) requireActivity()).getBottomNavigation();
                    bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
                }
            }
        });
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
                    Integer.parseInt(quantityInput.getText().toString().trim()) *
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
        if (nameInput.getText().toString().trim().length() <= 1) {
            nameInput.setError(getString(R.string.invalid_name_text));
            valid = false;
        }
        if (TextUtils.isEmpty(quantityInput.getText().toString().trim())) {
            quantityInput.setError("Cantitate invalidă!");
            valid = false;
        }
        if (!TextUtils.isEmpty(quantityInput.getText().toString().trim())
                && Integer.parseInt(quantityInput.getText().toString().trim()) < 1) {
            quantityInput.setError("Cantitate invalidă!");
            valid = false;
        }

        if (TextUtils.isEmpty(proteins100Input.getText().toString().trim())) {
            proteins100Input.setError("Valoare invalidă!");
            valid = false;
        }
//        if (!TextUtils.isEmpty(proteins100Input.getText().toString().trim())
//                && Integer.parseInt(proteins100Input.getText().toString().trim()) <= 0) {
//            proteins100Input.setError("Valoare invalidă!");
//            valid = false;
//        }

        if (TextUtils.isEmpty(calories100Input.getText().toString().trim())) {
            calories100Input.setError("Valoare invalidă!");
            valid = false;
        }
        if (!TextUtils.isEmpty(calories100Input.getText().toString().trim())
                && Integer.parseInt(calories100Input.getText().toString().trim()) < 1) {
            calories100Input.setError("Valoare invalidă!");
            valid = false;
        }

        if (TextUtils.isEmpty(carbs100Input.getText().toString().trim())) {
            carbs100Input.setError("Valoare invalidă!");
            valid = false;
        }
//        if (!TextUtils.isEmpty(carbs100Input.getText().toString().trim())
//                && Integer.parseInt(carbs100Input.getText().toString().trim()) < 1) {
//            carbs100Input.setError("Valoare invalidă!");
//            valid = false;
//        }

        if (TextUtils.isEmpty(fibers100Input.getText().toString().trim())) {
            fibers100Input.setError("Valoare invalidă!");
            valid = false;
        }
//        if (!TextUtils.isEmpty(fibers100Input.getText().toString().trim())
//                && Integer.parseInt(fibers100Input.getText().toString().trim()) < 1) {
//            fibers100Input.setError("Valoare invalidă!");
//            valid = false;
//        }

        if (TextUtils.isEmpty(lipids100Input.getText().toString().trim())) {
            lipids100Input.setError("Valoare invalidă!");
            valid = false;
        }
//        if (!TextUtils.isEmpty(lipids100Input.getText().toString().trim())
//                && Integer.parseInt(lipids100Input.getText().toString().trim()) < 1) {
//            lipids100Input.setError("Valoare invalidă!");
//            valid = false;
//        }
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
        if (nameInput.getText().toString().trim().length() <= 1) {
            nameInput.setError(getString(R.string.invalid_name_text));
            valid = false;
        }
        if (TextUtils.isEmpty(proteinsTotalInput.getText().toString().trim())) {
            proteinsTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
//        if (!TextUtils.isEmpty(proteins100Input.getText().toString().trim())
//                && Integer.parseInt(proteins100Input.getText().toString().trim()) <= 0) {
//            proteins100Input.setError("Valoare invalidă!");
//            valid = false;
//        }

        if (TextUtils.isEmpty(caloriesTotalInput.getText().toString().trim())) {
            caloriesTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
        if (!TextUtils.isEmpty(caloriesTotalInput.getText().toString().trim())
                && Integer.parseInt(caloriesTotalInput.getText().toString().trim()) < 1) {
            caloriesTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }

        if (TextUtils.isEmpty(carbsTotalInput.getText().toString().trim())) {
            carbsTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
//        if (!TextUtils.isEmpty(carbs100Input.getText().toString().trim())
//                && Integer.parseInt(carbs100Input.getText().toString().trim()) < 1) {
//            carbs100Input.setError("Valoare invalidă!");
//            valid = false;
//        }

        if (TextUtils.isEmpty(fibersTotalInput.getText().toString().trim())) {
            fibersTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
//        if (!TextUtils.isEmpty(fibers100Input.getText().toString().trim())
//                && Integer.parseInt(fibers100Input.getText().toString().trim()) < 1) {
//            fibers100Input.setError("Valoare invalidă!");
//            valid = false;
//        }

        if (TextUtils.isEmpty(lipidsTotalInput.getText().toString().trim())) {
            lipidsTotalInput.setError(getString(R.string.invalid_value_text));
            valid = false;
        }
//        if (!TextUtils.isEmpty(lipids100Input.getText().toString().trim())
//                && Integer.parseInt(lipids100Input.getText().toString().trim()) < 1) {
//            lipids100Input.setError("Valoare invalidă!");
//            valid = false;
//        }
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
        quantityInput.setVisibility(View.GONE);
        quantityInputLabel.setVisibility(View.GONE);
        per100InputLayout.setVisibility(View.GONE);
        totalInputLayout.setVisibility(View.VISIBLE);
        totalViewLayout.setVisibility(View.GONE);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalInputsValid()) {
                    //ADD
                    Food food = new Food();
                    food.setUserId(user.getUserId());
                    food.setFoodId("x" + UUID.randomUUID().toString());
                    food.setName(nameInput.getText().toString().trim());
                    food.setCalories(Integer.parseInt(calories100Input.getText().toString().trim()));
                    food.setProteins(Integer.parseInt(proteins100Input.getText().toString().trim()));
                    food.setLipids(Integer.parseInt(lipids100Input.getText().toString().trim()));
                    food.setCarbs(Integer.parseInt(carbs100Input.getText().toString().trim()));
                    food.setFibers(Integer.parseInt(fibers100Input.getText().toString().trim()));

                    FoodDrinkRecord foodDrinkRecord = new FoodDrinkRecord();
                    foodDrinkRecord.setName(nameInput.getText().toString().trim());
                    foodDrinkRecord.setRecordId(UUID.randomUUID().toString());
                    foodDrinkRecord.setUserId(user.getUserId());
                    foodDrinkRecord.setItemId(food.getFoodId());
                    foodDrinkRecord.setDate(DateConverter.fromLongDate(new Date()));
                    foodDrinkRecord.setRecordType(RecordType.FOOD);

                    ((MainActivity) requireActivity()).addFoodDrinkRecord(foodDrinkRecord);
                    ((MainActivity) requireActivity()).addFood(food);

                    BottomNavigationView bottomNavigationView =
                            ((MainActivity) requireActivity()).getBottomNavigation();
                    bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
                }
            }
        });

        //SET INPUTS TO 0
        quantityInput.setText("0");
        caloriesTotalInput.setText("0");
        proteinsTotalInput.setText("0");
        lipidsTotalInput.setText("0");
        carbsTotalInput.setText("0");
        fibersTotalInput.setText("0");

        //SET VIEWS TO 0
        totalInputsValid();

        totalInputTextChangedHandler();
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