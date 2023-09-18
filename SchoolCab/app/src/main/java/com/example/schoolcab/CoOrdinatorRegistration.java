package com.example.schoolcab;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CoOrdinatorRegistration extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_ordinator_registration);

        db = FirebaseFirestore.getInstance();
        SharedPreferences sharedpreferences=getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String school_id=sharedpreferences.getString("sId",NULL);

        Button registerButton = findViewById(R.id.co_register);
        registerButton.setOnClickListener(v -> {
            // Collect student details from EditText fields
            EditText nameEditText = findViewById(R.id.EditTextName);
            EditText lastNameEditText = findViewById(R.id.EditTextLastName);
            EditText coordinator=findViewById(R.id.EditTextSchoolId);
            EditText phoneNoEditText=findViewById(R.id.editTextPhoneNo);
            EditText passwordEditText=findViewById(R.id.password);

            String name = nameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String phoneNo=phoneNoEditText.getText().toString();
            String coordinator_id=coordinator.getText().toString();
            String password=passwordEditText.getText().toString();


//            Log.d("StudentRegistration", "Name: " + name + ", Password: " + password);

//            Student student = new Student(name, password);
            CoOrdinator c = new CoOrdinator();
            c.setName(name);
            c.setLastName(lastName);
            c.setSchoolId(school_id);
            c.setphoneNo(phoneNo);
            c.setCoordinatorId(coordinator_id);
            c.setPassword(password);



            // Add student to Firestore
            CollectionReference coordinatorCollection = db.collection("coordinators");

            coordinatorCollection.add(c)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Coordinator registered successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("coordinatorRegistration", "Error registering co-ordinator", e);
                        Toast.makeText(this, "Error registering co-ordinator", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}