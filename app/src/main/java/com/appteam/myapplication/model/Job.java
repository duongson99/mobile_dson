package com.appteam.myapplication.model;

import android.net.Uri;

import java.io.Serializable;

public class Job implements Serializable {
    private Uri image;
    private String name;
    private double salary;
    private String dateCreated;
    private boolean activated;

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Job(Uri image, String name, double salary, String dateCreated, boolean activated) {
        this.image = image;
        this.name = name;
        this.salary = salary;
        this.dateCreated = dateCreated;
        this.activated = activated;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "Job{" +
                "image=" + image +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", dateCreated='" + dateCreated + '\'' +
                ", activated=" + activated +
                '}';
    }
}
