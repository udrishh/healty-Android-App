package com.udrishh.healthy.classes;

public class PhysicalActivityRecord extends Record {
    private String recordId;
    private String userId;
    private String itemId;
    private String name;
    private int quantity;

    public PhysicalActivityRecord() {
        super();
        recordId = "N/A";
        userId = "N/A";
        itemId = "N/A";
        name = "N/A";
        quantity = 0;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return super.getDate();
    }

    public void setDate(String date) {
        super.setDate(date);
    }

}
