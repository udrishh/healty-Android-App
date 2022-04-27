package com.udrishh.healthy.classes;

import com.udrishh.healthy.enums.RecordType;

public class MeasurementRecord extends Record {
    private String recordId;
    private String userId;
    private String name;
    private RecordType category;
    private int value;
    private boolean isInitial;

    public MeasurementRecord() {
        super();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MeasurementRecord{");
        sb.append("recordId='").append(recordId).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", category=").append(category);
        sb.append(", date='").append(super.getDate()).append('\'');
        sb.append(", value=").append(value);
        sb.append(", isInitial=").append(isInitial);
        sb.append('}');
        return sb.toString();
    }

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean initial) {
        isInitial = initial;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RecordType getCategory() {
        return category;
    }

    public void setCategory(RecordType category) {
        this.category = category;
    }

    public String getDate() {
        return super.getDate();
    }

    public void setDate(String date) {
        super.setDate(date);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}


