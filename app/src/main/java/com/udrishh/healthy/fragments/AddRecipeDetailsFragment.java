package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.Recipe;
import com.udrishh.healthy.classes.RecipeRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.utilities.DateConverter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class AddRecipeDetailsFragment extends Fragment {
    private View view;
    private User user;
    private Recipe selectedRecipe;
    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;
    private CheckBox checkBox;
    private TextView totalCaloriesView;
    private MaterialButton addBtn;

    public AddRecipeDetailsFragment() {
    }

    public AddRecipeDetailsFragment(Recipe recipe) {
        selectedRecipe = recipe;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_recipe_details, container, false);
        importObjects();
        initialiseComponents();
        setCheckChangedListener();
        setTextChangedListener();
        setClickListener();
        return view;
    }

    private void setClickListener() {
        addBtn.setOnClickListener(v -> {
            boolean isValid = true;
            if (!checkBox.isChecked()) {
                if (Objects.requireNonNull(quantityInput.getText()).toString().trim().length() <= 0 ||
                Objects.requireNonNull(quantityInput.getText()).toString().trim().length() >= 5) {
                    quantityInput.setError(getString(R.string.invalid_quantity_text));
                    isValid = false;
                }
                if (quantityInput.getText().toString().trim().length() > 0 &&
                        quantityInput.getText().toString().trim().length() < 5) {
                    if (Integer.parseInt(quantityInput.getText().toString().trim()) <= 10
                            || Integer.parseInt(quantityInput.getText().toString().trim()) >= 2500) {
                        quantityInput.setError(getString(R.string.invalid_quantity_text));
                        isValid = false;
                    }
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
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Objects.requireNonNull(quantityInput.getText()).toString().trim().length() > 0 &&
                        Objects.requireNonNull(quantityInput.getText()).toString().trim().length() < 5) {
                    if (Integer.parseInt(quantityInput.getText().toString().trim()) > 0 &&
                            Integer.parseInt(quantityInput.getText().toString().trim()) < 2500) {
                        calculateTotal();
                    }
                }
            }
        });
    }

    private void setCheckChangedListener() {
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                quantityInput.setEnabled(false);
                totalCaloriesView.setText(getString(R.string.add_recipe_total_calories,
                        selectedRecipe.getCalories()));
                quantityInput.setText(String.valueOf(selectedRecipe.getQuantity()));
            } else {
                if (Objects.requireNonNull(quantityInput.getText()).toString().trim().length() > 0 &&
                        Objects.requireNonNull(quantityInput.getText()).toString().trim().length() < 5) {
                    if (Integer.parseInt(quantityInput.getText().toString().trim()) > 0 &&
                            Integer.parseInt(quantityInput.getText().toString().trim()) < 2500) {
                        quantityInput.setEnabled(true);
                        quantityInput.setText("0");
                        calculateTotal();
                    }
                }
            }
        });
    }

    private void importObjects() {
        user = ((MainActivity) this.requireActivity()).getUserObject();
    }

    private void initialiseComponents() {
        nameInput = view.findViewById(R.id.add_recipe_name_input);
        quantityInput = view.findViewById(R.id.add_recipe_quantity_input);
        checkBox = view.findViewById(R.id.add_recipe_quantity_all_check);
        totalCaloriesView = view.findViewById(R.id.add_recipe_total_calories);
        addBtn = view.findViewById(R.id.add_recipe_finish);
        nameInput.setText(selectedRecipe.getName());
        quantityInput.setText("0");
        checkBox.setText(getString(R.string.add_recipe_quantity_all, selectedRecipe.getQuantity()));
        totalCaloriesView.setText(getString(R.string.add_recipe_total_calories, 0));
    }

    private void moveToProfileFragment() {
        BottomNavigationView bottomNavigationView =
                ((MainActivity) requireActivity()).getBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
    }

    private void createAndAddRecord() {
        RecipeRecord recipeRecord = new RecipeRecord();
        recipeRecord.setRecordId(UUID.randomUUID().toString());
        recipeRecord.setDate(DateConverter.fromLongDate(new Date()));
        recipeRecord.setUserId(user.getUserId());
        recipeRecord.setItemId(selectedRecipe.getRecipeId());
        recipeRecord.setName(Objects.requireNonNull(nameInput.getText()).toString().trim());
        recipeRecord.setQuantity(Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()));

        ((MainActivity) requireActivity()).addRecipeRecord(recipeRecord);
    }

    private void calculateTotal() {
        if (Objects.requireNonNull(quantityInput.getText()).toString().trim().length() > 0) {
            totalCaloriesView.setText(getString(R.string.add_recipe_total_calories,
                    (selectedRecipe.getCalories() * 100 / selectedRecipe.getQuantity()) *
                            Integer.parseInt(quantityInput.getText().toString().trim()) / 100));
        } else {
            totalCaloriesView.setText(getString(R.string.add_recipe_total_calories, 0));
        }
    }
}