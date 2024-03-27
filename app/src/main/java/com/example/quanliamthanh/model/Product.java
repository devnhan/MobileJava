package com.example.quanliamthanh.model;

public class Product {
    public String productId;
    public String title;
    public Double price;
    public String quantity;
    public String description;
    public String category;
    public String brand;


    public Product () {

    }
    public Product(String productId, String title, Double price, String quantity, String description, String category, String brand, String image) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.image = image;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String image;




}
