package com.cafesio.kitchen.models;

import com.google.firebase.Timestamp;

public class HistoryModel {
    String dateStamp, deliveryStatus, numberOfItems, orderID, orderPrice, orderStatus, uid, uidNumber;
    Timestamp timeStamp;

    public HistoryModel(String dateStamp, String deliveryStatus, String numberOfItems, String orderID, String orderPrice, String orderStatus, String uid, String uidNumber, Timestamp timeStamp) {
        this.dateStamp = dateStamp;
        this.deliveryStatus = deliveryStatus;
        this.numberOfItems = numberOfItems;
        this.orderID = orderID;
        this.orderPrice = orderPrice;
        this.orderStatus = orderStatus;
        this.uid = uid;
        this.uidNumber = uidNumber;
        this.timeStamp = timeStamp;
    }

    public HistoryModel() {
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(String numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
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

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUidNumber() {
        return uidNumber;
    }

    public void setUidNumber(String uidNumber) {
        this.uidNumber = uidNumber;
    }
}