package com.udrishh.healthy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.MeasurementRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.ActivityLevel;
import com.udrishh.healthy.enums.GainLose;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.enums.Sex;
import com.udrishh.healthy.utilities.Calculator;
import com.udrishh.healthy.utilities.DateConverter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

    private MaterialButton continueBtn1;
    private MaterialButton continueBtn2;
    private MaterialButton finishBtn;
    private ProgressBar loading;
    private MaterialCardView firstCard;
    private MaterialCardView secondCard;
    private MaterialCardView thirdCard;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText nameInput;
    private TextInputEditText heightInput;
    private TextInputEditText weightInput;
    private DatePicker birthdateInput;
    private RadioGroup sexInput;
    private Spinner activityLevelInput;
    private Spinner gainLoseInput;
    private TextView planPreview;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private User newUserObject;
    private boolean isReady = false;

    //Firestore Connection
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");
    private final CollectionReference measurementRecordsReference = db.collection("MeasurementRecords");

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

    @SuppressLint("NonConstantResourceId")
    private Sex getSelectedSex() {
        switch (sexInput.getCheckedRadioButtonId()) {
            case R.id.signup_user_data_sex_m:
                return Sex.MALE;
            case R.id.signup_user_data_sex_f:
                return Sex.FEMALE;
        }
        return Sex.MALE;
    }

    private void setPlanPreview() {
        planPreview.setText(getString(R.string.signup_plan_calories_text,
                Calculator.BMR(Integer.parseInt(Objects.requireNonNull(weightInput.getText()).toString().trim()),
                        Integer.parseInt(Objects.requireNonNull(heightInput.getText()).toString().trim()),
                        getAge(getSelectedDate()),
                        getSelectedSex(), getSelectedActivityLevel(), getSelectedGainLose())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        newUserObject = new User();
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> currentUser = firebaseAuth.getCurrentUser();
        initialiseComponents();
        setClickListeners();
        setItemSelectedListeners();
    }

    private void setItemSelectedListeners() {
        activityLevelInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isReady) {
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
                if (isReady) {
                    setPlanPreview();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setClickListeners() {
        continueBtn1.setOnClickListener(v -> {
            boolean isValid = true;
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (TextUtils.isEmpty(Objects.requireNonNull(emailInput.getText()).toString().trim())
                    || !emailInput.getText().toString().trim().matches(emailPattern)) {
                emailInput.setError(getString(R.string.invalid_email_text));
                isValid = false;
            }
            if (TextUtils.isEmpty(Objects.requireNonNull(passwordInput.getText()).toString().trim())
                    || passwordInput.getText().toString().trim().length() < 6) {
                passwordInput.setError(getString(R.string.invalid_password_signup_text));
                isValid = false;
            }

            if (!isValid) {
                return;
            }
            passwordInput.setError(null);
            emailInput.setError(null);
            firstCard.setVisibility(View.INVISIBLE);
            secondCard.setVisibility(View.VISIBLE);
        });

        continueBtn2.setOnClickListener(v -> {
            boolean isValid = true;
            if (TextUtils.isEmpty(Objects.requireNonNull(nameInput.getText()).toString().trim())) {
                nameInput.setError(getString(R.string.invalid_username_text));
                isValid = false;
            } else if (nameInput.getText().toString().trim().length() < 2) {
                nameInput.setError(getString(R.string.invalid_username_text));
                isValid = false;
            }
            if (TextUtils.isEmpty(Objects.requireNonNull(heightInput.getText()).toString().trim())) {
                heightInput.setError(getString(R.string.invalid_height_text));
                isValid = false;
            }
            if (!heightInput.getText().toString().trim().equals("")) {
                if (Integer.parseInt(heightInput.getText().toString().trim()) < 50
                        || Integer.parseInt(heightInput.getText().toString().trim()) > 230) {
                    heightInput.setError(getString(R.string.invalid_height_text));
                    isValid = false;
                }
            }
            if (TextUtils.isEmpty(Objects.requireNonNull(weightInput.getText()).toString().trim())) {
                weightInput.setError(getString(R.string.invalid_weight_text));
                isValid = false;
            }
            if (!weightInput.getText().toString().trim().equals("")) {
                if (Integer.parseInt(weightInput.getText().toString().trim()) < 35
                        || Integer.parseInt(weightInput.getText().toString().trim()) > 200) {
                    weightInput.setError(getString(R.string.invalid_weight_text));
                    isValid = false;
                }
            }
            if (!isValid) {
                return;
            }
            nameInput.setError(null);
            heightInput.setError(null);
            weightInput.setError(null);

            setPlanPreview();
            isReady = true;
            activityLevelInput.setSelection(0);
            secondCard.setVisibility(View.INVISIBLE);
            thirdCard.setVisibility(View.VISIBLE);
        });

        finishBtn.setOnClickListener(v -> {
            newUserObject.setName(Objects.requireNonNull(nameInput.getText()).toString().trim());
            newUserObject.setHeight(Integer.parseInt(Objects.requireNonNull(heightInput.getText()).toString().trim()));
            newUserObject.setWeight(Integer.parseInt(Objects.requireNonNull(weightInput.getText()).toString().trim()));
            newUserObject.setBirthdate(getSelectedDate());
            newUserObject.setSex(getSelectedSex());
            ActivityLevel activityLevel = getSelectedActivityLevel();
            GainLose gainLose = getSelectedGainLose();
            int userAge = getAge(newUserObject.getBirthdate());
            newUserObject.setCaloriesPlan(Calculator.BMR(newUserObject.getWeight(),
                    newUserObject.getHeight(), userAge, newUserObject.getSex(), activityLevel, gainLose));
            String email = Objects.requireNonNull(emailInput.getText()).toString().trim();
            String password = Objects.requireNonNull(passwordInput.getText()).toString().trim();
            createUserEmailAccount(email, password, newUserObject);
        });
    }

    private String getSelectedDate() {
        return birthdateInput.getDayOfMonth() + "/"
                + (birthdateInput.getMonth() + 1) + "/" + birthdateInput.getYear();
    }

    private void createUserEmailAccount(String email, String password, User newUserObject) {
        if (newUserObject != null && !TextUtils.isEmpty(password)) {
            loading.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, R.string.user_created_text, Toast.LENGTH_LONG).show();
                            currentUser = firebaseAuth.getCurrentUser();
                            assert currentUser != null;
                            String currentUserId = currentUser.getUid();
                            newUserObject.setUserId(currentUserId);
                            //create user map to create user in the user collection
                            Map<String, Object> userFirebaseObject = new HashMap<>();
                            userFirebaseObject.put("userId", currentUserId);
                            userFirebaseObject.put("name", newUserObject.getName());
                            userFirebaseObject.put("birthdate", newUserObject.getBirthdate());
                            userFirebaseObject.put("height", newUserObject.getHeight());
                            userFirebaseObject.put("weight", newUserObject.getWeight());
                            userFirebaseObject.put("sex", newUserObject.getSex().toString());
                            userFirebaseObject.put("caloriesPlan", newUserObject.getCaloriesPlan());
                            saveUserToFirebase(currentUserId, userFirebaseObject);
                        } else {
                            Toast.makeText(SignupActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (e.getMessage() != null) {
                            if (e.getMessage().contains("already")) {
                                Toast.makeText(SignupActivity.this, R.string.already_used_email_error_text, Toast.LENGTH_LONG).show();
                            } else if (e.getMessage().contains("network")) {
                                Toast.makeText(SignupActivity.this, R.string.no_internet_error_text, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SignupActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(SignupActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_LONG).show();
        }
    }

    private void saveUserToFirebase(String currentUserId, Map<String, Object> userFirebaseObject) {
        collectionReference.document(currentUserId).set(userFirebaseObject)
                .addOnSuccessListener(o -> {
                    MeasurementRecord heightRecord = new MeasurementRecord();
                    heightRecord.setMeasurementCategory(RecordType.HEIGHT);
                    heightRecord.setUserId(currentUser.getUid());
                    //height
                    heightRecord.setName("Initial height");
                    heightRecord.setRecordId(UUID.randomUUID().toString());
                    heightRecord.setDate(DateConverter.fromLongDate(new Date()));
                    heightRecord.setValue(newUserObject.getHeight());
                    heightRecord.setInitial(true);
                    measurementRecordsReference.document(heightRecord.getRecordId()).set(heightRecord);
                    //weight
                    MeasurementRecord weightRecord = new MeasurementRecord();
                    weightRecord.setMeasurementCategory(RecordType.WEIGHT);
                    weightRecord.setUserId(currentUser.getUid());
                    weightRecord.setName("Initial weight");
                    weightRecord.setRecordId(UUID.randomUUID().toString());
                    weightRecord.setDate(DateConverter.fromLongDate(new Date()));
                    weightRecord.setValue(newUserObject.getWeight());
                    weightRecord.setInitial(true);
                    measurementRecordsReference.document(weightRecord.getRecordId()).set(weightRecord);
                    loading.setVisibility(View.INVISIBLE);
                    moveToMainActivity();
                });
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(SignupActivity.this,
                MainActivity.class);
        intent.putExtra("userObject", newUserObject);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void initialiseComponents() {
        continueBtn1 = findViewById(R.id.signup_continue_btn);
        continueBtn2 = findViewById(R.id.signup_continue2_btn);
        finishBtn = findViewById(R.id.signup_finish_btn);
        loading = findViewById(R.id.login_loading);
        firstCard = findViewById(R.id.signup_card);
        secondCard = findViewById(R.id.signup_user_data_card);
        thirdCard = findViewById(R.id.signup_plan_card);
        emailInput = findViewById(R.id.signup_email_input);
        passwordInput = findViewById(R.id.signup_password_input);
        nameInput = findViewById(R.id.signup_user_data_name_input);
        heightInput = findViewById(R.id.signup_user_data_height_input);
        weightInput = findViewById(R.id.signup_user_data_weight_input);
        sexInput = findViewById(R.id.signup_user_data_sex_radio_group);
        activityLevelInput = findViewById(R.id.signup_user_data_activity_level);
        activityLevelInput.setAdapter(ArrayAdapter.createFromResource(this, R.array.activity_levels, R.layout.support_simple_spinner_dropdown_item));
        gainLoseInput = findViewById(R.id.signup_user_data_gain_lose);
        gainLoseInput.setAdapter(ArrayAdapter.createFromResource(this, R.array.gain_lose, R.layout.support_simple_spinner_dropdown_item));
        planPreview = findViewById(R.id.signup_user_calories);
        birthdateInput = findViewById(R.id.signup_user_data_birthdate_input);
        initialiseDatePicker();
    }

    private void initialiseDatePicker() {
        Calendar calendar = new GregorianCalendar();
        birthdateInput.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        Calendar test = new GregorianCalendar();
        //ms in a month * 12 months in a year * nb of years
        birthdateInput.setMaxDate(test.getTimeInMillis() - 26298L * 100000 * 12 * 10);
        birthdateInput.setMinDate(test.getTimeInMillis() - 26298L * 100000 * 12 * 100);

    }

}