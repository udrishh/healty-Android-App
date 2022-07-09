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
import com.udrishh.healthy.classes.Drink;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.utilities.DateConverter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class AddDrinkDbDetailsFragment extends Fragment {
    private View view;
    private Drink selectedDrink;
    private MaterialButton addBtn;
    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;

    private TextView caloriesViewer;
    private TextView proteinsViewer;
    private TextView lipidsViewer;
    private TextView fibersViewer;
    private TextView carbsViewer;
    private TextView caloriesTotalViewer;
    private TextView proteinsTotalViewer;
    private TextView lipidsTotalViewer;
    private TextView fibersTotalViewer;
    private TextView carbsTotalViewer;

    private User user;

    public AddDrinkDbDetailsFragment() {
    }

    public AddDrinkDbDetailsFragment(Drink drink) {
        selectedDrink = drink;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_drink_db_details, container, false);
        user = ((MainActivity) this.requireActivity()).getUserObject();

        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        addBtn = view.findViewById(R.id.add_drink_finish);
        nameInput = view.findViewById(R.id.drink_name_input);
        quantityInput = view.findViewById(R.id.drink_quantity_input);
        quantityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //UPDATE TOTAL VIEWERS
                if (quantityInput.getText().toString().trim().length() > 0) {
                    caloriesTotalViewer.setText(getString(R.string.add_food_db_per100_calories,
                            selectedDrink.getCalories()
                                    * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
                    proteinsTotalViewer.setText(getString(R.string.add_food_db_per100_proteins,
                            selectedDrink.getProteins()
                                    * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
                    lipidsTotalViewer.setText(getString(R.string.add_food_db_per100_lipids,
                            selectedDrink.getLipids()
                                    * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
                    carbsTotalViewer.setText(getString(R.string.add_food_db_per100_carbs,
                            selectedDrink.getCarbs()
                                    * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
                    fibersTotalViewer.setText(getString(R.string.add_food_db_per100_fibers,
                            selectedDrink.getFibers()
                                    * Integer.parseInt(Objects.requireNonNull(quantityInput.getText()).toString().trim()) / 100));
                } else {
                    caloriesTotalViewer.setText(getString(R.string.add_food_db_per100_calories, 0));
                    proteinsTotalViewer.setText(getString(R.string.add_food_db_per100_proteins, 0));
                    lipidsTotalViewer.setText(getString(R.string.add_food_db_per100_lipids, 0));
                    carbsTotalViewer.setText(getString(R.string.add_food_db_per100_carbs, 0));
                    fibersTotalViewer.setText(getString(R.string.add_food_db_per100_fibers, 0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        caloriesViewer = view.findViewById(R.id.add_drink_db_per100_calories);
        caloriesViewer.setText(getString(R.string.add_food_db_per100_calories, selectedDrink.getCalories()));
        proteinsViewer = view.findViewById(R.id.add_drink_db_per100_proteins);
        proteinsViewer.setText(getString(R.string.add_food_db_per100_proteins, selectedDrink.getProteins()));
        lipidsViewer = view.findViewById(R.id.add_drink_db_per100_lipids);
        lipidsViewer.setText(getString(R.string.add_food_db_per100_lipids, selectedDrink.getLipids()));
        carbsViewer = view.findViewById(R.id.add_drink_db_per100_carbs);
        carbsViewer.setText(getString(R.string.add_food_db_per100_carbs, selectedDrink.getCarbs()));
        fibersViewer = view.findViewById(R.id.add_drink_db_per100_fibers);
        fibersViewer.setText(getString(R.string.add_food_db_per100_fibers, selectedDrink.getFibers()));

        caloriesTotalViewer = view.findViewById(R.id.add_drink_db_total_calories);
        proteinsTotalViewer = view.findViewById(R.id.add_drink_db_total_proteins);
        lipidsTotalViewer = view.findViewById(R.id.add_drink_db_total_lipids);
        carbsTotalViewer = view.findViewById(R.id.add_drink_db_total_carbs);
        fibersTotalViewer = view.findViewById(R.id.add_drink_db_total_fibers);

        nameInput.setText(selectedDrink.getName());
        quantityInput.setText("100");

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                if (quantityInput.getText().toString().trim().length() <= 0) {
                    quantityInput.setError(getString(R.string.invalid_quantity_text));
                    isValid = false;
                }
                if (quantityInput.getText().toString().trim().length() > 0) {
                    if (Integer.parseInt(quantityInput.getText().toString().trim()) <= 0){
                        quantityInput.setError(getString(R.string.invalid_quantity_text));
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
                FoodDrinkRecord foodDrinkRecord = new FoodDrinkRecord();
                foodDrinkRecord.setRecordId(UUID.randomUUID().toString());
                foodDrinkRecord.setUserId(user.getUserId());
                foodDrinkRecord.setItemId(selectedDrink.getDrinkId());
                foodDrinkRecord.setName(Objects.requireNonNull(nameInput.getText()).toString().trim());
                foodDrinkRecord.setCalories(selectedDrink.getCalories());
                foodDrinkRecord.setLipids(selectedDrink.getLipids());
                foodDrinkRecord.setCarbs(selectedDrink.getCarbs());
                foodDrinkRecord.setFibers(selectedDrink.getFibers());
                foodDrinkRecord.setProteins(selectedDrink.getProteins());
                foodDrinkRecord.setQuantity(Integer.parseInt(Objects.
                        requireNonNull(quantityInput.getText()).toString().trim()));
                foodDrinkRecord.setCategory(RecordType.DRINK);
                foodDrinkRecord.setDate(DateConverter.fromLongDate(new Date()));
                foodDrinkRecord.setTotalCalories(foodDrinkRecord.getQuantity() * foodDrinkRecord.getCalories() / 100);
                foodDrinkRecord.setTotalLipids(foodDrinkRecord.getQuantity() * foodDrinkRecord.getLipids() / 100);
                foodDrinkRecord.setTotalCarbs(foodDrinkRecord.getQuantity() * foodDrinkRecord.getCarbs() / 100);
                foodDrinkRecord.setTotalFibers(foodDrinkRecord.getQuantity() * foodDrinkRecord.getFibers() / 100);
                foodDrinkRecord.setTotalProteins(foodDrinkRecord.getQuantity() * foodDrinkRecord.getProteins() / 100);

                ((MainActivity) requireActivity()).addFoodDrinkRecord(foodDrinkRecord);

                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, new ProfileFragment())
                        .commit();
            }
        });
    }
}