package com.nhom1.kttstoreapp.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("_id")
    private String id;
    private String name;
    private double price;
    private String image;
    private double rating;
    private boolean isFeatured;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public double getRating() {
        return rating;
    }

    public boolean isFeatured() {
        return isFeatured;
    }
}
