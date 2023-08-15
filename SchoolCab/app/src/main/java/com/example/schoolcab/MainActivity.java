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

//        schoolButton.setOnClickListener(v -> {
//            // Handle school button click
//        });

        studentButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StudentRegistrationActivity.class);
            startActivity(intent);
        });
    }
}
