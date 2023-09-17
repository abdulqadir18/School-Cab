package com.example.schoolcab;

public class Attendance {

    private String studentId;
    private String schoolId;

    private String busId;
    private String date;

    public Attendance(String studentId, String schoolId, String busId, String date) {
        this.studentId = studentId;
        this.schoolId = schoolId;
        this.busId = busId;
        this.date = date;
    }

    public Attendance() {
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
