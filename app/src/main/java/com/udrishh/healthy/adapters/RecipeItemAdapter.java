package com.udrishh.healthy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.Recipe;
import com.udrishh.healthy.enums.RecipeCategory;

import java.util.ArrayList;

public class RecipeItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Recipe> recipes;

    public RecipeItemAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.recipe_title);
        TextView calories = convertView.findViewById(R.id.recipe_calories);
        TextView flags = convertView.findViewById(R.id.recipe_flags);
        ImageView image = convertView.findViewById(R.id.recipe_image);

        Picasso.get().load(recipes.get(position).getImg()).into(image);
        name.setText(context.getString(R.string.recipe_title, recipes.get(position).getName()));
        calories.setText(context.getString(R.string.recipe_calories,
                recipes.get(position).getCalories() * 100 / recipes.get(position).getQuantity()));
        if (recipes.get(position).isInCategory(RecipeCategory.LOW_CALORIES) && recipes.get(position).isInCategory(RecipeCategory.GLUTEN_FREE)) {
            flags.setText(context.getString(R.string.recipe_flags, "Calorii reduse", "Fără gluten"));
        } else if (recipes.get(position).isInCategory(RecipeCategory.LOW_CALORIES) && !recipes.get(position).isInCategory(RecipeCategory.GLUTEN_FREE)) {
            flags.setText(context.getString(R.string.recipe_flags, "Calorii reduse", ""));
        } else if (!recipes.get(position).isInCategory(RecipeCategory.LOW_CALORIES) && recipes.get(position).isInCategory(RecipeCategory.GLUTEN_FREE)) {
            flags.setText(context.getString(R.string.recipe_flags, "", "Fără gluten"));
        } else {
            flags.setText(context.getString(R.string.recipe_flags, "", ""));
        }

        return convertView;
    }
}
