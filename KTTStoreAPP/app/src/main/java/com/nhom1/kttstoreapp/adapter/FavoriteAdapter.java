package com.nhom1.kttstoreapp.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private List<Product> favoriteList;

    public FavoriteAdapter(Context context, List<Product> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_product, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Product product = favoriteList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage, ivDelete, ivSaveNote;
        TextView tvProductName, tvProductPrice, tvOriginalPrice;
        EditText etNote;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivSaveNote = itemView.findViewById(R.id.ivSaveNote);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            etNote = itemView.findViewById(R.id.etNote);

            ivDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    favoriteList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                }
            });

            ivSaveNote.setOnClickListener(v -> {
                String note = etNote.getText().toString();
                Toast.makeText(context, "Đã lưu ghi chú: " + note, Toast.LENGTH_SHORT).show();
                // In real app, save to DB
            });
        }

        public void bind(Product product) {
            tvProductName.setText(product.getName());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,### đ");
            tvProductPrice.setText(decimalFormat.format(product.getPrice()));

            // Mock original price
            tvOriginalPrice.setText(decimalFormat.format(product.getPrice() * 1.5));
            tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            Glide.with(context)
                    .load(product.getImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(ivProductImage);
        }
    }
}
