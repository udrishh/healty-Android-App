package com.udrishh.healthy.classes;

import com.udrishh.healthy.enums.RecordType;

public class FoodDrinkRecord extends Record{
    private String recordId;
    private String userId;
    private String itemId;
    private RecordType category;
    private String name;
    private int calories;
    private int proteins;
    private int lipids;
    private int carbs;
    private int fibers;
    private int quantity;
    private int totalCalories;
    private int totalLipids;
    private int totalCarbs;
    private int totalFibers;
    private int totalProteins;
    private String Date;

    public FoodDrinkRecord() {
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FoodDrinkRecord{");
        sb.append("recordId='").append(recordId).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", itemId='").append(itemId).append('\'');
        sb.append(", category=").append(category);
        sb.append(", name='").append(name).append('\'');
        sb.append(", calories=").append(calories);
        sb.append(", proteins=").append(proteins);
        sb.append(", lipids=").append(lipids);
        sb.append(", carbs=").append(carbs);
        sb.append(", fibers=").append(fibers);
        sb.append(", quantity=").append(quantity);
        sb.append(", totalCalories=").append(totalCalories);
        sb.append(", totalLipids=").append(totalLipids);
        sb.append(", totalCarbs=").append(totalCarbs);
        sb.append(", totalFibers=").append(totalFibers);
        sb.append(", totalProteins=").append(totalProteins);
        sb.append(", Date='").append(Date).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public RecordType getCategory() {
        return category;
    }

    public void setCategory(RecordType category) {
        this.category = category;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    public int getTotalLipids() {
        return totalLipids;
    }

    public void setTotalLipids(int totalLipids) {
        this.totalLipids = totalLipids;
    }

    public int getTotalCarbs() {
        return totalCarbs;
    }

    public void setTotalCarbs(int totalCarbs) {
        this.totalCarbs = totalCarbs;
    }

    public int getTotalFibers() {
        return totalFibers;
    }

    public void setTotalFibers(int totalFibers) {
        this.totalFibers = totalFibers;
    }

    public int getTotalProteins() {
        return totalProteins;
    }

    public void setTotalProteins(int totalProteins) {
        this.totalProteins = totalProteins;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}