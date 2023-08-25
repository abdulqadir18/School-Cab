package com.example.schoolcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddStudent extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String ID = mAuth.getCurrentUser().getUid().toString();


        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            // Collect student details from EditText fields

            EditText edtName = findViewById(R.id.edtName);
            EditText edtRollNo = findViewById(R.id.edtRollNo);
            EditText edtGuardian = findViewById(R.id.edtGuardian);
            EditText edtEmail = findViewById(R.id.edtEmail);
            EditText edtPhoneNo = findViewById(R.id.edtPhoneNo);
            EditText edtAddress = findViewById(R.id.edtAddress);
            EditText edtDefaultAddress = findViewById(R.id.edtDefaultAddress);
            EditText edtClass = findViewById(R.id.edtClass);
            EditText edtSection = findViewById(R.id.edtSection);
            EditText edtSex = findViewById(R.id.edtSex);
            EditText edtAge = findViewById(R.id.edtAge);
            EditText edtWeight = findViewById(R.id.edtWeight);



            String name = edtName.getText().toString();
            String rollNo = edtRollNo.getText().toString();
            String guardian = edtGuardian.getText().toString();
            String email = edtEmail.getText().toString();
            String phoneNo = edtPhoneNo.getText().toString();
            String address = edtAddress.getText().toString();
            String  defaultAddress = edtDefaultAddress.getText().toString();
            int standard = Integer.parseInt(edtClass.getText().toString());
            String section  = edtSection.getText().toString();
            String sex = edtSex.getText().toString();
            int age = Integer.parseInt(edtAge.getText().toString());
            int weight = Integer.parseInt(edtWeight.getText().toString());



            Log.d("StudentRegistration", "Name: " + name + ", Password: " + rollNo);

//            NewStudent student = new NewStudent(name, password);
            NewStudent student = new NewStudent();
            student.setName(name);
            student.setRollNo(rollNo);
            student.setGuardian(guardian);
            student.setPhoneNo(phoneNo);
            student.setAddress(address);
            student.setDefaultAddress(defaultAddress);
            student.setStandard(standard);
            student.setSection(section);
            student.setSex(sex);
            student.setAge(age);
            student.setWeight(weight);
            student.setEmail(email);
            student.setSchoolId(ID);


//            Creating a authenication user in firebase
            mAuth.createUserWithEmailAndPassword(email, "1234567890")
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // User signup successful
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getUid();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName("student")
                                        .build();

                                user.updateProfile(profileUpdates);

                                // Save additional user information to Firestore
                                DocumentReference userRef = db.collection("students").document(userId);

//                                Saving Additional information of user in fireStore with same id
                                userRef.set(student)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // User information saved to Firestore successfully
                                            Toast.makeText(AddStudent.this, "Student registered successfully!", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();
                                                } else {
                                                    // Handle Firestore document creation failure
                                                    Toast.makeText(AddStudent.this, "Error saving user data to Firestore.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            } else {
                                // Handle signup failure
                                Toast.makeText(AddStudent.this, "Signup failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });






        });




    }
}