package com.example.schoolcab;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Map;

public class SchoolLoginActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_login);

        db = FirebaseFirestore.getInstance();

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            EditText idEditText = findViewById(R.id.school_id);
            EditText passwordEditText = findViewById(R.id.school_password);

            String id = idEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            Log.d("SchoolLogin", "SchoolId: " + id + ", Password: " + password);
            // Check school in Firestore
            db.collection("schools")
                    .whereEqualTo("schoolId", id)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SchoolLoginActivity.this, SchoolDashboardActivity.class);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Map<String,Object> m = document.getData();
                                    intent.putExtra("schoolName", m.get("name").toString());
                                }

                                // Subscribe to topic
                                FirebaseMessaging.getInstance().subscribeToTopic("school_"+id)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                String msg = "Subscribed to school_"+id;
                                                if (!task.isSuccessful()) {
                                                    msg = "Subscribe failed";
                                                }
                                                Log.d(TAG, msg);
                                                Toast.makeText(SchoolLoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                startActivity(intent);
                            } else {
                                Log.d(TAG, "Error while login: ", task.getException());
                            }
                        }
                    });


        });
    }
}


