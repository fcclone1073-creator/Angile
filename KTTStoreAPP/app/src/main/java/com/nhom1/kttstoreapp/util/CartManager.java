package com.nhom1.kttstoreapp.util;

import com.nhom1.kttstoreapp.model.CartItem;
import com.nhom1.kttstoreapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addProduct(Product product) {
        // Check if product already exists in cart
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.increaseQuantity();
                return;
            }
        }
        // Add new product to cart
        cartItems.add(new CartItem(product, 1));
    }

    public void removeProduct(String productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void updateQuantity(String productId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                if (quantity <= 0) {
                    removeProduct(productId);
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getTotalItems() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}
