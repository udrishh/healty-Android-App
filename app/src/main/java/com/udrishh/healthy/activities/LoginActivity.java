package com.udrishh.healthy.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.Drink;
import com.udrishh.healthy.classes.Food;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.Sex;
import com.udrishh.healthy.threads.DrinksUploadThread;
import com.udrishh.healthy.threads.FoodsDownloadThread;
import com.udrishh.healthy.threads.FoodsLoaderThread;
import com.udrishh.healthy.threads.FoodsUploadThread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginActivity extends AppCompatActivity {

    private User userObject;

    private ArrayList<Food> foods = new ArrayList<>();

    private MaterialButton loginBtn;
    private MaterialButton signupBtn;
    private MaterialCardView loginCard;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firestore Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private FirebaseUser firebaseUser;

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private ProgressBar loading;
    private ProgressBar loadingBig;
    private ImageView loadingBigText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ExecutorService callablePool = Executors.newFixedThreadPool(1);
//        List<Future<List<Food>>> results = new ArrayList<>();
//        FoodsLoaderThread callable = new FoodsLoaderThread(getApplicationContext());
//        Future<List<Food>> result = callablePool.submit(callable);
//        results.add(result);
//        callablePool.shutdown();
//
//
//        for (Future<List<Food>> foodList : results) {
//            try {
//                //do stuff with food list
//                for(Food food : foodList){
//
//                }
//            } catch (Exception e) {
//                Log.d("mytag", "EXCEPTION OCCURED : " + e.getMessage());
//            }
//        }

//        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
//        boolean isAvailable = sharedPreferences.getBoolean("databaseAvailable", false);
//        isAvailable = false;
//        if (!isAvailable) {
////            //not available
//            Toast.makeText(LoginActivity.this, "Database not avaliable", Toast.LENGTH_SHORT).show();
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("databaseAvailable", true);
//            editor.apply();
//            try {
////                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(getFilesDir() + "db.txt"));
////                bufferedWriter.write("test");
////                bufferedWriter.close();
////                Toast.makeText(LoginActivity.this, "File written", Toast.LENGTH_SHORT).show();
//                //DOWNLOAD DB
//                new FoodsDownloadThread(db, Environment.getExternalStorageDirectory() + "/foods_db.bin").start();
//                Log.d("mytag","written in" + Environment.getExternalStorageDirectory() + "/foods_db.bin");
//
//            } catch (Exception e) {
//                Toast.makeText(LoginActivity.this, "File couldn't be written", Toast.LENGTH_SHORT).show();
//                Log.d("mytag", "File write failed: " + e.toString());
//            }
//            Toast.makeText(LoginActivity.this, "not available", Toast.LENGTH_SHORT).show();
//            Log.d("mytag", "not available");
//
//        } else {
////            // available
//            Toast.makeText(LoginActivity.this, "Database avaliable", Toast.LENGTH_SHORT).show();
//            try {
//                //READ DB
////                BufferedReader bufferedReader = new BufferedReader(new FileReader(getFilesDir() + "foods_db.txt"));
////                String string = bufferedReader.readLine();
////                bufferedReader.close();
////                Toast.makeText(LoginActivity.this, string, Toast.LENGTH_SHORT).show();
//
//
//                FileInputStream fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory() + "/foods_db.bin");
//                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//                Food food;
//                food = (Food) objectInputStream.readObject();
//                if (food != null) {
//                    Log.d("mytag", food.toString());
//                }
//                while (food != null) {
//                    food = (Food) objectInputStream.readObject();
//                    if (food != null) {
//                        Log.d("mytag", food.toString());
//                    }
//                }
//            } catch (Exception e) {
//                Log.d("mytag", "File read xx failed: " + e.toString());
//            }
//            Toast.makeText(LoginActivity.this, "available", Toast.LENGTH_SHORT).show();
//            Log.d("mytag", "available");
//        }
        //

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    loadingBig.setVisibility(View.VISIBLE);
                    loadingBigText.setVisibility(View.VISIBLE);
                    loginCard.setVisibility(View.INVISIBLE);
                    currentUser = firebaseAuth.getCurrentUser();
                    final String currentUserId = currentUser.getUid();

                    collectionReference
                            .whereEqualTo("userId", currentUserId)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        return;
                                    }

                                    userObject = new User();
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                            userObject.setUserId(snapshot.getString("userId"));
                                            userObject.setName(snapshot.getString("name"));
                                            userObject.setBirthdate(snapshot.getString("birthdate"));
                                            userObject.setHeight(snapshot.get("height",Integer.class));
                                            userObject.setWeight(snapshot.get("weight",Integer.class));
                                            userObject.setCaloriesPlan(snapshot.get("caloriesPlan",Integer.class));
                                            if (Objects.equals(snapshot.getString("sex"), "MALE")) {
                                                userObject.setSex(Sex.MALE);
                                            } else {
                                                userObject.setSex(Sex.FEMALE);
                                            }

                                            loadDatabase();
                                        }
                                    }

                                }
                            });
                } else {
                   // Toast.makeText(LoginActivity.this, R.string.try_again_login_err_text,Toast.LENGTH_LONG).show();
                }
            }
        };

        setContentView(R.layout.activity_login);

        initialiseComponents();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
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
                    passwordInput.setError(getString(R.string.invalid_password_login_text));
                    isValid = false;
                }

                if (!isValid) {
                    return;
                }
                passwordInput.setError(null);
                emailInput.setError(null);
                loginEmailPasswordUser(emailInput.getText().toString().trim(),
                        passwordInput.getText().toString().trim());
            }
        });
    }

    private void loadDatabase() {
//        Log.d("mytag", "dau start");
//
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//
//        StorageReference filepath = storageReference.child("foods_db.csv");
//
//        File localFile = null;
//        try {
//            localFile = File.createTempFile("foods_db", "csv");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        File finalLocalFile = localFile;
//        filepath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                try {
//                    Log.d("mytag", finalLocalFile.toString());
//                    BufferedReader bufferedReader = new BufferedReader(new FileReader(finalLocalFile));
//                    bufferedReader.readLine();
//                    String line;
//                    while ((line = bufferedReader.readLine()) != null) {
//                        String[] items = line.split(",");
//                        Food food = new Food();
//                        food.setName(items[0]);
//                        food.setCalories(Integer.parseInt(items[1]));
//                        food.setProteins(Integer.parseInt(items[2]));
//                        food.setLipids(Integer.parseInt(items[3]));
//                        food.setCarbs(Integer.parseInt(items[4]));
//                        food.setFibers(Integer.parseInt(items[5]));
//                        food.setUserId(items[6]);
//                        food.setFoodId(UUID.randomUUID().toString());
//
//                        foods.add(food);
//
//                        Log.d("mytag", food.toString());
//                    }
//                    bufferedReader.close();
//                    Log.d("mytag", "success");
//                } catch (Exception e) {
//                    Log.d("mytag", e.getMessage());
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                Log.d("mytag", exception.getMessage());
//            }
//        }).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//                if(task.isSuccessful()){
//                    //start activity
//                    Intent intent = new Intent(LoginActivity.this,
//                            MainActivity.class);
//                    intent.putExtra("userObject", userObject);
//                    //intent.putParcelableArrayListExtra("foods", foods);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        });
//
//        Log.d("mytag", "dau start");

        Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        intent.putExtra("userObject", userObject);
        //intent.putParcelableArrayListExtra("foods", foods);
        startActivity(intent);
        finish();
    }

    private void loginEmailPasswordUser(String email, String password) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            loading.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String currentUserId = firebaseUser.getUid();
                                collectionReference.whereEqualTo("userId", currentUserId)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                if (error != null) {
                                                    //
                                                }
                                                assert value != null;
                                                if (!value.isEmpty()) {
                                                    //retrieve data
                                                    userObject = new User();
                                                    for (QueryDocumentSnapshot snapshot : value) {
                                                        userObject.setUserId(snapshot.getString("userId"));
                                                        userObject.setName(snapshot.getString("name"));
                                                        userObject.setBirthdate(snapshot.getString("birthdate"));
                                                        userObject.setHeight(snapshot.get("height",Integer.class));
                                                        userObject.setWeight(snapshot.get("weight",Integer.class));
                                                        userObject.setCaloriesPlan(snapshot.get("caloriesPlan",Integer.class));
                                                        if (Objects.equals(snapshot.getString("sex"), "MALE")) {
                                                            userObject.setSex(Sex.MALE);
                                                        } else {
                                                            userObject.setSex(Sex.FEMALE);
                                                        }
                                                    }

                                                    //go to main activity
                                                    //LOAD DATABASE

                                                    loadDatabase();
                                                } else {
                                                    loading.setVisibility(View.INVISIBLE);
                                                    assert error != null;
                                                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });


                            } else {
                                Toast.makeText(LoginActivity.this, "Datele de conectare sunt invalide!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading.setVisibility(View.INVISIBLE);
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, "Introduceti datele de conectare!", Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.INVISIBLE);
        }
    }

    private void initialiseComponents() {
        loginBtn = findViewById(R.id.login_login_btn);
        signupBtn = findViewById(R.id.create_account_btn);
        emailInput = findViewById(R.id.login_email_input);
        passwordInput = findViewById(R.id.login_password_input);
        loading = findViewById(R.id.login_loading);
        loadingBig = findViewById(R.id.login_loadingBig);
        loadingBigText = findViewById(R.id.login_loadingBigText);
        loginCard = findViewById(R.id.login_card);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

        //new DrinksUploadThread(db).start();
        //new FoodsUploadThread(db).start();
        // new FoodsDownloadThread(db, getFilesDir() + "foods_db.bin");

        //database available?
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}