package com.example.schoolcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UpdateCoordinator extends AppCompatActivity
{


    Button update;
    EditText name,lastname,schoolid,phoneNo;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_coordinator);
        db=FirebaseFirestore.getInstance();
        update=findViewById(R.id.save);
        name=findViewById(R.id.name);
        lastname=findViewById(R.id.lastName);
        phoneNo=findViewById(R.id.phoneNo);
        schoolid=findViewById(R.id.schoolid);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String newName=name.getText().toString();
                String newLastName=lastname.getText().toString();
                String newPhoneNo=phoneNo.getText().toString();
                String school_id=schoolid.getText().toString();

                name.setText("");
                lastname.setText("");
                phoneNo.setText("");
                Updatedata(newName,newLastName,newPhoneNo,school_id);

            }
        });

    }

    private void Updatedata(String newName, String newLastName, String newPhoneNo, String school_id)
    {
        CoOrdinator c = new CoOrdinator();
        c.setName(newName);
        c.setLastName(newLastName);
        c.setSchoolId(school_id);
        c.setphoneNo(newPhoneNo);

        CollectionReference target = db.collection("coordinators");
        target.whereEqualTo("schoolId",school_id).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        if (task.getResult().isEmpty())
                        {

                            Toast.makeText(UpdateCoordinator.this, "STUDENT NOT FOUND WITH GIVEN ENROLLMENT NO.", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {

                                String id=document.getId();
                                target.document(id).set(c);
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(UpdateCoordinator.this, "Error getting documents: ", Toast.LENGTH_LONG).show();
                    }
                }
            });


    }

}
