package com.example.quanliamthanh.model;

public class Category {
    private String categoryId;
    private String categoryName;
    public Category() {

    }

    public Category(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }



    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public String toString() {
        return categoryName; // Trả về tên danh mục để hiển thị trong Spinner
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }







}
