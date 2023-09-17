package com.example.schoolcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();


        EditText edtEmail = findViewById(R.id.edtEmail);


        Button forgetButton = findViewById(R.id.forgetButton);
        forgetButton.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();

            if(email.isEmpty()){
                edtEmail.setError("Required");
            }else{

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ForgotPassword.this, "Check Your Mail",
                                            Toast.LENGTH_SHORT).show();
                                    edtEmail.setText("");

                                }
                                else{
                                    Toast.makeText(ForgotPassword.this, "Error In Sending Mail "+task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        });







            }


        });

    }
}