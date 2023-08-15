package com.example.schoolcab;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentRegistrationActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        db = FirebaseFirestore.getInstance();

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            // Collect student details from EditText fields
            EditText nameEditText = findViewById(R.id.student_name);
            EditText passwordEditText = findViewById(R.id.student_password);

            String name = nameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            Log.d("StudentRegistration", "Name: " + name + ", Password: " + password);

//            Student student = new Student(name, password);
            Student student = new Student();
            student.setName(name);
            student.setPassword(password);


            // Add student to Firestore
            CollectionReference studentsCollection = db.collection("students");

            studentsCollection.add(student)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Student registered successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("StudentRegistration", "Error registering student", e);
                        Toast.makeText(this, "Error registering student", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}


