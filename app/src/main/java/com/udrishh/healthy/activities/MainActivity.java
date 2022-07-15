package com.udrishh.healthy.activities;

import androidx.annotation.NonNull;
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
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.fragments.NoInternetFragment;
import com.udrishh.healthy.fragments.SettingsFragment;
import com.udrishh.healthy.fragments.AddFragment;
import com.udrishh.healthy.fragments.ProfileFragment;
import com.udrishh.healthy.fragments.RecipesFragment;
import com.udrishh.healthy.fragments.StatisticsFragment;
import com.udrishh.healthy.utilities.RecordDateDescComparator;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private FragmentManager fragmentManager;
    private String joinDate = "";

    private User user;
    private final ArrayList<Food> foods = new ArrayList<>();
    private final ArrayList<Drink> drinks = new ArrayList<>();
    private final ArrayList<Recipe> recipes = new ArrayList<>();
    private final ArrayList<PhysicalActivity> physicalActivities = new ArrayList<>();
    private final ArrayList<FoodDrinkRecord> foodDrinkRecords = new ArrayList<>();
    private final ArrayList<PhysicalActivityRecord> physicalActivityRecords = new ArrayList<>();
    private final ArrayList<MeasurementRecord> measurementRecords = new ArrayList<>();
    private final ArrayList<RecipeRecord> recipeRecords = new ArrayList<>();
    private final ArrayList<Record> records = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersReference = db.collection("Users");
    private final CollectionReference foodDrinkRecordsReference = db.collection("FoodDrinkRecords");
    private final CollectionReference drinkReference = db.collection("Drinks");
    private final CollectionReference foodReference = db.collection("Foods");
    private final CollectionReference physicalActivityReference = db.collection("PhysicalActivities");
    private final CollectionReference physicalActivityRecordsReference = db.collection("PhysicalActivityRecords");
    private final CollectionReference measurementRecordsReference = db.collection("MeasurementRecords");
    private final CollectionReference recipeRecordsReference = db.collection("RecipeRecords");

    private int eatenCalories;
    private boolean connectionAvailable;
    private boolean profileFragmentSet = false;
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
            user.setHeight(measurementRecord.getValue());
            usersReference.document(user.getUserId())
                    .update("height", measurementRecord.getValue());
        } else {
            user.setWeight(measurementRecord.getValue());
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
        profileFragmentSet = true;
        clearFragmentBackStack();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new ProfileFragment())
                .commit();
    }

    private void setRecipesFragment() {
        profileFragmentSet = false;
        clearFragmentBackStack();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new RecipesFragment())
                .commit();
    }

    private void setAddFragment() {
        profileFragmentSet = false;
        clearFragmentBackStack();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new AddFragment())
                .commit();
    }

    private void setStatisticsFragment() {
        profileFragmentSet = false;
        clearFragmentBackStack();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new StatisticsFragment())
                .commit();
    }

    private void setSettingsFragment() {
        profileFragmentSet = false;
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
        recipeRecordsReference
                .whereEqualTo("userId", user.getUserId())
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }
                    assert queryDocumentSnapshots != null;
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
                        }
                    }
                });
    }

    private void loadMeasurementRecords() {
        measurementRecordsReference
                .whereEqualTo("userId", user.getUserId())
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }
                    assert queryDocumentSnapshots != null;
                    if (!queryDocumentSnapshots.isEmpty() && measurementRecords.isEmpty()) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            if (snapshot != null) {
                                MeasurementRecord measurementRecord = new MeasurementRecord();
                                measurementRecord.setDate(snapshot.getString("date"));
                                measurementRecord.setName(snapshot.getString("name"));
                                measurementRecord.setRecordId(snapshot.getString("recordId"));
                                measurementRecord.setUserId(snapshot.getString("userId"));
                                measurementRecord.setValue(snapshot.get("value", Integer.class));
                                measurementRecord.setInitial(Boolean.TRUE.equals(snapshot.get("initial", Boolean.class)));
                                if (Objects.equals(snapshot.getString("measurementCategory"), "HEIGHT")) {
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
                            }
                        }
                    }
                });
    }

    private void loadPhysicalActivityRecords() {
        physicalActivityRecordsReference
                .whereEqualTo("userId", user.getUserId())
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }
                    assert queryDocumentSnapshots != null;
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
                        }
                    }
                });
    }

    private void setProfileProgress() {
        if (tasksReady >= 2 && profileFragmentSet) {
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

        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        connectivityManager.requestNetwork(networkRequest, networkCallback);


        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("userObject");

        firebaseAuth = FirebaseAuth.getInstance();
        fragmentManager = getSupportFragmentManager();
        uiReady = true;

        if (isNetworkAvailable()) {
            setProfileFragment();
        } else {
            setNoInternetFragment();
        }
        initialiseComponents();
    }

    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
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
                                }
                            }
                            tasksReady++;
                            setProfileProgress();
                        });
            }
        }.start();
    }

    private void loadPhysicalActivities() {
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
                                    }
                                }
                            }
                            tasksReady++;
                            setProfileProgress();
                        });
            }
        }.start();
    }

    private void loadDrinks() {
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
                                }
                            }
                            tasksReady++;
                            setProfileProgress();
                        });
            }
        }.start();
    }

    private void loadFoods() {
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
                                }
                            }
                            tasksReady++;
                            setProfileProgress();
                        });
            }
        }.start();
    }

    private void loadUserFoodDrinkRecords() {
        foodDrinkRecordsReference.whereEqualTo("userId", user.getUserId())
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }
                    assert queryDocumentSnapshots != null;
                    if (!queryDocumentSnapshots.isEmpty() && foodDrinkRecords.isEmpty()) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            FoodDrinkRecord foodDrinkRecord = new FoodDrinkRecord();
                            foodDrinkRecord.setRecordId(snapshot.getString("recordId"));
                            foodDrinkRecord.setUserId(snapshot.getString("userId"));
                            foodDrinkRecord.setItemId(snapshot.getString("itemId"));
                            foodDrinkRecord.setDate(snapshot.getString("date"));
                            foodDrinkRecord.setName(snapshot.getString("name"));
                            foodDrinkRecord.setQuantity(snapshot.get("quantity", Integer.class));
                            if (Objects.equals(snapshot.getString("recordType"), "FOOD")) {
                                foodDrinkRecord.setRecordType(RecordType.FOOD);
                            } else {
                                foodDrinkRecord.setRecordType(RecordType.DRINK);
                            }
                            foodDrinkRecords.add(foodDrinkRecord);
                            records.add(foodDrinkRecord);
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
        measurementRecords.sort(new RecordDateDescComparator());
        for (MeasurementRecord record : measurementRecords) {
            if (recordType == record.getMeasurementCategory()) {
                lastRecordAvailable = record;
            }
        }
        if (lastRecordAvailable != null) {
            if (recordType == RecordType.HEIGHT) {
                user.setHeight(lastRecordAvailable.getValue());
                usersReference.document(user.getUserId())
                        .update("height", lastRecordAvailable.getValue());
            } else {
                user.setWeight(lastRecordAvailable.getValue());
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