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
import com.udrishh.healthy.classes.Drink;

import java.util.ArrayList;
import java.util.List;

public class DrinksAdapter extends ArrayAdapter<Drink> {
    ArrayList<Drink> drinks;
    ArrayList<Drink> filteredDrinksList;

    public DrinksAdapter(@NonNull Context context, int resource, @NonNull List<Drink> objects) {
        super(context, resource, objects);
        drinks = new ArrayList<>(objects);
    }

    private final Filter drinksFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            filteredDrinksList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredDrinksList.addAll(drinks);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Drink drink : drinks) {
                    if (drink.getName().toLowerCase().contains(filterPattern) &&
                            !drink.getDrinkId().contains("x")) {
                        filteredDrinksList.add(drink);
                    }
                }
            }
            results.values = filteredDrinksList;
            results.count = filteredDrinksList.size();
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

        TextView drinkName = convertView.findViewById(R.id.food_drink_item_name);
        TextView drinkCalories = convertView.findViewById(R.id.food_drink_item_calories);
        ImageView starIcon = convertView.findViewById(R.id.food_drink_star_icon);

        Drink drink = getItem(position);
        if (drink != null) {
            drinkName.setText(drink.getName());
            drinkCalories.setText(drink.getCalories() + "\nkcal/100g");
            if (drink.getUserId().equals("admin")) {
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
        return drinksFilter;
    }


}
