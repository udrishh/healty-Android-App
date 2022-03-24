package com.udrishh.healthy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.Sex;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private View view;
    private TextView greetingText;
    private TextView nameText;
    private TextView ageText;
    private TextView sexText;
    private TextView heightText;
    private TextView weightText;
    private TextView planText;
    private ImageView sexIcon;

    private TextView caloriesProgressText;
    private TextView liquidsProgressText;
    private TextView burnedText;
    private TextView eatenText;
    private TextView proteinsText;
    private TextView lipidsText;
    private TextView carbsText;
    private TextView fibersText;
    private User user;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        user = ((MainActivity) this.requireActivity()).getUserObject();

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        if(user!=null){
            initialiseProfileCardComponents();
            initialiseProgressCardComponents();
        }

        return view;
    }

    private void initialiseProgressCardComponents() {
        caloriesProgressText = view.findViewById(R.id.progress_card_tv_calories_progress_number);
        caloriesProgressText.setText(getString(R.string.calories_progress_counter, 0, user.getCaloriesPlan()));
        liquidsProgressText = view.findViewById(R.id.progress_card_tv_liquids_progress_number);
        liquidsProgressText.setText(getString(R.string.liquids_progress_counter, 0, 2000));
        burnedText = view.findViewById(R.id.progress_card_tv_calories_burned_number);
        burnedText.setText(getString(R.string.calories_burned_counter, 0));
        eatenText = view.findViewById(R.id.progress_card_tv_calories_eaten_number);
        eatenText.setText(getString(R.string.calories_eaten_counter, 0));
        proteinsText = view.findViewById(R.id.progress_card_proteins_counter);
        proteinsText.setText(getString(R.string.proteins_counter, 0));
        lipidsText = view.findViewById(R.id.progress_card_lipids_counter);
        lipidsText.setText(getString(R.string.lipids_counter, 0));
        carbsText = view.findViewById(R.id.progress_card_carbs_counter);
        carbsText.setText(getString(R.string.carbs_counter, 0));
        fibersText = view.findViewById(R.id.progress_card_fibers_counter);
        fibersText.setText(getString(R.string.fibers_counter, 0));
    }

    private void initialiseProfileCardComponents() {
        greetingText = view.findViewById(R.id.user_greeting_textview);
        greetingText.setText(getString(R.string.user_greeting_text, "Buna seara", user.getName()));
        nameText = view.findViewById(R.id.user_name_textview);
        nameText.setText(getString(R.string.user_profile_name, user.getName()));
        ageText = view.findViewById(R.id.user_age_textview);
        ageText.setText(getString(R.string.user_profile_age,
                Calendar.getInstance().get(Calendar.YEAR)
                        - Integer.parseInt(user.getBirthdate().split("/")[2])));
        sexText = view.findViewById(R.id.user_sex_textview);
        sexIcon = view.findViewById(R.id.user_sex_icon);
        if (user.getSex() == Sex.MALE) {
            sexText.setText(getString(R.string.user_profile_sex, getString(R.string.signup_user_data_sex_m_text)));
            sexIcon.setImageResource(R.drawable.male_gender_icon);
        } else {
            sexText.setText(getString(R.string.user_profile_sex, getString(R.string.signup_user_data_sex_f_text)));
            sexIcon.setImageResource(R.drawable.female_gender_icon);
        }
        heightText = view.findViewById(R.id.user_height_textview);
        heightText.setText(getString(R.string.user_profile_height, user.getHeight()));
        weightText = view.findViewById(R.id.user_weight_textview);
        weightText.setText(getString(R.string.user_profile_weight, user.getWeight()));
        planText = view.findViewById(R.id.user_calories_textview);
        planText.setText(getString(R.string.user_profile_calories, user.getCaloriesPlan()));
    }
}