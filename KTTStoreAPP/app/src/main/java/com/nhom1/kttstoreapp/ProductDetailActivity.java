package com.nhom1.kttstoreapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nhom1.kttstoreapp.model.Product;
import com.nhom1.kttstoreapp.util.CartManager;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";
    public static final String EXTRA_PRODUCT_NAME = "extra_product_name";
    public static final String EXTRA_PRODUCT_PRICE = "extra_product_price";
    public static final String EXTRA_PRODUCT_IMAGE = "extra_product_image";
    public static final String EXTRA_PRODUCT_RATING = "extra_product_rating";
    public static final String EXTRA_PRODUCT_DESCRIPTION = "extra_product_description";
    public static final String EXTRA_PRODUCT_CATEGORY_ID = "extra_product_category_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageView ivProductImage = findViewById(R.id.ivProductImage);
        TextView tvProductName = findViewById(R.id.tvProductName);
        TextView tvProductPrice = findViewById(R.id.tvProductPrice);
        TextView tvProductRating = findViewById(R.id.tvProductRating);
        TextView tvProductDescription = findViewById(R.id.tvProductDescription);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);

        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        String name = getIntent().getStringExtra(EXTRA_PRODUCT_NAME);
        double price = getIntent().getDoubleExtra(EXTRA_PRODUCT_PRICE, 0);
        String image = getIntent().getStringExtra(EXTRA_PRODUCT_IMAGE);
        double rating = getIntent().getDoubleExtra(EXTRA_PRODUCT_RATING, 0);
        String description = getIntent().getStringExtra(EXTRA_PRODUCT_DESCRIPTION);
        String categoryId = getIntent().getStringExtra(EXTRA_PRODUCT_CATEGORY_ID);

        // Create Product object from intent data
        Product product = new Product();
        product.setId(productId);
        product.setName(name);
        product.setPrice(price);
        product.setImage(image);
        product.setRating(rating);
        product.setDescription(description);
        product.setCategoryId(categoryId);

        tvProductName.setText(name);

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvProductPrice.setText(format.format(price));

        tvProductRating.setText(String.valueOf(rating));

        if (description != null && !description.isEmpty()) {
            tvProductDescription.setText(description);
        }

        Glide.with(this).load(image).into(ivProductImage);

        btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addProduct(product);
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }
}

