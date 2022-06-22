package com.udrishh.healthy.threads;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.udrishh.healthy.activities.LoginActivity;
import com.udrishh.healthy.classes.Food;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FoodsDownloadThread extends Thread{
    private CollectionReference collectionReference;
    private SharedPreferences sharedPreferences;
    private List<Food> foodsList= new ArrayList<>();

    public FoodsDownloadThread(FirebaseFirestore db, SharedPreferences sharedPreferences) {
        collectionReference = db.collection("Foods");
        this.sharedPreferences = sharedPreferences;
    }

    public void run() {
        Log.d("mytag", "task started");
        //READ
        try {
        boolean isAvailable = sharedPreferences.getBoolean("databaseAvailable", false);
        isAvailable = true;
        if(isAvailable){
            //GET

        } else {
            //PUT
        }

            final int[] count = {0};
            collectionReference.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            if(!queryDocumentSnapshots.isEmpty()){
                                for(QueryDocumentSnapshot foods : queryDocumentSnapshots){
                                    Food food = foods.toObject(Food.class);
                                    foodsList.add(food);
                                    count[0]++;
                                }
                            }
                            Log.d("mytag", "DOWNLOAD " + count[0] + " out of " + "10814 SUCCESS");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("mytag", "DOWNLOAD " + count[0] + " out of " + "10814 FAIL");
                        }
                    });

            //WRITE ON SHARED PREFS
        } catch (Exception e) {
            Log.w("mytag", "EXCEPTION OCCURRED : " + e.getMessage());
        }
    }
}
