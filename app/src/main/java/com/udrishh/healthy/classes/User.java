package com.udrishh.healthy.classes;

import androidx.annotation.NonNull;

import com.udrishh.healthy.enums.Sex;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String name;
    private String birthdate;
    private Sex sex;
    private int height;
    private int weight;
    private int caloriesPlan;

    public User(String userId, String name, String birthdate, Sex sex, int height, int weight, int caloriesPlan) {
        this.userId = userId;
        this.name = name;
        this.birthdate = birthdate;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.caloriesPlan = caloriesPlan;
    }

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCaloriesPlan() {
        return caloriesPlan;
    }

    public void setCaloriesPlan(int caloriesPlan) {
        this.caloriesPlan = caloriesPlan;
    }

    @NonNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", birthdate=").append(birthdate);
        sb.append(", sex=").append(sex);
        sb.append(", height=").append(height);
        sb.append(", weight=").append(weight);
        sb.append(", caloriesPlan=").append(caloriesPlan);
        sb.append('}');
        return sb.toString();
    }
}
