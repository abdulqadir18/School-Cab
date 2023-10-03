package com.example.schoolcab;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

        @Override
    public void onStart() {
        super.onStart();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            String id = currentUser.getUid();

            if(currentUser.getDisplayName().equals("school") ) {
                Intent intent = new Intent(MainActivity.this, SchoolDashboardActivity.class);
                startActivity(intent);
            }
            else if(currentUser.getDisplayName().equals("student") ) {
                Intent intent = new Intent(MainActivity.this, StudentDashBoard.class);
                startActivity(intent);
            }
            else if(currentUser.getDisplayName().equals("coordinator") ) {
                Intent intent = new Intent(MainActivity.this, CoordinatorDashboard.class);
                startActivity(intent);
            }
            else if(currentUser.getDisplayName().equals("bus") ) {
                Intent intent = new Intent(MainActivity.this, BusDashboard.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        RelativeLayout schoolButton = findViewById(R.id.schoolButton);
        RelativeLayout studentButton = findViewById(R.id.studentButton);
        RelativeLayout busButton = findViewById(R.id.busButton);
        RelativeLayout tempButton = findViewById(R.id.tempButton);
      

        schoolButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SchoolLoginActivity.class);
            startActivity(intent);
        });

        studentButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StudentLogin.class);
            startActivity(intent);
        });


        busButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BusLoginActivity.class);
            startActivity(intent);
        });

        tempButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CoordinatorLoginActivity.class);
            startActivity(intent);
        });

    }
}


