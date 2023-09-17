package com.example.schoolcab;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ViewPreviousNotifications extends AppCompatActivity {

    private FirebaseFirestore db;
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
            Intent intent = new Intent(ViewPreviousNotifications.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_previous_notifications);
        //        Getting the school id saved in local preferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String schoolID = sharedpreferences.getString("sId", null);
        db = FirebaseFirestore.getInstance();



        db.collection("Notifications")
                .whereEqualTo("schoolId", schoolID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "Samoil=> " + document.getData());
                            }
                            postExecute(task);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });





        }

public void postExecute(Task<QuerySnapshot> x ){

        List<JSONObject> notifications = null;
    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    ArrayList<NotificationModel> NotificationsList = new ArrayList<NotificationModel>();
    for (QueryDocumentSnapshot document : x.getResult()) {
//        Log.d("Post Execute", document.getId() + "Samoil=> " + document.getData());
        JSONObject jsonObject = new JSONObject(document.getData());
        NotificationModel notification = new NotificationModel();

        try {
          Timestamp timestamp =   new Timestamp( Long.parseLong(jsonObject.getString("time"))/1000 , 0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy hh:mm a");
            System.out.println(timestamp);
            String date = dateFormat.format(timestamp.toDate());


            notification.setTitle( jsonObject.getString("title"));
            notification.setMessage( jsonObject.getString("message"));
            notification.setTime(date);
            NotificationsList.add(notification);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    Collections.reverse(NotificationsList);
    NotificationAdapter courseAdapter = new NotificationAdapter( this , NotificationsList);

    // below line is for setting a layout manager for our recycler view.
    // here we are creating vertical list so we will provide orientation as vertical
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    // in below two lines we are setting layoutmanager and adapter to our recycler view.
    recyclerView.setLayoutManager(linearLayoutManager);

    System.out.println("Hello Samoil YOu Have " + NotificationsList.size());

    recyclerView.setAdapter(courseAdapter);




}

    }