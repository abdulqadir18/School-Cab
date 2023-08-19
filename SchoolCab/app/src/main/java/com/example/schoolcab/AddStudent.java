package com.example.schoolcab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddStudent extends AppCompatActivity {

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);


        db = FirebaseFirestore.getInstance();


        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            // Collect student details from EditText fields
            EditText edtName = findViewById(R.id.edtName);
            EditText edtRollNo = findViewById(R.id.edtRollNo);
            EditText edtGuardian = findViewById(R.id.edtGuardian);
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
            int phoneNo = Integer.parseInt(edtPhoneNo.getText().toString());
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