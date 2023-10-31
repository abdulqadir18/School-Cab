package com.example.schoolcab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;


public class StudentDashBoard extends AppCompatActivity {

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
                Intent intent = new Intent(StudentDashBoard.this, MainActivity.class);
                startActivity(intent);
        }
    }

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dash_board);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

//        Getting the school id saved in local preferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String schoolID = sharedpreferences.getString("sId", null);

        RelativeLayout logoutButton = findViewById(R.id.logout_button);
        RelativeLayout viewNotifications = findViewById(R.id.viewNotifications);
        RelativeLayout trackBus = findViewById(R.id.busTrackingStudent);


        logoutButton.setOnClickListener(v -> {


            mAuth.signOut();

            FirebaseMessaging.getInstance().unsubscribeFromTopic(schoolID);
            FirebaseMessaging.getInstance().unsubscribeFromTopic("userABC");

//            removing the schooldid from shared preferences on logout
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(sId);
            editor.apply();

            Intent intent = new Intent(StudentDashBoard.this, MainActivity.class);
            startActivity(intent);
        });

        viewNotifications.setOnClickListener(v -> {

            Intent intent = new Intent(StudentDashBoard.this, ViewPreviousNotifications.class);
            startActivity(intent);
        });

        trackBus.setOnClickListener(v -> {

            Intent intent = new Intent(StudentDashBoard.this, ParentsMaps.class);
            startActivity(intent);
        });

    }


}