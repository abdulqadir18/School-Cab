package com.example.schoolcab;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchoolRegistrationActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_registration);

        db = FirebaseFirestore.getInstance();

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            // Collect school details from EditText fields
            EditText nameEditText = findViewById(R.id.school_name);
            EditText boardEditText = findViewById(R.id.school_board);
            EditText schoolIdEditText = findViewById(R.id.school_id);
            EditText passwordEditText = findViewById(R.id.school_password);
            EditText confirmPasswordEditText = findViewById(R.id.confirm_password);

            String name = nameEditText.getText().toString();
            String board = boardEditText.getText().toString();
            String schoolId = schoolIdEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            Log.d("SchoolRegistration", "Name: " + name + ", Password: " + password +
                                                "Board: " + board + "schoolId: " + schoolId + "confirmPassword: " + confirmPassword);


            Matcher matcher = Pattern.compile(password).matcher(confirmPassword);
            if (matcher.matches()) {
                Log.e("Matched Passwords", password);
                School school = new School();
                school.setName(name);
                school.setSchoolId(schoolId);
                school.setBoard(board);
                school.setVerifiedStatus(false);
                school.setPassword(password);


                // Add school to Firestore
                db.collection("schools")
                        .add(school)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "School registered successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("SchoolRegistration", "Error registering school", e);
                            Toast.makeText(this, "Error registering school", Toast.LENGTH_SHORT).show();
                        });

            } else {
                Toast.makeText(SchoolRegistrationActivity.this, "Passwords not Match", Toast.LENGTH_LONG).show();
            }

        });
    }
}



