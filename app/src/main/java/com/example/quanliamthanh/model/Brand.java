package com.example.quanliamthanh.model;

public class Brand {
    private String Brandid;
    private String name;

    public Brand() {
        // Default constructor required for Firestore
    }

    public Brand(String id, String name) {
        this.Brandid = id;
        this.name = name;
    }

    public String getId() {
        return Brandid;
    }

    public void setId(String id) {
        this.Brandid = id;
    }

    public String getName() {
        return name;
    }
    public String toString() {
        return name; // Trả về tên danh mục để hiển thị trong Spinner
    }

    public void setName(String name) {
        this.name = name;
    }
}
