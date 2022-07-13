package com.udrishh.healthy.utilities;

import com.udrishh.healthy.classes.Food;
import com.udrishh.healthy.classes.PhysicalActivity;
import com.udrishh.healthy.classes.PhysicalActivityRecord;

import java.util.ArrayList;

public interface Finder {
    static PhysicalActivity physicalActivity(ArrayList<PhysicalActivity> list, String id){
        for(PhysicalActivity physicalActivity : list){
            if (physicalActivity.getPhysicalActivityId().equals(id)){
                return physicalActivity;
            }
        }
        return null;
    }
}
