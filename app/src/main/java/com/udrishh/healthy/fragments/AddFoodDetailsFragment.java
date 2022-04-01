package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.Food;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.utilities.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class AddFoodDetailsFragment extends Fragment {
    private View view;
    private Food selectedFood;
    private MaterialButton addBtn;
    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;

    private User user;

    public AddFoodDetailsFragment() {
    }

    public AddFoodDetailsFragment(Food food) {
        selectedFood = food;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_food_details, container, false);
        user = ((MainActivity) this.requireActivity()).getUserObject();

        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        addBtn = view.findViewById(R.id.add_food_finish);
        nameInput = view.findViewById(R.id.food_name_input);
        quantityInput = view.findViewById(R.id.food_quantity_input);

        nameInput.setText(selectedFood.getName());
        quantityInput.setText("100");

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodDrinkRecord foodDrinkRecord = new FoodDrinkRecord();
                foodDrinkRecord.setRecordId(UUID.randomUUID().toString());
                foodDrinkRecord.setUserId(user.getUserId());
                foodDrinkRecord.setItemId(selectedFood.getFoodId());
                foodDrinkRecord.setName(Objects.requireNonNull(nameInput.getText()).toString().trim());
                foodDrinkRecord.setCalories(selectedFood.getCalories());
                foodDrinkRecord.setLipids(selectedFood.getLipids());
                foodDrinkRecord.setCarbs(selectedFood.getCarbs());
                foodDrinkRecord.setFibers(selectedFood.getFibers());
                foodDrinkRecord.setProteins(selectedFood.getProteins());
                foodDrinkRecord.setQuantity(Integer.parseInt(Objects.
                        requireNonNull(quantityInput.getText()).toString().trim()));
                foodDrinkRecord.setCategory(RecordType.FOOD);
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