package com.nhom1.kttstoreapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.adapter.CartAdapter;
import com.nhom1.kttstoreapp.model.CartItem;
import com.nhom1.kttstoreapp.util.CartManager;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.OnCartChangeListener {

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private CartAdapter cartAdapter;
    private CartManager cartManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCartItems = view.findViewById(R.id.rvCartItems);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        cartManager = CartManager.getInstance();

        rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(getContext(), cartManager.getCartItems(), this);
        rvCartItems.setAdapter(cartAdapter);

        btnCheckout.setOnClickListener(v -> {
            if (cartManager.isEmpty()) {
                Toast.makeText(getContext(), "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Chức năng đặt hàng sẽ được triển khai sau", Toast.LENGTH_SHORT).show();
                // TODO: Implement checkout functionality
            }
        });

        updateTotalPrice();
        updateCartItems();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCartItems();
        updateTotalPrice();
    }

    @Override
    public void onCartChanged() {
        updateCartItems();
        updateTotalPrice();
    }

    private void updateCartItems() {
        List<CartItem> items = cartManager.getCartItems();
        cartAdapter.updateCartItems(items);
    }

    private void updateTotalPrice() {
        double total = cartManager.getTotalPrice();
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText(format.format(total));
    }
}
