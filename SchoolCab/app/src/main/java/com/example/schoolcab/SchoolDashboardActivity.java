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
        busButton.setOnClickListener(v -> {

        });

        busCoordinatorButton.setOnClickListener(v -> {
            Intent intent = new Intent(SchoolDashboardActivity.this, CoordinatorDashboardActivity.class);
            startActivity(intent);
        });

        studentButton.setOnClickListener(v -> {

        });

       attendanceButton.setOnClickListener(v -> {

        });
    }
}


