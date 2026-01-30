package com.nhom1.kttstoreapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhom1.kttstoreapp.ProductDetailActivity;
import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.model.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvPrice.setText(format.format(product.getPrice()));

        holder.tvRating.setText(String.valueOf(product.getRating()));

        Glide.with(context).load(product.getImage()).into(holder.ivImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.getId());
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_NAME, product.getName());
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_PRICE, product.getPrice());
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_IMAGE, product.getImage());
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_RATING, product.getRating());
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_DESCRIPTION, product.getDescription());
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_CATEGORY_ID, product.getCategoryId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice, tvRating;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivProductImage);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvRating = itemView.findViewById(R.id.tvProductRating);
        }
    }
}
