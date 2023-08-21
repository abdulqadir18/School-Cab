package com.example.schoolcab;

public class NewStudent {
    private String name;
    private String rollNo;
    private String guardian;
    private String phoneNo;
    private String address;
    private String defaultAddress;
    private int standard;
    private String email;
    private String section;
    private String sex;
    private int age;
    private int weight;

    public NewStudent() {
        // Default constructor required for Firestore
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    // Getter and setter for rollNo
    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    // Getter and setter for guardian
    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    // Getter and setter for phoneNo
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    // Getter and setter for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    // Getter and setter for Email
    public String getEmail() {
        return address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for defaultAddress
    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    // Getter and setter for standard
    public int getStandard() {
        return standard;
    }

    public void setStandard(int standard) {
        this.standard = standard;
    }


    // Getter and setter for section
    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    // Getter and setter for sex
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    // Getter and setter for age
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Getter and setter for weight
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

