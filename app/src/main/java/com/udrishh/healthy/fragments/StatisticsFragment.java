package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.adapters.RecordAdapter;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.RecipeRecord;
import com.udrishh.healthy.classes.Record;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.utilities.DateConverter;
import com.udrishh.healthy.utilities.RecordDateComparator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class StatisticsFragment extends Fragment {
    private View view;
    private TextView recordsTitle;
    private TextView streakText;
    private ListView recordsList;
    private ArrayList<Record> records;
    private MaterialButton viewAllRecordsBtn;
    private MaterialButton viewPieChartBtn;
    private MaterialButton viewLineChartBtn;
    private CardView historyCard;
    private User user;
    private String joinDate;

    public StatisticsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        getRecords();
        initialiseComponents();
        return view;
    }

    private void getRecords() {
        records = new ArrayList<>();
        records = (((MainActivity) this.requireActivity()).getRecords());
        user = (((MainActivity) this.requireActivity()).getUserObject());
        joinDate = (((MainActivity) this.requireActivity()).getJoinDate());
    }

    private void initialiseComponents() {
        historyCard = view.findViewById(R.id.history_card);
        streakText = view.findViewById(R.id.statistics_streak);
        recordsTitle = view.findViewById(R.id.statistics_record_card_title);
        recordsList = view.findViewById(R.id.statistics_record_list);
        viewAllRecordsBtn = view.findViewById(R.id.statistics_view_all_records);
        viewPieChartBtn = view.findViewById(R.id.statistics_view_pie_chart);
        viewLineChartBtn = view.findViewById(R.id.statistics_view_line_chart);

        streakText.setText(getString(R.string.statistics_streak, user.getName(), getStreak(), getTotalDays()));


        if (records.isEmpty()) {
            recordsTitle.setText(getString(R.string.statistics_no_records_text));
            historyCard.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 280));
        } else {
            Collections.sort(records, new RecordDateComparator());
            ArrayList<Record> lastRecords = new ArrayList<>();
            int nb = Math.min(records.size(), 5);
            if (nb == 1) {
                historyCard.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 450));
                recordsTitle.setText(getString(R.string.statistics_one_stats_text));
            } else {
                recordsTitle.setText(getString(R.string.statistics_stats_text, nb));
                if (nb == 2) {
                    historyCard.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 620));
                } else if (nb == 3) {
                    historyCard.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 790));
                } else if (nb == 4) {
                    historyCard.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 960));
                } else {
                    historyCard.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 1130));
                }
            }
            for (int i = 0; i < nb; i++) {
                lastRecords.add(records.get(i));
            }
            recordsList.setAdapter(new RecordAdapter(getContext(), lastRecords));
        }

        viewAllRecordsBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, new ViewHistoryFragment(records))
                    .addToBackStack(null)
                    .commit();
        });

        viewPieChartBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, new PieChartFragment())
                    .addToBackStack(null)
                    .commit();
        });

        viewLineChartBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, new LineChartFragment())
                    .addToBackStack(null)
                    .commit();
        });

        recordsList.setOnItemClickListener((parent, view, position, id) -> {
            Record selectedRecord = (Record) parent.getItemAtPosition(position);
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, new RecordDetailsFragment(selectedRecord))
                    .addToBackStack(null)
                    .commit();
        });
    }

    private int getTotalDays() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date1 = LocalDate.parse(joinDate, dtf);
        LocalDate date2 = LocalDate.parse(DateConverter.fromDate(new Date()), dtf);
        return (int) ChronoUnit.DAYS.between(date1, date2);
    }

    private int getStreak() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date1 = LocalDate.parse(joinDate, dtf);
        LocalDate date2 = LocalDate.parse(DateConverter.fromDate(new Date()), dtf);
        int streak = 0;
        for(LocalDate date = date1; date.isBefore(date2.plusDays(1)); date = date.plusDays(1)){
            int dateCalories = 0;
            for(Record record : records){
                LocalDate recordDate = LocalDate.parse(record.getDate().split(" ")[0], dtf);
                if (recordDate.isEqual(date)){
                    if(record instanceof FoodDrinkRecord){
                        dateCalories += ((FoodDrinkRecord)record).getTotalCalories();
                    } else if(record instanceof RecipeRecord){
                        dateCalories += ((RecipeRecord)record).getTotalCalories();
                    }
                }
            }
            if(dateCalories>= user.getCaloriesPlan()){
                streak++;
            }
        }
        return streak;
    }
}