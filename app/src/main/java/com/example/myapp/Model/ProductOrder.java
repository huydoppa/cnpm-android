package com.example.myapp.Model;

public class ProductOrder {

    private String idProductOrder, idCustomer, name, address,totalPrice, status,id, phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProductOrder() {
        return idProductOrder;
    }

    public ProductOrder(String idProductOrder, String idCustomer, String name, String address, String totalPrice, String status, String id, String phone) {
        this.idProductOrder = idProductOrder;
        this.idCustomer = idCustomer;
        this.name = name;
        this.address = address;
        this.totalPrice = totalPrice;
        this.status = status;
        this.id = id;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setIdProductOrder(String idProductOrder) {
        this.idProductOrder = idProductOrder;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductOrder(String idProductOrder, String idCustomer, String name, String address, String totalPrice, String status) {
        this.idProductOrder = idProductOrder;
        this.idCustomer = idCustomer;
        this.name = name;
        this.address = address;
        this.totalPrice = totalPrice;
        this.status = status;
    }
}
