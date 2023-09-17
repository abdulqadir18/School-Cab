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
        Button uploadStudent = findViewById(R.id.uploadStudent);

        addStudent.setOnClickListener(v -> {
            Intent intent = new Intent(StudentAddUpdatePage.this, AddStudent.class);
            startActivity(intent);
        });

        updateStudent.setOnClickListener(v -> {
            Intent intent = new Intent(StudentAddUpdatePage.this, SearchStudent.class);
            intent.putExtra("activity" , "update Student");
            startActivity(intent);
        });

        deleteStudent.setOnClickListener(v -> {
            Intent intent = new Intent(StudentAddUpdatePage.this, SearchStudent.class);
            intent.putExtra("activity" , "delete Student");
            startActivity(intent);
        });


        uploadStudent.setOnClickListener(v -> {
            Intent intent = new Intent(StudentAddUpdatePage.this, UploadStudents.class);
            startActivity(intent);
        });


    }
}