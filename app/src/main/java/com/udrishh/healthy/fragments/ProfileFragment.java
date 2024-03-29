package com.udrishh.healthy.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.Drink;
import com.udrishh.healthy.classes.Food;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.PhysicalActivity;
import com.udrishh.healthy.classes.PhysicalActivityRecord;
import com.udrishh.healthy.classes.Recipe;
import com.udrishh.healthy.classes.RecipeRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecipeCategory;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.enums.Sex;
import com.udrishh.healthy.utilities.DateConverter;
import com.udrishh.healthy.utilities.Finder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private View view;

    private CircularProgressIndicator caloriesProgressIndicator;
    private LinearProgressIndicator liquidsProgressIndicator;
    private TextView caloriesProgressText;
    private TextView liquidsProgressText;
    private TextView burnedText;
    private TextView eatenText;
    private TextView proteinsText;
    private TextView lipidsText;
    private TextView carbsText;
    private TextView fibersText;
    private ImageView expandBtn;

    private ConstraintLayout cardLayout;
    private LinearLayout userDetailsLayout;

    private User user;
    private ArrayList<FoodDrinkRecord> foodDrinkRecords;
    private ArrayList<PhysicalActivityRecord> physicalActivityRecords;
    private ArrayList<RecipeRecord> recipeRecords;
    private ArrayList<Recipe> recipes;
    private ArrayList<Food> foods;
    private ArrayList<Drink> drinks;
    private ArrayList<PhysicalActivity> physicalActivities;

    private int caloriesProgress = 0;
    private int caloriesEaten = 0;
    private int caloriesBurned = 0;
    private int proteins = 0;
    private int fibers = 0;
    private int carbs = 0;
    private int lipids = 0;
    private int liquids = 0;

    private boolean isExpanded = false;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        importObjects();
        if (user != null) {
            initialiseProfileCardComponents();
            initialiseProgressCardComponents();
            loadEatenCaloriesProgress();
            loadBurnedCaloriesProgress();
        }
        return view;
    }

    private void importObjects() {
        user = ((MainActivity) this.requireActivity()).getUserObject();
        foodDrinkRecords = ((MainActivity) this.requireActivity()).getFoodDrinkRecords();
        physicalActivityRecords = ((MainActivity) this.requireActivity()).getPhysicalActivityRecords();
        recipeRecords = ((MainActivity) this.requireActivity()).getRecipeRecords();
        recipes = ((MainActivity) this.requireActivity()).getRecipes();
        foods = ((MainActivity) this.requireActivity()).getFoods();
        drinks = ((MainActivity) this.requireActivity()).getDrinks();
        physicalActivities = ((MainActivity) this.requireActivity()).getPhysicalActivities();
    }

    private void loadBurnedCaloriesProgress() {
        Calendar todayDate = Calendar.getInstance();
        Calendar recordDate = Calendar.getInstance();
        for (PhysicalActivityRecord physicalActivityRecord : physicalActivityRecords) {
            recordDate.setTime(Objects.requireNonNull(DateConverter.fromLongString(physicalActivityRecord.getDate())));
            if (todayDate.get(Calendar.DAY_OF_MONTH) == recordDate.get(Calendar.DAY_OF_MONTH)
                    && todayDate.get(Calendar.MONTH) == recordDate.get(Calendar.MONTH)
                    && todayDate.get(Calendar.YEAR) == recordDate.get(Calendar.YEAR)) {
                PhysicalActivity physicalActivity =
                        Finder.physicalActivity(physicalActivities, physicalActivityRecord.getItemId());
                if (physicalActivity != null) {
                    int totalCalories = Math.round((float) physicalActivityRecord.getQuantity() / 60
                            * physicalActivity.getCalories() * user.getWeight());
                    caloriesBurned += totalCalories;
                    burnedText.setText(getString(R.string.calories_burned_counter, caloriesBurned));
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void loadEatenCaloriesProgress() {
        loadFoodDrinkRecordsProgress();
        loadRecipeRecordsProgress();
        showGradient();
        ((MainActivity) this.requireActivity()).setEatenCalories(caloriesEaten);
    }

    private void loadFoodDrinkRecordsProgress() {
        Calendar todayDate = Calendar.getInstance();
        Calendar recordDate = Calendar.getInstance();
        for (FoodDrinkRecord foodDrinkRecord : foodDrinkRecords) {
            recordDate.setTime(Objects.requireNonNull(DateConverter.fromLongString(foodDrinkRecord.getDate())));
            if (todayDate.get(Calendar.DAY_OF_MONTH) == recordDate.get(Calendar.DAY_OF_MONTH)
                    && todayDate.get(Calendar.MONTH) == recordDate.get(Calendar.MONTH)
                    && todayDate.get(Calendar.YEAR) == recordDate.get(Calendar.YEAR)) {
                if (foodDrinkRecord.getRecordType() == RecordType.FOOD) {
                    Food food = Finder.food(foods, foodDrinkRecord.getItemId());
                    if (food != null) {
                        if (food.getFoodId().contains("x")) {
                            caloriesProgress += food.getCalories();
                            caloriesEaten += food.getCalories();
                        } else {
                            caloriesProgress += (float)food.getCalories() / 100 * foodDrinkRecord.getQuantity();
                            caloriesEaten += (float)food.getCalories() / 100 * foodDrinkRecord.getQuantity();
                        }
                        proteins += food.getProteins();
                        lipids += food.getLipids();
                        carbs += food.getCarbs();
                        fibers += food.getFibers();
                    }
                } else {
                    Drink drink = Finder.drink(drinks, foodDrinkRecord.getItemId());
                    if (drink != null) {
                        if (drink.getDrinkId().contains("x")) {
                            caloriesProgress += drink.getCalories();
                            caloriesEaten += drink.getCalories();
                        } else {
                            caloriesProgress += (float)drink.getCalories() / 100 * foodDrinkRecord.getQuantity();
                            caloriesEaten += (float)drink.getCalories() / 100 * foodDrinkRecord.getQuantity();
                        }
                        proteins += drink.getProteins();
                        lipids += drink.getLipids();
                        carbs += drink.getCarbs();
                        fibers += drink.getFibers();
                    }
                }

                caloriesProgressText.setText(getString(R.string.calories_progress_counter,
                        caloriesProgress, user.getCaloriesPlan()));
                eatenText.setText(getString(R.string.calories_eaten_counter, caloriesEaten));
                proteinsText.setText(getString(R.string.proteins_counter, proteins));
                lipidsText.setText(getString(R.string.lipids_counter, lipids));
                carbsText.setText(getString(R.string.carbs_counter, carbs));
                fibersText.setText(getString(R.string.fibers_counter, fibers));
                caloriesProgressIndicator.setProgress(caloriesProgress, true);
                if (foodDrinkRecord.getRecordType() == RecordType.DRINK) {
                    liquids += foodDrinkRecord.getQuantity();
                    liquidsProgressText.setText(getString(R.string.liquids_progress_counter, liquids, 2000));
                    liquidsProgressIndicator.setProgress(liquids, true);
                }
            }
        }
    }

    private void loadRecipeRecordsProgress() {
        Calendar todayDate = Calendar.getInstance();
        Calendar recordDate = Calendar.getInstance();
        for (RecipeRecord recipeRecord : recipeRecords) {
            recordDate.setTime(Objects.requireNonNull(DateConverter.fromLongString(recipeRecord.getDate())));
            if (todayDate.get(Calendar.DAY_OF_MONTH) == recordDate.get(Calendar.DAY_OF_MONTH)
                    && todayDate.get(Calendar.MONTH) == recordDate.get(Calendar.MONTH)
                    && todayDate.get(Calendar.YEAR) == recordDate.get(Calendar.YEAR)) {

                int calories = 0;
                Recipe recipe = Finder.recipe(recipes, (recipeRecord).getItemId());
                if (recipe != null) {
                    calories += (((float)recipe.getCalories() / recipe.getQuantity() * 100) /100) * (recipeRecord).getQuantity();
                    caloriesProgress += calories;
                    caloriesEaten += calories;
                    caloriesProgressText.setText(getString(R.string.calories_progress_counter,
                            caloriesProgress, user.getCaloriesPlan()));
                    eatenText.setText(getString(R.string.calories_eaten_counter, caloriesEaten));
                    proteinsText.setText(getString(R.string.proteins_counter, proteins));
                    lipidsText.setText(getString(R.string.lipids_counter, lipids));
                    carbsText.setText(getString(R.string.carbs_counter, carbs));
                    fibersText.setText(getString(R.string.fibers_counter, fibers));
                    caloriesProgressIndicator.setProgress(caloriesProgress, true);
                    if (recipe.getCategories().contains(RecipeCategory.DRINKS)) {
                        liquids += recipeRecord.getQuantity();
                        liquidsProgressText.setText(getString(R.string.liquids_progress_counter, liquids, 2000));
                        liquidsProgressIndicator.setProgress(liquids, true);
                    }
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showGradient() {
        if (caloriesEaten <= user.getCaloriesPlan() * 0.5f) {
            cardLayout.setBackground(requireContext().getDrawable(R.drawable.gradient_progress_low));
        } else if (caloriesEaten > user.getCaloriesPlan() * 0.5f
                && caloriesEaten <= user.getCaloriesPlan()) {
            cardLayout.setBackground(requireContext().getDrawable(R.drawable.gradient_progress_medium));
        } else if (caloriesEaten > user.getCaloriesPlan()
                && caloriesEaten <= user.getCaloriesPlan() * 1.25) {
            cardLayout.setBackground(requireContext().getDrawable(R.drawable.gradient_progress_high));
        } else if (caloriesEaten > user.getCaloriesPlan() * 1.25) {
            cardLayout.setBackground(requireContext().getDrawable(R.drawable.gradient_progress_high));
            caloriesProgressIndicator.setIndicatorColor(Color.RED);
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
        userDetailsLayout = view.findViewById(R.id.user_details_layout);
        TextView greetingText = view.findViewById(R.id.user_greeting_textview);
        int currHour = new Date().getHours();
        if (currHour >= 7 && currHour < 11) {
            greetingText.setText(getString(R.string.user_greeting_text, getString(R.string.greeting_morning), user.getName()));
        } else if (currHour >= 10 && currHour < 20) {
            greetingText.setText(getString(R.string.user_greeting_text, getString(R.string.greeting_afternoon), user.getName()));
        } else {
            greetingText.setText(getString(R.string.user_greeting_text, getString(R.string.greeting_night), user.getName()));

        }

        TextView nameText = view.findViewById(R.id.user_name_textview);
        nameText.setText(getString(R.string.user_profile_name, user.getName()));
        TextView ageText = view.findViewById(R.id.user_age_textview);
        ageText.setText(getString(R.string.user_profile_age,
                Calendar.getInstance().get(Calendar.YEAR)
                        - Integer.parseInt(user.getBirthdate().split("/")[2])));
        TextView sexText = view.findViewById(R.id.user_sex_textview);
        ImageView sexIcon = view.findViewById(R.id.user_sex_icon);
        if (user.getSex() == Sex.MALE) {
            sexText.setText(getString(R.string.user_profile_sex, getString(R.string.signup_user_data_sex_m_text)));
            sexIcon.setImageResource(R.drawable.male_gender_icon);
        } else {
            sexText.setText(getString(R.string.user_profile_sex, getString(R.string.signup_user_data_sex_f_text)));
            sexIcon.setImageResource(R.drawable.female_gender_icon);
        }
        TextView heightText = view.findViewById(R.id.user_height_textview);
        heightText.setText(getString(R.string.user_profile_height, user.getHeight()));
        TextView weightText = view.findViewById(R.id.user_weight_textview);
        weightText.setText(getString(R.string.user_profile_weight, user.getWeight()));
        TextView planText = view.findViewById(R.id.user_calories_textview);
        planText.setText(getString(R.string.user_profile_calories, user.getCaloriesPlan()));
        cardLayout = view.findViewById(R.id.progress_card_layout);

        expandBtn = view.findViewById(R.id.user_collapse_icon);
        expandBtn.setOnClickListener(v -> {
            int visibility;
            if (isExpanded) {
                visibility = View.GONE;
                expandBtn.setImageResource(R.drawable.expand_icon);
            } else {
                visibility = View.VISIBLE;
                expandBtn.setImageResource(R.drawable.collapse_icon);
            }
            Animation slideInAnimation;
            slideInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in);
            Animation slideOutAnimation;
            slideOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out);
            slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    userDetailsLayout.setVisibility(visibility);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if (visibility == View.GONE) {
                userDetailsLayout.startAnimation(slideOutAnimation);
            } else {
                userDetailsLayout.startAnimation(slideInAnimation);
                userDetailsLayout.setVisibility(visibility);
            }
            userDetailsLayout.animate();
            isExpanded = !isExpanded;
        });

        ProgressBar loadingData = view.findViewById(R.id.progress_loading);
        TextView loadingDataText = view.findViewById(R.id.progress_loading_text);
        if (((MainActivity) this.requireActivity()).getTasksReady() >= 4) {
            loadingData.setVisibility(View.GONE);
            loadingDataText.setVisibility(View.GONE);
        }
    }
}