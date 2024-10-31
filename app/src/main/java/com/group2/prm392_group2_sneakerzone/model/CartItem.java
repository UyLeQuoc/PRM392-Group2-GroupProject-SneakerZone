package com.group2.prm392_group2_sneakerzone.model;

public class CartItem {
    private Product product;
    private String size;
    private int quantity;

    public CartItem(Product product, String size, int quantity) {
        this.product = product;
        this.size = size;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public String getSize() {
        return size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
