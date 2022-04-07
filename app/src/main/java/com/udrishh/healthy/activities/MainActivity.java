package com.udrishh.healthy.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.enums.Sex;
import com.udrishh.healthy.fragments.SettingsFragment;
import com.udrishh.healthy.fragments.AddFragment;
import com.udrishh.healthy.fragments.ProfileFragment;
import com.udrishh.healthy.fragments.RecipesFragment;
import com.udrishh.healthy.fragments.StatisticsFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    private User user;
    private ArrayList<Food> foods = new ArrayList<>();
    private ArrayList<Drink> drinks = new ArrayList<>();
    private ArrayList<FoodDrinkRecord> foodDrinkRecords = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference foodDrinkRecordsReference = db.collection("Records");

    public void addFoodDrinkRecord(FoodDrinkRecord foodDrinkRecord) {
        if (foodDrinkRecord != null) {
            foodDrinkRecords.add(foodDrinkRecord);
            Log.d("mytag", foodDrinkRecord.toString());
            Log.d("mytag", "Record added succesfully");

            //add to firebase
            foodDrinkRecordsReference.add(foodDrinkRecord)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("mytag", "Record was added to firebase successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("mytag", "Error occurred while adding record to firebase!");
                        }
                    });
        }
    }

    public User getUserObject() {
        return user;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public ArrayList<Drink> getDrinks() {return drinks;}

    public ArrayList<FoodDrinkRecord> getFoodDrinkRecords() {
        return foodDrinkRecords;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    private void setProfileFragment() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_profile);
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame_layout, new ProfileFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("mytag","MainActivity onStart");
        loadDatabase();
        //load records
        if(foodDrinkRecords.isEmpty()){
            loadUserFoodDrinkRecords();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("mytag","MainActivity onCreate");

        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("userObject");

        firebaseAuth = FirebaseAuth.getInstance();

        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.main_frame_layout);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_profile);
        fragment = new ProfileFragment();
        fragmentManager.beginTransaction()
                .add(R.id.main_frame_layout, fragment)
                .commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_profile:
                        setProfileFragment();
                        break;
                    case R.id.menu_item_recipes:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_recipes);
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_frame_layout, new RecipesFragment())
                                .commit();
                        break;
                    case R.id.menu_item_add:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_add);
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_frame_layout, new AddFragment())
                                .commit();
                        break;
                    case R.id.menu_item_statistics:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_statistics);
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_frame_layout, new StatisticsFragment())
                                .commit();
                        break;
                    case R.id.menu_item_settings:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_settings);
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_frame_layout, new SettingsFragment())
                                .commit();
                        break;
                }
                return true;
            }
        });

    }

    private void loadDatabase() {
        Log.d("mytag", "Database task started!");
        loadFoods();
        loadDrinks();
    }

    private void loadDrinks() {
        //LOAD DRINKS
        new Thread() {
            @Override
            public void run() {
                super.run();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference filepath = storageReference.child("drinks_db.csv");

                File localFile = null;
                try {
                    localFile = File.createTempFile("drinks_db", "csv");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                File finalLocalFile = localFile;
                Log.d("mytag", finalLocalFile.toString());
                filepath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        try {
                            Log.d("mytag", "Started loading database!");
                            Log.d("mytag", finalLocalFile.toString());
                            BufferedReader bufferedReader = new BufferedReader(new FileReader(finalLocalFile));
                            bufferedReader.readLine();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                String[] items = line.split(",");
                                Drink drink = new Drink();
                                drink.setName(items[0]);
                                drink.setCalories(Integer.parseInt(items[1]));
                                drink.setProteins(Integer.parseInt(items[2]));
                                drink.setLipids(Integer.parseInt(items[3]));
                                drink.setCarbs(Integer.parseInt(items[4]));
                                drink.setFibers(Integer.parseInt(items[5]));
                                drink.setUserId(items[6]);
                                drink.setDrinkId(items[7]);

                                drinks.add(drink);
                                //Log.d("mytag", food.toString());
                            }
                            bufferedReader.close();
                            if(drinks.size() > 0){
                                Log.d("mytag", "Successfully loaded database!");
                            } else {
                                Log.d("mytag", "There've been an error loading the database!");
                            }
                        } catch (Exception e) {
                            Log.d("mytag", "Exception occured: " + e.getMessage());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("mytag", "Exception occured: " + exception.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            //DATABASE LOADED SUCCESSFULLY
                        }
                    }
                });

                Log.d("mytag", "Database task finished!");
            }
        }.start();
    }

    private void loadFoods() {
        //LOAD FOODS
        new Thread() {
            @Override
            public void run() {
                super.run();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference filepath = storageReference.child("foods_db.csv");

                File localFile = null;
                try {
                    localFile = File.createTempFile("foods_db", "csv");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                File finalLocalFile = localFile;
                Log.d("mytag", finalLocalFile.toString());
                filepath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        try {
                            Log.d("mytag", "Started loading database!");
                            Log.d("mytag", finalLocalFile.toString());
                            BufferedReader bufferedReader = new BufferedReader(new FileReader(finalLocalFile));
                            bufferedReader.readLine();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                String[] items = line.split(",");
                                Food food = new Food();
                                food.setName(items[0]);
                                food.setCalories(Integer.parseInt(items[1]));
                                food.setProteins(Integer.parseInt(items[2]));
                                food.setLipids(Integer.parseInt(items[3]));
                                food.setCarbs(Integer.parseInt(items[4]));
                                food.setFibers(Integer.parseInt(items[5]));
                                food.setUserId(items[6]);
                                food.setFoodId(items[7]);

                                foods.add(food);
                                //Log.d("mytag", food.toString());
                            }
                            bufferedReader.close();
                            if(foods.size() > 0){
                                Log.d("mytag", "Successfully loaded database!");
                            } else {
                                Log.d("mytag", "There've been an error loading the database!");
                            }
                        } catch (Exception e) {
                            Log.d("mytag", "Exception occured: " + e.getMessage());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("mytag", "Exception occured: " + exception.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            //DATABASE LOADED SUCCESSFULLY
                        }
                    }
                });

                Log.d("mytag", "Database task finished!");
            }
        }.start();
    }

    private void loadUserFoodDrinkRecords() {
        Log.d("mytag","MainActivity loading records............");

        foodDrinkRecordsReference
                .whereEqualTo("userId", user.getUserId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        if (!queryDocumentSnapshots.isEmpty() && foodDrinkRecords.isEmpty()) {
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                FoodDrinkRecord foodDrinkRecord = new FoodDrinkRecord();
                                foodDrinkRecord.setRecordId(snapshot.getString("recordId"));
                                foodDrinkRecord.setUserId(snapshot.getString("userId"));
                                foodDrinkRecord.setItemId(snapshot.getString("itemId"));
                                foodDrinkRecord.setDate(snapshot.getString("date"));
                                foodDrinkRecord.setName(snapshot.getString("name"));
                                foodDrinkRecord.setProteins(snapshot.get("proteins", Integer.class));
                                foodDrinkRecord.setLipids(snapshot.get("lipids", Integer.class));
                                foodDrinkRecord.setCarbs(snapshot.get("carbs", Integer.class));
                                foodDrinkRecord.setFibers(snapshot.get("fibers", Integer.class));
                                foodDrinkRecord.setQuantity(snapshot.get("quantity", Integer.class));
                                foodDrinkRecord.setTotalCalories(snapshot.get("totalCalories", Integer.class));
                                foodDrinkRecord.setTotalProteins(snapshot.get("totalProteins", Integer.class));
                                foodDrinkRecord.setTotalFibers(snapshot.get("totalFibers", Integer.class));
                                foodDrinkRecord.setTotalCarbs(snapshot.get("totalCarbs", Integer.class));
                                foodDrinkRecord.setTotalLipids(snapshot.get("totalLipids", Integer.class));
                                if (snapshot.getString("category").equals("FOOD")) {
                                    foodDrinkRecord.setCategory(RecordType.FOOD);
                                } else {
                                    foodDrinkRecord.setCategory(RecordType.DRINK);
                                }

                                foodDrinkRecords.add(foodDrinkRecord);
                                Log.d("mytag", "Record was retrieved from firebase successfully!");
                            }
                            setProfileFragment();
                        }
                    }
                });

    }
}