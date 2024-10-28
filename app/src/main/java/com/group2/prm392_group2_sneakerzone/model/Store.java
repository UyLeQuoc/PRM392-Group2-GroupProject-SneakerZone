package com.group2.prm392_group2_sneakerzone.model;

public class Store {
    private int storeId;
    private String storeName;
    private String storeImage;
    private String location;
    private int ownerId;

    public Store(int storeId, String storeName, String storeImage, String location, int ownerId) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeImage = storeImage;
        this.location = location;
        this.ownerId = ownerId;
    }

    // Getters and Setters
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}