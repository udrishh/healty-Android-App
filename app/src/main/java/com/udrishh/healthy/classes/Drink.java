package com.udrishh.healthy.classes;

import androidx.annotation.NonNull;

public class Drink {
    private String drinkId;
    private String name;
    private int calories;
    private int proteins;
    private int lipids;
    private int carbs;
    private int fibers;
    private String userId;

    public Drink() {

    }

    public String getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(String drinkId) {
        this.drinkId = drinkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProteins() {
        return proteins;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public int getLipids() {
        return lipids;
    }

    public void setLipids(int lipids) {
        this.lipids = lipids;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFibers() {
        return fibers;
    }

    public void setFibers(int fibers) {
        this.fibers = fibers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return name;
    }
}
