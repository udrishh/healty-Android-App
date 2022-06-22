package com.udrishh.healthy.threads;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udrishh.healthy.classes.Food;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class FoodsLoaderThread implements Callable<List<Food>> {
    Context context;

    public FoodsLoaderThread(Context context) {
        this.context = context;
    }

    @Override
    public List<Food> call() throws Exception {
        List<Food> foods = new ArrayList<>();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference filepath = storageReference.child("foods_db.csv");
        File localFile = File.createTempFile("foods_db", "csv");

        filepath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                try {
                    Log.d("mytag", localFile.toString());
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(localFile));
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
                    }
                    bufferedReader.close();
                    Log.d("mytag", "success");
                } catch (Exception e) {
                    Log.d("mytag", e.getMessage());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("mytag", exception.getMessage());
            }
        });
        return foods;
    }
}
