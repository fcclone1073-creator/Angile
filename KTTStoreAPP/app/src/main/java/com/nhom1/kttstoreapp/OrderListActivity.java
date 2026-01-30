package com.nhom1.kttstoreapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.adapter.OrderAdapter;
import com.nhom1.kttstoreapp.api.ApiClient;
import com.nhom1.kttstoreapp.api.ApiService;
import com.nhom1.kttstoreapp.model.Order;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;
    private ImageView ivBack;
    private Spinner spinnerStatus;

    private List<Order> allOrders = new ArrayList<>();
    private String selectedStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        rvOrders = findViewById(R.id.rvOrders);
        ivBack = findViewById(R.id.ivBack);
        spinnerStatus = findViewById(R.id.spinnerStatus);

        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(this, new java.util.ArrayList<>());
        rvOrders.setAdapter(orderAdapter);

        ivBack.setOnClickListener(v -> finish());

        setupStatusFilter();
        loadOrders();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    private void setupStatusFilter() {
        String[] statusLabels = {
                "Tất cả trạng thái",
                "Chờ xác nhận",
                "Đã xác nhận",
                "Đang chuẩn bị",
                "Đang giao hàng",
                "Đã giao hàng",
                "Đã hủy"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statusLabels
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        spinnerStatus.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectedStatus = "";
                        break;
                    case 1:
                        selectedStatus = "pending";
                        break;
                    case 2:
                        selectedStatus = "confirmed";
                        break;
                    case 3:
                        selectedStatus = "preparing";
                        break;
                    case 4:
                        selectedStatus = "shipping";
                        break;
                    case 5:
                        selectedStatus = "delivered";
                        break;
                    case 6:
                        selectedStatus = "cancelled";
                        break;
                }
                filterOrders();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // No-op
            }
        });
    }

    private void loadOrders() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allOrders = response.body();
                    filterOrders();
                } else {
                    Toast.makeText(OrderListActivity.this, "Lỗi tải danh sách đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(OrderListActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterOrders() {
        if (allOrders == null) {
            return;
        }

        List<Order> filtered = new ArrayList<>();
        for (Order order : allOrders) {
            if (selectedStatus.isEmpty() || selectedStatus.equals(order.getStatus())) {
                filtered.add(order);
            }
        }
        orderAdapter.updateOrders(filtered);
    }
}
