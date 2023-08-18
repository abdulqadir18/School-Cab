package com.example.schoolcab;

import static android.content.ContentValues.TAG;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SchoolNameActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_name);

        db = FirebaseFirestore.getInstance();

        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {
            EditText nameEditText = findViewById(R.id.school_name);

            String name = nameEditText.getText().toString();

            Log.d("School", "Name: " + name);

            // Check school in Firestore
            db.collection("schools")
                    .whereEqualTo("verifiedStatus", true)
                    .whereEqualTo("name", name)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult().isEmpty()){
                                    Log.d(TAG, "School Not Verified ");
                                    TextView textMessage= findViewById(R.id.textMessage);
                                    textMessage.setText("Your school has not been verified. Please request registration if it is your first time.");
                                    Button registerButton = findViewById(R.id.register_button);
                                    registerButton.setVisibility(View.VISIBLE);
                                    registerButton.setOnClickListener(view -> {
                                        Intent intent = new Intent(SchoolNameActivity.this, SchoolRegistrationActivity.class);
                                        startActivity(intent);
                                    });
                                }
                                else{
                                    Log.d(TAG, "School Is Verified ");
                                    Intent intent = new Intent(SchoolNameActivity.this, SchoolLoginActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        });
    }
}



