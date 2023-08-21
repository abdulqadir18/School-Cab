package com.example.schoolcab;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);


        mAuth = FirebaseAuth.getInstance();

        Button login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(v -> {
            // Collect student details from EditText fields
            EditText nameEditText = findViewById(R.id.edtEmail);
            EditText edtPassWord = findViewById(R.id.edtPassWord);

            String email = nameEditText.getText().toString();
            String password = edtPassWord.getText().toString();

            Log.d("StudentRegistration", "Name: " + email + ", Password: " + password);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Toast.makeText(StudentLogin.this, "Authentication Success.",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(StudentLogin.this, StudentDashBoard.class);
                                startActivity(intent);


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(StudentLogin.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        });





        }
}