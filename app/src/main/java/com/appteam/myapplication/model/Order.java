package com.appteam.myapplication.model;

import android.annotation.SuppressLint;
import android.net.Uri;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order implements Serializable {
    private String itemName;
    private String datePicker;
    private Long price;
    private Long rating;
    private Long thumbnail;

    public Order(String itemName, String datePicker, Long price, Long rating, Long thumbnail) {
        this.itemName = itemName;
        this.datePicker = datePicker;
        this.price = price;
        this.rating = rating;
        this.thumbnail = thumbnail;
    }

    public Order() {
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public Long getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Long thumbnail) {
        this.thumbnail = thumbnail;
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getRating() {
        return rating;
    }

}
