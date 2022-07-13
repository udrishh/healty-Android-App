package com.udrishh.healthy.threads;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udrishh.healthy.classes.Drink;
import com.udrishh.healthy.classes.Recipe;
import com.udrishh.healthy.enums.RecipeCategory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class RecipesUploadThread extends Thread {
    private CollectionReference collectionReference;

    public RecipesUploadThread(FirebaseFirestore db) {
        collectionReference = db.collection("Recipes");
    }

    public void run() {
        Log.d("recipes_upload", "task started");
        //READ
        try {
            URL url = new URL("https://raw.githubusercontent.com/udrishh/db_files/main/recipes_db.csv");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String line;
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            bufferedReader.readLine();//flush table head
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                count++;
                String[] items = line.split(",");
                Recipe recipe = new Recipe();
                recipe.setName(items[0]);
                String[] categories = items[1].split(";");
                for (String category : categories) {
                    if (category != null) {
                        switch (category) {
                            case "BREAKFAST":
                                recipe.addCategory(RecipeCategory.BREAKFAST);
                                break;
                            case "LUNCH":
                                recipe.addCategory(RecipeCategory.LUNCH);
                                break;
                            case "DINNER":
                                recipe.addCategory(RecipeCategory.DINNER);
                                break;
                            case "DESSERT":
                                recipe.addCategory(RecipeCategory.DESSERT);
                                break;
                            case "DRINKS":
                                recipe.addCategory(RecipeCategory.DRINKS);
                                break;
                            case "GLUTEN_FREE":
                                recipe.addCategory(RecipeCategory.GLUTEN_FREE);
                                break;
                            case "LOW_CALORIES":
                                recipe.addCategory(RecipeCategory.LOW_CALORIES);
                                break;
                        }
                    }
                }
                String[] ingredients = items[2].split(";");
                for (String ingredient : ingredients) {
                    if (ingredient != null) {
                        recipe.addIngredient(ingredient);
                    }
                }
                recipe.setServings(Integer.parseInt(items[3]));
                recipe.setQuantity(Integer.parseInt(items[4]));
                recipe.setCalories(Integer.parseInt(items[5]));
                recipe.setImg(items[6]);
                recipe.setSource(items[7]);
                recipe.setRecipeId(items[8]);
                //Log.d("drink_upload", "Read " + count + " out of " + "415 : SUCCESS");
                Log.d("recipes_upload", "Read " + count + " out of " + "415 : " + recipe.toString());

                //UPLOAD
                int finalCount = count;
                collectionReference.document(recipe.getRecipeId()).set(recipe)
                        .addOnSuccessListener(documentReference ->
                                Log.d("recipes_upload", "UPLOAD " + finalCount + " out of " + "10814 SUCCESS"))
                        .addOnFailureListener(e ->
                                Log.d("recipes_upload", "UPLOAD " + finalCount + " out of " + "10814 FAIL"));
            }
            Log.d("recipes_upload", "FINISHED");
            bufferedReader.close();
        } catch (Exception e) {
            Log.w("recipes_upload", "EXCEPTION OCCURRED : " + e.getMessage());
        }
    }
}
