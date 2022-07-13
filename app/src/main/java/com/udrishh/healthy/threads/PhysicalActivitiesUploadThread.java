package com.udrishh.healthy.threads;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udrishh.healthy.classes.PhysicalActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhysicalActivitiesUploadThread extends Thread{
    private CollectionReference collectionReference;

    public PhysicalActivitiesUploadThread(FirebaseFirestore db) {
        collectionReference = db.collection("PhysicalActivities");
    }

    public void run() {
        Log.d("activities_upload", "task started");
        //READ
        try {
            URL url = new URL("https://raw.githubusercontent.com/udrishh/db_files/main/activities_db.csv");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String line;
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            bufferedReader.readLine();//flush table head
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                count++;
                PhysicalActivity physicalActivity = new PhysicalActivity();
                String[] items = line.split(",");
                physicalActivity.setName(items[0]);
                physicalActivity.setCalories(Float.parseFloat(items[1]));
                physicalActivity.setPhysicalActivityId(items[2]);
                physicalActivity.setUserId(items[3]);
                Log.d("activities_upload", "Read " + count + " out of " + "415 : " + physicalActivity.toString());

                //UPLOAD
                int finalCount = count;
                collectionReference.document(physicalActivity.getPhysicalActivityId()).set(physicalActivity)
                        .addOnSuccessListener(documentReference ->
                                Log.d("activities_upload", "UPLOAD " + finalCount + " out of " + "10814 SUCCESS"))
                        .addOnFailureListener(e ->
                                Log.d("activities_upload", "UPLOAD " + finalCount + " out of " + "10814 FAIL"));
            }
            Log.d("activities_upload", "FINISHED");
            bufferedReader.close();
        } catch (Exception e) {
            Log.w("activities_upload", "EXCEPTION OCCURRED : " + e.getMessage());
        }
    }
}
