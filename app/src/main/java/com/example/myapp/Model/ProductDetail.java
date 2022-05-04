package com.example.myapp.Model;

public class ProductDetail {
    public ProductDetail(String idProduct, String name, String price, String proPrice, String amount) {
        this.idProduct = idProduct;
        this.name = name;
        this.price = price;
        this.proPrice = proPrice;
        this.amount = amount;
    }

    private String idProduct,name, price, proPrice, amount;

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProPrice() {
        return proPrice;
    }

    public void setProPrice(String proPrice) {
        this.proPrice = proPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
