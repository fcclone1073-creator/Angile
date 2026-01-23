package com.nhom1.kttstoreapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.adapter.BannerAdapter;
import com.nhom1.kttstoreapp.adapter.CategoryAdapter;
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

public class HomeFragment extends Fragment {

    private ViewPager2 viewPagerBanner;
    private RecyclerView rvCategories, rvFeaturedProducts;
    private BannerAdapter bannerAdapter;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private Handler sliderHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPagerBanner = view.findViewById(R.id.viewPagerBanner);
        rvCategories = view.findViewById(R.id.rvCategories);
        rvFeaturedProducts = view.findViewById(R.id.rvFeaturedProducts);

        setupBanner();
        setupCategories();
        setupFeaturedProducts();

        return view;
    }

    private void setupBanner() {
        List<String> bannerImages = new ArrayList<>();
        bannerImages.add(
                "https://img.freepik.com/free-vector/horizontal-banner-template-big-sale-with-woman-shopping-bags_23-2148786756.jpg");
        bannerImages.add("https://img.freepik.com/free-psd/fashion-sale-banner-template_23-2148936331.jpg");
        bannerImages.add("https://img.freepik.com/free-vector/fashion-sale-banner-template_23-2148165842.jpg");

        bannerAdapter = new BannerAdapter(getContext(), bannerImages);
        viewPagerBanner.setAdapter(bannerAdapter);

        // Auto slide
        viewPagerBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000); // Slide duration 3 seconds
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPagerBanner.getCurrentItem();
            int totalItems = bannerAdapter.getItemCount();
            if (currentItem < totalItems - 1) {
                viewPagerBanner.setCurrentItem(currentItem + 1);
            } else {
                viewPagerBanner.setCurrentItem(0);
            }
        }
    };

    private void setupCategories() {
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryAdapter = new CategoryAdapter(getContext(), response.body());
                    rvCategories.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupFeaturedProducts() {
        rvFeaturedProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        // Or LinearLayoutManager.HORIZONTAL if you want a horizontal list like the
        // design often implies

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getFeaturedProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productAdapter = new ProductAdapter(getContext(), response.body());
                    rvFeaturedProducts.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
}
