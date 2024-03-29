package com.example.schoolcab;

import java.security.PrivateKey;

public class CoOrdinator {
    private String name;
    private String lastName;

    private String schoolId;

    private String phoneNo;

    private String password;

    private String coordinatorId;


    public CoOrdinator() {
        // Default constructor required for Firestore
    }
    public CoOrdinator(String name, String lastName, String phoneNo) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getphoneNo() {
        return phoneNo;
    }

    public void setphoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(String coordinatorId) {
        this.coordinatorId = coordinatorId;
    }
}