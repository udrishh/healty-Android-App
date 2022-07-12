package com.udrishh.healthy.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.Recipe;
import com.udrishh.healthy.enums.RecipeCategory;

import org.w3c.dom.Text;


public class RecipeDetailsFragment extends Fragment {
    private View view;
    private Recipe selectedRecipe;
    private ImageView recipeImg;
    private TextView recipeName;
    private TextView recipeCalories;
    private TextView recipeServings;
    private TextView recipeQuantity;
    private TextView recipeTotalCalories;
    private TextView recipeFlags;
    private TextView recipeIngredients;
    private MaterialButton viewBtn;
    private MaterialButton addBtn;

    public RecipeDetailsFragment() {
    }

    public RecipeDetailsFragment(Recipe selectedRecipe) {
        this.selectedRecipe = selectedRecipe;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        recipeImg = view.findViewById(R.id.recipe_details_image);
        recipeName = view.findViewById(R.id.recipe_details_title);
        recipeCalories = view.findViewById(R.id.recipe_details_calories);
        recipeServings = view.findViewById(R.id.recipe_details_servings);
        recipeIngredients = view.findViewById(R.id.recipe_details_ingredients);
        recipeTotalCalories = view.findViewById(R.id.recipe_details_total_calories);
        recipeQuantity = view.findViewById(R.id.recipe_details_quantity);
        recipeFlags = view.findViewById(R.id.recipe_flags);
        viewBtn = view.findViewById(R.id.recipe_details_view_btn);
        addBtn = view.findViewById(R.id.recipe_details_add_btn);

        Picasso.get().load(selectedRecipe.getImg()).into(recipeImg);
        recipeName.setText(getString(R.string.recipe_title, selectedRecipe.getName()));
        recipeCalories.setText(getString(R.string.recipe_details_calories,
                selectedRecipe.getCalories() * 100 / selectedRecipe.getQuantity()));
        if (selectedRecipe.isInCategory(RecipeCategory.LOW_CALORIES) && selectedRecipe.isInCategory(RecipeCategory.GLUTEN_FREE)) {
            recipeFlags.setText(getString(R.string.recipe_flags, "Calorii reduse", "Fără gluten"));
        } else if (selectedRecipe.isInCategory(RecipeCategory.LOW_CALORIES) && !selectedRecipe.isInCategory(RecipeCategory.GLUTEN_FREE)) {
            recipeFlags.setText(getString(R.string.recipe_flags, "Calorii reduse", ""));
        } else if (!selectedRecipe.isInCategory(RecipeCategory.LOW_CALORIES) && selectedRecipe.isInCategory(RecipeCategory.GLUTEN_FREE)) {
            recipeFlags.setText(getString(R.string.recipe_flags, "", "Fără gluten"));
        } else {
            recipeFlags.setText(getString(R.string.recipe_flags, "", ""));
        }

        StringBuilder ingredients = new StringBuilder();
        for (String ingredient : selectedRecipe.getIngredients()) {
            ingredients.append("‣ ").append(ingredient).append("\n");
        }
        recipeIngredients.setText(getString(R.string.recipe_details_ingredients, ingredients.toString()));

        recipeQuantity.setText(getString(R.string.recipe_details_quantity, selectedRecipe.getQuantity()));
        recipeTotalCalories.setText(getString(R.string.recipe_details_total_calories, selectedRecipe.getCalories()));

        if (selectedRecipe.getServings() == 1) {
            recipeServings.setText(getString(R.string.recipe_details_serving, 1));
        } else if(selectedRecipe.getServings() > 1){
            recipeServings.setText(getString(R.string.recipe_details_servings, selectedRecipe.getServings()));
        } else {
            recipeServings.setVisibility(View.GONE);
        }
        viewBtn.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selectedRecipe.getSource())));
        });
        addBtn.setOnClickListener(v-> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                    .replace(R.id.main_frame_layout, new AddRecipeDetailsFragment(selectedRecipe))
                    .addToBackStack(null)
                    .commit();
        });
    }
}