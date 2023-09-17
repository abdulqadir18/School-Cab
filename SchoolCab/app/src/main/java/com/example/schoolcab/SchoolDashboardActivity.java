package com.example.schoolcab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SchoolDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_dashboard);

        TextView schoolName = findViewById(R.id.school_name);
        schoolName.setText(getIntent().getStringExtra("schoolName"));

        Button busButton = findViewById(R.id.bus_button);
        Button busCoordinatorButton = findViewById(R.id.bus_coordinator_button);
        Button studentButton = findViewById(R.id.student_details_button);
        Button attendanceButton = findViewById(R.id.attendance_button);
        Button notificationButton = findViewById(R.id.notification_button);
        Button reportButton = findViewById(R.id.report_button);
        busButton.setOnClickListener(v -> {

        });

        busCoordinatorButton.setOnClickListener(v -> {

        });

        studentButton.setOnClickListener(v -> {

        });

       attendanceButton.setOnClickListener(v -> {
           Intent intent = new Intent(SchoolDashboardActivity.this, AttendanceAddActivity.class);
           startActivity(intent);
        });

        notificationButton.setOnClickListener(v -> {
            Intent intent = new Intent(SchoolDashboardActivity.this, NotificationSendActivity.class);
            startActivity(intent);
        });

        reportButton.setOnClickListener(v-> {
            Intent intent = new Intent(SchoolDashboardActivity.this, ReportActivity.class);
            startActivity(intent);
        });

    }
}


