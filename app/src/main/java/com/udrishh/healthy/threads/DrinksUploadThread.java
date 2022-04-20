package com.udrishh.healthy.threads;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udrishh.healthy.classes.Drink;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class DrinksUploadThread extends Thread{
    private CollectionReference collectionReference;

    public DrinksUploadThread(FirebaseFirestore db) {
        collectionReference = db.collection("drinks");
    }

    public void run() {
        Log.d("drink_upload", "task started");
        //READ
        try {
            URL url = new URL("https://raw.githubusercontent.com/udrishh/db_files/main/drinks_db.csv");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String line;
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            bufferedReader.readLine();//flush table head
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                count++;
                Drink drink = new Drink();
                String[] items = line.split(",");
                drink.setName(items[0]);
                drink.setCalories(Integer.parseInt(items[1]));
                drink.setProteins(Integer.parseInt(items[2]));
                drink.setLipids(Integer.parseInt(items[3]));
                drink.setCarbs(Integer.parseInt(items[4]));
                drink.setFibers(Integer.parseInt(items[5]));
                drink.setUserId(items[6]);
                drink.setDrinkId(UUID.randomUUID().toString());
                //Log.d("drink_upload", "Read " + count + " out of " + "415 : SUCCESS");
                Log.d("drink_upload", "Read " + count + " out of " + "415 : " + drink.toString());

                //UPLOAD
                int finalCount = count;
                collectionReference.add(drink)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("drink_upload", "UPLOAD " + finalCount + " out of " + "10814 SUCCESS");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("drink_upload", "UPLOAD " + finalCount + " out of " + "10814 FAIL");
                            }
                        });
            }
            Log.d("drink_upload", "FINISHED");
            bufferedReader.close();
        } catch (Exception e) {
            Log.w("drink_upload", "EXCEPTION OCCURRED : " + e.getMessage());
        }
    }
}
