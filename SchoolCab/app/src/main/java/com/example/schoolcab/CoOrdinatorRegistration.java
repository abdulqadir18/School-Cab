package com.example.schoolcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CoOrdinatorRegistration extends AppCompatActivity {

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_ordinator_registration);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String ID = mAuth.getCurrentUser().getUid().toString();

        Button registerButton = findViewById(R.id.co_register);
        registerButton.setOnClickListener(v -> {
            // Collect coordinator details from EditText fields
            EditText nameEditText = findViewById(R.id.EditTextName);
            EditText lastNameEditText = findViewById(R.id.EditTextLastName);
            EditText ageEditText=findViewById(R.id.EditTextSchoolId);
            EditText phoneNoEditText=findViewById(R.id.editTextPhoneNo);

            String name = nameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String schoolId= ageEditText.getText().toString();
            String phoneNo=phoneNoEditText.getText().toString();

            CoOrdinator c = new CoOrdinator();
            c.setName(name);
            c.setLastName(lastName);
            c.setSchoolId(schoolId);
            c.setphoneNo(phoneNo);



            //            Creating authentication for user in firebase
            mAuth.createUserWithEmailAndPassword("kriti@gmail.com", "1234567890")
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // User signup successful
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getUid();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName("coordinator")
                                        .build();

                                user.updateProfile(profileUpdates);

                                // Save additional user information to Firestore
                                DocumentReference userRef = db.collection("coordinators").document(userId);

//                                Saving Additional information of user in fireStore with same id
                                userRef.set(c)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // User information saved to Firestore successfully
                                                    Toast.makeText(CoOrdinatorRegistration.this, "Coordinator registered successfully!", Toast.LENGTH_SHORT).show();
                                                    mAuth.signOut();
                                                } else {
                                                    // Handle Firestore document creation failure
                                                    Toast.makeText(CoOrdinatorRegistration.this, "Error saving user data to Firestore.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            } else {
                                // Handle signup failure
                                Toast.makeText(CoOrdinatorRegistration.this, "Signup failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



            // Add student to Firestore
//            CollectionReference coordinatorCollection = db.collection("coordinators");
//
//            coordinatorCollection.add(c)
//                    .addOnSuccessListener(documentReference -> {
//                        Toast.makeText(this, "Coordinator registered successfully!", Toast.LENGTH_SHORT).show();
//                    })
//                    .addOnFailureListener(e -> {
//                        Log.e("coordinatorRegistration", "Error registering co-ordinator", e);
//                        Toast.makeText(this, "Error registering co-ordinator", Toast.LENGTH_SHORT).show();
//                    });
        });
    }
}