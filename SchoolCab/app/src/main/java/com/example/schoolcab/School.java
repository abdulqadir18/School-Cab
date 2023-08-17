package com.example.schoolcab;

public class School {

    private String name;
    private String board;
    private Boolean verifiedStatus;
    private String schoolId;
    private String password;

    public School(String name, String board, Boolean verifiedStatus, String schoolId, String password) {
        this.name = name;
        this.board = board;
        this.verifiedStatus = verifiedStatus;
        this.schoolId = schoolId;
        this.password = password;
    }

    public School() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public Boolean getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setVerifiedStatus(Boolean verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
