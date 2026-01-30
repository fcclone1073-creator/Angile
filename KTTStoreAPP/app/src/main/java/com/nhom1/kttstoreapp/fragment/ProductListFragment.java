package com.nhom1.kttstoreapp.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
    private EditText etSearch;
    private ImageView ivFilter;
    private LinearLayout llFilters;
    private Spinner spinnerCategory;
    private Spinner spinnerPriceRange;

    private List<Product> allProducts = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private String selectedCategoryId = "";
    private String selectedPriceRange = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        rvAllProducts = view.findViewById(R.id.rvAllProducts);
        etSearch = view.findViewById(R.id.etSearch);
        ivFilter = view.findViewById(R.id.ivFilter);
        llFilters = view.findViewById(R.id.llFilters);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerPriceRange = view.findViewById(R.id.spinnerPriceRange);

        rvAllProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        setupSearch();
        setupFilters();
        loadCategories();
        loadProducts();

        return view;
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFilters() {
        ivFilter.setOnClickListener(v -> {
            if (llFilters.getVisibility() == View.VISIBLE) {
                llFilters.setVisibility(View.GONE);
            } else {
                llFilters.setVisibility(View.VISIBLE);
            }
        });

        // Setup price range spinner
        String[] priceRanges = {"Tất cả giá", "Dưới 100.000đ", "100.000đ - 500.000đ", "500.000đ - 1.000.000đ", "Trên 1.000.000đ"};
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, priceRanges);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriceRange.setAdapter(priceAdapter);

        spinnerPriceRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPriceRange = priceRanges[position];
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedCategoryId = "";
                } else {
                    selectedCategoryId = categories.get(position - 1).getId();
                }
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadCategories() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories = response.body();
                    setupCategorySpinner();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // Silent fail - categories are optional
            }
        });
    }

    private void setupCategorySpinner() {
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("Tất cả danh mục");
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
    }

    private void loadProducts() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allProducts = response.body();
                    filterProducts();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterProducts() {
        List<Product> filteredProducts = new ArrayList<>();

        String searchQuery = etSearch.getText().toString().toLowerCase().trim();

        for (Product product : allProducts) {
            // Filter by search query
            if (!searchQuery.isEmpty()) {
                if (!product.getName().toLowerCase().contains(searchQuery)) {
                    continue;
                }
            }

            // Filter by category
            if (!selectedCategoryId.isEmpty()) {
                if (product.getCategoryId() == null || !product.getCategoryId().equals(selectedCategoryId)) {
                    continue;
                }
            }

            // Filter by price range
            if (!selectedPriceRange.isEmpty() && !selectedPriceRange.equals("Tất cả giá")) {
                double price = product.getPrice();
                switch (selectedPriceRange) {
                    case "Dưới 100.000đ":
                        if (price >= 100000) continue;
                        break;
                    case "100.000đ - 500.000đ":
                        if (price < 100000 || price > 500000) continue;
                        break;
                    case "500.000đ - 1.000.000đ":
                        if (price < 500000 || price > 1000000) continue;
                        break;
                    case "Trên 1.000.000đ":
                        if (price <= 1000000) continue;
                        break;
                }
            }

            filteredProducts.add(product);
        }

        productAdapter = new ProductAdapter(getContext(), filteredProducts);
        rvAllProducts.setAdapter(productAdapter);
    }
}
