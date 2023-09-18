package com.example.schoolcab;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class Bus {

    private int busNo;

    private String busid;
    private int busCapacity;
    private List<GeoPoint> busStops;
    private GeoPoint location;
    private String schoolid;
    private String busUserId;
    private String password;

    public Bus() {
    }

    public int getBusNo() {
        return busNo;
    }

    public void setBusNo(int busNo) {
        this.busNo = busNo;
    }

    public String getBusId() {
        return busid;
    }

    public void setBusId(String busId) {
        this.busid = busId;
    }

    public int getBusCapacity() {
        return busCapacity;
    }

    public void setBusCapacity(int busCapacity) {
        this.busCapacity = busCapacity;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getSchoolId() {
        return schoolid;
    }

    public void setSchoolId(String schoolId) {
        this.schoolid = schoolId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<GeoPoint> getBusStops() {
        return busStops;
    }

    public void setBusStops(List<GeoPoint> busStops) {
        this.busStops = busStops;
    }

    public String getBusUserId() {
        return busUserId;
    }

    public void setBusUserId(String busUserId) {
        this.busUserId = busUserId;
    }
}
