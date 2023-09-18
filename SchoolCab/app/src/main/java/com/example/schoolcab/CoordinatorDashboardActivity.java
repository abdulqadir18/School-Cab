package com.example.schoolcab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CoordinatorDashboardActivity extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_ordinator_dashboard);

        Button register = findViewById(R.id.register);
        Button update = findViewById(R.id.update);
        Button delete = findViewById(R.id.delete);

        register.setOnClickListener(v -> {
            Intent intent = new Intent(CoordinatorDashboardActivity.this, CoOrdinatorRegistration.class);
            startActivity(intent);
        });

        update.setOnClickListener(v -> {
            Intent intent = new Intent(CoordinatorDashboardActivity.this, UpdateCoordinator.class);
            startActivity(intent);
        });

        delete.setOnClickListener(v -> {
            Intent intent = new Intent(CoordinatorDashboardActivity.this, DeleteCoordinator.class);
            startActivity(intent);
        });
    }
}
