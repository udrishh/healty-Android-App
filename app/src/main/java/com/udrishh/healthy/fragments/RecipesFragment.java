package com.udrishh.healthy.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.ChipGroup;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.adapters.PhysicalActivityAdapter;
import com.udrishh.healthy.adapters.RecipeDropdownItemAdapter;
import com.udrishh.healthy.adapters.RecipeItemAdapter;
import com.udrishh.healthy.classes.PhysicalActivity;
import com.udrishh.healthy.classes.Recipe;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecipeCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class RecipesFragment extends Fragment {
    private View view;
    private ChipGroup categories;
    private ArrayList<Recipe> allRecipes;
    private ArrayList<Recipe> selectedRecipes;
    private ListView recipesList;
    private AutoCompleteTextView recipeSearch;
    private TextView noRecipesText;

    private User user;

    public RecipesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_recipes, container, false);
        user = ((MainActivity) this.requireActivity()).getUserObject();
        allRecipes = ((MainActivity) this.requireActivity()).getRecipes();
        selectedRecipes = new ArrayList<>();

        initialiseComponents();
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    private void initialiseComponents() {
        categories = view.findViewById(R.id.recipes_categories);
        recipesList = view.findViewById(R.id.recipes_list);
        recipesList.setAdapter(new RecipeItemAdapter(getContext(), allRecipes));
        recipeSearch = view.findViewById(R.id.recipe_search);
        noRecipesText = view.findViewById(R.id.recipes_nothing);

        recipeSearch.setAdapter(new RecipeDropdownItemAdapter(requireContext(), R.layout.recipe_dropdown_item,
                (List<Recipe>) allRecipes.clone()));

        recipeSearch.setOnItemClickListener((parent, view, position, id) -> {
            Recipe selectedRecipe = (Recipe) parent.getItemAtPosition(position);
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, new RecipeDetailsFragment(selectedRecipe))
                    .commit();
        });

        recipesList.setOnItemClickListener((parent, view, position, id) -> {
            Recipe selectedRecipe = (Recipe) parent.getItemAtPosition(position);
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, new RecipeDetailsFragment(selectedRecipe))
                    .commit();
        });

        categories.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.recipes_category_recommended:
                    selectedRecipes.clear();
                    selectedRecipes = getRecommendations();
                    recipesList.setAdapter(new RecipeItemAdapter(getContext(), selectedRecipes));
                    noRecipesText.setVisibility(View.GONE);
                    break;
                case R.id.recipes_category_all:
                    recipesList.setAdapter(new RecipeItemAdapter(getContext(), allRecipes));
                    noRecipesText.setVisibility(View.GONE);
                    break;
                case R.id.recipes_category_breakfast:
                    selectedRecipes.clear();
                    for (Recipe recipe : allRecipes) {
                        if (recipe.isInCategory(RecipeCategory.BREAKFAST)) {
                            selectedRecipes.add(recipe);
                        }
                    }
                    recipesList.setAdapter(new RecipeItemAdapter(getContext(), selectedRecipes));
                    noRecipesText.setVisibility(View.GONE);
                    break;
                case R.id.recipes_category_lunch:
                    selectedRecipes.clear();
                    for (Recipe recipe : allRecipes) {
                        if (recipe.isInCategory(RecipeCategory.LUNCH)) {
                            selectedRecipes.add(recipe);
                        }
                    }
                    recipesList.setAdapter(new RecipeItemAdapter(getContext(), selectedRecipes));
                    noRecipesText.setVisibility(View.GONE);
                    break;
                case R.id.recipes_category_dinner:
                    selectedRecipes.clear();
                    for (Recipe recipe : allRecipes) {
                        if (recipe.isInCategory(RecipeCategory.DINNER)) {
                            selectedRecipes.add(recipe);
                        }
                    }
                    recipesList.setAdapter(new RecipeItemAdapter(getContext(), selectedRecipes));
                    noRecipesText.setVisibility(View.GONE);
                    break;
                case R.id.recipes_category_dessert:
                    selectedRecipes.clear();
                    for (Recipe recipe : allRecipes) {
                        if (recipe.isInCategory(RecipeCategory.DESSERT)) {
                            selectedRecipes.add(recipe);
                        }
                    }
                    recipesList.setAdapter(new RecipeItemAdapter(getContext(), selectedRecipes));
                    noRecipesText.setVisibility(View.GONE);
                    break;
                case R.id.recipes_category_drinks:
                    selectedRecipes.clear();
                    for (Recipe recipe : allRecipes) {
                        if (recipe.isInCategory(RecipeCategory.DRINKS)) {
                            selectedRecipes.add(recipe);
                        }
                    }
                    recipesList.setAdapter(new RecipeItemAdapter(getContext(), selectedRecipes));
                    noRecipesText.setVisibility(View.GONE);
                    break;
                case R.id.recipes_category_gluten_free:
                    selectedRecipes.clear();
                    for (Recipe recipe : allRecipes) {
                        if (recipe.isInCategory(RecipeCategory.GLUTEN_FREE)) {
                            selectedRecipes.add(recipe);
                        }
                    }
                    recipesList.setAdapter(new RecipeItemAdapter(getContext(), selectedRecipes));
                    noRecipesText.setVisibility(View.GONE);
                    break;
                case R.id.recipes_category_low_calories:
                    selectedRecipes.clear();
                    for (Recipe recipe : allRecipes) {
                        if (recipe.isInCategory(RecipeCategory.LOW_CALORIES)) {
                            selectedRecipes.add(recipe);
                        }
                    }
                    recipesList.setAdapter(new RecipeItemAdapter(getContext(), selectedRecipes));
                    noRecipesText.setVisibility(View.GONE);
                    break;
                default:
                    selectedRecipes.clear();
                    recipesList.setAdapter(new RecipeItemAdapter(getContext(), selectedRecipes));
                    noRecipesText.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    private ArrayList<Recipe> getRecommendations() {
        selectedRecipes = new ArrayList<>();
        RecipeCategory timeFlag;
        Calendar time = new GregorianCalendar();
        int hour = time.get(Calendar.HOUR_OF_DAY);
        // day moment
        if (hour <= 11) {
            timeFlag = RecipeCategory.BREAKFAST;
        } else if (hour <= 18) {
            timeFlag = RecipeCategory.LUNCH;
        } else {
            timeFlag = RecipeCategory.DINNER;
        }
        // calories needed
        int eatenCalories = ((MainActivity) this.requireActivity()).getEatenCalories();
        int remainingCalories = user.getCaloriesPlan() - eatenCalories;
        remainingCalories = Math.max(remainingCalories, 0);
        // getting recipes
        for (Recipe recipe : allRecipes) {
            switch (timeFlag) {
                case BREAKFAST:
                    if (recipe.getCalories() * 100 / recipe.getQuantity() <= remainingCalories) {
                        selectedRecipes.add(recipe);
                    } else if (remainingCalories == 0) {
                        if (recipe.getCategories().contains(RecipeCategory.LOW_CALORIES)) {
                            selectedRecipes.add(recipe);
                        }
                    }
                    break;
                case LUNCH:
                    if (!recipe.getCategories().contains(RecipeCategory.BREAKFAST)) {
                        if (recipe.getCalories() * 100 / recipe.getQuantity() <= remainingCalories) {
                            selectedRecipes.add(recipe);
                        } else if (remainingCalories == 0) {
                            if (recipe.getCategories().contains(RecipeCategory.LOW_CALORIES)) {
                                selectedRecipes.add(recipe);
                            }
                        }
                    }
                    break;
                case DINNER:
                    if (!recipe.getCategories().contains(RecipeCategory.BREAKFAST) ||
                            !recipe.getCategories().contains(RecipeCategory.LUNCH)) {
                        if (recipe.getCalories() * 100 / recipe.getQuantity() <= remainingCalories) {
                            selectedRecipes.add(recipe);
                        } else if (remainingCalories == 0) {
                            if (recipe.getCategories().contains(RecipeCategory.LOW_CALORIES)) {
                                selectedRecipes.add(recipe);
                            }
                        }
                    }
                    break;
            }
        }

        return selectedRecipes;
    }
}