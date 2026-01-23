package com.nhom1.kttstoreapp.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("msg")
    private String message;

    @SerializedName("user")
    private User user;

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
