package com.nhom1.kttstoreapp;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.nhom1.kttstoreapp.model.Product;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage, ivFavorite;
    private TextView tvProductName, tvProductPrice, tvOriginalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ivProductImage = findViewById(R.id.ivProductImage);
        ivFavorite = findViewById(R.id.ivFavorite);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvOriginalPrice = findViewById(R.id.tvOriginalPrice);

        Product product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            displayProductDetails(product);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }

        ivFavorite.setOnClickListener(v -> {
            boolean currentState = product != null && product.isFavorite();
            if (product != null)
                product.setFavorite(!currentState);
            updateFavoriteIcon(!currentState);
            Toast.makeText(this, !currentState ? "Đã thêm vào yêu thích" : "Đã bỏ yêu thích", Toast.LENGTH_SHORT)
                    .show();
        });

        findViewById(R.id.btnAddToCart).setOnClickListener(v -> {
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnBuyNow).setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng mua ngay đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void displayProductDetails(Product product) {
        tvProductName.setText(product.getName());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,### đ");
        tvProductPrice.setText(decimalFormat.format(product.getPrice()));

        // Mock original price for demo (price * 1.5)
        double originalPrice = product.getPrice() * 1.5;
        tvOriginalPrice.setText(decimalFormat.format(originalPrice));
        tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Glide.with(this)
                .load(product.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(ivProductImage);

        updateFavoriteIcon(product.isFavorite());
    }

    private void updateFavoriteIcon(boolean isFavorite) {
        if (isFavorite) {
            ivFavorite.setImageResource(R.drawable.ic_favorite_red);
        } else {
            ivFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
