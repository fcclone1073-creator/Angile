package com.nhom1.kttstoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nhom1.kttstoreapp.api.ApiClient;
import com.nhom1.kttstoreapp.api.ApiService;
import com.nhom1.kttstoreapp.model.AuthResponse;
import com.nhom1.kttstoreapp.model.ResetPasswordRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etNewPassword, etEmailReset;
    private Button btnResetPassword;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        email = getIntent().getStringExtra("EMAIL");
        etNewPassword = findViewById(R.id.etNewPassword);
        etEmailReset = findViewById(R.id.etEmailReset);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        if (email != null) {
            etEmailReset.setText(email);
        }

        btnResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String newPass = etNewPassword.getText().toString().trim();
        // Use email from EditText if user changed it? Or stick with Intent?
        // Let's stick with Intent default or update if field is not empty
        String emailInput = etEmailReset.getText().toString().trim();
        if (!emailInput.isEmpty()) {
            email = emailInput;
        }

        if (newPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
            return;
        }

        ResetPasswordRequest request = new ResetPasswordRequest(email, newPass);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<AuthResponse> call = apiService.resetPassword(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "Đặt lại mật khẩu thành công!", Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
