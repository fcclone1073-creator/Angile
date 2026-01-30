package com.nhom1.kttstoreapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.model.CartItem;
import com.nhom1.kttstoreapp.util.CartManager;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateCartItems(List<CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvQuantity;
        Button btnDecrease;
        Button btnIncrease;
        ImageView ivDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }

        public void bind(CartItem cartItem) {
            tvProductName.setText(cartItem.getProduct().getName());

            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvProductPrice.setText(format.format(cartItem.getProduct().getPrice()));

            tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

            Glide.with(context).load(cartItem.getProduct().getImage()).into(ivProductImage);

            btnDecrease.setOnClickListener(v -> {
                CartManager.getInstance().updateQuantity(
                    cartItem.getProduct().getId(),
                    cartItem.getQuantity() - 1
                );
                if (listener != null) {
                    listener.onCartChanged();
                }
            });

            btnIncrease.setOnClickListener(v -> {
                CartManager.getInstance().updateQuantity(
                    cartItem.getProduct().getId(),
                    cartItem.getQuantity() + 1
                );
                if (listener != null) {
                    listener.onCartChanged();
                }
            });

            ivDelete.setOnClickListener(v -> {
                CartManager.getInstance().removeProduct(cartItem.getProduct().getId());
                if (listener != null) {
                    listener.onCartChanged();
                }
            });
        }
    }
}
