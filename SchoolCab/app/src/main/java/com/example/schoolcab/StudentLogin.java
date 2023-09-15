package com.example.schoolcab;

import static android.content.ContentValues.TAG;

import static com.google.android.gms.tasks.Tasks.await;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class StudentLogin extends AppCompatActivity {


    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing email.
    public static final String sId = "sId";

    // key for storing password.

    // variable for shared preferences.
    SharedPreferences sharedpreferences;



    private FirebaseAuth mAuth;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

// getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);


        Button login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(v -> {
            // Collect student details from EditText fields
            EditText nameEditText = findViewById(R.id.edtEmail);
            EditText edtPassWord = findViewById(R.id.edtPassWord);



            String email = nameEditText.getText().toString();
            String password = edtPassWord.getText().toString();

            Log.d("StudentRegistration", "Name: " + email + ", Password: " + password);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                if(!user.getDisplayName().equals("student") )
                                {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(StudentLogin.this, "Authentication failed."+user.getDisplayName(),
                                            Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    return ;
                                }
                                final String[] schoolID = new String[1];

                                DocumentReference docRef = db.collection("students").document(user.getUid());


//                                HERE WE SUBSCRIBE TO THE SCHOOL GROUP BASED ON THE LOGIN OF THE USER
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {

                                                String jsonString = new Gson().toJson(document.getData());
                                                Gson gson = new Gson();
                                                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                                                Map<String, Object> jsonMap = gson.fromJson(jsonString, type);

                                                schoolID[0] = jsonMap.get("schoolId").toString();
                                                Log.d(TAG, "DocumentSnapshot data: Schoolid " + schoolID[0]);


                                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                                // below two lines will put values for
                                                // email and password in shared preferences.
                                                editor.putString(sId, schoolID[0]);

                                                // to save our data with key and value.
                                                editor.apply();
//                                              SUBSCRIBING TO THE TOPIC OF SCHOOL
                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public  void onComplete(@NonNull Task<String> task) {
                                        if(!task.isSuccessful())
                                        {
                                            System.out.println("Error in Token");
                                            return ;

                                        }
                                        String token = task.getResult();

                                        Intent intent = new Intent(StudentLogin.this, StudentDashBoard.class);
                                        intent.putExtra("schoolID" , schoolID[0]);
                                        startActivity(intent);

                                        String topic = schoolID[0];

                                        FirebaseMessaging.getInstance().subscribeToTopic(schoolID[0])
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @SuppressLint("RestrictedApi")
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        String msg = "Subscribed  to "+schoolID[0];
                                                        if (!task.isSuccessful()) {
                                                            msg = "Subscribe failed";
                                                        }
                                                        Log.d(FragmentManager.TAG, msg);
                                                        Toast.makeText(StudentLogin.this, msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });




                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });



                                Toast.makeText(StudentLogin.this, "Authentication Success.",
                                        Toast.LENGTH_SHORT).show();




                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(StudentLogin.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        });





        }
}