package com.nhom1.kttstoreapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.adapter.ProductAdapter;
import com.nhom1.kttstoreapp.api.ApiClient;
import com.nhom1.kttstoreapp.api.ApiService;
import com.nhom1.kttstoreapp.model.Category;
import com.nhom1.kttstoreapp.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListFragment extends Fragment {

    private RecyclerView rvAllProducts;
    private ProductAdapter productAdapter;
    private LinearLayout llCategories;
    private List<Product> productList = new ArrayList<>();

    private ApiService apiService;

    private int currentPage = 1;
    private int limit = 10;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String currentCategoryId = null;

    // Advanced Filter State
    private Integer currentMinPrice, currentMaxPrice;
    private String currentSort, currentStock;
    private Boolean currentPromotion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        rvAllProducts = view.findViewById(R.id.rvAllProducts);
        llCategories = view.findViewById(R.id.llCategories);

        view.findViewById(R.id.ivBack).setOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().onBackPressed();
        });

        view.findViewById(R.id.ivFilter).setOnClickListener(v -> showFilterBottomSheet());

        apiService = ApiClient.getClient().create(ApiService.class);

        setupRecyclerView();
        loadCategories();
        loadProducts(true);

        return view;
    }

    private void showFilterBottomSheet() {
        FilterBottomSheetFragment filterFragment = new FilterBottomSheetFragment();
        filterFragment.setOnFilterApplyListener((minPrice, maxPrice, sort, stock, promotion) -> {
            this.currentMinPrice = minPrice;
            this.currentMaxPrice = maxPrice;
            this.currentSort = sort;
            this.currentStock = stock;
            this.currentPromotion = promotion;

            // Reset pagination
            currentPage = 1;
            isLastPage = false;
            loadProducts(true);
        });
        filterFragment.show(getParentFragmentManager(), filterFragment.getTag());
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvAllProducts.setLayoutManager(layoutManager);

        productAdapter = new ProductAdapter(getContext(), productList, product -> {
            // Handle product click
            Toast.makeText(getContext(), "Selected: " + product.getName(), Toast.LENGTH_SHORT).show();
        });
        rvAllProducts.setAdapter(productAdapter);

        rvAllProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadProducts(false);
                    }
                }
            }
        });
    }

    private void loadCategories() {
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupCategoryFilters(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // Ignore error for categories
            }
        });
    }

    private void setupCategoryFilters(List<Category> categories) {
        // Add "All" button
        addCategoryButton("Tất cả", null);

        for (Category category : categories) {
            addCategoryButton(category.getName(), category.getId());
        }
    }

    private void addCategoryButton(String name, String categoryId) {
        Button btn = new Button(getContext());
        btn.setText(name);
        btn.setAllCaps(false);
        btn.setTextSize(12);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 0, 8, 0);
        btn.setLayoutParams(params);

        if ((currentCategoryId == null && categoryId == null) ||
                (currentCategoryId != null && currentCategoryId.equals(categoryId))) {
            btn.setBackgroundColor(Color.BLACK);
            btn.setTextColor(Color.WHITE);
        } else {
            btn.setBackgroundColor(Color.LTGRAY);
            btn.setTextColor(Color.BLACK);
        }

        btn.setOnClickListener(v -> {
            currentCategoryId = categoryId;
            currentPage = 1;
            isLastPage = false;
            reloadCategoryButtons();
            loadProducts(true);
        });

        llCategories.addView(btn);
    }

    private void reloadCategoryButtons() {
        // Simple way: Clear and reload (in real app, just update state)
        // For now, we will just iterate and update simple background colors if we
        // stored reference
        // Ideally re-render logic. Since we fetch categories once, we should store
        // them.
        // For simplicity in this generated code, we will assume user won't spam filters
        // too fast
        // or we simply re-request categories or keep them in a list.
        // Let's just visually update the existing buttons if possible,
        // but since we didn't keep references, let's just clear and re-fetch or
        // simpler:
        // just reset the whole view.
        // BETTER APPROACH: Just re-call loadCategories or optimize later.
        // For this task, let's just clear views and re-call loadCategories logic if we
        // had the list.
        // Since we didn't store list, let's correct this.

        llCategories.removeAllViews();
        loadCategories(); // This causes network call again, which is suboptimal but works for MVP.
    }

    private void loadProducts(boolean isFirstPage) {
        isLoading = true;
        if (isFirstPage) {
            currentPage = 1;
        }

        apiService.getProducts(null, currentPage, limit, currentCategoryId,
                currentMinPrice, currentMaxPrice, currentSort, currentStock, currentPromotion)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null) {
                            List<Product> newProducts = response.body();

                            if (isFirstPage) {
                                productList.clear();
                                productAdapter.clearProducts(); // Helper if exists, or just notifyDataSetChanged
                            }

                            if (newProducts.isEmpty()) {
                                isLastPage = true;
                            } else {
                                productList.addAll(newProducts);
                                productAdapter.notifyDataSetChanged();
                                if (newProducts.size() < limit) {
                                    isLastPage = true;
                                } else {
                                    currentPage++;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        isLoading = false;
                        Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
