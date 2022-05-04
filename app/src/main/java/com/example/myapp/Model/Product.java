package com.example.myapp.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private String idProduct;
    private String productName;
    private String type;
    private int priceProduct;
    private int proPrice;
    private int amount;
    private String img_product;
    private String description;

    public Product() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product(String idProduct, String productName, String type, int priceProduct, int proPrice, int amount, String img_product, String description) {
        this.idProduct = idProduct;
        this.productName = productName;
        this.type = type;
        this.priceProduct = priceProduct;
        this.proPrice = proPrice;
        this.amount = amount;
        this.img_product = img_product;
        this.description = description;
    }

    public Product(String idProduct, String productName, String type, int priceProduct, int proPrice, int amount, String img_product) {
        this.idProduct = idProduct;
        this.productName = productName;
        this.type = type;
        this.priceProduct = priceProduct;
        this.proPrice = proPrice;
        this.amount = amount;
        this.img_product = img_product;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }

    public int getProPrice() {
        return proPrice;
    }

    public void setProPrice(int proPrice) {
        this.proPrice = proPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImg_product() {
        return img_product;
    }

    public void setImg_product(String img_product) {
        this.img_product = img_product;
    }
}
