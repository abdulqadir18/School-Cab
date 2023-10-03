package com.example.schoolcab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class AttendanceTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_type);

        RelativeLayout arrival = findViewById(R.id.arrival);
        RelativeLayout departure = findViewById(R.id.departure);

        arrival.setOnClickListener(v -> {
            Intent intent = new Intent(this, AttendanceAddActivity.class);
            startActivity(intent);

        });

        departure.setOnClickListener(v -> {
            Intent intent = new Intent(this, AttendanceAddDepartureActivity.class);
            startActivity(intent);

        });

    }
}