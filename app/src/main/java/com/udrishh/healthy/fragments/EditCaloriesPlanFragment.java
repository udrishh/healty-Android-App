package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.ActivityLevel;
import com.udrishh.healthy.enums.GainLose;
import com.udrishh.healthy.utilities.Calculator;

import java.util.Calendar;
import java.util.Objects;

public class EditCaloriesPlanFragment extends Fragment {
    private View view;
    private User user;
    private MaterialButton saveBtn;
    private Spinner gainLoseInput;
    private Spinner activityLevelInput;
    private TextView planPreview;
    private CheckBox manuallyCheck;
    private TextInputEditText caloriesInput;
    private TextInputLayout caloriesInputLayout;
    private boolean isManualSelected = false;

    private int BMR;

    public EditCaloriesPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_calories_plan, container, false);
        user = ((MainActivity) this.requireActivity()).getUserObject();
        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        saveBtn = view.findViewById(R.id.edit_calories_plan_finish);
        activityLevelInput = view.findViewById(R.id.edit_calories_plan_activity_level);
        activityLevelInput.setAdapter(ArrayAdapter.createFromResource(getContext(), R.array.activity_levels, R.layout.support_simple_spinner_dropdown_item));
        gainLoseInput = view.findViewById(R.id.edit_calories_plan_gain_lose);
        gainLoseInput.setAdapter(ArrayAdapter.createFromResource(getContext(), R.array.gain_lose, R.layout.support_simple_spinner_dropdown_item));
        planPreview = view.findViewById(R.id.edit_calories_plan_calories_view);
        setPlanPreview();

        caloriesInputLayout = view.findViewById(R.id.edit_calories_plan_calories_input_layout);
        caloriesInput = view.findViewById(R.id.edit_calories_plan_calories_input);
        manuallyCheck = view.findViewById(R.id.edit_calories_plan_manual_check);

        saveBtn.setOnClickListener(v -> {
            boolean valid = true;
            if (isManualSelected) {
                valid = false;
                if (Objects.requireNonNull(caloriesInput.getText()).toString().trim().length() > 0) {
                    BMR = Integer.parseInt(caloriesInput.getText().toString().trim());
                    if (BMR < 5000 && BMR >= 1200) {
                        caloriesInput.setError(null);
                        valid = true;
                    } else {
                        caloriesInput.setError(getString(R.string.invalid_value_text));
                    }
                } else {
                    caloriesInput.setError(getString(R.string.invalid_value_text));
                }
            }

            if (valid) {
                user.setCaloriesPlan(BMR);
                caloriesInput.setError(null);
                ((MainActivity) requireActivity()).editUserCaloriesPlan(user);

                BottomNavigationView bottomNavigationView =
                        ((MainActivity) requireActivity()).getBottomNavigation();
                bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
            }
        });

        manuallyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                caloriesInput.setEnabled(true);
                caloriesInputLayout.setEnabled(true);
                gainLoseInput.setEnabled(false);
                activityLevelInput.setEnabled(false);
                isManualSelected = true;
            } else {
                caloriesInput.setEnabled(false);
                caloriesInputLayout.setEnabled(false);
                gainLoseInput.setEnabled(true);
                activityLevelInput.setEnabled(true);
                isManualSelected = false;
            }
        });

        activityLevelInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isManualSelected) {
                    setPlanPreview();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        gainLoseInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isManualSelected) {
                    setPlanPreview();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setPlanPreview() {
        BMR = Calculator.BMR(user.getWeight(), user.getHeight(), getAge(user.getBirthdate()),
                user.getSex(), getSelectedActivityLevel(), getSelectedGainLose());
        planPreview.setText(getString(R.string.signup_plan_calories_text, BMR));
    }

    private int getAge(String birthdate) {
        return Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(birthdate.split("/")[2]);
    }

    private ActivityLevel getSelectedActivityLevel() {
        switch (activityLevelInput.getSelectedItemPosition()) {
            case 0:
                return ActivityLevel.NONE;
            case 1:
                return ActivityLevel.LOW;
            case 2:
                return ActivityLevel.MEDIUM;
            case 3:
                return ActivityLevel.HIGH;
            case 4:
                return ActivityLevel.EXTREME;
        }
        return ActivityLevel.NONE;
    }

    private GainLose getSelectedGainLose() {
        switch (gainLoseInput.getSelectedItemPosition()) {
            case 0:
                return GainLose.NONE;
            case 1:
                return GainLose.LOSE1KG;
            case 2:
                return GainLose.LOSE075KG;
            case 3:
                return GainLose.LOSE050KG;
            case 4:
                return GainLose.LOSE025KG;
            case 5:
                return GainLose.GAIN025KG;
            case 6:
                return GainLose.GAIN050KG;
            case 7:
                return GainLose.GAIN075KG;
            case 8:
                return GainLose.GAIN1KG;
        }
        return GainLose.NONE;
    }
}