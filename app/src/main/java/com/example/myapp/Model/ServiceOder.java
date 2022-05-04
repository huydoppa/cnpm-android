package com.example.myapp.Model;

public class ServiceOder {
    private String idService, id, idCustomer, nameService, nameCustomer, address, time;

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ServiceOder(String idService, String id, String idCustomer, String nameService, String nameCustomer, String address, String time) {
        this.idService = idService;
        this.id = id;
        this.idCustomer = idCustomer;
        this.nameService = nameService;
        this.nameCustomer = nameCustomer;
        this.address = address;
        this.time = time;

    }
}
