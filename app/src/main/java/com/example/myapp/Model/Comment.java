package com.example.myapp.Model;

public class Comment {
    private String idProduct;
    private String Content;
    private String idUser;

    public Comment(String idProduct, String content, String idUser) {
        this.idProduct = idProduct;
        Content = content;
        this.idUser = idUser;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
