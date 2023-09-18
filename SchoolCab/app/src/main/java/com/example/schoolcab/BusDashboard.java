package com.example.schoolcab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class BusDashboard extends AppCompatActivity {

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
            Intent intent = new Intent(BusDashboard.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_dashboard2);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

//        Getting the school id saved in local preferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String schoolID = sharedpreferences.getString("sId", null);

        RelativeLayout attendance = findViewById(R.id.attendance_button);
        RelativeLayout busTracking = findViewById(R.id.bus_button);
        RelativeLayout logoutButton = findViewById(R.id.logout_button);
        RelativeLayout notification = findViewById(R.id.notification_button);

        attendance.setOnClickListener(v -> {
            Intent intent = new Intent(BusDashboard.this, AttendanceAddActivity.class);
            startActivity(intent);

        });
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();

//            removing the schooldid from shared preferences on logout
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(sId);
            editor.remove("busId");
            editor.apply();

            Intent intent = new Intent(BusDashboard.this, MainActivity.class);
            startActivity(intent);
        });

        busTracking.setOnClickListener(v -> {
            String busId = getIntent().getStringExtra("busid");
            String schoolId = getIntent().getStringExtra("schoolid");

            Intent intent = new Intent(BusDashboard.this, BusDashboardActivity.class);
            Log.d("Bus Dashboard","Bus ID"+ busId);
            Log.d("Bus Dashboard","School ID"+ schoolId);
            intent.putExtra("busid", busId);
            intent.putExtra("schoolid", schoolId);
            startActivity(intent);
        });
        notification.setOnClickListener(v -> {
            Intent intent = new Intent(BusDashboard.this, NotificationSend.class);

            startActivity(intent);
        });


    }
}