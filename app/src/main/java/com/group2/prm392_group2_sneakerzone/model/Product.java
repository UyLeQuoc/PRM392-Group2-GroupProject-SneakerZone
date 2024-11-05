package com.group2.prm392_group2_sneakerzone.model;

public class Product {
    private int productId;
    private String productName;
    private String prod uctImage;
    private int brandId;
    private int storeId;
    private double price;
    private String description;
    private String createdDate;
    private String updatedDate;

    public Product(int productId, String productName, String productImage, int brandId, int storeId, double price, String description, String createdDate, String updatedDate) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.brandId = brandId;
        this.storeId = storeId;
        this.price = price;
        this.description = description;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
