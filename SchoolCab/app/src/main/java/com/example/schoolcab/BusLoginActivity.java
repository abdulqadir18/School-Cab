//package com.example.schoolcab;
//
//import static android.content.ContentValues.TAG;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.Map;
//
//public class BusLoginActivity extends AppCompatActivity {
//
//    private FirebaseFirestore db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bus_login);
//
//        db = FirebaseFirestore.getInstance();
//
//        Button loginButton = findViewById(R.id.login_button);
//        loginButton.setOnClickListener(v -> {
//            EditText idEditText = findViewById(R.id.bus_id);
//            EditText passwordEditText = findViewById(R.id.bus_password);
//
//            String id = idEditText.getText().toString();
//            String password = passwordEditText.getText().toString();
//
//            Log.d("BusLogin", "BusId: " + id + ", Password: " + password);
//            // Check school in Firestore
//            db.collection("bus")
//                    .whereEqualTo("busid", id)
//                    .whereEqualTo("password", password)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                            if (task.isSuccessful()) {
////                                Intent intent = new Intent(BusLoginActivity.this, BusDashboardActivity.class);
////                                for (QueryDocumentSnapshot document : task.getResult()) {
////                                    Log.d(TAG, document.getId() + " => " + document.getData());
////                                    Map<String,Object> m = document.getData();
////                                    intent.putExtra("busid", m.get("busid").toString());
////                                }
////
////                                startActivity(intent);
////                            } else {
////                                Log.d(TAG, "Error while login: ", task.getException());
////                            }
//                            if (task.isSuccessful()) {
//                                if (!task.getResult().isEmpty()) {
//                                    // Document found, proceed to BusDashboardActivity
//                                    Intent intent = new Intent(BusLoginActivity.this, BusDashboardActivity.class);
//                                    intent.putExtra("busid", id); // Pass bus ID to the next activity
//                                    startActivity(intent);
//                                } else {
//                                    // No matching document found, show error message
//                                    Log.d(TAG, "No matching document found for busid: " + id);
//                                    Toast.makeText(BusLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Log.d(TAG, "Error while login: ", task.getException());
//                                // Handle database errors
//                            }
//                        }
//                    });
//
//
//        });
//    }
//}
//
//
//


package com.example.schoolcab;

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
import java.util.Map;

public class BusLoginActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_login);

        db = FirebaseFirestore.getInstance();

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            EditText idEditText = findViewById(R.id.bus_id);
            EditText passwordEditText = findViewById(R.id.bus_password);

            String id = idEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            Log.d("BusLogin", "BusId: " + id + ", Password: " + password);

            // Check bus credentials in Firestore
            db.collection("bus")
                    .whereEqualTo("busid", id)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> busData = document.getData();
                                    String schoolId = busData.get("schoolid").toString();

                                    // Redirect to BusDashboardActivity with schoolId
                                    Intent intent = new Intent(BusLoginActivity.this, BusDashboard.class);
                                    intent.putExtra("busid", id);
                                    intent.putExtra("schoolid", schoolId);
                                    startActivity(intent);
                                    finish(); // Close the current activity
                                }
                            } else {
                                Log.d("BusLoginActivity", "Error while login: ", task.getException());
                                Toast.makeText(BusLoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
}

