package com.nhom1.kttstoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom1.kttstoreapp.api.ApiClient;
import com.nhom1.kttstoreapp.api.ApiService;
import com.nhom1.kttstoreapp.model.AuthResponse;
import com.nhom1.kttstoreapp.model.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private ImageView ivShowPassword;
    private Button btnLogin;
    private TextView tvRegister, tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check Keep Login
        android.content.SharedPreferences prefs = getSharedPreferences("KTTStorePrefs", MODE_PRIVATE);
        if (prefs.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        ivShowPassword = findViewById(R.id.ivShowPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnLogin.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        ivShowPassword.setOnClickListener(v -> {
            if (etPassword.getTransformationMethod()
                    .equals(android.text.method.PasswordTransformationMethod.getInstance())) {
                // Show Password
                etPassword.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
                // Optional: Change icon if you have an 'eye-off' icon
                // ivShowPassword.setImageResource(R.drawable.ic_eye_off);
            } else {
                // Hide Password
                etPassword.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                ivShowPassword.setImageResource(android.R.drawable.ic_menu_view);
            }
            // Move cursor to end
            etPassword.setSelection(etPassword.getText().length());
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        LoginRequest request = new LoginRequest(email, password);

        Call<AuthResponse> call = apiService.loginUser(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Save Login State
                    android.content.SharedPreferences prefs = getSharedPreferences("KTTStorePrefs", MODE_PRIVATE);
                    android.content.SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("userEmail", email);
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công: " + response.body().getUser().getName(),
                            Toast.LENGTH_SHORT).show();
                    // Navigate to Home/Main Activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + response.message(), Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                String errorMsg = "Lỗi kết nối: " + t.getMessage();
                if (t.getCause() != null) {
                    errorMsg += "\nChi tiết: " + t.getCause().getMessage();
                }
                Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                // Log to console for debugging
                android.util.Log.e("LoginActivity", "Connection failed", t);
            }
        });
    }
}
