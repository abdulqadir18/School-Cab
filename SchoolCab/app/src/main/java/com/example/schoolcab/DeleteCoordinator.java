package com.example.schoolcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

public class DeleteCoordinator extends AppCompatActivity
{
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_coordinator);
        db = FirebaseFirestore.getInstance();
        Button btn = findViewById(R.id.delete_coordinator);
        EditText school_id = findViewById(R.id.id);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String school_Id = school_id.getText().toString();
                Deletedata(school_Id);

            }

        });
    }

    private void Deletedata(String school_Id)
    {
        CollectionReference target = db.collection("coordinators");
        target.whereEqualTo("schoolId",school_Id).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            if (task.getResult().isEmpty())
                            {

                                Toast.makeText(DeleteCoordinator.this, "COORDINATOR NOT FOUND WITH GIVEN SCHOOL ID NO.", Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {

                                    String id=document.getId();
                                    target.document(id).delete();
                                }
                                Intent intent = new Intent(DeleteCoordinator.this, CoordinatorDashboardActivity.class);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(DeleteCoordinator.this, "Error getting documents: ", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

}