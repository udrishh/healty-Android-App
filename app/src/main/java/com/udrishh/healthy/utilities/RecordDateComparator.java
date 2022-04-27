package com.udrishh.healthy.utilities;

import com.udrishh.healthy.classes.Record;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Objects;

public class RecordDateComparator implements Comparator<Record> {
    public RecordDateComparator() {
    }

    @Override
    public int compare(Record o1, Record o2) {
        if (o1 != null && o2 != null) {
            Calendar o1Calendar = Calendar.getInstance();
            o1Calendar.setTime(Objects.requireNonNull(DateConverter.fromLongString(o1.getDate())));
            Calendar o2Calendar = Calendar.getInstance();
            o2Calendar.setTime(Objects.requireNonNull(DateConverter.fromLongString(o2.getDate())));
            return -((o1Calendar).compareTo(o2Calendar));
        } else return 0;
    }
}
