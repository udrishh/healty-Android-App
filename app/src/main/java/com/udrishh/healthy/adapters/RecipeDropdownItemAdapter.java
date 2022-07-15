package com.udrishh.healthy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeDropdownItemAdapter extends ArrayAdapter<Recipe> {
    Context context;
    ArrayList<Recipe> recipes;
    ArrayList<Recipe> filteredRecipes;

    public RecipeDropdownItemAdapter(@NonNull Context context, int resource, @NonNull List<Recipe> objects) {
        super(context, resource, objects);
        recipes = new ArrayList<>(objects);
    }

    private final Filter recipesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            filteredRecipes = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredRecipes.addAll(recipes);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Recipe recipe : recipes) {
                    if (recipe.getName().toLowerCase().contains(filterPattern)) {
                        filteredRecipes.add(recipe);
                    }
                }
            }
            results.values = filteredRecipes;
            results.count = filteredRecipes.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_dropdown_item, parent, false);
        }

        ImageView recipeImg = convertView.findViewById(R.id.recipe_item_image);
        TextView recipeName = convertView.findViewById(R.id.recipe_item_name);
        Recipe recipe = getItem(position);
        if (recipe != null) {
            recipeName.setText(recipe.getName());
            Picasso.get().load(recipe.getImg()).into(recipeImg);
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return recipesFilter;
    }
}
