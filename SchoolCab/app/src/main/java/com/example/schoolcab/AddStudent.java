package com.example.schoolcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;


public class AddStudent extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public static final String SHARED_PREFS = "shared_prefs";

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    private int page =1 ;
    String name ;
    String guardian  ;
    String email  ;
    String phoneNo ;
    String address  ;
    String  defaultAddress;

    @Override
    public void onBackPressed() {

       if(page== 2)
       {
           LinearLayout container1 = findViewById(R.id.container1);
           LinearLayout container2 = findViewById(R.id.container2);
           TextView heading = findViewById(R.id.heading);


           container1.setVisibility(View.VISIBLE);
           container2.setVisibility(View.INVISIBLE);
           page =1;
           heading.setText("Student Details \n         page 1");

       }
else{

      finish();
       }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        //        Getting the school id saved in local preferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String id = sharedpreferences.getString("email", null);
        String password = sharedpreferences.getString("password", null);

        page =1;



        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String ID = mAuth.getCurrentUser().getUid().toString();


        Button nextButton = findViewById(R.id.nextButton);
        Button registerButton = findViewById(R.id.register_button);
        TextView heading = findViewById(R.id.heading);

        nextButton.setOnClickListener(v -> {
            LinearLayout container1 = findViewById(R.id.container1);
            LinearLayout container2 = findViewById(R.id.container2);

            EditText edtName = findViewById(R.id.edtName);
            EditText edtGuardian = findViewById(R.id.edtGuardian);
            EditText edtEmail = findViewById(R.id.edtEmail);
            EditText edtPhoneNo = findViewById(R.id.edtPhoneNo);
            EditText edtAddress = findViewById(R.id.edtAddress);
            EditText edtDefaultAddress = findViewById(R.id.edtDefaultAddress);

            name = edtName.getText().toString();
             guardian = edtGuardian.getText().toString();
             email = edtEmail.getText().toString();
             phoneNo = edtPhoneNo.getText().toString();
             address = edtAddress.getText().toString();
             defaultAddress = edtDefaultAddress.getText().toString();

if(name.isEmpty()|| guardian.isEmpty() || email.isEmpty() || phoneNo.isEmpty() || address.isEmpty() || defaultAddress.isEmpty())
{
    Toast.makeText(AddStudent.this, "Some Field Missing . Please Check", Toast.LENGTH_SHORT).show();

    return;
}

            container1.setVisibility(View.INVISIBLE);
            container2.setVisibility(View.VISIBLE);
            heading.setText("Student Details \n         page 2");
            page =2;


                });





        registerButton.setOnClickListener(v -> {
            // Collect student details from EditText fields

            EditText edtRollNo = findViewById(R.id.edtRollNo);
            EditText edtClass = findViewById(R.id.edtClass);
            EditText edtSection = findViewById(R.id.edtSection);
            EditText edtSex = findViewById(R.id.edtSex);
            EditText edtAge = findViewById(R.id.edtAge);
            EditText edtWeight = findViewById(R.id.edtWeight);




            String rollNo = edtRollNo.getText().toString();
            String section  = edtSection.getText().toString();
            String sex = edtSex.getText().toString();
            String Age = edtAge.getText().toString();
            String Weight  = edtWeight.getText().toString();
            String Standard = edtClass.getText().toString();

            if(rollNo.isEmpty() || section.isEmpty() || Age.isEmpty() || sex.isEmpty() || Weight.isEmpty() || Standard.isEmpty())
            {
                Toast.makeText(AddStudent.this, "Some Field Missing . Please Check", Toast.LENGTH_SHORT).show();
                return ;
            }



            int age = Integer.parseInt(edtAge.getText().toString());
            int weight = Integer.parseInt(edtWeight.getText().toString());
            int standard = Integer.parseInt(edtClass.getText().toString());




            Log.d("StudentRegistration", "Name: " + name + ", Password: " + rollNo);

//            NewStudent student = new NewStudent(name, password);
            NewStudent student = new NewStudent();
            student.setName(name);
            student.setRollNo(rollNo);
            student.setGuardian(guardian);
            student.setPhoneNo(phoneNo);
            student.setAddress(address);
            student.setDefaultAddress(defaultAddress);
            student.setStandard(standard);
            student.setSection(section);
            student.setSex(sex);
            student.setAge(age);
            student.setWeight(weight);
            student.setEmail(email);
            student.setSchoolId(ID);


//            Ccreating a authenication user in firebase
            mAuth.createUserWithEmailAndPassword(email, "1234567890")
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // User signup successful
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getUid();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName("student")
                                        .build();

                                user.updateProfile(profileUpdates);

                                // Save additional user information to Firestore
                                DocumentReference userRef = db.collection("students").document(userId);

//                                Saving Additional information of user in fireStore with same id
                                userRef.set(student)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // User information saved to Firestore successfully
                                            Toast.makeText(AddStudent.this, "Student registered successfully!", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();

                                            mAuth.signInWithEmailAndPassword(id, password);
                                                    Intent intent = new Intent(AddStudent.this, StudentAddUpdatePage.class);
                                                    startActivity(intent);
                                                    finish();

                                                } else {
                                                    // Handle Firestore document creation failure
                                                    Toast.makeText(AddStudent.this, "Error saving user data to Firestore.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            } else {
                                // Handle signup failure
                                Toast.makeText(AddStudent.this, "Signup failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



        });



    }
}