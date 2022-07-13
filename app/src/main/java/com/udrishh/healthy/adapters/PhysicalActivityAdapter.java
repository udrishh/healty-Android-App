package com.udrishh.healthy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.PhysicalActivity;

import java.util.ArrayList;
import java.util.List;

public class PhysicalActivityAdapter extends ArrayAdapter<PhysicalActivity> {
    ArrayList<PhysicalActivity> physicalActivities;
    ArrayList<PhysicalActivity> filteredPhysicalActivities;

    public PhysicalActivityAdapter(@NonNull Context context, int resource, @NonNull List<PhysicalActivity> objects) {
        super(context, resource, objects);
        physicalActivities = new ArrayList<>(objects);
    }

    private Filter physicalActivitiesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            filteredPhysicalActivities = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredPhysicalActivities.addAll(physicalActivities);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PhysicalActivity physicalActivity : physicalActivities) {
                    if (physicalActivity.getName().toLowerCase().contains(filterPattern)) {
                        filteredPhysicalActivities.add(physicalActivity);
                    }
                }
            }

            results.values = filteredPhysicalActivities;
            results.count = filteredPhysicalActivities.size();

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.physical_activity_item, parent, false);
        }

        TextView physicalActivityName = convertView.findViewById(R.id.physical_activity_item_name);
        ImageView starIcon = convertView.findViewById(R.id.physical_activity_star_icon);

        PhysicalActivity physicalActivity = getItem(position);
        if(physicalActivity!=null){
            physicalActivityName.setText(physicalActivity.getName());
            if(physicalActivity.getUserId().equals("admin")){
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
        return physicalActivitiesFilter;
    }


}
