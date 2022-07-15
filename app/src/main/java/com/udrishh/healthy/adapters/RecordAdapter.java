package com.udrishh.healthy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.MeasurementRecord;
import com.udrishh.healthy.classes.PhysicalActivityRecord;
import com.udrishh.healthy.classes.RecipeRecord;
import com.udrishh.healthy.classes.Record;
import com.udrishh.healthy.enums.RecordType;

import java.util.ArrayList;

public class RecordAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Record> records;

    public RecordAdapter(Context context, ArrayList<Record> records) {
        this.context = context;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.record_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.record_item_name);
        TextView date = convertView.findViewById(R.id.record_item_date);
        ImageView icon = convertView.findViewById(R.id.record_item_icon);

        if (records.get(position) instanceof FoodDrinkRecord) {
            //FOOD OR DRINK RECORD
            FoodDrinkRecord foodDrinkRecord = (FoodDrinkRecord) records.get(position);
            if (foodDrinkRecord.getRecordType() == RecordType.DRINK) {
                icon.setImageResource(R.drawable.glass_icon);
            } else {
                icon.setImageResource(R.drawable.food_icon);
            }
            name.setText(context.getString(R.string.record_item_name, foodDrinkRecord.getName()));
            String[] dates = foodDrinkRecord.getDate().split(" - ");
            date.setText(context.getString(R.string.record_item_date, dates[0], dates[1]));
        } else if (records.get(position) instanceof PhysicalActivityRecord) {
            //PHYSICAL ACTIVITY RECORD
            PhysicalActivityRecord physicalActivityRecord = (PhysicalActivityRecord) records.get(position);
            icon.setImageResource(R.drawable.activity_icon);
            name.setText(context.getString(R.string.record_item_name, physicalActivityRecord.getName()));
            String[] dates = physicalActivityRecord.getDate().split(" - ");
            date.setText(context.getString(R.string.record_item_date, dates[0], dates[1]));
        } else if (records.get(position) instanceof MeasurementRecord) {
            //MEASUREMENT RECORD
            MeasurementRecord measurementRecord = (MeasurementRecord) records.get(position);
            icon.setImageResource(R.drawable.measure_icon);
            name.setText(context.getString(R.string.record_item_name, measurementRecord.getName()));
            String[] dates = measurementRecord.getDate().split(" - ");
            date.setText(context.getString(R.string.record_item_date, dates[0], dates[1]));
        } else if (records.get(position) instanceof RecipeRecord) {
            //RECIPE RECORD
            RecipeRecord recipeRecord = (RecipeRecord) records.get(position);
            icon.setImageResource(R.drawable.meal_icon);
            name.setText(context.getString(R.string.record_item_name, recipeRecord.getName()));
            String[] dates = recipeRecord.getDate().split(" - ");
            date.setText(context.getString(R.string.record_item_date, dates[0], dates[1]));
        }

        return convertView;
    }
}
