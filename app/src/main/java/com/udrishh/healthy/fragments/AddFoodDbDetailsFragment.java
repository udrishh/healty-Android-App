package com.udrishh.healthy.fragments;

import android.annotation.SuppressLint;
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
import com.udrishh.healthy.classes.Food;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.utilities.DateConverter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class AddFoodDbDetailsFragment extends Fragment {
    private View view;
    private Food selectedFood;
    private MaterialButton addBtn;
    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;

    private TextView caloriesTotalViewer;
    private TextView proteinsTotalViewer;
    private TextView lipidsTotalViewer;
    private TextView fibersTotalViewer;
    private TextView carbsTotalViewer;

    private User user;

    public AddFoodDbDetailsFragment() {
    }

    public AddFoodDbDetailsFragment(Food food) {
        selectedFood = food;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food_db_details, container, false);
        importObjects();
        initialiseComponents();
        setTotalViewers();
        setTextChangedListeners();
        setClickListeners();
        return view;
    }

    private void setClickListeners() {
        addBtn.setOnClickListener(v -> {
            if (validInputs()) {
                createAndAddRecord();
                moveToProfileFragment();
            }
        });
    }

    private boolean validInputs() {
        if (Objects.requireNonNull(quantityInput.getText()).toString().trim().length() <= 0) {
            quantityInput.setError(getString(R.string.invalid_quantity_text));
            return false;
        }
        if (quantityInput.getText().toString().trim().length() > 0) {
            if (Objects.requireNonNull(quantityInput.getText()).toString().trim().length() > 4 ||
                    Integer.parseInt(quantityInput.getText().toString().trim()) < 10 ||
                    Integer.parseInt(quantityInput.getText().toString().trim()) > 2500) {
                quantityInput.setError(getString(R.string.invalid_quantity_text));
                return false;
            }
        }
        if (Objects.requireNonNull(nameInput.getText()).toString().trim().length() <= 1) {
            nameInput.setError(getString(R.string.invalid_name_text));
            return false;
        }
        quantityInput.setError(null);
        nameInput.setError(null);
        return true;
    }

    private void moveToProfileFragment() {
        BottomNavigationView bottomNavigationView =
                ((MainActivity) requireActivity()).getBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
    }

    private void createAndAddRecord() {
        FoodDrinkRecord foodDrinkRecord = new FoodDrinkRecord();
        foodDrinkRecord.setRecordId(UUID.randomUUID().toString());
        foodDrinkRecord.setUserId(user.getUserId());
        foodDrinkRecord.setItemId(selectedFood.getFoodId());
        foodDrinkRecord.setName(Objects.requireNonNull(nameInput.getText()).toString().trim());
        foodDrinkRecord.setQuantity(Integer.parseInt(Objects.
                requireNonNull(quantityInput.getText()).toString().trim()));
        foodDrinkRecord.setRecordType(RecordType.FOOD);
        foodDrinkRecord.setDate(DateConverter.fromLongDate(new Date()));

        ((MainActivity) requireActivity()).addFoodDrinkRecord(foodDrinkRecord);
    }

    private void setTextChangedListeners() {
        quantityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Objects.requireNonNull(quantityInput.getText()).toString().trim().length() > 0 &&
                        Objects.requireNonNull(quantityInput.getText()).toString().trim().length() <= 4 &&
                        Integer.parseInt(quantityInput.getText().toString().trim()) >= 0 &&
                        Integer.parseInt(quantityInput.getText().toString().trim()) < 2500) {
                    setTotalViewers();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setTotalViewers() {
        caloriesTotalViewer.setText(getString(R.string.add_food_db_per100_calories,
                selectedFood.getCalories()
                        * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
        proteinsTotalViewer.setText(getString(R.string.add_food_db_per100_proteins,
                selectedFood.getProteins()
                        * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
        lipidsTotalViewer.setText(getString(R.string.add_food_db_per100_lipids,
                selectedFood.getLipids()
                        * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
        carbsTotalViewer.setText(getString(R.string.add_food_db_per100_carbs,
                selectedFood.getCarbs()
                        * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
        fibersTotalViewer.setText(getString(R.string.add_food_db_per100_fibers,
                selectedFood.getFibers()
                        * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
    }

    private void importObjects() {
        user = ((MainActivity) this.requireActivity()).getUserObject();
    }

    @SuppressLint("SetTextI18n")
    private void initialiseComponents() {
        addBtn = view.findViewById(R.id.add_food_finish);
        nameInput = view.findViewById(R.id.food_name_input);
        quantityInput = view.findViewById(R.id.food_quantity_input);
        TextView caloriesViewer = view.findViewById(R.id.add_food_db_per100_calories);
        caloriesViewer.setText(getString(R.string.add_food_db_per100_calories, selectedFood.getCalories()));
        TextView proteinsViewer = view.findViewById(R.id.add_food_db_per100_proteins);
        proteinsViewer.setText(getString(R.string.add_food_db_per100_proteins, selectedFood.getProteins()));
        TextView lipidsViewer = view.findViewById(R.id.add_food_db_per100_lipids);
        lipidsViewer.setText(getString(R.string.add_food_db_per100_lipids, selectedFood.getLipids()));
        TextView carbsViewer = view.findViewById(R.id.add_food_db_per100_carbs);
        carbsViewer.setText(getString(R.string.add_food_db_per100_carbs, selectedFood.getCarbs()));
        TextView fibersViewer = view.findViewById(R.id.add_food_db_per100_fibers);
        fibersViewer.setText(getString(R.string.add_food_db_per100_fibers, selectedFood.getFibers()));
        caloriesTotalViewer = view.findViewById(R.id.add_food_db_total_calories);
        proteinsTotalViewer = view.findViewById(R.id.add_food_db_total_proteins);
        lipidsTotalViewer = view.findViewById(R.id.add_food_db_total_lipids);
        carbsTotalViewer = view.findViewById(R.id.add_food_db_total_carbs);
        fibersTotalViewer = view.findViewById(R.id.add_food_db_total_fibers);
        nameInput.setText(selectedFood.getName());
        quantityInput.setText("100");
    }
}