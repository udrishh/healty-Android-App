package com.udrishh.healthy.threads;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udrishh.healthy.classes.Drink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class DrinksLoaderThread implements Callable<List<Drink>> {
    Context context;

    public DrinksLoaderThread(Context context) {
        this.context = context;
    }

    @Override
    public List<Drink> call() throws Exception {
        List<Drink> drinks = new ArrayList<>();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference filepath = storageReference.child("drinks_db.csv");
        File localFile = File.createTempFile("drinks_db", "csv");

        filepath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            try {
                Log.d("mytag", localFile.toString());
                BufferedReader bufferedReader = new BufferedReader(new FileReader(localFile));
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
                }
                bufferedReader.close();
                Log.d("mytag", "success");
            } catch (Exception e) {
                Log.d("mytag", e.getMessage());
            }
        }).addOnFailureListener(exception -> Log.d("mytag", exception.getMessage()));
        return drinks;
    }
}
