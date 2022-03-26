package com.udrishh.healthy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.Food;

import java.util.ArrayList;

public class FoodsAdapter extends BaseAdapter {
    Context context;
    ArrayList<Food> foods;

    public FoodsAdapter(Context context, ArrayList<Food> foods) {
        this.context = context;
        this.foods = foods;
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Object getItem(int position) {
        return foods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.food_drink_item, parent, false);
        }

        TextView foodName;
        TextView foodCalories;

        foodName = convertView.findViewById(R.id.food_drink_item_name);
        foodCalories = convertView.findViewById(R.id.food_drink_item_calories);

        return convertView;
    }
}
