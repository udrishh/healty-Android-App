package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.udrishh.healthy.classes.Drink;
import com.udrishh.healthy.classes.Food;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.Recipe;
import com.udrishh.healthy.classes.RecipeRecord;
import com.udrishh.healthy.classes.Record;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.utilities.DateConverter;
import com.udrishh.healthy.utilities.Finder;
import com.udrishh.healthy.utilities.RecordDateComparator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public class StatisticsFragment extends Fragment {
    private View view;
    private TextView recordsTitle;
    private ListView recordsList;
    private ArrayList<Record> records;
    private MaterialButton viewAllRecordsBtn;
    private MaterialButton viewPieChartBtn;
    private MaterialButton viewLineChartBtn;
    private CardView historyCard;
    private User user;
    private String joinDate;
    private ArrayList<Recipe> recipes;
    private ArrayList<Food> foods;
    private ArrayList<Drink> drinks;
    private FragmentManager fragmentManager;

    public StatisticsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        importObjects();
        initialiseComponents();
        setClickListeners();
        return view;
    }

    private void setClickListeners() {
        viewAllRecordsBtn.setOnClickListener(v -> moveToViewHistoryFragment());

        viewPieChartBtn.setOnClickListener(v -> moveToPieChartFragment());

        viewLineChartBtn.setOnClickListener(v -> moveToLineChartFragment());

        recordsList.setOnItemClickListener((parent, view, position, id) -> {
            Record selectedRecord = (Record) parent.getItemAtPosition(position);
            moveToRecordDetailsFragment(selectedRecord);
        });
    }

    private void moveToRecordDetailsFragment(Record selectedRecord) {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new RecordDetailsFragment(selectedRecord))
                .addToBackStack(null)
                .commit();
    }

    private void moveToLineChartFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new LineChartFragment())
                .addToBackStack(null)
                .commit();
    }

    private void moveToPieChartFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new PieChartFragment())
                .addToBackStack(null)
                .commit();
    }

    private void moveToViewHistoryFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new ViewHistoryFragment(records))
                .addToBackStack(null)
                .commit();
    }

    private void importObjects() {
        records = new ArrayList<>();
        records = (((MainActivity) this.requireActivity()).getRecords());
        user = (((MainActivity) this.requireActivity()).getUserObject());
        joinDate = (((MainActivity) this.requireActivity()).getJoinDate());
        recipes = ((MainActivity) this.requireActivity()).getRecipes();
        foods = ((MainActivity) this.requireActivity()).getFoods();
        drinks = ((MainActivity) this.requireActivity()).getDrinks();
    }

    private void initialiseComponents() {
        historyCard = view.findViewById(R.id.history_card);
        TextView streakText = view.findViewById(R.id.statistics_streak);
        recordsTitle = view.findViewById(R.id.statistics_record_card_title);
        recordsList = view.findViewById(R.id.statistics_record_list);
        viewAllRecordsBtn = view.findViewById(R.id.statistics_view_all_records);
        viewPieChartBtn = view.findViewById(R.id.statistics_view_pie_chart);
        viewLineChartBtn = view.findViewById(R.id.statistics_view_line_chart);
        streakText.setText(getString(R.string.statistics_streak, user.getName(), getStreak(), getTotalDays()));
        initialiseListView();
    }

    private void initialiseListView() {
        if (records.isEmpty()) {
            recordsTitle.setText(getString(R.string.statistics_no_records_text));
            historyCard.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 280));
        } else {
            records.sort(new RecordDateComparator());
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
    }

    private int getTotalDays() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date1 = LocalDate.parse(joinDate, dtf);
        LocalDate date2 = LocalDate.parse(DateConverter.fromDate(new Date()), dtf);
        return (int) ChronoUnit.DAYS.between(date1, date2);
    }

    private int getStreak() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (Objects.equals(joinDate, "")) {
            joinDate = DateConverter.fromDate(new Date());
        }
        LocalDate date1 = LocalDate.parse(joinDate, dtf);
        LocalDate date2 = LocalDate.parse(DateConverter.fromDate(new Date()), dtf);
        int streak = 0;
        for (LocalDate date = date1; date.isBefore(date2.plusDays(1)); date = date.plusDays(1)) {
            int dateCalories = 0;
            for (Record record : records) {
                LocalDate recordDate = LocalDate.parse(record.getDate().split(" ")[0], dtf);
                if (recordDate.isEqual(date)) {
                    if (record instanceof FoodDrinkRecord) {
                        if (((FoodDrinkRecord) record).getRecordType() == RecordType.FOOD) {
                            Food food = Finder.food(foods, ((FoodDrinkRecord) record).getItemId());
                            if (food != null) {
                                dateCalories += food.getCalories() * ((FoodDrinkRecord) record).getQuantity();
                            }
                        } else {
                            Drink drink = Finder.drink(drinks, ((FoodDrinkRecord) record).getItemId());
                            if (drink != null) {
                                dateCalories += drink.getCalories() * ((FoodDrinkRecord) record).getQuantity();
                            }
                        }
                    } else if (record instanceof RecipeRecord) {
                        Recipe recipe = Finder.recipe(recipes, ((RecipeRecord) record).getItemId());
                        int calories = 0;
                        if (recipe != null) {
                            calories += recipe.getCalories() * 100 / ((RecipeRecord) record).getQuantity();
                        }
                        dateCalories += calories;
                    }
                }
            }
            if (dateCalories >= user.getCaloriesPlan()) {
                streak++;
            }
        }
        return streak;
    }
}