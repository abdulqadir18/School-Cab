package com.example.schoolcab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class StudentAddUpdatePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add_update_page);


        Button addStudent = findViewById(R.id.addStudent);
        Button updateStudent = findViewById(R.id.updateStudent);
        Button deleteStudent = findViewById(R.id.deleteStudent);

        addStudent.setOnClickListener(v -> {
            Intent intent = new Intent(StudentAddUpdatePage.this, AddStudent.class);
            startActivity(intent);
        });

        updateStudent.setOnClickListener(v -> {
            Intent intent = new Intent(StudentAddUpdatePage.this, SearchStudent.class);
            startActivity(intent);
        });

        deleteStudent.setOnClickListener(v -> {
            Toast.makeText(StudentAddUpdatePage.this, "This Functionality is not yet been Added please wait for updated version", Toast.LENGTH_LONG).show();

        });


    }
}