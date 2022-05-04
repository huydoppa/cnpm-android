package com.example.myapp.Model;

import java.io.Serializable;

public class Cart implements Serializable {
    private String idCustomer;
    private String idProduct;
    private String amount;
    private String id;

    public Cart(String idCustomer, String idProduct, String amount, String id) {
        this.idCustomer = idCustomer;
        this.idProduct = idProduct;
        this.amount = amount;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cart(String idCustomer, String idProduct, String amount) {
        this.idCustomer = idCustomer;
        this.idProduct = idProduct;
        this.amount = amount;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
