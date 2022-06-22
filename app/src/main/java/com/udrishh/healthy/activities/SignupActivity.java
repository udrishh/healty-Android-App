package com.udrishh.healthy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
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
import java.util.HashMap;
import java.util.Map;
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
    private TextInputEditText birthdateInput;
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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    private CollectionReference measurementRecordsReference = db.collection("MeasurementRecords");

    private int getAge(String birthdate) {
        return Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(birthdateInput.getText().toString().trim().split("/")[2]);
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
                Calculator.BMR(Integer.parseInt(weightInput.getText().toString().trim()),
                        Integer.parseInt(heightInput.getText().toString().trim()),
                        getAge(birthdateInput.getText().toString().trim()),
                        getSelectedSex(), getSelectedActivityLevel(), getSelectedGainLose())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initialiseComponents();

        newUserObject = new User();

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    //user is already logged in
                } else {
                    //no user yet
                }
            }
        };

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

        continueBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (TextUtils.isEmpty(emailInput.getText().toString().trim())
                        || !emailInput.getText().toString().trim().matches(emailPattern)) {
                    emailInput.setError(getString(R.string.invalid_email_text));
                    isValid = false;
                }
                if (TextUtils.isEmpty(passwordInput.getText().toString().trim())
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
            }
        });

        continueBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                if (TextUtils.isEmpty(nameInput.getText().toString().trim())) {
                    nameInput.setError(getString(R.string.invalid_username_text));
                    isValid = false;
                } else if (nameInput.getText().toString().trim().length() < 2) {
                    nameInput.setError(getString(R.string.invalid_username_text));
                    isValid = false;
                }
                if (TextUtils.isEmpty(heightInput.getText().toString().trim())) {
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
                if (TextUtils.isEmpty(weightInput.getText().toString().trim())) {
                    weightInput.setError(getString(R.string.invalid_weight_text));
                    isValid = false;
                }
                if (!weightInput.getText().toString().trim().equals("")) {
                    if (Integer.parseInt(weightInput.getText().toString().trim()) < 40
                            || Integer.parseInt(weightInput.getText().toString().trim()) > 300) {
                        weightInput.setError(getString(R.string.invalid_weight_text));
                        isValid = false;
                    }
                }
                if (TextUtils.isEmpty(birthdateInput.getText().toString().trim())) {
                    birthdateInput.setError(getString(R.string.invalid_birthdate_text));
                    isValid = false;
                }
                if (!TextUtils.isEmpty(birthdateInput.getText().toString().trim())) {
                    String[] date = birthdateInput.getText().toString().trim().split("/");
                    if ((Integer.parseInt(date[0]) < 1 && Integer.parseInt(date[0]) > 31)
                            || (Integer.parseInt(date[1]) < 1 && Integer.parseInt(date[1]) > 12)
                            || (Integer.parseInt(date[2]) < 1920 && Integer.parseInt(date[2]) > (new Date().getYear() - 14))) {
                        birthdateInput.setError(getString(R.string.invalid_birthdate_text));
                        isValid = false;
                    }
                }

                if (!isValid) {
                    return;
                }
                nameInput.setError(null);
                heightInput.setError(null);
                weightInput.setError(null);
                birthdateInput.setError(null);

                setPlanPreview();
                isReady = true;
                activityLevelInput.setSelection(0);
                secondCard.setVisibility(View.INVISIBLE);
                thirdCard.setVisibility(View.VISIBLE);
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create account from components
                newUserObject.setName(nameInput.getText().toString().trim());
                newUserObject.setHeight(Integer.parseInt(heightInput.getText().toString().trim()));
                newUserObject.setWeight(Integer.parseInt(weightInput.getText().toString().trim()));
                newUserObject.setBirthdate(birthdateInput.getText().toString().trim());
                newUserObject.setSex(getSelectedSex());
                ActivityLevel activityLevel = getSelectedActivityLevel();
                GainLose gainLose = getSelectedGainLose();
                int userAge = getAge(newUserObject.getBirthdate());

                newUserObject.setCaloriesPlan(Calculator.BMR(newUserObject.getWeight(),
                        newUserObject.getHeight(), userAge, newUserObject.getSex(), activityLevel, gainLose));

                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                createUserEmailAccount(email, password, newUserObject);
            }
        });
    }

    private void createUserEmailAccount(String email, String password, User newUserObject) {
        if (newUserObject != null && !TextUtils.isEmpty(password)) {

            loading.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, R.string.user_created_text, Toast.LENGTH_LONG).show();
                                //take user to the app main activity
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
                                userFirebaseObject.put("caloriesPlan", Integer.toString(newUserObject.getCaloriesPlan()));

                                //save to firebase firestore
                                collectionReference.document(currentUserId).set(userFirebaseObject)
                                        .addOnSuccessListener(new OnSuccessListener() {

                                            @Override
                                            public void onSuccess(Object o) {
                                                MeasurementRecord heightRecord = new MeasurementRecord();
                                                heightRecord.setMeasurementCategory(RecordType.HEIGHT);
                                                heightRecord.setUserId(currentUser.getUid());
                                                heightRecord.setName("Initial height");
                                                heightRecord.setRecordId(UUID.randomUUID().toString());
                                                heightRecord.setDate(DateConverter.fromDate(new Date()));
                                                heightRecord.setValue(newUserObject.getHeight());
                                                heightRecord.setInitial(true);
                                                measurementRecordsReference.add(heightRecord)
                                                        .addOnSuccessListener(documentReference -> Log.d("mytag", "Record was added to firebase successfully!"))
                                                        .addOnFailureListener(e -> Log.d("mytag", "Error occurred while adding record to firebase!"));
                                                //weight
                                                MeasurementRecord weightRecord = new MeasurementRecord();
                                                weightRecord.setMeasurementCategory(RecordType.WEIGHT);
                                                weightRecord.setUserId(currentUser.getUid());
                                                weightRecord.setName("Initial weight");
                                                weightRecord.setRecordId(UUID.randomUUID().toString());
                                                weightRecord.setDate(DateConverter.fromDate(new Date()));
                                                weightRecord.setValue(newUserObject.getWeight());
                                                weightRecord.setInitial(true);
                                                measurementRecordsReference.add(weightRecord)
                                                        .addOnSuccessListener(documentReference -> Log.d("mytag", "Record was added to firebase successfully!"))
                                                        .addOnFailureListener(e -> Log.d("mytag", "Error occurred while adding record to firebase!"));
                                                //start activity
                                                loading.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(SignupActivity.this,
                                                        MainActivity.class);
                                                intent.putExtra("userObject", newUserObject);
                                                startActivity(intent);
                                            }

//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//                                                documentReference.get()
//                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                                if (task.getResult().exists()) {
//                                                                    //upload first records
//                                                                    //height
//
//                                                                } else {
//                                                                    loading.setVisibility(View.INVISIBLE);
//                                                                }
//                                                            }
//                                                        });
//                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                            } else {
                                //something went wrong
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(SignupActivity.this, "Error creating user!", Toast.LENGTH_LONG).show();
                            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(SignupActivity.this, "Bad user data!", Toast.LENGTH_LONG).show();
        }
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
        birthdateInput = findViewById(R.id.signup_user_data_birthdate_input);
        sexInput = findViewById(R.id.signup_user_data_sex_radio_group);
        activityLevelInput = findViewById(R.id.signup_user_data_activity_level);
        activityLevelInput.setAdapter(ArrayAdapter.createFromResource(this, R.array.activity_levels, R.layout.support_simple_spinner_dropdown_item));
        gainLoseInput = findViewById(R.id.signup_user_data_gain_lose);
        gainLoseInput.setAdapter(ArrayAdapter.createFromResource(this, R.array.gain_lose, R.layout.support_simple_spinner_dropdown_item));
        planPreview = findViewById(R.id.signup_user_calories);
    }

}