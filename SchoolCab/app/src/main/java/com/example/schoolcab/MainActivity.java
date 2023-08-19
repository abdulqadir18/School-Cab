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
        Button newStudentButton = findViewById(R.id.newStudentButton);
        Button searchStudent = findViewById(R.id.searchStudent);


        schoolButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SchoolNameActivity.class);
            startActivity(intent);
        });

        studentButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StudentRegistrationActivity.class);
            startActivity(intent);
        });

        newStudentButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddStudent.class);
            startActivity(intent);
        });


        searchStudent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchStudent.class);
            startActivity(intent);
        });

//        uploadStudent.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, UploadStudents.class);
//            startActivity(intent);
//        });
    }
}
