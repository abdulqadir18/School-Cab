package com.example.schoolcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.os.Bundle;

public class NotificationSend extends AppCompatActivity {

    EditText edtTitle;
    EditText edtMessage;
    private FirebaseFirestore db;


    static final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    static final private String SERVER_KEY = "key=" + "AAAAWltvcVc:APA91bG-vbaylaqUZfh-xZhrzfDvonkC040H4CuITC4bh11RNHVpDh_EQ88P3uBPd0NiO6_G2hZYaqIn85gpFcXFC4TFI0XrcOb1phAE4s7mQN0NO6gT3Ow2UlF_0nhaCfTcvQLaKn3p";
    final String TAG = "NOTIFICATION TAG";

    public static final String SHARED_PREFS = "shared_prefs";

    // key for schoolId
    public static final String sId = "sId";
    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String TOPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_send);

        //        Getting the school id saved in local preferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String schoolID = sharedpreferences.getString("sId", null);

        Toast.makeText(NotificationSend.this, "Hello  " + schoolID, Toast.LENGTH_LONG).show();

        edtTitle = findViewById(R.id.edtTitle);
        edtMessage = findViewById(R.id.edtDescription);
        Button btnSend = findViewById(R.id.edtSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                String title = edtTitle.getText().toString();
                String message = edtMessage.getText().toString();

                pushNotification(NotificationSend.this, schoolID, title, message);

            }
        });


    }


    private void pushNotification(Context context, String schoolId, String title, String message) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        TOPIC = "/topics/" + schoolId; //topic must match with what the receiver subscribed to

        System.out.println("FCM " + TOPIC);

        StrictMode.setThreadPolicy(policy);


        RequestQueue queue = Volley.newRequestQueue(context);

        try {
            JSONObject json = new JSONObject();
            JSONObject notifcationBody = new JSONObject();

//            json.put("to", "ciGACugXQ_SaXeaxveZmGw:APA91bE8P2FptQ31J3NchtSYxZwWEEjikWu7CA5mCNp6rGDllHJg4KlDBwjhYccIqGNU9uNh2PzirhkuKJCySo7ppZfSfbflt7kxnkl2MH7-0-mfqQ9X5UuVFoupg4zgtaEXp8L0QHLL");
            notifcationBody.put("title", title);
            notifcationBody.put("body", message);

            json.put("to", TOPIC);
            json.put("notification", notifcationBody);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FCM_API, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("FCM " + response);
                    Toast.makeText(NotificationSend.this, "Notification sent Succesfully", Toast.LENGTH_LONG).show();
                    edtTitle.setText("");
                    edtMessage.setText("");
                    saveToDatabase(NotificationSend.this, title, message, schoolId);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("FCM    " + error);
                    Toast.makeText(NotificationSend.this, "Error in sending notification", Toast.LENGTH_LONG).show();

                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "key=AAAAWltvcVc:APA91bG-vbaylaqUZfh-xZhrzfDvonkC040H4CuITC4bh11RNHVpDh_EQ88P3uBPd0NiO6_G2hZYaqIn85gpFcXFC4TFI0XrcOb1phAE4s7mQN0NO6gT3Ow2UlF_0nhaCfTcvQLaKn3p");

                    return params;

                }
            };
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            System.out.println("Json Error" + e);
            throw new RuntimeException(e);
        }

    }

    private void saveToDatabase(Context context, String title, String message, String schoolId) {

        db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("message", message);
        data.put("schoolId", schoolId);
        data.put("time", System.currentTimeMillis());

        db.collection("Notifications")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }

}