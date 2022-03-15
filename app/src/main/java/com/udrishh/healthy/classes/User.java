package com.udrishh.healthy.classes;

import com.udrishh.healthy.enums.Sex;

import java.util.Date;

public class User {
    private long id;
    private String email;
    private String password;
    private String name;
    private Date birthdate;
    private Sex sex;
    private float height;
    private float weight;
    private int caloriesPlan;
    private float targetWeight;

    public User(long id, String email, String password, String name, Date birthdate, Sex sex, float height, float weight, int caloriesPlan, float targetWeight) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthdate = birthdate;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.caloriesPlan = caloriesPlan;
        this.targetWeight = targetWeight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getCaloriesPlan() {
        return caloriesPlan;
    }

    public void setCaloriesPlan(int caloriesPlan) {
        this.caloriesPlan = caloriesPlan;
    }

    public float getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(float targetWeight) {
        this.targetWeight = targetWeight;
    }
}
