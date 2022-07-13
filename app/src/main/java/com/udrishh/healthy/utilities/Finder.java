package com.udrishh.healthy.utilities;

import com.udrishh.healthy.classes.Drink;
import com.udrishh.healthy.classes.Food;
import com.udrishh.healthy.classes.PhysicalActivity;
import com.udrishh.healthy.classes.Recipe;

import java.util.ArrayList;

public interface Finder {
    static PhysicalActivity physicalActivity(ArrayList<PhysicalActivity> list, String id) {
        for (PhysicalActivity physicalActivity : list) {
            if (physicalActivity.getPhysicalActivityId().equals(id)) {
                return physicalActivity;
            }
        }
        return null;
    }

    static Recipe recipe(ArrayList<Recipe> list, String id) {
        for (Recipe recipe : list) {
            if (recipe.getRecipeId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    static Food food(ArrayList<Food> list, String id) {
        for (Food food : list) {
            if (food.getFoodId().equals(id)) {
                return food;
            }
        }
        return null;
    }

    static Drink drink(ArrayList<Drink> list, String id) {
        for (Drink drink : list) {
            if (drink.getDrinkId().equals(id)) {
                return drink;
            }
        }
        return null;
    }
}
