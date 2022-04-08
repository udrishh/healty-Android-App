package com.udrishh.healthy.classes;

public class PhysicalActivity {
    private String physicalActivityId;
    private String userId;
    private String name;
    private float calories;

    public PhysicalActivity() {
    }

    public String getPhysicalActivityId() {
        return physicalActivityId;
    }

    public void setPhysicalActivityId(String physicalActivityId) {
        this.physicalActivityId = physicalActivityId;
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

    @Override
    public String toString() {
        return name;
    }
}
