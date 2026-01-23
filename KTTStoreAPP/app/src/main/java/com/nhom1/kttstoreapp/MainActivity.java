package com.nhom1.kttstoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhom1.kttstoreapp.fragment.BoardFragment;
import com.nhom1.kttstoreapp.fragment.HomeFragment;
import com.nhom1.kttstoreapp.fragment.ProductListFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_cart) {
                // Board/Task Management
                selectedFragment = new BoardFragment();
            } else if (itemId == R.id.nav_orders) {
                selectedFragment = new ProductListFragment();
            } else if (itemId == R.id.nav_notification) {
                selectedFragment = new ProductListFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProductListFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }
}