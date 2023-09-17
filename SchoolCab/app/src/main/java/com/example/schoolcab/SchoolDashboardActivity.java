package com.example.schoolcab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SchoolDashboardActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public static final String SHARED_PREFS = "shared_prefs";

    // key for schoolId
    public static final String sId = "sId";
    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(SchoolDashboardActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_dashboard);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        TextView schoolName = findViewById(R.id.school_name);
        schoolName.setText(getIntent().getStringExtra("schoolName"));

        Button busButton = findViewById(R.id.bus_button);
        Button busCoordinatorButton = findViewById(R.id.bus_coordinator_button);
        Button studentButton = findViewById(R.id.student_details_button);
        Button attendanceButton = findViewById(R.id.attendance_button);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button sendNotification = findViewById(R.id.sendNotification);


        busButton.setOnClickListener(v -> {
            Toast.makeText(SchoolDashboardActivity.this, "This Functionality is not yet been Added please wait for updated version", Toast.LENGTH_LONG).show();

        });

        busCoordinatorButton.setOnClickListener(v -> {
            Toast.makeText(SchoolDashboardActivity.this, "This Functionality is not yet been Added please wait for updated version", Toast.LENGTH_LONG).show();


        });

        studentButton.setOnClickListener(v -> {
            Intent intent = new Intent(SchoolDashboardActivity.this, StudentAddUpdatePage.class);
//            Sending id of the school
//            intent.putExtra("data", jsonString);
//            intent.putExtra("id" ,document.getId());
            startActivity(intent);

        });

       attendanceButton.setOnClickListener(v -> {
           Toast.makeText(SchoolDashboardActivity.this, "This Functionality is not yet been Added please wait for updated version", Toast.LENGTH_LONG).show();

        });

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            //            removing the schooldid from shared preferences on logout
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(sId);
            editor.apply();

            Intent intent = new Intent(SchoolDashboardActivity.this, MainActivity.class);
            startActivity(intent);
        });
        sendNotification.setOnClickListener(v -> {

            Intent intent = new Intent(SchoolDashboardActivity.this, NotificationSend.class);
            startActivity(intent);
        });

    }
}


