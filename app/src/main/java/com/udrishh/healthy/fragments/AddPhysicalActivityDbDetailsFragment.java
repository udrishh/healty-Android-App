package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.Food;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.PhysicalActivity;
import com.udrishh.healthy.classes.PhysicalActivityRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.utilities.DateConverter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class AddPhysicalActivityDbDetailsFragment extends Fragment {
    private View view;
    private PhysicalActivity selectedActivity;
    private MaterialButton addBtn;
    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;

    private TextView burnedCaloriesViewer;

    private User user;

    public AddPhysicalActivityDbDetailsFragment() {
    }

    public AddPhysicalActivityDbDetailsFragment(PhysicalActivity physicalActivity) {
        selectedActivity = physicalActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_physical_activity_db_details, container, false);
        user = ((MainActivity) this.requireActivity()).getUserObject();

        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        addBtn = view.findViewById(R.id.add_activity_finish);
        nameInput = view.findViewById(R.id.activity_name_input);
        quantityInput = view.findViewById(R.id.activity_quantity_input);
        burnedCaloriesViewer = view.findViewById(R.id.add_physical_activity_total_view);
        burnedCaloriesViewer.setText(getString(R.string.add_physical_activity_total_view,Math.round(selectedActivity.getCalories())));
        quantityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //UPDATE TOTAL VIEWERS
                if (quantityInput.getText().toString().trim().length() > 0) {
                   burnedCaloriesViewer.setText(getString(R.string.add_physical_activity_total_view,
                           Math.round(Float.parseFloat(quantityInput.getText().toString().trim()) / 60
                                   * selectedActivity.getCalories() * user.getWeight())));
                } else {
                    burnedCaloriesViewer.setText(getString(R.string.add_physical_activity_total_view,0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        burnedCaloriesViewer = view.findViewById(R.id.add_physical_activity_total_view);

        nameInput.setText(selectedActivity.getName());
        quantityInput.setText("1");

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                if (quantityInput.getText().toString().trim().length() <= 0) {
                    quantityInput.setError(getString(R.string.invalid_quantity_text));
                    isValid = false;
                }
                if (quantityInput.getText().toString().trim().length() > 0) {
                    if (Integer.parseInt(quantityInput.getText().toString().trim()) <= 0) {
                        quantityInput.setError(getString(R.string.invalid_quantity_text));
                        isValid = false;
                    }
                }
                if (nameInput.getText().toString().trim().length() <= 1) {
                    nameInput.setError(getString(R.string.invalid_name_text));
                }
                if (!isValid) {
                    return;
                }

                quantityInput.setError(null);
                nameInput.setError(null);

                PhysicalActivityRecord physicalActivityRecord = new PhysicalActivityRecord();
                physicalActivityRecord.setRecordId(UUID.randomUUID().toString());
                physicalActivityRecord.setDate(DateConverter.fromLongDate(new Date()));
                physicalActivityRecord.setUserId(user.getUserId());
                physicalActivityRecord.setItemId(selectedActivity.getPhysicalActivityId());
                physicalActivityRecord.setName(nameInput.getText().toString().trim());
                physicalActivityRecord.setCalories(selectedActivity.getCalories());
                physicalActivityRecord.setDuration(Integer.parseInt(quantityInput.getText().toString().trim()));
                physicalActivityRecord.setTotalCalories(Math.round(Float.parseFloat(quantityInput.getText().toString().trim()) / 60
                        * selectedActivity.getCalories() * user.getWeight()));

                ((MainActivity) requireActivity()).addPhysicalActivityRecord(physicalActivityRecord);

                BottomNavigationView bottomNavigationView =
                        ((MainActivity) requireActivity()).getBottomNavigation();
                bottomNavigationView.setSelectedItemId(R.id.menu_item_profile);
            }
        });
    }
}