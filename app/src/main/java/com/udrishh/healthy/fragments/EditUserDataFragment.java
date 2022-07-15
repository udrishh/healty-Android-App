package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.Sex;
import com.udrishh.healthy.utilities.DateConverter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class EditUserDataFragment extends Fragment {
    private User user;
    private View view;

    private MaterialButton saveBtn;
    private TextInputEditText nameInput;
    private RadioGroup radioGroup;
    private DatePicker birthdateInput;

    public EditUserDataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_user_data, container, false);
        importObjects();
        initialiseComponents();
        addClickListeners();
        return view;
    }

    private void importObjects() {
        user = ((MainActivity) this.requireActivity()).getUserObject();
    }

    private void addClickListeners() {
        saveBtn.setOnClickListener(v -> {
            boolean valid = false;
            if (nameInput.getText() != null) {
                if (nameInput.getText().toString().trim().length() > 2) {
                    valid = true;
                }
            }
            if (valid) {
                editUserData();
                moveToProfileFragment();
            } else {
                nameInput.setError(getText(R.string.invalid_username_text));
            }
        });
    }

    private void moveToProfileFragment() {
        BottomNavigationView bottomNavigationView =
                ((MainActivity) requireActivity()).getBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
    }

    private void initialiseComponents() {
        saveBtn = view.findViewById(R.id.edit_user_data_finish);
        nameInput = view.findViewById(R.id.edit_user_data_name_input);
        radioGroup = view.findViewById(R.id.edit_user_data_sex_radio_group);
        birthdateInput = view.findViewById(R.id.edit_user_data_birthdate_picker);

        nameInput.setText(user.getName());
        if (user.getSex() == Sex.MALE) {
            radioGroup.check(radioGroup.getChildAt(0).getId());
        } else {
            radioGroup.check(radioGroup.getChildAt(1).getId());
        }
        initialiseDatePicker();
    }

    private void editUserData() {
        nameInput.setError(null);
        user.setName(Objects.requireNonNull(nameInput.getText()).toString().trim());
        if (radioGroup.getCheckedRadioButtonId() == R.id.edit_user_data_sex_m) {
            user.setSex(Sex.MALE);
        } else {
            user.setSex(Sex.FEMALE);
        }
        String selectedDate = birthdateInput.getDayOfMonth() + "/"
                + (birthdateInput.getMonth() + 1) + "/" + birthdateInput.getYear();
        user.setBirthdate(selectedDate);
        ((MainActivity) requireActivity()).editUserData(user);
    }

    private void initialiseDatePicker() {
        Date date = DateConverter.fromString(user.getBirthdate());
        if (date != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            birthdateInput.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            Calendar test = new GregorianCalendar();
            //ms in a month * 12 months in a year * nb of years
            birthdateInput.setMaxDate(test.getTimeInMillis() - 26298L * 100000 * 12 * 10);
            birthdateInput.setMinDate(test.getTimeInMillis() - 26298L * 100000 * 12 * 100);
        }
    }
}