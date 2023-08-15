package com.example.schoolcab;

public class Student {
    private String name;
    private String password;

    public Student() {
        // Default constructor required for Firestore
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
