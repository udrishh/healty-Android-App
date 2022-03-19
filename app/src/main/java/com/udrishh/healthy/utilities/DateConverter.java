package com.udrishh.healthy.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public interface DateConverter {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy,", Locale.US);

    static Date fromString(String value) {
        try{
            return formatter.parse(value);
        } catch (ParseException e){
            return null;
        }
    }

    static String fromDate(Date value) {
        return value == null ? null : formatter.format(value);
    }
}
