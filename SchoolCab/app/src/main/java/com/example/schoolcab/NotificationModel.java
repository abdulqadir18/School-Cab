package com.example.schoolcab;

import java.sql.Timestamp;

public class NotificationModel {

    private String title;
    private String message;
    private String time;

    // Constructor
    public NotificationModel() {


    }

    // Getter and Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public String getMessage () {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}