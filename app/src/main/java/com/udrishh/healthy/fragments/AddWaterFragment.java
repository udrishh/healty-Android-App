package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.MeasurementRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.utilities.DateConverter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class AddWaterFragment extends Fragment {
    private View view;
    private MaterialButton addBtn;
    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;
    private User user;

    public AddWaterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_water, container, false);
        user = ((MainActivity) this.requireActivity()).getUserObject();
        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        nameInput = view.findViewById(R.id.water_name_input);
        nameInput.setText(R.string.water);
        quantityInput = view.findViewById(R.id.water_quantity_input);
        addBtn = view.findViewById(R.id.add_water_finish);

        addBtn.setOnClickListener(v -> {
            boolean isValid = true;
            if (quantityInput.getText().toString().trim().length() <= 0) {
                quantityInput.setError(getString(R.string.invalid_quantity_text));
                isValid = false;
            }
            if (quantityInput.getText().toString().trim().length() > 0) {

                if (Integer.parseInt(quantityInput.getText().toString().trim()) <= 50) {
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
            foodDrinkRecord.setName(nameInput.getText().toString().trim());
            foodDrinkRecord.setRecordId(UUID.randomUUID().toString());
            foodDrinkRecord.setUserId(user.getUserId());
            foodDrinkRecord.setCalories(0);
            foodDrinkRecord.setProteins(0);
            foodDrinkRecord.setCarbs(0);
            foodDrinkRecord.setLipids(0);
            foodDrinkRecord.setFibers(0);
            foodDrinkRecord.setDate(DateConverter.fromLongDate(new Date()));
            foodDrinkRecord.setCategory(RecordType.DRINK);
            foodDrinkRecord.setQuantity(Integer.parseInt(quantityInput.getText().toString().trim()));
            foodDrinkRecord.setTotalCalories(0);
            foodDrinkRecord.setTotalProteins(0);
            foodDrinkRecord.setTotalLipids(0);
            foodDrinkRecord.setTotalCarbs(0);
            foodDrinkRecord.setTotalFibers(0);

            ((MainActivity) requireActivity()).addFoodDrinkRecord(foodDrinkRecord);

            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, new ProfileFragment())
                    .commit();

            Toast.makeText(getContext(), R.string.record_added_text, Toast.LENGTH_SHORT).show();
        });
    }
}