package com.udrishh.healthy.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
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
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.MeasurementRecord;
import com.udrishh.healthy.classes.PhysicalActivity;
import com.udrishh.healthy.classes.PhysicalActivityRecord;
import com.udrishh.healthy.classes.Recipe;
import com.udrishh.healthy.classes.RecipeRecord;
import com.udrishh.healthy.classes.Record;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.RecipeCategory;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.fragments.NoInternetFragment;
import com.udrishh.healthy.fragments.SettingsFragment;
import com.udrishh.healthy.fragments.AddFragment;
import com.udrishh.healthy.fragments.ProfileFragment;
import com.udrishh.healthy.fragments.RecipesFragment;
import com.udrishh.healthy.fragments.StatisticsFragment;
import com.udrishh.healthy.threads.DrinksUploadThread;
import com.udrishh.healthy.threads.FoodsUploadThread;
import com.udrishh.healthy.threads.RecipesUploadThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private FragmentManager fragmentManager;
    private String joinDate = "";

    private User user;
    private ArrayList<Food> foods = new ArrayList<>();
    private ArrayList<Drink> drinks = new ArrayList<>();
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private ArrayList<PhysicalActivity> physicalActivities = new ArrayList<>();
    private ArrayList<FoodDrinkRecord> foodDrinkRecords = new ArrayList<>();
    private ArrayList<PhysicalActivityRecord> physicalActivityRecords = new ArrayList<>();
    private ArrayList<MeasurementRecord> measurementRecords = new ArrayList<>();
    private ArrayList<RecipeRecord> recipeRecords = new ArrayList<>();

    private ArrayList<Record> records = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference usersReference = db.collection("Users");

    private CollectionReference foodDrinkRecordsReference = db.collection("FoodDrinkRecords");
    private CollectionReference drinkReference = db.collection("Drinks");
    private CollectionReference foodReference = db.collection("Foods");
    private CollectionReference physicalActivityReference = db.collection("PhysicalActivities");
    private CollectionReference physicalActivityRecordsReference = db.collection("PhysicalActivityRecords");
    private CollectionReference measurementRecordsReference = db.collection("MeasurementRecords");
    private CollectionReference recipeRecordsReference = db.collection("RecipeRecords");

    private int eatenCalories;
    private boolean connectionAvailable;
    private boolean uiReady = false;
    private int tasksReady = 0;

    public void addFoodDrinkRecord(FoodDrinkRecord foodDrinkRecord) {
        if (foodDrinkRecord != null) {
            records.add(foodDrinkRecord);
            foodDrinkRecords.add(foodDrinkRecord);
            foodDrinkRecordsReference.document(foodDrinkRecord.getRecordId()).set(foodDrinkRecord)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(MainActivity.this, getText(R.string.record_added_text), Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
        }
    }

    public void addPhysicalActivityRecord(PhysicalActivityRecord physicalActivityRecord) {
        if (physicalActivityRecord != null) {
            records.add(physicalActivityRecord);
            physicalActivityRecords.add(physicalActivityRecord);
            physicalActivityRecordsReference.document(physicalActivityRecord.getRecordId()).set(physicalActivityRecord)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(MainActivity.this, getText(R.string.record_added_text), Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
        }
    }

    public void addMeasurementRecord(MeasurementRecord measurementRecord) {
        if (measurementRecord != null) {
            records.add(measurementRecord);
            measurementRecords.add(measurementRecord);
            measurementRecordsReference.document(measurementRecord.getRecordId()).set(measurementRecord)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(MainActivity.this, getText(R.string.record_added_text), Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
        }
        //update user height or weight
        assert measurementRecord != null;
        if (measurementRecord.getMeasurementCategory() == RecordType.HEIGHT) {
            usersReference.document(user.getUserId())
                    .update("height", measurementRecord.getValue());
        } else {
            usersReference.document(user.getUserId())
                    .update("weight", measurementRecord.getValue());
        }
    }

    public void addRecipeRecord(RecipeRecord recipeRecord) {
        if (recipeRecord != null) {
            records.add(recipeRecord);
            recipeRecords.add(recipeRecord);
            recipeRecordsReference.document(recipeRecord.getRecordId()).set(recipeRecord)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(MainActivity.this, getText(R.string.record_added_text), Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
        }
    }

    public BottomNavigationView getBottomNavigation() {
        return bottomNavigation;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public int getTasksReady() {
        return tasksReady;
    }

    public User getUserObject() {
        return user;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public ArrayList<Drink> getDrinks() {
        return drinks;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public ArrayList<PhysicalActivity> getPhysicalActivities() {
        return physicalActivities;
    }

    public ArrayList<FoodDrinkRecord> getFoodDrinkRecords() {
        return foodDrinkRecords;
    }

    public ArrayList<PhysicalActivityRecord> getPhysicalActivityRecords() {
        return physicalActivityRecords;
    }

    public ArrayList<RecipeRecord> getRecipeRecords() {
        return recipeRecords;
    }

    public ArrayList<MeasurementRecord> getMeasurementRecords() {
        return measurementRecords;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    private void setNoInternetFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame_layout, new NoInternetFragment())
                .commit();
    }

    private void clearFragmentBackStack() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void setProfileFragment() {
        clearFragmentBackStack();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new ProfileFragment())
                .commit();
    }

    private void setRecipesFragment() {
        clearFragmentBackStack();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new RecipesFragment())
                .commit();
    }

    private void setAddFragment() {
        clearFragmentBackStack();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new AddFragment())
                .commit();
    }

    private void setStatisticsFragment() {
        clearFragmentBackStack();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new StatisticsFragment())
                .commit();
    }

    private void setSettingsFragment() {
        clearFragmentBackStack();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new SettingsFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDatabase();
        loadRecords();
    }

    private void loadRecords() {
        //load records
        if (foodDrinkRecords.isEmpty()) {
            loadUserFoodDrinkRecords();
        }
        if (physicalActivityRecords.isEmpty()) {
            loadPhysicalActivityRecords();
        }
        if (measurementRecords.isEmpty()) {
            loadMeasurementRecords();
        }
        if (recipeRecords.isEmpty()) {
            loadRecipeRecords();
        }
    }

    private void loadRecipeRecords() {
        Log.d("mytag", "MainActivity loading records............");

        recipeRecordsReference
                .whereEqualTo("userId", user.getUserId())
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }

                    if (!queryDocumentSnapshots.isEmpty() && recipeRecords.isEmpty()) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            RecipeRecord recipeRecord = new RecipeRecord();
                            recipeRecord.setDate(snapshot.getString("date"));
                            recipeRecord.setQuantity(snapshot.get("quantity", Integer.class));
                            recipeRecord.setItemId(snapshot.getString("itemId"));
                            recipeRecord.setName(snapshot.getString("name"));
                            recipeRecord.setRecordId(snapshot.getString("recordId"));
                            recipeRecord.setUserId(snapshot.getString("userId"));
                            recipeRecords.add(recipeRecord);
                            records.add(recipeRecord);

                            Log.d("mytag", "Record was retrieved from firebase successfully!");
                        }
                    }
                });
    }

    private void loadMeasurementRecords() {
        Log.d("mytag", "MainActivity loading records............");

        measurementRecordsReference
                .whereEqualTo("userId", user.getUserId())
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }

                    if (!queryDocumentSnapshots.isEmpty() && measurementRecords.isEmpty()) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            if (snapshot != null) {
                                MeasurementRecord measurementRecord = new MeasurementRecord();
                                measurementRecord.setDate(snapshot.getString("date"));
                                measurementRecord.setName(snapshot.getString("name"));
                                measurementRecord.setRecordId(snapshot.getString("recordId"));
                                measurementRecord.setUserId(snapshot.getString("userId"));
                                measurementRecord.setValue(snapshot.get("value", Integer.class));
                                measurementRecord.setInitial(snapshot.get("initial", Boolean.class));
                                if (snapshot.getString("measurementCategory").equals("HEIGHT")) {
                                    measurementRecord.setMeasurementCategory(RecordType.HEIGHT);
                                } else {
                                    measurementRecord.setMeasurementCategory(RecordType.WEIGHT);
                                }
                                measurementRecords.add(measurementRecord);
                                if (!measurementRecord.isInitial()) {
                                    records.add(measurementRecord);
                                } else {
                                    joinDate = measurementRecord.getDate().split(" ")[0];
                                }

                                Log.d("mytag", "Record was retrieved from firebase successfully!");
                            }
                        }
                    }
                });
    }

    private void loadPhysicalActivityRecords() {
        Log.d("mytag", "MainActivity loading records............");

        physicalActivityRecordsReference
                .whereEqualTo("userId", user.getUserId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        if (!queryDocumentSnapshots.isEmpty() && physicalActivityRecords.isEmpty()) {
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                PhysicalActivityRecord physicalActivityRecord = new PhysicalActivityRecord();
                                physicalActivityRecord.setRecordId(snapshot.getString("recordId"));
                                physicalActivityRecord.setUserId(snapshot.getString("userId"));
                                physicalActivityRecord.setItemId(snapshot.getString("itemId"));
                                physicalActivityRecord.setName(snapshot.getString("name"));
                                physicalActivityRecord.setQuantity(snapshot.get("quantity", Integer.class));
                                physicalActivityRecord.setDate(snapshot.getString("date"));

                                physicalActivityRecords.add(physicalActivityRecord);
                                records.add(physicalActivityRecord);

                                Log.d("mytag", "Record was retrieved from firebase successfully!");
                            }
                        }
                    }
                });
    }

    private void setProfileProgress() {
        Log.d("tasks_waiting", String.valueOf(tasksReady));
        if (tasksReady >= 2) {
            bottomNavigation.setSelectedItemId(R.id.menu_item_profile);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(ConnectivityManager.class);
        connectivityManager.requestNetwork(networkRequest, networkCallback);

        //firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("userObject");

        firebaseAuth = FirebaseAuth.getInstance();
        fragmentManager = getSupportFragmentManager();
        uiReady = true;

//        while(tasksReady!=3){
        Log.d("tasks_waiting", String.valueOf(tasksReady));
//        }

        if (isNetworkAvailable()) {
            setProfileFragment();
        } else {
            setNoInternetFragment();
        }
        initialiseComponents();

        //new PhysicalActivitiesUploadThread(db).start();
        //new DrinksUploadThread(db).start();
        //new FoodsUploadThread(db).start();
        //new RecipesUploadThread(db).start();
    }

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            if (uiReady) {
                setProfileFragment();
            }
            connectionAvailable = true;
            bottomNavigation.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            setNoInternetFragment();
            connectionAvailable = false;
        }
    };

    @SuppressLint("NonConstantResourceId")
    private void initialiseComponents() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (connectionAvailable) {
                switch (item.getItemId()) {
                    case R.id.menu_item_profile:
                        setProfileFragment();
                        break;
                    case R.id.menu_item_recipes:
                        setRecipesFragment();
                        break;
                    case R.id.menu_item_add:
                        setAddFragment();
                        break;
                    case R.id.menu_item_statistics:
                        setStatisticsFragment();
                        break;
                    case R.id.menu_item_settings:
                        setSettingsFragment();
                        break;
                }
                return true;
            } else {
                return false;
            }

        });
    }

    private void loadDatabase() {
        if (foods.isEmpty()) {
            loadFoods();
        }
        if (drinks.isEmpty()) {
            loadDrinks();
        }
        if (physicalActivities.isEmpty()) {
            loadPhysicalActivities();
        }
        if (recipes.isEmpty()) {
            loadRecipes();
        }
    }

    private void loadRecipes() {
        //LOAD RECIPES
        new Thread() {
            private final CollectionReference collectionReference = db.collection("Recipes");

            @Override
            public void run() {
                super.run();
                collectionReference.get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot recipeSnapshot : queryDocumentSnapshots) {
                                    Recipe recipe = recipeSnapshot.toObject(Recipe.class);
                                    recipes.add(recipe);
                                    Log.d("task_db_load", "LOADED R: " + recipe.getName());
                                }
                            }
                            tasksReady++;
                            setProfileProgress();
                        })
                        .addOnFailureListener(e -> Log.d("db_threads", "Error loading recipes"));
            }
        }.start();

//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//                StorageReference filepath = storageReference.child("recipes_db.csv");
//
//                File localFile = null;
//                try {
//                    localFile = File.createTempFile("recipes_db", "csv");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                File finalLocalFile = localFile;
//                Log.d("mytag2", finalLocalFile.toString());
//                filepath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        try {
//                            Log.d("mytag2", "Started loading database!");
//                            Log.d("mytag2", finalLocalFile.toString());
//                            BufferedReader bufferedReader = new BufferedReader(new FileReader(finalLocalFile));
//                            String line;
//                            while ((line = bufferedReader.readLine()) != null) {
//                                String[] items = line.split(",");
//
//                                Recipe recipe = new Recipe();
//                                recipe.setName(items[0]);
//                                String[] categories = items[1].split(";");
//                                for (String category : categories) {
//                                    if (category != null) {
//                                        Log.d("categ", "category: " + category);
//                                        switch (category) {
//                                            case "BREAKFAST":
//                                                recipe.addCategory(RecipeCategory.BREAKFAST);
//                                                break;
//                                            case "LUNCH":
//                                                recipe.addCategory(RecipeCategory.LUNCH);
//                                                break;
//                                            case "DINNER":
//                                                recipe.addCategory(RecipeCategory.DINNER);
//                                                break;
//                                            case "DESSERT":
//                                                recipe.addCategory(RecipeCategory.DESSERT);
//                                                break;
//                                            case "DRINKS":
//                                                recipe.addCategory(RecipeCategory.DRINKS);
//                                                break;
//                                            case "GLUTEN_FREE":
//                                                recipe.addCategory(RecipeCategory.GLUTEN_FREE);
//                                                break;
//                                            case "LOW_CALORIES":
//                                                recipe.addCategory(RecipeCategory.LOW_CALORIES);
//                                                break;
//                                        }
//                                    }
//                                }
//                                String[] ingredients = items[2].split(";");
//                                for (String ingredient : ingredients) {
//                                    if (ingredient != null) {
//                                        recipe.addIngredient(ingredient);
//                                    }
//                                }
//                                recipe.setServings(Integer.parseInt(items[3]));
//                                recipe.setQuantity(Integer.parseInt(items[4]));
//                                recipe.setCalories(Integer.parseInt(items[5]));
//                                recipe.setImg(items[6]);
//                                recipe.setSource(items[7]);
//                                recipe.setRecipeId(items[8]);
//
//                                recipes.add(recipe);
//                                Log.d("checkrecipes", recipe.toString() + " " + recipe.getCategories());
//                            }
//                            bufferedReader.close();
//                            if (recipes.size() > 0) {
//                                Log.d("mytag2", "Successfully loaded database!");
//                                Toast.makeText(MainActivity.this, "PhysicalActivityDB Loaded!", Toast.LENGTH_SHORT);
//                            } else {
//                                Log.d("mytag2", "There've been an error loading the database!");
//                            }
//                        } catch (Exception e) {
//                            Log.d("mytag2", "Exception occured: " + e.getMessage());
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Log.d("mytag2", "Exception occured: " + exception.getMessage());
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            //DATABASE LOADED SUCCESSFULLY
//                        }
//                    }
//                });
//
//                Log.d("mytag2", "Database task finished!");
//            }
//        }.start();
    }

    private void loadPhysicalActivities() {
        //LOAD FOODS
        new Thread() {
            private final CollectionReference collectionReference = db.collection("PhysicalActivities");

            @Override
            public void run() {
                super.run();
                collectionReference.get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot physicalActivitySnapshot : queryDocumentSnapshots) {
                                    PhysicalActivity physicalActivity = physicalActivitySnapshot.toObject(PhysicalActivity.class);
                                    if (physicalActivity.getUserId().equals("admin") ||
                                            physicalActivity.getUserId().equals(user.getUserId())) {
                                        physicalActivities.add(physicalActivity);
                                        Log.d("task_db_load", "LOADED P: " + physicalActivity.getName());
                                    }
                                }
                            }
                            tasksReady++;
                            setProfileProgress();
                        })
                        .addOnFailureListener(e -> Log.d("db_threads", "Error loading foods"));
            }
        }.start();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//                StorageReference filepath = storageReference.child("activities_db.csv");
//
//                File localFile = null;
//                try {
//                    localFile = File.createTempFile("activities_db", "csv");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                File finalLocalFile = localFile;
//                Log.d("mytag", finalLocalFile.toString());
//                filepath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        try {
//                            Log.d("mytag", "Started loading database!");
//                            Log.d("mytag", finalLocalFile.toString());
//                            BufferedReader bufferedReader = new BufferedReader(new FileReader(finalLocalFile));
//                            bufferedReader.readLine();
//                            String line;
//                            while ((line = bufferedReader.readLine()) != null) {
//                                String[] items = line.split(",");
//                                PhysicalActivity physicalActivity = new PhysicalActivity();
//                                physicalActivity.setName(items[0]);
//                                physicalActivity.setCalories(Float.parseFloat(items[1]));
//                                physicalActivity.setPhysicalActivityId(items[2]);
//                                physicalActivity.setUserId(items[3]);
//
//                                if (physicalActivity.getUserId().equals("admin") ||
//                                        physicalActivity.getUserId().equals(user.getUserId())) {
//                                    physicalActivities.add(physicalActivity);
//                                }
//                                //Log.d("mytag", food.toString());
//                            }
//                            bufferedReader.close();
//                            if (physicalActivities.size() > 0) {
//                                Log.d("mytag", "Successfully loaded database!");
//                                Toast.makeText(MainActivity.this, "PhysicalActivityDB Loaded!", Toast.LENGTH_SHORT);
//                            } else {
//                                Log.d("mytag", "There've been an error loading the database!");
//                            }
//                        } catch (Exception e) {
//                            Log.d("mytag", "Exception occured: " + e.getMessage());
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Log.d("mytag", "Exception occured: " + exception.getMessage());
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            //DATABASE LOADED SUCCESSFULLY
//                        }
//                    }
//                });
//
//                Log.d("mytag", "Database task finished!");
//            }
//        }.start();
    }

    private void loadDrinks() {
        //LOAD DRINKS
        new Thread() {
            private final CollectionReference collectionReference = db.collection("Drinks");

            @Override
            public void run() {
                super.run();
                collectionReference.get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot drinkSnapshot : queryDocumentSnapshots) {
                                    Drink drink = drinkSnapshot.toObject(Drink.class);
                                    drinks.add(drink);
                                    Log.d("task_db_load", "LOADED D: " + drink.getName());
                                }
                            }
                            tasksReady++;
                            setProfileProgress();
                        })
                        .addOnFailureListener(e -> Log.d("db_threads", "Error loading drinks"));
            }
        }.start();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//                StorageReference filepath = storageReference.child("drinks_db.csv");
//
//                File localFile = null;
//                try {
//                    localFile = File.createTempFile("drinks_db", "csv");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                File finalLocalFile = localFile;
//                Log.d("mytag", finalLocalFile.toString());
//                filepath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
//                    try {
//                        Log.d("mytag", "Started loading database!");
//                        Log.d("mytag", finalLocalFile.toString());
//                        BufferedReader bufferedReader = new BufferedReader(new FileReader(finalLocalFile));
//                        bufferedReader.readLine();
//                        String line;
//                        while ((line = bufferedReader.readLine()) != null) {
//                            String[] items = line.split(",");
//                            Drink drink = new Drink();
//                            drink.setName(items[0]);
//                            drink.setCalories(Integer.parseInt(items[1]));
//                            drink.setProteins(Integer.parseInt(items[2]));
//                            drink.setLipids(Integer.parseInt(items[3]));
//                            drink.setCarbs(Integer.parseInt(items[4]));
//                            drink.setFibers(Integer.parseInt(items[5]));
//                            drink.setUserId(items[6]);
//                            drink.setDrinkId(items[7]);
//
//                            drinks.add(drink);
//                            //Log.d("mytag", food.toString());
//                        }
//                        bufferedReader.close();
//                        if (drinks.size() > 0) {
//                            Log.d("mytag", "Successfully loaded database!");
//                            Toast.makeText(MainActivity.this, "DrinkDB Loaded!", Toast.LENGTH_SHORT);
//                        } else {
//                            Log.d("mytag", "There've been an error loading the database!");
//                        }
//                    } catch (Exception e) {
//                        Log.d("mytag", "Exception occured: " + e.getMessage());
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Log.d("mytag", "Exception occured: " + exception.getMessage());
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            //DATABASE LOADED SUCCESSFULLY
//                        }
//                    }
//                });
//
//                Log.d("mytag", "Database task finished!");
//            }
//        }.start();
    }

    private void loadFoods() {
        //LOAD FOODS
        new Thread() {
            private final CollectionReference collectionReference = db.collection("Foods");

            @Override
            public void run() {
                super.run();
                collectionReference.get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot foodSnapshot : queryDocumentSnapshots) {
                                    Food food = foodSnapshot.toObject(Food.class);
                                    foods.add(food);
                                    Log.d("task_db_load", "LOADED F: " + food.getName());
                                }
                            }
                            tasksReady++;
                            setProfileProgress();
                        })
                        .addOnFailureListener(e -> Log.d("db_threads", "Error loading drinks"));
            }
        }.start();

//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//                StorageReference filepath = storageReference.child("foods_db.csv");
//
//                File localFile = null;
//                try {
//                    localFile = File.createTempFile("foods_db", "csv");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                File finalLocalFile = localFile;
//                Log.d("mytag", finalLocalFile.toString());
//                filepath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        try {
//                            Log.d("mytag", "Started loading database!");
//                            Log.d("mytag", finalLocalFile.toString());
//                            BufferedReader bufferedReader = new BufferedReader(new FileReader(finalLocalFile));
//                            bufferedReader.readLine();
//                            String line;
//                            while ((line = bufferedReader.readLine()) != null) {
//                                String[] items = line.split(",");
//                                Food food = new Food();
//                                food.setName(items[0]);
//                                food.setCalories(Integer.parseInt(items[1]));
//                                food.setProteins(Integer.parseInt(items[2]));
//                                food.setLipids(Integer.parseInt(items[3]));
//                                food.setCarbs(Integer.parseInt(items[4]));
//                                food.setFibers(Integer.parseInt(items[5]));
//                                food.setUserId(items[6]);
//                                food.setFoodId(items[7]);
//
//                                foods.add(food);
//                                //Log.d("mytag", food.toString());
//                            }
//                            bufferedReader.close();
//                            if (foods.size() > 0) {
//                                Log.d("mytag", "Successfully loaded database!");
//                                Toast.makeText(MainActivity.this, "FoodDB Loaded!", Toast.LENGTH_SHORT);
//                            } else {
//                                Log.d("mytag", "There've been an error loading the database!");
//                            }
//                        } catch (Exception e) {
//                            Log.d("mytag", "Exception occured: " + e.getMessage());
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Log.d("mytag", "Exception occured: " + exception.getMessage());
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            //DATABASE LOADED SUCCESSFULLY
//                        }
//                    }
//                });
//
//                Log.d("mytag", "Database task finished!");
//            }
//        }.start();
    }

    private void loadUserFoodDrinkRecords() {
        Log.d("mytag", "MainActivity loading records............");

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
                                foodDrinkRecord.setQuantity(snapshot.get("quantity", Integer.class));
                                if (snapshot.getString("recordType").equals("FOOD")) {
                                    foodDrinkRecord.setRecordType(RecordType.FOOD);
                                } else {
                                    foodDrinkRecord.setRecordType(RecordType.DRINK);
                                }

                                foodDrinkRecords.add(foodDrinkRecord);
                                records.add(foodDrinkRecord);
                                Log.d("mytag", "Record was retrieved from firebase successfully!");
                            }
                        }
                    }
                });

    }

    public void editFoodDrinkRecord(FoodDrinkRecord foodDrinkRecord) {
        foodDrinkRecordsReference.document(foodDrinkRecord.getRecordId())
                .update("name", foodDrinkRecord.getName())
                .addOnSuccessListener(unused ->
                        Toast.makeText(MainActivity.this, getText(R.string.record_edited_message), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
    }

    public void editMeasurementRecord(MeasurementRecord measurementRecord) {
        measurementRecordsReference.document(measurementRecord.getRecordId())
                .update("name", measurementRecord.getName())
                .addOnSuccessListener(unused ->
                        Toast.makeText(MainActivity.this, getText(R.string.record_edited_message), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
    }

    public void editRecipeRecord(RecipeRecord recipeRecord) {
        recipeRecordsReference.document(recipeRecord.getRecordId())
                .update("name", recipeRecord.getName())
                .addOnSuccessListener(unused ->
                        Toast.makeText(MainActivity.this, getText(R.string.record_edited_message), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
    }

    public void editPhysicalActivityRecord(PhysicalActivityRecord physicalActivityRecord) {
        physicalActivityRecordsReference.document(physicalActivityRecord.getRecordId())
                .update("name", physicalActivityRecord.getName())
                .addOnSuccessListener(unused ->
                        Toast.makeText(MainActivity.this, getText(R.string.record_edited_message), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
    }

    public void deleteFoodDrinkRecord(FoodDrinkRecord foodDrinkRecord) {
        foodDrinkRecordsReference.document(foodDrinkRecord.getRecordId()).delete()
                .addOnSuccessListener(unused ->
                        Toast.makeText(MainActivity.this, getText(R.string.record_deleted_message), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
        records.remove(foodDrinkRecord);
        foodDrinkRecords.remove(foodDrinkRecord);
    }

    public void deleteMeasurementRecord(MeasurementRecord measurementRecord, RecordType recordType) {
        measurementRecordsReference.document(measurementRecord.getRecordId()).delete()
                .addOnSuccessListener(unused ->
                        Toast.makeText(MainActivity.this, getText(R.string.record_deleted_message), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
        records.remove(measurementRecord);
        measurementRecords.remove(measurementRecord);
        //update user data with last record available
        MeasurementRecord lastRecordAvailable = null;
        for (MeasurementRecord record : measurementRecords) {
            if (recordType == record.getMeasurementCategory()) {
                lastRecordAvailable = record;
            }
        }
        if (lastRecordAvailable != null) {
            if (recordType == RecordType.HEIGHT) {
                usersReference.document(user.getUserId())
                        .update("height", lastRecordAvailable.getValue());
            } else {
                usersReference.document(user.getUserId())
                        .update("weight", lastRecordAvailable.getValue());
            }
        }
    }

    public void deleteRecipeRecord(RecipeRecord recipeRecord) {
        recipeRecordsReference.document(recipeRecord.getRecordId())
                .delete()
                .addOnSuccessListener(unused ->
                        Toast.makeText(MainActivity.this, getText(R.string.record_deleted_message), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
        records.remove(recipeRecord);
        recipeRecords.remove(recipeRecord);
    }

    public void deletePhysicalActivityRecord(PhysicalActivityRecord physicalActivityRecord) {
        physicalActivityRecordsReference.document(physicalActivityRecord.getRecordId())
                .delete()
                .addOnSuccessListener(unused ->
                        Toast.makeText(MainActivity.this, getText(R.string.record_deleted_message), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
        records.remove(physicalActivityRecord);
        physicalActivityRecords.remove(physicalActivityRecord);
    }

    public void editUserCaloriesPlan(User user) {
        this.user = user;
        usersReference.document(user.getUserId())
                .update("caloriesPlan", user.getCaloriesPlan())
                .addOnSuccessListener(unused ->
                        Toast.makeText(MainActivity.this, getText(R.string.user_calories_plan_edited_text), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
    }

    public void editUserData(User user) {
        this.user = user;
        usersReference.document(user.getUserId())
                .update("name", user.getName(),
                        "sex", user.getSex(), "birthdate", user.getBirthdate())
                .addOnSuccessListener(unused ->
                        Toast.makeText(MainActivity.this, getText(R.string.user_data_edited_text), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, getText(R.string.try_again_error_text), Toast.LENGTH_SHORT).show());
    }

    public void setEatenCalories(int value) {
        eatenCalories = value;
    }

    public int getEatenCalories() {
        return eatenCalories;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void addPhysicalActivity(PhysicalActivity physicalActivity) {
        if (physicalActivity != null) {
            physicalActivities.add(physicalActivity);
            physicalActivityReference.document(physicalActivity.getPhysicalActivityId()).set(physicalActivity);
        }
    }

    public void addDrink(Drink drink) {
        if (drink != null) {
            drinks.add(drink);
            drinkReference.document(drink.getDrinkId()).set(drink);
        }
    }

    public void addFood(Food food) {
        if (food != null) {
            foods.add(food);
            foodReference.document(food.getFoodId()).set(food);
        }
    }
}