package com.udrishh.healthy.classes;

import com.udrishh.healthy.utilities.DateConverter;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Record {
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Record() {
        date = "N/A";
    }

//    public boolean isBefore(Calendar referenceDate) {
//        Calendar thisCalendar = Calendar.getInstance();
//        thisCalendar.setTime(Objects.requireNonNull(DateConverter.fromLongString(this.date)));
//        return thisCalendar.compareTo(referenceDate) < 0;
//    }
}
