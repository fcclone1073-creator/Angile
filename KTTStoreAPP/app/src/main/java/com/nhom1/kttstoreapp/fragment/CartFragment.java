package com.nhom1.kttstoreapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.nhom1.kttstoreapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.CartItemListener {

    private RecyclerView rvCartItems;
    private CartAdapter adapter;
    private List<CartItem> cartList;
    private TextView tvAddress;
    private TextView tvUserName, tvUserPhone, tvUserEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        rvCartItems = view.findViewById(R.id.rvCartItems);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserPhone = view.findViewById(R.id.tvUserPhone);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);

        // Setup RecyclerView
        rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCartItems.setNestedScrollingEnabled(false); // Disable scrolling to work well inside NestedScrollView
    }

    private void initData() {
        // Mock Data as per screenshot
        cartList = new ArrayList<>();

        Product p1 = new Product("p1", "Chân váy Mini Khaki", 763000, "https://example.com/skirt.jpg");
        // Note: Using placeholder URL, Glide will show error placeholder if it fails,
        // which is fine for mock.
        // If user provided an image, I would use that, but I don't have the image file
        // URL from the web.
        // I will use a resource ID if I can, but Model expects String.
        // For local testing without internet, this might show placeholder.

        cartList.add(new CartItem(p1, 3, "Be", "M"));

        // Add another item for testing
        Product p2 = new Product("p2", "Áo sơ mi trắng", 450000, "");
        cartList.add(new CartItem(p2, 1, "Trắng", "L"));

        adapter = new CartAdapter(getContext(), cartList, this);
        rvCartItems.setAdapter(adapter);
    }

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        // Handle quantity update (e.g., update total price if implemented)
        // Toast.makeText(getContext(), "Updated: " + item.getProduct().getName() + " ->
        // " + newQuantity, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(CartItem item, int position) {
        cartList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, cartList.size());
        Toast.makeText(getContext(), "Đã xóa " + item.getProduct().getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemChecked(CartItem item, boolean isChecked) {
        // Handle selection
    }
}
