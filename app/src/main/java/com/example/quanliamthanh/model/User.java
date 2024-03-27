package com.example.quanliamthanh.model;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String email;
    private String fullName;
    public User(){

    }

    public User(String id, String email,String fullName) {
        this.id = id;
        this.email = email;
        this.fullName=fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
