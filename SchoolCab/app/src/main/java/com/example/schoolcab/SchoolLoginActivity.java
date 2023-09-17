package com.example.schoolcab;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class SchoolLoginActivity extends AppCompatActivity {


    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing email.
    public static final String sId = "sId";

    // key for storing password.

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_login);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(v -> {
            Toast.makeText(SchoolLoginActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
                });

        TextView register = findViewById(R.id.register);
        register.setOnClickListener(v -> {
            Intent intent = new Intent(SchoolLoginActivity.this, SchoolRegistrationActivity.class);
            startActivity(intent);

        });

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            EditText idEditText = findViewById(R.id.school_id);
            EditText passwordEditText = findViewById(R.id.school_password);

            String id = idEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            Log.d("SchoolLogin", "SchoolId: " + id + ", Password: " + password);


            mAuth.signInWithEmailAndPassword(id, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                if(!user.getDisplayName().equals("school") )
                                {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SchoolLoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    return ;
                                }

                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                // below two lines will put values for
                                // email and password in shared preferences.
                                editor.putString(sId, user.getUid());

                                // to save our data with key and value.
                                editor.apply();


                                Intent intent = new Intent(SchoolLoginActivity.this, SchoolDashboardActivity.class);
                                startActivity(intent);


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SchoolLoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });




//            // Check school in Firestore
//            db.collection("schools")
//                    .whereEqualTo("schoolId", id)
//                    .whereEqualTo("password", password)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                Intent intent = new Intent(SchoolLoginActivity.this, SchoolDashboardActivity.class);
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                                    Map<String,Object> m = document.getData();
//                                    intent.putExtra("schoolName", m.get("name").toString());
//                                }
//
//                                startActivity(intent);
//                            } else {
//                                Log.d(TAG, "Error while login: ", task.getException());
//                            }
//                        }
//                    });


        });
    }
}


