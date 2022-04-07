package com.udrishh.healthy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.Drink;

import java.util.ArrayList;
import java.util.List;

public class DrinksAdapter extends ArrayAdapter<Drink> {
    Context context;
    ArrayList<Drink> drinks;
    ArrayList<Drink> filteredDrinksList;

    public DrinksAdapter(@NonNull Context context, int resource, @NonNull List<Drink> objects) {
        super(context, resource, objects);
        drinks = new ArrayList<>(objects);
    }

    private Filter drinksFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            filteredDrinksList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredDrinksList.addAll(drinks);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Drink drink : drinks) {
                    if (drink.getName().toLowerCase().contains(filterPattern)) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_drink_item, parent, false);
        }

        TextView drinkName = convertView.findViewById(R.id.food_drink_item_name);
        TextView drinkCalories = convertView.findViewById(R.id.food_drink_item_calories);

        Drink drink = getItem(position);
        if(drink!=null){
            drinkName.setText(drink.getName());
            drinkCalories.setText(String.valueOf(drink.getCalories())+"\nkcal/100g");
        }

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return drinksFilter;
    }


}
