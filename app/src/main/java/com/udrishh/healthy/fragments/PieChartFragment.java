package com.udrishh.healthy.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.PhysicalActivityRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PieChartFragment extends Fragment {
    private View view;
    private PieChart pieChart;
    private CalendarView calendarView;
    private ArrayList<FoodDrinkRecord> foodDrinkRecords;
    private ArrayList<PhysicalActivityRecord> physicalActivityRecords;
    private TextView noStatsText;
    private TextView highlightedDetails;
    private ArrayList<FoodDrinkRecord> showingFoodDrinkRecords;
    private ArrayList<PhysicalActivityRecord> showingPhysicalActivityRecords;
    private RadioGroup radioGroup;
    private boolean isEatenSelected;
    private String selectedDate;

    public PieChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        foodDrinkRecords = ((MainActivity) this.requireActivity()).getFoodDrinkRecords();
        physicalActivityRecords = ((MainActivity) this.requireActivity()).getPhysicalActivityRecords();
        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        Calendar calendar = Calendar.getInstance();
        String today = crateSelectedDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
        selectedDate = today;
        pieChart = view.findViewById(R.id.pie_chart);
        calendarView = view.findViewById(R.id.statistics_calendar);
        noStatsText = view.findViewById(R.id.statistics_no_data);
        highlightedDetails = view.findViewById(R.id.statistics_highlighted_details);
        highlightedDetails.setVisibility(View.GONE);
        radioGroup = view.findViewById(R.id.pie_chart_radio_group);
        isEatenSelected = true;
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            month++;
            String selectedDate = crateSelectedDateString(year, month, dayOfMonth);
            loadPieChartData(selectedDate);
        });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.pie_chart_radio_eaten) {
                isEatenSelected = true;
            } else {
                isEatenSelected = false;
            }
            loadPieChartData(selectedDate);
        });
        setupPieChart();
        loadPieChartData(today);
    }

    private String crateSelectedDateString(int year, int month, int dayOfMonth) {
        selectedDate = "";
        if (dayOfMonth < 10) {
            selectedDate += "0" + dayOfMonth + "/";
        } else {
            selectedDate += dayOfMonth + "/";
        }
        if (month < 10) {
            selectedDate += "0" + month + "/";
        } else {
            selectedDate += month + "/";
        }
        selectedDate += year;
        return selectedDate;
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);
        pieChart.setBackgroundColor(Color.WHITE);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(isEatenSelected){
                    highlightedDetails.setText(getString(R.string.statistics_highlighted_details_text,
                            showingFoodDrinkRecords.get((int) h.getX()).getName(),
                            showingFoodDrinkRecords.get((int) h.getX()).getTotalCalories()));
                } else{
                    highlightedDetails.setText(getString(R.string.statistics_highlighted_details_text,
                            showingPhysicalActivityRecords.get((int) h.getX()).getName(),
                            (int)showingPhysicalActivityRecords.get((int) h.getX()).getTotalCalories()));
                }
                highlightedDetails.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected() {
                highlightedDetails.setVisibility(View.GONE);
            }
        });
        //legend
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(false);
    }

    private void loadPieChartData(String selectedDate) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        int totalCalories = 0;
        if(isEatenSelected){
            for (FoodDrinkRecord record : foodDrinkRecords) {
                if (record.getDate().contains(selectedDate)) {
                    totalCalories += record.getTotalCalories();
                }
            }
            showingFoodDrinkRecords = new ArrayList<>();
            for (FoodDrinkRecord record : foodDrinkRecords) {
                if (record.getDate().contains(selectedDate)) {
                    float percent = (record.getTotalCalories() * 100) / (float) totalCalories;
                    entries.add(new PieEntry(percent, record.getName().split(" ")[0]));
                    showingFoodDrinkRecords.add(record);
                }
            }
        } else {
            for (PhysicalActivityRecord record : physicalActivityRecords) {
                if (record.getDate().contains(selectedDate)) {
                    totalCalories += record.getTotalCalories();
                }
            }
            showingPhysicalActivityRecords = new ArrayList<>();
            for (PhysicalActivityRecord record : physicalActivityRecords) {
                if (record.getDate().contains(selectedDate)) {
                    float percent = (record.getTotalCalories() * 100) / (float) totalCalories;
                    entries.add(new PieEntry(percent, record.getName().split(" ")[0]));
                    showingPhysicalActivityRecords.add(record);
                }
            }
        }
        if (entries.size() > 0) {
            //We can display the chart
            pieChart.setVisibility(View.VISIBLE);
            noStatsText.setVisibility(View.GONE);
            ArrayList<Integer> colors = new ArrayList<>();
            for (int color : ColorTemplate.MATERIAL_COLORS) {
                colors.add(color);
            }
//            for (int color : ColorTemplate.VORDIPLOM_COLORS) {
//                colors.add(color);
//            }

            PieDataSet dataSet = new PieDataSet(entries, "Statistici pentru ziua selectată");
            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            data.setDrawValues(true);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.BLACK);

            pieChart.setCenterText(selectedDate + "\n" + totalCalories + " kcal.");

            pieChart.setData(data);
            pieChart.invalidate();

            //animatie
            pieChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
        } else {
            pieChart.setVisibility(View.GONE);
            highlightedDetails.setVisibility(View.GONE);
            noStatsText.setVisibility(View.VISIBLE);
        }
    }
}