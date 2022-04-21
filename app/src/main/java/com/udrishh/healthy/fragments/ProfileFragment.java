package com.udrishh.healthy.fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.PhysicalActivity;
import com.udrishh.healthy.classes.PhysicalActivityRecord;
import com.udrishh.healthy.classes.RecipeRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecipeCategory;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.enums.Sex;
import com.udrishh.healthy.utilities.DateConverter;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private View view;

    private CircularProgressIndicator caloriesProgressIndicator;
    private LinearProgressIndicator liquidsProgressIndicator;
    private TextView greetingText;
    private TextView nameText;
    private TextView ageText;
    private TextView sexText;
    private TextView heightText;
    private TextView weightText;
    private TextView planText;
    private ImageView sexIcon;
    private TextView caloriesProgressText;
    private TextView liquidsProgressText;
    private TextView burnedText;
    private TextView eatenText;
    private TextView proteinsText;
    private TextView lipidsText;
    private TextView carbsText;
    private TextView fibersText;

    private ConstraintLayout cardLayout;

    private User user;
    private ArrayList<FoodDrinkRecord> foodDrinkRecords;
    private ArrayList<PhysicalActivityRecord> physicalActivityRecords;
    private ArrayList<RecipeRecord> recipeRecords;

    private int caloriesProgress;
    private int caloriesEaten;
    private int caloriesBurned;
    private int proteins;
    private int fibers;
    private int carbs;
    private int lipids;
    private int liquids;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        user = ((MainActivity) this.requireActivity()).getUserObject();
        foodDrinkRecords = ((MainActivity) this.requireActivity()).getFoodDrinkRecords();
        physicalActivityRecords = ((MainActivity) this.requireActivity()).getPhysicalActivityRecords();
        recipeRecords = ((MainActivity) this.requireActivity()).getRecipeRecords();

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (user != null) {
            initialiseProfileCardComponents();
            initialiseProgressCardComponents();

            loadFoodDrinksRecordsProgress();
            loadPhysicalActivtiesProgress();
        }

        return view;
    }

    private void loadPhysicalActivtiesProgress() {
        caloriesBurned = 0;
        Calendar todayDate = Calendar.getInstance();
        Calendar recordDate = Calendar.getInstance();
        for (PhysicalActivityRecord physicalActivityRecord : physicalActivityRecords) {
            recordDate.setTime(DateConverter.fromLongString(physicalActivityRecord.getDate()));
            if (todayDate.get(Calendar.DAY_OF_MONTH) == recordDate.get(Calendar.DAY_OF_MONTH)
                    && todayDate.get(Calendar.MONTH) == recordDate.get(Calendar.MONTH)
                    && todayDate.get(Calendar.YEAR) == recordDate.get(Calendar.YEAR)) {
                caloriesBurned += physicalActivityRecord.getTotalCalories();
                burnedText.setText(getString(R.string.calories_burned_counter, caloriesBurned));
            }
        }
    }

    private void loadFoodDrinksRecordsProgress() {
        Log.d("mytag", "Profile fragment loading progress....");
        caloriesProgress = 0;
        caloriesEaten = 0;
        proteins = 0;
        fibers = 0;
        carbs = 0;
        lipids = 0;
        liquids = 0;
        Calendar todayDate = Calendar.getInstance();
        Calendar recordDate = Calendar.getInstance();

        for (FoodDrinkRecord foodDrinkRecord : foodDrinkRecords) {
            Log.d("mytag", "Adding record: " + foodDrinkRecord.toString());
            recordDate.setTime(DateConverter.fromLongString(foodDrinkRecord.getDate()));
            if (todayDate.get(Calendar.DAY_OF_MONTH) == recordDate.get(Calendar.DAY_OF_MONTH)
                    && todayDate.get(Calendar.MONTH) == recordDate.get(Calendar.MONTH)
                    && todayDate.get(Calendar.YEAR) == recordDate.get(Calendar.YEAR)) {
                caloriesProgress += foodDrinkRecord.getTotalCalories();
                caloriesEaten += foodDrinkRecord.getTotalCalories();
                proteins += foodDrinkRecord.getTotalProteins();
                lipids += foodDrinkRecord.getTotalLipids();
                carbs += foodDrinkRecord.getTotalCarbs();
                fibers += foodDrinkRecord.getTotalFibers();

                caloriesProgressText.setText(getString(R.string.calories_progress_counter,
                        caloriesProgress, user.getCaloriesPlan()));
                eatenText.setText(getString(R.string.calories_eaten_counter, caloriesEaten));
                proteinsText.setText(getString(R.string.proteins_counter, proteins));
                lipidsText.setText(getString(R.string.lipids_counter, lipids));
                carbsText.setText(getString(R.string.carbs_counter, carbs));
                fibersText.setText(getString(R.string.fibers_counter, fibers));

                caloriesProgressIndicator.setProgress(caloriesProgress, true);

                if (foodDrinkRecord.getCategory() == RecordType.DRINK) {
                    Log.d("mytag", "This is a drink!");
                    liquids += foodDrinkRecord.getQuantity();
                    liquidsProgressText.setText(getString(R.string.liquids_progress_counter, liquids, 2000));
                    liquidsProgressIndicator.setProgress(liquids, true);
                }
            }
        }

        for(RecipeRecord recipeRecord : recipeRecords){
            recordDate.setTime(DateConverter.fromLongString(recipeRecord.getDate()));
            if (todayDate.get(Calendar.DAY_OF_MONTH) == recordDate.get(Calendar.DAY_OF_MONTH)
                    && todayDate.get(Calendar.MONTH) == recordDate.get(Calendar.MONTH)
                    && todayDate.get(Calendar.YEAR) == recordDate.get(Calendar.YEAR)) {
                caloriesProgress += recipeRecord.getTotalCalories();
                caloriesEaten += recipeRecord.getTotalCalories();

                caloriesProgressText.setText(getString(R.string.calories_progress_counter,
                        caloriesProgress, user.getCaloriesPlan()));
                eatenText.setText(getString(R.string.calories_eaten_counter, caloriesEaten));
                proteinsText.setText(getString(R.string.proteins_counter, proteins));
                lipidsText.setText(getString(R.string.lipids_counter, lipids));
                carbsText.setText(getString(R.string.carbs_counter, carbs));
                fibersText.setText(getString(R.string.fibers_counter, fibers));

                caloriesProgressIndicator.setProgress(caloriesProgress, true);

                if (recipeRecord.getCategory() == RecipeCategory.DRINKS) {
                    Log.d("mytag", "This is a drink!");
                    liquids += recipeRecord.getQuantity();
                    liquidsProgressText.setText(getString(R.string.liquids_progress_counter, liquids, 2000));
                    liquidsProgressIndicator.setProgress(liquids, true);
                }
            }
        }
    }

    private void initialiseProgressCardComponents() {
        caloriesProgressIndicator = view.findViewById(R.id.progress_card_calories_progress_indicator);
        caloriesProgressIndicator.setMax(user.getCaloriesPlan());
        caloriesProgressIndicator.setMin(0);
        liquidsProgressIndicator = view.findViewById(R.id.progress_card_liquids_progress_indicator);
        liquidsProgressIndicator.setMax(2000);
        liquidsProgressIndicator.setMin(0);
        caloriesProgressText = view.findViewById(R.id.progress_card_tv_calories_progress_number);
        caloriesProgressText.setText(getString(R.string.calories_progress_counter, 0, user.getCaloriesPlan()));
        liquidsProgressText = view.findViewById(R.id.progress_card_tv_liquids_progress_number);
        liquidsProgressText.setText(getString(R.string.liquids_progress_counter, 0, 2000));
        burnedText = view.findViewById(R.id.progress_card_tv_calories_burned_number);
        burnedText.setText(getString(R.string.calories_burned_counter, 0));
        eatenText = view.findViewById(R.id.progress_card_tv_calories_eaten_number);
        eatenText.setText(getString(R.string.calories_eaten_counter, 0));
        proteinsText = view.findViewById(R.id.progress_card_proteins_counter);
        proteinsText.setText(getString(R.string.proteins_counter, 0));
        lipidsText = view.findViewById(R.id.progress_card_lipids_counter);
        lipidsText.setText(getString(R.string.lipids_counter, 0));
        carbsText = view.findViewById(R.id.progress_card_carbs_counter);
        carbsText.setText(getString(R.string.carbs_counter, 0));
        fibersText = view.findViewById(R.id.progress_card_fibers_counter);
        fibersText.setText(getString(R.string.fibers_counter, 0));

        cardLayout = view.findViewById(R.id.progress_card_layout);
    }

    @SuppressLint("StringFormatMatches")
    private void initialiseProfileCardComponents() {
        greetingText = view.findViewById(R.id.user_greeting_textview);
        int currHour = new Date().getHours();
        if (currHour >= 7 && currHour < 11) {
            greetingText.setText(getString(R.string.user_greeting_text, getString(R.string.greeting_morning), user.getName()));
        } else if (currHour >= 10 && currHour < 20) {
            greetingText.setText(getString(R.string.user_greeting_text, getString(R.string.greeting_afternoon), user.getName()));
        } else {
            greetingText.setText(getString(R.string.user_greeting_text, getString(R.string.greeting_night), user.getName()));

        }

        nameText = view.findViewById(R.id.user_name_textview);
        nameText.setText(getString(R.string.user_profile_name, user.getName()));
        ageText = view.findViewById(R.id.user_age_textview);
        ageText.setText(getString(R.string.user_profile_age,
                Calendar.getInstance().get(Calendar.YEAR)
                        - Integer.parseInt(user.getBirthdate().split("/")[2])));
        sexText = view.findViewById(R.id.user_sex_textview);
        sexIcon = view.findViewById(R.id.user_sex_icon);
        if (user.getSex() == Sex.MALE) {
            sexText.setText(getString(R.string.user_profile_sex, getString(R.string.signup_user_data_sex_m_text)));
            sexIcon.setImageResource(R.drawable.male_gender_icon);
        } else {
            sexText.setText(getString(R.string.user_profile_sex, getString(R.string.signup_user_data_sex_f_text)));
            sexIcon.setImageResource(R.drawable.female_gender_icon);
        }
        heightText = view.findViewById(R.id.user_height_textview);
        heightText.setText(getString(R.string.user_profile_height, user.getHeight()));
        weightText = view.findViewById(R.id.user_weight_textview);
        weightText.setText(getString(R.string.user_profile_weight, user.getWeight()));
        planText = view.findViewById(R.id.user_calories_textview);
        planText.setText(getString(R.string.user_profile_calories, user.getCaloriesPlan()));
    }
}