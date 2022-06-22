package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.MeasurementRecord;
import com.udrishh.healthy.classes.PhysicalActivity;
import com.udrishh.healthy.classes.PhysicalActivityRecord;
import com.udrishh.healthy.classes.Recipe;
import com.udrishh.healthy.classes.RecipeRecord;
import com.udrishh.healthy.classes.Record;
import com.udrishh.healthy.enums.RecipeCategory;
import com.udrishh.healthy.enums.RecordType;


public class RecordDetailsFragment extends Fragment {
    private View view;
    private Record selectedRecord;
    private RecordType recordType = RecordType.NONE;
    private TextView recordCategory;
    private ImageView recordImage;
    private TextInputEditText recordName;
    private TextView recordQuantity;
    private TextView recordValue;
    private TextView recordDate;
    private boolean isRecipe = false;

    public RecordDetailsFragment() {
    }

    public RecordDetailsFragment(Record record) {
        selectedRecord = record;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record_details, container, false);
        initialiseComponents();
        determineRecordType();
        return view;
    }

    private void determineRecordType() {
        if (selectedRecord instanceof FoodDrinkRecord) {
            if (((FoodDrinkRecord) selectedRecord).getCategory() == RecordType.DRINK) {
                recordType = RecordType.DRINK;
                initialiseDrinkRecord();
            } else {
                recordType = RecordType.FOOD;
                initialiseFoodRecord();
            }
        } else if (selectedRecord instanceof RecipeRecord) {
            isRecipe = true;
            if (((RecipeRecord) selectedRecord).getRecipeCategory() == RecipeCategory.DRINKS) {
                recordType = RecordType.DRINK;
                initialiseDrinkRecord();
            }
            initialiseFoodRecord();
        } else if (selectedRecord instanceof PhysicalActivityRecord) {
            recordType = RecordType.PHYSICAL_ACTIVITY;
            initialisePhisicalActivityRecord();
        } else {
            if (((MeasurementRecord) selectedRecord).getMeasurementCategory() == RecordType.HEIGHT) {
                recordType = RecordType.HEIGHT;
                initialiseHeightRecord();
            } else {
                recordType = RecordType.WEIGHT;
                initialiseWeightRecord();
            }
        }
    }

    private void initialiseComponents() {
        recordCategory = view.findViewById(R.id.record_type_title);
        recordDate = view.findViewById(R.id.record_details_date);
        recordName = view.findViewById(R.id.record_details_name);
        recordImage = view.findViewById(R.id.record_details_image);

        recordDate.setText(getString(R.string.record_details_date_text, selectedRecord.getDate()));
    }

    private void initialiseFoodRecord() {
        if (isRecipe) {
            recordImage.setImageResource(R.drawable.meal_icon);
            recordCategory.setText(getString(R.string.add_recipe_title));
            recordName.setText(((RecipeRecord) selectedRecord).getName());
            recordQuantity = view.findViewById(R.id.record_details_food_quantity);
            recordQuantity.setText(getString(R.string.record_details_food_quantity_text,
                    ((RecipeRecord) selectedRecord).getQuantity()));
            recordValue = view.findViewById(R.id.record_details_total_calories);
            recordValue.setText(getString(R.string.record_details_total_calories_text,
                    ((RecipeRecord) selectedRecord).getTotalCalories()));
        } else {
            recordImage.setImageResource(R.drawable.food_icon);
            recordCategory.setText(getString(R.string.add_food_db_food_title));
            recordName.setText(((FoodDrinkRecord) selectedRecord).getName());
            recordQuantity = view.findViewById(R.id.record_details_food_quantity);
            recordQuantity.setText(getString(R.string.record_details_food_quantity_text,
                    ((FoodDrinkRecord) selectedRecord).getQuantity()));
            recordValue = view.findViewById(R.id.record_details_total_calories);
            recordValue.setText(getString(R.string.record_details_total_calories_text,
                    ((FoodDrinkRecord) selectedRecord).getTotalCalories()));
        }
        recordQuantity.setVisibility(View.VISIBLE);
        recordValue.setVisibility(View.VISIBLE);
    }

    private void initialiseDrinkRecord() {
        Toast.makeText(this.getContext(), "test", Toast.LENGTH_LONG).show();
        if (isRecipe) {
            recordImage.setImageResource(R.drawable.meal_icon);
            recordCategory.setText(getString(R.string.add_recipe_title));
            recordName.setText(((RecipeRecord) selectedRecord).getName());
            recordQuantity = view.findViewById(R.id.record_details_drink_quantity);
            recordQuantity.setText(getString(R.string.record_details_drink_quantity_text,
                    ((RecipeRecord) selectedRecord).getQuantity()));
            recordValue = view.findViewById(R.id.record_details_total_calories);
            recordValue.setText(getString(R.string.record_details_total_calories_text,
                    ((RecipeRecord) selectedRecord).getTotalCalories()));
        } else {
            recordImage.setImageResource(R.drawable.glass_icon);
            recordCategory.setText(getString(R.string.add_drink_db_drink_title));
            recordName.setText(((FoodDrinkRecord) selectedRecord).getName());
            recordQuantity = view.findViewById(R.id.record_details_drink_quantity);
            recordQuantity.setText(getString(R.string.record_details_drink_quantity_text,
                    ((FoodDrinkRecord) selectedRecord).getQuantity()));
            recordValue = view.findViewById(R.id.record_details_total_calories);
            recordValue.setText(getString(R.string.record_details_total_calories_text,
                    ((FoodDrinkRecord) selectedRecord).getTotalCalories()));
        }
        recordQuantity.setVisibility(View.VISIBLE);
        recordValue.setVisibility(View.VISIBLE);
    }

    private void initialisePhisicalActivityRecord() {
        recordImage.setImageResource(R.drawable.activity_icon);
        recordCategory.setText(getString(R.string.add_physical_activity_title));
        recordName.setText(((PhysicalActivityRecord) selectedRecord).getName());
        recordQuantity = view.findViewById(R.id.record_details_duration);
        recordQuantity.setText(getString(R.string.record_details_duration_text,
                (int) ((PhysicalActivityRecord) selectedRecord).getDuration()));
        recordValue = view.findViewById(R.id.record_details_burned_calories);
        recordValue.setText(getString(R.string.record_details_burned_calories_text,
                (int) ((PhysicalActivityRecord) selectedRecord).getCalories()));

        recordQuantity.setVisibility(View.VISIBLE);
        recordValue.setVisibility(View.VISIBLE);
    }

    private void initialiseWeightRecord() {
        recordImage.setImageResource(R.drawable.measure_icon);
        recordCategory.setText(getString(R.string.add_weight_title));
        recordName.setText(((MeasurementRecord) selectedRecord).getName());
        recordValue = view.findViewById(R.id.record_details_weight_value);
        recordValue.setText(getString(R.string.record_details_weight_value_text,
                (int) ((MeasurementRecord) selectedRecord).getValue()));
        recordValue.setVisibility(View.VISIBLE);
    }

    private void initialiseHeightRecord() {
        recordImage.setImageResource(R.drawable.measure_icon);
        recordCategory.setText(getString(R.string.add_height_title));
        recordName.setText(((MeasurementRecord) selectedRecord).getName());
        recordValue = view.findViewById(R.id.record_details_height_value);
        recordValue.setText(getString(R.string.record_details_height_value_text,
                (int) ((MeasurementRecord) selectedRecord).getValue()));
        recordValue.setVisibility(View.VISIBLE);
    }
}