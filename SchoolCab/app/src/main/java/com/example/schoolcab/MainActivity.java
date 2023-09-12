package com.example.schoolcab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button schoolButton = findViewById(R.id.schoolButton);
        Button studentButton = findViewById(R.id.studentButton);
        Button busButton = findViewById(R.id.busButton);
        Button tempButton = findViewById(R.id.tempButton);

        schoolButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SchoolNameActivity.class);
            startActivity(intent);
        });

        studentButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StudentRegistrationActivity.class);
            startActivity(intent);
        });

        busButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BusLoginActivity.class);
            startActivity(intent);
        });

        tempButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GpsTempActivity.class);
            startActivity(intent);
        });
    }
}
