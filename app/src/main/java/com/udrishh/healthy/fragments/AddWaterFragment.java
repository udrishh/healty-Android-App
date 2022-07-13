package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
            foodDrinkRecord.setDate(DateConverter.fromLongDate(new Date()));
            foodDrinkRecord.setRecordType(RecordType.DRINK);
            foodDrinkRecord.setQuantity(Integer.parseInt(quantityInput.getText().toString().trim()));
            foodDrinkRecord.setItemId("water-water-water");//water id in db

            ((MainActivity) requireActivity()).addFoodDrinkRecord(foodDrinkRecord);

            BottomNavigationView bottomNavigationView =
                    ((MainActivity) requireActivity()).getBottomNavigation();
            bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
        });
    }
}