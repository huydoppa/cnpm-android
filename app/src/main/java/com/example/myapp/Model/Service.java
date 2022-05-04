package com.example.myapp.Model;

import java.io.Serializable;

public class Service implements Serializable {
    private String idService;
    private String nameService;
    private int priceService;
    private int proPriceService;
    private String Address;
    private String Description;
    private String imgService;
    private String supplier;

    public Service() {
    }

    public Service(String idService, String nameService, int priceService, int proPriceService, String address, String description, String imgService, String supplier) {
        this.idService = idService;
        this.nameService = nameService;
        this.priceService = priceService;
        this.proPriceService = proPriceService;
        Address = address;
        Description = description;
        this.imgService = imgService;
        this.supplier = supplier;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }

    public int getPriceService() {
        return priceService;
    }

    public void setPriceService(int priceService) {
        this.priceService = priceService;
    }

    public int getProPriceService() {
        return proPriceService;
    }

    public void setProPriceService(int proPriceService) {
        this.proPriceService = proPriceService;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImgService() {
        return imgService;
    }

    public void setImgService(String imgService) {
        this.imgService = imgService;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
