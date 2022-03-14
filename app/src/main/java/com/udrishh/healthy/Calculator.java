package com.udrishh.healthy;

public interface Calculator {
    public static int BMR(int weight, int height, int age, Sex sex, ActivityLevel activityLevel, GainLose gainLose) {
        double bmr = 1200;

        switch (sex) {
            case MALE:
                bmr = 88.362 + 13.397 * weight + 4.799 * height - 5.677 * age;
                break;
            case FEMALE:
                bmr = 447.593 + 9.247 * weight + 3.098 * height - 4.33 * age;
                break;
        }

        switch (activityLevel) {
            case NONE:
                bmr *= 1.2;
                break;
            case LOW:
                bmr *= 1.375;
                break;
            case MEDIUM:
                bmr *= 1.55;
                break;
            case HIGH:
                bmr *= 1.725;
                break;
            case EXTREME:
                bmr *= 1.9;
        }

        switch (gainLose) {
            case LOSE1KG:
                bmr += -1100;
                break;
            case LOSE075KG:
                bmr += -825;
                break;
            case LOSE050KG:
                bmr += -550;
                break;
            case LOSE025KG:
                bmr += -275;
                break;
            case NONE:
                bmr += 0;
                break;
            case GAIN025KG:
                bmr += 275;
                break;
            case GAIN050KG:
                bmr += 550;
                break;
            case GAIN075KG:
                bmr += 825;
                break;
            case GAIN1KG:
                bmr += 1100;
                break;
        }

        return Math.max((int) bmr, 1200);
    }
}
