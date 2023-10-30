//package com.example.schoolcab;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.google.auth.oauth2.GoogleCredentials;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import io.opencensus.resource.Resource;
//
//
//public class NotificationSendActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
//
//    final String TAG = "NOTIFICATION TAG";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notification_send);
//
//        //get the spinner from the xml.
//        Spinner dropdown = findViewById(R.id.spinner1);
//        //create a list of items for the spinner.
//        String[] items = new String[]{"1", "2", "three"};
//        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
//        //There are multiple variations of this, but this is the basic variant.
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        //set the spinners adapter to the previously created one.
//        dropdown.setAdapter(adapter);
//        dropdown.setOnItemSelectedListener(this);
//
//        Button sendButton = findViewById(R.id.notification_button);
//        sendButton.setOnClickListener(v -> {
//            String TOPIC = "school_dis123";
//            String NOTIFICATION_TITLE = "Notify";
//            String NOTIFICATION_MESSAGE = "First Notification";
//            JSONObject notification = new JSONObject();
//            JSONObject notificationBody = new JSONObject();
//            try {
//                notificationBody.put("title", NOTIFICATION_TITLE);
//                notificationBody.put("message", NOTIFICATION_MESSAGE);
//
//                notification.put("to", TOPIC);
//                notification.put("data", notificationBody);
//            }
//            catch ( JSONException e){
//                Log.e(TAG, "onCreate: "+ e.getMessage() );
//            }
//            sendNotification(notification);
//        });
//    }
//
//    private void sendNotification(JSONObject notification){
////        Firebase ref = new Firebase(FIREBASE_URL);
////        final Firebase notifications = ref.child("notificationRequests");
////        notifications.push().setValue(notification);
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }
//}
//
//
