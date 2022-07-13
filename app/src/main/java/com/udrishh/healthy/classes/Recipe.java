package com.udrishh.healthy.classes;

import com.udrishh.healthy.enums.RecipeCategory;

import java.util.ArrayList;

public class Recipe {
    private String recipeId;
    private String name;
    private ArrayList<String> ingredients;
    private ArrayList<RecipeCategory> categories;
    private int servings;
    private int quantity;
    private int calories;
    private String img;
    private String source;

    public Recipe() {
        categories = new ArrayList<>();
        ingredients = new ArrayList<>();
        recipeId = "N/A";
        name = "N/A";
        servings = 0;
        quantity = 0;
        calories = 0;
        img = "N/A";
        source = "N/A";
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isInCategory(RecipeCategory givenCategory) {
        for (RecipeCategory category : categories) {
            if (category.equals(givenCategory)) {
                return true;
            }
        }
        return false;
    }

    public void addCategory(RecipeCategory category) {
        categories.add(category);
    }

    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    public ArrayList<RecipeCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<RecipeCategory> categories) {
        this.categories = categories;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
