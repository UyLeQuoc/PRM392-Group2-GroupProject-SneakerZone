package com.group2.prm392_group2_sneakerzone.model;

public class OrderDetail {
    private int orderDetailId;
    private int orderId;
    private int productSizeId;
    private int quantity;
    private double unitUnitPrice;

    public OrderDetail(int orderDetailId, int orderId, int productSizeId, int quantity, double unitUnitPrice) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.productSizeId = productSizeId;
        this.quantity = quantity;
        this.unitUnitPrice = unitUnitPrice;
    }

    // Getter và Setter
    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(int productSizeId) {
        this.productSizeId = productSizeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitUnitPrice;
    }

    public void setUnitPrice(double unitUnitPrice) {
        this.unitUnitPrice = unitUnitPrice;
    }
}

