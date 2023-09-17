package com.example.schoolcab;

import static android.app.ProgressDialog.show;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

    public static final String SHARED_PREFS = "shared_prefs";

    // key for schoolId
    public static final String sId = "sId";
    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_student);
        //        Getting the school id saved in local preferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String schoolID = sharedpreferences.getString("sId", null);


        db = FirebaseFirestore.getInstance();

        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {
            EditText edtEnrollmentNo = findViewById(R.id.edtEnrollmentNo);

            String enrollmentNo = edtEnrollmentNo.getText().toString();

            Log.d("School", "Name: " + enrollmentNo);


            db.collection("students")
                    .whereEqualTo("rollNo", enrollmentNo)
                    .whereEqualTo("schoolId",schoolID)
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

                                        String jsonString = new Gson().toJson(document.getData());

                                        if(getIntent().getStringExtra("activity").equals("update Student"))
                                        {
                                        Intent intent = new Intent(SearchStudent.this, EditStudentDetails.class);
                                        intent.putExtra("data", jsonString);
                                        intent.putExtra("id" ,document.getId());
                                        startActivity(intent);
                                        }
                                        else if (getIntent().getStringExtra("activity").equals("delete Student"))
                                        {
                                            deleteStudent(document.getId());
                                        }

                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


        });
    }

    private void deleteStudent(String id)
    {

        db = FirebaseFirestore.getInstance();

        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity").setMessage("Are you sure you Want to delete this student ? Once Deleted Cannot be Recovered")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("students").document(id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SearchStudent.this, "Student successfully deleted!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(SearchStudent.this, StudentAddUpdatePage.class);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SearchStudent.this, "Error Deleting Student", Toast.LENGTH_LONG).show();

//                                        Log.w(TAG, "Error deleting Student", e);
                                    }
                                });


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SearchStudent.this, StudentAddUpdatePage.class);
                        startActivity(intent);
                    }
                }).show();
    }

}