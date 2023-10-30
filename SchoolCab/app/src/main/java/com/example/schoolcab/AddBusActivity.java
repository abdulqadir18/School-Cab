package com.example.schoolcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddBusActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    SharedPreferences sharedPreferences;
    String schoolId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String ID = mAuth.getCurrentUser().getUid().toString();

        sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        schoolId = sharedPreferences.getString("sId", null);


        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            // Collect bus details from EditText fields

            EditText edtNumber = findViewById(R.id.edtBusNo);
            EditText edtId = findViewById(R.id.edtBusId);
            EditText edtCapacity = findViewById(R.id.edtCapacity);
            EditText edtUserId = findViewById(R.id.edtUserId);

            String number = edtNumber.getText().toString();
            String id = edtId.getText().toString();
            String capacity = edtCapacity.getText().toString();
            String userId = edtUserId.getText().toString();

            Bus bus = new Bus();
            bus.setBusNo(Integer.parseInt(number));
            bus.setBusId(id);
            bus.setBusCapacity(Integer.parseInt(capacity));
            bus.setBusUserId(userId);
            bus.setPassword("1234567890");
            bus.setSchoolId(schoolId);


//            Ccreating authentication for user in firebase
            mAuth.createUserWithEmailAndPassword(bus.getBusUserId(), "1234567890")
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // User signup successful
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getUid();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName("bus")
                                        .build();

                                user.updateProfile(profileUpdates);

                                // Save additional user information to Firestore
                                DocumentReference userRef = db.collection("bus").document(userId);

//                                Saving Additional information of user in fireStore with same id
                                userRef.set(bus)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // User information saved to Firestore successfully
                                                    Toast.makeText(AddBusActivity.this, "Bus registered successfully!", Toast.LENGTH_SHORT).show();
                                                    mAuth.signOut();
                                                } else {
                                                    // Handle Firestore document creation failure
                                                    Toast.makeText(AddBusActivity.this, "Error saving bus data to Firestore.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            } else {
                                // Handle signup failure
                                Toast.makeText(AddBusActivity.this, "Signup failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        });




    }
}
