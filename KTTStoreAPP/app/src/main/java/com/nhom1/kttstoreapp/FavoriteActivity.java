package com.nhom1.kttstoreapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.adapter.FavoriteAdapter;
import com.nhom1.kttstoreapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView rvFavorites;
    private FavoriteAdapter favoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        rvFavorites = findViewById(R.id.rvFavorites);
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));

        // Mock data
        List<Product> favoriteProducts = getMockFavoriteProducts();
        favoriteAdapter = new FavoriteAdapter(this, favoriteProducts);
        rvFavorites.setAdapter(favoriteAdapter);
    }

    private List<Product> getMockFavoriteProducts() {
        List<Product> list = new ArrayList<>();
        // In real app, fetch from DB

        // Mock items based on user image
        list.add(createMockProduct("1", "Áo thun polo dài tay", 0,
                "https://img.freepik.com/free-photo/handsome-man-wearing-long-sleeve-shirt_23-2148786756.jpg"));
        list.add(createMockProduct("2", "Áo Polo Modal Slim Fit phối cổ", 0,
                "https://img.freepik.com/free-photo/polo-shirt-mockup_1308-36427.jpg"));
        list.add(createMockProduct("3", "Prime Polo - Áo Polo Modal Slim Fit", 0,
                "https://img.freepik.com/free-photo/man-wearing-white-polo-shirt_53876-120610.jpg"));
        list.add(createMockProduct("4", "đầm xẻ tà xếp ly vai", 1113000,
                "https://img.freepik.com/free-photo/elegant-woman-dress_144627-14765.jpg"));

        return list;
    }

    private Product createMockProduct(String id, String name, double price, String image) {
        Product p = new Product();
        // Since we don't have setters for everything in the model shown earlier, we
        // rely on Gson or reflection usually,
        // but let's see if we can instantiate it cleanly. The ID field was private
        // without setter in previous view.
        // Assuming we can just use what's available or JSON parsing.
        // For simplicity in this Task, I will use reflection or just assume we modify
        // Product to have setters or constructor.
        // Wait, I can try to set fields if they are accessible or add setters if
        // needed.
        // Let's check Product.java again. It has no setters for name/price/image.
        // I will update Product.java to include setters for this mock data to work
        // easier.
        return new com.google.gson.Gson().fromJson("{\"_id\":\"" + id + "\", \"name\":\"" + name + "\", \"price\":"
                + price + ", \"image\":\"" + image + "\"}", Product.class);
    }
}
