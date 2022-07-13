package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.MeasurementRecord;
import com.udrishh.healthy.classes.PhysicalActivity;
import com.udrishh.healthy.classes.PhysicalActivityRecord;
import com.udrishh.healthy.classes.Recipe;
import com.udrishh.healthy.classes.RecipeRecord;
import com.udrishh.healthy.classes.Record;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecipeCategory;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.utilities.Finder;

import java.util.ArrayList;


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
    private MaterialButton deleteBtn;
    private MaterialButton editBtn;
    private boolean isRecipe = false;

    private User user;
    private ArrayList<PhysicalActivity> physicalActivities;
    private ArrayList<Recipe> recipes;

    public RecordDetailsFragment() {
    }

    public RecordDetailsFragment(Record record) {
        selectedRecord = record;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record_details, container, false);

        user = ((MainActivity) this.requireActivity()).getUserObject();
        physicalActivities = ((MainActivity) this.requireActivity()).getPhysicalActivities();
        recipes = ((MainActivity) this.requireActivity()).getRecipes();

        initialiseComponents();
        determineRecordType();
        addBtnEvents();
        return view;
    }

    private void addBtnEvents() {
        editBtn.setOnClickListener(v -> {
            boolean isValid = true;
            if (recordName.getText().toString().trim().length() <= 1) {
                recordName.setError(getString(R.string.invalid_name_text));
                isValid = false;
            }
            if (isValid) {
                recordName.setError(null);

                if (isRecipe) {
                    ((RecipeRecord) selectedRecord).setName(recordName.getText().toString().trim());
                    ((MainActivity) requireActivity()).editRecipeRecord((RecipeRecord) selectedRecord);
                } else if (recordType == RecordType.FOOD || recordType == RecordType.DRINK) {
                    ((FoodDrinkRecord) selectedRecord).setName(recordName.getText().toString().trim());
                    ((MainActivity) requireActivity()).editFoodDrinkRecord((FoodDrinkRecord) selectedRecord);
                } else if (recordType == RecordType.HEIGHT || recordType == RecordType.WEIGHT) {
                    ((MeasurementRecord) selectedRecord).setName(recordName.getText().toString().trim());
                    ((MainActivity) requireActivity()).editMeasurementRecord((MeasurementRecord) selectedRecord);
                } else if (recordType == RecordType.PHYSICAL_ACTIVITY) {
                    ((PhysicalActivityRecord) selectedRecord).setName(recordName.getText().toString().trim());
                    ((MainActivity) requireActivity()).editPhysicalActivityRecord((PhysicalActivityRecord) selectedRecord);
                }
                BottomNavigationView bottomNavigationView =
                        ((MainActivity) requireActivity()).getBottomNavigation();
                bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
            }
        });

        deleteBtn.setOnClickListener(v -> {
            if (isRecipe) {
                ((MainActivity) requireActivity()).deleteRecipeRecord((RecipeRecord) selectedRecord);
            } else if (recordType == RecordType.FOOD || recordType == RecordType.DRINK) {
                ((MainActivity) requireActivity()).deleteFoodDrinkRecord((FoodDrinkRecord) selectedRecord);
            } else if (recordType == RecordType.HEIGHT || recordType == RecordType.WEIGHT) {
                ((MainActivity) requireActivity()).deleteMeasurementRecord((MeasurementRecord) selectedRecord, recordType);
            } else if (recordType == RecordType.PHYSICAL_ACTIVITY) {
                ((MainActivity) requireActivity()).deletePhysicalActivityRecord((PhysicalActivityRecord) selectedRecord);
            }
            BottomNavigationView bottomNavigationView =
                    ((MainActivity) requireActivity()).getBottomNavigation();
            bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
        });
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
            Recipe recipe = Finder.recipe(recipes, ((RecipeRecord) selectedRecord).getItemId());
            if (recipe != null) {
                if (recipe.getCategories().contains(RecipeCategory.DRINKS)) {
                    recordType = RecordType.DRINK;
                    initialiseDrinkRecord();
                }
            }
            initialiseFoodRecord();
        } else if (selectedRecord instanceof PhysicalActivityRecord) {
            recordType = RecordType.PHYSICAL_ACTIVITY;
            initialisePhysicalActivityRecord();
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
        deleteBtn = view.findViewById(R.id.record_details_delete);
        editBtn = view.findViewById(R.id.record_details_edit);
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
            int calories = 0;
            Recipe recipe = Finder.recipe(recipes, ((RecipeRecord) selectedRecord).getItemId());
            if (recipe != null) {
                calories += recipe.getCalories() * 100 / ((RecipeRecord) selectedRecord).getQuantity();
            }
            recordValue.setText(getString(R.string.record_details_total_calories_text, calories));
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
        if (isRecipe) {
            recordImage.setImageResource(R.drawable.meal_icon);
            recordCategory.setText(getString(R.string.add_recipe_title));
            recordName.setText(((RecipeRecord) selectedRecord).getName());
            recordQuantity = view.findViewById(R.id.record_details_drink_quantity);
            recordQuantity.setText(getString(R.string.record_details_drink_quantity_text,
                    ((RecipeRecord) selectedRecord).getQuantity()));
            recordValue = view.findViewById(R.id.record_details_total_calories);
            int calories = 0;
            Recipe recipe = Finder.recipe(recipes, ((RecipeRecord) selectedRecord).getItemId());
            if (recipe != null) {
                calories += recipe.getCalories() * 100 / ((RecipeRecord) selectedRecord).getQuantity();
            }
            recordValue.setText(getString(R.string.record_details_total_calories_text, calories));
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

    private void initialisePhysicalActivityRecord() {
        recordImage.setImageResource(R.drawable.activity_icon);
        recordCategory.setText(getString(R.string.add_physical_activity_title));
        recordName.setText(((PhysicalActivityRecord) selectedRecord).getName());
        recordQuantity = view.findViewById(R.id.record_details_duration);
        recordQuantity.setText(getString(R.string.record_details_duration_text,
                (int) ((PhysicalActivityRecord) selectedRecord).getQuantity()));
        recordValue = view.findViewById(R.id.record_details_burned_calories);

        PhysicalActivity physicalActivity =
                Finder.physicalActivity(physicalActivities, ((PhysicalActivityRecord) selectedRecord).getItemId());
        int totalCalories = Math.round((float) ((PhysicalActivityRecord) selectedRecord).getQuantity() / 60
                * physicalActivity.getCalories() * user.getWeight());

        recordValue.setText(getString(R.string.record_details_burned_calories_text,
                totalCalories));

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