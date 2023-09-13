package com.example.schoolcab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LastLocationRequest;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.concurrent.Executor;

public class GpsTempActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private FirebaseFirestore db;
    private TextView locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_temp);

        locationTextView = findViewById(R.id.locationTextView);
        db = FirebaseFirestore.getInstance();
        fusedLocationClient = new FusedLocationProviderClient() {
            @NonNull
            @Override
            public ApiKey<Api.ApiOptions.NoOptions> getApiKey() {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> flushLocations() {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> getCurrentLocation(int i, @Nullable CancellationToken cancellationToken) {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> getCurrentLocation(@NonNull CurrentLocationRequest currentLocationRequest, @Nullable CancellationToken cancellationToken) {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> getLastLocation() {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> getLastLocation(@NonNull LastLocationRequest lastLocationRequest) {
                return null;
            }

            @NonNull
            @Override
            public Task<LocationAvailability> getLocationAvailability() {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> removeLocationUpdates(@NonNull PendingIntent pendingIntent) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> removeLocationUpdates(@NonNull LocationCallback locationCallback) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> removeLocationUpdates(@NonNull LocationListener locationListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull PendingIntent pendingIntent) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull LocationCallback locationCallback, @Nullable Looper looper) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull LocationListener locationListener, @Nullable Looper looper) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull Executor executor, @NonNull LocationCallback locationCallback) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull Executor executor, @NonNull LocationListener locationListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> setMockLocation(@NonNull Location location) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> setMockMode(boolean b) {
                return null;
            }
        };

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateLocationInFirebase(location);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdateDelayMillis(100)
                .build();

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void updateLocationInFirebase(Location location) {
        // Get the bus ID (you should implement this)
        String tempid = "temp1";

        // Create a GeoPoint from the location
        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

        // Update the bus location in Firebase Firestore
        db.collection("temp").document(tempid)
                .update("location", geoPoint)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Location updated successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error updating location", e);
                });
    }
}
