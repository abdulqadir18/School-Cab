package com.example.schoolcab;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.gson.Gson;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchStudent extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_student);


        db = FirebaseFirestore.getInstance();

        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {
            EditText edtEnrollmentNo = findViewById(R.id.edtEnrollmentNo);

            String enrollmentNo = edtEnrollmentNo.getText().toString();

            Log.d("School", "Name: " + enrollmentNo);


            db.collection("students")
                    .whereEqualTo("rollNo", enrollmentNo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    Log.d(TAG, "Student Not Found with Given Enrollment No. ");

                                    Toast.makeText(SearchStudent.this, "STUDENT NOT FOUND WITH GIVEN ENROLLMENT NO.", Toast.LENGTH_LONG).show();
//                                    TextView textMessage = findViewById(R.id.textMessage);
//                                    textMessage.setText("No Student Found with Given Enrollment No. Please Enter Again");


                                } else {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        Log.d(TAG, document.getId() + " => " + document.getData());
//                                        Log.d(TAG, "Student Found");
//                                        Log.d(TAG, task.getResult().toString());

                                        String jsonString = new Gson().toJson(document.getData());
                                        Log.d(TAG, jsonString);
                                        Intent intent = new Intent(SearchStudent.this, EditStudentDetails.class);
                                        intent.putExtra("data", jsonString);
                                        intent.putExtra("id" ,document.getId());
                                        startActivity(intent);
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


        });
    }

}