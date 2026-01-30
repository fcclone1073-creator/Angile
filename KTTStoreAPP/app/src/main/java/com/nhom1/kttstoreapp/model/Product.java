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
    private String description;
    @SerializedName("categoryId")
    private String categoryId;

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

    public String getDescription() {
        return description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
