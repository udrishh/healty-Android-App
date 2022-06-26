package com.udrishh.healthy.utilities;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udrishh.healthy.classes.FoodDrinkRecord;
import com.udrishh.healthy.classes.RecipeRecord;
import com.udrishh.healthy.classes.Record;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ValueFormatter implements IAxisValueFormatter {
    private DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd MMM");
    private ArrayList<String> dates;

    public ValueFormatter(String startDate){
        dates = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date1 = LocalDate.parse(startDate, dtf);
        LocalDate date2 = LocalDate.parse(DateConverter.fromDate(new Date()), dtf);
        for(LocalDate date = date1; date.isBefore(date2.plusDays(1)); date = date.plusDays(1)){
            dates.add(date.format(sdf));
        }
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return dates.get((int)value);
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
