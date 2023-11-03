package com.example.schoolcab;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;


public class StudentDashBoard extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";

    // key for schoolId
    public static final String sId = "sId";

    private String jsonString;
    private FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();


//        Getting the school id saved in local preferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String schoolID = sharedpreferences.getString("sId", null);

        RelativeLayout logoutButton = findViewById(R.id.logout_button);
        RelativeLayout viewNotifications = findViewById(R.id.viewNotifications);
        RelativeLayout trackBus = findViewById(R.id.busTrackingStudent);

        getRoute("Ed8b6yjYDIQYgPxUCFFfHwgeEkw2");

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
            intent.putExtra("data", jsonString);
            startActivity(intent);
        });

    }

    public void getRoute(String busId)
    {
        Log.d(TAG, "starting to get Route ");
        CollectionReference busCollection = db.collection("bus");
        busCollection.document(busId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // The document exists, you can access its data
                                Log.d(TAG, String.format(document.getId() + " onComplete: Bus Data " + document.getData().get("Route").getClass().getName()));

                                // If you want to convert the data to JSON
                                jsonString = new Gson().toJson(document.getData());

                            } else {
                                // The document does not exist
                                Log.d(TAG, "Document does not exist.");
                                Toast.makeText(StudentDashBoard.this, "Document not found", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.d(TAG, "Error getting document: " + task.getException());
                        }
                    }
                });

    }

}