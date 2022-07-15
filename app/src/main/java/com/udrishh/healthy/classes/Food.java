package com.udrishh.healthy.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Food implements Serializable, Parcelable {
    private String foodId;
    private String name;
    private int calories;
    private int proteins;
    private int lipids;
    private int carbs;
    private int fibers;
    private String userId;

    public Food() {

    }

    protected Food(Parcel in) {
        foodId = in.readString();
        name = in.readString();
        calories = in.readInt();
        proteins = in.readInt();
        lipids = in.readInt();
        carbs = in.readInt();
        fibers = in.readInt();
        userId = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            Food food = new Food();
            food.setFoodId(in.readString());
            food.setName(in.readString());
            food.setCalories(in.readInt());
            food.setProteins(in.readInt());
            food.setLipids(in.readInt());
            food.setCarbs(in.readInt());
            food.setFibers(in.readInt());
            food.setUserId(in.readString());
            return food;
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
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

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodId);
        dest.writeString(name);
        dest.writeInt(calories);
        dest.writeInt(proteins);
        dest.writeInt(lipids);
        dest.writeInt(carbs);
        dest.writeInt(fibers);
        dest.writeString(userId);
    }
}
