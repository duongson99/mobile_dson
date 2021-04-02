package com.appteam.myapplication;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;

public class Job implements Serializable {
    private String name;
    private double salary;
    private Uri image;
    private Date date_created;
    private boolean activated;

    public Job() {
    }

    public Job(String name, double salary, Date date_created, boolean activated) {
        this.name = name;
        this.salary = salary;
        this.date_created = date_created;
        this.activated = activated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
