package com.udrishh.healthy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodsAdapter extends ArrayAdapter<Food> {
    ArrayList<Food> foods;
    ArrayList<Food> filteredFoodsList;

    public FoodsAdapter(@NonNull Context context, int resource, @NonNull List<Food> objects) {
        super(context, resource, objects);
        foods = new ArrayList<>(objects);
    }

    private final Filter foodsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            filteredFoodsList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredFoodsList.addAll(foods);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Food food : foods) {
                    if (food.getName().toLowerCase().contains(filterPattern) &&
                            !food.getFoodId().contains("x")) {
                        filteredFoodsList.add(food);
                    }
                }
            }
            results.values = filteredFoodsList;
            results.count = filteredFoodsList.size();
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_drink_item, parent, false);
        }

        TextView foodName = convertView.findViewById(R.id.food_drink_item_name);
        TextView foodCalories = convertView.findViewById(R.id.food_drink_item_calories);
        ImageView starIcon = convertView.findViewById(R.id.food_drink_star_icon);

        Food food = getItem(position);
        if (food != null) {
            foodName.setText(food.getName());
            foodCalories.setText(food.getCalories() + "\nkcal/100g");
            if (food.getUserId().equals("admin")) {
                starIcon.setVisibility(View.INVISIBLE);
            } else {
                starIcon.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return foodsFilter;
    }


}
