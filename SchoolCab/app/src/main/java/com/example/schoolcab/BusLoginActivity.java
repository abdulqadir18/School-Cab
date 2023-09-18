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

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class BusLoginActivity extends AppCompatActivity {
    public static final String sId = "sId";
    private String busId;
    SharedPreferences sharedpreferences;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            EditText idEditText = findViewById(R.id.bus_id);
            EditText passwordEditText = findViewById(R.id.bus_password);

            String id = idEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            Log.d("BusLogin", "BusId: " + id + ", Password: " + password);

            mAuth.signInWithEmailAndPassword(id, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                if(!user.getDisplayName().equals("bus") )
                                {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(BusLoginActivity.this, "Authentication failed."+user.getDisplayName(),
                                            Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    return ;
                                }
                                final String[] schoolID = new String[1];

                                Log.d("Bus Login","Uid: "+user.getUid());

                                DocumentReference docRef = db.collection("bus").document(user.getUid());


//                                HERE WE SUBSCRIBE TO THE SCHOOL GROUP BASED ON THE LOGIN OF THE USER
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {

                                                String jsonString = new Gson().toJson(document.getData());
                                                Gson gson = new Gson();
                                                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                                                Map<String, Object> jsonMap = gson.fromJson(jsonString, type);

                                                schoolID[0] = jsonMap.get("schoolid").toString();
                                                busId = jsonMap.get("busid").toString();
                                                Log.d(TAG, "DocumentSnapshot data: Schoolid " + schoolID[0]);
                                                Log.d(TAG, "DocumentSnapshot data: busid " + busId);

                                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                                editor.putString(sId, schoolID[0]);
                                                editor.putString("busId", user.getUid());

                                                // to save our data with key and value.
                                                editor.apply();

                                            } else {
                                                Log.d(TAG, "No such document");
                                            }

                                            Toast.makeText(BusLoginActivity.this, "Authentication Success.",
                                                    Toast.LENGTH_SHORT).show();

                                            Log.d(TAG, "Bus Login: Schoolid " + schoolID[0]);
                                            Log.d(TAG, "Bus Login: busid " + busId);
                                            Intent intent = new Intent(BusLoginActivity.this, BusDashboard.class);
                                            intent.putExtra("docRef", user.getUid());
                                            intent.putExtra("busid", busId);
                                            intent.putExtra("schoolid", schoolID[0]);

                                            startActivity(intent);

                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });






                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(BusLoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

            // Check bus credentials in Firestore
//            db.collection("bus")
//                    .whereEqualTo("busid", id)
//                    .whereEqualTo("password", password)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Map<String, Object> busData = document.getData();
//                                    String schoolId = busData.get("schoolid").toString();
//
//                                    // Redirect to BusDashboardActivity with schoolId
//
//                                    Intent intent = new Intent(BusLoginActivity.this, BusDashboard.class);
//
//                                    intent.putExtra("busid", id);
//                                    intent.putExtra("schoolid", schoolId);
//                                    startActivity(intent);
//                                    finish(); // Close the current activity
//                                }
//                            } else {
//                                Log.d("BusLoginActivity", "Error while login: ", task.getException());
//                                Toast.makeText(BusLoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
        });
    }
}

