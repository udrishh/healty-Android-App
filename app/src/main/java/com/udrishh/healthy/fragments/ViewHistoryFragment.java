package com.udrishh.healthy.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.chip.ChipGroup;
import com.udrishh.healthy.R;
import com.udrishh.healthy.adapters.RecordAdapter;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.MeasurementRecord;
import com.udrishh.healthy.classes.PhysicalActivityRecord;
import com.udrishh.healthy.classes.Recipe;
import com.udrishh.healthy.classes.RecipeRecord;
import com.udrishh.healthy.classes.Record;
import com.udrishh.healthy.enums.RecipeCategory;
import com.udrishh.healthy.enums.RecordPeriod;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.utilities.RecordDateComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ViewHistoryFragment extends Fragment {
    private View view;
    private ArrayList<Record> records;
    private ListView recordList;
    private RecordType recordTypeFlag = RecordType.ALL;
    private RecordPeriod recordPeriodFlag = RecordPeriod.ALL;
    private ChipGroup recordCategories;
    private ChipGroup recordPeriods;
    private ArrayList<Record> selectedRecords;
    private TextView noRecordsText;

    public ViewHistoryFragment() {
    }

    public ViewHistoryFragment(ArrayList<Record> records) {
        this.records = new ArrayList<>();
        this.records = records;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_history, container, false);
        selectedRecords = new ArrayList<>();
        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        recordList = view.findViewById(R.id.history_record_list);
        recordCategories = view.findViewById(R.id.history_record_categories);
        recordPeriods = view.findViewById(R.id.history_record_periods);
        noRecordsText = view.findViewById(R.id.records_nothing);

        showRecords(records);
        categoryChangeListener();
        //periodChangeListener();
        recordPeriods.setVisibility(View.GONE);
    }

//    @SuppressLint("NonConstantResourceId")
//    private void periodChangeListener() {
//        recordPeriods.setOnCheckedChangeListener((group, checkedId) -> {
//            switch (checkedId) {
//                case R.id.history_record_period_all:
//                    recordPeriodFlag = RecordPeriod.ALL;
//                    selectedRecords = getRecordsWithFlags(records);
//                    showRecords(selectedRecords);
//                    break;
//                case R.id.history_record_period_today:
//                    recordPeriodFlag = RecordPeriod.TODAY;
//                    selectedRecords = getRecordsWithFlags(records);
//                    showRecords(selectedRecords);
//                    break;
//                case R.id.history_record_period_yesterday:
//                    recordPeriodFlag = RecordPeriod.YESTARDAY;
//                    selectedRecords = getRecordsWithFlags(records);
//                    showRecords(selectedRecords);
//                    break;
//                case R.id.history_record_period_last_w:
//                    recordPeriodFlag = RecordPeriod.LAST_WEEK;
//                    selectedRecords = getRecordsWithFlags(records);
//                    showRecords(selectedRecords);
//                    break;
//                case R.id.history_record_period_last_2w:
//                    recordPeriodFlag = RecordPeriod.LAST_2WEEKS;
//                    selectedRecords = getRecordsWithFlags(records);
//                    showRecords(selectedRecords);
//                    break;
//                case R.id.history_record_period_last_m:
//                    recordPeriodFlag = RecordPeriod.LAST_MONTH;
//                    selectedRecords = getRecordsWithFlags(records);
//                    showRecords(selectedRecords);
//                    break;
//                case R.id.history_record_period_last_6m:
//                    recordPeriodFlag = RecordPeriod.LAST_6MONTHS;
//                    selectedRecords = getRecordsWithFlags(records);
//                    showRecords(selectedRecords);
//                    break;
//                case R.id.history_record_period_last_y:
//                    recordPeriodFlag = RecordPeriod.LAST_YEAR;
//                    selectedRecords = getRecordsWithFlags(records);
//                    showRecords(selectedRecords);
//                    break;
//                default:
//                    selectedRecords.clear();
//                    showRecords(selectedRecords);
//                    break;
//            }
//        });
//    }

    @SuppressLint("NonConstantResourceId")
    private void categoryChangeListener() {
        recordCategories.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.history_record_category_all:
//                    recordTypeFlag = RecordType.ALL;
                    showRecords(records);
                    break;
                case R.id.history_record_category_food:
                    recordTypeFlag = RecordType.FOOD;
                    selectedRecords = getRecordsWithFlags(records);
                    showRecords(selectedRecords);
                    break;
                case R.id.history_record_category_drink:
                    recordTypeFlag = RecordType.DRINK;
                    selectedRecords = getRecordsWithFlags(records);
                    showRecords(selectedRecords);
                    break;
                case R.id.history_record_category_recipe:
                    recordTypeFlag = RecordType.RECIPE;
                    selectedRecords = getRecordsWithFlags(records);
                    showRecords(selectedRecords);
                    break;
                case R.id.history_record_category_activity:
                    recordTypeFlag = RecordType.PHYSICAL_ACTIVITY;
                    selectedRecords = getRecordsWithFlags(records);
                    showRecords(selectedRecords);
                    break;
                case R.id.history_record_category_measurement:
                    recordTypeFlag = RecordType.MEASUREMENT;
                    selectedRecords = getRecordsWithFlags(records);
                    showRecords(selectedRecords);
                    break;
                default:
                    selectedRecords.clear();
                    showRecords(selectedRecords);
                    break;
            }
        });
    }

    private ArrayList<Record> getRecordsWithFlags(ArrayList<Record> records) {
        selectedRecords.clear();
        switch (recordTypeFlag) {
            case FOOD:
                for (Record record : records) {
                    if (record instanceof FoodDrinkRecord) {
                        if (((FoodDrinkRecord) record).getCategory() == RecordType.FOOD) {
                            selectedRecords.add(record);
                        }
                    }
                }
                return selectedRecords;
            case DRINK:
                for (Record record : records) {
                    if (record instanceof FoodDrinkRecord) {
                        if (((FoodDrinkRecord) record).getCategory() == RecordType.DRINK) {
                            selectedRecords.add(record);
                        }
                    }
                }
                return selectedRecords;
            case RECIPE:
                for (Record record : records) {
                    if (record instanceof RecipeRecord) {
                        selectedRecords.add(record);
                    }
                }
                return selectedRecords;
            case PHYSICAL_ACTIVITY:
                for (Record record : records) {
                    if (record instanceof PhysicalActivityRecord) {
                        selectedRecords.add(record);
                    }
                }
                return selectedRecords;
            case MEASUREMENT:
                for (Record record : records) {
                    if (record instanceof MeasurementRecord) {
                        selectedRecords.add(record);
                    }
                }
                return selectedRecords;
            default:
                return selectedRecords;
        }
    }

    private void showRecords(ArrayList<Record> selectedRecords) {
        if (selectedRecords.isEmpty()) {
            noRecordsText.setVisibility(View.VISIBLE);
            recordList.setVisibility(View.GONE);
        } else {
            noRecordsText.setVisibility(View.GONE);
            recordList.setVisibility(View.VISIBLE);
            Collections.sort(selectedRecords, new RecordDateComparator());
            recordList.setAdapter(new RecordAdapter(getContext(), selectedRecords));
        }
    }
}