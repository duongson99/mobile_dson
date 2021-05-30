package com.appteam.myapplication.model;

import android.annotation.SuppressLint;
import android.net.Uri;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order implements Serializable {
    private int id;
    private String itemName;
    private String datePicker;
    private Double price;
    private float rating;

    public Order() {
    }

    public Order(int id, String itemName, String datePicker, Double price, float rating) {
        this.id = id;
        this.itemName = itemName;
        this.datePicker = datePicker;
        this.price = price;
        this.rating = rating;
    }


    public Order(int id, String itemName, String datePicker, Double price, int rating) {
        this.id = id;
        this.itemName = itemName;
        this.datePicker = datePicker;
        this.price = price;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(Date datePicker) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.datePicker = sdf.format(datePicker);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
