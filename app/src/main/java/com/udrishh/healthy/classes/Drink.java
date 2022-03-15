package com.udrishh.healthy.classes;

import androidx.annotation.NonNull;

public class Drink {
    private String name;
    private float calories;
    private float proteins;
    private float lipids;
    private float carbs;
    private float fibers;

    public Drink(String name, float calories, float proteins, float lipids, float carbs, float fibers) {
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.lipids = lipids;
        this.carbs = carbs;
        this.fibers = fibers;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getProteins() {
        return proteins;
    }

    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    public float getLipids() {
        return lipids;
    }

    public void setLipids(float lipids) {
        this.lipids = lipids;
    }

    public float getCarbs() {
        return carbs;
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }

    public float getFibers() {
        return fibers;
    }

    public void setFibers(float fibers) {
        this.fibers = fibers;
    }

    @NonNull
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Drink{");
        sb.append("name='").append(name).append('\'');
        sb.append(", calories=").append(calories);
        sb.append(", proteins=").append(proteins);
        sb.append(", lipids=").append(lipids);
        sb.append(", carbs=").append(carbs);
        sb.append(", fibers=").append(fibers);
        sb.append('}');
        return sb.toString();
    }
}
