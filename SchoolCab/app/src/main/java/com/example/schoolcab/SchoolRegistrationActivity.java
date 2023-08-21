package com.example.schoolcab;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchoolRegistrationActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_registration);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            // Collect school details from EditText fields
            EditText nameEditText = findViewById(R.id.school_name);
            EditText boardEditText = findViewById(R.id.school_board);
            EditText schoolIdEditText = findViewById(R.id.school_id);
            EditText passwordEditText = findViewById(R.id.school_password);
            EditText confirmPasswordEditText = findViewById(R.id.confirm_password);
            EditText schoolEmail = findViewById(R.id.school_email);

            String name = nameEditText.getText().toString();
            String board = boardEditText.getText().toString();
            String schoolId = schoolIdEditText.getText().toString();
            String email = schoolEmail.getText().toString();
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
                school.setEmail(email);


                mAuth.createUserWithEmailAndPassword(email, "1234567890")
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User signup successful
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String schoolId = user.getUid();

                                    // Save additional School information to Firestore
                                    DocumentReference userRef = db.collection("schools").document(schoolId);

//                                Saving Additional information of user in fireStore with same id
                                    userRef.set(school)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // school information saved to Firestore successfully
                                                        Toast.makeText(SchoolRegistrationActivity.this, "Student registered successfully!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        // Handle Firestore document creation failure
                                                        Toast.makeText(SchoolRegistrationActivity.this, "Error saving user data to Firestore.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else {
                                    // Handle signup failure
                                    Toast.makeText(SchoolRegistrationActivity.this, "Signup failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } else {
                Toast.makeText(SchoolRegistrationActivity.this, "Passwords not Match", Toast.LENGTH_LONG).show();
            }

        });
    }
}



