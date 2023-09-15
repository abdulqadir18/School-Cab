package com.example.schoolcab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class BusDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_dashboard2);

        Button attendance = findViewById(R.id.attendance);
        Button busTracking = findViewById(R.id.busTracking);
        Button logoutButton = findViewById(R.id.logout_button1);
        Button notification = findViewById(R.id.notification1);

        attendance.setOnClickListener(v -> {
            Intent intent = new Intent(BusDashboard.this, AttendanceAddActivity.class);

            startActivity(intent);

        });
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(BusDashboard.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(BusDashboard.this, "Logged Out Succesfully", Toast.LENGTH_LONG).show();
        });

        busTracking.setOnClickListener(v -> {

            Intent intent = new Intent(BusDashboard.this, BusDashboardActivity.class);
            intent.putExtra("busid", getIntent().getStringExtra("busid"));
            intent.putExtra("schoolid", getIntent().getStringExtra("schoolid"));
            startActivity(intent);
        });
        notification.setOnClickListener(v -> {
            Intent intent = new Intent(BusDashboard.this, NotificationSend.class);

            startActivity(intent);
        });


    }
}