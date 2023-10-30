package com.example.schoolcab;

import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

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


        RelativeLayout busButton = findViewById(R.id.bus_button);
        RelativeLayout busCoordinatorButton = findViewById(R.id.bus_coordinator_button);
        RelativeLayout studentButton = findViewById(R.id.student_details_button);
        RelativeLayout attendanceButton = findViewById(R.id.attendance_button);
        RelativeLayout logoutButton = findViewById(R.id.logoutButton);
        RelativeLayout sendNotification = findViewById(R.id.sendNotification);


        busButton.setOnClickListener(v -> {
            Toast.makeText(SchoolDashboardActivity.this, "This Functionality is not yet been Added please wait for updated version", Toast.LENGTH_LONG).show();

        });

        busCoordinatorButton.setOnClickListener(v -> {
            Intent intent = new Intent(SchoolDashboardActivity.this, CoordinatorDashboardActivity.class);
            startActivity(intent);
        });

        studentButton.setOnClickListener(v -> {
            Intent intent = new Intent(SchoolDashboardActivity.this, StudentAddUpdatePage.class);
//            Sending id of the school
//            intent.putExtra("data", jsonString);
//            intent.putExtra("id" ,document.getId());
            startActivity(intent);

        });

       attendanceButton.setOnClickListener(v -> {
           Log.d("School Dashboard", "Call Report Activity");
//           Toast.makeText(SchoolDashboardActivity.this, "Attendance Report Clicked", Toast.LENGTH_LONG).show();
           Intent intent = new Intent(SchoolDashboardActivity.this, ReportActivity.class);
           startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            //            removing the schooldid from shared preferences on logout
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(sId);
            editor.remove("email");
            editor.remove("password");
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


