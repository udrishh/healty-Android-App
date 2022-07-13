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
import com.udrishh.healthy.classes.PhysicalActivity;
import com.udrishh.healthy.classes.PhysicalActivityRecord;
import com.udrishh.healthy.classes.Recipe;
import com.udrishh.healthy.classes.RecipeRecord;
import com.udrishh.healthy.classes.Record;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.utilities.Finder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PieChartFragment extends Fragment {
    private View view;
    private PieChart pieChart;
    private CalendarView calendarView;
    private ArrayList<Record> eatenRecords;
    private ArrayList<PhysicalActivityRecord> physicalActivityRecords;
    private ArrayList<PhysicalActivity> physicalActivities;
    private TextView noStatsText;
    private TextView highlightedDetails;
    private ArrayList<Record> showingEatenRecords;
    private ArrayList<PhysicalActivityRecord> showingPhysicalActivityRecords;
    private RadioGroup radioGroup;
    private boolean isEatenSelected;
    private String selectedDate;

    private User user;

    public PieChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        eatenRecords = new ArrayList<>();
        eatenRecords.addAll(((MainActivity) this.requireActivity()).getFoodDrinkRecords());
        eatenRecords.addAll(((MainActivity) this.requireActivity()).getRecipeRecords());

        Log.d("mytagg",String.valueOf(eatenRecords.size()));
        physicalActivityRecords = ((MainActivity) this.requireActivity()).getPhysicalActivityRecords();
        physicalActivities = ((MainActivity) this.requireActivity()).getPhysicalActivities();

        user = ((MainActivity) this.requireActivity()).getUserObject();
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
                if (isEatenSelected) {
                    if (showingEatenRecords.get((int) h.getX()) instanceof RecipeRecord) {
                        highlightedDetails.setText(getString(R.string.statistics_highlighted_details_text,
                                ((RecipeRecord) showingEatenRecords.get((int) h.getX())).getName(),
                                ((RecipeRecord) showingEatenRecords.get((int) h.getX())).getTotalCalories()));
                    } else {
                        highlightedDetails.setText(getString(R.string.statistics_highlighted_details_text,
                                ((FoodDrinkRecord) showingEatenRecords.get((int) h.getX())).getName(),
                                ((FoodDrinkRecord) showingEatenRecords.get((int) h.getX())).getTotalCalories()));
                    }

                } else {
                    PhysicalActivity physicalActivity =
                            Finder.physicalActivity(physicalActivities, showingPhysicalActivityRecords.get((int) h.getX()).getItemId());
                    assert physicalActivity != null;
                    int recordCalories = Math.round((float)showingPhysicalActivityRecords.get((int) h.getX()).getQuantity() / 60
                            * physicalActivity.getCalories() * user.getWeight());
                    highlightedDetails.setText(getString(R.string.statistics_highlighted_details_text,
                            showingPhysicalActivityRecords.get((int) h.getX()).getName(),
                            recordCalories));
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
        if (isEatenSelected) {
            for (Record record : eatenRecords) {
                if (record.getDate().contains(selectedDate)) {
                    if (record instanceof RecipeRecord) {
                        totalCalories += ((RecipeRecord) record).getTotalCalories();
                    } else {
                        totalCalories += ((FoodDrinkRecord) record).getTotalCalories();
                    }
                }
            }
            showingEatenRecords = new ArrayList<>();
            for (Record record : eatenRecords) {
                if (record.getDate().contains(selectedDate)) {
                    if (record instanceof RecipeRecord) {
                        float percent = (((RecipeRecord)record).getTotalCalories() * 100) / (float) totalCalories;
                        if (percent >= 1) {
                            entries.add(new PieEntry(percent, ((RecipeRecord)record).getName().split(" ")[0]));
                            showingEatenRecords.add(record);
                        }
                    } else {
                        float percent = (((FoodDrinkRecord)record).getTotalCalories() * 100) / (float) totalCalories;
                        if (percent >= 1) {
                            entries.add(new PieEntry(percent, ((FoodDrinkRecord)record).getName().split(" ")[0]));
                            showingEatenRecords.add(record);
                        }
                    }
                }
            }
        } else {
            for (PhysicalActivityRecord physicalActivityRecord : physicalActivityRecords) {
                if (physicalActivityRecord.getDate().contains(selectedDate)) {
                    PhysicalActivity physicalActivity =
                            Finder.physicalActivity(physicalActivities, physicalActivityRecord.getItemId());
                    assert physicalActivity != null;
                    int recordCalories = Math.round((float)physicalActivityRecord.getQuantity() / 60
                            * physicalActivity.getCalories() * user.getWeight());
                    totalCalories += recordCalories;
                }
            }
            showingPhysicalActivityRecords = new ArrayList<>();
            for (PhysicalActivityRecord physicalActivityRecord : physicalActivityRecords) {
                if (physicalActivityRecord.getDate().contains(selectedDate)) {
                    PhysicalActivity physicalActivity =
                            Finder.physicalActivity(physicalActivities, physicalActivityRecord.getItemId());
                    assert physicalActivity != null;
                    int recordCalories = Math.round((float)physicalActivityRecord.getQuantity() / 60
                            * physicalActivity.getCalories() * user.getWeight());
                    float percent = (recordCalories * 100) / (float) totalCalories;
                    if (percent >= 1) {
                        entries.add(new PieEntry(percent, physicalActivityRecord.getName().split(" ")[0]));
                        showingPhysicalActivityRecords.add(physicalActivityRecord);
                    }
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
            for (int color : ColorTemplate.VORDIPLOM_COLORS) {
                colors.add(color);
            }

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