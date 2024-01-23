package com.cafesio.kitchen.models;

public class OrderModel {
    String deliveryStatus, orderID, orderStatus, uid, uidNumber;

    public OrderModel(String deliveryStatus, String orderID, String orderStatus, String uid, String uidNumber) {
        this.deliveryStatus = deliveryStatus;
        this.orderID = orderID;
        this.orderStatus = orderStatus;
        this.uid = uid;
        this.uidNumber = uidNumber;
    }

    public OrderModel() {}

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUidNumber() {
        return uidNumber;
    }

    public void setUidNumber(String uidNumber) {
        this.uidNumber = uidNumber;
    }
}
