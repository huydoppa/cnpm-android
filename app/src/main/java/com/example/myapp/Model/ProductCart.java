package com.example.myapp.Model;

import java.io.Serializable;

public class ProductCart implements Serializable {

    private Cart cart;
    private int count;

    public ProductCart(Cart cart, int count) {
        this.cart = cart;
        this.count = count;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
