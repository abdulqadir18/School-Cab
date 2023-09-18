//package com.example.schoolcab;
//
//import static android.content.ContentValues.TAG;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.database.annotations.Nullable;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.GeoPoint;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import android.Manifest;
//
//
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
//import org.osmdroid.views.MapView;
//import org.osmdroid.views.overlay.Marker;
//import org.osmdroid.views.overlay.Polyline;
//
//import java.util.Map;
//
//public class BusDashboardActivity extends AppCompatActivity {
//
//    private FirebaseFirestore db;
//    private String targetBusId;
//    private String targetSchoolId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // Set the layout
//        setContentView(R.layout.activity_bus_dashboard);
//        // Set the user agent for osmdroid
//        org.osmdroid.config.Configuration.getInstance().setUserAgentValue("com.example.schoolcab");
//        // Get firebase instance
//        db = FirebaseFirestore.getInstance();
//
//        // Get the busid and schoolid from the Intent
//        Intent intent = getIntent();
//        targetBusId = intent.getStringExtra("busid");
//        Log.d("Bus ID", targetBusId);
//        targetSchoolId = intent.getStringExtra("schoolid");
//        Log.d("School ID", targetSchoolId);
//
////        --------------------------------GPS
//        // Initialize the FusedLocationProviderClient in your activity or service.
//        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        // Request location updates.
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            Log.d(TAG, "Permission Not Granted");
//            checkLocationPermission();
//        }
//
//        // Location Request
//        LocationRequest locationRequest = LocationRequest.create()
//                .setInterval(10000)  // Update interval in milliseconds (e.g., every 10 seconds).
//                .setFastestInterval(5000)  // Fastest update interval.
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);  // High accuracy for bus tracking.
//
//        // Location Callback
//        LocationCallback locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//                Location location = locationResult.getLastLocation();
//
//                // Send the updated location to Firebase.
//                updateFirebaseLocation(location.getLatitude(), location.getLongitude());
//            }
//        };
//
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//
////        ------------------------------------MAP
//        MapView mapView = findViewById(R.id.map);
//        mapView.setTileSource(TileSourceFactory.MAPNIK); // You can change the tile source as needed.
////        mapView.setBuiltInZoomControls(true);
//        mapView.setMultiTouchControls(true);
//
//
//
//        db.collection("bus")
//                .whereEqualTo("busid", targetBusId)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            // Handle database errors.
//                            return;
//                        }
//
//                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
//                            // Retrieve and update the bus location on the map.
//                            // You can access the latitude and longitude from the doc.
//                            Double latitude = doc.getDouble("latitude");
//                            Double longitude = doc.getDouble("longitude");
//
//                            // Assuming you have a GeoPoint from Firestore
//                            com.google.firebase.firestore.GeoPoint firebaseGeoPoint = doc.getGeoPoint("location");
//                            Log.d("firebaseGeoPoint", String.valueOf(firebaseGeoPoint));
//
//                            if (firebaseGeoPoint != null) {
//                                // Convert Firebase GeoPoint to osmdroid GeoPoint
//                                org.osmdroid.util.GeoPoint osmGeoPoint = new org.osmdroid.util.GeoPoint(firebaseGeoPoint.getLatitude(), firebaseGeoPoint.getLongitude());
//                                Log.d("osmGeoPoint", String.valueOf(osmGeoPoint));
//                                // Add a marker for the bus
//                                Marker busMarker = new Marker(mapView);
//                                busMarker.setPosition(osmGeoPoint);
//                                busMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//                                mapView.getOverlays().add(busMarker);
//
//                                // Create a Polyline for a route.
//                                Polyline line = new Polyline();
////                                line.setPoints(routePoints); // List of GeoPoints representing the route.
//                                mapView.getOverlayManager().add(line);
//
//
//                                // Optionally, you can update other properties of the marker here.
//                            }
//
//
//                            if (latitude != null && longitude != null) {
//                                // Update the map with the new location data.
//                            }
//                        }
//                    }
//                });
//
////        // Add a marker for a bus.
////        Marker busMarker = new Marker(mapView);
////        busMarker.setPosition(new GeoPoint(latitude, longitude));
////        busMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
////        mapView.getOverlays().add(busMarker);
//
//        db.collection("schools")
//                .whereEqualTo("schoolId", targetSchoolId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Map<String, Object> busData = document.getData();
//                                String schoolId = busData.get("schoolId").toString();
//                                // Assuming you have a GeoPoint from Firestore
//                                com.google.firebase.firestore.GeoPoint firebaseGeoPointSchool = document.getGeoPoint("location");
//                                Log.d("firebaseGeoPointSchool", String.valueOf(firebaseGeoPointSchool));
//                                org.osmdroid.util.GeoPoint osmGeoPointSchool = new org.osmdroid.util.GeoPoint(firebaseGeoPointSchool.getLatitude(), firebaseGeoPointSchool.getLongitude());
//                                Log.d("osmGeoPointSchool", String.valueOf(osmGeoPointSchool));
//                                // Add a marker for the bus
//                                Marker schoolMarker = new Marker(mapView);
//                                schoolMarker.setPosition(osmGeoPointSchool);
//                                schoolMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//                                mapView.getOverlays().add(schoolMarker);
//                                mapView.invalidate(); // Refresh the map
//                            }
//                        } else {
//
//                        }
//                    }
//                });
//
//    }
//
//    private void updateFirebaseLocation(double latitude, double longitude) {
//
//        Log.d("Geopoint", "Latitude: " + latitude + ", Longitude: " + longitude);
//        // Query for the document with the matching busid
//        db.collection("bus")
//                .whereEqualTo("busid", targetBusId)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if (!queryDocumentSnapshots.isEmpty()) {
//                        // There should be only one matching document, so use .get(0)
//                        DocumentReference docRef = queryDocumentSnapshots.getDocuments().get(0).getReference();
//
//                        // Create a new GeoPoint with your desired coordinates
//                        GeoPoint newLocation = new GeoPoint(latitude, longitude);
//
//                        // Update the location field of the document
//                        docRef.update("location", newLocation)
//                                .addOnSuccessListener(aVoid -> {
//                                    // Location updated successfully
//                                    System.out.println("Location updated successfully.");
//                                    Toast.makeText(this, "Location updated successfully.", Toast.LENGTH_SHORT).show();
//                                })
//                                .addOnFailureListener(e -> {
//                                    // Handle the error
//                                    System.err.println("Error updating location: " + e.getMessage());
//                                    Toast.makeText(this, "Location update failed.", Toast.LENGTH_SHORT).show();
//                                });
//                    } else {
//                        System.out.println("No matching document found.");
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    // Handle any errors that may occur while querying
//                    System.err.println("Error querying for document: " + e.getMessage());
//                });
//
//
//
//    }
//
//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//
//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//                new AlertDialog.Builder(this)
//                        .setTitle("Permission Request")
//                        .setMessage("This app needs location access")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //Prompt the user once explanation has been shown
//                                ActivityCompat.requestPermissions(BusDashboardActivity.this,
//                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                        MY_PERMISSIONS_REQUEST_LOCATION);
//                            }
//                        })
//                        .create()
//                        .show();
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                        //Request location updates:
////                        locationManager.requestLocationUpdates(provider, 400, 1, this);
//                        // Initialize the FusedLocationProviderClient in your activity or service.
//                        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//                        // Create a location request.
//                        LocationRequest locationRequest = LocationRequest.create()
//                                .setInterval(10000)  // Update interval in milliseconds (e.g., every 10 seconds).
//                                .setFastestInterval(5000)  // Fastest update interval.
//                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);  // High accuracy for bus tracking.
//
//                        LocationCallback locationCallback = new LocationCallback() {
//                            @Override
//                            public void onLocationResult(LocationResult locationResult) {
//                                if (locationResult == null) {
//                                    return;
//                                }
//                                Location location = locationResult.getLastLocation();
//
//                                // Send the updated location to Firebase.
//                                updateFirebaseLocation(location.getLatitude(), location.getLongitude());
//                            }
//                        };
//
//                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//
//                    }
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//
//                }
//                return;
//            }
//
//        }
//    }
//}


package com.example.schoolcab;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.renderscript.RenderScript;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.Manifest;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class BusDashboardActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String targetBusId;
    private String targetSchoolId;
    private MapView mapView;
    private Marker busMarker;
    private Polyline routePolyline;
    private List<GeoPoint> busRouteGeoPoints = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_dashboard);

        // Initialize the user agent for osmdroid
        Configuration.getInstance().setUserAgentValue("com.example.schoolcab");

        db = FirebaseFirestore.getInstance();

        // Get the busid from the Intent
        Intent intent = getIntent();
        targetBusId = intent.getStringExtra("busid");
        Log.d("Bus Tracking","Bus ID"+ targetBusId);
        targetSchoolId = intent.getStringExtra("schoolid");
        Log.d("Bus Tracking","School ID"+ targetSchoolId);

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Log.d("gps location", "no");
            buildAlertMessageNoGps();
        } else {
            Log.d("gps location", "yes");
        }

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
                Log.d("In Location Callback", "No");
                if (locationResult == null) {
                    Log.d("Location Found", "No");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d("Location Found", String.valueOf(location));
                    updateLocationInFirebase(location);
                }
            }
        };

        // Initialize the MapView
        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Fetch and display the bus and school locations
        fetchBusLocation();
        fetchSchoolLocation();
        fetchBusStops(); // Fetch the bus stops

        // Initialize the route Polyline
        routePolyline = new Polyline();
        routePolyline.setColor(Color.BLUE); // Set the color of the route line
        routePolyline.setWidth(5f); // Set the width of the route line
        mapView.getOverlayManager().add(routePolyline);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("Location Granted", "Yes");
            startLocationUpdates();
        } else {
            Log.d("Location Granted", "No");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Location Granted 2", "Yes");
                startLocationUpdates();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        Log.d("Start Location", "Yes");
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdateDelayMillis(100)
                .build();
        Log.d("Start Fused Location", "Yes");
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        Log.d("End Fused Location", "Yes");
    }



    private void fetchBusStops() {
        // Fetch the array of bus stops from your Firestore collection
        db.collection("bus")
                .document(targetBusId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Assuming you have a field called "busStops" that contains an array of GeoPoints
                            List<GeoPoint> busStops = (List<GeoPoint>) document.get("busStops");
                            if (busStops != null && !busStops.isEmpty()) {
                                // Add the bus stops to the routePolyline
                                for (GeoPoint stop : busStops) {
                                    Log.d("Bus Stops", String.valueOf(stop));
                                    routePolyline.addPoint(new org.osmdroid.util.GeoPoint(stop.getLatitude(), stop.getLongitude()));
                                }
                            }
                        }
                    } else {
                        // Handle the error
                    }
                });
    }

    private void fetchBusLocation() {
        db.collection("bus")
                .whereEqualTo("busid", targetBusId)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        // Handle database errors.
                        return;
                    }

                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        // Retrieve and update the bus location on the map.
                        GeoPoint firebaseGeoPoint = doc.getGeoPoint("location");
                        Log.d("Bus Location", String.valueOf(firebaseGeoPoint));
                        if (firebaseGeoPoint != null) {
                            // Convert Firebase GeoPoint to osmdroid GeoPoint
                            org.osmdroid.util.GeoPoint osmGeoPoint = new org.osmdroid.util.GeoPoint(firebaseGeoPoint.getLatitude(), firebaseGeoPoint.getLongitude());

                            if (busMarker == null) {
                                // Add a marker for the bus
                                busMarker = new Marker(mapView);
                                busMarker.setPosition(osmGeoPoint);
                                busMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                mapView.getOverlays().add(busMarker);
                            } else {
                                // Update the bus marker position
                                busMarker.setPosition(osmGeoPoint);
                            }

                            // Set the initial zoom level to the bus's location
                            if (busMarker != null) {
                                mapView.getController().setZoom(15); // Adjust the zoom level as needed
                                mapView.getController().setCenter(busMarker.getPosition());
                            }

                            // Update the routePolyline
                            routePolyline.addPoint(osmGeoPoint);
                            mapView.invalidate(); // Refresh the map
                        }
                    }
                });
    }

    private void fetchSchoolLocation() {

        DocumentReference userRef = db.collection("schools").document(targetSchoolId);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("Documnet Snapshot: ", String.valueOf(document));
                    if (document.exists()) {

                        GeoPoint firebaseGeoPointSchool = document.getGeoPoint("location");
                        Log.d("School Location", String.valueOf(firebaseGeoPointSchool));

                        if (firebaseGeoPointSchool != null) {
                            // Convert Firebase GeoPoint to osmdroid GeoPoint
                            org.osmdroid.util.GeoPoint osmGeoPointSchool = new org.osmdroid.util.GeoPoint(firebaseGeoPointSchool.getLatitude(), firebaseGeoPointSchool.getLongitude());

                            // Add a marker for the school
                            Marker schoolMarker = new Marker(mapView);
                            schoolMarker.setPosition(osmGeoPointSchool);
                            schoolMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                            mapView.getOverlays().add(schoolMarker);

                            mapView.invalidate(); // Refresh the map
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void updateLocationInFirebase(Location location) {
        // Query for the document with the matching busid
        Log.d("Update Location", "Yes");
        db.collection("bus")
                .whereEqualTo("busid", targetBusId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // There should be only one matching document, so use .get(0)
                        DocumentReference docRef = queryDocumentSnapshots.getDocuments().get(0).getReference();

                        // Create a new GeoPoint with your desired coordinates
                        GeoPoint newLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

                        // Update the location field of the document
                        docRef.update("location", newLocation)
                                .addOnSuccessListener(aVoid -> {
                                    // Location updated successfully
                                    Log.d("Bus Location", "Location updated successfully.");
                                })
                                .addOnFailureListener(e -> {
                                    // Handle the error
                                    Log.e("Bus Location", "Error updating location: " + e.getMessage());
                                });
                    } else {
                        Log.d("Bus Location", "No matching document found.");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that may occur while querying
                    Log.e("Bus Location", "Error querying for document: " + e.getMessage());
                });
    }

//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted, so request it.
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                Log.d("Location Access", "no");
//                new AlertDialog.Builder(this)
//                        .setTitle("Permission Request")
//                        .setMessage("This app needs location access")
//                        .setPositiveButton("OK", (dialogInterface, i) -> {
//                            ActivityCompat.requestPermissions(BusDashboardActivity.this,
//                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                    MY_PERMISSIONS_REQUEST_LOCATION);
//                        })
//                        .create()
//                        .show();
//            } else {
//                Log.d("Location Access", "yes");
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false; // Permission is not yet granted
//        } else {
//            // Permission is already granted
//            return true;
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission granted, you can now request location updates
//                    Log.d("Request Location", "location requested");
//                    startLocationUpdates();
//                } else {
//                    // Handle permission denied
//                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//        }
//    }

//    private void startLocationUpdates() {
//        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        LocationRequest locationRequest = LocationRequest.create()
//                .setInterval(10)
//                .setFastestInterval(5)
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        LocationCallback locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//                Location location = locationResult.getLastLocation();
//
//                // Send the updated location to Firebase.
//                Log.d("Update Location", "location updated");
//                updateFirebaseLocation(location.getLatitude(), location.getLongitude());
//            }
//        };
//
//        if(checkLocationPermission()){
//            Log.d("Start Location update", "startLocationUpdates: ");
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//        }
//    }


}


