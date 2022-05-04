package com.example.myapp.Model;

public class CategoriesProduct {
    private int idCategories;
    private String nameCategory;
    private int imgCategory;

    public CategoriesProduct() {
    }

    public int getIdCategories() {
        return idCategories;
    }

    public void setIdCategories(int idCategories) {
        this.idCategories = idCategories;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public int getImgCategory() {
        return imgCategory;
    }

    public void setImgCategory(int imgCategory) {
        this.imgCategory = imgCategory;
    }

    public CategoriesProduct(int idCategories, String nameCategory, int imgCategory) {
        this.idCategories = idCategories;
        this.nameCategory = nameCategory;
        this.imgCategory = imgCategory;
    }
}
