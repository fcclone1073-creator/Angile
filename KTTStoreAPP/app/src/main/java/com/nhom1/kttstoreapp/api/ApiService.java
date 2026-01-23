package com.nhom1.kttstoreapp.api;

import com.nhom1.kttstoreapp.model.AuthResponse;
import com.nhom1.kttstoreapp.model.ForgotPasswordRequest;
import com.nhom1.kttstoreapp.model.LoginRequest;
import com.nhom1.kttstoreapp.model.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/register")
    Call<AuthResponse> registerUser(@Body RegisterRequest registerRequest);

    @POST("auth/login")
    Call<AuthResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("auth/forgot-password")
    Call<AuthResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @POST("auth/verify-otp")
    Call<AuthResponse> verifyOtp(@Body com.nhom1.kttstoreapp.model.VerifyOtpRequest verifyOtpRequest);

    @POST("auth/reset-password")
    Call<AuthResponse> resetPassword(@Body com.nhom1.kttstoreapp.model.ResetPasswordRequest resetPasswordRequest);

    @retrofit2.http.GET("products")
    Call<java.util.List<com.nhom1.kttstoreapp.model.Product>> getProducts();

    @retrofit2.http.GET("products/featured")
    Call<java.util.List<com.nhom1.kttstoreapp.model.Product>> getFeaturedProducts();

    @retrofit2.http.GET("products/categories")
    Call<java.util.List<com.nhom1.kttstoreapp.model.Category>> getCategories();
}
