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
import com.nhom1.kttstoreapp.model.VerifyOtpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpActivity extends AppCompatActivity {

    private EditText etOtp;
    private Button btnVerifyOtp;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        email = getIntent().getStringExtra("EMAIL");
        etOtp = findViewById(R.id.etOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);

        btnVerifyOtp.setOnClickListener(v -> verifyOtp());
    }

    private void verifyOtp() {
        String otp = etOtp.getText().toString().trim();
        if (otp.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        VerifyOtpRequest request = new VerifyOtpRequest(email, otp);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<AuthResponse> call = apiService.verifyOtp(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VerifyOtpActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VerifyOtpActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("EMAIL", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(VerifyOtpActivity.this, "Mã OTP không chính xác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(VerifyOtpActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
