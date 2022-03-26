package com.udrishh.healthy.threads;

import android.os.Environment;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udrishh.healthy.classes.Food;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class FoodsUploadThread extends Thread {
    private CollectionReference collectionReference;

    public FoodsUploadThread(FirebaseFirestore db) {
        collectionReference = db.collection("Foods");
    }

    public void run() {
        Log.d("food_upload", "task started");
        //READ
        try {
            URL url = new URL("https://raw.githubusercontent.com/udrishh/db_files/main/foods_db.csv");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String line;
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            bufferedReader.readLine();//flush table head
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                count++;
                Food food = new Food();
                String[] items = line.split(",");
                food.setName(items[0]);
                food.setCalories(Integer.parseInt(items[1]));
                food.setProteins(Integer.parseInt(items[2]));
                food.setLipids(Integer.parseInt(items[3]));
                food.setCarbs(Integer.parseInt(items[4]));
                food.setFibers(Integer.parseInt(items[5]));
                food.setUserId(items[6]);
                food.setFoodId(UUID.randomUUID().toString());
                //Log.d("food_upload", "Read " + count + " out of " + "10814 : SUCCESS");
                Log.d("food_upload", "Read " + count + " out of " + "10814 : " + food.toString());

                //UPLOAD
//                int finalCount = count;
//                collectionReference.add(food)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.d("food_upload", "UPLOAD " + finalCount + " out of " + "10814 SUCCESS");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d("food_upload", "UPLOAD " + finalCount + " out of " + "10814 FAIL");
//                            }
//                        });
            }
            Log.d("food_upload", "FINISHED");
            bufferedReader.close();
        } catch (Exception e) {
            Log.w("food_upload", "EXCEPTION OCCURRED : " + e.getMessage());
        }
    }
}
